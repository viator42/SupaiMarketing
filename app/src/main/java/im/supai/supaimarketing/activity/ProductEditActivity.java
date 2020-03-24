package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.R;

public class ProductEditActivity extends AppCompatActivity {
    private ImageView imgView;
    private Bitmap img;
    private EditText aliasEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Switch activeSwitch;
    private Button confirmBtn;
    private Button deleteBtn;
    private EditText countEditText;
    private AppContext context;

    protected Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        context = (AppContext) getApplicationContext();

        imgView = (ImageView)findViewById(R.id.img);
        aliasEditText = (EditText)findViewById(R.id.alias);
        descriptionEditText = (EditText)findViewById(R.id.description);
        priceEditText = (EditText)findViewById(R.id.price);
        confirmBtn = (Button)findViewById(R.id.confirm);
        activeSwitch = (Switch)findViewById(R.id.active);
        deleteBtn = (Button)findViewById(R.id.delete);
        countEditText = (EditText) findViewById(R.id.count);

        Bundle bundle = this.getIntent().getExtras();
        product = bundle.getParcelable("obj");

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_product) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_product) // resource or drawable
//                .showImageOnFail(R.drawable.ic_product) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(product.getImgUrl()), imgView, options);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductEditActivity.this);
                builder.setMessage("选择图片的获取方式");
                builder.setTitle("提示");
                builder.setPositiveButton("来自相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //打开相册
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, StaticValues.IMAGE_UPLOAD);
                    }
                });
                builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //拍照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, StaticValues.IMAGE_CAPTURE);
                    }
                });
                builder.create().show();
            }
        });

        aliasEditText.setText(product.getAlias());
        if(!CommonUtils.isValueEmpty(product.getAdditional()))
        {
            descriptionEditText.setText(product.getAdditional());
        }

        priceEditText.setText(Double.toString(product.getPrice()));
        countEditText.setText(Integer.toString(product.getCount()));

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setAdditional(descriptionEditText.getText().toString().trim());
                product.setPrice(Double.valueOf(priceEditText.getText().toString()));
                product.setAlias(aliasEditText.getText().toString().trim());
                int count = 0;
                try {
                    count = Integer.valueOf(countEditText.getText().toString());

                } catch (Exception e) {

                }
                product.setCount(count);

                countEditText.getText().toString();

                if (product.updateValidation()) {
                    new ProductUpdateTask().execute(product);
                } else {
                    Toast.makeText(ProductEditActivity.this, product.getValidationMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        activeSwitch.setChecked(product.getActiveStatus());
        activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                product.setActiveStatus(b);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductEditActivity.this);
                builder.setMessage("是否删除此商品？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new ProductDeleteTask().execute(product.getId());
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();


            }
        });

    }

    //商品更新
    public class ProductUpdateTask extends AsyncTask<Product, Void, Product>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Product doInBackground(Product... product) {
            //上传图片
//            product[0].setImgUrl(null);
            if(img != null)
            {
//                Image resultImage = new ImageAction().ImageUpload(img);
//
//                if(resultImage.isUploadSuccess())
//                {
//                    product[0].setImgUrl(resultImage.getUrl());
//                    product[0].setImg(BitmapTools.Bitmap2Bytes(img));
//
//                }
//                else
//                {
//                    return null;
//                }

            }
            return new ProductAction().update(ProductEditActivity.this, product[0]);


        }

        @Override
        protected void onPostExecute(Product result) {
            super.onPostExecute(result);
            if(result == null)
            {
                Toast.makeText(ProductEditActivity.this, "更新失败", Toast.LENGTH_SHORT).show();

            }
            else
            {
                //跳转到商品页面
                Intent intent = new Intent(ProductEditActivity.this, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", result);
                intent.putExtras(bundle);
                setResult(StaticValues.PRODUCT_EDIT, intent);
                finish();
            }

        }
    }

    //商品删除
    public class ProductDeleteTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new ProductAction().delete(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result)
            {
                //Toast.makeText(ProductEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProductEditActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
            else
            {
                Toast.makeText(ProductEditActivity.this, "删除失败", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(ProductEditActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
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

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出确认
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductEditActivity.this);
            builder.setMessage("是否放弃修改并退出");
            builder.setTitle("提示");
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ProductEditActivity.this.finish();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();

        }

        return true;
    }
}
