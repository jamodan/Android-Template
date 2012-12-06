/*
 * SwipeMain.java
 * Feed Cost Calculator
 * Created By : Daniel Jamison
 * Date : August 03, 2012
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
 
public class SettingsMainActivity extends FragmentActivity {
    private ViewPager _mViewPager;
    private SettingsViewPagerAdapter _adapter;
    private RelativeLayout page_one = null;
    private RelativeLayout page_two = null;
    private RelativeLayout page_three = null;
    Intent intent;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        setUpView();
        setTab();
        
        page_one = (RelativeLayout)findViewById(R.id.first_tab);
        page_one.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view){
	    		_mViewPager.setCurrentItem(0);
	    	}
	    }
	    );
        
        page_two = (RelativeLayout)findViewById(R.id.second_tab);
        page_two.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view){
	    		_mViewPager.setCurrentItem(1);
	    	}
	    }
	    );
        
        page_three = (RelativeLayout)findViewById(R.id.third_tab);
        page_three.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view){
	    		_mViewPager.setCurrentItem(2);
	    	}
	    }
	    );
    }
    private void setUpView(){
     _mViewPager = (ViewPager) findViewById(R.id.viewPager);
     _adapter = new SettingsViewPagerAdapter(getApplicationContext(),getSupportFragmentManager());
     _mViewPager.setAdapter(_adapter);
     _mViewPager.setCurrentItem(0);
    }
    private void setTab(){
    	_mViewPager.setOnPageChangeListener(new OnPageChangeListener(){
 
	        public void onPageScrollStateChanged(int position) {}
	        public void onPageScrolled(int arg0, float arg1, int arg2) {}
	        public void onPageSelected(int position) {
	        // Changes which tab is highlighted
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
        });
    }
    
    // Called by pressing the iGrow logo button
    public void info(View v)
    {
    	Uri uri = Uri.parse( "http://igrow.org" );
		startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
    }
    
    // Called by pressing the rate this app button. Opens the app store to the app for users to enter feedback.
    public void rate(View v)
    {
    	Toast.makeText(SettingsMainActivity.this, "This feature is not available in Beta versions.", Toast.LENGTH_SHORT).show();
    	/*Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse("market://details?id=edu.sdstate.feedcostcalculator"));
    	
		 try {
               startActivity(intent);
           }
        catch (ActivityNotFoundException e){
               Toast.makeText(SettingsMainActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
           }*/
    }
    
    // Called when the Other Apps by Us button is pressed. Launches a search in Google Play for our developer name.
    public void otherApps(View v)
    {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse("market://search?q=pub:SDSU Mobile Apps"));
    	
		 try {
               startActivity(intent);
           }
        catch (ActivityNotFoundException e){
               Toast.makeText(SettingsMainActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
           }
    }
}