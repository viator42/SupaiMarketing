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
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.action.RefAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

public class StoreEditActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText descriptionEditText;
    private ImageView iconImageView;
    private Button confirmBtn;
    private Store store;
    private Bitmap logo = null;
    private Switch activeSwitch;
    private AppContext context;
    private Button relocationBtn;
    private Button cancelBtn;
    private EditText storageWarningEditText;
    private TableLayout storageWarningLayout;

    private Uri imageUri = Uri.parse(StaticValues.IMAGE_FILE_LOCATION);//The Uri to store the big bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_edit);

        context = (AppContext)getApplicationContext();

        nameEditText = (EditText) findViewById(R.id.name);
        addressEditText = (EditText) findViewById(R.id.address);
        descriptionEditText = (EditText) findViewById(R.id.description);
        iconImageView = (ImageView) findViewById(R.id.icon);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更换头像
                AlertDialog.Builder builder = new AlertDialog.Builder(StoreEditActivity.this);
                builder.setMessage("选择图片的获取方式");
                builder.setTitle("提示");
                builder.setPositiveButton("来自相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //打开相册
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra("return-data", true);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        intent.putExtra("noFaceDetection", true); // no face detection
                        startActivityForResult(intent, StaticValues.IMAGE_UPLOAD);

                    }
                });
                builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //拍照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.putExtra("return-data", false);
                        startActivityForResult(intent, StaticValues.IMAGE_CAPTURE);

                    }
                });
                builder.create().show();
            }
        });

        activeSwitch = (Switch)findViewById(R.id.active);

        relocationBtn = (Button) findViewById(R.id.relocation);
        relocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreEditActivity.this, RelocationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("longitude", store.getLongitude());
                bundle.putDouble("latitude", store.getLatitude());
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.MAP_LOCATE);
            }
        });

        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store.setName(nameEditText.getText().toString().trim());
                store.setAddress(addressEditText.getText().toString().trim());
                store.setDescription(descriptionEditText.getText().toString().trim());

                if(storageWarningEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(StoreEditActivity.this, "缺货报警值不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                store.setStorageWarning(Integer.valueOf(storageWarningEditText.getText().toString().trim()));

                if (store.validate()) {
                    new StoreUpdateTask().execute(store);
                } else {
                    Toast.makeText(StoreEditActivity.this, store.getErrMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        storageWarningEditText = (EditText) findViewById(R.id.storage_warning);
        storageWarningLayout = (TableLayout) findViewById(R.id.storage_warning_layout);

        cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreEditActivity.this.finish();

            }
        });

        HashMap modules = context.getModules();
        if(modules == null || !modules.containsKey(StaticValues.MODULE_STATISTICS))
        {
            storageWarningLayout.setVisibility(View.GONE);
        }

        Bundle bundle = this.getIntent().getExtras();
        store = bundle.getParcelable("obj");

        setValues();

    }

    public void setValues()
    {
        nameEditText.setText(store.getName());
        addressEditText.setText(store.getAddress());
        if(!CommonUtils.isValueEmpty(store.getDescription()))
        {
            descriptionEditText.setText(store.getDescription());
        }

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_store_default) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_store_default) // resource or drawable
//                .showImageOnFail(R.drawable.ic_store_default) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(store.getLogoUrl()), iconImageView, options);

        activeSwitch.setChecked(store.getActiveStatus());
        activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                store.setActiveStatus(b);
            }
        });
        storageWarningEditText.setText(Integer.toString(store.getStorageWarning()));

    }

    //店铺更新
    public class StoreUpdateTask extends AsyncTask<Store, Void, Store>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Store doInBackground(Store... store) {
            //上传图片
            store[0].setLogoUrl(null);
            if(logo != null)
            {
//                Image resultImage = new ImageAction().ImageUpload(logo);
//
//                if(resultImage.isUploadSuccess())
//                {
//                    store[0].setLogoUrl(resultImage.getUrl());
//                    store[0].setLogo(BitmapTools.Bitmap2Bytes(logo));
//
//                }
//                else
//                {
//                    return null;
//
//                }
            }
            return new StoreAction().update(store[0]);


        }

        @Override
        protected void onPostExecute(Store result) {
            if(result != null)
            {
                context.setStore(result);
                new RefAction().setStore(StoreEditActivity.this, result);

                finish();

            }
            else
            {
                Toast.makeText(StoreEditActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticValues.IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            //拍照
            if(imageUri != null){
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", StaticValues.ICON_WIDTH);
                intent.putExtra("outputY", StaticValues.ICON_HEIGHT);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra("return-data", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true); // no face detection
                startActivityForResult(intent, StaticValues.IMAGE_CROP);

            }
            else
            {
                Toast.makeText(StoreEditActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == StaticValues.IMAGE_UPLOAD && resultCode == RESULT_OK) {
            //相册
            if(data != null)
            {
                Bitmap img = null;
                ContentResolver resolver = getContentResolver();
                Uri originalUri = data.getData();
                try {
                    img = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (img != null)
                {
                    //获取Bitmap
                    logo = img;
                    iconImageView.setImageBitmap(img);

                }

            }
            else
            {
                Toast.makeText(StoreEditActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == StaticValues.IMAGE_CROP && resultCode == RESULT_OK)
        {
            //裁剪
            if(imageUri != null){
                logo = decodeUriAsBitmap(imageUri);//decode bitmap
                iconImageView.setImageBitmap(logo);
            }
            else
            {
                Toast.makeText(StoreEditActivity.this, "图片裁剪失败", Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode == StaticValues.MAP_LOCATE)
        {
            Bundle bundle = data.getExtras();
            store.setLongitude(bundle.getDouble("longitude"));
            store.setLatitude(bundle.getDouble("latitude"));

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //退出确认
            AlertDialog.Builder builder = new AlertDialog.Builder(StoreEditActivity.this);
            builder.setMessage("是否放弃修改并退出");
            builder.setTitle("提示");
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StoreEditActivity.this.finish();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();

            return true;
        }

        return false;
    }

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
