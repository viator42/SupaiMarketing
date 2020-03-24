package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.action.RefAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.Follower;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.model.Module;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

public class SettingsActivity extends AppCompatActivity {
    private ImageView iconBtn;

    private TableRow logoutBtn;
    private TableRow refBtn;
    private TableRow storeSettingBtn;
    private TableRow printerBtn;
    private TableRow followerBtn;
    private TableRow statisticsBtn;
    private TableRow salesmanBtn;
    private TableRow proApplyBtn;
    private TableRow aboutusBtn;
    private TableRow helpBtn;

    private TextView nameTextView;
    private TextView telTextView;
    private TextView addressTextView;

    private LinearLayout storeContainer;
    private LinearLayout proContainer;

    private AppContext context;
    private User user;
    private Store store;

    private MenuItem profileEditMenuItem;
    private ActionBar actionBar;

    private Button userEditBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = (AppContext)getApplication();
        user = context.getUser();
        store = context.getStore();

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameTextView = (TextView)findViewById(R.id.alias);
        telTextView = (TextView)findViewById(R.id.tel);
        addressTextView = (TextView)findViewById(R.id.address);

        userEditBtn = (Button) findViewById(R.id.user_edit);
        userEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfileEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.USER_EDIT);

            }
        });

        //店铺相关设置
        storeContainer = (LinearLayout) findViewById(R.id.store_container);
        storeSettingBtn = (TableRow) findViewById(R.id.store_setting);
        followerBtn = (TableRow) findViewById(R.id.follower);

        if(store != null)
        {
            storeSettingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SettingsActivity.this, StoreEditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("obj", store);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
            followerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SettingsActivity.this, FollowerActivity.class);
                    startActivity(intent);

                }
            });
        }
        else
        {
            storeContainer.setVisibility(View.GONE);

        }

        //高级功能  //创建店铺才能开启
        proContainer = (LinearLayout) findViewById(R.id.pro_container);
        proApplyBtn = (TableRow) findViewById(R.id.pro_apply);
        printerBtn = (TableRow) findViewById(R.id.printer);
        statisticsBtn = (TableRow) findViewById(R.id.statistics);
        salesmanBtn = (TableRow) findViewById(R.id.salesman);

        proContainer.setVisibility(View.GONE);
        proApplyBtn.setVisibility(View.GONE);
        printerBtn.setVisibility(View.GONE);
        statisticsBtn.setVisibility(View.GONE);
        salesmanBtn.setVisibility(View.GONE);

        if(store != null)
        {
            proContainer.setVisibility(View.VISIBLE);

            HashMap modules = context.getModules();

            if(modules.isEmpty() || modules == null)
            {
                proApplyBtn.setVisibility(View.VISIBLE);
                proApplyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingsActivity.this,
                                ProIntroductionActivity.class);
                        startActivity(intent);
                    }
                });

            }
            else
            {
                //打印功能
                printerBtn.setVisibility(View.VISIBLE);
                printerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(SettingsActivity.this,
//                                PrinterConnectDialog.class);
//                        startActivity(intent);
                    }
                });
                //统计功能
                statisticsBtn.setVisibility(View.VISIBLE);
                statisticsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingsActivity.this, StatisticsActivity.class);
                        startActivity(intent);

                    }
                });
                //售货员功能
                if(user.getClerkOf() == 0)
                {
                    salesmanBtn.setVisibility(View.VISIBLE);
                    salesmanBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(SettingsActivity.this, ClerksActivity.class);
                            startActivity(intent);

                        }
                    });

                }

            }
        }

        iconBtn = (ImageView)findViewById(R.id.icon);

        logoutBtn = (TableRow)findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("是否登出账户?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new UserAction().logout(SettingsActivity.this);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        helpBtn = (TableRow) findViewById(R.id.help);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                startActivity(intent);

            }
        });

        //提交建议
        refBtn = (TableRow)findViewById(R.id.ref);
        refBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, RefActivity.class);
                startActivity(intent);

            }
        });

        aboutusBtn = (TableRow) findViewById(R.id.about_us);
        aboutusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                startActivity(intent);

            }
        });

        new LoadSettingsTask().execute(user.getId());
        setValues();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    //获取设置任务
    public class LoadSettingsTask extends AsyncTask<Long, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Long... userid) {
            return new UserAction().loadSettings(userid[0]);

        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);

            //设置属性
            if(result != null)
            {
                //更新context
                user = result;
                context.setUser(result);

                new RefAction().setUser(SettingsActivity.this, user);
//                writeToRef(result);
            }

        }
    }

    public void setValues()
    {
        nameTextView.setText(user.getName());
        telTextView.setText(user.getTel());
        addressTextView.setText(user.getAddress());

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_user) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_user) // resource or drawable
//                .showImageOnFail(R.drawable.ic_user) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(user.getIconUrl()), iconBtn, options);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.menu_order, menu);
//        super.onCreateOptionsMenu(menu);
//        //添加菜单项
//        profileEditMenuItem = menu.add(0, StaticValues.MENU_PROFILE_EDIT,0,"修改");
//        profileEditMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        switch (id)
//        {
//            case StaticValues.MENU_PROFILE_EDIT:
//
//
//                break;
//
//            case android.R.id.home:
//                this.onBackPressed();
//
//            default:
//                break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode)
        {
            case StaticValues.USER_EDIT:
                Bundle bundle = data.getExtras();
                user = bundle.getParcelable("obj");
                setValues();

                context.setUser(user);

                new RefAction().setUser(SettingsActivity.this, user);

                break;
        }

    }
}
