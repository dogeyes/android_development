package com.github.dogeyes.todolist;

import android.provider.BaseColumns;

public interface EventConstants extends BaseColumns {
	public static final String TABLE_NAME = "events";
	public static final String TIME = "time";
	public static final String TITLE = "title";
	public static final String ISFINISH = "is_finish";
}
