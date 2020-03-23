package im.supai.supaimarketing.action;

import android.graphics.Bitmap;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import im.supai.supaimarketing.util.URLConnectionUtil;

/**
 * Created by viator42 on 15/3/30.
 * 图片操作类
 */
public class ImageAction extends BaseAction
{
    /*
    //图片上传 返回url
    public Image ImageUpload(Bitmap bitmap)
    {
        Image image = new Image();
        image.setUploadSuccess(false);
        String url = null;

        try {
            String resultString = HttpClientUtil.uploadImage(BitmapTools.zoomImg(bitmap));
            if (resultString != null) {
                Log.v("supai resultString", resultString);
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    image.setUrl(jsonObject.getString("path"));
                    image.setUploadSuccess(jsonObject.getBoolean("success"));

                }
                image.setMsg(jsonObject.getString("msg"));

            }

        } catch (Exception e) {
            e.printStackTrace();
            image.setMsg("图片上传失败");

        }

        return image;
    }

     */

    /**
     * 查询网络图片
     * word: 关键词    count: 图片个数
     */
    public ArrayList<Bitmap> searchImage(String word, int count)
    {
        ArrayList<Bitmap> result = new ArrayList<Bitmap>();

        String resultString = null;

        HashMap params = new HashMap<String, Object>();
        params.put("tn", "baiduimagejson");
        params.put("word", word);
        params.put("rn", Integer.toString(count));
        params.put("pn", "1");
        params.put("ie", "utf-8");

        try
        {
            resultString = URLConnectionUtil.get("http://image.baidu.com/i", params);
            Log.v("supai resultString", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                String url = "";
                Bitmap bitmap = null;
                JSONArray data = jsonObject.getJSONArray("data");
                for(int a=0; a<data.length(); a++)
                {
                    JSONObject imgObj = data.getJSONObject(a);
                    url = imgObj.getString("objURL");
                    //获取图片
                    try
                    {
//                        byte[] byteArray = HttpClientUtil.getImageFromWeb(url);
//                        bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
//                                byteArray.length);
//                        if(bitmap != null)
//                        {
//                            result.add(bitmap);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }


}
