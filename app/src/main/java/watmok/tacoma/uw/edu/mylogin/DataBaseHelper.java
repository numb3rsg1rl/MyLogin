package watmok.tacoma.uw.edu.mylogin;

/**
 * Created by numb3 on 11/5/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This creates and updates the database ONLY
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    /**
     * This creates a database based on the input given by the
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DataBaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    /**
     * Creates the database by using the execSQL
     * @param _db
     */
    @Override
    public void onCreate(SQLiteDatabase _db) {
        _db.execSQL(FavoritesDataBaseAdapter.DATABASE_CREATE);

    }

    /**
     * Upgrades the Database using the specified version given, and recreates the database.
     * Is not implemented in the code, but must be implemented in DatabaseHelper
     * @param _db
     * @param _oldVersion
     * @param _newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion + " to "
                + _newVersion + ", which will destroy all old data");
        _db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");

        onCreate(_db);
    }

}
