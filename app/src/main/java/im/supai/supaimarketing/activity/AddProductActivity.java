package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.action.RccodeAction;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Rccode;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

import static im.supai.supaimarketing.util.StaticValues.IMAGE_UPLOADED;

public class AddProductActivity extends AppCompatActivity {
    private TextView rccodeEditText;
    private TimeoutbleProgressDialog getProductInfoDialog;
    private LinearLayout goodsInfoLayout;
    private Button searchImgBtn;
    private ArrayList<Bitmap> imgArray;
    private ImageView productImgView;
    private Button changeImg;
    private EditText priceEditText;
    private Button addProductBtn;

    private CharSequence rccode;
    private String goodsName;
    private String merchant;
    private String origin;
    private String priceInterval;
    private String merchantCode;

    private EditText aliasEditText;
    private TextView goodsMerchant;
    private TextView goodsOrigin;
    private TextView goodsPriceInterval;
    private LinearLayout imageContainer;
    private Button changeImgBtn;
    private Button fromAlbumBtn;
    private Button fromCameraBtn;
    private EditText descriptionEditText;
    private EditText countEditText;

    private Bitmap imgSelected;
    private ImageView imgView;

    private long storeId;

    protected String imgUrl;
    protected ArrayList<Bitmap> imageList;  //搜索图片列表

    private boolean createPrepared = false; //获取成功

    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            //获取Message消息
            if(msg.what == IMAGE_UPLOADED)
            {
                imgUrl = msg.obj.toString();
            }

        }
    };

    private TimeoutbleProgressDialog newProductDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getProductInfoDialog = TimeoutbleProgressDialog.createProgressDialog(AddProductActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getProductInfoDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setMessage("未获取到商品信息");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });

        newProductDialog = TimeoutbleProgressDialog.createProgressDialog(AddProductActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                newProductDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setMessage("添加失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                builder.create().show();
            }
        });

        rccodeEditText = (TextView)findViewById(R.id.rccode);
        /*
        productImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                productImgView.setImageBitmap(imgArray.get(random.nextInt(imgArray.size() - 1)));

            }
        });
        */

        aliasEditText = (EditText)findViewById(R.id.alias);
        goodsMerchant = (TextView)findViewById(R.id.merchant);
        goodsOrigin = (TextView)findViewById(R.id.origin);
        goodsPriceInterval = (TextView)findViewById(R.id.price_interval);
        imageContainer = (LinearLayout)findViewById(R.id.img_container);
        descriptionEditText = (EditText)findViewById(R.id.description);
        countEditText = (EditText) findViewById(R.id.count);
        imgView = (ImageView)findViewById(R.id.img);

        //获取条形码
        AppContext context = (AppContext)getApplicationContext();
        rccode = context.getBarcode();
        rccodeEditText.setText(rccode);
        storeId = context.getStore().getId();

        //查询条形码
        new RccodeSearchTask().execute(context.getBarcode().toString());

        //更换图片
        changeImgBtn = (Button)findViewById(R.id.change_img);
        changeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageList != null && !imageList.isEmpty())
                {
                    imgSelected = imageList.get((int) (Math.random() * imageList.size()));
                    imgView.setImageBitmap(imgSelected);

                }

            }
        });

        //选择相册
        fromAlbumBtn = (Button)findViewById(R.id.from_album);
        fromAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开相册
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data", true);

                startActivityForResult(intent, StaticValues.IMAGE_UPLOAD);
            }
        });

        fromCameraBtn = (Button)findViewById(R.id.from_camera);
        fromCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, StaticValues.IMAGE_CAPTURE);

            }
        });

        priceEditText = (EditText)findViewById(R.id.price);
        addProductBtn = (Button)findViewById(R.id.add_product);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加商品
                String price = priceEditText.getText().toString().trim();
                String alias = aliasEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                int count = 0;
                try
                {
                    count = Integer.valueOf(countEditText.getText().toString());

                }catch (Exception e)
                {

                }

                if(alias.isEmpty() || alias == null)
                {
                    Toast.makeText(AddProductActivity.this, "商品名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(price.isEmpty() || price == null)
                {
                    Toast.makeText(AddProductActivity.this, "请填写价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imgSelected == null)
                {
                    Toast.makeText(AddProductActivity.this, "必须选择一张图片", Toast.LENGTH_SHORT).show();
                    return;

                }

                //为属性设置默认值


                //添加商品
                Product product = new Product();
                product.setBarcode(rccode.toString());

                product.setAlias(alias);
                product.setPrice(Double.valueOf(price));
                product.setStoreId(storeId);
                product.setImgUrl(imgUrl);
                product.setAdditional(description);
                product.setCount(count);

                if(goodsName == null || goodsName.isEmpty() )
                {
                    product.setName("未知");

                }
                else
                {
                    product.setName(goodsName);
                }

                if(merchant == null || merchant.isEmpty() )
                {
                    product.setMerchant("未知");

                }
                else
                {
                    product.setMerchant(merchant);
                }

                if(origin == null || origin.isEmpty() )
                {
                    product.setOrigin("未知");

                }
                else
                {
                    product.setOrigin(origin);
                }

                if(priceInterval == null || priceInterval.isEmpty() )
                {
                    product.setPriceInterval("未知");

                }
                else
                {
                    product.setPriceInterval(priceInterval);
                }

                if(product.updateValidation())
                {
                    new AddProductTask().execute(product);
                }
                else
                {
                    Toast.makeText(AddProductActivity.this, product.getValidationMsg(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    //条码查询任务
    public class RccodeSearchTask extends AsyncTask<String, Void, Rccode>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getProductInfoDialog.show();
        }

        @Override
        protected Rccode doInBackground(String... barcode) {
            return new RccodeAction().search(barcode[0], storeId);

        }

        @Override
        protected void onPostExecute(Rccode result) {
            super.onPostExecute(result);

            getProductInfoDialog.dismiss();

            if(result!= null) {
                //更新UI
                Log.v("supai resultString", result.toString());

                aliasEditText.setText(result.getName());
                goodsName = result.getName();

                goodsMerchant.setText(result.getMerchant());
                merchant = result.getMerchant();

                goodsOrigin.setText(result.getOrigin());
                origin = result.getOrigin();

                goodsPriceInterval.setText(result.getPriceInterval());
                priceInterval = result.getPriceInterval();

                merchantCode = result.getMerchantCode();

                //设置图片
                imageList = result.getImages();

                if(imageList != null && imageList.size() > 0)
                {
                    imgSelected = imageList.get(0);
                    imgView.setImageBitmap(imgSelected);

                    changeImgBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    changeImgBtn.setVisibility(View.GONE);

                }

                //商品已存在的情况
                if(result.isExist())
                {
//                    new ProductExistDialog(AddProductActivity.this, result.getProduct()).show();
                }

            }
            else
            {
                Toast.makeText(AddProductActivity.this, "获取商品信息失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //图片搜索任务
    /*
    public class ImageSearchTask extends AsyncTask<String, Void, ArrayList<Bitmap>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(String... word) {
            return new ImageAction().searchImage(word[0], 3);

        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> result) {
            super.onPostExecute(result);
            //
            imgArray = result;
            productImgView.setImageBitmap(result.get(0));

            img = result.get(0);
        }
    }
    */

    //商品添加任务
    public class AddProductTask extends AsyncTask<Product, Void, Product>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newProductDialog.show();
            addProductBtn.setEnabled(false);

        }

        @Override
        protected Product doInBackground(Product... product) {
            //上传图片
//            Image resultImage = new ImageAction().ImageUpload(imgSelected);
//
//            if(resultImage.isUploadSuccess())
//            {
//                product[0].setImgUrl(resultImage.getUrl()) ;
//                product[0].setImg(BitmapTools.Bitmap2Bytes(imgSelected));
//                return new ProductAction().add(product[0]);
//
//            }
//            else
//            {
//                Toast.makeText(AddProductActivity.this, resultImage.getMsg(), Toast.LENGTH_SHORT).show();
//                return null;
//
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Product result) {
            super.onPostExecute(result);
            newProductDialog.dismiss();

            if(result != null)
            {
                finish();
            }
            else
            {
                addProductBtn.setEnabled(true);
                Toast.makeText(AddProductActivity.this, "商品添加失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //图片上传
    public class ImgUploadThread implements Runnable
    {
        private Bitmap bitmap;
        public ImgUploadThread(Bitmap bitmap)
        {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
//            Image img = new ImageAction().ImageUpload(bitmap);
//            Message message = Message.obtain();
//            message.obj = img.getUrl();
//            message.what = IMAGE_UPLOADED;
//            //发送Message消息
//            handler.sendMessage(message);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticValues.IMAGE_UPLOAD && resultCode == RESULT_OK) {
            if(data != null)
            {
//                ContentResolver resolver = getContentResolver();
//                Uri originalUri = data.getData();
//                try {
//                    imgSelected = BitmapTools.zoomImg(MediaStore.Images.Media.getBitmap(resolver, originalUri));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (imgSelected != null)
//                {
//                    //获取Bitmap
//                    imgView.setImageBitmap(imgSelected);
//
//                }

            }
            else
            {
                Toast.makeText(AddProductActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == StaticValues.IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            imgSelected = (Bitmap)bundle.get("data");
            if (imgSelected != null)
            {
                //获取Bitmap
                imgView.setImageBitmap(imgSelected);

            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    //查询商品是否存在
//    public class CheckProductExistTask extends AsyncTask<String, Void, Product>
//    {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            newProductDialog.show();
//            addProductBtn.setEnabled(false);
//
//        }
//
//        @Override
//        protected Product doInBackground(String... barcode) {
//            new ProductAction().checkBarcodeExist(barcode[0], storeId);
//
//        }
//
//        @Override
//        protected void onPostExecute(Product result) {
//            super.onPostExecute(result);
//            newProductDialog.dismiss();
//
//
//
//        }
//    }

}
