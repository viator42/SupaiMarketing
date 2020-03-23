package im.supai.supaimarketing.action;

import org.json.JSONException;
import org.json.JSONObject;

import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;

/**
 * Created by viator42 on 15/9/24.
 */
public class BaseAction
{
    protected Product getProductFromJsonObject(JSONObject dataObject)
    {
        Product product = new Product();
        try {
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

            return product;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected Product getProductWithoutBarcodeFromJsonObject(JSONObject dataObject)
    {
        Product product = new Product();
        try {
            product.setId(dataObject.getLong("id"));
            product.setAlias(dataObject.getString("alias"));
            product.setImgUrl(dataObject.getString("img"));
            product.setAdditional(dataObject.getString("additional"));
            product.setPrice(dataObject.getDouble("price"));
            product.setCount(dataObject.getInt("count"));
            product.setStatus(dataObject.getInt("status"));
            product.setStoreId(dataObject.getLong("storeId"));
            product.setFavourite(dataObject.getInt("favourite"));

            return product;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected Store getStoreFromJsonObject(JSONObject dataObject)
    {
        Store result = new Store();
        try
        {
            result.setId(dataObject.getInt("id"));
            result.setLogoUrl(dataObject.getString("logo"));
            result.setName(dataObject.getString("name"));
            result.setUserid(dataObject.getLong("user_id"));
            result.setAddress(dataObject.getString("address"));
            result.setDescription(dataObject.getString("description"));
            result.setLongitude(dataObject.getDouble("longitude"));
            result.setLatitude(dataObject.getDouble("latitude"));
            result.setFavourite(dataObject.getInt("favourite"));
            result.setArea(dataObject.getString("area_id"));
            result.setStatus(dataObject.getInt("status"));
            result.setStorageWarning(dataObject.getInt("storage_warning"));

        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return result;

    }
}
