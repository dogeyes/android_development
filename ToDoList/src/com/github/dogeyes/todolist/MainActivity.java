package com.github.dogeyes.todolist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;

public class MainActivity extends Activity {

	private static final String TAG = "Event main";
	private String[] events = {"event1", "event2", "event3"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		Log.d(TAG, events[1].toString());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, events);
		ListView event_list = (ListView)findViewById(R.id.event_list);
		event_list.setAdapter(adapter);
		Log.d(TAG, String.valueOf(adapter.getCount()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
