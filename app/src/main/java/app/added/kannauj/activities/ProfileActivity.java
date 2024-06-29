package app.added.kannauj.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.added.kannauj.R;
import app.added.kannauj.Utils.CircleTransform;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.Utils.VolleyMultipartRequest;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.databinding.ActivityMyAccountBinding;


public class ProfileActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE = 100;
    Bitmap bitmap = null;
    ActivityMyAccountBinding binding;
    ArrayList<Bitmap> encodedImage = null;
    Prefs prefs;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        initialize();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProfileImageClick();
            }
        });
        ImagePickerActivity.clearCache(this);
    }


    protected void onActivityResults(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    encodedImage = new ArrayList();
                    Bitmap selectedImage = null;

                    for (int i = 0; i < 1; i++) {
                        final InputStream imageStream = getContentResolver().openInputStream(uri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        encodedImage.add(i, selectedImage);

                    }
                    // loading profile image from local cache
                    loadProfile(uri.toString());
                    //  SharedPrefHelper.putSharedPreferencesString(mContext, SharedPrefHelper.SpKeys.PROFILE_IMAGE, uri.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadProfile(String url) {
        Log.d("Profile", "Image cache path: " + url);
        Picasso.get().load(url).placeholder(R.drawable.student).transform(new CircleTransform()).into(binding.ivImg);
        openAlert();
    }

    private void openAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Profile Picture Update");
        builder.setMessage("Are you sure.you want to upload this profile image");
        builder.setPositiveButton("Sure", (dialog, which) -> {
            dialog.cancel();
            updateData();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("dialog_permission_title");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onActivityResults(101,result.getResultCode(),result.getData());
            }
        }).launch(intent);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onActivityResults(REQUEST_IMAGE,result.getResultCode(),result.getData());
            }
        }).launch(intent);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onActivityResults(REQUEST_IMAGE,result.getResultCode(),result.getData());
            }
        }).launch(intent);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        return imgString;
    }

    private void updateData() {
        if (encodedImage != null && encodedImage.size() > 0) {


            for (int i = 0; i < encodedImage.size(); i++) {

                //   final Bitmap converetdImage = getResizedBitmap(encodedImage.get(i), 500);
                final Bitmap converetdImage = encodedImage.get(i);
                //getting the tag from the edittext
                //   final String tags = editTextTags.getText().toString().trim();

                progressDialog.show();
                final VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, new AppConfig().URL_UPDATE_PROFILE_IMAGE,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                progressDialog.dismiss();
                                Log.v("SUDI", "Response: " + response.data);
                                try {
                                    String respose = new String(response.data);
                                    Log.v("SUDI", "Response: " + respose);
                                    JSONObject object = new JSONObject(respose);
                                    if (object.getString("error").equals("0")) {
                                        String msg = object.getString("message");
                                        Snackbar snackbar = Snackbar
                                                .make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
                                        snackbar.show();
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
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("", "Error Response: " + error.toString());
                                progressDialog.dismiss();
                                Snackbar snackbar = Snackbar
                                        .make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                //showReloadAlerts();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Token", new Prefs(ProfileActivity.this).getToken());
                        params.put("StudentID", new Prefs(ProfileActivity.this).getStudent()[0]);
                        Log.v("data", params.toString());
                        return params;
                    }

                    /*
                     * Here we are passing image by renaming it with a unique name
                     * */
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        long imagename = System.currentTimeMillis();
                        params.put("StudentPhoto", new DataPart(imagename + ".png", getFileDataFromDrawable(converetdImage), ".png"));
                        Log.v("para", params.toString());
                        return params;
                    }
                };

                //adding the request to volley
                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                volleyMultipartRequest.setShouldCache(false);
                Volley.newRequestQueue(ProfileActivity.this).add(volleyMultipartRequest);
            }
        } else {
            Toast.makeText(ProfileActivity.this, "Please select a image", Toast.LENGTH_LONG).show();
        }


    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void initialize() {
        prefs = Prefs.with(ProfileActivity.this);
        if (!prefs.getStudent()[5].isEmpty()) {
            Picasso.get().load(prefs.getStudent()[5]).placeholder(R.drawable.student).transform(new CircleTransform()).into(binding.ivImg);

        } else {
            Picasso.get().load(R.drawable.student).into(binding.ivImg);
        }
        binding.tvName.setText("Name : " + prefs.getStudent()[1]);
        binding.tvClass.setText("Class : " + prefs.getStudent()[4]);
        binding.tvFatherName.setText("Father's name : " + prefs.getParent()[0]);
        binding.tvMotherName.setText("Mother's name : " + prefs.getParent()[1]);
        binding.tvFatherMobileNumber.setText("Father's mobile : " + prefs.getParent()[3]);
        binding.tvMotherMobileNumber.setText("Mother's mobile : " + prefs.getParent()[4]);
        binding.tvDob.setText("Date of Birth : " + prefs.getParent()[5]);
    }


}
