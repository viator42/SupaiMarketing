package im.supai.supaimarketing.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viator42 on 15/5/21.
 * 地图标识物
 */
public class MapMarker implements Parcelable
{
    private long id;
    private String title;
    private String snippet;
    private double longitute;
    private double latitude;
    private byte[] icon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        public MapMarker createFromParcel(Parcel in) {
            return new MapMarker(in);
        }

        public MapMarker[] newArray(int size) {
            return new MapMarker[size];
        }
    };

    public MapMarker(Parcel in){
        readFromParcel(in);
    }

    public MapMarker()
    {

    }

    public void readFromParcel(Parcel in){
        id = in.readLong();
        title = in.readString();
        snippet = in.readString();
        longitute = in.readDouble();
        latitude = in.readDouble();
//        icon = new byte[in.readInt()];
//        in.readByteArray(icon);

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(snippet);
        parcel.writeDouble(longitute);
        parcel.writeDouble(latitude);
//        parcel.writeByteArray(icon);

    }

}
