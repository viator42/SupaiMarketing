package im.supai.supaimarketing.action;

import android.app.Activity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/4/13.
 */
public class ProductAction extends BaseAction
{
    //最近购买的商品列表
    public ArrayList<Product> recent(Long userid)
    {
        ArrayList<Product> result = new ArrayList<Product>();
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));

        try
        {
            resultString = URLConnectionUtil.post("product/recent", params);
            Log.v("supai resultString", resultString);

            JSONArray data = new JSONArray(resultString);
            for(int a=0; a<data.length(); a++)
            {
                try
                {
                    Product product = new Product();
                    JSONObject jsonObject = data.getJSONObject(a);

                    product.setGoodsId(jsonObject.getLong("goods_id"));
                    if(product.getGoodsId() != 0)
                    {
                        product.setId(jsonObject.getLong("id"));
                        product.setName(jsonObject.getString("name"));
                        product.setAlias(jsonObject.getString("alias"));
                        product.setImgUrl(jsonObject.getString("img"));
                        product.setPrice(jsonObject.getDouble("price"));
                        product.setAdditional(jsonObject.getString("additional"));
                        product.setOrigin(jsonObject.getString("origin"));
                        product.setMerchant(jsonObject.getString("merchant"));
                        product.setMerchantCode(jsonObject.getString("merchant_code"));
                        product.setBarcode(jsonObject.getString("rccode"));
                        product.setStoreId(jsonObject.getLong("store_id"));
                        product.setStatus(jsonObject.getInt("status"));
                        product.setFavourite(jsonObject.getInt("favourite"));
                        product.setCount(jsonObject.getInt("count"));

                    }
                    else
                    {
                        product.setId(jsonObject.getLong("id"));
                        product.setAlias(jsonObject.getString("alias"));
                        product.setImgUrl(jsonObject.getString("img"));
                        product.setAdditional(jsonObject.getString("additional"));
                        product.setPrice(jsonObject.getDouble("price"));
                        product.setCount(jsonObject.getInt("count"));
                        product.setStatus(jsonObject.getInt("status"));
                        product.setStoreId(jsonObject.getLong("store_id"));
                        product.setFavourite(jsonObject.getInt("favourite"));

                    }

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

    //商品详情
    public Product detail(Activity activity, long id, boolean forceRefreash)
    {
        String cacheKey = StaticValues.CACHE_KEY_PRODUCT_DETAIL+'_'+id;

        JSONObject data = null;
        Product result = new Product();
        String resultString = null;

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("id", Long.toString(id));

            resultString = URLConnectionUtil.post("product/detail", params);
            data = new JSONObject(resultString);
            Log.v("supai product detail", "from web");
            Log.v("supai resultString", resultString);

            if(data.getBoolean("success"))
            {
                JSONObject dataObject = data.getJSONObject("data");

                result.setGoodsId(dataObject.getInt("goodsId"));
                if(result.getGoodsId() != 0)
                {
                    //赋值
                    result.setId(dataObject.getLong("id"));
                    result.setName(dataObject.getString("name"));
                    result.setAlias(dataObject.getString("alias"));
                    result.setPrice(dataObject.getDouble("price"));
                    result.setAdditional(dataObject.getString("additional"));
                    result.setOrigin(dataObject.getString("origin"));
                    result.setMerchant(dataObject.getString("merchant"));
                    result.setMerchantCode(dataObject.getString("merchant_code"));
                    result.setBarcode(dataObject.getString("rccode"));
                    result.setStoreId(dataObject.getInt("storeId"));
                    result.setImgUrl(dataObject.getString("img"));
                    result.setStatus(dataObject.getInt("status"));
                    result.setCount(dataObject.getInt("count"));
                    result.setFavourite(dataObject.getInt("favourite"));
                }
                else
                {
                    result.setId(dataObject.getLong("id"));
                    result.setAlias(dataObject.getString("alias"));
                    result.setAdditional(dataObject.getString("additional"));
                    result.setPrice(dataObject.getDouble("price"));
                    result.setCount(dataObject.getInt("count"));
                    result.setStatus(dataObject.getInt("status"));
                    result.setStoreId(dataObject.getLong("storeId"));
                    result.setImgUrl(dataObject.getString("img"));
                    result.setFavourite(dataObject.getInt("favourite"));

                }

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
     * 添加商品
     * @return
     */
    public Product add(Product product)
    {
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("name", product.getName());
        params.put("alias", product.getAlias());
        params.put("barcode", product.getBarcode());
        params.put("priceInterval", product.getPriceInterval());
        params.put("origin", product.getOrigin());
        params.put("merchantCode", product.getMerchantCode());
        params.put("merchant", product.getMerchant());
        params.put("price", Double.toString(product.getPrice()));
        params.put("storeId", Long.toString(product.getStoreId()));
        params.put("img", product.getImgUrl());
        params.put("additional", product.getAdditional());
        params.put("count", Integer.toString(product.getCount()));

        try
        {
            //商品信息
            resultString = URLConnectionUtil.post("product/add", params);
            Log.v("supai resultString", resultString);

            JSONObject jsonObject = new JSONObject(resultString);

            if(jsonObject.getBoolean("success"))
            {
                product.setId(jsonObject.getLong("id"));

            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return product;

    }

    //根据barcode查询商品
    public ArrayList<Product> search(Activity activity, String bracode, long storeId, int page, boolean forceRefreash)
    {
        String cacheKey = StaticValues.CACHE_KEY_SEARCH+'_'+bracode+'_'+storeId;

        ArrayList<Product> result = new ArrayList<Product>();
        String resultString = null;
        JSONArray data = null;

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("barcode", bracode);
            params.put("storeId", Long.toString(storeId));
            params.put("page", Integer.toString(page));
            params.put("limit", Integer.toString(StaticValues.LIMIT));

            resultString = URLConnectionUtil.post("product/search", params);
            Log.v("supai search", "from web");
            Log.v("supai resultString", resultString);
            data = new JSONArray(resultString);

            for(int a=0; a<data.length(); a++)
            {
                try
                {
                    JSONObject dataObject = data.getJSONObject(a);

                    //赋值
                    Product product = getProductFromJsonObject(dataObject);
                    if(product != null)
                    {
                        result.add(product);
                    }

//                    Product product = new Product();
//                    product.setId(dataObject.getInt("id"));
//                    product.setName(dataObject.getString("name"));
//                    product.setAlias(dataObject.getString("alias"));
//                    product.setOrigin(dataObject.getString("origin"));
//                    product.setMerchant(dataObject.getString("merchant"));
//                    product.setMerchantCode(dataObject.getString("merchant_code"));
//                    product.setPrice(dataObject.getDouble("price"));
//                    product.setCount(dataObject.getInt("count"));
//                    product.setAdditional(dataObject.getString("additional"));
//                    product.setStoreId(dataObject.getInt("store_id"));
//                    product.setStoreName(dataObject.getString("store_name"));
//                    product.setAddress(dataObject.getString("address"));
//
//                    product.setImgUrl(dataObject.getString("img"));
//
//                    product.setFavourite(dataObject.getInt("favourite"));


                }catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    //修改商品
    public Product update(Activity activity, Product product)
    {
        String cacheKey = StaticValues.CACHE_KEY_PRODUCT_DETAIL+'_'+product.getId();

        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(product.getId()));
        params.put("description", product.getAdditional());
        params.put("price", Double.toString(product.getPrice()));
        params.put("count", Integer.toString(product.getCount()));
        params.put("status", Integer.toString(product.getStatus()));
        params.put("alias", product.getAlias());

        if(product.getImgUrl() != null)
        {
            params.put("img", product.getImgUrl());
        }

        try
        {
            //商品信息
            resultString = URLConnectionUtil.post("product/update", params);
            Log.v("supai resultString", resultString);

            JSONObject data = new JSONObject(resultString);
            if(data.getBoolean("success"))
            {
                return product;
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

    //收藏商品
    public boolean addFavourite(long userid, long productId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("productId", Long.toString(productId));

        try
        {
            //商品信息
            resultString = URLConnectionUtil.post("product/addFavourite", params);
            Log.v("supai resultString", resultString);

            JSONObject jsonObject = new JSONObject(resultString);
            if(jsonObject.getBoolean("success"))
            {
                result = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //取消收藏商品
    public boolean unfavourite(long userid, long productId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("productId", Long.toString(productId));

        try
        {
            //商品信息
            resultString = URLConnectionUtil.post("product/unfavourite", params);
            Log.v("supai resultString", resultString);

            JSONObject jsonObject = new JSONObject(resultString);
            if(jsonObject.getBoolean("success"))
            {
                result = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //删除商品
    public boolean delete(long id)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(id));

        try
        {
            //商品信息
            resultString = URLConnectionUtil.post("product/delete", params);
            Log.v("supai resultString", resultString);

            JSONObject jsonObject = new JSONObject(resultString);
            if(jsonObject.getBoolean("success"))
            {
                result = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 手动添加商品
     * @return
     */
    public Product addManually(Product product)
    {
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("alias", product.getAlias());
        params.put("description", product.getAdditional());
        params.put("price", Double.toString(product.getPrice()));
        params.put("img", product.getImgUrl());
        params.put("storeId", Long.toString(product.getStoreId()));
        params.put("count", Integer.toString(product.getCount()));

        try
        {
            //商品信息
            resultString = URLConnectionUtil.post("product/addManually", params);
            Log.v("supai resultString", resultString);

            JSONObject jsonObject = new JSONObject(resultString);

            if(jsonObject.getBoolean("success"))
            {
                product.setId(jsonObject.getLong("id"));

            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return product;

    }

    /**
     * 根据名称查询商品
     * @return
     */
    public ArrayList<Product> searchByName(Activity activity, String alias, long storeId, int page, boolean forceRefreash)
    {
        String cacheKey = StaticValues.CACHE_KEY_SEARCH_BY_NAME+'_'+alias+'_'+storeId;

        JSONArray data = null;
        ArrayList<Product> result = new ArrayList<Product>();
        String resultString = null;

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("alias", alias);
            params.put("storeId", Long.toString(storeId));
            params.put("page", Integer.toString(page));
            params.put("limit", Integer.toString(StaticValues.LIMIT));

            resultString = URLConnectionUtil.post("/product/searchByName", params);
            data = new JSONArray(resultString);
            Log.v("supai search by name", "from web");
            Log.v("supai resultString", resultString);

            for(int a=0; a<data.length(); a++)
            {
                try
                {
                    JSONObject dataObject = data.getJSONObject(a);

                    //赋值
                    Product product = new Product();
                    product.setId(dataObject.getInt("id"));
                    product.setName(dataObject.getString("name"));
                    product.setAlias(dataObject.getString("alias"));
                    product.setOrigin(dataObject.getString("origin"));
                    product.setMerchant(dataObject.getString("merchant"));
                    product.setMerchantCode(dataObject.getString("merchant_code"));
                    product.setPrice(dataObject.getDouble("price"));
                    product.setCount(dataObject.getInt("count"));
                    product.setAdditional(dataObject.getString("additional"));
                    product.setStoreId(dataObject.getInt("store_id"));
                    product.setStoreName(dataObject.getString("store_name"));
                    product.setAddress(dataObject.getString("address"));
                    product.setImgUrl(dataObject.getString("img"));
                    product.setFavourite(dataObject.getInt("favourite"));

                    result.add(product);

                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;

    }

    /**
     * 商品数量增加
     * @return
     */
    public Product countIncrease(Product product)
    {
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("productId", Long.toString(product.getId()));
        params.put("count", Integer.toString(product.getCount()));

        try
        {
            //商品信息
            resultString = URLConnectionUtil.post("product/countIncrease", params);
            Log.v("supai resultString", resultString);

            JSONObject jsonObject = new JSONObject(resultString);

            if(jsonObject.getBoolean("success"))
            {
                product.setCount(jsonObject.getInt("count"));

            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return product;

    }

    //是否已有商品 根据条码扫描时
//    public Product checkBarcodeExist(String barcode, long storeId)
//    {
//        Product result = null;
//        String resultString = null;
//
//        List params = new ArrayList();
//        params.add(new BasicNameValuePair("barcode", Long.toString(storeId)));
//        params.add(new BasicNameValuePair("storeId", Long.toString(storeId)));
//
//        try
//        {
//            resultString = HttpClientUtil.post("product/barcodeExist", params);
//            Log.v("supai resultString", resultString);
//
//            JSONObject data = new JSONObject(resultString);
//
//            if(data.getBoolean("success"))
//            {
//                result = new Product();
//
//            }
//
//            return result;
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }

}
