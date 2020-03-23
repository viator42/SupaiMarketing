package im.supai.supaimarketing.model;

import java.util.ArrayList;

/**
 * Created by viator42 on 15/5/19.
 * 包含customer和merchant两个列表
 */
public class Orders
{
    private ArrayList<Order> customerList = null;
    private ArrayList<Order> merchantList = null;
    private int unreadCount  = 0;   //未读个数
    private int customerPn; //页数
    private int merchantPn;
    private int type;

    public ArrayList<Order> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(ArrayList<Order> customerList) {
        this.customerList = customerList;
    }

    public ArrayList<Order> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(ArrayList<Order> merchantList) {
        this.merchantList = merchantList;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int unreadMinusOne()
    {
        if(this.unreadCount > 0)
        {
            this.unreadCount -= 1;
        }

        return this.unreadCount;
    }

    public int getCustomerPn() {
        return customerPn;
    }

    public void setCustomerPn(int customerPn) {
        this.customerPn = customerPn;
    }

    public int getMerchantPn() {
        return merchantPn;
    }

    public void setMerchantPn(int merchantPn) {
        this.merchantPn = merchantPn;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
