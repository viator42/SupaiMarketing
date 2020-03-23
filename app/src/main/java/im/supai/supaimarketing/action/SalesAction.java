package im.supai.supaimarketing.action;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Sale;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/9/29.
 * 售货员销售
 */
public class SalesAction
{
    //查询员工的有效性
    //成功返回店铺信息
    public Store clerkAvalible(long userid)
    {
        Store result = null;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));

        try
        {
            String resultString = URLConnectionUtil.post("store/clerkAvaliable", params);

            JSONObject data = new JSONObject(resultString);
            if(data.getBoolean("success"))
            {
                result = new Store();

                result.setId(data.getLong("storeId"));
                result.setName(data.getString("storeName"));
                result.setAddress(data.getString("storeAddress"));
                result.setLogoUrl(data.getString("storeLogo"));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 员工列表
     */
    public ArrayList clerks(long storeId)
    {
        ArrayList<User> result = new ArrayList<User>();

        HashMap params = new HashMap<String, Object>();
        params.put("storeId", Long.toString(storeId));

        try
        {
            String resultString = URLConnectionUtil.post("store/clerks", params);

            JSONArray data = new JSONArray(resultString);

            for(int a=0; a<data.length(); a++)
            {
                //赋值
                User user = new User();
                JSONObject clerkObject = data.getJSONObject(a);

                user.setId(clerkObject.getLong("id"));
                user.setSn(clerkObject.getString("sn"));
                user.setName(clerkObject.getString("name"));
                user.setUsername(clerkObject.getString("username"));
                user.setIconUrl(clerkObject.getString("icon"));
                user.setTel(clerkObject.getString("tel"));

                result.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 添加员工
     */
    public boolean clerkRegister(long userid, long storeId)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("storeId", Long.toString(storeId));

        try
        {
            String resultString = URLConnectionUtil.post("store/clerkRegister", params);
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
     * 解雇员工
     */
    public boolean clerkRemove(long userid)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));

        try
        {
            String resultString = URLConnectionUtil.post("store/clerkRemove", params);
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

    public Store detail(long userid, long storeId, double summary, ArrayList<Product> products)
    {
        Store result = new Store();

        JSONArray jsonArray = new JSONArray();

        for(int a=0; a<products.size(); a++)
        {
            Product product = products.get(a);
            JSONObject productJSONObject = new JSONObject();
            try {
                productJSONObject.put("productId", product.getId());
                productJSONObject.put("price", product.getPrice());
                productJSONObject.put("count", product.getCount());

                jsonArray.put(productJSONObject);

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }


        }

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("storeId", Long.toString(storeId));
        params.put("summary", Double.toString(summary));
        params.put("products", jsonArray.toString());

        try
        {
            String resultString = URLConnectionUtil.post("store/detail", params);

            JSONObject jsonObject = new JSONObject(resultString);
            Log.v("supai resultString", resultString);
            if(jsonObject.getBoolean("success"))
            {
//                JSONObject dataObject = jsonObject.getJSONObject("data");
//
//                //赋值
//                result.setId(dataObject.getInt("id"));
//                result.setName(dataObject.getString("name"));
//                result.setDescription(dataObject.getString("description"));
//                result.setAddress(dataObject.getString("address"));
//                result.setArea(dataObject.getString("area_id"));
//                result.setLogoUrl(dataObject.getString("logo"));
//                result.setUserid(dataObject.getLong("user_id"));

//                result.setLogo(HttpClientUtil.getImageFromWeb(dataObject.getString("logo")));

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
     * 售货员销售
     *
     */

    //商品列表
    public ArrayList<Sale> list(long clerkId, long storeId)
    {
        ArrayList<Sale> result = new ArrayList<Sale>();
        HashMap params = new HashMap<String, Object>();

        params.put("clerkId", Long.toString(clerkId));
        params.put("storeId", Long.toString(storeId));

        try
        {
            String resultString = URLConnectionUtil.post("sales/list", params);

            JSONArray data = new JSONArray(resultString);

            for(int a=0; a<data.length(); a++)
            {
                JSONObject jsonObject = data.getJSONObject(a);

                Sale sale = new Sale();

                sale.setId(jsonObject.getLong("id"));
                sale.setProductId(jsonObject.getLong("product_id"));
                sale.setAlias(jsonObject.getString("alias"));
                sale.setCount(jsonObject.getInt("count"));
                sale.setPrice(jsonObject.getDouble("price"));
                sale.setClerkId(jsonObject.getLong("clerk_id"));
                sale.setAddTime(jsonObject.getInt("add_time"));
                sale.setStoreId(jsonObject.getLong("store_id"));
                sale.setImg(jsonObject.getString("img"));

                result.add(sale);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //添加商品
    public boolean add(Sale sale)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("productId", Long.toString(sale.getProductId()));
        params.put("clerkId", Long.toString(sale.getClerkId()));
        params.put("storeId", Long.toString(sale.getStoreId()));
        params.put("price", Double.toString(sale.getPrice()));
        params.put("count", Integer.toString(sale.getCount()));

        try
        {
            String resultString = URLConnectionUtil.post("sales/add", params);

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

    //删除商品
    public boolean remove(long salesId)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("salesId", Long.toString(salesId));

        try
        {
            String resultString = URLConnectionUtil.post("sales/remove", params);

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

    //商品修改
    public boolean edit(Sale sale)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("salesId", Long.toString(sale.getId()));
        params.put("price", Double.toString(sale.getPrice()));
        params.put("count", Integer.toString(sale.getCount()));

        try
        {
            String resultString = URLConnectionUtil.post("sales/edit", params);

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

    //清空列表
    public boolean clear(long clerkId, long storeId)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("clerkId", Long.toString(clerkId));
        params.put("storeId", Long.toString(storeId));

        try
        {
            String resultString = URLConnectionUtil.post("sales/clearAll", params);

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

    //生成订单
    public boolean createOrder(long clerkId, long storeId)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("clerkId", Long.toString(clerkId));
        params.put("storeId", Long.toString(storeId));

        try
        {
            String resultString = URLConnectionUtil.post("sales/createOrder", params);

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

    //订单历史
    // clerkId = 0 查询所有人    != 0 查询某个售货员
    public ArrayList<Order> orders(int type, long clerkId, long storeId, int page)
    {
        ArrayList<Order> result = new ArrayList<Order>();

        HashMap params = new HashMap<String, Object>();
        params.put("type", Integer.toString(type));
        params.put("clerkId", Long.toString(clerkId));
        params.put("storeId", Long.toString(storeId));
        params.put("page", Integer.toString(page));
        params.put("limit", Integer.toString(StaticValues.LIMIT));

        try
        {
            String resultString = URLConnectionUtil.post("sales/orders", params);

            JSONArray data = new JSONArray(resultString);

            for(int a=0; a<data.length(); a++)
            {
                //赋值
                Order order = new Order();
                JSONObject orderObject = data.getJSONObject(a);

                order.setId(orderObject.getLong("id"));
                order.setCreateTime(orderObject.getInt("create_time"));
                order.setMerchangName(orderObject.getString("merchant_name"));
                order.setMerchantId(orderObject.getLong("merchant_id"));
                order.setCount(orderObject.getInt("count"));
                order.setSummary(orderObject.getDouble("summary"));

                result.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
