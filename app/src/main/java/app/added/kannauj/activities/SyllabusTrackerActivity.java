package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
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

import app.added.kannauj.Adapters.ChapterSpinnerAdapter;
import app.added.kannauj.Adapters.ScheduleTopicAdapter;
import app.added.kannauj.Adapters.SubjectListSpinnerAdapter;
import app.added.kannauj.Models.ScheduleModel;
import app.added.kannauj.Models.SubjectModel;
import app.added.kannauj.Models.TopicModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivitySyllabusTrackerBinding;


public class SyllabusTrackerActivity extends AppCompatActivity {

    ActivitySyllabusTrackerBinding binding;
    ProgressDialog progressDialog;
    private static final String TAG = "TeacherScheduleActivity";
    List<SubjectModel>subjectList;
    List<ScheduleModel> scheduleDetailList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_syllabus_tracker);
        initialize();
    }

    private void initialize() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        fetchSubjects();
        binding.spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0) {
                    binding.rvTopic.setAdapter(null);
                    SubjectModel model = (SubjectModel) adapterView.getAdapter().getItem(i);
                    fetchScheduleDetails(model.getId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0) {
                    binding.rvTopic.setAdapter(new ScheduleTopicAdapter(SyllabusTrackerActivity.this,scheduleDetailList.get(i).getTopicList()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void fetchSubjects() {
        String tag_string_req = "req_login";
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_SUBJECTS , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        subjectList = initializeSubjectList(dataObject.getJSONArray("ClassSubjectList"));
                        binding.spinnerSubject.setAdapter(new SubjectListSpinnerAdapter(SyllabusTrackerActivity.this,subjectList));
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
                //showReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",
                        new Prefs(SyllabusTrackerActivity.this).getToken());
                        //"1c075338b1a8cc4ad6790da302954a2c");
                //params.put("ClassSubjectID","2");
                params.put("StudentID",
                        Prefs.with(SyllabusTrackerActivity.this).getStudent()[0]);
                        //"1");
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(SyllabusTrackerActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void fetchScheduleDetails(final String subId) {
        String tag_string_req = "req_login";
        progressDialog.show();
        scheduleDetailList = new ArrayList<>();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_SYLLABUS_TRACKER , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        scheduleDetailList = initializeScheduleDetailList(dataObject.getJSONArray("ChapterStatusDetails"));
                        binding.spinnerChapter.setAdapter(new ChapterSpinnerAdapter(SyllabusTrackerActivity.this,scheduleDetailList));
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
                //showReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",
                        new Prefs(SyllabusTrackerActivity.this).getToken());
                        //"1c075338b1a8cc4ad6790da302954a2c");
                params.put("ClassSubjectID",subId);
                //Log.e(TAG, "Std id : "+Prefs.with(SyllabusTrackerActivity.this).getStudent()[0]);
                params.put("StudentID",
                        //"1");
                        Prefs.with(SyllabusTrackerActivity.this).getStudent()[0]);

                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(SyllabusTrackerActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private List<ScheduleModel> initializeScheduleDetailList(JSONArray array) {
        List<ScheduleModel>tempList = new ArrayList<>();
        List<TopicModel>topicList;
        for (int i=0;i<array.length();i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                topicList = new ArrayList<>();
                for (int j=0;j<object.getJSONArray("TopicDetails").length();j++) {
                    JSONObject topicObject = object.getJSONArray("TopicDetails").getJSONObject(j);
                    TopicModel topicModel = new TopicModel("",topicObject.getString("TopicName"),topicObject.getString("Status"));
                    topicList.add(topicModel);
                }
                ScheduleModel model = new ScheduleModel(object.getString("id"), object.getString("ChapterName"),topicList);
                tempList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "size : "+tempList.size());
        return tempList;
    }

    private List<SubjectModel> initializeSubjectList(JSONArray array) {
        List<SubjectModel> tempList = new ArrayList<>();
        for (int i=0;i<array.length();i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                SubjectModel model = new SubjectModel(object.getString("id"), object.getString("Subject"));
                model.setSubjectId("SubjectID");
                tempList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tempList;
    }

}
