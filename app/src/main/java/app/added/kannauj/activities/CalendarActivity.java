
package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.added.kannauj.CustomViews.CalendarViewWidget;
import app.added.kannauj.Models.CalendarEventModel;
import app.added.kannauj.Models.DateModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;

import static android.widget.Toast.LENGTH_SHORT;

public class CalendarActivity extends AppCompatActivity {

    CalendarViewWidget calendarView;
    List<DateModel> dateList;
    private static final String TAG = "CalendarActivity";
    ProgressDialog progressDialog;
    LinearLayout parentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initialize();
    }

    private void initialize() {
        parentLayout = findViewById(R.id.parentLayout);
        calendarView = findViewById(R.id.calendar_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

//        HashSet<Date> events = new HashSet<>();
        dateList = new ArrayList<>();
        //Date d=null,d1=null;
//        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // here set the pattern as you date in string was containing like date/month/year
//            d = sdf.parse("19/02/2019");
//            d1 = sdf.parse("20/02/2019");
//        }catch(ParseException ex){
//            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
//            Log.e(TAG, ex.getMessage());
//        }
//        events.add(d);
//        dateList.add(new DateModel(d,0));
//        dateList.add(new DateModel(d1,1));
//        Log.e("date",d+"");
        //calendarView.updateCalendar(dateList);
        //calendarView.initializeCalendarData(dateList);
        calendarView.setEventHandler(new CalendarViewWidget.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {

            }

            @Override
            public void onDayPress(Date date) {
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(CalendarActivity.this, df.format(date),  LENGTH_SHORT).show();
            }
        });
        if (getIntent().getStringExtra("type").equalsIgnoreCase("atn")) {
            fetchAttendance();
        } else {
            fetchCalendar();
        }
        //fetchCalendar();
    }

    public void fetchCalendar() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_ACADEMIC_CALENDAR , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        //List<DateModel> weekOffList = getListFromStringJSONArray(dataObject.getJSONArray("Week_offs"));
                        List<CalendarEventModel> eventList = initializeEventList(dataObject.getJSONArray("academic_calendar_event"));
                        calendarView.setEventsAdapter(eventList);
                        //dateList = weekOffList;
                        calendarView.initializeCalendarData(dateList);
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
                params.put("Token",new Prefs(CalendarActivity.this).getToken());
                params.put("StudentID",Prefs.with(CalendarActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(CalendarActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void fetchAttendance() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_ATTENDANCE , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        List<DateModel> weekOffList = getListFromStringJSONArray(dataObject.getJSONArray("attendance_list"));
                        calendarView.setAttendenceEventsAdapter(weekOffList);
                        dateList = weekOffList;
                        calendarView.initializeCalendarData(dateList);
                        calendarView.showAttendanceDetail();
                        calendarView.updateAttendanceDetail(Calendar.MONTH);

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
                params.put("Token",
                        //"08b461d5b9d51b083343277f9f84b64a");
                        new Prefs(CalendarActivity.this).getToken());
                params.put("StudentID",new Prefs(CalendarActivity.this).getStudent()[0]);
                        //"71");
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(CalendarActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }



    private List<DateModel> getListFromStringJSONArray(JSONArray array) {
        List<DateModel> list = new ArrayList<>();
        for (int i=0;i<array.length();i++) {
            try {
                Date date = getDateFromString(array.getJSONObject(i).getString("Date"));
                if (date!=null)
                    list.add(new DateModel(date, 0, array.getJSONObject(i).getString("Status")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private Date getDateFromString(String str) {
        Date d=null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // here set the pattern as you date in string was containing like date/month/year
            d = sdf.parse(str);
            //(TAG,d.toString());
        }catch(ParseException ex){
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            //Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        return d;
    }

    private List<CalendarEventModel> initializeEventList(JSONArray array) {
        List<CalendarEventModel> list = new ArrayList<>();
        for (int i=0;i<array.length();i++) {
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                list.add(new CalendarEventModel(jsonObject.getString("id")
                    , jsonObject.getString("EventName")
                    , jsonObject.getString("EventDetails")
                    , getDateFromString(jsonObject.getString("EventDate"))));
                dateList.add(new DateModel(getDateFromString(jsonObject.getString("EventDate")),1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
        builder.setTitle("Calendar");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchCalendar();
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

