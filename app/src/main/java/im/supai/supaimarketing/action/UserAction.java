package im.supai.supaimarketing.action;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;
import im.supai.supaimarketing.model.Appeal;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/3/25.
 * 用户相关的业务操作
 *
 */
public class UserAction extends BaseAction
{
    /**
     * 登录
     */
    public User Login(final User user)
    {
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("tel", user.getTel());
        params.put("password", user.getPassword());

        try
        {
            resultString = URLConnectionUtil.post("user/login", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                user.setErrCode(jsonObject.getInt("errorCode"));
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    user.setLoginSuccess(true);

                    user.setId(jsonObject.getInt("id"));
                    user.setName(jsonObject.getString("name"));
                    user.setUsername(jsonObject.getString("username"));
                    user.setTel(jsonObject.getString("tel"));
                    user.setArea(jsonObject.getString("area"));
                    user.setAddress(jsonObject.getString("address"));
                    user.setIconUrl(jsonObject.getString("icon"));
                    user.setSn(jsonObject.getString("sn"));
                    user.setPasstype(jsonObject.getInt("passtype"));
                    user.setClerkOf(jsonObject.getLong("clerk_of"));
                    user.setStatus(jsonObject.getInt("status"));
                    user.setPrintCopy(jsonObject.getInt("print_copy"));

                }
                else
                {
                    user.setLoginSuccess(false);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 验证密码
     */
    public User passwordValidate(long userid, String password)
    {
        String resultString = null;
        User user = new User();

        HashMap params = new HashMap<String, Object>();
        params.put("tel", Long.toString(userid));
        params.put("password", password);

        try
        {
            resultString = URLConnectionUtil.post("user/validatePassword", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                user.setErrCode(jsonObject.getInt("errorCode"));
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    user.setLoginSuccess(true);

                    user.setId(jsonObject.getInt("id"));
                    user.setName(jsonObject.getString("name"));
                    user.setUsername(jsonObject.getString("username"));
                    user.setTel(jsonObject.getString("tel"));
                    user.setArea(jsonObject.getString("area"));
                    user.setAddress(jsonObject.getString("address"));
                    user.setIconUrl(jsonObject.getString("icon"));
                    user.setSn(jsonObject.getString("sn"));
                    user.setPasstype(jsonObject.getInt("passtype"));
                    user.setClerkOf(jsonObject.getLong("clerk_of"));
                    user.setStatus(jsonObject.getInt("status"));
                    user.setPrintCopy(jsonObject.getInt("print_copy"));

                }
                else
                {
                    user.setLoginSuccess(false);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

    }

    /**
     * 注册
     */
    public User Register(User user)
    {
        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("tel", user.getTel());
        params.put("name", user.getName());
        params.put("password", user.getPassword());
        params.put("address", user.getAddress());
        params.put("area", user.getArea());

        try
        {
            resultString = URLConnectionUtil.post("user/register", params);

            if(resultString != null)
            {
                Log.v("supai result", resultString);
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    user.setLoginSuccess(true);

                    user.setId(jsonObject.getInt("id"));
                    user.setName(jsonObject.getString("name"));
                    user.setUsername(jsonObject.getString("username"));
                    user.setTel(jsonObject.getString("tel"));
                    user.setArea(jsonObject.getString("area"));
                    user.setAddress(jsonObject.getString("address"));
                    user.setIconUrl(jsonObject.getString("icon"));
//                    user.setIcon(HttpClientUtil.getImageFromWeb(jsonObject.getString("icon")));
                    user.setSn(jsonObject.getString("sn"));
                    user.setPasstype(jsonObject.getInt("passtype"));
                    user.setClerkOf(jsonObject.getLong("clerk_of"));
                    user.setStatus(jsonObject.getInt("status"));
                    user.setPrintCopy(jsonObject.getInt("print_copy"));

                }
                else
                {
                    user.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

    }

    //登出
    public void logout(final Activity activity)
    {
        //删除ref
        SharedPreferences ref = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        ref.edit().clear().commit();

        ref = activity.getSharedPreferences("store", Context.MODE_PRIVATE);
        ref.edit().clear().commit();

        //跳转到登录页
//        Intent intent = new Intent();
//        intent.setClass(activity, LoginActivity.class);
//        activity.startActivity(intent);
//        activity.finish();

    }

    /**
     * 读取用户设置
     */
    public User loadSettings(long userid)
    {
        String resultString = null;
        User user = null;
        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));

        try
        {
            resultString = URLConnectionUtil.post("user/loadSettings", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    user = new User();

                    user.setId(jsonObject.getLong("id"));
                    user.setUsername(jsonObject.getString("username"));
                    user.setTel(jsonObject.getString("tel"));
                    user.setIconUrl(jsonObject.getString("icon"));
                    user.setName(jsonObject.getString("name"));
                    user.setAddress(jsonObject.getString("address"));
                    user.setArea(jsonObject.getString("area"));
                    user.setSn(jsonObject.getString("sn"));
                    user.setPasstype(jsonObject.getInt("passtype"));
                    user.setPassword(jsonObject.getString("password"));
                    user.setClerkOf(jsonObject.getLong("clerk_of"));
                    user.setStatus(jsonObject.getInt("status"));
                    user.setPrintCopy(jsonObject.getInt("print_copy"));

                    user.setLoginSuccess(true);

//                    user.setIcon(HttpClientUtil.getImageFromWeb(jsonObject.getString("icon")));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return user;

    }

    /**
     * 更新设置信息
     */
    public User update(User user)
    {
        HashMap params = new HashMap<String, Object>();
        params.put("id", Long.toString(user.getId()));
        params.put("name", user.getName());
        params.put("address", user.getAddress());
        params.put("icon", user.getIconUrl());
        params.put("area", user.getArea());
        params.put("password", user.getPassword());
        params.put("passtype", Integer.toString(user.getPasstype()));
        params.put("print_copy", Integer.toString(user.getPrintCopy()));

        try
        {
            String resultString = URLConnectionUtil.post("user/update", params);

            JSONObject data = new JSONObject(resultString);

            if(data.getBoolean("success"))
            {
                return user;

            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 上传位置
     */
    public boolean uploadLocation(long userid, double longitude, double latitude)
    {
        String resultString = null;
        boolean result = false;

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("longitude", Double.toString(longitude));
        params.put("latitude", Double.toString(latitude));

        try
        {
            resultString = URLConnectionUtil.post("user/uploadLocation", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    result = true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }

    /**
     * 获取省市信息
     */
    public String getCodeAddress(String code)
    {
        String resultString = null;
        String result = "";

        HashMap params = new HashMap<String, Object>();
        params.put("code", code);

        try
        {
            resultString = URLConnectionUtil.post("user/address", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                //赋值
                result = jsonObject.getString("data");

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }

    /**
     * 删除用户账户
     */
    public boolean destroy(long userid)
    {
        boolean result  = false;

        String resultString = "";

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));

        try
        {
            resultString = URLConnectionUtil.post("user/destroy", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                //赋值
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

    /**
     * 提交反馈意见
     */
    public boolean ref(long userid, String content)
    {
        boolean result  = false;

        String resultString = "";

        HashMap params = new HashMap<String, Object>();
        params.put("userid", Long.toString(userid));
        params.put("content", content);

        try
        {
            resultString = URLConnectionUtil.post("user/ref", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                //赋值
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

    /**
     * 申请
     */
    public boolean appeal(Appeal appeal)
    {
        boolean result  = false;

        String resultString = "";

        HashMap params = new HashMap<String, Object>();
        params.put("oldTel", appeal.getOldTel());
        params.put("newTel", appeal.getNewTel());
        params.put("name", appeal.getName());
        params.put("address", appeal.getAddress());
        params.put("imie", appeal.getImie());

        try
        {
            resultString = URLConnectionUtil.post("user/appeal", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                //赋值
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

    /**
     * 根据手机号查找用户
     */
    public User findByTel(String tel)
    {
        User result = null;

        String resultString = "";

        HashMap params = new HashMap<String, Object>();
        params.put("tel", tel);

        try
        {
            resultString = URLConnectionUtil.post("user/findByTel", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                //赋值
                if(jsonObject.getBoolean("success"))
                {
                    result = new User();
                    result.setId(jsonObject.getLong("id"));
                    result.setName(jsonObject.getString("name"));
                    result.setUsername(jsonObject.getString("username"));
                    result.setTel(jsonObject.getString("tel"));
                    result.setIconUrl(jsonObject.getString("icon"));
                    result.setSn(jsonObject.getString("sn"));
                    result.setPasstype(jsonObject.getInt("passtype"));
                    result.setClerkOf(jsonObject.getLong("clerk_of"));
                    result.setStatus(jsonObject.getInt("status"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;

    }

    /**
     * 储存用户信息
     */
    private void saveUserInfo(User user)
    {

    }

}
