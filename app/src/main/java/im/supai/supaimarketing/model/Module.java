package im.supai.supaimarketing.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by viator42 on 15/8/4.
 */
public class Module  implements Parcelable
{
    private long id;
    private long bundleId;
    private long userid;
    private String name;
    private String description;
    private String username;
    private String tel;
    private String address;
    private long startTime;
    private long finishTime;
    private double price;
    private int status;
    private String license;

    private boolean applySuccess;
    private String validationMsg;
    private HashMap<String, Module> categories;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getValidationMsg() {
        return validationMsg;
    }

    public void setValidationMsg(String validationMsg) {
        this.validationMsg = validationMsg;
    }

    public long getBundleId() {
        return bundleId;
    }

    public void setBundleId(long bundleId) {
        this.bundleId = bundleId;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public boolean isApplySuccess() {
        return applySuccess;
    }

    public void setApplySuccess(boolean applySuccess) {
        this.applySuccess = applySuccess;
    }

    public HashMap<String, Module> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, Module> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public static final Creator CREATOR = new Creator() {
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    public Module(Parcel in){
        readFromParcel(in);
    }

    public Module()
    {

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(bundleId);
        parcel.writeLong(userid);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(username);
        parcel.writeString(tel);
        parcel.writeString(address);
        parcel.writeLong(startTime);
        parcel.writeLong(finishTime);
        parcel.writeDouble(price);
        parcel.writeInt(status);
        parcel.writeString(license);

    }

    public void readFromParcel(Parcel in){
        id = in.readLong();
        bundleId = in.readLong();
        userid = in.readLong();
        name = in.readString();
        description = in.readString();
        username = in.readString();
        tel = in.readString();
        address = in.readString();
        startTime = in.readLong();
        finishTime = in.readLong();
        price = in.readDouble();
        status = in.readInt();
        license = in.readString();

    }

    //申请校验
    public boolean applyValidate()
    {
        boolean result = true;
        if(this.username.isEmpty())
        {
            validationMsg = "姓名不能为空";
            return false;
        }
        if(this.tel.isEmpty())
        {
            validationMsg = "联系电话不能为空";
            return false;
        }
        if(this.address.isEmpty())
        {
            validationMsg = "地址不能为空";
            return false;
        }

        return result;

    }

}
