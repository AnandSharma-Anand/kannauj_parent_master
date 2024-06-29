package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.added.kannauj.BuildConfig;
import app.added.kannauj.Models.StudentModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.DatabaseHelper;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;


public class LoginActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    AnimationDrawable animationDrawable;
    CardView cardSubmit;
    ProgressDialog progressDialog;
    EditText etContact, etPassword;
    Prefs prefs;
    DatabaseHelper helper;
    private static final String TAG = "LoginActivity";
    private int UPDATE_REQUEST_CODE = 0;

    private AppUpdateManager appUpdateManager;
    private Task<AppUpdateInfo> appUpdateInfoTask;
    TextView tvTandC, tvPivacyPolicy, tvRefund;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e(TAG, "FCM Token : " + FirebaseInstanceId.getInstance().getToken());
        checkForAppUpdate();
        if (Prefs.with(LoginActivity.this).getLoginState()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        initializeViews();
        initialize();

        cardSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                finish();
                if (etContact.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(relativeLayout, "Please fill all details!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    getDomain(etContact.getText().toString(), etPassword.getText().toString());
                    //login(etContact.getText().toString(), etPassword.getText().toString());
                }
            }
        });
    }

    private void checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(LoginActivity.this);
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, LoginActivity.this, UPDATE_REQUEST_CODE);

                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
//                    Toast.makeText(LoginActivity.this, "No updates needed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability()
                        == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, LoginActivity.this, UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == UPDATE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public void initializeViews() {
        relativeLayout = findViewById(R.id.relativeLayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        cardSubmit = findViewById(R.id.cardSubmit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        etContact = findViewById(R.id.etContact);
        etPassword = findViewById(R.id.etPassword);
        if (BuildConfig.DEBUG) {
            etContact.setText("pankajsingh2019@kpsk");
            etPassword.setText("PANK2019");
        }
        tvTandC = findViewById(R.id.tvTandC);
        tvPivacyPolicy = findViewById(R.id.tvPivacyPolicy);
        tvRefund = findViewById(R.id.tvRefund);
        tvTandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://www.addedschools.com/added_website/terms_conditions.html");
                intent.putExtra("heading", "Terms & Condition");
                startActivity(intent);
            }
        });
        tvPivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://www.addedschools.com/added_website/privacy_policy.html");
                intent.putExtra("heading", "Privacy Policy");
                startActivity(intent);
            }
        });
        tvRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://www.addedschools.com/added_website/refundcancellation_policy.html");
                intent.putExtra("heading", "Refund & Cancellation Policy");
                startActivity(intent);
            }
        });


    }

    public void initialize() {
        prefs = new Prefs(this);
        helper = new DatabaseHelper(LoginActivity.this);

        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }


    public void getDomain(final String username, final String password) {
        String tag_string_req = "req_login";
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserName", username.trim());
        params.put("Password", password.trim());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,AppConfig.GET_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    JSONObject object =new JSONObject(response);
                    Log.d(TAG, "Response: " + object.toString());
                    if (object.getString("error").equals("0")) {
                        String domain = object.getJSONObject("data").getString("Domain");
                        String baseUrl = "https://" + domain + "/mobile_app_services/parents_app/";
                        Prefs.with(LoginActivity.this).setDomain(baseUrl);
                        new AppConfig().BASE_URL = baseUrl;
                        Log.i(TAG, "domain : " + new AppConfig().BASE_URL);
                        login(username, password);
                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, msg, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("slakjasld", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Response: " + error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(LoginActivity.this);
        appController.addToRequestQueue(jsonObjectRequest, tag_string_req);
    }

    public void login(final String username, final String password) {
        String tag_string_req = "req_login";
        progressDialog.show();
        Log.i(TAG, "login : " + new AppConfig().URL_LOGIN);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.i(TAG, "Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        String token = object.getJSONObject("data").getString("UniqueToken");
//                        Prefs.with(LoginActivity.this).saveToken("49e75c106d7653833c87a73ce9cbb3f5");
                        Prefs.with(LoginActivity.this).saveToken(token);
                        getLoginData(token);
                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, msg, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Response: " + error.toString());
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", username.trim());
                params.put("Password", password.trim());
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(LoginActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void getLoginData(final String token) {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_USER_INFO, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.i(TAG, "Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        Log.e("adil", "here");
                        //saveData(object.getJSONObject("data"));
                        saveParent(object.getJSONObject("data"));
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, msg, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("adil", "Error Response: " + error.toString());
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token", token.trim());
                //Log.e("adil","token : "+token);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(LoginActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void saveData(JSONObject object) {
        try {
            String name = object.getString("FirstName") + " " + object.getString("LastName");
            String id = object.getString("StudentID");
            String classId = object.getString("ClassID");
            String phoneNo = object.getString("PhoneNumber");
            String fatherNo = object.getString("FatherMobileNumber");
            String motherNo = object.getString("MotherMobileNumber");
            String photo = object.getString("student_photo_directory") + object.getString("StudentPhoto");
            String studentClass = object.getString("ClassTeacherClassSymbol") + " - " + object.getString("SectionName");
            Prefs.with(LoginActivity.this).saveData(name, id, phoneNo, classId, fatherNo, motherNo, photo, studentClass);
            Prefs.with(LoginActivity.this).setLoginState(true);


            Prefs.with(LoginActivity.this).setLoginState(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveParent(JSONObject object) {
        try {

            String fatherName = object.getString("FatherFirstName") + " " + object.getString("FatherLastName");
            String motherName = object.getString("MotherFirstName") + " " + object.getString("MotherLastName");
            String phoneNo = object.getString("PhoneNumber");
            String fatherNo = object.getString("FatherMobileNumber");
            String motherNo = object.getString("MotherMobileNumber");
            String photoDirectory = object.getString("student_photo_directory");
            String DOB = object.getString("DOB");

            prefs.saveParent(fatherName, motherName, phoneNo, fatherNo, motherNo, DOB);
            prefs.setLoginState(true);

            JSONArray array = object.getJSONArray("StudentDetails");
            helper.deleteAllStudents();
            for (int i = 0; i < array.length(); i++) {
                JSONObject stdObject = array.getJSONObject(i);
                if (i == 0)
                    saveStudent(stdObject, photoDirectory, true);
                else
                    saveStudent(stdObject, photoDirectory, false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveStudent(JSONObject object, String photoDirectory, boolean isFirst) {
        try {
            String id = object.getString("StudentID");
            String name = object.getString("FirstName") + " " + object.getString("LastName");
            String photo = photoDirectory + object.getString("StudentPhoto");
            String rollNo = object.getString("RollNumber");
            String className = object.getString("ClassSymbol") + " - " + object.getString("SectionName");
            String classId = object.getString("ClassID");
            String classSectionId = object.getString("ClassSectionID");

            StudentModel model = new StudentModel(id, name, rollNo, "", photo);
            model.setClassName(className);
            model.setClassSectionId(classSectionId);
            model.setClassId(classId);
            helper.addStudent(model);

            if (isFirst) {
                prefs.saveStudent(name, id, classId, classSectionId, className, photo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
