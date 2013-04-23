package com.example.sudoku;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity {

	private static final String TAG = "Sudoku";
	public static final String KEY_DIFFICULTY = "org.example.sudoku.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	private static int puzzle[] = new int[9*9];
	private static boolean puzzleMask[] = new boolean[9*9];
	private static String puzMask;
	private PuzzleView puzzleView;
	private int count;
	private static final String PREF_PUZZLE = "puzzle";
	private static final String PREF_PUZZLE_MASK = "puzzle_mask";
	protected static final int DIFFICULTY_CONTINUE = -1;
	
	private final String easyPuzzle = 
			"360000000004230800000004200" + 
			"070460003820000014500013020" + 
			"001900000007048300000000045";
	private final String mediumPuzzle = 
			"650000070000506000014000005" + 
			"007009000002314700000700800" + 
			"500000630000201000030000097";
	private final String hardPuzzle = 
			"009000000080605020501078000" + 
			"000000700706040102004000000" + 
			"000720903090301080000000600";
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	private int[] getPuzzle(int diff)
	{
		String puz = "";
		Log.d(TAG, "getPuzzle( " + diff + ")");
		switch(diff)
		{
		case DIFFICULTY_CONTINUE:
			puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, easyPuzzle);
			puzMask = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE_MASK, easyPuzzle);
			break;
		case DIFFICULTY_HARD:
			puz = hardPuzzle;
			puzMask = puz;
			break;

		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			puzMask = puz;
			break;

		case DIFFICULTY_EASY:
			puz = easyPuzzle;
			puzMask = puz;
			break;
		}
		count = 0;
		for(int i = 0; i < puz.length(); ++i)
		{
			if(puz.charAt(i) != '0')
				count ++;
		}
		puzzleMask = fromMaskString(puzMask);
		return fromPuzzleString(puz);
	}
	private void calculateUsedTiles()
	{
		Log.d(TAG, "calculateUsedTiles()");
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
			{
				used[x][y] = calculateUsedTiles(x, y);
			}
	}
	private int[] calculateUsedTiles(int x, int y)
	{
		int c[] = new int[9];
		for(int i = 0; i < 9; ++i)
		{
			if(i == y)
				continue;
			int t = getTile(x, i);
			if(t != 0)
				c[t - 1] = t;
		}
		for(int i = 0; i < 9; ++i)
		{
			if(i == x)
				continue;
			int t = getTile(i, y);
			if(t != 0)
				c[t - 1] = t;
		}
		
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;
		for(int i = startx; i < startx + 3; ++i)
			for(int j = starty; j < starty + 3; ++j)
			{
				if(i == x && j == y)
					continue;
				int t = getTile(i, j);
				if(t != 0)
					c[t - 1] = t;
			}
		
		int nused = 0;
		for(int t: c)
			if(t != 0)
				nused++;
		
		int c1[] = new int[nused];
		nused = 0;
		for(int t: c)
			if(t != 0)
				c1[nused++] = t;
		return c1;
	}
	public boolean setTileIfValid(int x, int y, int value)
	{
		int tiles[] = getUsedTiles(x, y);
		if(value != 0)
			for(int tile : tiles)
			{
				if(tile == value)
					return false;
			}
		setTile(x, y, value);
		calculateUsedTiles();
		return true;
	}
	public void showKeypadOrError(int x, int y)
	{
		int tiles[] = getUsedTiles(x, y);
		if(tiles.length == 9)
		{
			Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		else
		{
			if(!getMask(x, y))
			{	
				Log.d(TAG, "showKeypad: used = " + toPuzzleString(tiles));
				Dialog v = new Keypad(this, tiles, puzzleView);
				v.show();
			}
		}
	}
	public boolean getMask(int x, int y)
	{
		return puzzleMask[y * 9 + x];
	}
	public String getTileString(int x, int y)
	{
		int v = getTile(x, y);
		if(v == 0)
			return "";
		else
			return String.valueOf(v);
	}
	private int getTile(int x, int y)
	{
		return puzzle[y * 9 + x];
	}
	private void setTile(int x, int y, int value)
	{
		if(puzzle[y*9 + x] == 0 && value != 0)
			count++;
		else if(puzzle[y * 9 + x] != 0 && value == 0)
			count--;
		
		puzzle[y * 9 + x] = value;
	}
	private final int used[][][] = new int[9][9][];
	protected int[] getUsedTiles(int x, int y)
	{
		return used[x][y];
	}
	
	static private String toPuzzleString(int[] tiles)
	{
		StringBuilder buf = new StringBuilder();
		for(int c : tiles)
			buf.append(c);
		return buf.toString();
	}
	
	static protected int[] fromPuzzleString(String string)
	{
		int[] puz = new int[string.length()];
		for(int i = 0; i < puz.length; ++i)
		{
			puz[i] = string.charAt(i) - '0';
		}
		return puz;
	}
	static protected boolean[] fromMaskString(String string)
	{
		boolean[] mas = new boolean[string.length()];
		for(int i = 0; i < string.length(); ++i)
		{
			if(string.charAt(i) == '0')
				mas[i] = false;
			else 
				mas[i] = true;
		}
		return mas;
	}
	static protected String toMaskString(boolean[] mas)
	{
		StringBuilder sb = new StringBuilder();
		for(boolean b: mas)
		{
			if(b)
				sb.append('1');
			else
				sb.append('0');
		}
		return sb.toString();
	}
	public boolean isWin()
	{
		return count == 81;
	}
	public void showWin()
	{
		Toast toast = Toast.makeText(this, R.string.finish_text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		Music.play(this, R.raw.game);
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");
		Music.stop(this);
		
		getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE, toPuzzleString(puzzle)).commit();
		getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE_MASK, puzMask).commit();
	}

}
