package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import android.net.Uri;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.AreaAction;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;
import im.supai.supaimarketing.R;

public class NewStoreActivity extends AppCompatActivity {
    private EditText nameTextEdit;
    private ImageView iconImageBtn;
    private EditText descriptionTextEdit;
    private EditText addressTextEdit;
    private Button confirmBtn;
    private Activity activity;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private AppContext context;
    private Button relocationBtn;

    private static String areaCode;

    protected String logoUrl;
    protected Bitmap logoBitmap;

    //private File sdcardTempFile = null;
    private Uri imageUri = Uri.parse(StaticValues.IMAGE_FILE_LOCATION);//The Uri to store the big bitmap

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            //获取Message消息
            if(msg.what == StaticValues.IMAGE_UPLOADED)
            {
                logoUrl = msg.obj.toString();
            }

        }
    };

    private double longitude;
    private double latitude;
    private User user;

    private TimeoutbleProgressDialog newStoreDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_store);
        activity = this;

        context = (AppContext)NewStoreActivity.this.getApplicationContext();

        newStoreDialog = TimeoutbleProgressDialog.createProgressDialog(NewStoreActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                newStoreDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(NewStoreActivity.this);
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

        //获取userid
        user = context.getUser();

        longitude = context.getLongitude();
        latitude = context.getLatitude();

        nameTextEdit = (EditText)findViewById(R.id.alias);
        iconImageBtn = (ImageView)findViewById(R.id.icon);
        iconImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewStoreActivity.this);
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

        descriptionTextEdit = (EditText)findViewById(R.id.description);
        addressTextEdit = (EditText)findViewById(R.id.address);
        addressTextEdit.setText(context.getUser().getAddress());

        relocationBtn = (Button) findViewById(R.id.relocation);
        relocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewStoreActivity.this, RelocationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("longitude", longitude);
                bundle.putDouble("latitude", latitude);
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.MAP_LOCATE);

            }
        });

        confirmBtn = (Button)findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude == 0 || latitude == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewStoreActivity.this);
                    builder.setMessage("未获取到位置信息，请手动指定您的店铺的位置。");
                    builder.setTitle("提示");
                    builder.setPositiveButton("手动指定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(NewStoreActivity.this, RelocationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putDouble("longitude", longitude);
                            bundle.putDouble("latitude", latitude);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, StaticValues.MAP_LOCATE);
                        }
                    });
                    builder.create().show();

                    return;
                }

                //提交表单
                Store store = new Store();
                store.setName(nameTextEdit.getText().toString().trim());
                store.setAddress(addressTextEdit.getText().toString().trim());
                store.setDescription(descriptionTextEdit.getText().toString().trim());
                store.setLogoUrl(logoUrl);
                store.setUserid(user.getId());
                store.setArea(areaCode);
                store.setLongitude(longitude);
                store.setLatitude(latitude);

                //校验
                if(store.validate())
                {
                    //提交
                    new NewStoreTask().execute(store);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), store.getErrMsg(),  Toast.LENGTH_SHORT).show();

                }

            }
        });

        provinceSpinner = (Spinner)findViewById(R.id.province);
        citySpinner = (Spinner)findViewById(R.id.city);

        new GetProivinceTask().execute();

        //获取当前位置
        locateManually();

    }

    //获取Logo图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                Toast.makeText(NewStoreActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
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
                    this.logoBitmap = img;
                    iconImageBtn.setImageBitmap(img);

                }

            }
            else
            {
                Toast.makeText(NewStoreActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == StaticValues.IMAGE_CROP && resultCode == RESULT_OK)
        {
            //裁剪
            if(imageUri != null){
                logoBitmap = decodeUriAsBitmap(imageUri);//decode bitmap
                iconImageBtn.setImageBitmap(logoBitmap);
            }
            else
            {
                Toast.makeText(NewStoreActivity.this, "图片裁剪失败", Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode == StaticValues.MAP_LOCATE)
        {
            Bundle bundle = data.getExtras();
            longitude = bundle.getDouble("longitude");
            latitude = bundle.getDouble("latitude");

            Log.v("supai position selected", Double.toString(longitude) + "  " + Double.toString(latitude));

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //新建店铺任务
    public class NewStoreTask extends AsyncTask<Store, Void, Store>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newStoreDialog.show();
        }

        @Override
        protected Store doInBackground(Store... store) {
            //上传图片
            if(logoBitmap != null)
            {
//                Image resultImage = new ImageAction().ImageUpload(logoBitmap);
//                if(resultImage.isUploadSuccess())
//                {
//                    store[0].setLogoUrl(resultImage.getUrl());
//                    store[0].setLogo(BitmapTools.Bitmap2Bytes(logoBitmap));
//
//                }
//                else
//                {
//                    return null;
//
//                }

            }
            return new StoreAction().newStore(store[0]);

        }

        @Override
        protected void onPostExecute(Store result) {
            super.onPostExecute(result);
            newStoreDialog.dismiss();
            if(result != null)
            {
                //更新UI
                context.setStore(result);
                //店铺信息存入ref
                SharedPreferences storeRef = getSharedPreferences("store", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = storeRef.edit();
                editor.putLong("id", result.getId());
                editor.putString("name", result.getName());
                editor.putString("area", result.getArea());
                editor.putString("address", result.getAddress());
                editor.putString("logo", result.getLogoUrl());
                editor.putString("description", result.getDescription());
                editor.putFloat("longitude", (float)result.getLongitude());
                editor.putFloat("longitude", (float) result.getLatitude());

                editor.commit();

                //跳转
                Intent intent = new Intent();
                intent.setClass(NewStoreActivity.this, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();

            }
            else
            {
                Toast.makeText(NewStoreActivity.this, "新建店铺失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //获取省列表事件
    public class GetProivinceTask extends AsyncTask<Void, Void, ArrayList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(Void... id) {
            return new AreaAction().getChildren(0);

        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);

            SimpleAdapter adapter = new SimpleAdapter(NewStoreActivity.this, result, R.layout.spinner_item, new String[] {"name"}, new int[] {R.id.alias});
            provinceSpinner.setAdapter(adapter);
            provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String code = ((Map) provinceSpinner.getItemAtPosition(i)).get("id").toString();
                    new GetCityTask().execute(new Integer(code));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            provinceSpinner.setSelection(CommonUtils.getSpinnerPosition(CommonUtils.getProvinceCode(user.getArea()), result), true);

        }
    }

    public class GetCityTask extends AsyncTask<Integer, Void, ArrayList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(Integer... pcode) {
            return new AreaAction().getChildren(pcode[0]);

        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);

            SimpleAdapter adapter = new SimpleAdapter(NewStoreActivity.this, result, R.layout.spinner_item, new String[] {"name"}, new int[] {R.id.alias});
            citySpinner.setAdapter(adapter);
            citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    NewStoreActivity.areaCode = ((Map) citySpinner.getItemAtPosition(i)).get("id").toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            citySpinner.setSelection(CommonUtils.getSpinnerPosition(CommonUtils.getCityCode(user.getArea()), result), true);


        }
    }

    private void locateManually()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewStoreActivity.this);
        builder.setMessage("店铺的位置使用您的当前位置还是手动指定?");
        builder.setTitle("提示");
        builder.setPositiveButton("手动指定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(NewStoreActivity.this, RelocationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("longitude", longitude);
                bundle.putDouble("latitude", latitude);
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.MAP_LOCATE);
            }
        });
        builder.setNegativeButton("使用当前位置", null);
        builder.create().show();

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
