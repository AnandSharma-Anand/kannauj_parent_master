package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.util.Map;

import app.added.kannauj.Adapters.AlbumGridAdapter;
import app.added.kannauj.Adapters.AlbumListAdapter;
import app.added.kannauj.Models.GalleryModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityAlbumBinding;


public class AlbumActivity extends AppCompatActivity {
    ActivityAlbumBinding binding;
    GridLayoutManager gridLayoutManager;
    ProgressDialog progressDialog;
    private static final String TAG = "AlbumActivity";
    String imageURL = "";
    ArrayList<GalleryModel> albumList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album);
        initialize();
    }

    public void initialize() {
        gridLayoutManager = new GridLayoutManager(AlbumActivity.this,3);
        binding.rvAlbum.setLayoutManager(gridLayoutManager);
        binding.tvGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvGrid.setTextColor(Color.parseColor("#c7c9ca"));
                binding.tvList.setTextColor(Color.parseColor("#787a7c"));
                binding.tvGrid.setTypeface(null, Typeface.BOLD);
                binding.tvList.setTypeface(null, Typeface.NORMAL);
                binding.rvAlbum.setLayoutManager(gridLayoutManager);
                binding.rvAlbum.setAdapter(new AlbumGridAdapter(AlbumActivity.this, albumList, imageURL));
            }
        });
        binding.tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvGrid.setTextColor(Color.parseColor("#787a7c"));
                binding.tvList.setTextColor(Color.parseColor("#c7c9ca"));
                binding.tvGrid.setTypeface(null, Typeface.NORMAL);
                binding.tvList.setTypeface(null, Typeface.BOLD);
                binding.rvAlbum.setLayoutManager(new LinearLayoutManager(AlbumActivity.this));
                binding.rvAlbum.setAdapter(new AlbumListAdapter(AlbumActivity.this, albumList, imageURL));
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        fetchAlbum();
    }

    public void fetchAlbum() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                new AppConfig().URL_ALBUM , new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        imageURL = dataObject.getString("event_images_path");
                        albumList =  initializeAlbumList(dataObject);
                        binding.rvAlbum.setAdapter(new AlbumGridAdapter(AlbumActivity.this, albumList, imageURL));
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",new Prefs(AlbumActivity.this).getToken());
                params.put("EventGalleryID",getIntent().getStringExtra("id"));
                params.put("StudentID",Prefs.with(AlbumActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(AlbumActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private ArrayList<GalleryModel> initializeAlbumList(JSONObject dataObject) {
        JSONArray imagesArray = null;
        ArrayList<GalleryModel> albumList = new ArrayList<>();
        GalleryModel galleryModel = null;
        try {
            imagesArray = dataObject.getJSONArray("events_images");
            for (int i=0;i<imagesArray.length();i++) {
                JSONObject galleryObject = imagesArray.getJSONObject(i);
                galleryModel = new GalleryModel(galleryObject.getString("id"), "", galleryObject.getString("Description"), 1, galleryObject.getString("ImageName"), galleryObject.getString("CreateDate")/*,galleryObject.getString("TotalImages")*/, "");
                albumList.add(galleryModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  albumList;
    }


}
