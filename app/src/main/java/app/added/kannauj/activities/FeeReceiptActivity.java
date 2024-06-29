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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.added.kannauj.Adapters.FeesReceiptAdapter;
import app.added.kannauj.Models.FeesReceiptModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityFeeReceiptBinding;

public class FeeReceiptActivity extends AppCompatActivity {

    private static final String TAG = "FeeReceiptActivity";
    ActivityFeeReceiptBinding binding;
    ProgressDialog progressDialog;
    int total, due;
    // String header1, header2, header3, header4;
    String header1[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee_receipt);
        initialize();
    }

    private void initialize() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        fetchFeeReceipt();
    }


    public void fetchFeeReceipt() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().FEES_RECEIPT, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        Gson gson = new Gson();
                        FeesReceiptModel feesReceiptModel = gson.fromJson(response, FeesReceiptModel.class);
                        binding.rvFee.setAdapter(new FeesReceiptAdapter(feesReceiptModel, FeeReceiptActivity.this));

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
                //params.put("Token",new Prefs(FeeActivity.this).getToken());
                params.put("Token",
                        new Prefs(FeeReceiptActivity.this).getToken());
                //"1c075338b1a8cc4ad6790da302954a2c");
                params.put("StudentID",
                        //"1");
                        Prefs.with(FeeReceiptActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(FeeReceiptActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FeeReceiptActivity.this);
        builder.setTitle("Fee Structure");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.receipt);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchFeeReceipt();
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
