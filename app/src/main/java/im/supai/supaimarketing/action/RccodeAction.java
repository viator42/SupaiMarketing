package im.supai.supaimarketing.action;

import android.graphics.Bitmap;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Rccode;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/4/15.
 */
public class RccodeAction extends BaseAction
{
    /**
     * 根据条形码查询商品信息
     * @param barcode
     */
    public Rccode search(String barcode, long storeId)
    {
        Rccode result = new Rccode();
        result.setRccode(barcode);

        String resultString;

        //先进行本库查询,没有的话再调用API
        HashMap params = new HashMap<String, Object>();
        params.put("barcode", barcode);
        params.put("storeId", Long.toString(storeId));

        try
        {
            resultString = URLConnectionUtil.post("goods/search", params);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    result.setName(jsonObject.getString("name"));
                    result.setPriceInterval(jsonObject.getString("priceInterval"));
                    result.setOrigin(jsonObject.getString("origin"));
                    result.setMerchantCode(jsonObject.getString("merchantCode"));
                    result.setMerchant(jsonObject.getString("merchant"));

                    JSONArray imageJSONArray = jsonObject.getJSONArray("images");
                    ArrayList<Bitmap> images = new ArrayList<Bitmap>();
                    for(int a=0; a<imageJSONArray.length(); a++)
                    {
//                        byte[] byteArray= HttpClientUtil.getImageFromWeb(imageJSONArray.get(a).toString());
//                        images.add(BitmapFactory.decodeByteArray(byteArray, 0,
//                                byteArray.length));

                    }
                    result.setImages(images);

                    //商品已存在的情况
                    result.setExist(jsonObject.getBoolean("exist"));
                    if(jsonObject.getBoolean("exist"))
                    {
                        JSONObject productObject = jsonObject.getJSONObject("product");

                        Product product = new Product();

                        product.setId(productObject.getInt("id"));
                        product.setName(productObject.getString("name"));
                        product.setAlias(productObject.getString("alias"));
                        product.setOrigin(productObject.getString("origin"));
                        product.setMerchant(productObject.getString("merchant"));
                        product.setMerchantCode(productObject.getString("merchant_code"));
                        product.setPrice(productObject.getDouble("price"));
                        product.setCount(productObject.getInt("count"));
                        product.setAdditional(productObject.getString("additional"));
                        product.setStoreId(productObject.getInt("store_id"));
                        product.setStoreName(productObject.getString("store_name"));
                        product.setAddress(productObject.getString("address"));
                        product.setImgUrl(productObject.getString("img"));
                        product.setFavourite(productObject.getInt("favourite"));
                        product.setStatus(productObject.getInt("status"));

                        result.setProduct(product);
                    }

                    return result;
                }
                else
                {
                    return searchAPI(barcode);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    //条形码查询API 搜索商品信息和图片
    public Rccode searchAPI(String barcode)
    {
        String resultString = "";

        Rccode result = new Rccode();
        result.setRccode(barcode);

        HashMap params = new HashMap<String, Object>();
        params.put("pkg", "im.supai.supaimarketing");
        params.put("barcode", barcode);
        params.put("cityid", "1");
        params.put("appkey", "2a1cc8376c5a2a520cbd25a15efd9afd");

        try
        {
            resultString = URLConnectionUtil.get("http://api.juheapi.com/jhbar/bar", params);
            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getInt("error_code") == 0)
                {
                    JSONObject summary = jsonObject.getJSONObject("result").getJSONObject("summary");

                    result.setName(summary.getString("name"));
                    result.setPriceInterval(summary.getString("interval"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

        params = new HashMap<String, Object>();
        params.put("ean", barcode);
        try
        {
            resultString = URLConnectionUtil.get("http://www.liantu.com/tiaoma/query.php", params);
            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                result.setMerchant(jsonObject.getString("fac_name"));
                result.setOrigin(jsonObject.getString("guobie"));
                result.setMerchantCode(jsonObject.getString("faccode"));

            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //搜索图片
        try
        {
            result.setImages(new ImageAction().searchImage(result.getName(), StaticValues.IMAGE_SEARCH_COUNT));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;

    }

}
