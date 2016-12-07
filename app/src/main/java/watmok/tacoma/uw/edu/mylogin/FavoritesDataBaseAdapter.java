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
    static final String DATABASE_NAME = "SavedHikes.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
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
     * This returns the database itself (not used in the code at all!)
     * @return
     */
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    /**
     * This code creates a new entry with the password and username that the user provides
     * @param name
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
     * When called with the desired username, this method returns the password of the entry
     * @param name
     * @return password of that username
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

                    //you could add additional columns here..

                    list.add(obj);
                } while (cursor.moveToNext());
            }

        } finally {
            try { cursor.close(); } catch (Exception ignore) {}
        }

        return list;
    }
}