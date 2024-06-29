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

import app.added.kannauj.Adapters.GalleryAlbumAdapter;
import app.added.kannauj.Models.GalleryModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityGalleryBinding;


public class GalleryActivity extends AppCompatActivity {

    ActivityGalleryBinding binding;
    ProgressDialog progressDialog;
    private static final String TAG = "GalleryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        initialize();
    }

    public void initialize() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        fetchGalleryEvents();
    }

    public void fetchGalleryEvents() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_GALLERY_EVENTS , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        String imageURL = dataObject.getString("event_images_path");
                        List<GalleryModel> galleryList =  initializeGalleryList(dataObject);
                        binding.rvGallery.setAdapter(new GalleryAlbumAdapter(GalleryActivity.this, imageURL,galleryList));
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
                params.put("Token",new Prefs(GalleryActivity.this).getToken());
                params.put("StudentID",Prefs.with(GalleryActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(GalleryActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private List<GalleryModel> initializeGalleryList(JSONObject dataObject) {
        JSONArray eventsArray = null;
        List<GalleryModel> galleryList = new ArrayList<>();
        GalleryModel galleryModel = null;
        try {
            eventsArray = dataObject.getJSONArray("events_list");
            for (int i=0;i<eventsArray.length();i++) {
                JSONObject galleryObject = eventsArray.getJSONObject(i);
                galleryModel = new GalleryModel(galleryObject.getString("id"), galleryObject.getString("Name"), galleryObject.getString("Description"), galleryObject.getInt("IsActive"), galleryObject.getString("ImageName"), galleryObject.getString("CreateDate"), galleryObject.getString("TotalImages"));
                galleryList.add(galleryModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  galleryList;
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
        builder.setTitle("Gallery");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchGalleryEvents();
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
