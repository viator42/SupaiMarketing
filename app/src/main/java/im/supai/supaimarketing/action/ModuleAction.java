package im.supai.supaimarketing.action;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import im.supai.supaimarketing.model.Module;
import im.supai.supaimarketing.model.ModuleCategory;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/8/4.
 */
public class ModuleAction extends BaseAction
{
    /**
     * 获取用户的收费模块
     *
     */
    public HashMap<String, ModuleCategory> list(long userid)
    {
        HashMap<String, ModuleCategory> result= new HashMap<String, ModuleCategory>();

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));

        try
        {
            String resultString = URLConnectionUtil.post("module/list", params);

            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONArray jsonArray = new JSONArray(resultString);

                for(int a=0; a<jsonArray.length(); a++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(a);
                    ModuleCategory module = new ModuleCategory();

                    module.setId(jsonObject.getLong("id"));
                    module.setName(jsonObject.getString("name"));
                    module.setDescription(jsonObject.getString("description"));
                    module.setCode(jsonObject.getString("code"));
                    
                    result.put(module.getCode(), module);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //获取收费模块的详细信息
    public ModuleCategory detail(long id)
    {
        ModuleCategory result = null;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(id));

        try
        {
            String resultString = URLConnectionUtil.post("module/detail", params);

            if(resultString != null)
            {
                Log.v("supai resultString", resultString);
                JSONObject data = new JSONObject(resultString);

                result = new ModuleCategory();

                result.setId(data.getLong("id"));
                result.setName(data.getString("name"));
                result.setDescription(data.getString("description"));
                result.setCode(data.getString("code"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //收费模块提交申请
    public boolean apply(Module module)
    {
        HashMap params = new HashMap<String, Object>();

        boolean result = false;

        params.put("userid", Long.toString(module.getUserid()));
        params.put("username", module.getUsername());
        params.put("tel", module.getTel());
        params.put("address", module.getAddress());
        params.put("bundle", Long.toString(module.getBundleId()));

        String license = module.getLicense();
        if(license != null && !license.isEmpty())
        {
            params.put("license", license);
        }

        try
        {
            String resultString = URLConnectionUtil.post("module/apply", params);

            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject data = new JSONObject(resultString);

                if(data.getBoolean("success"))
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
