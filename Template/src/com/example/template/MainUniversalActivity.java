/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainUniversalActivity extends ActivityConstants {

	boolean mIsDualPane;
	Intent intent;
	private SettingsDisclaimerTab fragment;
	private SettingsAboutTab fragment1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragments", "0");
        setContentView(R.layout.u_main);
        
        View previous = findViewById(R.id.previous);
        mIsDualPane = previous != null && previous.getVisibility() == View.VISIBLE;

        Log.d("Fragments", "2.5");
     // create fragments to use
        if (savedInstanceState != null) {
        	Log.d("Fragments", "3");
            fragment = (SettingsDisclaimerTab) getSupportFragmentManager().getFragment(savedInstanceState, "current");
            		
            if (mIsDualPane && getSupportFragmentManager().getFragment(savedInstanceState, "previous") != null)
            {
            	fragment1 = (SettingsAboutTab) getSupportFragmentManager().getFragment(savedInstanceState, "previous");
            	if (fragment1 == null)
            	{
            		fragment1 = new SettingsAboutTab();
                	getSupportFragmentManager().beginTransaction().add(R.id.previous, fragment1).commit();
            	}
            }
        }
        else
        {
        	Log.d("Fragments", "1");
            if (fragment == null)
            //if (getSupportFragmentManager().getFragment(savedInstanceState, "current") == null)
            {
            	Log.d("Fragments", "4");
                fragment = new SettingsDisclaimerTab();
                getSupportFragmentManager().beginTransaction().add(R.id.current, fragment, "current").commit();
            }
       
            if (mIsDualPane && fragment1 == null)
            //if (mIsDualPane && getSupportFragmentManager().getFragment(savedInstanceState, "previous") != null)
        	{
        		fragment1 = new SettingsAboutTab();
            	getSupportFragmentManager().beginTransaction().add(R.id.previous, fragment1).commit();
            }
        }
        Log.d("Fragments", "3.5");
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Fragments", "5");
        getSupportFragmentManager().putFragment(outState, "current", fragment);
        //if (mIsDualPane || fragment1 != null)
        	if (getSupportFragmentManager().getFragment(outState, "previous") != null)
        {
        	//fragment1.getActivity().finish();
        	//getSupportFragmentManager().beginTransaction().addToBackStack("previous");
        	getSupportFragmentManager().putFragment(outState, "previous", fragment1);
        	//fragment1 = null;
        }
        Log.d("Fragments", "6");
    }
}
