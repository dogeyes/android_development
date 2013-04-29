package com.example.events;

import android.provider.BaseColumns;
import android.net.Uri;


public interface Constants {
	public static final String TABLE_NAME = "events";
	public static final String TIME = "time";
	public static final String TITLE = "title";
	public static final String AUTHORITY = "com.example.events";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
}
