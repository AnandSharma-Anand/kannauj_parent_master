package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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

import app.added.kannauj.Adapters.ChatHomeAdapter;
import app.added.kannauj.Models.ChatListModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    ProgressDialog progressDialog;
    private static final String TAG = "ChatActivity";
    BroadcastReceiver receiver;
    String Name = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    String action = intent.getAction();
                    if (action.equals("UPDATE_UI_PARENT")) {
                        Name = intent.getExtras().getString("Name");

                        initialize();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filterRefreshUpdate = new IntentFilter();
        filterRefreshUpdate.addAction("UPDATE_UI_PARENT");
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), filterRefreshUpdate);
        initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("UPDATE_UI_PARENT")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    }
    private void initialize() {
      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");*/
        //vxplore
        loadList();
    }

    private void loadList() {
        String tag_string_req = "req_login";
        // progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().CHAT_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                //  progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                   /* if (progressDialog.getProgress()>0){
                        progressDialog.dismiss();
                    }*/
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        List<ChatListModel> chatListModels = initializeList(dataObject.getJSONArray("teachers"));
                        //  List<FeeGroupModel> list = initializeAdvancedList(dataObject.getJSONObject("fee_details").getJSONArray("Month"),dataObject.getInt("FeeSubmissionLastMonthPriority"));

                        binding.rvChat.setAdapter(new ChatHomeAdapter(ChatActivity.this, chatListModels, Name));
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
                // progressDialog.dismiss();
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
                //params.put("Token",new Prefs(FeeActivity.this).getToken());
                params.put("Token",
                        new Prefs(ChatActivity.this).getToken());
                //"1c075338b1a8cc4ad6790da302954a2c");
                params.put("StudentID",
                        //"1");
                        Prefs.with(ChatActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(ChatActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle("Week internet Error");
        builder.setMessage("Check internet connection");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                loadList();
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

    private List<ChatListModel> initializeList(JSONArray array) {
        List<ChatListModel> tempList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String FirstName = object.getString("FirstName");
                String id = object.getString("id");
                String LastName = object.getString("LastName");
                String IsActive = object.getString("IsActive");
                String LastMessage = object.getString("LastMessage");
                String BranchStaffID = object.getString("BranchStaffID");
                String Image = object.getString("Image");
                String subject;
                if (object.getJSONArray("Subjects").length() > 1) {
                    subject = object.getJSONArray("Subjects").getJSONObject(0).getString("Subject") + "," + object.getJSONArray("Subjects").getJSONObject(1).getString("Subject") + "...";
                } else {
                    subject = object.getJSONArray("Subjects").getJSONObject(0).getString("Subject");
                }
                ChatListModel chatListModel = new ChatListModel(id, FirstName, LastName, IsActive, LastMessage, BranchStaffID, Image, subject);
                tempList.add(chatListModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tempList;
    }
}
