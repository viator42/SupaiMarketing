package im.supai.supaimarketing.action;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.OrderDetail;
import im.supai.supaimarketing.model.Orders;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/4/2.
 *
 */
public class OrderAction extends BaseAction
{
    //public ArrayList<Order> orderlist;

    //商户查看已提交的订单
    public void activeOrdersForMerchant()
    {

    }
    /*
    //返回用户已提交,已发货的所有订单
    public ArrayList<Order> activeOrdersForCustomer(final Order order)
    {
        ArrayList<Order> result = new ArrayList<Order>();
        String resultString = null;

        List params = new ArrayList();
        params.add(new BasicNameValuePair("id", "1"));
        try
        {
            resultString = HttpClientUtil.post("order/ActiveOrdersForCustomer", params);

            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    JSONArray data = jsonObject.getJSONArray("data");

                    for(int a=0; a<data.length(); a++)
                    {
                        Order o = new Order();
                        JSONObject object = data.getJSONObject(a);
                        o.setId(object.getLong("id"));
                        o.setMerchantId(object.getInt("merchantId"));
                        o.setMerchantName(object.getString("merchantName"));
                        o.setStoreId(object.getInt("storeId"));
                        o.setStoreName(object.getString("storeName"));
                        o.setCreateTime(object.getInt("createTime"));
                        o.setSummary(object.getDouble("summary"));
                        o.setStatus(object.getString("status"));
                        o.setAdditional(object.getString("additional"));

                        result.add(o);

                    }
                }
                else
                {
                    return null;

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

        return result;

    }
    */

    //返回已提交,已发货的所有订单
    public Orders activeOrders(Activity activity, long userid, int type, int cpage, int mpage, boolean forceRefreash)
    {
        String cacheKey = StaticValues.CACHE_KEY_ORDERS+'_'+userid+'_'+type+'_'+cpage+'_'+mpage;

        Orders result = new Orders();
        String resultString = null;
        int unreadCount = 0;
        result.setType(type);

        JSONObject data = null;

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("userid", Long.toString(userid));
            params.put("type", Integer.toString(type));
            params.put("cpage", Integer.toString(cpage));
            params.put("mpage", Integer.toString(mpage));
            params.put("limit", Integer.toString(StaticValues.LIMIT));
            resultString = URLConnectionUtil.post("order/ActiveOrders", params);
            data = new JSONObject(resultString);
            Log.v("supai orders", "from web");
            Log.v("supai resultString", resultString);

            JSONArray merchantJSONArray = data.getJSONArray("merchantList");
            JSONArray customerJSONArray = data.getJSONArray("customerList");

            if(customerJSONArray.length() != 0)
            {
                ArrayList<Order> customerList = new ArrayList<Order>();

                //客户订单列表
                for(int a=0; a<customerJSONArray.length(); a++)
                {
                    Order order = getOrderInfo(customerJSONArray.getJSONObject(a));
                    if(order != null)
                    {
                        customerList.add(order);

                        //未读计数 商家已发货时
                        if(order.getStatus() == 2 && order.getReaded() == 1)
                        {
                            unreadCount += 1;
                        }
                    }

                }

                result.setCustomerList(customerList);
            }
            else
            {
                result.setCustomerList(null);
            }

            if(merchantJSONArray.length() != 0)
            {
                ArrayList<Order> merchantList = new ArrayList<Order>();

                //商户订单列表
                for(int b=0; b<merchantJSONArray.length(); b++)
                {
                    Order order = getOrderInfo(merchantJSONArray.getJSONObject(b));
                    if(order != null)
                    {
                        merchantList.add(order);

                        //未读计数 客户提交新订单时
                        if(order.getStatus() == 1 && order.getReaded() == 1)
                        {
                            unreadCount += 1;
                        }
                    }

                }

                result.setMerchantList(merchantList);
            }
            else
            {
                result.setMerchantList(null);
            }

            result.setUnreadCount(unreadCount);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //查询订单详情
    public Order detail(Context context, long orderId, boolean forceRefreash)
    {
        String cacheKey = StaticValues.CACHE_KEY_ORDER_DETAIL+'_'+orderId;

        Order result = null;
        String resultString = null;
        JSONObject data = null;

        try
        {
            HashMap params = new HashMap<String, Object>();
            params.put("orderId", Long.toString(orderId));
            resultString = URLConnectionUtil.post("order/detail", params);
            Log.v("supai order detail", "from web");
            Log.v("supai resultString", resultString);
            data = new JSONObject(resultString);

            if(data.getBoolean("success"))
            {
                result = getOrderInfo(data);
                if(result != null)
                {
                    ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

                    JSONArray orderDetailsObjs = data.getJSONArray("orderDetailList");
                    for(int a=0; a<orderDetailsObjs.length(); a++)
                    {
                        OrderDetail orderDetail = new OrderDetail();
                        JSONObject orderDetailsObj = orderDetailsObjs.getJSONObject(a);

                        orderDetail.setId(orderDetailsObj.getInt("id"));
                        orderDetail.setProductId(orderDetailsObj.getInt("productId"));
                        orderDetail.setCount(orderDetailsObj.getInt("count"));
                        orderDetail.setPrice(orderDetailsObj.getDouble("price"));
                        orderDetail.setImgUrl(orderDetailsObj.getString("image"));
                        orderDetail.setGoodsName(orderDetailsObj.getString("name"));

                        orderDetails.add(orderDetail);
                    }
                    result.setOrderDetails(orderDetails);
                }
                else
                {
                    return null;
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

        return result;
    }

    //订单取消
    public boolean cancel(long orderId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("orderId", Long.toString(orderId));

        try
        {
            resultString = URLConnectionUtil.post("order/cancel", params);
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

    //订单取消申请
    public boolean returnApply(long orderId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("orderId", Long.toString(orderId));

        try
        {
            resultString = URLConnectionUtil.post("order/returnApply", params);
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

    //订单取消
    public boolean returnOrder(long orderId, int accept)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("orderId", Long.toString(orderId));
        params.put("accept", Integer.toString(accept));

        try
        {
            resultString = URLConnectionUtil.post("order/return", params);
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

    //商家确认订单,开始发货
    public boolean confirm(long orderId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("orderId", Long.toString(orderId));

        try
        {
            resultString = URLConnectionUtil.post("order/confirm", params);
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

    //订单完成确认
    public boolean delivered(long orderId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("orderId", Long.toString(orderId));

        try
        {
            resultString = URLConnectionUtil.post("order/delivered", params);
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

    //订单设为已读
    public boolean setReaded(long orderId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("orderId", Long.toString(orderId));

        try
        {
            resultString = URLConnectionUtil.post("order/readed", params);
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

    //获取订单信息
    private Order getOrderInfo(JSONObject object)
    {
        try
        {
            Order o = new Order();

            o.setId(object.getLong("id"));
            o.setMerchantId(object.getInt("merchantId"));
            o.setCustomerId(object.getInt("customerId"));
            o.setCustomerName(object.getString("customerName"));
            o.setMerchangName(object.getString("merchantName"));
            o.setCustomerTel(object.getString("customerTel"));
            o.setMerchantTel(object.getString("merchantTel"));
            o.setStoreId(object.getInt("storeId"));
            o.setStoreName(object.getString("storeName"));
            o.setCreateTime(object.getLong("createTime"));
            o.setCount(object.getInt("count"));
            o.setSummary(object.getDouble("summary"));
            o.setStatus(object.getInt("status"));
            o.setAdditional(object.getString("additional"));
            o.setCustomerLongitude(object.getDouble("customerLongitude"));
            o.setCustomerLatitude(object.getDouble("customerLatitude"));
            o.setMerchantLongitude(object.getDouble("merchantLongitude"));
            o.setMerchantLatitude(object.getDouble("merchantLatitude"));
            o.setReaded(object.getInt("readed"));
            o.setSn(object.getString("sn"));
            o.setCustomerAddress(object.getString("customerAddress"));
            o.setMerchantAddress(object.getString("merchantAddress"));
            o.setPayMethod(object.getInt("payMethod"));
            o.setPaid(object.getInt("paid"));
            o.setPayAfter(object.getInt("payAfter"));
            return o;

        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    //订单支付
//    public boolean setReaded(long orderId)
//    {
//
//    }

    //订单设为已支付状态
    public boolean setPaid(long orderId)
    {
        boolean result = false;
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("orderId", Long.toString(orderId));

        try
        {
            resultString = URLConnectionUtil.post("order/setPaid", params);
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

}
