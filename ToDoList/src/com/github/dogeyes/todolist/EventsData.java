package com.github.dogeyes.todolist;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventsData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "events.db";
	private static final int DATABASE_VERSION = 1;
	
	public EventsData(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String createQuery = "CREATE TABLE " + EventConstants.TABLE_NAME + " ( " 
				+ EventConstants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ EventConstants.TIME + " INTEGER,"
				+ EventConstants.TITLE + " TEXT NOT NULL, "
				+ EventConstants.ISFINISH + " INTEGER);";
		db.execSQL(createQuery);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + EventConstants.TABLE_NAME);
		onCreate(db);
	}
}
