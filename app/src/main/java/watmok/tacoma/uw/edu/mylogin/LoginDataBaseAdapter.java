package watmok.tacoma.uw.edu.mylogin;

/**
 * Created by numb3 on 11/5/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class creates and edits the database created by SQLite
 */
public class LoginDataBaseAdapter {
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    static final String DATABASE_CREATE = "create table " + "LOGIN" + "( "
            + "ID" + " integer primary key autoincrement,"
            + "USERNAME  text,PASSWORD text); ";
    public SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;

    /**
     * This creates a new database using the DataBaseHelper class
     * @param _context
     */
    public LoginDataBaseAdapter(Context _context) {
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
    public LoginDataBaseAdapter open() throws SQLException {
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
     * @param userName
     * @param password
     */
    public void insertEntry(String userName, String password) {
        ContentValues newValues = new ContentValues();
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD", password);
        db.insert("LOGIN", null, newValues);

    }

    /**
     * This deletes the desired entry
     * @param UserName the entry you want to delete
     * @return the number of entries deleted (should be 1)
     */
    public int deleteEntry(String UserName) {

        String where = "USERNAME=?";
        int numberOFEntriesDeleted = db.delete("LOGIN", where,
                new String[] { UserName });
        return numberOFEntriesDeleted;
    }

    /**
     * When called with the desired username, this method returns the password of the entry
     * @param userName
     * @return password of that username
     */
    public String getSingleEntry(String userName) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?",
                new String[] { userName }, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    /**
     * updateEntry updates the username and password of the desired account. Not used yet.
     * @param userName
     * @param password
     */
    public void updateEntry(String userName, String password) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD", password);

        String where = "USERNAME = ?";
        db.update("LOGIN", updatedValues, where, new String[] { userName });
    }
}