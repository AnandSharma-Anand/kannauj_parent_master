package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.added.kannauj.Adapters.TimeTablePagerAdapter;
import app.added.kannauj.Models.TimeTableModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityTimeTableBinding;


public class TimeTableActivity extends AppCompatActivity {

     ActivityTimeTableBinding binding;
     ProgressDialog progressDialog;
    private static final String TAG = "TimeTableActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_table);
        binding.titleStrip.setTextColor(Color.WHITE);
        binding.titleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        for (int i = 0; i < binding.titleStrip.getChildCount(); ++i) {
            View nextChild = binding.titleStrip.getChildAt(i);
            if (nextChild instanceof TextView) {
                TextView textViewToConvert = (TextView) nextChild;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textViewToConvert.setLetterSpacing(0.2f);
                }
            }
        }
        fetchTimeTable();
    }

    public void fetchTimeTable() {
        String tag_string_req = "req_login";
        progressDialog.show();

        Log.e(TAG, "time table " + new AppConfig().URL_TIME_TABLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_TIME_TABLE , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        List<List<TimeTableModel>> list = initializeList(dataObject.getJSONObject("time_table"));
                        binding.timeTablePager.setAdapter(new TimeTablePagerAdapter(getSupportFragmentManager(),list));
                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar
                                .make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
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
                Snackbar snackbar = Snackbar
                        .make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
                showReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",
                        //"1c075338b1a8cc4ad6790da302954a2c");
                        new Prefs(TimeTableActivity.this).getToken());
                params.put("StudentID",
                        //"1");
                        Prefs.with(TimeTableActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(TimeTableActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public List<List<TimeTableModel>> initializeList(JSONObject days) {
        List<List<TimeTableModel>> list = new ArrayList<>();
        try {
                List<TimeTableModel> timeTableList = new ArrayList<>();
                JSONArray monday = days.getJSONArray("Monday");
                JSONArray tuesday = days.getJSONArray("Tuesday");
                JSONArray wednesday = days.getJSONArray("Wednesday");
                JSONArray thursday = days.getJSONArray("Thursday");
                JSONArray friday = days.getJSONArray("Friday");
                JSONArray saturday = days.getJSONArray("Saturday");
                Log.e(TAG,"monday :  " + monday.toString());

                for (int j=0;j<monday.length();j++) {
                    JSONObject object = monday.getJSONObject(j);
                    TimeTableModel model = new TimeTableModel(object.getString("PeriodName"),
                            object.getString("SubjectName"), object.getString("StartTime"),
                            object.getString("EndTime"));
                    timeTableList.add(model);
                }
                list.add(timeTableList);

            timeTableList = new ArrayList<>();
            for (int j=0;j<tuesday.length();j++) {
                JSONObject object = tuesday.getJSONObject(j);
                TimeTableModel model = new TimeTableModel(object.getString("PeriodName"),
                        object.getString("SubjectName"), object.getString("StartTime"),
                        object.getString("EndTime"));
                timeTableList.add(model);
            }
            list.add(timeTableList);

            timeTableList = new ArrayList<>();
            for (int j=0;j<wednesday.length();j++) {
                JSONObject object = wednesday.getJSONObject(j);
                TimeTableModel model = new TimeTableModel(object.getString("PeriodName"),
                        object.getString("SubjectName"), object.getString("StartTime"),
                        object.getString("EndTime"));
                timeTableList.add(model);
            }
            list.add(timeTableList);

            timeTableList = new ArrayList<>();
            for (int j=0;j<thursday.length();j++) {
                JSONObject object = thursday.getJSONObject(j);
                TimeTableModel model = new TimeTableModel(object.getString("PeriodName"),
                        object.getString("SubjectName"), object.getString("StartTime"),
                        object.getString("EndTime"));
                timeTableList.add(model);
            }
            list.add(timeTableList);

            timeTableList = new ArrayList<>();
            for (int j=0;j<friday.length();j++) {
                JSONObject object = friday.getJSONObject(j);
                TimeTableModel model = new TimeTableModel(object.getString("PeriodName"),
                        object.getString("SubjectName"), object.getString("StartTime"),
                        object.getString("EndTime"));
                timeTableList.add(model);
            }
            list.add(timeTableList);

            timeTableList = new ArrayList<>();
            for (int j=0;j<saturday.length();j++) {
                JSONObject object = saturday.getJSONObject(j);
                TimeTableModel model = new TimeTableModel(object.getString("PeriodName"),
                        object.getString("SubjectName"), object.getString("StartTime"),
                        object.getString("EndTime"));
                timeTableList.add(model);
            }
            list.add(timeTableList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TimeTableActivity.this);
        builder.setTitle("Timetable");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchTimeTable();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
