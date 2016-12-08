package watmok.tacoma.uw.edu.mylogin;

/**
 * Created by numb3 on 11/5/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * This class creates and edits the database created by SQLite
 */
public class FavoritesDataBaseAdapter {
    //String DATABASE name
    static final String DATABASE_NAME = "SavedHikes.db";

    //Version of Database which is set to 1
    static final int DATABASE_VERSION = 1;


    static final String DATABASE_CREATE = "create table " + "SAVEDHIKES" + "( "
            + "NAME text); ";
    public SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;

    /**
     * This creates a new database using the DataBaseHelper class
     * @param _context
     */
    public FavoritesDataBaseAdapter(Context _context) {
        context = _context;

        //Creates Database via DataBaseHelper
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    /**
     * When called, this method opens up the database to make it readily available to the user
     * to access when logging in or registering
     * @return the database
     * @throws SQLException
     */
    public FavoritesDataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * When called, this closes the database
     */
    public void close() {
        db.close();
    }

    /**
     * This code creates a new entry with the name of the Trail that is provided
     * @param name the Trail name being added to SAVEDHIKES
     */
    public void insertEntry(String name) {
        ContentValues newValues = new ContentValues();
        newValues.put("NAME", name);
        db.insert("SAVEDHIKES", null, newValues);

    }

    /**
     * This deletes the desired entry
     * @param name the entry you want to delete
     * @return the number of entries deleted (should be 1)
     */
    public int deleteEntry(String name) {

        String where = "NAME=?";
        int numberOFEntriesDeleted = db.delete("SAVEDHIKES", where,
                new String[] { name });
        return numberOFEntriesDeleted;
    }

    /**
     * When called with the Trail name, this method searches the database to see if the trail name
     * has already been added to the SAVEDHIKES database or if it does not exist in the database.
     * It then returns either the name of the Trail or the String NOT EXIST
     * @param name
     * @return name that is found or NOT EXIST if can't find in database
     */
    public String getSingleEntry(String name) {
        Cursor cursor = db.query("SAVEDHIKES", null, "NAME=?",
                new String[] { name }, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String uname = cursor.getString(cursor.getColumnIndex("NAME"));
        cursor.close();
        return uname;
    }

    /**
     * This method, when called, creates an ArrayList of all the Trail names in the database
     * @return an ArrayList of all the names in the database
     */
    public ArrayList<String> getAllEntries(){
        String selectQuery = "SELECT  * FROM SAVEDHIKES";
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery,null);
        try {

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                     String obj="";
                    //only one column
                    obj=cursor.getString(0);
                    list.add(obj);
                } while (cursor.moveToNext());
            }

        } finally {
            try { cursor.close(); } catch (Exception ignore) {}
        }

        return list;
    }
}