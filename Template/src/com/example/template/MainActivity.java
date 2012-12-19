/*
 * Created By : Daniel Jamison
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */
package com.example.template;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Intent intent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Integer.valueOf(android.os.Build.VERSION.SDK) < 11)
        {
        	// Show the disclaimer
        	intent = new Intent(MainActivity.this,SettingsMainActivity.class);
        	try {
        		startActivity(intent);
        	}
        	catch (ActivityNotFoundException e){
        		Toast.makeText(MainActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
        	}
        }
        else
        {
        	MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main, menu);
	        ImageView iGrowLogo = (ImageView) menu.findItem(R.id.menu_igrow).getActionView();
	        iGrowLogo.setOnClickListener(new OnClickListener(){
		    	public void onClick(View view){
		    		// Display additional information for feed analysis values
		    		Uri uri = Uri.parse( "http://igrow.org" );
					startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
		    	}
		    }
		    );
        }
        //inflater.inflate(menuRes, menu)
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case R.id.menu_settings:
	            Intent intent = new Intent(this, SettingsMainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		 try {
	                    startActivity(intent);
	                }
	             catch (ActivityNotFoundException e){
	                    Toast.makeText(MainActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
	                }
	            return true;
	        default:
            return super.onOptionsItemSelected(item);
    	}
    }
}
