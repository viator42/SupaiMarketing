package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.AreaAction;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

public class ProfileEditActivity extends AppCompatActivity {
    private EditText nameEditText;
    private TextView telEditText;
    private EditText addressEditText;
    private User user;
    private Bitmap icon = null;
    private ImageView iconImgView;
    private Button changePasswordBtn;
    private Button confirmBtn;
    private Button cancelBtn;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private AppContext context;
    private String newPassword;
    private TableLayout passtypeLayout;
    private TableLayout proLayout;
    private EditText printCopyEditText;

    private RadioButton autoPasswordRadioButton;
    private RadioButton independentPasswordRadioButton;

    private Uri imageUri = Uri.parse(StaticValues.IMAGE_FILE_LOCATION);//The Uri to store the big bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        context = (AppContext) getApplicationContext();
//        user = context.getUser();
        Bundle bundle = this.getIntent().getExtras();
        user = bundle.getParcelable("obj");

        nameEditText = (EditText) findViewById(R.id.name);
        telEditText = (TextView) findViewById(R.id.tel);
        addressEditText = (EditText) findViewById(R.id.address);
        iconImgView = (ImageView) findViewById(R.id.icon);
        confirmBtn = (Button) findViewById(R.id.confirm);
        changePasswordBtn = (Button) findViewById(R.id.change_password);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setName(nameEditText.getText().toString().trim());
//                user.setTel(telEditText.getText().toString());
                user.setAddress(addressEditText.getText().toString().trim());

                user.setPrintCopy(Integer.valueOf(printCopyEditText.getText().toString().trim()));

                if(user.updateValidation())
                {
                    new UserUpdateTask().execute(user);
                }
                else
                {
                    Toast.makeText(ProfileEditActivity.this, user.getValidationMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        passtypeLayout = (TableLayout) findViewById(R.id.passtype_layout);
        passtypeLayout.setVisibility(View.GONE);

        autoPasswordRadioButton = (RadioButton) findViewById(R.id.auto_password);
        independentPasswordRadioButton = (RadioButton) findViewById(R.id.independent_password);

        if(user.getPasstype() == StaticValues.USER_PASSTYPE_AUTO)
        {
            autoPasswordRadioButton.setChecked(true);
            independentPasswordRadioButton.setChecked(false);

            changePasswordBtn.setVisibility(View.GONE);
        }
        if(user.getPasstype() == StaticValues.USER_PASSTYPE_INDEPENDENT)
        {
            independentPasswordRadioButton.setChecked(true);
            autoPasswordRadioButton.setChecked(false);

            changePasswordBtn.setVisibility(View.VISIBLE);
        }

        autoPasswordRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    autoPasswordRadioButton.setChecked(true);
                    independentPasswordRadioButton.setChecked(false);

                    user.setPasstype(StaticValues.USER_PASSTYPE_AUTO);
                    changePasswordBtn.setVisibility(View.GONE);
                }
            }
        });
        independentPasswordRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    independentPasswordRadioButton.setChecked(true);
                    autoPasswordRadioButton.setChecked(false);

                    user.setPasstype(StaticValues.USER_PASSTYPE_INDEPENDENT);
                    changePasswordBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        changePasswordBtn = (Button) findViewById(R.id.change_password);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileEditActivity.this, PasswordChangeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("passtype", user.getPasstype());
                bundle.putString("password", user.getPassword());
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.CHANGE_PASSWORD);
            }
        });

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_user) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_user) // resource or drawable
//                .showImageOnFail(R.drawable.ic_user) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(user.getIconUrl()), iconImgView, options);
        iconImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
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

        cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileEditActivity.this.finish();

            }
        });

        provinceSpinner = (Spinner)findViewById(R.id.province);
        citySpinner = (Spinner)findViewById(R.id.city);

        //
        proLayout = (TableLayout) findViewById(R.id.pro_layout);
        proLayout.setVisibility(View.GONE);
        printCopyEditText = (EditText) findViewById(R.id.print_copy);

        HashMap modules = context.getModules();

        if(modules.isEmpty() || modules == null)
        {
            proLayout.setVisibility(View.GONE);

        }
        else
        {
            proLayout.setVisibility(View.VISIBLE);

            printCopyEditText.setText(Integer.toString(user.getPrintCopy()));

        }

        new GetProivinceTask().execute();

        setValues();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void setValues()
    {
        nameEditText.setText(user.getName());
        telEditText.setText(user.getTel());
        addressEditText.setText(user.getAddress());

//        ImageLoader.getInstance().loadImage(CommonUtils.getImgFullpath(user.getIconUrl()), new SimpleImageLoadingListener() {
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view,
//                                          Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                iconImgView.setImageBitmap(loadedImage);
//            }
//
//        });

    }

    // 提交修改结果
    public class UserUpdateTask extends AsyncTask<User, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(User... user) {
            //上传图片
            user[0].setIconUrl(null);
            if(icon != null)
            {
//                Image resultImage = new ImageAction().ImageUpload(icon);
//                if(resultImage.isUploadSuccess())
//                {
//                    user[0].setIconUrl(resultImage.getUrl());
//                    ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(resultImage.getUrl()), iconImgView);
//                }
//                else
//                {
//                    return null;
//
//                }

            }
            if(user[0].getPasstype() == StaticValues.USER_PASSTYPE_INDEPENDENT)
            {
                user[0].setPassword(newPassword);

            }
            return new UserAction().update(user[0]);

        }

        @Override
        protected void onPostExecute(User result) {
            if(result != null)
            {
                //跳转回店铺页面
                Intent intent = new Intent(ProfileEditActivity.this, SettingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", result);
                intent.putExtras(bundle);

                setResult(StaticValues.USER_EDIT, intent);
                finish();

            }
            else
            {
                Toast.makeText(ProfileEditActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //获取Logo图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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
                Toast.makeText(ProfileEditActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode == StaticValues.IMAGE_UPLOAD && resultCode == RESULT_OK)
        {
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
                    this.icon = img;
                    iconImgView.setImageBitmap(img);

                }

            }
            else
            {
                Toast.makeText(ProfileEditActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode == StaticValues.IMAGE_CROP && resultCode == RESULT_OK)
        {
            //裁剪
            if(imageUri != null){
                icon = decodeUriAsBitmap(imageUri);//decode bitmap
                iconImgView.setImageBitmap(icon);
            }
            else
            {
                Toast.makeText(ProfileEditActivity.this, "图片裁剪失败", Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode == StaticValues.USER_EDIT)
        {
            //密码修改
            Bundle bundle = data.getExtras();
            newPassword = bundle.getString("password_new");

        }

        super.onActivityResult(requestCode, resultCode, data);

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

            SimpleAdapter adapter = new SimpleAdapter(ProfileEditActivity.this, result, R.layout.spinner_item, new String[] {"name"}, new int[] {R.id.alias});
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

            SimpleAdapter adapter = new SimpleAdapter(ProfileEditActivity.this, result, R.layout.spinner_item, new String[] {"name"}, new int[] {R.id.alias});
            citySpinner.setAdapter(adapter);
            citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    user.setArea(((Map) citySpinner.getItemAtPosition(i)).get("id").toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            citySpinner.setSelection(CommonUtils.getSpinnerPosition(CommonUtils.getCityCode(user.getArea()), result), true);


        }
    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出确认
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
            builder.setMessage("是否放弃修改并退出");
            builder.setTitle("提示");
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ProfileEditActivity.this.finish();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();

        }

        return true;
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
