package im.supai.supaimarketing.action;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.base.BaseAction;
import im.supai.supaimarketing.util.URLConnectionUtil;

public class AreaAction extends BaseAction {
    /**
     * 获取节点列表
     * @return
     */
    public ArrayList<Map<String, Object>> getChildren(int pcode)
    {
        ArrayList<Map<String, Object>> result= new ArrayList<Map<String, Object>>();

        HashMap params = new HashMap<String, Object>();
        params.put("pcode", Integer.toString(pcode));

        try
        {
            String resultString = URLConnectionUtil.post("area/children", params);
            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONArray jsonArray = new JSONArray(resultString);

                for(int a=0; a<jsonArray.length(); a++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(a);

                    Map province = new HashMap();
                    province.put("id", jsonObject.getString("code"));
                    province.put("name", jsonObject.getString("name"));

                    result.add(province);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
