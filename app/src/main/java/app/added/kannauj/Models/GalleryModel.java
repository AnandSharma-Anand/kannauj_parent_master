package app.added.kannauj.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryModel implements Parcelable {

    String id, name, description, imageName, createdDate, TotalImages;
    int isActive;

    public GalleryModel(String id, String name, String description, int isActive, String imageName, String createdDate, String totalImages) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.imageName = imageName;
        this.createdDate = createdDate;
        this.TotalImages = totalImages;
    }


    protected GalleryModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        imageName = in.readString();
        createdDate = in.readString();
        isActive = in.readInt();
        TotalImages = in.readString();
    }

    public static final Creator<GalleryModel> CREATOR = new Creator<GalleryModel>() {
        @Override
        public GalleryModel createFromParcel(Parcel in) {
            return new GalleryModel(in);
        }

        @Override
        public GalleryModel[] newArray(int size) {
            return new GalleryModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTotalImages() {
        return TotalImages;
    }

    public void setTotalImages(String totalImages) {
        TotalImages = totalImages;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imageName);
        parcel.writeString(createdDate);
        parcel.writeInt(isActive);
        parcel.writeString(TotalImages);
    }
}
