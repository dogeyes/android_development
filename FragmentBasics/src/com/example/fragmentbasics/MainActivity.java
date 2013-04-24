package com.example.fragmentbasics;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class MainActivity extends FragmentActivity
	implements HeadlinesFragment.OnHeadlineSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(findViewById(R.id.fragment_container) != null)
		{
			if(savedInstanceState != null)
				return;
			HeadlinesFragment firstFragment = new HeadlinesFragment();
			
			firstFragment.setArguments(getIntent().getExtras());
			
			getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, firstFragment).commit();
		}
		
	}
	public void onArticleSelected(int position)
	{
		ArticleFragment newFragment = new ArticleFragment();
		Bundle args = new Bundle();
		args.putInt(ArticleFragment.ARG_POSITION, position);
		newFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);
		
		transaction.commit();
	}
}
