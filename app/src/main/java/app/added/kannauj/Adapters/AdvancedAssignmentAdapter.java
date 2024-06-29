package app.added.kannauj.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import app.added.kannauj.Models.AssignmentGroupModel;
import app.added.kannauj.Models.AssignmentModel;
import app.added.kannauj.R;
import app.added.kannauj.activities.ViewImageActivity;

public class AdvancedAssignmentAdapter extends ExpandableRecyclerViewAdapter<AdvancedAssignmentAdapter.TitleViewHolder, AdvancedAssignmentAdapter.AssignmentViewHolder> {

    Context context;
    LayoutInflater inflater;
    Date date = new Date();
    String pdfName = String.valueOf(date.getTime()) + ".pdf";
    ProgressDialog progressDialog;
    public AdvancedAssignmentAdapter(List<? extends ExpandableGroup> groups, Context context) {
        super(groups);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_assignment_heading, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public AssignmentViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_assignment, parent, false);
        return new AssignmentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindChildViewHolder(final AssignmentViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final AssignmentModel assignmentModel = ((AssignmentGroupModel) group).getItems().get(childIndex);
        String startDate = DateFormat.getDateInstance(DateFormat.FULL).format(assignmentModel.getIssueDate());
        String endDate = DateFormat.getDateInstance(DateFormat.FULL).format(assignmentModel.getEndDate());
        holder.tvSubject.setText("Subject : " + assignmentModel.getSubject());
        holder.tvHeading.setText("Detail : " + assignmentModel.getAssignmentDetail());
        holder.tvChapter.setText("Chapter : " + assignmentModel.getChapterName() + "\nTopic : " + assignmentModel.getTopicName());
        holder.tvDate.setText("Issue Date : " + startDate + "\n" + "End Date : " + endDate);


        if (assignmentModel.getImagelist().size() > 0) {
            holder.tvDownload.setVisibility(View.VISIBLE);
        } else {
            holder.tvDownload.setVisibility(View.GONE);
        }

        holder.tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileExt = MimeTypeMap.getFileExtensionFromUrl(assignmentModel.getImagelist().get(0).getImageNameList());
                //  Toast.makeText(context, fileExt, Toast.LENGTH_SHORT).show();
                if (fileExt.equals("pdf") || fileExt.equals("PDF")) {
                    //  Log.v("image",assignmentModel.getAssignmentImagePath() + "/" + assignmentModel.getId() + "/" + assignmentModel.getImagelist().get(0).getImageNameList());
                    pdfName = assignmentModel.getImagelist().get(0).getImageNameList();
                    // Toast.makeText(context, fileExt, Toast.LENGTH_SHORT).show();
                    final DownloadTask downloadTask = new DownloadTask(context);
                    downloadTask.execute(assignmentModel.getAssignmentImagePath() + "/" + assignmentModel.getId() + "/" + assignmentModel.getImagelist().get(0).getImageNameList());
                } else {

                    if (assignmentModel.getImagelist().size() > 0) {
                        Log.v("IMAGE_PATH", assignmentModel.getAssignmentImagePath() + "/" + assignmentModel.getId() + "/" + assignmentModel.getImagelist().get(0).getImageNameList());
                        for (int i = 0; i < assignmentModel.getImagelist().size(); i++) {
                            final int localI = i;
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // progressDialog.dismiss();

                                    Picasso.get()
                                            .load(assignmentModel.getAssignmentImagePath() + "/" + assignmentModel.getId() + "/" + assignmentModel.getImagelist().get(0).getImageNameList())
                                            .into(new Target() {
                                                      @Override
                                                      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                          try {

                                                              String root = Environment.getExternalStorageDirectory().toString();
                                                              String name = assignmentModel.getImagelist().get(0).getImageNameList();

                                                              File myDir = new File(root + "/ADDED");

                                                              if (!myDir.exists()) {
                                                                  myDir.mkdirs();
                                                              }

                                                              myDir = new File(myDir, name);
                                                              FileOutputStream out = new FileOutputStream(myDir);
                                                              bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

                                                              out.flush();
                                                              out.close();

                                                              Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_LONG).show();

                                                              //   context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(myDir.toString())));

                                                              Intent intent = new Intent(context, ViewImageActivity.class);
                                                              intent.putExtra("Image Path", name);

                                                              context.startActivity(intent);
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


                    } else {
                        holder.tvDownload.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(TitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.tvTitle.setText(group.getTitle());
        AssignmentModel model = (AssignmentModel) group.getItems().get(0);
        holder.tvSubject.setText(model.getSubject());
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;
        private Context context;

        public DownloadTask(Context context) {
            this.context = context;
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
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
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
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
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

    class TitleViewHolder extends GroupViewHolder {

        TextView tvTitle, tvSubject;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubject = itemView.findViewById(R.id.tvSubject);
        }
    }

    class AssignmentViewHolder extends ChildViewHolder {

        TextView tvSubject, tvHeading, tvChapter, tvDate, tvDownload;

        public AssignmentViewHolder(View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDownload = itemView.findViewById(R.id.tvDownload);
        }
    }

}
