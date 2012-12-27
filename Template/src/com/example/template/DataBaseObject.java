/*
 * Created By : Daniel Jamison
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseObject 
{
	//Initialize properties of the class
	private static String DATABASE_NAME = "data";
	private SQLiteDatabase db = null; 
	private static final String TAG = "TemplateDBAdapter";
	private final Context context;
	private static final int DATABASE_VERSION = 3;
	private DatabaseHelper databaseHelper;
	
	// In the options menu the default value should always be zero
	private static final String CREATE_TABLE_Options = "CREATE TABLE Options (_id INTEGER PRIMARY KEY  NOT NULL , optionName VARCHAR, value DOUBLE DEFAULT (0))";
	private static final String INSERT_TABLE_Options = "INSERT INTO Options(_id, optionName,value) values ";
	private static final String[] Options = {
		"(0,'Hide disclaimer',0)", // 0 = false, 1 = true
		"(1,'Unit set',0)" // 0 = us standard, 1 = metric, 2 = uk
	};
		
	private static final String CREATE_TABLE_Data1 = "CREATE TABLE Data1 (_id INTEGER PRIMARY KEY  NOT NULL, name VARCHAR, dataStuff INTEGER)";
	
	
	//##########################################################################################
	
	private static class DatabaseHelper extends SQLiteOpenHelper 
	{
	    public DatabaseHelper(Context context) 
	    {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) 
	    {
	    	db.execSQL("DROP TABLE IF EXISTS Options");
   
			db.execSQL(CREATE_TABLE_Options);
	
			db.beginTransaction();
			try
			{
				for(String newLine : Options)
				{
					db.execSQL(INSERT_TABLE_Options + newLine);
				}
				
				db.setTransactionSuccessful();
			}
			finally
			{
				db.endTransaction();
			}
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			int currentVersion = oldVersion;
			
			switch (oldVersion){
			// sample update format only
				case 1:
					Log.w(TAG, "Upgrading database from version " + currentVersion + " to " + ++currentVersion + ", no data will be lost");
					db.execSQL("DROP TABLE IF EXISTS FeedstuffType");
					db.execSQL("DROP TABLE IF EXISTS LivestockType");
					db.execSQL("DROP TABLE IF EXISTS NeedsOld");
					
					db.execSQL(CREATE_TABLE_FeedstuffType);
					for(String newLine : DefaultFeedstuffType)
					{
						db.execSQL(INSERT_TABLE_FeedstuffType + newLine);
					}
					
					db.execSQL(CREATE_TABLE_LivestockType);
					for(String newLine : DefaultLivestockType)
					{
						db.execSQL(INSERT_TABLE_LivestockType + newLine);
					}
					
					db.execSQL("ALTER TABLE Needs RENAME TO NeedsOld");
					db.execSQL("CREATE TABLE Needs (_id INTEGER PRIMARY KEY  NOT NULL ,feedstuff INTEGER,livestock INTEGER,quantityPerDay DOUBLE DEFAULT (0),numDays INTEGER)");
					db.execSQL("INSERT INTO Needs SELECT _id,feedstuff,livestock,quantityPerDay,numDays FROM NeedsOld");
					db.execSQL("DROP TABLE IF EXISTS NeedsOld");
					// No Break Statement so that the DB will perform all updates required to go from the current version to the newest version
				case 2: 
					Log.w(TAG, "Upgrading database from version " + currentVersion + " to " + ++currentVersion + ", no data will be lost");
					// Remove "other fields"
					db.execSQL("delete from FeedstuffUse  where _id = 0 ");
					db.execSQL("delete from DefaultFeedstuff  where _id = 20 ");
					db.execSQL("delete from DefaultFeedstuff  where _id = 33 ");
					db.execSQL("delete from DefaultFeedstuff  where _id = 55 ");				
			}
        }
    }
	
	// Used for testing an update before applying it
	public void testupdate()
	{
		open();
		Log.w(TAG, "Upgrading database no data will be lost");
		db.beginTransaction();
		try
		{
			db.execSQL("ALTER TABLE Needs ADD COLUMN totRequired DOUBLE DEFAULT (0)");
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		close();
	}
	
    public DataBaseObject(Context ctx) {
        this.context = ctx;
    }
    
    public DataBaseObject open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    // Get the value of an option in the database
 	public void changeUnitSystem(String type)
 	{
 		open();
 		
 		Cursor c = null;
		try
    	{
			c = db.rawQuery("select _id,unit,label,scaler from UnitSystem where system = "+ type +" ", null);
    	}
		catch(SQLException sqle)
    	{
			
    		throw sqle;
    	}
		
		db.beginTransaction();
		
		try
		{
			if(c.getCount() != 3)
			{
				
				return;
			}
			if(c.moveToFirst())
			{
				int i = 1;
				do
				{
					db.execSQL("Update Unit set unit = '"+c.getString(c.getColumnIndex("unit"))+"', label = '"+c.getString(c.getColumnIndex("label"))+"', scaler = "+c.getDouble(c.getColumnIndex("scaler"))+" where _id = "+i+" ");
					i++;
				}while(c.moveToNext());
				
			}
			db.setTransactionSuccessful();
		}
		finally
		{
			  db.endTransaction();
		}
 	}
    
	// Get unit information
	public Cursor getUnit(int type)
	{
		open();
		if (type == 0)
		{
			try
	    	{
				Cursor c = db.rawQuery("select distinct _id,unit,label,scaler from Unit", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
		else
		{
			try
	    	{
				Cursor c = db.rawQuery("select _id,unit,label,scaler from Unit where _id = "+ type +" ", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
	}
	
	// Get the unit name
	public String getUnitName(int unit)
	{
		open();
		String value = "";
	
		Cursor c = db.rawQuery("select _id,unit from Unit where _id = '"+ unit +"' ", null);
		if(c.getCount()!=1)
		{
			return "";
		}
		if(c.moveToFirst())
		{
			do
			{
				value = decodeCharString(c.getString(c.getColumnIndex("unit")));
			}while(c.moveToNext());
		}
		return value;
	}
	
	// Get the unit label
	public String getUnitLabel(int unit)
	{
		open();
		String value = "";
	
		Cursor c = db.rawQuery("select _id,label from Unit where _id = '"+ unit +"' ", null);
		if(c.getCount()!=1)
		{
			return "";
		}
		if(c.moveToFirst())
		{
			do
			{
				value = decodeCharString(c.getString(c.getColumnIndex("label")));
			}while(c.moveToNext());
		}
		return value;
	}
	
	// Get the value of a unit
	public int getUnitValue(String unit)
	{
		unit = encodeCharString(unit);
		open();
		int value = 0;
	
		Cursor c = db.rawQuery("select _id from Unit where unit = '"+ unit +"' ", null);
		if(c.getCount()!=1)
		{
			return 0;
		}
		if(c.moveToFirst())
		{
			do
			{
				value = c.getInt(c.getColumnIndex("_id"));
			}while(c.moveToNext());
		}
		return value;
	}
	
	// Get unit conversion scaler
	public double getUnitScaler(int type)
	{
		open();
		// Zero is the default option value if there is an error returns the default
		double value = 0;
		try
    	{
			Cursor c = db.rawQuery("select _id,scaler from Unit where _id = "+ type +" ", null);
			if(c.getCount()==0)
			{
				
				return value;
			}
			if(c.moveToFirst())
			{
				do
				{
					value = c.getInt(c.getColumnIndex("scaler"));
				}while(c.moveToNext());
			}
    	}
		catch(SQLException sqle)
    	{
    		throw sqle;
    	}
		
		return value;
	}
		
	// Update an option in the database
	public void upateOption(int _id,double value)
	{
		open();
		db.beginTransaction();
		
		try
		{
			db.execSQL("Update Options set value = "+value+" where _id = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			  db.endTransaction();
		}
		
	}
	
	// Get the value of an option in the database
	public Double getOption(int _id)
	{
		open();
		// Zero is the default option value if there is an error returns the default
		double value = 0;
		try
    	{
			Cursor c = db.rawQuery("select optionName, value from Options where _id = "+_id+" ", null);
			if(c.getCount()==0)
			{
				
				return value;
			}
			if(c.moveToFirst())
			{
				do
				{
					value = c.getDouble(c.getColumnIndex("value"));
				}while(c.moveToNext());
			}
    	}
		catch(SQLException sqle)
    	{
    		throw sqle;
    	}
		
		return value;
	}
	
	public Cursor getFeedstuffType(int type)
	{
		open();
		if (type == 0)
		{
			try
	    	{
				Cursor c = db.rawQuery("select distinct _id,feedstuffType from FeedstuffType", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
		else
		{
			try
	    	{
				Cursor c = db.rawQuery("select _id,feedstuffType from FeedstuffType where _id = "+ type +" ", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
	}
	
	public Cursor getDefaultFeedstuff(int type)
	{
		open();
		if (type == 0)
		{
			try
	    	{
				Cursor c = db.rawQuery("select distinct _id,feedstuffType,feedstuff,unit,conversion from DefaultFeedstuff", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
		else
		{
			try
	    	{
				Cursor c = db.rawQuery("select _id,feedstuffType,feedstuff,unit,conversion from DefaultFeedstuff where feedstuffType = "+ type +" ", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
	}
	
	public int getDefaultFeedstuffValue(String feedstuff)
	{
		feedstuff = encodeCharString(feedstuff);
		
		open();
		int value = 0;
	
		Cursor c = db.rawQuery("select _id from DefaultFeedstuff where feedstuff = '"+ feedstuff +"' ", null);
		if(c.getCount()!=1)
		{
			return 0;
		}
		if(c.moveToFirst())
		{
			do
			{
				value = c.getInt(c.getColumnIndex("_id"));
			}while(c.moveToNext());
		}
		return value;
	}

	public Cursor getFeedstuffUse(int type)
	{
		open();
		if (type == 0)
		{
			try
	    	{
				Cursor c = db.rawQuery("select distinct _id,feedstuffType,use from FeedstuffUse", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
		else
		{
			try
	    	{
				Cursor c = db.rawQuery("select _id,feedstuffType,use from FeedstuffUse where feedstuffType = "+ type +" OR feedstuffType = 0 ", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
	}
	
	public int getUseValue(String use)
	{
		use = encodeCharString(use);
		
		open();
		int value = 0;
	
		Cursor c = db.rawQuery("select _id from FeedstuffUse where use = '"+ use +"' ", null);
		if(c.getCount()!=1)
		{
			return 0;
		}
		if(c.moveToFirst())
		{
			do
			{
				value = c.getInt(c.getColumnIndex("_id"));
			}while(c.moveToNext());
		}
		return value;
	}
	
	// Get all the data for a single feedstuff
	public Cursor getFeedstuff(int _id)
	{
		open();
		Cursor c = db.rawQuery("select _id,feedstuffType,feedstuff,use,quantity,unit,conversion,quality,valuePer,loc,note,usedPerDay,usedTotal from Feedstuff where _id = "+_id+" ", null);
		if(c.getCount()==0)
		{
			return null;
		}
		
		return c;
	}

	// Insert a single feedstuff into the database
	public void insertFeedstuff(int feedstuffType, String feedstuff, String use, double quantity, int unit, double conversion, String quality, double valuePer, String loc, String note)
	{
		feedstuff = encodeCharString(feedstuff);
		use = encodeCharString(use);
		quality  = encodeCharString(quality);
		loc = encodeCharString(loc);
		note  = encodeCharString(note);
		
		open();
		db.beginTransaction();
		try
		{ 
			db.execSQL("INSERT INTO Feedstuff (feedstuffType,feedstuff,use,quantity,unit,conversion,quality,valuePer,loc,note) VALUES ("+feedstuffType+", '"+feedstuff+"', '"+use+"', "+quantity+", "+ unit +", "+ conversion +", '"+ quality +"', "+ valuePer +", '"+ loc +"', '"+ note +"')");
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
		
	}

	// Update a feedstuff in the database
	public void updateFeedstuff(int _id, int feedstuffType, String feedstuff, String use, double quantity, int unit, double conversion, String quality, double valuePer, String loc, String note)
	{
		feedstuff = encodeCharString(feedstuff);
		use = encodeCharString(use);
		quality  = encodeCharString(quality);
		loc = encodeCharString(loc);
		note  = encodeCharString(note);
		
		db.beginTransaction();
		
		try
		{
			db.execSQL("Update Feedstuff set feedstuffType = "+feedstuffType+", feedstuff = '"+feedstuff+"', use = '"+use+"', quantity = "+quantity+", unit = "+ unit +", conversion = "+ conversion +", quality = '"+ quality +"', valuePer = "+ valuePer +", loc = '"+ loc +"', note = '"+ note +"' where _id = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			  db.endTransaction();
		}
	}
	
	public void clearUsedFeedstuff(int _id)
	{
		db.beginTransaction();
		
		try
		{
			db.execSQL("Update Feedstuff set usedPerDay = 0, usedTotal = 0 where _id = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			  db.endTransaction();
		}
	}
	
	public void updateUsedFeedstuff(int _id, double usedPerDay, double usedTotal)
	{
		db.beginTransaction();
		
		try
		{
			db.execSQL("Update Feedstuff set usedPerDay = usedPerDay + "+usedPerDay+", usedTotal = usedTotal + "+usedTotal+" where _id = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			  db.endTransaction();
		}
	}

	// Get a list of all feedstuffs for the main view ordered so the newest is on top
	public Cursor getAllFeedstuff()
	{
		Cursor c = db.rawQuery("select distinct _id,feedstuff,feedstuffType,use,quantity,unit,conversion,quality,valuePer,loc,note from Feedstuff order by _id DESC", null);
		if(c.getCount()==0)
		{
			return null;
		}
		return c;
	}
	
	// Delete a single feedstuff from the database
	public void deleteFeedstuff(int _id)
	{
		db.beginTransaction();
		try
		{
			db.execSQL("delete from Feedstuff  where _id = "+_id+" ");
			db.execSQL("delete from Needs  where feedstuff = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
	}
	
	// Delete a single livestock from the database
	public void deleteLivestock(int _id)
	{
		db.beginTransaction();
		try
		{
			db.execSQL("delete from Livestock where _id = "+_id+" ");
			db.execSQL("delete from Needs  where livestock = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
	}
	
	// Delete a single need from the database
	public void deleteNeed(int _id)
	{
		db.beginTransaction();
		try
		{
			db.execSQL("delete from Needs  where _id = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
	}
	
	public Cursor getLivestockType(int type)
	{
		open();
		if (type == 0)
		{
			try
	    	{
				Cursor c = db.rawQuery("select distinct _id,livestockType from LivestockType", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
		else
		{
			try
	    	{
				Cursor c = db.rawQuery("select _id,livestockType from LivestockType where _id = "+ type +" ", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
	}
	
	public Cursor getLivestockSpecies(int type)
	{
		open();
		
		try
    	{
			Cursor c = db.rawQuery("select  _id,livestockType,species from DefaultLivestock where livestockType = "+ type +" GROUP BY species", null);
			
			return c;
    	}
		catch(SQLException sqle)
    	{
			
    		throw sqle;
    	}
	}
		
	public Cursor getLivestockPhase(int type, String species)
	{
		species = encodeCharString(species);
		open();
		
		try
    	{
			Cursor c = db.rawQuery("select _id,livestockType,species,phase from DefaultLivestock where livestockType = "+ type +" AND species = '" + species + "' ", null);
			
			return c;
    	}
		catch(SQLException sqle)
    	{
			
    		throw sqle;
    	}
	}
	
	public Cursor getDefaultLivestock(int type)
	{
		open();
		if (type == 0)
		{
			try
	    	{
				Cursor c = db.rawQuery("select distinct _id,feedstuffType,feedstuff,unit,conversion from DefaultFeedstuff", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
		else
		{
			try
	    	{
				Cursor c = db.rawQuery("select _id,feedstuffType,feedstuff,unit,conversion from DefaultFeedstuff where feedstuffType = "+ type +" ", null);
				
				return c;
	    	}
			catch(SQLException sqle)
	    	{
				
	    		throw sqle;
	    	}
		}
	}
	
	// Get all the data for a single livestock
	public Cursor getLivestock(int _id)
	{
		open();
		Cursor c = db.rawQuery("select _id,livestockType,species,phase,quantity,bodyCondition,weight,valuePer,loc,note from Livestock where _id = "+_id+" ", null);
		if(c.getCount()==0)
		{
			return null;
		}
		
		return c;
	}
	
	// Insert a single livestock into the database
	public void insertLivestock(int livestockType, String species, String phase,int quantity, String bodyCondition, double weight, double valuePer, String loc, String note)
	{
		species = encodeCharString(species);
		phase = encodeCharString(phase);
		bodyCondition = encodeCharString(bodyCondition);
		loc  = encodeCharString(loc);
		note = encodeCharString(note);
		
		open();
		db.beginTransaction();
		try
		{ 
			db.execSQL("INSERT INTO Livestock (livestockType,species,phase,quantity,bodyCondition,weight,valuePer,loc,note) VALUES ("+livestockType+", '"+species+"', '"+phase+"', "+quantity+", '"+ bodyCondition +"', "+ weight +", "+ valuePer +", '"+ loc +"', '"+ note +"')");
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
		
	}

	// Update a livestock in the database
	public void updateLivestock(int _id, int livestockType, String species, String phase,int quantity, String bodyCondition, double weight, double valuePer, String loc, String note)
	{
		species = encodeCharString(species);
		phase = encodeCharString(phase);
		bodyCondition = encodeCharString(bodyCondition);
		loc  = encodeCharString(loc);
		note = encodeCharString(note);
		
		db.beginTransaction();
		
		try
		{
			db.execSQL("Update Livestock set livestockType = "+livestockType+", species = '"+species+"', phase = '"+phase+"', quantity = "+quantity+", bodyCondition = '"+ bodyCondition +"', weight = "+ weight +", valuePer = "+ valuePer +", loc = '"+ loc +"', note = '"+ note +"' where _id = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			  db.endTransaction();
		}
	}
	
	// Get a list of all livestock for the main view ordered so the newest is on top
	public Cursor getAllLivestock()
	{
		Cursor c = db.rawQuery("select distinct _id,species,livestockType,phase,quantity,bodyCondition,weight,valuePer,loc,note from Livestock order by _id DESC", null);
		if(c.getCount()==0)
		{
			return null;
		}
		return c;
	}
  
	// Get all the data for a single need
	public Cursor getNeeds(int _id)
	{
		open();
		Cursor c = db.rawQuery("select _id,feedstuff,livestock,quantityPerDay,numDays from Needs where _id = "+_id+" ", null);
		if(c.getCount()==0)
		{
			return null;
		}
		
		return c;
	}
	
	// Insert a single need into the database
	public void insertNeed(int feedstuff, int livestock, double quantityPerDay, int numDays)
	{
		open();
		db.beginTransaction();
		try
		{ 
			db.execSQL("INSERT INTO Needs (feedstuff,livestock,quantityPerDay,numDays) VALUES ("+feedstuff+", "+livestock+", "+quantityPerDay+", "+ numDays +")");
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
		
	}

	// Update a need in the database
	public void updateNeed(int _id, int feedstuff, int livestock, double quantityPerDay, int numDays)
	{
		db.beginTransaction();
		
		try
		{
			db.execSQL("Update Needs set feedstuff = "+feedstuff+", livestock = "+livestock+", quantityPerDay = "+quantityPerDay+", numDays = "+ numDays +" where _id = "+_id+" ");
			db.setTransactionSuccessful();
		}
		finally
		{
			  db.endTransaction();
		}
	}
	
	// Get a list of all needs for the main view ordered so the newest is on top
	public Cursor getAllNeeds()
	{
		Cursor c = db.rawQuery("select distinct _id,feedstuff,livestock,quantityPerDay,numDays from Needs order by _id DESC", null);
		if(c.getCount()==0)
		{
			return null;
		}
		return c;
	}

	
	// Closes databaseHelper
	public void close() 
	{  	
		databaseHelper.close(); 	
	}
	
	// Apostrophes cannot be in an SQLite database so remove them
	public String encodeCharString(String tempStr)
	{
		//return tempStr.replace("'", "&#39");
		return tempStr.replace("'", "");
	}
	
	// Apostrophes cannot be in an SQLite database so convert them back
	public String decodeCharString(String tempStr)
	{
		//return tempStr.replace("&#39", "'");
		return tempStr.replace("&#39", "");
	}
}
