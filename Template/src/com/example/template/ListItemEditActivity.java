/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.template.R.string;

public class ListItemEditActivity extends ActivityConstants {
	public static final String KEY_ID = "ID";
	public int ID = 0;
	public String accessType = "";
	
	private TextView windowTitle = null;
	private Button button_logo = null;
	private Button button_info = null;
	private Button button_done = null;
	Intent intent;
	Context context;
	
	// Generic place holders for data
	private RelativeLayout row1 = null;
	private TextView label1 = null;
	private Object data1 = null;
	private RelativeLayout row2 = null;
	private TextView label2 = null;
	private Object data2 = null;
	private RelativeLayout row3 = null;
	private TextView label3 = null;
	private Object data3 = null;
	private RelativeLayout row4 = null;
	private TextView label4 = null;
	private Object data4 = null;
		
	Bundle extras = null;
	private Boolean updateFlag = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_edit);
        
        windowTitle = (TextView)findViewById(R.id.windowTitle);
        
        // Get the feed ID passed to this class
        extras = getIntent().getExtras();
        if(extras!= null)
        {
        	ID = Integer.parseInt(extras.getString(KEY_ID));
        }
        
        // Make sure the passed inventoryID is a valid number then get its data, set the save flag, 
        //    hide the softtouch keyboard by focusing on the top control bar
        if(ID > 0)
        {
        	//getData();
        	updateFlag = true;
        }
         
        accessType = ListViewActivity.accessType;
           
        if (accessType.equals("Data"))
        {
        	windowTitle.setText("Data");
        	setupDataRows();
        	if(updateFlag)
        	{
        		Cursor c = DatabaseDataTable.get(null, DatabaseDataTable.COLUMN_ID + " = ?", new String[] { "" + ID }, null);
        		if(c != null)
    			{
    				if(c.moveToFirst())
    				{
    					do
    					{
    						// Populate view with old data for editing
    						((EditText) data1).setText(c.getString(c.getColumnIndex(DatabaseDataTable.COLUMN_ID)));
    						((EditText) data2).setText(c.getString(c.getColumnIndex(DatabaseDataTable.COLUMN_NAME)));
    						((EditText) data3).setText(c.getString(c.getColumnIndex(DatabaseDataTable.COLUMN_VALUE)));
    						((EditText) data4).setText(c.getString(c.getColumnIndex(DatabaseDataTable.COLUMN_DESCRIPTION)));
    					}while(c.moveToNext());
    				}
    			}
        	}
        }
        else
        {
        	Toast.makeText(ListItemEditActivity.this, "Access Type Not Defined", Toast.LENGTH_SHORT).show();
        	finish();
        }
        
        
        button_done = (Button)findViewById(R.id.buttonDone);
        button_done.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view){
	    		// Save the feed record and return to the main screen
	    		if (accessType.equals("Data"))
	            {
	            	saveData();
	            }
	            else
	            {
	            	Toast.makeText(ListItemEditActivity.this, "Access Type Not Defined For Save", Toast.LENGTH_SHORT).show();
	            }
	    	}
	    });
        
        // If a new feedstuff sets values to zero by default otherwise calculates numbers based on the passed feed ID
        //calculations();
    }
	
	// Set all EditTexts to erase a zero when focused
    // Set all EditTexts to recalculate all TextView values on each character change.
	/*private void setupRows()
	{
		// ##### SPINNER FROM DATABASE #####
		row# = (RelativeLayout) findViewById(R.id.data#Stuff);
		row#.setVisibility(View.VISIBLE);
		label# = (TextView) findViewById(R.id.label#);
		label#.setText(string.info#...);
		data# = (Spinner) findViewById(R.id.data#);
        SimpleCursorAdapter spinnerAdapter# = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, db.getAll...(), new String[] { "column_name" }, new int[] { android.R.id.text1 });
        spinnerAdapter#.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) data#).setAdapter(spinnerAdapter#);
        ((Spinner) data#).setOnItemSelectedListener(new OnItemSelectedListener()
        {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				// TODO Auto-generated method stub
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
			}
        });
        
        // ##### EDIT TEXT #####
        row# = (RelativeLayout) findViewById(R.id.data#Stuff);
		row#.setVisibility(View.VISIBLE);
		label# = (TextView) findViewById(R.id.label#);
		label#.setText(string.info#...);
    	data# = (EditText)findViewById(R.id.data#);
    	((EditText) data#).setInputType(InputType.bla | InputType.bla);
    	((EditText) data#).setOnFocusChangeListener(new OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if (hasFocus==true)
                {
                	String temp = ((EditText) data#).getText().toString();
                	// Check to see if the value in the box is zero, if it is erase the zero so the user can type in a new number
                    if (temp.compareTo("0")==0 || temp.compareTo("0.")==0 || temp.compareTo("0.0")==0 || temp.compareTo("0.00")==0)
                    {
                    	((EditText) data#).setText("");
                    }
                }
            }
        });
    	// After any character change redo all calculations on the screen
    	((EditText) data#).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				//calculations();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
    	
    	// ##### TEXT VIEW #####
    	row# = (RelativeLayout) findViewById(R.id.data#Stuff);
		row#.setVisibility(View.VISIBLE);
		label# = (TextView) findViewById(R.id.label#);
		label#.setText(string.info#...);
    	data# = (TextView)findViewById(R.id.data#);
    	
	}*/
	
	private void setupDataRows()
	{       
		// ##### EDIT TEXT #####
        row1 = (RelativeLayout) findViewById(R.id.data1Stuff);
        row1.setVisibility(View.VISIBLE);
		label1 = (TextView) findViewById(R.id.label1);
		label1.setText(string.info1);
    	data1 = (EditText)findViewById(R.id.data1);
    	((EditText) data1).setInputType(InputType.TYPE_CLASS_TEXT);
    	((EditText) data1).setOnFocusChangeListener(new OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if (hasFocus==true)
                {
                	String temp = ((EditText) data1).getText().toString();
                	// Check to see if the value in the box is zero, if it is erase the zero so the user can type in a new number
                    if (temp.compareTo("0")==0 || temp.compareTo("0.")==0 || temp.compareTo("0.0")==0 || temp.compareTo("0.00")==0)
                    {
                    	((EditText) data1).setText("");
                    }
                }
            }
        }); 
    	// After any character change redo all calculations on the screen
    	((EditText) data1).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				dataCalculations();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
        
        // ##### EDIT TEXT #####
        row2 = (RelativeLayout) findViewById(R.id.data2Stuff);
        row2.setVisibility(View.VISIBLE);
		label2 = (TextView) findViewById(R.id.label2);
		label2.setText(string.info2);
    	data2 = (EditText)findViewById(R.id.data2);
    	((EditText) data2).setInputType(InputType.TYPE_CLASS_TEXT);
    	((EditText) data2).setOnFocusChangeListener(new OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if (hasFocus==true)
                {
                	String temp = ((EditText) data2).getText().toString();
                	// Check to see if the value in the box is zero, if it is erase the zero so the user can type in a new number
                    if (temp.compareTo("0")==0 || temp.compareTo("0.")==0 || temp.compareTo("0.0")==0 || temp.compareTo("0.00")==0)
                    {
                    	((EditText) data2).setText("");
                    }
                }
            }
        }); 
    	// After any character change redo all calculations on the screen
    	((EditText) data2).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				dataCalculations();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
    	
    	// ##### EDIT TEXT #####
        row3 = (RelativeLayout) findViewById(R.id.data3Stuff);
        row3.setVisibility(View.VISIBLE);
		label3 = (TextView) findViewById(R.id.label3);
		label3.setText(string.info3);
    	data3 = (EditText)findViewById(R.id.data3);
    	((EditText) data3).setInputType(InputType.TYPE_CLASS_TEXT);
    	((EditText) data3).setOnFocusChangeListener(new OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if (hasFocus==true)
                {
                	String temp = ((EditText) data3).getText().toString();
                	// Check to see if the value in the box is zero, if it is erase the zero so the user can type in a new number
                    if (temp.compareTo("0")==0 || temp.compareTo("0.")==0 || temp.compareTo("0.0")==0 || temp.compareTo("0.00")==0)
                    {
                    	((EditText) data3).setText("");
                    }
                }
            }
        }); 
    	// After any character change redo all calculations on the screen
    	((EditText) data3).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				dataCalculations();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
        
        // ##### EDIT TEXT #####
        row4 = (RelativeLayout) findViewById(R.id.data4Stuff);
        row4.setVisibility(View.VISIBLE);
		label4 = (TextView) findViewById(R.id.label4);
		label4.setText(string.info4);
    	data4 = (EditText)findViewById(R.id.data4);
    	((EditText) data4).setInputType(InputType.TYPE_CLASS_TEXT);
    	((EditText) data4).setOnFocusChangeListener(new OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if (hasFocus==true)
                {
                	String temp = ((EditText) data4).getText().toString();
                	// Check to see if the value in the box is zero, if it is erase the zero so the user can type in a new number
                    if (temp.compareTo("0")==0 || temp.compareTo("0.")==0 || temp.compareTo("0.0")==0 || temp.compareTo("0.00")==0)
                    {
                    	((EditText) data4).setText("");
                    }
                }
            }
        }); 
    	// After any character change redo all calculations on the screen
    	((EditText) data4).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				dataCalculations();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
	}
	
	private void dataCalculations()
	{
		
	}
	
	// Save the data to the database
	public void saveData()
	{
		if(updateFlag == false)
		{
			DatabaseDataTable.insert(
					((EditText) data2).getText().toString(), 
					((EditText) data3).getText().toString(),
					((EditText) data4).getText().toString());
		}
		else
		{
			DatabaseDataTable.update(
					((EditText) data2).getText().toString(), 
					((EditText) data3).getText().toString(),
					((EditText) data4).getText().toString(),
					DatabaseDataTable.COLUMN_ID + " = ?",
					new String[] { "" + ID });
		}
		
		finish();
		
	}
	
	// Error messages
	public Dialog onCreateDialog(String error, int id)
	{
	 AlertDialog.Builder builder =null;
	 AlertDialog alert = null;
		switch(id)
		{
		case 1:
			
			builder = new AlertDialog.Builder(this);
			builder.setMessage(error)       
			.setCancelable(false)       
			.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{
					
				}      
			});
			alert = builder.create();
			alert.show();
			break;
			
		case 2:
			
			builder = new AlertDialog.Builder(this);
			builder.setMessage(error)       
			.setCancelable(false)       
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) 
				{
					finish();
				
				}      
			}) 
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)
				{	
					
				}       
			});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}
}
