package im.supai.supaimarketing.action;

import android.app.Activity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/3/29.
 * 
 */
public class StoreAction  extends BaseAction
{
    //添加店铺
    public Store newStore(final Store store)
    {
        Store result = new Store();

        HashMap params = new HashMap<String, Object>();
        params.put("name",store.getName());
        params.put("description",store.getDescription());
        params.put("address",store.getAddress());
        params.put("userid",Long.toString(store.getUserid()));
        params.put("logo",store.getLogoUrl());
        params.put("area",store.getArea());
        params.put("longitude",Double.toString(store.getLongitude()));
        params.put("latitude",Double.toString(store.getLatitude()));

        try
        {
            String resultString = URLConnectionUtil.post("store/new", params);

            JSONObject jsonObject = new JSONObject(resultString);
            Log.v("supai resultString", resultString);
            if(jsonObject.getBoolean("success"))
            {
                result = getStoreFromJsonObject(jsonObject);

            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;

    }

    /**
     * 查询用户店铺
     *
     */
    public Store getInfo(long userid)
    {
        Store result = new Store();

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));

        try
        {
            String resultString = URLConnectionUtil.post("store/info", params);

            JSONObject jsonObject = new JSONObject(resultString);
            Log.v("supai resultString", resultString);
            if(jsonObject.getBoolean("success"))
            {
                JSONObject dataObject = jsonObject.getJSONObject("data");

                //赋值
                result = getStoreFromJsonObject(dataObject);
            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
       return result;

    }

    /**
     * 获取用户店铺详情
     *
     */
    public Store detail(long id)
    {
        Store result = new Store();

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(id));

        try
        {
            String resultString = URLConnectionUtil.post("store/detail", params);

            JSONObject jsonObject = new JSONObject(resultString);
            Log.v("supai resultString", resultString);
            if(jsonObject.getBoolean("success"))
            {
                JSONObject dataObject = jsonObject.getJSONObject("data");

                //赋值
                result = getStoreFromJsonObject(dataObject);

            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
        return result;

    }

    /**
     * 获取店铺商品列表
     * @return
     */
    public ArrayList<Product> productList(Activity activity, long id, int page, boolean forceRefreash)
    {
        String cacheKey = StaticValues.CACHE_KEY_STORE_PRODUCTS+'_'+id+'_'+page;
        ArrayList<Product> result = new ArrayList<Product>();
        JSONArray data = null;

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("id", Long.toString(id));
            params.put("page", Integer.toString(page));
            params.put("limit", Integer.toString(StaticValues.LIMIT));

            String resultString = URLConnectionUtil.post("store/products", params);
            Log.v("supai store product", "from web");
            Log.v("supai resultString", resultString);
            data = new JSONArray(resultString);

            /*
            if(forceRefreash == true)
            {
                List params = new ArrayList();
                params.add(new BasicNameValuePair("id", Long.toString(id)));
                params.add(new BasicNameValuePair("page", Integer.toString(page)));
                params.add(new BasicNameValuePair("limit", Integer.toString(StaticValues.LIMIT)));

                String resultString = HttpClientUtil.post("store/products", params);
                Log.v("supai store product", "from web");
                Log.v("supai resultString", resultString);
                data = new JSONArray(resultString);

                //存入缓存
                mCache.put(cacheKey, data, StaticValues.CACHE_LIFE);

            }
            else
            {
                data = mCache.getAsJSONArray(cacheKey);
                if(data ==null)
                {
                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("id", Long.toString(id)));
                    params.add(new BasicNameValuePair("page", Integer.toString(page)));
                    params.add(new BasicNameValuePair("limit", Integer.toString(StaticValues.LIMIT)));

                    String resultString = HttpClientUtil.post("store/products", params);
                    Log.v("supai store product", "from web");
                    Log.v("supai resultString", resultString);
                    data = new JSONArray(resultString);

                    //存入缓存
                    mCache.put(cacheKey, data, StaticValues.CACHE_LIFE);

                }
                else
                {
                    Log.v("supai store product", "from cache");
                }

            }

             */

            for(int a=0; a<data.length(); a++)
            {
                try
                {
                    //赋值
                    Product product = new Product();
                    JSONObject productObject = data.getJSONObject(a);

                    product.setGoodsId(productObject.getInt("goodsId"));
                    if(product.getGoodsId() != 0)
                    {
                        //赋值
                        product.setId(productObject.getLong("id"));
                        product.setName(productObject.getString("name"));
                        product.setAlias(productObject.getString("alias"));
                        product.setPrice(productObject.getDouble("price"));
                        product.setAdditional(productObject.getString("additional"));
                        product.setOrigin(productObject.getString("origin"));
                        product.setMerchant(productObject.getString("merchant"));
                        product.setMerchantCode(productObject.getString("merchant_code"));
                        product.setBarcode(productObject.getString("rccode"));
                        product.setStoreId(productObject.getInt("storeId"));
                        product.setStatus(productObject.getInt("status"));
                        product.setCount(productObject.getInt("count"));
                    }
                    else
                    {
                        product.setId(productObject.getLong("id"));
                        product.setAlias(productObject.getString("alias"));
                        product.setAdditional(productObject.getString("additional"));
                        product.setPrice(productObject.getDouble("price"));
                        product.setCount(productObject.getInt("count"));
                        product.setStatus(productObject.getInt("status"));
                        product.setStoreId(productObject.getLong("storeId"));

                    }
                    product.setFavourite(productObject.getInt("favourite"));

                    product.setImgUrl(productObject.getString("img"));
//                    product.setImg(HttpClientUtil.getImageFromWeb(productObject.getString("img")));

                    result.add(product);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }

    /**
     * 查询周边商铺列表,按距离排列
     */
    public ArrayList<Store> around(Activity activity, double longitude, double latitude, long userid, boolean forceRefreash)
    {
        ArrayList<Store> result = new ArrayList<Store>();
        JSONArray data = null;

        HashMap params = new HashMap<String, Object>();
        params.put("longitude", Double.toString(longitude));
        params.put("latitude", Double.toString(latitude));
        params.put("userid", Long.toString(userid));


        String resultString = null;
        try {
            resultString = URLConnectionUtil.post("store/around", params);
            Log.v("supai around", "from web");
            Log.v("supai resultString", resultString);
            data = new JSONArray(resultString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(data != null)
        {
            for(int a=0; a<data.length(); a++)
            {
                try
                {
                    //赋值
                    JSONObject storeObject = data.getJSONObject(a);
                    Store store = new Store();

                    store = getStoreFromJsonObject(storeObject);
                    if(store != null)
                    {
                        result.add(store);

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }

            }

        }

        return result;
    }

    /**
     * 店铺信息更新
     * @return
     */
    public Store update(Store store)
    {
        Store result = store;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(store.getId()));
        params.put("name", store.getName());
        params.put("address", store.getAddress());
        params.put("description", store.getDescription());
        params.put("status", Integer.toString(store.getStatus()));
        params.put("longitude", Double.toString(store.getLongitude()));
        params.put("latitude", Double.toString(store.getLatitude()));
        params.put("storage_warning", Integer.toString(store.getStorageWarning()));

        if(store.getLogoUrl() != null)
        {
            params.put("logo", store.getLogoUrl());
        }

        try
        {
            String resultString = URLConnectionUtil.post("store/update", params);

            JSONObject data = new JSONObject(resultString);

            if(data.getBoolean("success"))
            {
                return result;

            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 收藏店铺
     */
    public boolean addToFavourite(long userid, long storeid)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("storeid", Long.toString(storeid));

        try
        {
            String resultString = URLConnectionUtil.post("store/addFavourite", params);

            JSONObject data = new JSONObject(resultString);

            if(data.getBoolean("success"))
            {
                result=true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 取消收藏店铺
     */
    public boolean unfavourite(long userid, long storeid)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("storeid", Long.toString(storeid));

        try
        {
            String resultString = URLConnectionUtil.post("store/unfavourite", params);

            JSONObject data = new JSONObject(resultString);

            if(data.getBoolean("success"))
            {
                result=true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 根据名称查询
     */
    public ArrayList<Store> searchByName(Activity activity, String name, long userid, boolean forceRefreash)
    {
        ArrayList<Store> result = new ArrayList<Store>();
        JSONArray data = null;

        HashMap params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("userid", Long.toString(userid));

        String resultString = null;
        try {
            resultString = URLConnectionUtil.post("store/searchByName", params);
            Log.v("supai around", "from web");
            Log.v("supai resultString", resultString);
            data = new JSONArray(resultString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(data != null)
        {
            for(int a=0; a<data.length(); a++)
            {
                try
                {
                    //赋值
                    JSONObject storeObject = data.getJSONObject(a);
                    Store store = new Store();

                    store = getStoreFromJsonObject(storeObject);
                    if(store != null)
                    {
                        result.add(store);

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }

            }

        }

        return result;

    }

}
