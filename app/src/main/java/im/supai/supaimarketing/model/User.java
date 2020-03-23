package im.supai.supaimarketing.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/3/24.
 * 用户
 */
public class User implements Parcelable
{
    private long id;
    private String username;
    private String name;
    private String password;
    private String tel;
    private String address;
    private String area;
//    private byte[] icon;
    private String iconUrl;
    private String sn;
    private int passtype;
    private long clerkOf;
    private int status;
    private int printCopy;

    private String validationMsg; //验证信息
    private String msg; //登录/注册提示信息
    private int errCode = 0;
    private boolean loginSuccess = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

//    public byte[] getIcon() {
//        return icon;
//    }
//
//    public void setIcon(byte[] icon) {
//        this.icon = icon;
//    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getPasstype() {
        return passtype;
    }

    public void setPasstype(int passtype) {
        this.passtype = passtype;
    }

    public long getClerkOf() {
        return clerkOf;
    }

    public void setClerkOf(long clerkOf) {
        this.clerkOf = clerkOf;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrintCopy() {
        return printCopy;
    }

    public void setPrintCopy(int printCopy) {
        this.printCopy = printCopy;
    }

    //注册验证
    public boolean registerValidation()
    {
        boolean result = true;

        if(tel == null || tel.isEmpty())
        {
            validationMsg = "手机号不能为空";
            result = false;
        } else
        if(name == null || name.isEmpty())
        {
            validationMsg = "姓名不能为空";
            result = false;
        } else
        if(address == null || address.isEmpty())
        {
            validationMsg = "地址不能为空";
            result = false;

        } else
        if(password == null || password.isEmpty())
        {
            validationMsg = "密码获取失败";
            result = false;

        }

        return result;
    }

    /**
     * 登录验证
     * @return
     */
    public boolean loginValidation()
    {
        boolean result = true;

        if(tel.isEmpty())
        {
            validationMsg = "手机号不能为空";
            result = false;
        } else
        if(password.isEmpty())
        {
            validationMsg = "密码获取失败";
            result = false;

        }

        return result;

    }

    /**
     * 修改验证
     * @return
     */
    public boolean updateValidation()
    {
        boolean result = true;

        if(name.isEmpty() || name == null)
        {
            validationMsg = "姓名不能为空";
            result = false;
        }

        if(address.isEmpty() || address == null)
        {
            validationMsg = "地址不能为空";
            result = false;

        }

        if(printCopy < 1)
        {
            validationMsg = "打印份数不能小于1";
            result = false;

        }

        return result;

    }

    public String getValidationMsg() {
        return validationMsg;
    }

    public void setValidationMsg(String validationMsg) {
        this.validationMsg = validationMsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in){
        readFromParcel(in);
    }

    public User()
    {

    }

    public void readFromParcel(Parcel in){
        id = in.readLong();
        username = in.readString();
        name = in.readString();
        password = in.readString();
        tel = in.readString();
        address = in.readString();
        area = in.readString();
//        icon = new byte[in.readInt()];
//        in.readByteArray(icon);
        iconUrl = in.readString();
        sn = in.readString();
        passtype = in.readInt();
        clerkOf = in.readLong();
        status = in.readInt();
        printCopy = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(password);
        parcel.writeString(tel);
        parcel.writeString(address);
        parcel.writeString(area);
//        parcel.writeInt(icon.length);
//        parcel.writeByteArray(icon);
        parcel.writeString(iconUrl);
        parcel.writeString(sn);
        parcel.writeInt(passtype);
        parcel.writeLong(clerkOf);
        parcel.writeInt(status);
        parcel.writeInt(printCopy);

    }

}
