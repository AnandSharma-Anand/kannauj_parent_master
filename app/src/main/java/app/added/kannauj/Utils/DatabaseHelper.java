package app.added.kannauj.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import app.added.kannauj.Models.StudentModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "parents_db";

    // Table Name
    private static final String TABLE_NAME = "students";

    // Table Name
    private static final String TABLE_NAME_EXAM = "exams";

    private static final String COLUMN_EXAM_ID = "exam_id";
    private static final String COLUMN_EXAM_DURATION = "duration";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_NAME = "student_name";
    private static final String COLUMN_PHOTO = "student_photo";
    private static final String COLUMN_CLASS = "student_class";
    private static final String COLUMN_CLASS_SECTION_ID = "student_class_section_id";
    private static final String COLUMN_CLASS_ID = "student_class_id";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_STUDENT_ID + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PHOTO + " TEXT,"
                    + COLUMN_CLASS + " TEXT,"
                    + COLUMN_CLASS_SECTION_ID + " TEXT,"
                    + COLUMN_CLASS_ID + " TEXT"
                    + ")";

    // Create table SQL query
    public static final String CREATE_TABLE_EXAM =
            "CREATE TABLE " + TABLE_NAME_EXAM + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EXAM_ID + " TEXT,"
                    + COLUMN_EXAM_DURATION + " TEXT"
                    + ")";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_EXAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXAM);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public int deleteAllStudents() {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from "+ TABLE_NAME);
        return db.delete(TABLE_NAME, null, null);
    }

    public long addStudent(StudentModel model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id`  will be inserted automatically.
        // no need to add them
        values.put(COLUMN_STUDENT_ID, model.getId());
        values.put(COLUMN_NAME, model.getName());
        values.put(COLUMN_CLASS, model.getClassName());
        values.put(COLUMN_CLASS_SECTION_ID, model.getClassSectionId());
        values.put(COLUMN_PHOTO, model.getProfileImage());

        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<StudentModel> getAllStudents() {
        List<StudentModel> students = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentModel model = new StudentModel(
                        cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                                "","",
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO))
                );
                model.setClassName(cursor.getString(cursor.getColumnIndex(COLUMN_CLASS)));
                model.setClassSectionId(cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_SECTION_ID)));

                students.add(model);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return students;
    }

    public int getExamDuration(String id) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EXAM + " WHERE id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EXAM_DURATION)));
        } else
            return -1;
    }

    public int addExamDuration(String id, String duration) {
        int tempDuration = 0;
        if (getExamDuration(id)==-1) {
            addExam(id, duration);
        } else {
            tempDuration = getExamDuration(id);
        }
        return tempDuration;
    }

    public void addExam(String id, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXAM_ID, id);
        values.put(COLUMN_EXAM_DURATION, duration);
    }

    public void updateExamDuration(String id, String duration) {
        String strSQL = "UPDATE "+ TABLE_NAME_EXAM +" SET duration = "+ duration +" WHERE "+ COLUMN_EXAM_ID +" = "+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
    }


}
