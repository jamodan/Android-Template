/*
 * Created By : Daniel Jamison
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */
package com.example.template;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SettingsMainActivity extends ActivityConstants {
	public static final String TAB = "tab";
	public static String tab = "";
	private ViewPager _mViewPager;
	private SettingsViewPagerAdapter _adapter;
	private RelativeLayout page_one = null;
	private RelativeLayout page_two = null;
	private RelativeLayout page_three = null;
	FragmentManager fm = null;
	Intent intent;
	Bundle extras = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		extras = getIntent().getExtras();
		if(extras!= null)
		{
			tab = extras.getString(TAB);
		}
		else
		{
			tab = "about";
		}

		page_one = (RelativeLayout)findViewById(R.id.first_tab);
		page_one.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				_mViewPager.setCurrentItem(0);
			}
		});

		page_two = (RelativeLayout)findViewById(R.id.second_tab);
		page_two.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				_mViewPager.setCurrentItem(1);
			}
		});

		page_three = (RelativeLayout)findViewById(R.id.third_tab);
		page_three.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				_mViewPager.setCurrentItem(2);
			}
		});
		
		setUpView();
		setTab();
	}
	private void setUpView(){
		_mViewPager = (ViewPager) findViewById(R.id.viewPager);
		_adapter = new SettingsViewPagerAdapter(getApplicationContext(),getSupportFragmentManager());
		_mViewPager.setAdapter(_adapter);
		if(tab.equals("about"))
		{
			_mViewPager.setCurrentItem(0);
			changeTab(0);
		}
		else if(tab.equals("disclaimer"))
		{
			_mViewPager.setCurrentItem(1);
			changeTab(1);
		}
		else if(tab.equals("credits"))
		{
			_mViewPager.setCurrentItem(2);
			changeTab(2);
		}
		else
		{
			_mViewPager.setCurrentItem(0);
			changeTab(0);
		}
	}
	
	private void setTab(){
		_mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

			public void onPageScrollStateChanged(int position) {}
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			public void onPageSelected(int position) {
				changeTab(position);
			}
		});
	}
	
	// Changes which tab is highlighted
	private void changeTab(int position)
	{
		switch(position){
		case 0:
			findViewById(R.id.first_tab).setBackgroundResource(R.drawable.settings_tab_indicator_selected);
			findViewById(R.id.second_tab).setBackgroundResource(R.drawable.settings_tab_indicator_unselected);
			findViewById(R.id.third_tab).setBackgroundResource(R.drawable.settings_tab_indicator_unselected);
			break;
		case 1:
			findViewById(R.id.first_tab).setBackgroundResource(R.drawable.settings_tab_indicator_unselected);
			findViewById(R.id.second_tab).setBackgroundResource(R.drawable.settings_tab_indicator_selected);
			findViewById(R.id.third_tab).setBackgroundResource(R.drawable.settings_tab_indicator_unselected);
			break;
		case 2:
			findViewById(R.id.first_tab).setBackgroundResource(R.drawable.settings_tab_indicator_unselected);
			findViewById(R.id.second_tab).setBackgroundResource(R.drawable.settings_tab_indicator_unselected);
			findViewById(R.id.third_tab).setBackgroundResource(R.drawable.settings_tab_indicator_selected);
			break; 
		}  
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_about:
			_mViewPager.setCurrentItem(0);
			return true;
		case R.id.menu_disclaimer:
			_mViewPager.setCurrentItem(1);
			return true;
		case R.id.menu_credits:
			_mViewPager.setCurrentItem(2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}