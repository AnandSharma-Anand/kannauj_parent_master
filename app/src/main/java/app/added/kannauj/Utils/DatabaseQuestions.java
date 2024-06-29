package app.added.kannauj.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import app.added.parents.Models.ExamQuestionModel;


public class DatabaseQuestions extends SQLiteOpenHelper {

    private static String DB_NAME = "question_db";
    private static int DB_VERSION = 5;
    private SQLiteDatabase db;
    public static final String QUESTION_TABLE = "question_tb";
    public static final String COLUMN_ID = "cid";
    public static final String QID = "id";
    public static final String ANS = "ans";
    public static final String PID = "pid";

    public DatabaseQuestions(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        String exe = "CREATE TABLE IF NOT EXISTS " + QUESTION_TABLE
                + "(" + COLUMN_ID + " integer primary key AUTOINCREMENT, "
                + QID + " TEXT NOT NULL, "
                + ANS + " TEXT NOT NULL, "
                + PID + " TEXT NOT NULL"
                + ")";

        db.execSQL(exe);

    }

    public void setAns(String qid, String pid, String ans) {
        db = getWritableDatabase();
        if (getQuest(pid, qid)) {
            db.execSQL("update " + QUESTION_TABLE + " set " + ANS + " = '" + ans + "' where " + PID + " = " + pid + " AND " + QID + " = " + qid);
        }
    }

 /*   public void setQuestions(ArrayList<ExamQuestionModel> models, String pid) {
        db = getWritableDatabase();
        for (int i = 0; i < models.size(); i++) {
            if (getQuest(pid, models.get(i).getId())) {
                System.out.println("Animesh3747");
                return;
            } else {
                System.out.println("Animesh178787");
                ContentValues values = new ContentValues();
                values.put(PID, pid);
                values.put(QID, models.get(i).getId());
                values.put(ANS, "0");
                db.insert(QUESTION_TABLE, null, values);
            }
        }
    }
*/
    public boolean getQuest(String pid, String qid) {
        db = getReadableDatabase();
        String qry = "Select *  from " + QUESTION_TABLE + " where " + QID + " = " + qid + " AND " + PID + " = " + pid;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;

        return false;
    }

    public String getAns(String pid, String qid) {
        if (getQuest(pid, qid)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + QUESTION_TABLE + " where " + PID + " = " + pid + " AND " + QID + " = " + qid;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(ANS));
        } else {
            return "0";
        }
    }

    public void removeAllQuestions(String pid) {
        db = getReadableDatabase();
        db.execSQL("delete from " + QUESTION_TABLE + " where " + PID + " = " + PID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
