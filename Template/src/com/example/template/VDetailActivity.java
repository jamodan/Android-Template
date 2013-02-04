/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

public class VDetailActivity extends Activity {
  
  public static final String EXTRA_URL = "url";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Need to check if Activity has been switched to landscape mode
    // If yes, finished and go back to the start Activity
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      finish();
      return;
    }
    setContentView(R.layout.v_activity_detail);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String s = extras.getString(EXTRA_URL);
      TextView view = (TextView) findViewById(R.id.detailsText);
      view.setText(s);
    }
  }
} 