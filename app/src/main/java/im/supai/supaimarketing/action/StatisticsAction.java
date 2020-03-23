package im.supai.supaimarketing.action;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Statistics;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/9/13.
 */
public class StatisticsAction extends BaseAction
{
    /**
     * 查询库存量少的商品列表
     */
    public ArrayList<Product> productLowInStore(long storeId, int page)
    {
        ArrayList<Product> result = new ArrayList<Product>();

        HashMap params = new HashMap<String, Object>();
        params.put("storeId", Long.toString(storeId));
        params.put("page", Integer.toString(page));
        params.put("limit", Integer.toString(StaticValues.LIMIT));

        try
        {
            String resultString = URLConnectionUtil.post("statistics/productLowInStore", params);

            JSONArray data = new JSONArray(resultString);

            for(int a=0; a<data.length(); a++)
            {
                //赋值
                Product product = new Product();
                JSONObject productObject = data.getJSONObject(a);

                product.setId(productObject.getLong("id"));
                product.setAlias(productObject.getString("alias"));
                product.setPrice(productObject.getDouble("price"));
                product.setCount(productObject.getInt("count"));
                product.setStatus(productObject.getInt("status"));
                product.setImgUrl(productObject.getString("img"));

                result.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }

    /**
     * 获取统计信息
     */
    public Statistics getInfo(long storeId)
    {
        Statistics result = new Statistics();

        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("storeId", Long.toString(storeId));

        try
        {
            resultString = URLConnectionUtil.post("statistics/info", params);
            Log.v("supai resultString", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                result.setProductCount(jsonObject.getInt("productCount"));
                result.setProductValueSum(jsonObject.getDouble("productValueSum"));
                result.setTurnoverToday(jsonObject.getDouble("turnoverToday"));
                result.setTurnoverMonth(jsonObject.getDouble("turnoverMonth"));
                result.setTurnoverYear(jsonObject.getDouble("turnoverYear"));
                result.setUnpaidSum(jsonObject.getDouble("unpaidSum"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 商品明细
     */
    public ArrayList<Product> particulars(long storeId, int page)
    {
        ArrayList result = new ArrayList();

        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("storeId", Long.toString(storeId));
        params.put("page", Integer.toString(page));
        params.put("limit", Integer.toString(StaticValues.LIMIT));

        try
        {
            resultString = URLConnectionUtil.post("statistics/particulars", params);
            Log.v("supai resultString", resultString);
            if(resultString != null)
            {
                JSONArray data = new JSONArray(resultString);
                for(int a=0; a<data.length(); a++)
                {
                    try
                    {
                        //赋值
                        JSONObject jsonObject = data.getJSONObject(a);

                        Product product = new Product();
                        product.setId(jsonObject.getLong("id"));
                        product.setAlias(jsonObject.getString("alias"));
                        product.setImgUrl(jsonObject.getString("img"));
                        product.setPrice(jsonObject.getDouble("price"));
                        product.setCount(jsonObject.getInt("count"));

                        result.add(product);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        continue;
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
