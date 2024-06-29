package app.added.kannauj.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import app.added.kannauj.Adapters.FeedAdapter;
import app.added.kannauj.Models.FeedModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.FeedItemDecoration;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.FragmentFeedBinding;


public class FeedFragment extends Fragment {

    FragmentFeedBinding binding;
    ProgressDialog progressDialog;
    private static final String TAG = "FeedFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvNotice.setLayoutManager(linearLayoutManager);
        //binding.rvNotice.setAdapter(new NoticeAdapter(getActivity()));
        binding.rvNotice.addItemDecoration(new FeedItemDecoration(50,50,9));
    }
    
    private void initialize() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        fetchNotice();
    }

    public void fetchNotice() {
        String tag_string_req = "req_login";
        //progressDialog.show();
        binding.rvNotice.setVisibility(View.GONE);
        binding.shimmerContainer.setVisibility(View.VISIBLE);
        binding.shimmerContainer.startShimmer();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_FEED , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                //progressDialog.dismiss();
                binding.shimmerContainer.stopShimmer();
                binding.shimmerContainer.setVisibility(View.GONE);
                binding.rvNotice.setVisibility(View.VISIBLE);
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        String imagePath = dataObject.getString("event_images_path");
                        JSONArray feedJSONArray = dataObject.getJSONArray("news_feeds");
                        List<FeedModel> feedList = initializeFeedList(feedJSONArray);
                        binding.rvNotice.setAdapter(new FeedAdapter(getActivity(),feedList,imagePath));
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
                //progressDialog.dismiss();
                binding.shimmerContainer.stopShimmer();
                binding.shimmerContainer.setVisibility(View.GONE);
                binding.rvNotice.setVisibility(View.VISIBLE);
                Snackbar snackbar = Snackbar
                        .make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",
                        //"1c075338b1a8cc4ad6790da302954a2c");
                        new Prefs(getActivity()).getToken());
                params.put("StudentID",
                        //"1");
                        Prefs.with(getActivity()).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(getActivity());
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private List<FeedModel> initializeFeedList(JSONArray array) {
        List<FeedModel> list = new ArrayList<>();
        for (int i=0; i< array.length(); i++) {
            try {
                JSONObject feedObject = array.getJSONObject(i);
                FeedModel model = null;
                String type = feedObject.getString("ItemType");
                switch (type) {
                    case "Notice":
                        model = new FeedModel(feedObject.getString("RecordID"),type);
                        model.setNoticeSubject(feedObject.getString("NoticeCircularSubject"));
                        model.setNoticeDetail(feedObject.getString("NoticeCircularDetails"));
                        model.setDate(getDateFromString(feedObject.getString("Date")));
                        break;
                    case "Holiday":
                        model = new FeedModel(feedObject.getString("RecordID"),type);
                        model.setHolidayName(feedObject.getString("EventName"));
                        model.setHolidayDetail(feedObject.getString("EventDetails"));
                        model.setHolidayStartDate(getDateFromString(feedObject.getString("EventStartDate")));
                        model.setHolidayEndDate(getDateFromString(feedObject.getString("EventEndDate")));
                        model.setDate(getDateFromString(feedObject.getString("Date")));
                        break;
                    case "EventGalleryImage":
                        model = new FeedModel(feedObject.getString("RecordID"),type);
                        model.setGalleryImageName(feedObject.getString("ImageName"));
                        model.setGalleryImageDescription(feedObject.getString("ImageDescription"));
                        model.setDate(getDateFromString(feedObject.getString("Date")));
                        break;
                    case "DueFeeStatus":
                        model = new FeedModel(feedObject.getString("RecordID"),type);
                        model.setDueAmt("Rs. " + feedObject.getString("DueAmount"));
                        model.setDate(getDateFromString(feedObject.getString("Date")));
                        break;
                    case "Homework":
                        model = new FeedModel(feedObject.getString("RecordID"),type);
                        model.setSubject(feedObject.getString("Subject"));
                        model.setChapter(feedObject.getString("ChapterName"));
                        model.setTopic(feedObject.getString("TopicName"));
                        model.setAssignmentHeading(feedObject.getString("AssignmentHeading"));
                        model.setAssignmentDetail(feedObject.getString("Assignment"));
                        model.setDate(getDateFromString(feedObject.getString("Date")));
                        break;

                }
                if (model!=null) {
                    list.add(model);
                    //Log.e(TAG, model.getType());
                }
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
//            Log.e(TAG,d.toString());
        }catch(ParseException ex){
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            //Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        return d;
    }

}
