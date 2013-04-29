package com.github.dogeyes.todolist;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class EventsProvider extends ContentProvider {
	private static final String TAG = "EventProvider";
	private EventsData eventsData;
	private SQLiteDatabase db;
	private UriMatcher uriMatcher;
	
	public EventsProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		db = eventsData.getWritableDatabase();
		return db.delete(EventConstants.TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = eventsData.getWritableDatabase();
		db.insert(EventConstants.TABLE_NAME, null, values);
		return uri;
	}

	@Override
	public boolean onCreate() {
		eventsData = new EventsData(this.getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = eventsData.getReadableDatabase();
		return db.query(EventConstants.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		db = eventsData.getWritableDatabase();
		return db.update(EventConstants.TABLE_NAME, values, selection, selectionArgs);
	}
}
