package im.supai.supaimarketing.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by viator42 on 15/3/24.
 * 商户添加的待售产品
 */
public class Product implements Parcelable
{
    private long id;
    private String name;
    private String alias;   //用户为商品取的别名
    private int count;
    private double price;
    private String priceInterval;
    private String additional;
    private String merchant;
    private String merchantCode;
    private String origin;
    private String barcode;
    private long storeId;
    private String storeName;
    private String address;
    private double longitude;
    private double latitude;
    private int status;
    private String imgUrl;
    private byte[] img;
    private int favourite = 0;  //1: 已收藏 0:未收藏 2:不显示收藏按钮
    private long goodsId;

    //出错校验信息
    private String validationMsg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getPriceInterval() {
        return priceInterval;
    }

    public void setPriceInterval(String priceInterval) {
        this.priceInterval = priceInterval;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValidationMsg() {
        return validationMsg;
    }

    public void setValidationMsg(String validationMsg) {
        this.validationMsg = validationMsg;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(Parcel in){
        readFromParcel(in);
    }

    public Product()
    {

    }

    public void readFromParcel(Parcel in){
        id = in.readLong();
        name = in.readString();
        alias = in.readString();
        count = in.readInt();
        price = in.readDouble();
        priceInterval = in.readString();
        additional = in.readString();
        merchant = in.readString();
        merchantCode = in.readString();
        origin = in.readString();
        barcode = in.readString();
        storeId = in.readLong();
        storeName = in.readString();
        address = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        status = in.readInt();
        imgUrl = in.readString();
//        img = new byte[in.readInt()];
//        in.readByteArray(img);
        favourite = in.readInt();
        goodsId = in.readLong();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(alias);
        parcel.writeInt(count);
        parcel.writeDouble(price);
        parcel.writeString(priceInterval);
        parcel.writeString(additional);
        parcel.writeString(merchant);
        parcel.writeString(merchantCode);
        parcel.writeString(origin);
        parcel.writeString(barcode);
        parcel.writeLong(storeId);
        parcel.writeString(storeName);
        parcel.writeString(address);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeInt(status);
        parcel.writeString(imgUrl);
//        parcel.writeInt(img.length);
//        parcel.writeByteArray(img);
        parcel.writeInt(favourite);
        parcel.writeLong(goodsId);

    }

    //提交校验
    public boolean updateValidation()
    {
        boolean result = true;

        if(alias.isEmpty())
        {
            validationMsg = "商品名称不能为空";
            return false;
        }
        if(price < 0)
        {
            validationMsg = "价格设置错误";
            return false;
        }

        return result;
    }

    public String getStatusText()
    {
        String result = "";

        switch (status)
        {
            case 1:
                result = "已上架";
                break;
            case 2:
                result = "已下架";
            break;
            case 0:
                result = "已删除";
            break;

        }
        return result;
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
