package im.supai.supaimarketing.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by viator42 on 15/3/24.
 * 购物车
 *
 */
public class Cart
{
    private long id;
    private long userId;
    private long storeId;
    private String storeName;
    private int status;
    private int createTime;
    private int count;
    private double summary;
    private List<CartDetail> detailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSummary() {
        return summary;
    }

    public void setSummary(double summary) {
        this.summary = summary;
    }

    public List<CartDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<CartDetail> detailList) {
        this.detailList = detailList;
    }

    //计算当前购物车的总金额
//    public double CalculateSum()
//    {
//        this.summary = 0;
//        for (int i = 0; i < this.cartProducts.size(); i++)
//        {
//            this.summary += this.cartProducts.get(i).getPrice();
//
//        }
//
//        return this.summary;
//    }

}
