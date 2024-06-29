package app.added.kannauj.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import app.added.kannauj.Adapters.ChatMessageAdapter;
import app.added.kannauj.Models.ChatMessageListModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.ChatScrollListener;
import app.added.kannauj.Utils.MessagingService;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityChatMessageBinding;

public class ChatMessageActivity extends AppCompatActivity {

    ActivityChatMessageBinding binding;
    boolean isLoading;
    LinearLayoutManager linearLayoutManager;
    List<ChatMessageListModel> list;
    ChatMessageAdapter adapter;
    // ProgressDialog progressDialog;
    private static final String TAG = "ChatMessageActivity";
    Bundle bundle = null;
    String BranchStaffID;
    private BroadcastReceiver receiver;
    int flag;
    String UserName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_message);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            BranchStaffID = bundle.getString("BranchStaffID");
            UserName = bundle.getString("UserName");
            binding.tvTitle.setText(UserName);
        }
        flag = 0;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("val");
                switch (s) {
                    case "tick":
                        if (flag == 0)
                            loadData();
                        break;
                }
            }
        };

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, MessagingService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("CHAT"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MessagingService.class));
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void initialize() {

      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");*/
        list = new ArrayList<>();

        linearLayoutManager = (LinearLayoutManager) binding.rvChat.getLayoutManager();
        binding.rvChat.addOnScrollListener(new ChatScrollListener(linearLayoutManager) {
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                //  loadItem();
            }
        });
        //vxplore
        loadData();

        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentMessage();
            }
        });

    }

    private void sentMessage() {

        if (binding.etMsg.getText().toString().trim().equals("")) {
            Toast.makeText(ChatMessageActivity.this, "Please enter some message", Toast.LENGTH_LONG).show();
            return;
        }

        String tag_string_req = "req_login";
        // progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().SENT_MESSAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                //   progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        binding.etMsg.setText("");
                        loadData();
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
                //   progressDialog.dismiss();
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
                        new Prefs(ChatMessageActivity.this).getToken());
                //"1c075338b1a8cc4ad6790da302954a2c");
                params.put("BranchStaffID",
                        //"1");
                        BranchStaffID);
                params.put("Message", binding.etMsg.getText().toString().trim()
                );
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(ChatMessageActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private void loadData() {
        String tag_string_req = "req_login";
        //  progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().FETCH_CHAT, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                //       progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        list = initializeList(dataObject.getJSONArray("chats"));
                        //  List<FeeGroupModel> list = initializeAdvancedList(dataObject.getJSONObject("fee_details").getJSONArray("Month"),dataObject.getInt("FeeSubmissionLastMonthPriority"));

                        //    binding.rvChat.setAdapter(new ChatHomeAdapter(ChatActivity.this,chatListModels));
                        // if (chatMessageListModels.size()<15) {
                        adapter = new ChatMessageAdapter(ChatMessageActivity.this, list);
                        //  }
                        //  else {

                        //  }
                        //adapter = new ChatMessageAdapter(ChatMessageActivity.this,list);
                        binding.rvChat.setAdapter(adapter);
                        // binding.rvChat.smoothScrollToPosition(list.size());
                        binding.rvChat.smoothScrollToPosition(0);
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
                //     progressDialog.dismiss();
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
                        new Prefs(ChatMessageActivity.this).getToken());
                //"1c075338b1a8cc4ad6790da302954a2c");
                params.put("BranchStaffID",
                        //"1");
                        BranchStaffID);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(ChatMessageActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private List<ChatMessageListModel> initializeList(JSONArray array) {
        List<ChatMessageListModel> tempList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String CreateDate = object.getString("CreateDate");
                String id = object.getString("id");
                String IsDelivered = object.getString("IsDelivered");
                String IsSeen = object.getString("IsSeen");
                String isSelfMessage = object.getString("isSelfMessage");
                String Message = object.getString("Message");
                String SeenDateTime = object.getString("SeenDateTime");
                ChatMessageListModel feeModel = new ChatMessageListModel(CreateDate, id, IsDelivered, IsSeen, isSelfMessage, Message, SeenDateTime);
                tempList.add(feeModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tempList;
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatMessageActivity.this);
        builder.setTitle("Week internet Error");
        builder.setMessage("Check internet connection");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                loadData();
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
   /* public void loadItem() {
        Log.e("load","here");
        adapter.addLoadingFooter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ChatMessageListModel>tempList = new ArrayList<>();
                for (int i=0;i<15;i++) {
                    tempList=list;
                }
                adapter.addAll(tempList);
                isLoading = false;
                adapter.removeLoadingFooter();
            }
        },5000);
    }*/

}
