package im.supai.supaimarketing.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * URLConnection 网络连接类
 *
 */
public class URLConnectionUtil
{
    public static String post(String path, Map<String, Object> params) throws Exception
    {
        String result;
        Log.v("params", path + " ---- " + params.toString());
        URL url = new URL(path);

        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        StringBuffer responseResult = null;
        StringBuffer paramsStringBuffer = new StringBuffer();

        // 组织请求参数
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            paramsStringBuffer.append(element.getKey());
            paramsStringBuffer.append("=");
            paramsStringBuffer.append(URLEncoder.encode((String) element.getValue(), "UTF-8"));
            paramsStringBuffer.append("&");
        }

        String paramsString = paramsStringBuffer.toString();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // 设置通用的请求属性
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setRequestProperty("Content-Length", String.valueOf(paramsString.length()));
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("contentType", "utf-8");

        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);

        // 获取URLConnection对象对应的输出流
        printWriter = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        printWriter.write(paramsString);

        // flush输出流的缓冲
        printWriter.flush();
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            // 定义BufferedReader输入流来读取URL的ResponseData
            bufferedReader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            responseResult = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                responseResult.append(line);
            }

        }

        if (printWriter != null) {
            printWriter.close();
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        conn.disconnect();

        if(responseResult != null)
        {
            Log.v("resultString", path + " ---- " + responseResult.toString());
            result = responseResult.toString();
            return result;
        }
        else
        {
            return null;
        }
    }

    /**
     * 获取网络图片
     *
     * @param urlString
     *            如：http://f.hiphotos.baidu.com/image/w%3D2048/sign=3
     *            b06d28fc91349547e1eef6462769358
     *            /d000baa1cd11728b22c9e62ccafcc3cec2fd2cd3.jpg
     * @return
     * @date 2014.05.10
     */
    public static Bitmap getNetWorkBitmap(String urlString) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String get(String path, Map<String, Object> params) throws Exception
    {
        return null;
    }

}
