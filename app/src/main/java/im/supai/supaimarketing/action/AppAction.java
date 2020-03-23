package im.supai.supaimarketing.action;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.base.BaseAction;
import im.supai.supaimarketing.model.AppInfo;
import im.supai.supaimarketing.model.Collection;
import im.supai.supaimarketing.model.Orders;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.util.URLConnectionUtil;

public class AppAction extends BaseAction {
    //返回版本信息
    public AppInfo getInfo()
    {
        AppInfo result = new AppInfo();

        HashMap params = new HashMap<String, Object>();

        try
        {
            String resultString = URLConnectionUtil.post("app/info", params);

            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                result.setVersionCode(jsonObject.getInt("versionCode"));
                result.setVersionName(jsonObject.getString("versionName"));

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

        return result;

    }

    //返回网页内容
    public String getWebContent(String url)
    {
        String resultString = null;
        List params = new ArrayList();

        try
        {
//            resultString = HttpClientUtil.post("app/info", params);
//            Log.v("supai resultString", resultString);


        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

        return resultString;

    }

    //应用初始化获取网络数据
    public boolean appDataInit(Activity activity, long userid)
    {
        boolean result = true;
        AppContext appContext = (AppContext) activity.getApplicationContext();

        //获取用户信息
        User user = new UserAction().loadSettings(userid);
        if(user != null)
        {
//            appContext.setNetworkStatus(StaticValues.NETWORK_STATUS_OKAY);

            //获取用户收藏任务
            Collection collection = new CollectAction().all(activity, userid, true);
            appContext.setCollection(collection);

            //获取用户店铺信息
            SharedPreferences storeRef = activity.getSharedPreferences("store", Context.MODE_PRIVATE);
            long storeId = storeRef.getLong("id", 0);
            if(storeId != 0)
            {
                Store store = new Store();

                store.setId(storeId);
                store.setName(storeRef.getString("name", ""));
                store.setArea(storeRef.getString("area", ""));
                store.setAddress(storeRef.getString("address", ""));
                store.setLogoUrl(storeRef.getString("logo", ""));
                store.setDescription(storeRef.getString("description", ""));
                store.setLongitude(storeRef.getFloat("longitude", 0));
                store.setLatitude(storeRef.getFloat("latitude", 0));
                store.setActiveStatus(storeRef.getBoolean("activeStatus", true));
                store.setStatus(storeRef.getInt("status", 1));
                store.setStorageWarning(storeRef.getInt("storage_warning", 0));

                appContext.setStore(store);
            }
            else
            {
                //获取用户商铺信息
                Store store = new StoreAction().getInfo(userid);
                if(store != null)
                {
                    appContext.setStore(store);
                    //店铺信息保存到ref
                    new RefAction().setStore(activity, store);

                }
            }

            //获取用户的收费模块信息
            appContext.setModules(new ModuleAction().list(userid));

            //获取用户订单信息
            Orders orders = new OrderAction().activeOrders(activity, userid, 1, 0, 0, false);
            appContext.setOrders(orders);

            //打印机服务初始化
            HashMap modules = appContext.getModules();
            if(modules != null && modules.containsKey(StaticValues.MODULE_PRINTER))
            {
//                appContext.startPrinterService();
//                appContext.connection();

//                context.initPortParam();
//                PortParamDataBase database = new PortParamDataBase(SplashActivity.this);
//                PortParameters mPortParam = database.queryPortParamDataBase("0");

//                try {
//                    context.getmGpService().openPort(0, mPortParam.getPortType(), mPortParam.getBluetoothAddr(), 0);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

            }

            //获取展示广告
            appContext.setAdvertisements(new AdvertisementAction().all());

        }
        else
        {
            //无网络连接
            appContext.setNetworkStatus(StaticValues.NETWORK_STATUS_ERROR);

        }

        return result;
    }

}
