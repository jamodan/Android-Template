/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.template.Database.DatabaseTableData;
import com.example.template.Database.DatabaseTableOptions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewActivity extends ActivityConstants{
	
	
	public static final String ACCESS_TYPE = "accessType";
	public static String accessType = "";
	SimpleCursorAdapter adapter = null;
	private MatrixCursor mCursor = null;
	
	ArrayList<Integer> listIDS = null;
	ArrayList<Integer> checkmarkedIDS = null;
	private TextView windowTitle=null;
	private Button button_export=null;
	private Button button_add=null;
	private Button button_delete=null;
	int selectedID = 0;
	Context context = null;
	
	ListView lv = null;
	Intent intent;
	Bundle extras = null;
	
	String columnData = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        
        
        windowTitle = (TextView)findViewById(R.id.windowTitle);
        
        extras = getIntent().getExtras();
        if(extras!= null)
        {
        	accessType = extras.getString(ACCESS_TYPE);
        }  
        
        listIDS = new ArrayList<Integer>();
        checkmarkedIDS = new ArrayList<Integer>();
 		// Displays the information in the list
        lv = (ListView) findViewById(R.id.list);
 		//setupListView();

        lv.setTextFilterEnabled(true);
        lv.setFocusable(true);
		lv.setFocusableInTouchMode(true);
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				if (accessType.equals("Data"))
				{
					selectedID = listIDS.get(position);
					  	
					intent		 = new Intent(getApplicationContext(), ListItemEditActivity.class);
					String ID = Integer.toString(listIDS.get(position));
					intent.putExtra(ListItemEditActivity.KEY_ID, ID);
					startActivity(intent);
				}
			}
		});
		lv.setOnScrollListener(new OnScrollListener()
		{
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// Change color based on positive/negative number
				if (accessType.equals("Bla1"))
				{
					setTotalBackgroundColor();
				}
			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Change color based on positive/negative number
				if (accessType.equals("Bla1"))
				{
					setTotalBackgroundColor();
				}
			}
		});
        
        button_export = (Button)findViewById(R.id.buttonExport);
        button_export.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view)
	    	{
	    		//getCheckedItems();
	    		export();
	    	}
	    }
	    );
        
        button_add = (Button)findViewById(R.id.buttonAdd);
        button_add.setOnClickListener(new OnClickListener()
        {
	    	public void onClick(View view){
	    		// Create a new row in the DB and pass its unique identifier to edit activity
	    		intent = new Intent(ListViewActivity.this,ListItemEditActivity.class);
	    		
	    		 try {
	                    startActivity(intent);
	                }
	             catch (ActivityNotFoundException e){
	                    Toast.makeText(ListViewActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
	                }
	    	}
	    }
	    );
        
        button_delete = (Button)findViewById(R.id.buttonDelete);
        button_delete.setOnClickListener(new OnClickListener()
        {
			public void onClick(View view)
	    	{
				getCheckedItems();
				
				int count = checkmarkedIDS.size();
				
				if (count < 1)
                {
                	onCreateDialog("No items selected.", 1);
                }
                else if (count == 1)
                {
                	onCreateDialog("Would you like to delete this item?", 2);
                }
                else
                {
                	onCreateDialog("Would you like to delete these items?", 2);
                }
	    	}
	    });
        
        // Special button setups for each list type
        if (accessType.equals("bla"))
		{
        	button_delete.setVisibility(View.INVISIBLE);
        	button_add.setVisibility(View.GONE);
		}
    }
    
    public void export()
    { 	
    	String combinedString = "";
    	if (accessType.equals("Bla1"))
		{
    		String columnName = "\"column1\",\"column2\",\"column3\"\n";
    		combinedString = columnName + columnData;
		}

    	File file = null;
    	File root = Environment.getExternalStorageDirectory();
    	if (root.canWrite())
    	{
    		File dir = new File (root.getAbsolutePath() + "/ExportData");
    	    dir.mkdirs();
    	    if (accessType.equals("Bla1"))
			{
    	    	file = new File(dir, "ExportStuff-Bla1.csv");
			}
    	    
    	    FileOutputStream out   =   null;
    	    try
    	    {
    	    	out = new FileOutputStream(file);
    	    } 
    	    catch (FileNotFoundException e) {
    	        e.printStackTrace();
    	    }
    	    try
    	    {
    	    	out.write(combinedString.getBytes());
    	    }
    	    catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	    try
    	    {
    	    	out.close();
    	    }
    	    catch (IOException e) {
    	    	e.printStackTrace();
    	    }
    	    
    	}
    	Uri u1  =   null;
    	u1  =   Uri.fromFile(file);

    	Intent sendIntent = new Intent(Intent.ACTION_SEND);
    	if (accessType.equals("Bla1"))
		{
    		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Exported Data: Bla1");
    		sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello,\n\nPlease find the attached comma-separated values (CSV) file. This file can be opened by any common spreadsheet application (such as Microsoft Excel) and it contains the data you exported from " + getString(R.string.app_name) + ".");
		}
    	
    	sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
    	sendIntent.setType("text/html");
    	startActivity(sendIntent);
    }
    
    public void getCheckedItems()
    {
    	checkmarkedIDS.clear();
    	int numItems = lv.getChildCount();
    	for (int i = 0; i < numItems; i++)
    	{
    		View item = lv.getChildAt(i);
    		CheckBox chkBox = (CheckBox)item.findViewById(R.id.checkBox);
    		if(chkBox.isChecked())
    		{
    			checkmarkedIDS.add(i);
    		}
    	}
    }
    
    public void setTotalBackgroundColor()
    {
    	int numItems = lv.getChildCount();
    	for (int i = 0; i < numItems; i++)
    	{
    		View item = lv.getChildAt(i);
    		TextView total = (TextView)item.findViewById(R.id.bottomData);
    		// Get the value out of the string
    		String[] totalRequiredStr = total.getText().toString().split(" ", 2);
    		if(Double.valueOf(totalRequiredStr[0]) < 0)
    		{
    			//Toast.makeText(ListViewActivity.this, "Changing color to red", Toast.LENGTH_SHORT).show();
    			RelativeLayout newBackground = (RelativeLayout)item.findViewById(R.id.bottom);
    			newBackground.setBackgroundResource(R.color.red_background);
    		}
    	}
    }
    
 // Load all feedstuffs from the database
    public void setupListView()
	{
    	adapter = null;
    	
    	String[] allColumns = null;
    	String[] dataColumns = null;
    	int[] layoutIds = null;		 
		Cursor c = null;
		
		try
		{
			if (accessType.equals("Options"))
			{
				windowTitle.setText("Options");
				c = (Cursor) DatabaseTableOptions.get(new String[] { DatabaseTableOptions.COLUMN_ID, DatabaseTableOptions.COLUMN_OPTION_NAME }, null, null, null);
				listIDS.clear();
				checkmarkedIDS.clear();
				allColumns = new String[] {	"_id", "topLeftLabel", "topLeftData" };
		    	dataColumns = new String[] {	"topLeftLabel", "topLeftData" };
		    	layoutIds = new int[] { R.id.leftTopLable, R.id.leftTopData };
			}
			else if (accessType.equals("Data"))
			{
				windowTitle.setText("Data");
				c = (Cursor) DatabaseTableData.get(new String[] { DatabaseTableData.COLUMN_ID, DatabaseTableData.COLUMN_NAME, DatabaseTableData.COLUMN_VALUE, DatabaseTableData.COLUMN_DESCRIPTION }, null, null, null);
				listIDS.clear();
				checkmarkedIDS.clear();
				allColumns = new String[] {	"_id", "topLeftLabel", "topLeftData", "topRightLabel", "topRightData", "bottomLeftLabel", "bottomLeftData", "bottomRightLabel", "bottomRightData" };
		    	dataColumns = new String[] {	"topLeftLabel", "topLeftData", "topRightLabel", "topRightData", "bottomLeftLabel", "bottomLeftData", "bottomRightLabel", "bottomRightData" };
		    	layoutIds = new int[] { R.id.leftTopLable, R.id.leftTopData, R.id.rightTopLable, R.id.rightTopData, R.id.leftBottomLable, R.id.leftBottomData, R.id.rightBottomLable, R.id.rightBottomData };
			}
			else
			{
				finish();
			}
					
			mCursor = new MatrixCursor(allColumns);
			if(c != null)
			{
				if(c.moveToFirst())
				{
					columnData = "";
					do
					{
						listIDS.add(c.getInt(c.getColumnIndex("_id")));
						if (accessType.equals("Options"))
						{
							mCursor.addRow(new Object[] {
									c.getInt(c.getColumnIndex(DatabaseTableOptions.COLUMN_ID)), 
									"Label1", c.getString(c.getColumnIndex(DatabaseTableOptions.COLUMN_OPTION_NAME))
							});
							
						}
						else if (accessType.equals("Data"))
						{
							mCursor.addRow(new Object[] {
									c.getInt(c.getColumnIndex(DatabaseTableData.COLUMN_ID)), 
									"Name", c.getString(c.getColumnIndex(DatabaseTableData.COLUMN_NAME)),
									"Value", c.getString(c.getColumnIndex(DatabaseTableData.COLUMN_VALUE)),
									"Description", c.getString(c.getColumnIndex(DatabaseTableData.COLUMN_DESCRIPTION)),
									"ID", c.getString(c.getColumnIndex(DatabaseTableData.COLUMN_ID))
							});
							
						}
					}while(c.moveToNext());
				}	 		
			}
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(c != null)
			{
				c = null;
			}
		}
		
		if (accessType.equals("Options"))
		{
			adapter = new SimpleCursorAdapter(this, R.layout.list_item, mCursor, dataColumns, layoutIds);
		}
		else if (accessType.equals("Data"))
		{
			adapter = new SimpleCursorAdapter(this, R.layout.list_item, mCursor, dataColumns, layoutIds);
		}

		lv.setAdapter(adapter);
	}
    
    // Delete the checkmarked records
 	public void deleteRecord()
 	{
 		int count = checkmarkedIDS.size();
 		
 		// Step through the checked items and delete them
 		for (int index = 0; index < count; index++) 
 		{
 			if (accessType.equals("Data"))
 			{
 				DatabaseTableData.delete(DatabaseTableData.COLUMN_ID + " = ?" , new String[] { "" + listIDS.get(checkmarkedIDS.get(index)) });
 			}
 			//db.deleteItem(accessType, listIDS.get(checkmarkedIDS.get(index)));
 			//Toast.makeText(getBaseContext(), "Delete" + checkmarkedIDS.get(index), Toast.LENGTH_LONG).show();
 		}
 		
 		// Resetup the list
 		setupListView();
 	}
    
    // Error messages
	public Dialog onCreateDialog(String error, int id)
	{
	 AlertDialog.Builder builder =null;
	 AlertDialog alert = null;
		switch(id)
		{
		case 1: // Click OK -- no action
			
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
			
		case 2: // Click Yes or No for delete
			
			builder = new AlertDialog.Builder(this);
			builder.setMessage(error)       
			.setCancelable(false)       
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) 
				{
					deleteRecord();
					checkmarkedIDS.clear();
					
				}      
			}) 
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)
				{
					//inventoryAdapter.clearChoices();
					dialog.cancel(); 
				}       
			});
			alert = builder.create();
			alert.show();
			break;
		
		case 3: // Click OK -- return to previous view
			
			builder = new AlertDialog.Builder(this);
			builder.setMessage(error)       
			.setCancelable(false)       
			.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{
					finish();
				}      
			});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}
	
	// When the view receives focus after user has navigated back check the reset flag and re-setup the list
	@Override
	public void onResume()
	{
		super.onResume();
		setupListView();
	}
}
