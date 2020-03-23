package im.supai.supaimarketing.action;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import im.supai.supaimarketing.model.Follower;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/9/29.
 */
public class FollowerAction
{
    /**
     * 获取店铺关注者
     *
     */
    public ArrayList<Follower> follower(Activity activity, long storeid, int page, boolean forceRefreash)
    {
        ArrayList<Follower> result = new ArrayList<Follower>();
        JSONArray data = null;
        String cacheKey = StaticValues.CACHE_KEY_FOLLOWER+'_' + storeid;

        HashMap params = new HashMap<String, Object>();
        params.put("storeid", Long.toString(storeid));
        params.put("page", Integer.toString(page));
        params.put("limit", Integer.toString(StaticValues.LIMIT));

        try
        {
            String resultString = URLConnectionUtil.post("store/follower", params);
            Log.v("supai follower", "from web");
            Log.v("supai resultString", resultString);
            data = new JSONArray(resultString);
            /*
            data = mCache.getAsJSONArray(cacheKey);
            if(forceRefreash == true)
            {
                String resultString = HttpClientUtil.post("store/follower", params);
                Log.v("supai follower", "from web");
                Log.v("supai resultString", resultString);
                data = new JSONArray(resultString);

                //存入缓存
                mCache.put(cacheKey, data, StaticValues.CACHE_LIFE);

            }
            else
            {
                if(data == null)
                {
                    String resultString = HttpClientUtil.post("store/follower", params);
                    Log.v("supai follower", "from web");
                    Log.v("supai resultString", resultString);
                    data = new JSONArray(resultString);

                    //存入缓存
                    mCache.put(cacheKey, data, StaticValues.CACHE_LIFE);
                }
                else
                {
                    Log.v("supai follower", "from cache");
                }

            }

             */

            for(int a=0; a<data.length(); a++)
            {
                //赋值
                JSONObject followerObject = data.getJSONObject(a);

                Follower follower = getFollowerFormJSONObject(followerObject);
                if(follower != null)
                {
                    result.add(follower);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 查询店铺关注者
     *
     */
    public ArrayList<Follower> searchFollower(Activity activity, long storeid, String keyword, int type)
    {
        ArrayList<Follower> result = new ArrayList<Follower>();

        HashMap params = new HashMap<String, Object>();
        params.put("storeid", Long.toString(storeid));
        params.put("keyword", keyword);
        params.put("type", Integer.toString(type));

        try
        {
            String resultString = URLConnectionUtil.post("store/searchFollower", params);

            JSONArray data = new JSONArray(resultString);
            for(int a=0; a<data.length(); a++)
            {
                //赋值
                JSONObject followerObject = data.getJSONObject(a);

                Follower follower = getFollowerFormJSONObject(followerObject);
                if(follower != null)
                {
                    result.add(follower);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private Follower getFollowerFormJSONObject(JSONObject followerObject)
    {
        Follower follower = null;

        try {
            follower = new Follower();
            follower.setId(followerObject.getLong("id"));
            follower.setName(followerObject.getString("name"));
            follower.setTel(followerObject.getString("tel"));
            follower.setAddress(followerObject.getString("address"));
            follower.setIcon(followerObject.getString("icon"));
            follower.setSn(followerObject.getString("sn"));
            follower.setFollowTime(followerObject.getLong("followTime"));
            follower.setStatus(followerObject.getInt("status"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return follower;
    }

    /**
     * 店铺屏蔽用户
     * status 要改为的状态
     * 1 关注
     * 2 已屏蔽
     *
     */
    public boolean blockCustomer(long customerId, int status)
    {
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(customerId));
        params.put("status", Integer.toString(status));

        try
        {
            String resultString = URLConnectionUtil.post("store/blockCustomer", params);

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

}
