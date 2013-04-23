package com.example.browserintent;

import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;

public class MainActivity extends Activity {
	private EditText urlText;
	private Button goButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		urlText = (EditText)findViewById(R.id.url_field);
		goButton = (Button)findViewById(R.id.go_button);
		
		goButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view)
			{
				openBrowser();
			}
		});
		urlText.setOnKeyListener(new OnKeyListener(){
			public boolean onKey(View view, int keyCode, KeyEvent event)
			{
				if(keyCode == KeyEvent.KEYCODE_ENTER)
				{
					openBrowser();
					return true;
				}
				return false;
			}
		});
	}
	
	private void openBrowser()
	{
		Uri uri = Uri.parse(urlText.getText().toString());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
