package app.added.kannauj.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sudipta Samanta on 20-07-2019.
 */

public class ImageNameModel implements Parcelable {

    String imageNameList;

    public ImageNameModel(String imageNameList) {
        this.imageNameList = imageNameList;
    }

    public ImageNameModel(Parcel in) {
        imageNameList = in.readString();
    }

    public static final Creator<ImageNameModel> CREATOR = new Creator<ImageNameModel>() {
        @Override
        public ImageNameModel createFromParcel(Parcel in) {
            return new ImageNameModel(in);
        }

        @Override
        public ImageNameModel[] newArray(int size) {
            return new ImageNameModel[size];
        }
    };

    public String getImageNameList() {
        return imageNameList;
    }

    public void setImageNameList(String imageNameList) {
        this.imageNameList = imageNameList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(imageNameList);
    }
}
