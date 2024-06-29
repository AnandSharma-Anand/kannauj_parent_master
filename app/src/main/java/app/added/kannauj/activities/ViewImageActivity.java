package app.added.kannauj.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;

import app.added.kannauj.R;
import app.added.kannauj.managers.ConstantsManager;

public class ViewImageActivity extends Activity {

    private String imagePath;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.view_image_imageView);

        getIntentValues();

        setUpActivity();
    }

    private void setUpActivity() {
        String root = Environment.getExternalStorageDirectory().toString();
        File imageFilePath = new File(root + ConstantsManager.appFolderName + "/" + imagePath);

        if (imageFilePath.exists()) {
            //  Bitmap myBitmap = BitmapFactory.decodeFile(imageFilePath.getAbsolutePath());
            imageView.setImageURI(Uri.fromFile(imageFilePath));
        }
    }

    private void getIntentValues() {
        Intent intent = getIntent();
        imagePath = intent.getStringExtra("Image Path");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
