package app.added.kannauj.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import app.added.parents.Models.SelectExamModel;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static String DB_NAME = "paper_db1";
    private static int DB_VERSION = 5;
    private SQLiteDatabase db;

    public static final String PAPER_TABLE = "paper";
    public static final String COLUMN_ID = "cid";
    public static final String COLUMN_PRID = "id";
    public static final String COLUMN_NAME = "TestName";
    public static final String COLUMN_DUR = "TestDuration";
    public static final String COLUMN_NEWDU = "TotalDuration";
    public static final String COMPLETED = "completed";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        String exe = "CREATE TABLE IF NOT EXISTS " + PAPER_TABLE
                + "(" + COLUMN_ID + " integer primary key AUTOINCREMENT, "
                + COLUMN_PRID + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_DUR + " TEXT NOT NULL, "
                + COMPLETED + " TEXT NOT NULL,"
                + COLUMN_NEWDU + " TEXT NOT NULL"
                + ")";

        db.execSQL(exe);

    }

    public void setDuration(String paper_id, String duration) {
        db = getWritableDatabase();
        if (getPaper(paper_id)) {
            db.execSQL("update " + PAPER_TABLE + " set " + COLUMN_NEWDU + " = '" + duration + "' where " + COLUMN_PRID + " = " + paper_id);
        }
    }

    public void setCompleted(String paper_id, String com) {
        db = getWritableDatabase();
        if (getPaper(paper_id)) {
            db.execSQL("update " + PAPER_TABLE + " set " + COMPLETED + " = '" + com + "' where " + COLUMN_PRID + " = " + paper_id);
        }
    }

/*    public void setPapers(ArrayList<SelectExamModel> models) {
        db = getWritableDatabase();
        for (int i = 0; i < models.size(); i++) {
            if (getPaper(models.get(i).getId())) {
                System.out.println("Animesh");
                System.out.println("Animesh Lalal" + models.get(i).getId());
            } else {
                System.out.println("Animesh1");
                System.out.println("Animesh Binama" + models.get(i).getId() + "   ----   " + models.get(i).getDuration());
                ContentValues values = new ContentValues();
                values.put(COLUMN_PRID, models.get(i).getId());
                values.put(COLUMN_NAME, models.get(i).getExamName());
                values.put(COLUMN_DUR, models.get(i).getDuration());
                values.put(COLUMN_NEWDU, models.get(i).getDuration());
                values.put(COMPLETED, "0");
                db.insert(PAPER_TABLE, null, values);
            }
        }
    }*/

    public boolean getPaper(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + PAPER_TABLE + " where " + COLUMN_PRID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;

        return false;
    }

    public String getDuration(String id) {
        if (getPaper(id)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + PAPER_TABLE + " where " + COLUMN_PRID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            int position=cursor.getColumnIndex(COLUMN_NEWDU);
            String dur = cursor.getString(position);
            return dur;
        } else {
            return "0";
        }
    }

    public String getCompleted(String id) {
        if (getPaper(id)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + PAPER_TABLE + " where " + COLUMN_PRID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            int completeIndex=cursor.getColumnIndex(COMPLETED);
            return cursor.getString(completeIndex);
        } else {
            return "0";
        }
    }


    public void removeItemFromPapers(String id) {
        System.out.println("Animesh Mihshhah" + id);
        db = getReadableDatabase();
        db.execSQL("delete from " + PAPER_TABLE + " where " + COLUMN_PRID + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
