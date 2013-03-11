/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template;

import com.example.template.Database.DatabaseMain;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActivityConstants {

	Intent intent;
	public DatabaseMain datasource;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        datasource = new DatabaseMain(this);
        datasource.open();

        Button button_style1 = (Button)findViewById(R.id.mainStyle1);
        button_style1.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		// Show the disclaimer
        		intent = new Intent(getApplicationContext(),ListViewActivity.class);
        		intent.putExtra(ListViewActivity.ACCESS_TYPE, ListViewActivity.LIST_BY_DATA);

        		try {
        			startActivity(intent);
        		}
        		catch (ActivityNotFoundException e){
        			Toast.makeText(getApplicationContext(), "NO Viewer", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
        
        Button button_style2 = (Button)findViewById(R.id.mainStyle2);
        button_style2.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		// Show the disclaimer
        		intent = new Intent(getApplicationContext(),MainUniversalActivity.class);

        		try {
        			startActivity(intent);
        		}
        		catch (ActivityNotFoundException e){
        			Toast.makeText(getApplicationContext(), "NO Viewer", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
        
        Button button_style3 = (Button)findViewById(R.id.mainStyle3);
        button_style3.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		// Show the disclaimer
        		intent = new Intent(getApplicationContext(),VRssfeedActivity.class);

        		try {
        			startActivity(intent);
        		}
        		catch (ActivityNotFoundException e){
        			Toast.makeText(getApplicationContext(), "NO Viewer", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
    }
}
