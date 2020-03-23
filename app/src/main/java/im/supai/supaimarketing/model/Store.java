package im.supai.supaimarketing.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/3/24.
 * 商铺
 */
public class Store implements Parcelable
{
    private long id;
    private String name;
    private long userid;
    private double longitude;
    private double latitude;
    private String address;
    private String area;
    private String description;
    private String errMsg;
    private String logoUrl;
    private byte[] logo;
    private int favourite = 0;  //1: 已收藏 0:未收藏 2:不显示收藏按钮
    private int status;
    private int storageWarning;

    //not in Parcelable
    private ArrayList<Product> collectProducts;

    //完整性校验
    public boolean validate()
    {
        errMsg = "";

        boolean result = true;

        if(name == null || name.isEmpty())
        {
            errMsg = "店铺名称不能为空";
            return false;
        }
        if(address == null || address.isEmpty())
        {
            errMsg = "地址不能为空";
            return false;
        }

        //设置默认logo
        if(logoUrl == null || logoUrl.isEmpty())
        {
            logoUrl = "/images/store_default.png";
        }

        return result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStorageWarning() {
        return storageWarning;
    }

    public void setStorageWarning(int storageWarning) {
        this.storageWarning = storageWarning;
    }

    public ArrayList<Product> getCollectProducts() {
        return collectProducts;
    }

    public void setCollectProducts(ArrayList<Product> collectProducts) {
        this.collectProducts = collectProducts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeLong(id);
//        parcel.writeString(name);
//        parcel.writeString(logoUrl);
//        parcel.writeLong(userid);
//        parcel.writeDouble(latitude);
//        parcel.writeDouble(longitude);
//        parcel.writeString(address);
//        parcel.writeString(area);
//        parcel.writeString(description);
//        parcel.writeInt(logo.length);
//        parcel.writeByteArray(logo);
//
//    }

    public static final Creator CREATOR = new Creator() {
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public Store(Parcel in){
        readFromParcel(in);
    }

    public Store()
    {

    }

    public void readFromParcel(Parcel in){

        id = in.readLong();
        name = in.readString();
        userid = in.readLong();
        longitude = in.readDouble();
        latitude = in.readDouble();
        address = in.readString();
        area = in.readString();
        description = in.readString();
        errMsg = in.readString();
        logoUrl = in.readString();
        favourite = in.readInt();
        status = in.readInt();
        storageWarning = in.readInt();

    }

//    public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>()
//    {
//        public Store createFromParcel(Parcel in) {
//            Store store = new Store();
//
//            store.id = in.readLong();
//            store.name = in.readString();
//            store.userid = in.readLong();
//            store.longitude = in.readDouble();
//            store.latitude = in.readDouble();
//            store.address = in.readString();
//            store.area = in.readString();
//            store.description = in.readString();
//            store.errMsg = in.readString();
//            store.logoUrl = in.readString();
////            store.logo = Bitmap.CREATOR.createFromParcel(in);
//
//            in.readByteArray(store.logo);
//            return store;
//        }
//
//        public Store[] newArray(int size) {
//            return new Store[size];
//        }
//    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeLong(userid);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(address);
        parcel.writeString(area);
        parcel.writeString(description);
        parcel.writeString(errMsg);
        parcel.writeString(logoUrl);
        parcel.writeInt(favourite);
        parcel.writeInt(status);
        parcel.writeInt(storageWarning);

    }

    public boolean getActiveStatus()
    {
        if(status == 1)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public void setActiveStatus(Boolean activeStatus)
    {
        if(activeStatus==true)
        {
            status = 1;

        }
        else
        {
            status = 2;

        }

    }
}
