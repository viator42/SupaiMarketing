package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;
import im.supai.supaimarketing.R;

public class NewProductManuallyActivity extends AppCompatActivity {
    private EditText nameEdittext;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private ImageView imgView;
    private Bitmap img = null;
    private Button fromGalleryBtn;
    private Button fromCameraBtn;
    private Button addBtn;
    private Product product;
    protected String imgUrl = null;
    protected long storeId;
    private EditText countEditText;

    private TimeoutbleProgressDialog newProductDialog;

    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            //获取Message消息
//            if(msg.what == IMAGE_UPLOADED)
//            {
//                imgUrl = msg.obj.toString();
//            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_manually);

        newProductDialog = TimeoutbleProgressDialog.createProgressDialog(NewProductManuallyActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                newProductDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(NewProductManuallyActivity.this);
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

        //获取条形码
        AppContext context = (AppContext)getApplicationContext();
        storeId = context.getStore().getId();

        nameEdittext = (EditText)findViewById(R.id.name);
        descriptionEditText = (EditText)findViewById(R.id.description);
        priceEditText = (EditText)findViewById(R.id.price);
        imgView = (ImageView)findViewById(R.id.img);
        fromGalleryBtn = (Button)findViewById(R.id.from_gallery);
        fromGalleryBtn.setOnClickListener(new View.OnClickListener() {
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

        addBtn = (Button)findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alias = nameEdittext.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                int count = 0;
                try
                {
                    count = Integer.valueOf(countEditText.getText().toString());

                }catch (Exception e)
                {

                }

                if(alias.isEmpty() || alias == null)
                {
                    Toast.makeText(NewProductManuallyActivity.this, "商品名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(price.isEmpty() || price == null)
                {
                    Toast.makeText(NewProductManuallyActivity.this, "请填写价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(img == null)
                {
                    Toast.makeText(NewProductManuallyActivity.this, "请为商品添加图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                product = new Product();
                product.setAlias(alias);
                product.setAdditional(description);
                product.setPrice(Double.valueOf(price));
                product.setStoreId(storeId);
                product.setCount(count);

                new AddProductTask().execute(product);

            }
        });

        countEditText = (EditText) findViewById(R.id.count);


    }

    //商品添加任务
    public class AddProductTask extends AsyncTask<Product, Void, Product>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newProductDialog.show();
            addBtn.setEnabled(false);

        }

        @Override
        protected Product doInBackground(Product... product) {
            //上传图片
//            Image resultImage = new ImageAction().ImageUpload(img);

//            if(resultImage.isUploadSuccess())
//            {
//                product[0].setImgUrl(resultImage.getUrl()) ;
//                product[0].setImg(BitmapTools.Bitmap2Bytes(img));
//                return new ProductAction().addManually(product[0]);
//
//            }
//            else
//            {
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
                addBtn.setEnabled(true);
                Toast.makeText(NewProductManuallyActivity.this, "商品添加失败", Toast.LENGTH_SHORT).show();

            }


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticValues.IMAGE_UPLOAD && resultCode == RESULT_OK) {
            if(data != null)
            {
                ContentResolver resolver = getContentResolver();
                Uri originalUri = data.getData();
                try {
                    img = BitmapTools.zoomImg(MediaStore.Images.Media.getBitmap(resolver, originalUri));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (img != null)
                {
                    //获取Bitmap
                    imgView.setImageBitmap(img);

                }

            }
            else
            {
                Toast.makeText(NewProductManuallyActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == StaticValues.IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            img = (Bitmap)bundle.get("data");
            if (img != null)
            {
                //获取Bitmap
                imgView.setImageBitmap(img);

            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
