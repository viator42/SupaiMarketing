package im.supai.supaimarketing.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/3/24.
 * 订单
 */
public class Order implements Parcelable
{
    private long id;
    private long merchantId;
    private long customerId;
    private long storeId;
    private String customerName;
    private String merchangName;
    private String customerTel;
    private String merchantTel;
    private String storeName;
    private long createTime;
    private double summary;
    private int status;
    private String additional;
    private double customerLongitude;
    private double customerLatitude;
    private double merchantLongitude;
    private double merchantLatitude;
    private int readed; //是否已读
    private String sn;  //订单编号
    private String customerAddress;
    private String merchantAddress;
    private int payMethod;
    private int paid;
    private int payAfter;
    private int count;  //商品数量

    //not in Parcelable
    private ArrayList<OrderDetail> orderDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMerchangName() {
        return merchangName;
    }

    public void setMerchangName(String merchangName) {
        this.merchangName = merchangName;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getMerchantTel() {
        return merchantTel;
    }

    public void setMerchantTel(String merchantTel) {
        this.merchantTel = merchantTel;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public double getSummary() {
        return summary;
    }

    public void setSummary(double summary) {
        this.summary = summary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public double getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(double customerLongitude) {
        this.customerLongitude = customerLongitude;
    }

    public double getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(double customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public double getMerchantLongitude() {
        return merchantLongitude;
    }

    public void setMerchantLongitude(double merchantLongitude) {
        this.merchantLongitude = merchantLongitude;
    }

    public double getMerchantLatitude() {
        return merchantLatitude;
    }

    public void setMerchantLatitude(double merchantLatitude) {
        this.merchantLatitude = merchantLatitude;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getPayAfter() {
        return payAfter;
    }

    public void setPayAfter(int payAfter) {
        this.payAfter = payAfter;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public Order(Parcel in){
        readFromParcel(in);
    }

    public Order()
    {

    }

    public void readFromParcel(Parcel in){
        id = in.readLong();
        merchantId = in.readLong();
        customerId = in.readLong();
        storeId = in.readLong();
        customerName = in.readString();
        merchangName = in.readString();
        customerTel = in.readString();
        merchantTel = in.readString();
        storeName = in.readString();
        createTime = in.readLong();
        summary = in.readDouble();
        status = in.readInt();
        additional = in.readString();
        customerLongitude = in.readDouble();
        customerLatitude = in.readDouble();
        merchantLongitude = in.readDouble();
        merchantLatitude = in.readDouble();
        readed = in.readInt();
        sn = in.readString();
        customerAddress = in.readString();
        merchantAddress = in.readString();
        payMethod = in.readInt();
        paid = in.readInt();
        payAfter = in.readInt();
        count = in.readInt();

    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(merchantId);
        parcel.writeLong(customerId);
        parcel.writeLong(storeId);
        parcel.writeString(customerName);
        parcel.writeString(merchangName);
        parcel.writeString(customerTel);
        parcel.writeString(merchantTel);
        parcel.writeString(storeName);
        parcel.writeLong(createTime);
        parcel.writeDouble(summary);
        parcel.writeInt(status);
        parcel.writeString(additional);
        parcel.writeDouble(customerLongitude);
        parcel.writeDouble(customerLatitude);
        parcel.writeDouble(merchantLongitude);
        parcel.writeDouble(merchantLatitude);
        parcel.writeInt(readed);
        parcel.writeString(sn);
        parcel.writeString(customerAddress);
        parcel.writeString(merchantAddress);
        parcel.writeInt(payMethod);
        parcel.writeInt(paid);
        parcel.writeInt(payAfter);
        parcel.writeInt(count);

    }

    public String getStatusName()
    {
        switch(status)
        {
            case StaticValues.ORDER_STATUS_UNPAID:
                return "未付款";

            case StaticValues.ORDER_STATUS_READY:
                return "等待发货";

            case StaticValues.ORDER_STATUS_DELIVERING:
                return "已发货";

            case StaticValues.ORDER_STATUS_SUCCEED:
                return "交易成功";

            case StaticValues.ORDER_STATUS_CLOSED:
                return "交易关闭";

            case StaticValues.ORDER_STATUS_RETURN_APPLY:
                return "申请退货";

            default:
                return "状态获取失败";

        }
    }

    public String getPayMethodName()
    {
        switch(status)
        {
            case 1:
                return "货到付款";

            case 2:
                return "支付宝";

            default:
                return "支付方式获取失败";

        }
    }

}
