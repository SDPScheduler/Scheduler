package nz.ac.aut.sdp.scheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jack on 15/05/15.
 */
public class DBAdapter {
    public static final String ID = "id";
    public static final String EVENT_NAME = "eventName";
    public static final String DATE = "date";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String NOTES = "notes";

    public static final String DATABASE_NAME = "EventsDB";
    public static final String DATABASE_TABLE = "events";

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE = "create table if not exists " + DATABASE_TABLE + " (" +
            ID + " integer primary key auto-increment, " + EVENT_NAME + " VARCHAR not null, " + DATE +
            " VARCHAR not null, " + START_TIME + " integer not null, " + END_TIME + " integer not null, " +
            NOTES + " VARCHAR " + ");";


    private final Context context;
    private DatabaseHelper DBHelper;
    private static SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertRecord(String name, String date, int startTime, int endTime, String notes) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(EVENT_NAME, name);
        initialValues.put(DATE, date);
        initialValues.put(START_TIME, startTime);
        initialValues.put(END_TIME, endTime);
        initialValues.put(NOTES, notes);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteEvent(long rowId) {
        return db.delete(DATABASE_TABLE, ID + "=" + rowId, null) > 0;
    }


    public Cursor getAllRecords() {

        return db.query(DATABASE_TABLE, new String[]{ID, EVENT_NAME, DATE, START_TIME, END_TIME, NOTES},
                null, null, null, null, null, null);
    }

    public Cursor getRecord(long rowId) throws SQLException {

        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{ID, EVENT_NAME, DATE, START_TIME, END_TIME, NOTES },
                                                                ID + "=" + rowId, null, null, null, null, null, null);

        if (mCursor != null) {

            mCursor.moveToFirst();

        }

        return mCursor;
    }


    public boolean updateRecord(long rowId, String eventName, String date, int startTime, int endTime, String notes) {
        ContentValues args = new ContentValues();
        args.put(EVENT_NAME, eventName);
        args.put(DATE, date);
        args.put(START_TIME, startTime);
        args.put(END_TIME, endTime);
        args.put(NOTES, notes);
        return db.update(DATABASE_TABLE, args, ID + "=" + rowId, null) > 0;
    }

}


