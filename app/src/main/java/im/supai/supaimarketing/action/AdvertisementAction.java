package im.supai.supaimarketing.action;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import im.supai.supaimarketing.model.Advertisement;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/8/28.
 */
public class AdvertisementAction extends BaseAction{
    //返回所有广告
    public ArrayList<Advertisement> all()
    {
        ArrayList<Advertisement> result = new ArrayList<Advertisement>();

        HashMap params = new HashMap<String, Object>();

        try
        {
            String resultString = URLConnectionUtil.post("advertisement/all", params);

            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONArray data = new JSONArray(resultString);

                for(int a=0; a<data.length(); a++)
                {
                    try
                    {
                        JSONObject jsonObjectbject = data.getJSONObject(a);
                        Advertisement advertisement = new Advertisement();

                        advertisement.setId(jsonObjectbject.getLong("id"));
                        advertisement.setSn(jsonObjectbject.getString("sn"));
                        advertisement.setTitle(jsonObjectbject.getString("title"));
                        advertisement.setContent(jsonObjectbject.getString("content"));
                        advertisement.setCreateTime(jsonObjectbject.getLong("create_time"));
                        advertisement.setStartTime(jsonObjectbject.getLong("start_time"));
                        advertisement.setFinishTime(jsonObjectbject.getLong("finish_time"));
                        advertisement.setLogo(jsonObjectbject.getString("logo"));

                        result.add(advertisement);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        continue;
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

        return result;

    }


}
