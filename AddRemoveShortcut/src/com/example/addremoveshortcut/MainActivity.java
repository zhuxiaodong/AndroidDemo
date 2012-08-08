package com.example.addremoveshortcut;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity
{
    private static final String CREATE_SHORTCUT_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final String DROP_SHORTCUT_ACTION = "com.android.launcher.action.UNINSTALL_SHORTCUT";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.b_add:
                setUpShortCut();
                break;
            case R.id.b_remove:
                tearDownShortCut();
                break;
        }

    }

    private void setUpShortCut()
    {

        Intent intent = new Intent(CREATE_SHORTCUT_ACTION);

        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));

        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "sina");

        intent.putExtra("duplicate", false);

        Intent targetIntent = new Intent();
        targetIntent.setAction(Intent.ACTION_MAIN);
        targetIntent.addCategory("android.intent.category.LAUNCHER");

        ComponentName componentName = new ComponentName(getPackageName(), this.getClass().getName());
        targetIntent.setComponent(componentName);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);

        sendBroadcast(intent);

    }

    private void tearDownShortCut()
    {

        Intent intent = new Intent(DROP_SHORTCUT_ACTION);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "sina");

        String appClass = getPackageName() + "." + this.getLocalClassName();

        ComponentName component = new ComponentName(getPackageName(), appClass);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent().setAction(Intent.ACTION_MAIN)
                .setComponent(component).addCategory("android.intent.category.LAUNCHER"));
        sendBroadcast(intent);

    }

}
