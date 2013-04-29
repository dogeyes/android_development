package com.github.dogeyes.todolist;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import android.database.Cursor;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.widget.SimpleCursorAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.app.LoaderManager;
import android.text.Html;
import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import com.atermenji.android.iconictextview.IconicTextView;
import com.atermenji.android.iconictextview.icon.*;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.app.DialogFragment;

public class MainActivity extends ListActivity 
	implements LoaderManager.LoaderCallbacks<Cursor>, SensorEventListener, ConfirmFragment.ConfirmDialogListener{

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	
	private static final String TAG = "Event main";
	private EventsData events;
	private IconicTextView add_button;
	private Button clear_finish_button;
	private EditText new_text;
	private Cursor cursorData;
	
	private static String[] FROM = {EventConstants._ID, EventConstants.TIME, EventConstants.TITLE, EventConstants.ISFINISH};
	private static int[] TO = {R.id.event_id, R.id.event_time, R.id.event_title, R.id.event_is_finish};
	private static String[] FROM2 = {EventConstants._ID};
	private static int[] TO2 = {R.id.event};
	private static String ORDER_BY = EventConstants.ISFINISH + " ASC" +  "," + EventConstants.TIME  + " DESC";
	private SimpleCursorAdapter eventsAdapter;
	
	private boolean confirmFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		add_button = (IconicTextView)findViewById(R.id.add_button);
		add_button.setIcon(EntypoIcon.PLUS);
		clear_finish_button = (Button)findViewById(R.id.clear_finish_button);
		new_text = (EditText)findViewById(R.id.new_text);
		add_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.d(TAG, new_text.getText().toString());
				addEvent(new_text.getText().toString());
				new_text.setText(null);
			}
		});
		
		clear_finish_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showConfirmDialog();
			}
		});
		eventsAdapter = new SimpleCursorAdapter(this, R.layout.event, null, FROM2, TO2, 0);
		eventsAdapter.setViewBinder(new myViewBinder());
		this.setListAdapter(eventsAdapter);
		
		getLoaderManager().initLoader(0, null, this);

	}
	
	public void showConfirmDialog()
	{
		if(!confirmFlag)
		{
			confirmFlag = true;
			DialogFragment dialog = new ConfirmFragment();
			dialog.show(getFragmentManager(), "ConfirmDialogFragment");
		}
	}

	public void onDialogPositiveClick(DialogFragment dialog)
	{
		//Log.d(TAG, "Confirmed");
		clearFinished();
	}
	
	@Override
	public void onConfirmDialogRelease()
	{
		confirmFlag = false;
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
	
	private float[] preGravity = new float[3];
	private int count = 0;
	private long preTime;
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] gravity = new float[3];
		long nowTime = 0;
		if(event.sensor.equals(mAccelerometer))
		{
			gravity[0] = event.values[0];
			gravity[1] = event.values[1];
			gravity[2] = event.values[2];
			nowTime = event.timestamp;
			
			if(count == 0)
			{
				preGravity[0] = event.values[0];
				preGravity[1] = event.values[1];
				preGravity[2] = event.values[2];
				preTime = event.timestamp;
				count++;
			}
			else
			{
				if( (Math.abs(preGravity[0] - gravity[0]) > 5.0 || 
						Math.abs(preGravity[1] - gravity[1]) > 5.0 || 
						Math.abs(preGravity[2] - gravity[2]) > 5.0) &&
						Math.abs(nowTime - preTime) < 300000000)
				{
					count++;
				}
				if(Math.abs(nowTime - preTime) > 500000000)
				{
					count = 0;
				}
			}
			Log.d(TAG, "count = " + count + " x = " + preGravity[0] + ", y = " + preGravity[1] + ", z = " + preGravity[2] + " Time = " + System.currentTimeMillis());
			Log.d(TAG, "count = " + count + " dx = " + Math.abs(preGravity[0] - gravity[0]) +
					", dy = " + Math.abs(preGravity[1] - gravity[1]) 
					+ ", dz = " + Math.abs(preGravity[2] - gravity[2]) 
					+ " dTime = " + Math.abs(nowTime - preTime));
			if( (Math.abs(preGravity[0] - gravity[0]) > 2.0 || 
					Math.abs(preGravity[1] - gravity[1]) > 2.0 || 
					Math.abs(preGravity[2] - gravity[2]) > 2.0))
			{
				preGravity[0] = gravity[0];
				preGravity[1] = gravity[1];
				preGravity[2] = gravity[2];
				preTime = nowTime;
			}
			if(count >= 4)
			{
				count = 0;
				showConfirmDialog();
			}
		}
    }
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		return new CursorLoader(this, EventConstants.EVENT_URI, FROM, null, null, ORDER_BY);
	}
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		cursorData = data;
		eventsAdapter.swapCursor(data);
	}
	@Override
    public void onLoaderReset(Loader<Cursor> loader) {
        eventsAdapter.swapCursor(null);
    }

	private void addEvent(String string)
	{
		if(string.equals("") || string == null)
			return;
		ContentValues values = new ContentValues();
		values.put(EventConstants.TIME, System.currentTimeMillis());
		values.put(EventConstants.TITLE, string);
		values.put(EventConstants.ISFINISH, 0);
		this.getContentResolver().insert(EventConstants.EVENT_URI, values);
		getLoaderManager().restartLoader(0, null, this);
	}
	
	public void finishEvent(long id, int isFinish)
	{
		ContentValues values = new ContentValues();
		values.put(EventConstants.ISFINISH, 1 - isFinish);
		String selection = EventConstants._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		this.getContentResolver().update(EventConstants.EVENT_URI, values, selection, selectionArgs);
		this.getLoaderManager().restartLoader(0, null, this);
	}

	public void deleteEvent(Long id)
	{
		String selection = EventConstants._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		this.getContentResolver().delete(EventConstants.EVENT_URI, selection, selectionArgs);
		this.getLoaderManager().restartLoader(0, null, this);
	}
	
	private void clearFinished()
	{
		String selection = EventConstants.ISFINISH + " LIKE ?";
		String[] selectionArgs = { String.valueOf(1) };
		this.getContentResolver().delete(EventConstants.EVENT_URI, selection, selectionArgs);
		this.getLoaderManager().restartLoader(0, null, this);
	}
	
	private DeleteGestureDetector myGestDetector;
	
	private class myViewBinder implements SimpleCursorAdapter.ViewBinder
	{
		@Override
		public boolean setViewValue(View view, Cursor cursor, int colomn)
		{
			LinearLayout eventLayout = (LinearLayout)view;
			TextView event_id = (TextView)eventLayout.findViewById(R.id.event_id);
			TextView event_time =(TextView)eventLayout.findViewById(R.id.event_time);
			TextView event_title = (TextView)eventLayout.findViewById(R.id.event_title);
			TextView event_is_finish = (TextView)eventLayout.findViewById(R.id.event_is_finish);
			
			IconicTextView delete_event = (IconicTextView)eventLayout.findViewById(R.id.delete_iconic_text_view);
			delete_event.setIcon(EntypoIcon.CIRCLED_CROSS);
			
			long id = cursor.getLong(cursor.getColumnIndex(EventConstants._ID));
			event_id.setText(String.valueOf(id));
			event_time.setText(String.valueOf(cursor.getLong(cursor.getColumnIndex(EventConstants.TIME))));
			int isFinish = cursor.getInt(cursor.getColumnIndex(EventConstants.ISFINISH));
			event_is_finish.setText(String.valueOf(isFinish));
			
			myGestDetector = new DeleteGestureDetector(MainActivity.this, new DeleteOnGestureListener());
			event_title.setOnTouchListener(new EventFinishOnTouchListener(id, isFinish));
			
			//event_title.setOnClickListener(new EventFinishOnClickListener(id, isFinish));
			if(isFinish == 1)
			{
				event_title.setPaintFlags(event_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				event_title.setTextColor(MainActivity.this.getResources().getColor(R.color.finished_color));
			}
			else
			{
				event_title.setPaintFlags(event_title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
				event_title.setTextColor(MainActivity.this.getResources().getColor(R.color.not_finished_color));
			}
			event_title.setText(cursor.getString(cursor.getColumnIndex(EventConstants.TITLE)));
			
			delete_event.setOnClickListener(new EventDeleteOnClickListener(id));
			return true;
		}
	}
	public class DeleteGestureDetector extends GestureDetector
	{
		long id;
		int isFinish;
		DeleteOnGestureListener deleteOnGestureListener;
		public DeleteGestureDetector(Context context, DeleteOnGestureListener deleteOnGestureListener)
		{
			super(context, deleteOnGestureListener);
			id = 0;
			isFinish = 1;
			this.deleteOnGestureListener = deleteOnGestureListener;
		}
		public void setId(long id)
		{
			this.id = id;
			deleteOnGestureListener.setId(id);
		}
		public void setIsFinish(int isFinish)
		{
			this.isFinish = isFinish;
			deleteOnGestureListener.setIsFinish(isFinish);
		}
	}
	public class DeleteOnGestureListener extends SimpleOnGestureListener
	{
		long id;
		int isFinish;
		public DeleteOnGestureListener()
		{
			super();
			id = 0;
			isFinish = 1;
		}
		public void setId(long id)
		{
			this.id = id;
		}
		public void setIsFinish(int isFinish)
		{
			this.isFinish = isFinish;
		}
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
        	MainActivity.this.finishEvent(id, isFinish);
            return true;
        }
        @Override
        public boolean onDown(MotionEvent e1)
        {
        	Log.d(TAG, "onDown" + String.valueOf(id));
            return true;
        }
	}
	public class EventFinishOnTouchListener implements OnTouchListener
	{
		long id;
		int isFinished;
		public EventFinishOnTouchListener(long id, int isFinished)
		{
			super();
			this.id = id;
			this.isFinished = isFinished;
		}
		@Override
		public boolean onTouch(View view, MotionEvent event)
		{
			LinearLayout lay = (LinearLayout)(view.getParent());
			TextView text = (TextView)lay.findViewById(R.id.event_id);
			Log.d(TAG, "event_title.onTouch " + text.getText().toString());
			myGestDetector.setId(id);
			myGestDetector.setIsFinish(isFinished);
			return myGestDetector.onTouchEvent(event);
		}
	}
	public class EventFinishOnClickListener implements OnClickListener
	{
		long id;
		int isFinished;
		public EventFinishOnClickListener(long id, int isFinished)
		{
			super();
			this.id = id;
			this.isFinished = isFinished;
		}
		@Override 
		public void onClick(View view)
		{
			MainActivity.this.finishEvent(id, isFinished);
		}
	}
	public class EventDeleteOnClickListener implements OnClickListener
	{
		long id;
		public EventDeleteOnClickListener(long id)
		{
			super();
			this.id = id;
		}
		@Override 
		public void onClick(View view)
		{
			MainActivity.this.deleteEvent(id);
		}
	}
}
