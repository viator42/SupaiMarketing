package im.supai.supaimarketing.action;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.model.Collection;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/5/13.
 * 用户收藏相关的操作类
 */
public class CollectAction extends BaseAction
{
    public Collection all(Activity activity, long userid, boolean forceRefreash)
    {
//        mCache = ACache.get(activity);
        Collection result = new Collection();
        JSONObject data = null;

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("userid", Long.toString(userid));
            String resultString = null;

            resultString = URLConnectionUtil.post("collect/all", params);

            /*
            if(forceRefreash == true)
            {
                resultString = HttpClientUtil.post("collect/all", params);
                Log.v("supai resultString", resultString);
                data = new JSONObject(resultString);
                //存入缓存
                mCache.put("store_collect", data, StaticValues.CACHE_LIFE);
            }
            else
            {
                data = mCache.getAsJSONObject("store_collect");
                if(data == null)
                {
                    resultString = HttpClientUtil.post("collect/all", params);
                    Log.v("supai resultString", resultString);
                    data = new JSONObject(resultString);
                    //存入缓存
                    mCache.put("store_collect", data, StaticValues.CACHE_LIFE);
                }

            }
             */

            //商店列表
            JSONArray storesJSONArray = data.getJSONArray("stores");
            ArrayList<Store> stores = new ArrayList<Store>();
            for(int a=0; a<storesJSONArray.length(); a++)
            {
                try
                {
                    Store store = new Store();
                    JSONObject storeJSONObject = storesJSONArray.getJSONObject(a);

//                    store = getStoreFromJsonObject(storeJSONObject);

                    store.setId(storeJSONObject.getLong("id"));
                    store.setName(storeJSONObject.getString("name"));
                    store.setLogoUrl(storeJSONObject.getString("logo"));
                    store.setUserid(storeJSONObject.getLong("userId"));
                    store.setAddress(storeJSONObject.getString("address"));
                    store.setDescription(storeJSONObject.getString("description"));
                    store.setLongitude(storeJSONObject.getDouble("longitude"));
                    store.setLatitude(storeJSONObject.getDouble("latitude"));
                    store.setFavourite(storeJSONObject.getInt("favourite"));
                    store.setStatus(storeJSONObject.getInt("status"));

                    //商店商品列表
                    ArrayList<Product> products = new ArrayList<Product>();
                    JSONArray productsJSONArray = storeJSONObject.getJSONArray("products");
                    for(int b=0; b<productsJSONArray.length(); b++)
                    {
                        try
                        {
                            Product product = new Product();
                            JSONObject productJSONObject = productsJSONArray.getJSONObject(b);

                            product.setGoodsId(productJSONObject.getLong("goods_id"));
                            if(product.getGoodsId() != 0)
                            {
//                                product = getProductFromJsonObject(productJSONObject);

                                product.setId(productJSONObject.getLong("id"));
                                product.setName(productJSONObject.getString("name"));
                                product.setAlias(productJSONObject.getString("alias"));
                                product.setImgUrl(productJSONObject.getString("img"));
                                product.setPrice(productJSONObject.getDouble("price"));
                                product.setAdditional(productJSONObject.getString("additional"));
                                product.setOrigin(productJSONObject.getString("origin"));
                                product.setMerchant(productJSONObject.getString("merchant"));
                                product.setMerchantCode(productJSONObject.getString("merchant_code"));
                                product.setBarcode(productJSONObject.getString("rccode"));
                                product.setStoreId(productJSONObject.getLong("store_id"));
                                product.setStatus(productJSONObject.getInt("status"));
                                product.setFavourite(productJSONObject.getInt("favourite"));
                                product.setCount(productJSONObject.getInt("count"));

                            }
                            else
                            {
//                                product = getProductWithoutBarcodeFromJsonObject(productJSONObject);

                                product.setId(productJSONObject.getLong("id"));
                                product.setAlias(productJSONObject.getString("alias"));
                                product.setImgUrl(productJSONObject.getString("img"));
                                product.setAdditional(productJSONObject.getString("additional"));
                                product.setPrice(productJSONObject.getDouble("price"));
                                product.setCount(productJSONObject.getInt("count"));
                                product.setStatus(productJSONObject.getInt("status"));
                                product.setStoreId(productJSONObject.getLong("store_id"));
                                product.setFavourite(productJSONObject.getInt("favourite"));

                            }

                            products.add(product);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    store.setCollectProducts(products);


                    stores.add(store);

                }catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }

            }
            result.setStores(stores);

            //不属于商店的商品列表
            JSONArray defaultsJSONArray = data.getJSONArray("defaults");
            ArrayList<Product> defaultProducts = new ArrayList<Product>();
            for(int c=0; c<defaultsJSONArray.length(); c++)
            {
                try
                {
                    Product defaultProduct = new Product();
                    JSONObject defaultJSONObject = defaultsJSONArray.getJSONObject(c);

                    defaultProduct.setGoodsId(defaultJSONObject.getLong("goods_id"));
                    if(defaultProduct.getGoodsId() != 0)
                    {
                        defaultProduct.setId(defaultJSONObject.getLong("id"));
                        defaultProduct.setName(defaultJSONObject.getString("name"));
                        defaultProduct.setAlias(defaultJSONObject.getString("alias"));
                        defaultProduct.setImgUrl(defaultJSONObject.getString("img"));
                        defaultProduct.setPrice(defaultJSONObject.getDouble("price"));
                        defaultProduct.setAdditional(defaultJSONObject.getString("additional"));
                        defaultProduct.setOrigin(defaultJSONObject.getString("origin"));
                        defaultProduct.setMerchant(defaultJSONObject.getString("merchant"));
                        defaultProduct.setMerchantCode(defaultJSONObject.getString("merchant_code"));
                        defaultProduct.setBarcode(defaultJSONObject.getString("rccode"));
                        defaultProduct.setStoreId(defaultJSONObject.getLong("store_id"));
                        defaultProduct.setStatus(defaultJSONObject.getInt("status"));
                        defaultProduct.setFavourite(defaultJSONObject.getInt("favourite"));
                        defaultProduct.setCount(defaultJSONObject.getInt("count"));

                    }
                    else
                    {
                        defaultProduct.setId(defaultJSONObject.getLong("id"));
                        defaultProduct.setAlias(defaultJSONObject.getString("alias"));
                        defaultProduct.setImgUrl(defaultJSONObject.getString("img"));
                        defaultProduct.setAdditional(defaultJSONObject.getString("additional"));
                        defaultProduct.setPrice(defaultJSONObject.getDouble("price"));
                        defaultProduct.setCount(defaultJSONObject.getInt("count"));
                        defaultProduct.setStatus(defaultJSONObject.getInt("status"));
                        defaultProduct.setStoreId(defaultJSONObject.getLong("store_id"));
                        defaultProduct.setFavourite(defaultJSONObject.getInt("favourite"));

                    }

                    defaultProducts.add(defaultProduct);

                }catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }

            }
            result.setDefaultProducts(defaultProducts);;

            return result;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

}
