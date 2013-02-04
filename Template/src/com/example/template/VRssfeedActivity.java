/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.os.Bundle;
import android.view.Menu;

public class VRssfeedActivity extends ActivityConstants implements VMyListFragment.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_activity_rssfeed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.v_activity_rssfeed, menu);
        return true;
    }

  public void onRssItemSelected(String link) {
    VDetailFragment fragment = null;//(VDetailFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
          fragment.setText(link);
        } 
  }
    
} 