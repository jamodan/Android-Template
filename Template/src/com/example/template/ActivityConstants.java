/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityConstants extends FragmentActivity
{
	Intent intent;
	Context context = null;
	
	public void iGrowButtonClick(View view){
		// Display additional information for feed analysis values
		Uri uri = Uri.parse( "http://igrow.org" );
		startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
	}
	
	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        if (android.os.Build.VERSION.SDK_INT >= 11)
        {
	        ImageView iGrowLogo = (ImageView) menu.findItem(R.id.menu_igrow).getActionView();
	        iGrowLogo.setOnClickListener(new OnClickListener(){
		    	public void onClick(View view){
		    		// Display additional information for feed analysis values
		    		iGrowButtonClick(view);
		    	}
		    }
		    );
        }
        return true;
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId()) {
    	case R.id.menu_about:
    		intent = new Intent(this, SettingsMainActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.putExtra(SettingsMainActivity.TAB, "about");
    		try {
    			startActivity(intent);
    		}
    		catch (ActivityNotFoundException e){
    			Toast.makeText(context, "NO Viewer", Toast.LENGTH_SHORT).show();
    		}
    		return true;
    	case R.id.menu_disclaimer:
    		intent = new Intent(this, SettingsMainActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.putExtra(SettingsMainActivity.TAB, "disclaimer");
    		try {
    			startActivity(intent);
    		}
    		catch (ActivityNotFoundException e){
    			Toast.makeText(context, "NO Viewer", Toast.LENGTH_SHORT).show();
    		}
    		return true;
    	case R.id.menu_credits:
    		intent = new Intent(this, SettingsMainActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.putExtra(SettingsMainActivity.TAB, "credits");
    		try {
    			startActivity(intent);
    		}
    		catch (ActivityNotFoundException e){
    			Toast.makeText(context, "NO Viewer", Toast.LENGTH_SHORT).show();
    		}
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	// Called by pressing the rate this app button. Opens the app store to the app for users to enter feedback.
	public void rate(View v)
	{
		Toast.makeText(context, "This feature is not available in Beta versions.", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(context, "NO Viewer", Toast.LENGTH_SHORT).show();
		}
	}
}