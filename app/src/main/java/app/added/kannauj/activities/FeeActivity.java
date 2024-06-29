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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.added.kannauj.Adapters.AdvancedFeeAdapter;
import app.added.kannauj.Models.FeeGroupModel;
import app.added.kannauj.Models.FeeModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityFeeBinding;

public class FeeActivity extends AppCompatActivity {

    ActivityFeeBinding binding;
    ProgressDialog progressDialog;
    private static final String TAG = "FeeActivity";
    int total,due;
    // String header1, header2, header3, header4;
    String header1[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee);
        initialize();
    }

    private void initialize() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        //vxplore
        fetHead();


        //binding.rvFee.setAdapter(new OldFeeAdapter(FeeActivity.this));
    }

    //vxplore
    private void fetHead() {

        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().FEE_HEAD, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        JSONArray feeHead = dataObject.getJSONArray("FeeHeads");
                        header1 = new String[feeHead.length()];
                        for (int i = 0; i < header1.length; i++) {
                            header1[i] = feeHead.getJSONObject(i).getString("FeeHead");
                        }

                       /* header2 = feeHead.getJSONObject(1).getString("FeeHead");
                        header3 = feeHead.getJSONObject(2).getString("FeeHead");
                        header4 = feeHead.getJSONObject(3).getString("FeeHead");*/
                        fetchFee();
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
                        new Prefs(FeeActivity.this).getToken());
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(FeeActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void fetchFee() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_FEE , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        List<FeeGroupModel> list = initializeAdvancedList(dataObject.getJSONObject("fee_details").getJSONArray("Month"),dataObject.getInt("FeeSubmissionLastMonthPriority"));
                        binding.rvFee.setAdapter(new AdvancedFeeAdapter(header1, list, FeeActivity.this));

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
                        new Prefs(FeeActivity.this).getToken());
                        //"1c075338b1a8cc4ad6790da302954a2c");
                params.put("StudentID",
                        //"1");
                        Prefs.with(FeeActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(FeeActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FeeActivity.this);
        builder.setTitle("Fee Structure");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchFee();
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

   /* private List<FeeModel> initializeList(JSONArray array) {
        List<FeeModel> tempList = new ArrayList<>();
        for (int i=0;i<array.length();i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String monthName = object.getString("MonthName");
                String admAmt = object.getJSONObject("Admission fee").getString("AmountPayable");
                String admPaid = object.getJSONObject("Admission fee").getString("AmountPaid");
                String admDue = object.getJSONObject("Admission fee").getString("DueAmount");
                String ttnAmt = object.getJSONObject("Tution Fee").getString("AmountPayable");
                String ttnPaid = object.getJSONObject("Tution Fee").getString("AmountPaid");
                String ttnDue = object.getJSONObject("Tution Fee").getString("DueAmount");
                String cmpAmt = object.getJSONObject("Computer Fee").getString("AmountPayable");
                String cmpPaid = object.getJSONObject("Computer Fee").getString("AmountPaid");
                String cmpDue = object.getJSONObject("Computer Fee").getString("DueAmount");
                String exmAmt = object.getJSONObject("Exam Fee").getString("AmountPayable");
                String exmPaid = object.getJSONObject("Exam Fee").getString("AmountPaid");
                String exmDue = object.getJSONObject("Exam Fee").getString("DueAmount");
                total = total + Integer.parseInt(admAmt)+Integer.parseInt(ttnAmt)+Integer.parseInt(cmpAmt)+Integer.parseInt(exmAmt);
                due = due + Integer.parseInt(admDue)+Integer.parseInt(ttnDue)+Integer.parseInt(cmpDue)+Integer.parseInt(exmDue);
                int feePriority = object.getInt("FeePriority");
                FeeModel feeModel = new FeeModel(monthName,admAmt,admPaid,admDue,ttnAmt,ttnPaid,ttnDue,cmpAmt,cmpPaid,cmpDue,exmAmt,exmPaid,exmDue,feePriority);
                tempList.add(feeModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        binding.tvDetail.setText("Total Fee : "+total+"   |   Due Fee : "+due);
        return tempList;
    }*/

    private List<FeeGroupModel> initializeAdvancedList(JSONArray array,int lastMonthFeePriority) {
        List<FeeGroupModel> tempList = new ArrayList<>();
        for (int i=0;i<array.length();i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String monthName = object.getString("MonthName");
                String admAmt[] = new String[header1.length];
                String admPaid[] = new String[header1.length];
                String admDue[] = new String[header1.length];

                for (int j = 0; j < header1.length; j++) {
                    admAmt[j] = object.getJSONObject(header1[j]).getString("AmountPayable");
                    admPaid[j] = object.getJSONObject(header1[j]).getString("AmountPaid");
                    admDue[j] = object.getJSONObject(header1[j]).getString("DueAmount");
                }
              /*  String ttnAmt = object.getJSONObject(header4).getString("AmountPayable");
                String ttnPaid = object.getJSONObject(header4).getString("AmountPaid");
                String ttnDue = object.getJSONObject(header4).getString("DueAmount");
                String cmpAmt = object.getJSONObject(header3).getString("AmountPayable");
                String cmpPaid = object.getJSONObject(header3).getString("AmountPaid");
                String cmpDue = object.getJSONObject(header3).getString("DueAmount");
                String exmAmt = object.getJSONObject(header2).getString("AmountPayable");
                String exmPaid = object.getJSONObject(header2).getString("AmountPaid");
                String exmDue = object.getJSONObject(header2).getString("DueAmount");*/
                int feePriority = 0;
                int feeStatus = 0;
                int dueAmt = 0;
                for (int k = 0; k < admAmt.length; k++) {
                    total = total + Integer.parseInt(admAmt[k]);
                    due = due + Integer.parseInt(admDue[k]);
                    feePriority = object.getInt("FeePriority");


                    if (feePriority <= lastMonthFeePriority) {
                        dueAmt = dueAmt + Integer.parseInt(admDue[k]);
                        feeStatus = dueAmt > 0 ? -1 : 1;
                    } else {
                        feeStatus = 0;
                    }


                }
                FeeModel feeModel = new FeeModel(monthName, admAmt, admPaid, admDue, feePriority);
                List<FeeModel> oneItemList = new ArrayList<>();
                oneItemList.add(feeModel);
                FeeGroupModel feeGroupModel = new FeeGroupModel(monthName, oneItemList,feeStatus);
                tempList.add(feeGroupModel);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        binding.tvDetail.setText("Total Fee : "+total+"   |   Due Fee : "+due);
        return tempList;
    }


}
