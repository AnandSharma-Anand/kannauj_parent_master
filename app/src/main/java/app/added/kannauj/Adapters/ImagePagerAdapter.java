package app.added.kannauj.Adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import app.added.kannauj.Fragments.ImageFragment;
import app.added.kannauj.Models.GalleryModel;


public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<GalleryModel> list;
    String imagePath;

    public ImagePagerAdapter(FragmentManager fm, ArrayList<GalleryModel> list, String imagePath) {
        super(fm);
        this.list = list;
        this.imagePath = imagePath;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putString("image",imagePath + list.get(position).getImageName());
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(args);
        return imageFragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
