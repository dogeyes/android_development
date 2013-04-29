package com.example.events;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import static android.provider.BaseColumns._ID;
import static com.example.events.Constants.TABLE_NAME;
import static com.example.events.Constants.TIME;
import static com.example.events.Constants.TITLE;
import static com.example.events.Constants.CONTENT_URI;
import static com.example.events.Constants.AUTHORITY;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class EventsProvider extends ContentProvider {
	private static final String TAG = "EventsProvider";
	private static final int EVENTS = 1;
	private static final int EVENTS_ID = 2;
	private static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.example.event";
	private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.event";
	
	private EventsData eventsData;
	private SQLiteDatabase db;
	private UriMatcher uriMatcher;
	public EventsProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO: Implement this to handle requests to insert a new row.
		db = eventsData.getWritableDatabase();
		Log.d(TAG, "insert " + uri.getPath());
		db.insert(TABLE_NAME, null, values);
		return uri;
	}

	@Override
	public boolean onCreate() {
		eventsData = new EventsData(this.getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = eventsData.getReadableDatabase();
		Log.d(TAG, "query " + uri.getPath());
		return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
