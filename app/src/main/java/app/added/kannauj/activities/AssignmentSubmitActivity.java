package app.added.kannauj.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import app.added.kannauj.Adapters.AdvancedAssignmentAdapter;
import app.added.kannauj.Adapters.AdvancedSubmitAssignmentAdapter;
import app.added.kannauj.Adapters.SubjectListSpinnerAdapter;
import app.added.kannauj.Models.AssignmentGroupModel;
import app.added.kannauj.Models.AssignmentModel;
import app.added.kannauj.Models.ImageNameModel;
import app.added.kannauj.Models.SubjectModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.MultipleVolleyMultiPartRequest;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.app.AppConfig;
import app.added.kannauj.app.AppController;
import app.added.kannauj.databinding.ActivityAssignmentBinding;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;


public class AssignmentSubmitActivity extends AppCompatActivity {

    private static final String TAG = "StudentListActivity";
    ProgressDialog progressDialog;
    // private RelativeLayout parentLayout;
    ArrayList<Bitmap> encodedImage = null;
    ArrayList<byte[]> files = null;
    String extention = ".png";
    List<SubjectModel> subjectList = new ArrayList<>();
    List<AssignmentGroupModel> advancedAssignmentList;
    byte[] buffer;
    ActivityAssignmentBinding binding;
    Date date = new Date();
    String pdfName = String.valueOf(date.getTime()) + ".pdf";
    String AssignmentId;
    private Spinner spinner;
    private RecyclerView rvStudents;
    private String[] arr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assignment);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        spinner = findViewById(R.id.choose_subject_spinner);
        rvStudents = findViewById(R.id.rvStudents);

        initialize();
    }

    public void initialize() {
        rvStudents.setLayoutManager(new LinearLayoutManager(AssignmentSubmitActivity.this));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    SubjectModel model = (SubjectModel) spinner.getAdapter().getItem(i);
                    List<AssignmentGroupModel> tempList = filterAdvancedAssignmentList(advancedAssignmentList, model.getSubject());
                    rvStudents.setAdapter(new AdvancedSubmitAssignmentAdapter(tempList, AssignmentSubmitActivity.this, new AdvancedSubmitAssignmentAdapter.onItemClick() {
                        @Override
                        public void uploadAssignment(String assignmentId) {
                            new AlertDialog.Builder(AssignmentSubmitActivity.this).setTitle("Select Option").setMessage("Which you want to upload")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("IMAGE", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (encodedImage != null) {
                                                encodedImage.clear();
                                            }
                                            AssignmentId = assignmentId;
                                            extention = ".png";

                                            int MyVersion = Build.VERSION.SDK_INT;
                                            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
                                                if (!checkIfAlreadyHavePermission()) {
                                                    requestForSpecificPermission();
                                                } else {
                                                    Intent intent = new Intent(AssignmentSubmitActivity.this, AlbumSelectActivity.class);
                                                    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                                                        @Override
                                                        public void onActivityResult(ActivityResult result) {
                                                            onActivityResults(ConstantsCustomGallery.REQUEST_CODE, result.getResultCode(), result.getData());
                                                        }
                                                    }).launch(intent);
                                                }
                                            else {
                                                Intent intent = new Intent(AssignmentSubmitActivity.this, AlbumSelectActivity.class);
                                                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                                                    @Override
                                                    public void onActivityResult(ActivityResult result) {
                                                        onActivityResults(ConstantsCustomGallery.REQUEST_CODE, result.getResultCode(), result.getData());
                                                    }
                                                }).launch(intent);
                                            }


                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("PDF", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (encodedImage != null) {
                                                encodedImage.clear();
                                            }
                                            AssignmentId = assignmentId;
                                            extention = ".pdf";

                                            int MyVersion = Build.VERSION.SDK_INT;
                                            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
                                                if (!checkIfAlreadyHavePermission()) {
                                                    requestForSpecificPermission();
                                                } else {
                                                    new MaterialFilePicker().withActivity(AssignmentSubmitActivity.this).withRequestCode(998).withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                                                            .withFilterDirectories(false) // Set directories filterable (false by default)
                                                            .withHiddenFiles(true) // Show hidden files and folders
                                                            .start();

                                                }
                                            else {
                                                new MaterialFilePicker().withActivity(AssignmentSubmitActivity.this).withRequestCode(998).withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                                                        .withFilterDirectories(false) // Set directories filterable (false by default)
                                                        .withHiddenFiles(true) // Show hidden files and folders
                                                        .start();

                                            }


                                        }
                                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        }

                        @Override
                        public void viewAssignment(String id) {
                            // sfs
                            getSubmitAnswer(id);
                        }
                    }));
                } else {
                    rvStudents.setAdapter(new AdvancedSubmitAssignmentAdapter(advancedAssignmentList, AssignmentSubmitActivity.this, new AdvancedSubmitAssignmentAdapter.onItemClick() {
                        @Override
                        public void uploadAssignment(String assignmentId) {


                            new AlertDialog.Builder(AssignmentSubmitActivity.this).setTitle("Select Option").setMessage("Which you want to upload")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("IMAGE", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (encodedImage != null) {
                                                encodedImage.clear();
                                            }
                                            AssignmentId = assignmentId;
                                            extention = ".png";

                                            int MyVersion = Build.VERSION.SDK_INT;
                                            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
                                                if (!checkIfAlreadyHavePermission()) {
                                                    requestForSpecificPermission();
                                                } else {
                                                    Intent intent = new Intent(AssignmentSubmitActivity.this, AlbumSelectActivity.class);
                                                    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                                                        @Override
                                                        public void onActivityResult(ActivityResult result) {
                                                            onActivityResults(ConstantsCustomGallery.REQUEST_CODE, result.getResultCode(), result.getData());
                                                        }
                                                    }).launch(intent);

                                                }
                                            else {
                                                Intent intent = new Intent(AssignmentSubmitActivity.this, AlbumSelectActivity.class);
                                                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                                                    @Override
                                                    public void onActivityResult(ActivityResult result) {
                                                        onActivityResults(ConstantsCustomGallery.REQUEST_CODE, result.getResultCode(), result.getData());
                                                    }
                                                }).launch(intent);
                                                ;
                                            }
                                        }
                                    })
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("PDF", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (encodedImage != null) {
                                                encodedImage.clear();
                                            }
                                            extention = ".pdf";
                                            AssignmentId = assignmentId;

                                            int MyVersion = Build.VERSION.SDK_INT;
                                            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
                                                if (!checkIfAlreadyHavePermission()) {
                                                    requestForSpecificPermission();
                                                } else {
                                                    new MaterialFilePicker().withActivity(AssignmentSubmitActivity.this).withRequestCode(998).withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                                                            .withFilterDirectories(false) // Set directories filterable (false by default)
                                                            .withHiddenFiles(true)
                                                            .start();

                                                }
                                            else {
                                                new MaterialFilePicker().withActivity(AssignmentSubmitActivity.this).withRequestCode(998).withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                                                        .withFilterDirectories(false) // Set directories filterable (false by default)
                                                        .withHiddenFiles(true) // Show hidden files and folders
                                                        .start();

                                            }


                                        }
                                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        }

                        @Override
                        public void viewAssignment(String id) {
                            // sds

                            getSubmitAnswer(id);
                        }
                    }));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        fetchSubjects();
    }

    private void getSubmitAnswer(String assignmentId) {

        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, new AppConfig().STUDENT_ASSIGNMENT_ANSWER, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        JSONArray assignmentAnswers = dataObject.getJSONArray("AssignmentAnswers");
                        if (assignmentAnswers.length() > 0) {
                            ArrayList<String> imageName = new ArrayList<>();
                            String path = dataObject.getString("AssignmentAnswersImagePath");
                            for (int i = 0; i < assignmentAnswers.length(); i++) {
                                JSONObject jsonObject = assignmentAnswers.getJSONObject(i);
                                imageName.add(jsonObject.getString("AnswerImageName"));
                            }
                            Intent intent = new Intent(AssignmentSubmitActivity.this, AlbumActivity.class);
                            intent.putExtra("ImageAssignment", true);
                            intent.putExtra("path", path);
                            intent.putStringArrayListExtra("images", imageName);
                            startActivity(intent);

                          /*  AssignmentSubmitAnswerModel assignmentSubmitAnswerModel = new AssignmentSubmitAnswerModel(
                                    path,imageName
                            );*/


//                            getImage(imageName, path);
                        } else {
                            Snackbar snackbar = Snackbar.make(binding.parentLayout, "Please upload a answer", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }

                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar.make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
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
                Snackbar snackbar = Snackbar.make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
                showAssignmentReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",
                        //  "b9de694ec6527b5ed55bcec46d12547f");
                        new Prefs(AssignmentSubmitActivity.this).getToken());
                params.put("StudentID",
                        //  "1699");
                        new Prefs(AssignmentSubmitActivity.this).getStudent()[0]);
                params.put("AssignmentID", assignmentId);

                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(AssignmentSubmitActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);


    }

    private void getImage(String imageName, String path) {

        String fileExt = MimeTypeMap.getFileExtensionFromUrl(imageName);
        //  Toast.makeText(context, fileExt, Toast.LENGTH_SHORT).show();
        if (fileExt.equals("pdf") || fileExt.equals("PDF")) {
            //  Log.v("image",assignmentModel.getAssignmentImagePath() + "/" + assignmentModel.getId() + "/" + assignmentModel.getImagelist().get(0).getImageNameList());
            pdfName = imageName;
            // Toast.makeText(context, fileExt, Toast.LENGTH_SHORT).show();
            final DownloadTask downloadTask = new DownloadTask(AssignmentSubmitActivity.this, pdfName);
            downloadTask.execute(path + imageName);
        } else {

            /* if (assignmentModel.getImagelist().size() > 0) {*/
            // Log.v("IMAGE_PATH",assignmentModel.getAssignmentImagePath() + "/" + assignmentModel.getId() + "/" + assignmentModel.getImagelist().get(0).getImageNameList());
            for (int i = 0; i < 1; i++) {
                final int localI = i;
                progressDialog = new ProgressDialog(AssignmentSubmitActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // progressDialog.dismiss();

                        Picasso.get().load(path + imageName).into(new Target() {
                                                                      @Override
                                                                      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                                          try {

                                                                              String root = Environment.getExternalStorageDirectory().toString();
                                                                              String name = imageName;

                                                                              File myDir = new File(root + "/ADDED");

                                                                              if (!myDir.exists()) {
                                                                                  myDir.mkdirs();
                                                                              }

                                                                              myDir = new File(myDir, name);
                                                                              FileOutputStream out = new FileOutputStream(myDir);
                                                                              bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

                                                                              out.flush();
                                                                              out.close();

                                                                              Toast.makeText(AssignmentSubmitActivity.this, "Downloaded Successfully", Toast.LENGTH_LONG).show();

                                                                              //   context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(myDir.toString())));

                                                                              Intent intent = new Intent(AssignmentSubmitActivity.this, ViewImageActivity.class);
                                                                              intent.putExtra("Image Path", name);

                                                                              startActivity(intent);
                                                                              progressDialog.dismiss();

                                                                          } catch (Exception e) {
                                                                              progressDialog.dismiss();
                                                                              Log.v("Image", e.getMessage());
                                                                          }
                                                                      }

                                                                      @Override
                                                                      public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                                                          progressDialog.dismiss();
                                                                          Log.v("Image1", e.getMessage());
                                                                      }

                                                                      @Override
                                                                      public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                                          // progressDialog.dismiss();
                                                                      }
                                                                  }

                        );

                    }
                }, 500);


            }


            /*} else {
                holder.tvDownload.setVisibility(View.GONE);
            }*/
        }
    }

    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestForSpecificPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }


    public void onActivityResults(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

                encodedImage = new ArrayList();
                files = new ArrayList<>();
                Bitmap selectedImage = null;
                ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                for (int i = 0; i < images.size(); i++) {
                    Uri uri = Uri.fromFile(new File(images.get(i).path));
                    final InputStream imageStream = getContentResolver().openInputStream(uri);
                    buffer = new byte[imageStream.available()];
                    files.add(buffer);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodedImage.add(i, selectedImage);
                }

                arr = new String[encodedImage.size()];
                for (int i = 0; i < encodedImage.size(); i++) {
                    arr[i] = "data:image/png;base64," + Base64.encodeToString(getFileDataFromDrawable(encodedImage.get(i)), Base64.DEFAULT);
                }


            }
            if (requestCode == 998) {
                Bitmap selectedImage = null;
                encodedImage = new ArrayList();
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                InputStream initialStream = new FileInputStream(new File(filePath));
                String encodeFileToBase64Binary = encodeFileToBase64Binary(new File(filePath));
                arr = new String[1];
                arr[0] = "data:application/pdf;base64," + encodeFileToBase64Binary;
                buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                selectedImage = BitmapFactory.decodeStream(initialStream);
                encodedImage.add(0, selectedImage);
                if (encodedImage.size() == 0) {
                    Log.d("bitmap", "sds");
                }
            }

            uploadAssignment();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return encoded;
    }

    private void uploadAssignment() {

        if (encodedImage != null && encodedImage.size() > 0) {

            progressDialog.show();
            final MultipleVolleyMultiPartRequest volleyMultipartRequest = new MultipleVolleyMultiPartRequest(Request.Method.POST, new AppConfig().SAVE_STUDENT_ASSIGNMENT_ANSWER, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    progressDialog.dismiss();
                    try {
                        Log.e(TAG, "Response Header: " + response.allHeaders);
                        String respose = new String(response.data);
                        Log.e(TAG, "Response String : " + respose);
//                                JSONObject object = new JSONObject(respose);
//                                Log.e(TAG, "Response: " + object);
//                                if (object.getString("error").equals("0")) {
//                                    String msg = object.getString("message");
                        Snackbar snackbar = Snackbar.make(binding.parentLayout, respose, Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        fetchSubjects();

                                /*} else {
                                    String msg = object.getString("message");
                                    Snackbar snackbar = Snackbar
                                            .make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error Response: " + error.toString());
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    //showReloadAlerts();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Token", new Prefs(AssignmentSubmitActivity.this).getToken());
                    params.put("StudentID", new Prefs(AssignmentSubmitActivity.this).getStudentId());
                    params.put("AssignmentID", AssignmentId);
                    Log.d("para", AssignmentId + "");
                    JSONArray jsonArray = new JSONArray(Arrays.asList(arr));
                    params.put("AssignmentFiles", jsonArray.toString());
                    Log.d("para", params.toString());

                    return params;
                }

                /*
                 * Here we are passing image by renaming it with a unique name
                 * */
/*
                @Override
                protected Map<String, ArrayList<DataPart>> getByteData() {
                    Map<String, ArrayList<DataPart>> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    ArrayList<DataPart> dataPart = new ArrayList<>();
                    if (extention.equals(".png")) {
                        for (int i = 0; i < encodedImage.size(); i++) {
                            Bitmap converetdImage = null;
                            if (extention.equals(".png")) {
                                converetdImage = getResizedBitmap(encodedImage.get(i), 1500);
                            }
                            //getting the tag from the edittext
                            //   final String tags = editTextTags.getText().toString().trim();

                            final Bitmap finalConveretdImage = converetdImage;
                            MultipleVolleyMultiPartRequest.DataPart dp = new MultipleVolleyMultiPartRequest.DataPart(imagename + extention, getFileDataFromDrawable(finalConveretdImage), extention);
                            dataPart.add(dp);
                            //params.put(imagename, new DataPart(imagename+i, Base64.decode(encodedImageList.get(i), Base64.DEFAULT), "image/jpeg"));
                        }
                        params.put("AssignmentFiles", dataPart);

                    } else {
                        dataPart.add(new DataPart(imagename + extention, buffer, extention));
                        params.put("AssignmentFiles", dataPart);
                    }


                    Log.d("para", params.toString());
                    return params;
                }
*/
            };


            //adding the request to volley
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            volleyMultipartRequest.setShouldCache(false);
            Volley.newRequestQueue(AssignmentSubmitActivity.this).add(volleyMultipartRequest);
        }

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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void fetchSubjects() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, new AppConfig().URL_SUBJECTS, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");
                        subjectList = initializeClassList(dataObject.getJSONArray("ClassSubjectList"));
                        fetchAssignment();
                        //  binding.spinner.setAdapter(new SubjectListSpinnerAdapter(AssignmentActivity.this,subjectList));
                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar.make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
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
                Snackbar snackbar = Snackbar.make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
                showReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token", new Prefs(AssignmentSubmitActivity.this).getToken());
                params.put("StudentID", Prefs.with(AssignmentSubmitActivity.this).getStudent()[0]);
                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(AssignmentSubmitActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    public void fetchAssignment() {
        String tag_string_req = "req_login";
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, new AppConfig().URL_ASSIGNMENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONObject dataObject = object.getJSONObject("data");

                        List<AssignmentGroupModel> list = initializeAdvancedAssignmentList(dataObject.getString("AssignmentImagePath"), dataObject.getJSONArray("Assignments"));
                        advancedAssignmentList = list;
                        rvStudents.setAdapter(new AdvancedAssignmentAdapter(list, AssignmentSubmitActivity.this));

                        subjectList = filterAdvancedSubjectList(advancedAssignmentList, subjectList);
                        Log.e(TAG, subjectList.size() + "");
                        spinner.setAdapter(new SubjectListSpinnerAdapter(AssignmentSubmitActivity.this, subjectList));

                    } else {
                        String msg = object.getString("message");
                        Snackbar snackbar = Snackbar.make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT);
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
                Snackbar snackbar = Snackbar.make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT);
                snackbar.show();
                showAssignmentReloadAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token",
                        //  "b9de694ec6527b5ed55bcec46d12547f");
                        new Prefs(AssignmentSubmitActivity.this).getToken());
                params.put("StudentID",
                        //  "1699");
                        new Prefs(AssignmentSubmitActivity.this).getStudent()[0]);

                return params;
            }
        };

        // Adding request to request queue
        AppController appController = new AppController();
        appController.setContext(AssignmentSubmitActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }

    private List<SubjectModel> initializeClassList(JSONArray array) {
        List<SubjectModel> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                SubjectModel model = new SubjectModel(object.getString("id"), object.getString("Subject"));
                list.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("list", list.size() + "");
        return list;
    }

    private List<AssignmentGroupModel> initializeAdvancedAssignmentList(String ImagePath, JSONArray array) {
        List<AssignmentGroupModel> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            List<ImageNameModel> Imagelist = new ArrayList<>();
            try {
                JSONObject object = array.getJSONObject(i);

                for (int j = 0; j < object.getJSONArray("AssignmentImage").length(); j++) {
                    JSONObject image = object.getJSONArray("AssignmentImage").getJSONObject(j);
                    ImageNameModel imagelist = new ImageNameModel(image.getString("ImageName"));
                    Imagelist.add(imagelist);
                }

                AssignmentModel model = new AssignmentModel();
                model.setId(object.getString("id"));
                model.setSubject(object.getString("SubjectName"));
                model.setChapterName(object.getString("ChapterName"));
                model.setTopicName(object.getString("TopicName"));
                model.setAssignmentHeading(object.getString("AssignmentHeading"));
                model.setAssignmentDetail(object.getString("Assignment"));
                model.setAnswerUploaded(object.getString("AnswerUploaded"));
                model.setIssueDate(getDateFromString(object.getString("IssueDate")));
                model.setEndDate(getDateFromString(object.getString("EndDate")));
                model.setAssignmentImagePath(ImagePath);
                model.setImagelist(Imagelist);

                List<AssignmentModel> oneItemList = new ArrayList<>();
                oneItemList.add(model);

                AssignmentGroupModel assignmentGroupModel = new AssignmentGroupModel(object.getString("AssignmentHeading"), oneItemList);
                list.add(assignmentGroupModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private List<AssignmentGroupModel> filterAdvancedAssignmentList(List<AssignmentGroupModel> list, String subjectName) {
        List<AssignmentGroupModel> tempList = new ArrayList<>();
        for (AssignmentGroupModel model : list) {
            if (model.getItems().get(0).getSubject().equalsIgnoreCase(subjectName))
                tempList.add(model);
        }
        return tempList;
    }

    private List<SubjectModel> filterAdvancedSubjectList(List<AssignmentGroupModel> assignmentList, List<SubjectModel> subjectList) {
        List<SubjectModel> tempList = new ArrayList<>();
        for (SubjectModel subjectModel : subjectList) {
            for (AssignmentGroupModel assignmentModel : assignmentList) {
                if (assignmentModel.getItems().get(0).getSubject().equalsIgnoreCase(subjectModel.getSubject())) {
                    tempList.add(subjectModel);
                    break;
                }
            }
        }
        return tempList;
    }

    private Date getDateFromString(String str) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // here set the pattern as you date in string was containing like date/month/year
            d = sdf.parse(str);
//            Log.e(TAG,d.toString());
        } catch (ParseException ex) {
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            //Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        return d;
    }

    private void showReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentSubmitActivity.this);
        builder.setTitle("Subjects");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //fetchClasses();
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

    private void showAssignmentReloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentSubmitActivity.this);
        builder.setTitle("Assignments");
        builder.setMessage("Connectivity Error");
        builder.setIcon(R.drawable.attendance);
        builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fetchAssignment();
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

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;
        private Context context;
        String pdfName;


        public DownloadTask(Context context, String pdfName) {
            this.context = context;
            this.pdfName = pdfName;
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                String root = Environment.getExternalStorageDirectory().toString();
                String name = pdfName;

                File myDir = new File(root + "/ADDED");

                if (!myDir.exists()) {
                    myDir.mkdirs();
                }

                myDir = new File(myDir, name);

                output = new FileOutputStream(myDir);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null) output.close();
                    if (input != null) input.close();
                } catch (IOException ignored) {
                }

                if (connection != null) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            progressDialog.dismiss();
            Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_LONG).show();

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ADDED/" + pdfName);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        }
    }


}
