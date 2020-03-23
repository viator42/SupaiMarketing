package im.supai.supaimarketing.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.Math;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Map;

/**
 * Created by viator42 on 15/5/5.
 *
 * 一般工具类
 */
public class CommonUtils {

    /// region 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
    /// <summary>
    /// 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
    /// </summary>
    /// <param name="lng1">经度1</param>
    /// <param name="lat1">纬度1</param>
    /// <param name="lng2">经度2</param>
    /// <param name="lat2">纬度2</param>
    /// <returns>返回距离（米）</returns>
    public static double getDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = lat1 * StaticValues.RAD;  // // RAD=π/180
        double radLat2 = lat2 * StaticValues.RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * StaticValues.RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * StaticValues.EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static int getSpinnerPosition(String code, ArrayList<Map<String, Object>> list)
    {
        int position = 0;

        for(int a=0; a<list.size(); a++)
        {
            Map item = list.get(a);
            if(code.equals(item.get("id")))
            {
                position = a;
            }
        }

        return position;
    }

    public static String getProvinceCode(String code)
    {
        return code.substring(0, 2)+"0000";

    }

    public static String getCityCode(String code)
    {
        return code.substring(0, 4)+"00";

    }

    //位置信息是否可用
    public static boolean isPositionAvalible(double longitude, double latitude)
    {
        if(longitude == 0 || latitude == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static String distanceFormat(double value) {
        /*
         * %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
         */
        return new Formatter().format("%.2f", value).toString();
    }

    // 时间戳转换为日期字符串
    public static String timestampToDate(long timestamp)
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(timestamp * 1000L);

    }

    // 时间戳转换为日期+时间字符串
    public static String timestampToDatetime(long timestamp)
    {
        return new SimpleDateFormat("yyyy-MM-dd \n HH:mm").format(timestamp * 1000L);

    }

    //字符串是否为空值
    public static boolean isValueEmpty(String str)
    {
        if(str == null || str.isEmpty() || str.equals("null"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //获取资源路径
    public static String getImgFullpath(String path)
    {
        return EnvValues.serverPath + path;

    }

    /**
     * 获取版本
     * @return 当前应用的版本
     * */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     * */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isNumber(String str)
    {
        return str.matches("[0-9]+");
    }

}
