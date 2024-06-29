package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.added.kannauj.Adapters.AdvancedAssignmentAdapter;
import app.added.kannauj.Adapters.SubjectListSpinnerAdapter;
import app.added.kannauj.Models.AssignmentGroupModel;
import app.added.kannauj.Models.AssignmentModel;
import app.added.kannauj.Models.ImageNameModel;
import app.added.kannauj.Models.SubjectModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;


public class AssignmentActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    private Spinner spinner;
    private RecyclerView rvStudents;
    private RelativeLayout parentLayout;

    private static final String TAG = "StudentListActivity";
    List<SubjectModel> subjectList = new ArrayList<>();
    List<AssignmentGroupModel> advancedAssignmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        spinner = findViewById(R.id.choose_subject_spinner);
        rvStudents = findViewById(R.id.rvStudents);

        initialize();
    }

    public void initialize() {
        rvStudents.setLayoutManager(new LinearLayoutManager(AssignmentActivity.this));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    SubjectModel model = (SubjectModel) spinner.getAdapter().getItem(i);
                    List<AssignmentGroupModel> tempList = filterAdvancedAssignmentList(advancedAssignmentList, model.getSubject());
                    rvStudents.setAdapter(new AdvancedAssignmentAdapter(tempList, AssignmentActivity.this));
                } else {
                    rvStudents.setAdapter(new AdvancedAssignmentAdapter(advancedAssignmentList, AssignmentActivity.this));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        fetchSubjects();
    }

    public void fetchSubjects() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_SUBJECTS, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        subjectList = initializeClassList(dataObject.getJSONArray("ClassSubjectList"));
                        fetchAssignment();
                        //  binding.spinner.setAdapter(new SubjectListSpinnerAdapter(AssignmentActivity.this,subjectList));
                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar
                                .make(parentLayout, msg, Snackbar.LENGTH_SHORT);
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
                        .make(parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
                showReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token", new Prefs(AssignmentActivity.this).getToken());
                params.put("StudentID", Prefs.with(AssignmentActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(AssignmentActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void fetchAssignment() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_ASSIGNMENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");

                        List<AssignmentGroupModel> list = initializeAdvancedAssignmentList(dataObject.getString("AssignmentImagePath"), dataObject.getJSONArray("Assignments"));
                        advancedAssignmentList = list;
                        rvStudents.setAdapter(new AdvancedAssignmentAdapter(list, AssignmentActivity.this));

                        subjectList = filterAdvancedSubjectList(advancedAssignmentList, subjectList);
                        Log.e(TAG, subjectList.size() + "");
                        spinner.setAdapter(new SubjectListSpinnerAdapter(AssignmentActivity.this, subjectList));

                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar
                                .make(parentLayout, msg, Snackbar.LENGTH_SHORT);
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
                        .make(parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
                showAssignmentReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",
                        //  "b9de694ec6527b5ed55bcec46d12547f");
                        new Prefs(AssignmentActivity.this).getToken());
                params.put("StudentID",
                        //  "1699");
                        new Prefs(AssignmentActivity.this).getStudent()[0]);

                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(AssignmentActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }


    private List<SubjectModel> initializeClassList(JSONArray array) {
        List<SubjectModel> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                SubjectModel model = new SubjectModel(object.getString("id"), object.getString("Subject"));
                list.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("list", list.size() + "");
        return list;
    }

    private List<AssignmentGroupModel> initializeAdvancedAssignmentList(String ImagePath, JSONArray array) {
        List<AssignmentGroupModel> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            List<ImageNameModel> Imagelist = new ArrayList<>();
            try {
                JSONObject object = array.getJSONObject(i);

                for (int j = 0; j < object.getJSONArray("AssignmentImage").length(); j++) {
                    JSONObject image = object.getJSONArray("AssignmentImage").getJSONObject(j);
                    ImageNameModel imagelist = new ImageNameModel(image.getString("ImageName"));
                    Imagelist.add(imagelist);
                }

                AssignmentModel model = new AssignmentModel();
                model.setId(object.getString("id"));
                model.setSubject(object.getString("SubjectName"));
                model.setChapterName(object.getString("ChapterName"));
                model.setTopicName(object.getString("TopicName"));
                model.setAssignmentHeading(object.getString("AssignmentHeading"));
                model.setAssignmentDetail(object.getString("Assignment"));
                model.setIssueDate(getDateFromString(object.getString("IssueDate")));
                model.setEndDate(getDateFromString(object.getString("EndDate")));
                model.setAssignmentImagePath(ImagePath);
                model.setImagelist(Imagelist);

                List<AssignmentModel> oneItemList = new ArrayList<>();
                oneItemList.add(model);

                AssignmentGroupModel assignmentGroupModel = new AssignmentGroupModel(object.getString("AssignmentHeading"), oneItemList);
                list.add(assignmentGroupModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private List<AssignmentGroupModel> filterAdvancedAssignmentList(List<AssignmentGroupModel> list, String subjectName) {
        List<AssignmentGroupModel> tempList = new ArrayList<>();
        for (AssignmentGroupModel model : list) {
            if (model.getItems().get(0).getSubject().equalsIgnoreCase(subjectName))
                tempList.add(model);
        }
        return tempList;
    }

    private List<SubjectModel> filterAdvancedSubjectList(List<AssignmentGroupModel> assignmentList, List<SubjectModel> subjectList) {
        List<SubjectModel> tempList = new ArrayList<>();
        for (SubjectModel subjectModel : subjectList) {
            for (AssignmentGroupModel assignmentModel : assignmentList) {
                if (assignmentModel.getItems().get(0).getSubject().equalsIgnoreCase(subjectModel.getSubject())) {
                    tempList.add(subjectModel);
                    break;
                }
            }
        }
        return tempList;
    }

    private Date getDateFromString(String str) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // here set the pattern as you date in string was containing like date/month/year
            d = sdf.parse(str);
//            Log.e(TAG,d.toString());
        } catch (ParseException ex) {
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            //Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        return d;
    }


    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentActivity.this);
        builder.setTitle("Subjects");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //fetchClasses();
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

    private void showAssignmentReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentActivity.this);
        builder.setTitle("Assignments");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchAssignment();
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
