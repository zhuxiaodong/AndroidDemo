package com.example.launchappfromwebpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class MainActivity extends Activity
{

    static private final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Log.d(TAG, intent.toString());

        Bundle bundle = intent.getExtras();
        if (null != bundle)
        {
            Log.d(TAG, bundle.toString());
            for (String key : bundle.keySet())
            {
                Log.d(TAG, key);
            }
        }

        String data = intent.getDataString();
        if (null != data)
        {
            Log.d(TAG, "data " + data);
            setContentView(R.layout.activity_main);
        } else
        {
            loadWeb();
        }
    }

    private void loadWeb()
    {
        WebView webview = new WebView(this);
        setContentView(webview);

        String summary = "<META content=\"text/html;charset=gb2312\" http-equiv=\"Content-Type\">"
                + "<BODY>This is an internal web page! <br><br><br>"
                + "<A href=\"http://www.kaixin001.com\">Ushi</A><br><br><br>"
                + "<A href=\"dlink://news.baidu.com/index.html\"> dlink:// </A><br><br><br>"
                + "<A href=\"http://www.95599.cn/cn/FinancialService/FinancialManagement/FinancialProducts/BenLiFeng/Announcement/201007/W020100708613892155982.pdf\">  mimeType=application/pdf</A>"
                + "</BODY></HTML>";
        webview.loadData(summary, "text/html", null);
    }
}
