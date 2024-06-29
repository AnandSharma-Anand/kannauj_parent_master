package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.added.kannauj.Adapters.EDiaryAdapter;
import app.added.kannauj.Models.EDiaryModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.FeedItemDecoration;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityNoticeViewBinding;

public class EDiaryViewActivity extends AppCompatActivity {

    private static final String TAG = "EDiaryViewActivity";
    ActivityNoticeViewBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice_view);
        initialize();
    }

    private void initialize() {
        binding.rvNotice.addItemDecoration(new FeedItemDecoration(50, 50, 9));
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        binding.tvSubstitution.setText("E-Diary");
        fetchNotices();
    }


    public void fetchNotices() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().STUDENT_DIARIES, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        List<EDiaryModel> noticeList = initializeNoticeList(dataObject.getJSONArray("StudentDiaries"));

                        binding.rvNotice.setAdapter(new EDiaryAdapter(EDiaryViewActivity.this, noticeList));
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
                        //"08b461d5b9d51b083343277f9f84b64a");
                        new Prefs(EDiaryViewActivity.this).getToken());
                params.put("ClassID",
                        //"1");
                        new Prefs(EDiaryViewActivity.this).getData()[2]);
                params.put("StudentID", Prefs.with(EDiaryViewActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(EDiaryViewActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private List<EDiaryModel> initializeNoticeList(JSONArray array) {
        List<EDiaryModel> noticeList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                EDiaryModel noticeModel = new EDiaryModel(object.getString("id"),
                        object.getString("Heading"),
                        object.getString("Details"),
                        getDateFromString(object.getString("CreateDate")));
                noticeList.add(noticeModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return noticeList;
    }

    private Date getDateFromString(String str) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // here set the pattern as you date in string was containing like date/month/year
            d = sdf.parse(str);
            Log.e(TAG, d.toString());
        } catch (ParseException ex) {
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            Log.e(TAG, ex.getMessage());
        }
        return d;
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EDiaryViewActivity.this);
        builder.setTitle("E-Diary");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.e_diary);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchNotices();
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
