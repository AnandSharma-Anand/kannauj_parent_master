package app.added.kannauj.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import app.added.kannauj.activities.LoginActivity;

public class Prefs {

    private static final String PRE_LOAD = "preLoad";
    private static final String PREFS_NAME = "prefs";
    private static final String UNIQUE_TOKEN = "token";

    //parent
    private static final String FATHER_NAME = "father_name";
    private static final String MOTHER_NAME = "mother_name";
    private static final String PHONE_NUMBER = "phone";
    private static final String MOBILE_NUMBER1 = "mobile1";
    private static final String MOBILE_NUMBER2 = "mobile2";
    private static final String DOB = "DOB";
    // student
    private static final String NAME = "name";
    private static final String STUDENT_ID = "studentId";
    private static final String STUDENT_PHOTO = "studentPhoto";
    private static final String STUDENT_CLASS = "studentClass";
    private static final String CLASS_ID = "class_id";
    private static final String CLASS_SECTION_ID = "class_section_id";


    private static final String LOGIN_STATE = "isLoggedIn";
    private static final String CLOCK_IN = "clock_in";
    private static final String CLOCK_OUT = "clock_out";
    private static final String DOMAIN_URL = "domain";

    private static Prefs instance;
    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {

        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context) {

        if (instance == null) {
            instance = new Prefs(context);
        }
        return instance;
    }

    public void saveToken(String token) {
        sharedPreferences
                .edit()
                .putString(UNIQUE_TOKEN, token)
                .commit();
    }

    public String getToken() {
        return  sharedPreferences.getString(UNIQUE_TOKEN,"");
    }

    //sudipta samanta 18/07/19
    public String getStudentId() {
        return sharedPreferences.getString(STUDENT_ID, "");
    }

    public void saveData(String name, String id, String classId, String phnNo, String fatherNo, String motherNo, String photo, String studentclass) {
//        sharedPreferences
//                .edit()
//                .putString(NAME, name)
//                .putString(STUDENT_ID, id)
//                .putString(CLASS_ID, classId)
//                .putString()
//                .putString(STUDENT_PHOTO, photo)
//                .putString(STUDENT_CLASS, studentclass)
//                .commit();
    }

    public void saveParent(String fatherName, String motherName, String phnNo, String fatherPhnNo, String motherPhnNo, String dob) {
        sharedPreferences
                .edit()
                .putString(FATHER_NAME, fatherName)
                .putString(MOTHER_NAME, motherName)
                .putString(PHONE_NUMBER, phnNo)
                .putString(MOBILE_NUMBER1, fatherPhnNo)
                .putString(MOBILE_NUMBER2, motherPhnNo)
                .putString(DOB, dob)
                .commit();
    }

    public void saveStudent(String name, String stdId, String classId, String classSectionId, String className, String photo) {
        sharedPreferences
                .edit()
                .putString(NAME, name)
                .putString(STUDENT_ID, stdId)
                .putString(CLASS_ID, classId)
                .putString(CLASS_SECTION_ID, classSectionId)
                .putString(STUDENT_PHOTO, photo)
                .putString(STUDENT_CLASS, className)
                .commit();
    }

    public void removeStudent() {
        sharedPreferences
                .edit()
                .remove(NAME)
                .remove(STUDENT_ID)
                .remove(CLASS_ID)
                .remove(CLASS_SECTION_ID)
                .remove(STUDENT_PHOTO)
                .remove(STUDENT_CLASS)
                .commit();
    }

    public String[] getParent() {
        return new String[]{
                sharedPreferences.getString(FATHER_NAME,""),
                sharedPreferences.getString(MOTHER_NAME,""),
                sharedPreferences.getString(PHONE_NUMBER,""),
                sharedPreferences.getString(MOBILE_NUMBER1,""),
                sharedPreferences.getString(MOBILE_NUMBER2, ""),
                sharedPreferences.getString(DOB, "")
        };
    }

    public String[] getStudent() {
        return new String[]{
                sharedPreferences.getString(STUDENT_ID,""),
                sharedPreferences.getString(NAME,""),
                sharedPreferences.getString(CLASS_ID,""),
                sharedPreferences.getString(CLASS_SECTION_ID,""),
                sharedPreferences.getString(STUDENT_CLASS,""),
                sharedPreferences.getString(STUDENT_PHOTO,""),
        };
    }

    public String[] getData() {
        return new String[]{ sharedPreferences
                .getString(NAME,""),
                sharedPreferences
                        .getString(STUDENT_ID,""),
                sharedPreferences
                        .getString(CLASS_ID,""),
                sharedPreferences
                        .getString(PHONE_NUMBER,""),
                sharedPreferences
                        .getString(MOBILE_NUMBER2,""),
                sharedPreferences
                        .getString(MOBILE_NUMBER2,""),
                sharedPreferences
                        .getString(STUDENT_PHOTO,""),
                sharedPreferences
                        .getString(STUDENT_CLASS,"")
        };
    }

    public void setLoginState(boolean value) {
        sharedPreferences.edit()
                .putBoolean(LOGIN_STATE, value)
                .commit();
    }

    public boolean getLoginState() {
        return sharedPreferences.getBoolean(LOGIN_STATE, false);
    }

    public void setDomain(String domain) {
        sharedPreferences.edit()
                .putString(DOMAIN_URL, domain)
                .commit();
    }

    public String getDomain() {
        return sharedPreferences.getString(DOMAIN_URL, "");
    }

    public void makeLogout(Activity activity){
        sharedPreferences.edit().clear().clear().commit();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

}
