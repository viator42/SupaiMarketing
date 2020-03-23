package im.supai.supaimarketing.action;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.model.CartDetail;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.PayOptions;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/4/12.
 */
public class CartAction extends BaseAction
{
//    private ACache mCache;

    /**
     * 获取购物车列表
     * @return
     */
    public List<Cart> list(Activity activity, long userid, boolean forceRefreash)
    {
        String cacheKey = StaticValues.CACHE_KEY_CARTS+'_'+userid;
//        mCache = ACache.get(activity);
        JSONObject data = null;
        List<Cart> cartList = new ArrayList<Cart>();

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("userid", Long.toString(userid));
            String resultString = URLConnectionUtil.post("cart/list", params);
            data = new JSONObject(resultString);

            /*
            if(forceRefreash == true)
            {
                List params = new ArrayList();
                params.add(new BasicNameValuePair("userid", Long.toString(userid)));
                String resultString = HttpClientUtil.post("cart/list", params);
                data = new JSONObject(resultString);
                Log.v("supai carts", "from web");
                Log.v("supai resultString", resultString);
                //存入缓存
                mCache.put(cacheKey, data, StaticValues.CACHE_LIFE);

            }
            else
            {
                data = mCache.getAsJSONObject(cacheKey);
                if(data ==null)
                {
                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("userid", Long.toString(userid)));
                    String resultString = HttpClientUtil.post("cart/list", params);
                    data = new JSONObject(resultString);
                    Log.v("supai carts", "from web");
                    Log.v("supai resultString", resultString);
                    //存入缓存
                    mCache.put(cacheKey, data, StaticValues.CACHE_LIFE);

                }
                else
                {
                    Log.v("supai carts", "from cache");

                }

            }

             */

            if(data.getBoolean("success"))
            {
                JSONArray cartJSONArray = data.getJSONArray("data");

                for(int a=0; a<cartJSONArray.length(); a++)
                {
                    try
                    {
                        JSONObject cartObject = cartJSONArray.getJSONObject(a);
                        Cart cart = new Cart();

                        cart.setId(cartObject.getLong("id"));
                        cart.setStoreId(cartObject.getLong("storeId"));
                        cart.setStoreName(cartObject.getString("storeName"));
                        cart.setUserId(cartObject.getLong("userId"));
                        cart.setStatus(cartObject.getInt("status"));
                        cart.setCount(cartObject.getInt("count"));
                        cart.setSummary(cartObject.getDouble("summary"));

                        //获取商品列表
                        List<CartDetail> cartDetails = new ArrayList<CartDetail>();

                        JSONArray detailJSONArray = cartObject.getJSONArray("details");
                        for(int b=0; b<detailJSONArray.length(); b++)
                        {
                            CartDetail cartDetail = new CartDetail();
                            JSONObject cartJSONObject = detailJSONArray.getJSONObject(b);
                            cartDetail.setId(cartJSONObject.getLong("id"));
                            cartDetail.setProductId(cartJSONObject.getLong("productId"));
                            cartDetail.setGoodsName(cartJSONObject.getString("name"));
                            cartDetail.setPrice(cartJSONObject.getDouble("price"));
                            cartDetail.setCount(cartJSONObject.getInt("count"));
                            cartDetail.setImgUrl(cartJSONObject.getString("img"));

                            //获取图片
//                                cartDetail.setImg(HttpClientUtil.getImageFromWeb(cartDetail.getImgUrl()));

                            cartDetails.add(cartDetail);
                        }
                        cart.setDetailList(cartDetails);

                        cartList.add(cart);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        continue;
                    }

                }
            }
            else
            {
                return null;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return cartList;
    }

    /**
     * 添加到购物车
     */
    public boolean addToCart(long userid, long storeId, long productId, int count, double price)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("storeId", Long.toString(storeId));
        params.put("productId", Long.toString(productId));
        params.put("price", Double.toString(price));
        params.put("count", Integer.toString(count));

        try
        {
            String resultString = URLConnectionUtil.post("cart/add", params);

            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    result = true;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 修改数量
     */
    public boolean updateCount(long id, int count)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(id));
        params.put("count", Integer.toString(count));

        try
        {
            String resultString = URLConnectionUtil.post("cart/updateCount", params);
            Log.v("supai resultString", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    result = true;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 删除商品
     */
    public boolean remove(long id)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(id));

        try
        {
            String resultString = URLConnectionUtil.post("cart/remove", params);
//            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    result = true;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 结算,生成订单
     * cart id.
     */
    public boolean createOrder(long cartId, Order order)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(cartId));
        params.put("additional", order.getAdditional());
        params.put("address", order.getCustomerAddress());
        params.put("payMethod", Integer.toString(order.getPayMethod()));
        params.put("paid", Integer.toString(order.getPaid()));
        params.put("payAfter", Integer.toString(order.getPayAfter()));

        try
        {
            String resultString = URLConnectionUtil.post("cart/createOrder", params);
            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    result = true;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 结算,生成订单
     * cart id.
     */
    public PayOptions getPayOptions(long cartId)
    {
        PayOptions result = null;

        HashMap params = new HashMap<String, Object>();
        params.put("cartId", Long.toString(cartId));

        try
        {
            String resultString = URLConnectionUtil.post("cart/getPayOptions", params);
            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                result = new PayOptions();

                result.setPayAfterSupport(jsonObject.getBoolean("pay_after"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

}
