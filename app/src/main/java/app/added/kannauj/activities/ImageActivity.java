package app.added.kannauj.activities;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import app.added.kannauj.Adapters.ImagePagerAdapter;
import app.added.kannauj.CustomViews.ImageViewTouchViewPager;
import app.added.kannauj.Models.GalleryModel;
import app.added.kannauj.R;


public class ImageActivity extends AppCompatActivity {

    ImageViewTouchViewPager viewPager;
    ProgressDialog progressDialog;
    private static final String TAG = "ImageActivity";
    ArrayList<GalleryModel> galleryList = new ArrayList<>();
    String imagePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initialize();
    }

    private void initialize() {
        galleryList = getIntent().getParcelableArrayListExtra("list");
        imagePath = getIntent().getStringExtra("imagePath");
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(),galleryList,imagePath));
        viewPager.setCurrentItem(getIntent().getIntExtra("position",0),true);
    }

}
