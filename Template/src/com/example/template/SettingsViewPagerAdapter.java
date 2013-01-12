/*
 * Created By : Daniel Jamison
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */
package com.example.template;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class SettingsViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;
 
    public SettingsViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context=context;
 
        }
    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        // Each position is a tab that can be swiped to. So each tab has a different layout
        switch(position){
        case 0:
        	f=SettingsAboutTab.newInstance(_context);
            break;
        case 1:
            f=SettingsDisclaimerTab.newInstance(_context);
            break;
        case 2:
        	f=SettingsCreditsTab.newInstance(_context);
            break;
        }
        return f;
    }
    @Override
    public int getCount() {
    	// Returns the total number of screens
        return 3;
    }
}