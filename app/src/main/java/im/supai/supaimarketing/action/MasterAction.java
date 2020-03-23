package im.supai.supaimarketing.action;

import android.util.Log;

import org.json.JSONObject;
import java.util.HashMap;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/10/19.
 */
public class MasterAction
{

    //授权
    public boolean auth(String imie)
    {
        boolean result = false;

        String resultString = null;
        HashMap params = new HashMap<String, Object>();
        params.put("imie", imie);

        try
        {
            resultString = URLConnectionUtil.post("user/auth", params);
            Log.v("supai resultString", resultString);

            JSONObject data = new JSONObject(resultString);
            result = data.getBoolean("success");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    //返回网页内容
    public boolean addProManually(String imie, String tel, long bundleId)
    {
        boolean result = false;

        String resultString = null;
        HashMap params = new HashMap<String, Object>();

        params.put("imie", imie);
        params.put("tel", tel);
        params.put("bundleId", Long.toString(bundleId));

        try
        {
            resultString = URLConnectionUtil.post("user/addProManually", params);
            Log.v("supai resultString", resultString);

            JSONObject data = new JSONObject(resultString);
            result = data.getBoolean("success");

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;

    }
}
