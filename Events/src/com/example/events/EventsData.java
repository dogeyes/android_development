package com.example.events;

import static android.provider.BaseColumns._ID;
import static com.example.events.Constants.TABLE_NAME;
import static com.example.events.Constants.TIME;
import static com.example.events.Constants.TITLE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventsData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "events.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG="EventsData";
	public EventsData(Context ctx)
	{
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d(TAG, "onCreate");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ TIME + " INTEGER," + TITLE + " TEXT NOT NULL);");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
