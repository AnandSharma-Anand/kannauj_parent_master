package app.added.kannauj.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.added.kannauj.Adapters.BottomSheetStudentAdapter;
import app.added.kannauj.Adapters.ViewPagerAdapter;
import app.added.kannauj.Models.StudentModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.CircleTransform;
import app.added.kannauj.Utils.DatabaseHelper;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class HomeActivity extends AppCompatActivity {

    TextView tvFeed, tvMenu, tvName, tvClass;
    RelativeLayout rlFeed, rlMenu;
    CardView cardFeed, cardMenu;
    ImageView ivImg;
    ImageView ivInfo;
    //  ImageView ivArrow;
    ViewPager viewPager;
    NestedScrollView nestedScrollView;
    Prefs prefs;
    //ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    LocationManager locationManager;
    private static final String TAG = "HomeActivity";
    BottomSheetBehavior sheetBehavior;
    RelativeLayout bottomSheet;
    RecyclerView rvStudents;
    int activityFinishCounter = 0;
    DatabaseHelper helper;
    //vxplore 18/7/19
    String isFirstTimeLogin = "1";
    Bundle bundle = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_home);
        //vxplore 18/7/19
        bundle = getIntent().getExtras();
        if (bundle != null) {
            isFirstTimeLogin = bundle.getString("isLoginFirstTime");
        }
        //  new AppConfig().BASE_URL = new Prefs(this).getDomain();
        try {
            if (Prefs.with(HomeActivity.this).getDomain().contains("https://")) {
                AppConfig.BASE_URL = Prefs.with(HomeActivity.this).getDomain();
            } else if (Prefs.with(HomeActivity.this).getDomain().contains("http://")) {
                AppConfig.BASE_URL = Prefs.with(HomeActivity.this).getDomain().replaceAll("http://", "https://");
                Prefs.with(HomeActivity.this).setDomain(AppConfig.BASE_URL);
            }

        } catch (Exception e) {
            AppConfig.BASE_URL = Prefs.with(HomeActivity.this).getDomain();
        }
        new AppConfig();
        Log.e("AppConfig.BASE_URL", "FCM Token : " + AppConfig.BASE_URL);
        //    Log.e(TAG, "Token : " + new Prefs(this).getToken());
        initializeViews();
        initialize();
        FirebaseApp.initializeApp(this);
        Log.e("fcm", "FCM Token : " + FirebaseInstanceId.getInstance().getToken());
        if (FirebaseInstanceId.getInstance().getToken() != null)
            if (!FirebaseInstanceId.getInstance().getToken().isEmpty()) {
                registerFCMToken(FirebaseInstanceId.getInstance().getToken());
            }
        requestLocationPermission();
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e(TAG, location.getLatitude() + "");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    public void initializeViews() {
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        tvName = findViewById(R.id.tvName);
        tvClass = findViewById(R.id.tvClass);
        tvFeed = findViewById(R.id.tvFeed);
        tvMenu = findViewById(R.id.tvMenu);
        rlFeed = findViewById(R.id.rlFeed);
        rlMenu = findViewById(R.id.rlMenu);
        cardFeed = findViewById(R.id.cardFeed);
        cardMenu = findViewById(R.id.cardMenu);
        ivInfo = findViewById(R.id.ivInfo);
        ivImg = findViewById(R.id.ivImg);
        //  ivArrow = findViewById(R.id.ivArrow);
        //Picasso.get().load(R.drawable.girl_1).transform(new CircleTransform()).into(ivImg);
        viewPager = findViewById(R.id.viewPager);

        bottomSheet = findViewById(R.id.bottom_sheet);
        rvStudents = findViewById(R.id.rvStudents);

        nestedScrollView = findViewById(R.id.scroll);
        nestedScrollView.setFillViewport(true);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
    }

    public void initialize() {
        //Log.e(TAG, token);

        helper = new DatabaseHelper(this);
        prefs = Prefs.with(HomeActivity.this);

        //vxplore 18/7/19
        if (isFirstTimeLogin.equals("0")) {
        getLoginData(prefs.getToken());
        }


        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        tvName.setText(prefs.getStudent()[1]);
        tvClass.setText(prefs.getStudent()[4]);
        if (!prefs.getStudent()[5].isEmpty()) {
            Picasso.get().load(prefs.getStudent()[5]).placeholder(R.drawable.student).transform(new CircleTransform()).into(ivImg);
        } else {
            Picasso.get().load(R.drawable.student).into(ivImg);
        }

        final ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                ivImg,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(500);
        scaleDown.setInterpolator(new AccelerateInterpolator());

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();

        final PulsatorLayout pulsator = findViewById(R.id.pulsator);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // only for marshmallow and newer versions
            pulsator.setCount(4);
        } else {
            pulsator.setCount(1);
        }
        pulsator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scaleDown.setRepeatCount(0);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(pulsator,
                        PropertyValuesHolder.ofFloat("alpha", 0));
                animator.setDuration(400);
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        pulsator.stop();
                    }
                });
            }
        }, 6000);

        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPageMenu();
                //  ivArrow.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(1, true);
            }
        });
        rlFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPageFeed();
                // ivArrow.setVisibility(View.INVISIBLE);
                viewPager.setCurrentItem(0, true);
            }
        });
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    selectPageFeed();
                } else {
                    selectPageMenu();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "state : " + sheetBehavior.getState());
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        rvStudents.setAdapter(new BottomSheetStudentAdapter(HomeActivity.this));

    }

    public void registerFCMToken(final String token) {
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_SAVE_TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                Log.e("adil", "Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
//                    String msg = object.getString("message");
//                        Snackbar snackbar = Snackbar
//                                .make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT);
//                        snackbar.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Response: " + error.toString());
                //progressDialog.dismiss();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token", prefs.getToken());
                params.put("FcmToken", token);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(HomeActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void getLoginData(final String token) {
        String tag_string_req = "req_login";
        Log.e("adil_userInfo", "Response: " + token);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_USER_INFO, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                Log.e("adil_userInfo", "Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        Log.e("adil", "here");
                        //saveData(object.getJSONObject("data"));
                        saveParent(object.getJSONObject("data"));
                        //vxplore 18/7/19
                        isFirstTimeLogin = "1";
                    } else {
                        String msg = object.getString("message");
//                        Snackbar snackbar = Snackbar
//                                .make(relativeLayout, msg, Snackbar.LENGTH_SHORT);
//                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("adil", "Error Response: " + error.toString());
//                Snackbar snackbar = Snackbar
//                        .make(relativeLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
//                snackbar.show();
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
        appController.setContext(HomeActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
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
                String s = (prefs.getStudent())[0];
                Log.i("StutedentugPrefrence",s);
                if (i == 0)

                    saveStudent(stdObject, photoDirectory, true);
                else
                    //false
                    saveStudent(stdObject, photoDirectory, false);
            }

        } catch (JSONException e) {
            Log.i("StutedentPrefrence",e.getMessage());
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
            tvName.setText(prefs.getStudent()[1]);
            tvClass.setText(prefs.getStudent()[4]);
            if (!prefs.getStudent()[5].isEmpty()) {
                Picasso.get().load(prefs.getStudent()[5]).placeholder(R.drawable.student).transform(new CircleTransform()).into(ivImg);
            } else {
                Picasso.get().load(R.drawable.student).into(ivImg);
            }
             }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void selectPageMenu() {
        tvMenu.setTypeface(null, Typeface.BOLD);
        tvFeed.setTypeface(null, Typeface.NORMAL);
        cardMenu.setCardBackgroundColor(Color.parseColor("#82ffffff"));
        cardFeed.setCardBackgroundColor(Color.TRANSPARENT);
    }

    private void selectPageFeed() {
        tvMenu.setTypeface(null, Typeface.NORMAL);
        tvFeed.setTypeface(null, Typeface.BOLD);
        cardFeed.setCardBackgroundColor(Color.parseColor("#82ffffff"));
        cardMenu.setCardBackgroundColor(Color.TRANSPARENT);
    }

    // Permission Stuff

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }


    @Override
    public void onBackPressed() {
        activityFinishCounter++;

        if (activityFinishCounter != 2) {
            Toast.makeText(HomeActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityFinishCounter = 0;
            }
        }, 2000);

        if (activityFinishCounter == 2) {
            super.onBackPressed();
        }
    }


}




