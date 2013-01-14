/*
 * Created By : Daniel Jamison
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */
package com.example.template;

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
        		intent = new Intent(MainActivity.this,ListViewActivity.class);
        		intent.putExtra(ListViewActivity.ACCESS_TYPE, "Data");

        		try {
        			startActivity(intent);
        		}
        		catch (ActivityNotFoundException e){
        			Toast.makeText(MainActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
    }
}
