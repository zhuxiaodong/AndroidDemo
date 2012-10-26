package com.example.dndlist;

import com.example.dd.MyListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((ListView) findViewById(R.id.listView1))
				.setAdapter(new MyListAdapter());
	}

}
