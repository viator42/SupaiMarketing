package im.supai.supaimarketing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by viator42 on 15/5/13.
 * 用户收藏的店铺/商品相关
 */
public class Collection
{
    private ArrayList<Store> stores;
    private ArrayList<Product> defaultProducts;

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public ArrayList<Product> getDefaultProducts() {
        return defaultProducts;
    }

    public void setDefaultProducts(ArrayList<Product> defaultProducts) {
        this.defaultProducts = defaultProducts;
    }


}
