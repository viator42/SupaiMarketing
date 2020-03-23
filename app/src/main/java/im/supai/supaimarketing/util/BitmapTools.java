package im.supai.supaimarketing.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class BitmapTools {

    public BitmapTools() {
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param resources
     *            资源文件
     * @param resId
     *            解码位图的id
     * @param reqWith
     *            指定输出位图的宽度
     * @param reqHeight
     *            指定输出位图的高度
     * @return
     */
    public static Bitmap decodeBitmap(Resources resources, int resId,
                                      int reqWith, int reqHeight) {
        // 对位图进行解码的参数设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 在对位图进行解码的过程中，避免申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        // 对图片进行一定比例的压缩处理
        options.inSampleSize = calculateInSimpleSize(options, reqWith,
                reqHeight);
        options.inJustDecodeBounds = false;// 真正输出位图
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    public static Bitmap decodeBitmap(byte[] data, int reqWith, int reqHeight) {
        // 对位图进行解码的参数设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 在对位图进行解码的过程中，避免申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // 对图片进行一定比例的压缩处理
        options.inSampleSize = calculateInSimpleSize(options, reqWith,
                reqHeight);
        options.inJustDecodeBounds = false;// 真正输出位图
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     *
     * @param options
     * @param reqWith
     * @param reqHeight
     * @return
     */
    public static int calculateInSimpleSize(BitmapFactory.Options options,
                                            int reqWith, int reqHeight) {
        // 获得图片的原始宽高
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        int inSimpleSize = 1;// 压缩比例
        if (imageHeight > reqHeight || imageWidth > reqWith) {
            final int heightRatio = Math.round((float) imageHeight
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) imageWidth
                    / (float) reqWith);
            inSimpleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSimpleSize;
    }

    //根据url获取图片
    public static Bitmap getImage(String Url) throws Exception {

        try {

            URL url = new URL(Url);
            String responseCode = url.openConnection().getHeaderField(0);
            if (responseCode.indexOf("200") < 0)
                throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);
            return BitmapFactory.decodeStream(url.openStream());

        } catch (IOException e) {

            // TODO Auto-generated catch block

            throw new Exception(e.getMessage());

        }

    }

    //图片圆角化 第一个参数是Bitmap,第二个参数是圆角角度
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int roundPixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFFFFFFFF);
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 图片进行压缩处理,宽度固定,高度自动适配
     * @param bm
     *
     * @return
     */
    public static Bitmap zoomImg(Bitmap bm){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();

        if(width > 0 && height > 0)
        {
            if(width <= StaticValues.WIDTH_LIMIT)
            {
                //原图小于规定尺寸的不用缩放
                return bm;

            }
            else
            {
                int newWidth = StaticValues.WIDTH_LIMIT;
                int newHeight = (int)((newWidth * height) / width);
                // 计算缩放比例
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;

                // 取得想要缩放的matrix参数
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
                return newbm;
            }
        }
        else
        {
            return bm;

        }



    }

}
