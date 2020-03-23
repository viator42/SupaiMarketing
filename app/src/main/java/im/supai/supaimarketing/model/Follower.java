package im.supai.supaimarketing.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viator42 on 15/8/10.
 */
public class Follower implements Parcelable
{
    private long id;
    private String name;
    private String tel;
    private String address;
    private String icon;
    private String sn;
    private long followTime;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long getFollowTime() {
        return followTime;
    }

    public void setFollowTime(long followTime) {
        this.followTime = followTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static final Creator CREATOR = new Creator() {
        public Follower createFromParcel(Parcel in) {
            return new Follower(in);
        }

        public Follower[] newArray(int size) {
            return new Follower[size];
        }
    };

    public Follower(Parcel in){
        readFromParcel(in);
    }

    public Follower()
    {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in){
        id = in.readLong();
        name = in.readString();
        tel = in.readString();
        address = in.readString();
        icon = in.readString();
        sn = in.readString();
        followTime = in.readLong();
        status = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(tel);
        parcel.writeString(address);
        parcel.writeString(icon);
        parcel.writeString(sn);
        parcel.writeLong(followTime);
        parcel.writeInt(status);

    }
}
