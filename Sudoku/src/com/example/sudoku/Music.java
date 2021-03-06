package com.example.sudoku;

import android.media.MediaPlayer;
import android.content.Context;

public class Music {
	private static MediaPlayer mp;
	public static void play(Context context, int resId)
	{
		stop(context);
		if(Prefs.getMusic(context) == true)
		{
			mp = MediaPlayer.create(context, resId);
			mp.setLooping(true);
			mp.start();
		}
	}
	public static void stop(Context context)
	{
		if(mp != null)
		{
			mp.stop();
			mp.release();
			mp = null;
		}
	}
}
