package com.github.dogeyes.todolist;

import android.provider.BaseColumns;
import android.net.Uri;

public interface EventConstants extends BaseColumns {
	public static final String TABLE_NAME = "events";
	public static final String TIME = "time";
	public static final String TITLE = "title";
	public static final String ISFINISH = "is_finish";
	public static final String AUTHORITY="com.github.dogeyes.todolist.provider";
	public static final Uri EVENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
}
