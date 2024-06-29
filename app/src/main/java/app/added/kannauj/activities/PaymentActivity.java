package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easebuzz.payment.kit.PWECouponsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.added.kannauj.Adapters.AdvancedFeePaymentAdapter;
import app.added.kannauj.Adapters.PaymentAdapter;
import app.added.kannauj.Models.PaymentFeeGroupModel;
import app.added.kannauj.Models.PaymentFeeModel;
import app.added.kannauj.Models.PaymentFeesModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityPaymentBinding;
import datamodels.PWEStaticDataModel;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    ActivityPaymentBinding binding;
    ProgressDialog progressDialog;
    int flatPos = 0;
    PaymentFeesModel paymentFeesModel;
    PaymentFeesModel.DataBean.FeeDetailsBean.MonthBean monthBean;
    //int dueAmount=0;
    String transactionId, txnid;
    AdvancedFeePaymentAdapter advancedFeePaymentAdapter;
    ArrayList<String> DueAmount = null;
    ArrayList<String> StudentFeetructureID = null;
    String CentralFeeTransactionID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        initialize();
    }

    private void initialize() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        //vxplore
        fetchFee();

        binding.spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                monthBean = (PaymentFeesModel.DataBean.FeeDetailsBean.MonthBean) binding.spMonth.getAdapter().getItem(i);

                // Toast.makeText(PaymentActivity.this,monthBean.getFeeHeadDetails().get(0).getDueAmount(),Toast.LENGTH_LONG).show();

                binding.rvFee.setAdapter(null);

                binding.rvFee.setAdapter(new PaymentAdapter(monthBean.getFeeHeadDetails(), PaymentActivity.this));

                //  dueAmount=0;
                for (int j = 0; j < monthBean.getFeeHeadDetails().size(); j++) {
                    //  dueAmount=dueAmount+Integer.parseInt(monthBean.getFeeHeadDetails().get(j).getDueAmount());
                }
                // binding.tvDetail.setText("Total Due Amount Rs. "+dueAmount);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Log.v("AMount",DueAmount.toString());
                Log.v("iddddddd",StudentFeetructureID.toString());*/
                if (DueAmount.size() == 0) {
                    Toast.makeText(PaymentActivity.this, "Please Select a Month", Toast.LENGTH_SHORT).show();
                    return;
                }
                int dueAmount = 0;
                for (int i = 0; i < DueAmount.size(); i++) {
                    dueAmount = dueAmount + Integer.parseInt(DueAmount.get(i));
                }
                if (dueAmount == 0) {
                    Toast.makeText(PaymentActivity.this, "Due amount should be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }


                String tag_string_req = "req_login";
                progressDialog.show();

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        new AppConfig().SAVE_FEE_TRANSACTION, new Response.Listener<String>() {

                    @Override
                    public void onResponse(final String response) {
                        progressDialog.dismiss();
                        Log.e(TAG, "Response: " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("error").equals("0")) {
                                JSONObject dataObject = object.getJSONObject("data");
                                JSONObject jsonObject = dataObject.getJSONObject("TransactionData");
                                txnid = jsonObject.getString("txnid");
                                String amount = jsonObject.getString("amount");
                                String productinfo = jsonObject.getString("productinfo");
                                String firstname = jsonObject.getString("firstname");
                                String email = jsonObject.getString("email");
                                String phone = jsonObject.getString("phone");
                                String key = jsonObject.getString("key");
                                String Salt = jsonObject.getString("Salt");
                                String udf1 = jsonObject.getString("udf1");
                                String udf2 = jsonObject.getString("udf2");
                                String udf3 = jsonObject.getString("udf3");
                                String udf4 = jsonObject.getString("udf4");
                                String udf5 = jsonObject.getString("udf5");
                                String address1 = jsonObject.getString("address1");
                                String city = jsonObject.getString("city");
                                String state = jsonObject.getString("state");
                                String country = jsonObject.getString("country");
                                String zipcode = jsonObject.getString("zipcode");
                                String unique_id = jsonObject.getString("unique_id");
                                String pay_mode = jsonObject.getString("pay_mode");
                                String sub_merchant_id = jsonObject.getString("sub_merchant_id");
                                CentralFeeTransactionID = jsonObject.getString("CentralFeeTransactionID");


                                submitPaymentGateway(
                                        txnid,
                                        amount,
                                        productinfo,
                                        firstname,
                                        email,
                                        phone,
                                        key,
                                        Salt,
                                        udf1,
                                        udf2,
                                        udf3,
                                        udf3,
                                        udf4,
                                        udf5,
                                        address1,
                                        city,
                                        state,
                                        country,
                                        zipcode,
                                        unique_id,
                                        zipcode,
                                        pay_mode,
                                        sub_merchant_id

                                );

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
                                new Prefs(PaymentActivity.this).getToken());
                        //"1c075338b1a8cc4ad6790da302954a2c");
                        params.put("StudentID",
                                //"1");
                                Prefs.with(PaymentActivity.this).getStudent()[0]);
                        params.put("DueAmount",
                                //"1");
                                DueAmount.toString());
                        params.put("StudentFeetructureID",
                                //"1");
                                StudentFeetructureID.toString());
                        return params;
                    }
                };

                // Adding request to request queue
                AppController appController = new AppController();
                appController.setContext(PaymentActivity.this);
                appController.addToRequestQueue(strReq, tag_string_req);












             /* Long tsLong = System.currentTimeMillis()/1000;
              transactionId = tsLong.toString();

              DecimalFormat df = new DecimalFormat("#.0");
              Double amount= Double.valueOf(df.format(dueAmount));

              String[] email=Prefs.with(PaymentActivity.this).getStudent()[1].split("\\s+");

              Intent intentProceed = new Intent(PaymentActivity.this, PWECouponsActivity.class);
              intentProceed.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);     // This is mandatory flag
              intentProceed.putExtra("txnid",transactionId);
              intentProceed.putExtra("amount",amount);
              intentProceed.putExtra("productinfo","Paid Fees for - "+monthBean.getMonthName());
              intentProceed.putExtra("firstname",Prefs.with(PaymentActivity.this).getStudent()[1]);
              intentProceed.putExtra("email",email[0]+"@added.com");
              intentProceed.putExtra("phone",Prefs.with(PaymentActivity.this).getParent()[3]);
              intentProceed.putExtra("key","G1QY20PUUO");
              intentProceed.putExtra("udf1",Prefs.with(PaymentActivity.this).getStudent()[4]); // "Class : " +
              intentProceed.putExtra("udf2", Prefs.with(PaymentActivity.this).getParent()[4]); //"Mother's mobile : " +
              intentProceed.putExtra("udf3",Prefs.with(PaymentActivity.this).getParent()[5]); //"Date of Birth : "+
              intentProceed.putExtra("udf4",Prefs.with(PaymentActivity.this).getStudent()[1]); //"Student Name : "+
              intentProceed.putExtra("udf5","ADDED-PARENT");
              intentProceed.putExtra("address1","unknown");
              intentProceed.putExtra("address2","unknown");
              intentProceed.putExtra("city","Lucknow");
              intentProceed.putExtra("state","UP");
              intentProceed.putExtra("country","india");
              intentProceed.putExtra("zipcode","226001");
              intentProceed.putExtra("hash",getHashes(
                      "G1QY20PUUO"
                      ,transactionId
                      ,String.valueOf(amount)
                      ,"Paid Fees for - "+monthBean.getMonthName()
                      ,Prefs.with(PaymentActivity.this).getStudent()[1]
                      ,Prefs.with(PaymentActivity.this).getStudent()[1]+"@added.com"
                      ,Prefs.with(PaymentActivity.this).getStudent()[4]
                      , Prefs.with(PaymentActivity.this).getParent()[4]
                      ,Prefs.with(PaymentActivity.this).getParent()[5]
                      ,Prefs.with(PaymentActivity.this).getStudent()[1]
                      ,"ADDED-PARENT"
                      ,"WXHKALITQQ"
              ));
              intentProceed.putExtra("unique_id",Prefs.with(PaymentActivity.this).getStudent()[0]);
              intentProceed.putExtra("pay_mode","test"); //production / test
              intentProceed.putExtra("sub_merchant_id","");
              startActivityForResult(intentProceed, PWEStaticDataModel.PWE_REQUEST_CODE);*/



          /*    Long tsLong = System.currentTimeMillis()/1000;
              transactionId = tsLong.toString();

              DecimalFormat df = new DecimalFormat("#.0");
              Double amount= Double.valueOf(df.format(dueAmount));

              String[] email=Prefs.with(PaymentActivity.this).getStudent()[1].split("\\s+");

              Intent intentProceed = new Intent(PaymentActivity.this, PWECouponsActivity.class);
              intentProceed.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);     // This is mandatory flag
              intentProceed.putExtra("txnid",transactionId);
              intentProceed.putExtra("amount",amount);
              intentProceed.putExtra("productinfo","Paid Fees for - "+monthBean.getMonthName());
              intentProceed.putExtra("firstname",Prefs.with(PaymentActivity.this).getStudent()[1]);
              intentProceed.putExtra("email",email[0]+"@added.com");
              intentProceed.putExtra("phone",Prefs.with(PaymentActivity.this).getParent()[3]);
              intentProceed.putExtra("key","G1QY20PUUO");
              intentProceed.putExtra("udf1",Prefs.with(PaymentActivity.this).getStudent()[4]); // "Class : " +
              intentProceed.putExtra("udf2", Prefs.with(PaymentActivity.this).getParent()[4]); //"Mother's mobile : " +
              intentProceed.putExtra("udf3",Prefs.with(PaymentActivity.this).getParent()[5]); //"Date of Birth : "+
              intentProceed.putExtra("udf4",Prefs.with(PaymentActivity.this).getStudent()[1]); //"Student Name : "+
              intentProceed.putExtra("udf5","ADDED-PARENT");
              intentProceed.putExtra("address1","unknown");
              intentProceed.putExtra("address2","unknown");
              intentProceed.putExtra("city","Lucknow");
              intentProceed.putExtra("state","UP");
              intentProceed.putExtra("country","india");
              intentProceed.putExtra("zipcode","226001");
              intentProceed.putExtra("hash",getHashes(
                      "G1QY20PUUO"
                      ,transactionId
                      ,String.valueOf(amount)
                      ,"Paid Fees for - "+monthBean.getMonthName()
                      ,Prefs.with(PaymentActivity.this).getStudent()[1]
                      ,email[0]+"@added.com"
                      ,Prefs.with(PaymentActivity.this).getStudent()[4]
                      , Prefs.with(PaymentActivity.this).getParent()[4]
                      ,Prefs.with(PaymentActivity.this).getParent()[5]
                      ,Prefs.with(PaymentActivity.this).getStudent()[1]
                      ,"ADDED-PARENT"
                      ,"WXHKALITQQ"
              ));
              intentProceed.putExtra("unique_id",Prefs.with(PaymentActivity.this).getStudent()[0]);
              intentProceed.putExtra("pay_mode","test"); //production / test
              intentProceed.putExtra("sub_merchant_id","");
              startActivityForResult(intentProceed, PWEStaticDataModel.PWE_REQUEST_CODE);*/

            }

        });
    }

    private void submitPaymentGateway(String txnid, String amount, String productinfo, String firstname, String email, String phone, String key, String salt, String udf1, String udf2, String udf3, String udf31, String udf4, String udf5, String address1, String city, String state, String country, String zipcode, String unique_id, String zipcode1, String pay_mode, String sub_merchant_id) {


        DecimalFormat df = new DecimalFormat("#.0");
        Double dueamount = Double.valueOf(df.format(Integer.parseInt(amount)));


        Intent intentProceed = new Intent(PaymentActivity.this, PWECouponsActivity.class);
        intentProceed.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);     // This is mandatory flag
        intentProceed.putExtra("txnid", txnid);
        intentProceed.putExtra("amount", dueamount);
        intentProceed.putExtra("productinfo", productinfo);
        intentProceed.putExtra("firstname", firstname);
        intentProceed.putExtra("email", email);
        intentProceed.putExtra("phone", phone);
        intentProceed.putExtra("key", key);
        intentProceed.putExtra("udf1", udf1); // "Class : " +
        intentProceed.putExtra("udf2", udf2); //"Mother's mobile : " +
        intentProceed.putExtra("udf3", udf3); //"Date of Birth : "+
        intentProceed.putExtra("udf4", udf4); //"Student Name : "+
        intentProceed.putExtra("udf5", udf5);
        intentProceed.putExtra("address1", address1);
        intentProceed.putExtra("address2", address1);
        intentProceed.putExtra("city", city);
        intentProceed.putExtra("state", state);
        intentProceed.putExtra("country", country);
        intentProceed.putExtra("zipcode", zipcode);
        intentProceed.putExtra("surl", "https://skoolics.com/");
        intentProceed.putExtra("furl", "https://addedschools.com/");
        intentProceed.putExtra("hash", getHashes(
                key
                , txnid
                , String.valueOf(dueamount)
                , productinfo
                , firstname
                , email
                , udf1
                , udf2
                , udf3
                , udf4
                , udf5
                , salt
        ));
        intentProceed.putExtra("unique_id", unique_id);
        intentProceed.putExtra("pay_mode", pay_mode); //production / test
        intentProceed.putExtra("sub_merchant_id", sub_merchant_id);
        startActivityForResult(intentProceed, PWEStaticDataModel.PWE_REQUEST_CODE);
     /*   registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onActivityResults(PWEStaticDataModel.PWE_REQUEST_CODE,result.getResultCode(),result.getData());
            }
        }).launch(intentProceed);*/

    }

    protected void onActivityResults(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == PWEStaticDataModel.PWE_REQUEST_CODE) {
                //   String result = data.getStringExtra("result");
                String payment_response = data.getStringExtra("payment_response");
                //  Log.v("payment_response",payment_response);
                try {
                    JSONObject jsonObject = new JSONObject(payment_response);
                    if (jsonObject.getString("status").equals("success")) {


                        String tag_string_req = "req_login";
                        progressDialog.show();

                        StringRequest strReq = new StringRequest(Request.Method.POST,
                                new AppConfig().AFTER_PAYMENT, new Response.Listener<String>() {

                            @Override
                            public void onResponse(final String response) {
                                progressDialog.dismiss();
                                Log.e(TAG, "Response: " + response);
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if (object.getString("error").equals("0")) {
                                        //      JSONObject dataObject = object.getJSONObject("data");
                                        //    Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                      /*  String msg = "Payment Successful";
                                        Snackbar snackbar = Snackbar
                                                .make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
                                        snackbar.show();*/
                                        Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

                                        //  binding.rvFee.setAdapter(new AdvancedFeeAdapter( list, PaymentActivity.this));
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
                                        new Prefs(PaymentActivity.this).getToken());
                                //"1c075338b1a8cc4ad6790da302954a2c");
                                params.put("StudentID",
                                        //"1");
                                        Prefs.with(PaymentActivity.this).getStudent()[0]);
                                params.put("CentralFeeTransactionID",
                                        //"1");
                                        CentralFeeTransactionID);
                                params.put("TransactionID",
                                        //"1");
                                        txnid);

                                params.put("PaymentStatus",
                                        //"1");
                                        "1");

                                return params;
                            }
                        };

                        // Adding request to request queue
                        AppController appController = new AppController();
                        appController.setContext(PaymentActivity.this);
                        appController.addToRequestQueue(strReq, tag_string_req);

                    } else {
                        //    Toast.makeText(PaymentActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        String tag_string_req = "req_login";
                        progressDialog.show();

                        StringRequest strReq = new StringRequest(Request.Method.POST,
                                new AppConfig().AFTER_PAYMENT, new Response.Listener<String>() {

                            @Override
                            public void onResponse(final String response) {
                                progressDialog.dismiss();
                                Log.e(TAG, "Response: " + response);
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if (object.getString("error").equals("0")) {
                                        //      JSONObject dataObject = object.getJSONObject("data");
                                        //    Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                       /* String msg = "Payment Failed";
                                        Snackbar snackbar = Snackbar
                                                .make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
                                        snackbar.show();*/
                                        Toast.makeText(PaymentActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                                        //  binding.rvFee.setAdapter(new AdvancedFeeAdapter( list, PaymentActivity.this));
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
                                        new Prefs(PaymentActivity.this).getToken());
                                //"1c075338b1a8cc4ad6790da302954a2c");
                                params.put("StudentID",
                                        //"1");
                                        Prefs.with(PaymentActivity.this).getStudent()[0]);
                                params.put("CentralFeeTransactionID",
                                        //"1");
                                        CentralFeeTransactionID);
                                params.put("TransactionID",
                                        //"1");
                                        txnid);

                                params.put("PaymentStatus",
                                        //"1");
                                        "0");

                                return params;
                            }
                        };

                        // Adding request to request queue
                        AppController appController = new AppController();
                        appController.setContext(PaymentActivity.this);
                        appController.addToRequestQueue(strReq, tag_string_req);

                    }

                    // Handle response here
                } catch (Exception e) {
                    // Handle exception here
                }
            }
        }
    }

    public String getHashes(String key, String txnid, String amount, String productInfo, String firstname, String email,
                            String udf1, String udf2, String udf3, String udf4, String udf5, String salt) {
        String paymentHash = null;
        try {
            String ph = checkNull(key) + "|" + checkNull(txnid) + "|" + checkNull(amount) + "|" + checkNull(productInfo)
                    + "|" + checkNull(firstname) + "|" + checkNull(email) + "|" + checkNull(udf1) + "|" + checkNull(udf2)
                    + "|" + checkNull(udf3) + "|" + checkNull(udf4) + "|" + checkNull(udf5) + "||||||" + salt + "|" + checkNull(key);

            Log.v("phhhhh", ph);
            paymentHash = getSHA(ph);

        } catch (Exception e) {

        }
        return paymentHash;

    }


    private String checkNull(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    private String getSHA(String str) {

        MessageDigest md;
        String out = "";
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes());
            byte[] mb = md.digest();

            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return out;

    }

    public void fetchFee() {
        DueAmount = new ArrayList<>();
        StudentFeetructureID = new ArrayList<>();
        binding.tvDetail.setText("Total Amount : Rs. 0");
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_FEE_PAYMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        //  List<FeeGroupModel> list = initializeAdvancedList(dataObject.getJSONObject("fee_details").getJSONArray("Month"),dataObject.getInt("FeeSubmissionLastMonthPriority"));
                        List<PaymentFeeGroupModel> list = initializeAdvancedList(dataObject.getJSONObject("fee_details").getJSONArray("Month"), dataObject.getInt("FeeSubmissionLastMonthPriority"));

                        Gson gson = new Gson();
                        //    String json = gson.toJson(response); // serializes target to Json
                        paymentFeesModel = gson.fromJson(response, PaymentFeesModel.class);
                        //  binding.spMonth.setAdapter(new PaymentMonthpinnerAdapter(PaymentActivity.this, paymentFeesModel.getData().getFee_details()));

                        advancedFeePaymentAdapter = new AdvancedFeePaymentAdapter(list, PaymentActivity.this, new AdvancedFeePaymentAdapter.onClickLitener() {

                            @Override
                            public void headerCheck(int position1, String title) {
                                Log.v("SSSSSSSSS", String.valueOf(position1));
                                boolean flag = true;
                                int position = position1;
                                for (int k = 0; k < paymentFeesModel.getData().getFee_details().getMonth().size(); k++) {

                                    if (paymentFeesModel.getData().getFee_details().getMonth().get(k).getMonthName().equals(title)) {
                                        position = k;
                                        k = paymentFeesModel.getData().getFee_details().getMonth().size();
                                    }
                                }

                                for (int i = 0; i < paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().size(); i++) {
                                    //    dueAmount=dueAmount+ Integer.parseInt(paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().get(i).getDueAmount());


                                    //      dueAmount=dueAmount+ Integer.parseInt(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getDueAmount());

                                    for (int j = 0; j < StudentFeetructureID.size(); j++) {
                                        if (paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().get(i).getStudentFeeStructureID().equals(StudentFeetructureID.get(j))) {
                                            // StudentFeetructureID.remove(i);
                                            //   StudentFeetructureID.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getStudentFeeStructureID());
                                            // DueAmount.remove(i);
                                            flag = false;
                                            i = StudentFeetructureID.size();
                                            //   DueAmount.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getDueAmount());
                                        } else {
                                            flag = true;
                                        }
                                    }
                                    if (flag) {
                                        StudentFeetructureID.add(paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().get(i).getStudentFeeStructureID());
                                        DueAmount.add(paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().get(i).getDueAmount());

                                    }


                                }
                                Log.v("ARRAY_DATA", DueAmount.toString());
                                Log.v("ARRAY_DATA", StudentFeetructureID.toString());
                                int dueAmount = 0;
                                for (int i = 0; i < DueAmount.size(); i++) {
                                    dueAmount = dueAmount + Integer.parseInt(DueAmount.get(i));
                                }
                                binding.tvDetail.setText("Total Amount : Rs. " + dueAmount);
                                //  advancedFeePaymentAdapter.notifyDataSetChanged();
                                //     advancedFeePaymentAdapter.notifyItemChanged(position);

                            }

                            @Override
                            public void headerUnCheck(int position1, String title) {
                                Log.v("SSSSSSSSS", String.valueOf(position1));
                                boolean flag = true;
                                int position = position1;
                                for (int k = 0; k < paymentFeesModel.getData().getFee_details().getMonth().size(); k++) {

                                    if (paymentFeesModel.getData().getFee_details().getMonth().get(k).getMonthName().equals(title)) {
                                        position = k;
                                        k = paymentFeesModel.getData().getFee_details().getMonth().size();
                                    }
                                }
                                for (int i = 0; i < paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().size(); i++) {
                                    //     dueAmount=dueAmount- Integer.parseInt(paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().get(i).getDueAmount());


                                    for (int j = 0; j < StudentFeetructureID.size(); j++) {
                                        if (paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().get(i).getStudentFeeStructureID().equals(StudentFeetructureID.get(j))) {
                                            StudentFeetructureID.remove(j);
                                            //   StudentFeetructureID.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getStudentFeeStructureID());
                                            DueAmount.remove(j);
                                            //   DueAmount.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getDueAmount());
                                        }
                                    }


                                    //  StudentFeetructureID.remove();
                                    //      DueAmount.remove(paymentFeesModel.getData().getFee_details().getMonth().get(position).getFeeHeadDetails().get(i).getDueAmount());

                                }
                                Log.v("ARRAY_DATA", DueAmount.toString());
                                Log.v("ARRAY_DATA", StudentFeetructureID.toString());
                                int dueAmount = 0;
                                for (int i = 0; i < DueAmount.size(); i++) {
                                    dueAmount = dueAmount + Integer.parseInt(DueAmount.get(i));
                                }
                                binding.tvDetail.setText("Total Amount : Rs. " + dueAmount);
                                //   advancedFeePaymentAdapter.notifyDataSetChanged();
                                //  advancedFeePaymentAdapter.notifyItemChanged(position);
                            }

                            @Override
                            public void childCheck(int childPosition, int parentPosition, String StudentFeeStructureID, String dueAmount1) {

                                Log.v("ARRAY_DATA", String.valueOf(childPosition) + " ,,," + String.valueOf(parentPosition));
                                boolean flag = true;
                                //      dueAmount=dueAmount+ Integer.parseInt(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getDueAmount());

                                for (int i = 0; i < StudentFeetructureID.size(); i++) {
                                    if (StudentFeeStructureID.equals(StudentFeetructureID.get(i))) {
                                        // StudentFeetructureID.remove(i);
                                        //   StudentFeetructureID.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getStudentFeeStructureID());
                                        // DueAmount.remove(i);
                                        flag = false;
                                        i = StudentFeetructureID.size();
                                        //   DueAmount.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getDueAmount());
                                    } else {
                                        flag = true;
                                    }
                                }
                                if (flag) {
                                    StudentFeetructureID.add(StudentFeeStructureID);
                                    DueAmount.add(dueAmount1);

                                }


                                Log.v("ARRAY_DATA", DueAmount.toString());
                                Log.v("ARRAY_DATA", StudentFeetructureID.toString());
                                int dueAmount = 0;
                                for (int i = 0; i < DueAmount.size(); i++) {
                                    dueAmount = dueAmount + Integer.parseInt(DueAmount.get(i));
                                }
                                binding.tvDetail.setText("Total Amount : Rs. " + dueAmount);
                                //    advancedFeePaymentAdapter.notifyItemChanged(parentPosition);
                                //dueAmount=dueAmount+dueamount;
                                //  Toast.makeText(PaymentActivity.this, list.get(position), Toast.LENGTH_SHORT).show();
                                //    binding.tvDetail.setText("Total Due Fee : Rs. "+dueAmount);
                            }

                            @Override
                            public void childUnCheck(int childPosition, int parentPosition, String StudentFeeStructureID, String dueAmount1) {
                                //    dueAmount=dueAmount- Integer.parseInt(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getDueAmount());

                                Log.v("ARRAY_DATA", String.valueOf(childPosition) + " ,,," + String.valueOf(parentPosition));

                                for (int i = 0; i < StudentFeetructureID.size(); i++) {
                                    if (StudentFeeStructureID.equals(StudentFeetructureID.get(i))) {
                                        StudentFeetructureID.remove(i);
                                        //   StudentFeetructureID.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getStudentFeeStructureID());
                                        DueAmount.remove(i);
                                        //   DueAmount.remove(paymentFeesModel.getData().getFee_details().getMonth().get(parentPosition).getFeeHeadDetails().get(childPosition).getDueAmount());
                                    }
                                }

                                Log.v("ARRAY_DATA", DueAmount.toString());
                                Log.v("ARRAY_DATA", StudentFeetructureID.toString());
                                int dueAmount = 0;
                                for (int i = 0; i < DueAmount.size(); i++) {
                                    dueAmount = dueAmount + Integer.parseInt(DueAmount.get(i));
                                }
                                binding.tvDetail.setText("Total Amount : Rs. " + dueAmount);
                                //    advancedFeePaymentAdapter.notifyItemChanged(parentPosition);
                                //    dueAmount=dueAmount-dueamount;
                                //  binding.tvDetail.setText("Total Due Fee : Rs. "+dueAmount);
                            }
                        });

                        binding.rvFee.setAdapter(advancedFeePaymentAdapter);

                        //  binding.rvFee.notifyAll();
                        //  advancedFeePaymentAdapter.notifyDataSetChanged();

                       /* advancedFeePaymentAdapter.setOnGroupClickListener(new OnGroupClickListener() {
                            //  int previousGroup = -1;
                            @Override
                            public boolean onGroupClick(int flatpos) {
                               // Toast.makeText(PaymentActivity.this, String.valueOf(flatPos), Toast.LENGTH_SHORT).show();
                                flatPos=flatpos;
                                return true;
                            }
                        });

                        advancedFeePaymentAdapter.setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {

                            @Override
                            public void onGroupExpanded(ExpandableGroup group) {
                                for (int i = 0; i < advancedFeePaymentAdapter.getGroups().size(); i++) {
                                if (flatPos==i) {
                                  //  advancedFeePaymentAdapter.
                                    expandGroup(i);
                                }
                                }
                            }

                            @Override
                            public void onGroupCollapsed(ExpandableGroup group) {
                                for (int i = 0; i < advancedFeePaymentAdapter.getGroups().size(); i++) {
                                  //  if (flatPos!)
                                    collpaseGroup(i);
                                }

                            }
                        });*/

                        //  binding.rvFee.setAdapter(new AdvancedFeeAdapter( list, PaymentActivity.this));

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
                        new Prefs(PaymentActivity.this).getToken());
                //"1c075338b1a8cc4ad6790da302954a2c");
                params.put("StudentID",
                        //"1");
                        Prefs.with(PaymentActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(PaymentActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void expandGroup(int gPos) {
        if (advancedFeePaymentAdapter.isGroupExpanded(gPos)) {
            advancedFeePaymentAdapter.notifyItemChanged(gPos);

            return;
        }
        advancedFeePaymentAdapter.toggleGroup(gPos);
        advancedFeePaymentAdapter.notifyItemChanged(gPos);
    }

    public void collpaseGroup(int gPos) {
        if (!advancedFeePaymentAdapter.isGroupExpanded(gPos)) {
            advancedFeePaymentAdapter.notifyItemChanged(gPos);
            return;
        }
        advancedFeePaymentAdapter.toggleGroup(gPos);
        advancedFeePaymentAdapter.notifyItemChanged(gPos);
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
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

    private List<PaymentFeeGroupModel> initializeAdvancedList(JSONArray array, int lastMonthFeePriority) {
        List<PaymentFeeGroupModel> tempList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String monthName = object.getString("MonthName");
                //   int FeePriority = object.getInt("FeePriority");
                String[] amountPayable;
                String[] amountPaid;
                String[] dueAmount;
                String[] feeHead;
                String[] StudentFeeStructureID;
                JSONArray jsonArray = object.getJSONArray("FeeHeadDetails");
                amountPayable = new String[jsonArray.length()];
                amountPaid = new String[jsonArray.length()];
                dueAmount = new String[jsonArray.length()];
                feeHead = new String[jsonArray.length()];
                StudentFeeStructureID = new String[jsonArray.length()];
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject object1 = jsonArray.getJSONObject(j);
                    amountPayable[j] = object1.getString("AmountPayable");
                    amountPaid[j] = object1.getString("AmountPaid");
                    dueAmount[j] = object1.getString("DueAmount");
                    feeHead[j] = object1.getString("FeeHead");
                    StudentFeeStructureID[j] = object1.getString("StudentFeeStructureID");
                }

                int feePriority = 0;
                int feeStatus = 0;
                int dueAmt = 0;
                int total = 0;
                int due = 0;
                for (int k = 0; k < amountPayable.length; k++) {
                    total = total + Integer.parseInt(amountPayable[k]);
                    due = due + Integer.parseInt(dueAmount[k]);
                    feePriority = object.getInt("FeePriority");


                    if (feePriority <= lastMonthFeePriority) {
                        dueAmt = dueAmt + Integer.parseInt(dueAmount[k]);
                        feeStatus = dueAmt > 0 ? -1 : 1;
                    } else {
                        feeStatus = 0;
                    }


                }


                PaymentFeeModel feeModel = new PaymentFeeModel(monthName, amountPayable, amountPaid, dueAmount, feeHead, StudentFeeStructureID, feePriority);
                List<PaymentFeeModel> oneItemList = new ArrayList<>();
                oneItemList.add(feeModel);
                PaymentFeeGroupModel feeGroupModel = new PaymentFeeGroupModel(monthName, oneItemList, feeStatus);
                tempList.add(feeGroupModel);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // binding.tvDetail.setText("Total Fee : "+total+"   |   Due Fee : "+due);
        return tempList;
    }
}
