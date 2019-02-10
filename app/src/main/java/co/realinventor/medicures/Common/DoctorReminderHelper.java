package co.realinventor.medicures.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DoctorReminderHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "DOCVISITS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_HOUR = "HOUR";
    public static final String COLUMN_MINUTE = "MINUTE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_DOCTOR = "DOCTOR";
    public static final String COLUMN_TONE = "TONE";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alarm_app.db";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOUR + " TEXT NOT NULL, " +
                    COLUMN_MINUTE + " TEXT NOT NULL, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_DOCTOR + " TEXT, " +
                    COLUMN_TONE + " TEXT);";

    public DoctorReminderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
