package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.AdvertisementAction;
import im.supai.supaimarketing.action.AppAction;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.ModuleAction;
import im.supai.supaimarketing.action.OrderAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.fragment.MainpageContentFragment;
import im.supai.supaimarketing.fragment.StorepageFragment;
import im.supai.supaimarketing.model.Collection;
import im.supai.supaimarketing.model.Module;
import im.supai.supaimarketing.model.ModuleCategory;
import im.supai.supaimarketing.model.Orders;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class SplashActivity extends AppCompatActivity {
    private AppContext context;

    private long SplashTime = 2000;
    private long currentTimeMil;
    private Intent intent;
    private TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = (AppContext)getApplicationContext();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        currentTimeMil = System.currentTimeMillis();

        //版本号
        versionTextView = (TextView) findViewById(R.id.version);
        versionTextView.setText(CommonUtils.getVersion(SplashActivity.this));

        //设置别名
//        mHandler.sendMessage(mHandler.obtainMessage(StaticValues.MSG_SET_ALIAS, context.getUser().getSn()));
//        JPushInterface.setAlias(getApplicationContext(), context.getUser().getSn(), mAliasCallback);

        new LoadTask(context.getUser().getId()).start();
//        if(isOpenNetwork())
//        {
//            new LoadTask(context.getUser().getId()).start();
//
//        }
//        else
//        {
//            intent = new Intent(SplashActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//
//        }

    }


    public class LoadTask extends Thread
    {
        private long userid;

        public LoadTask(long userid) {
            this.userid = userid;
        }

        @Override
        public void run() {
            //启动定位服务
//            intent = new Intent(SplashActivity.this, LocationService.class);
//            Bundle bundle = new Bundle();
//            bundle.putLong("userid", userid);
//            intent.putExtras(bundle);
//            startService(intent);

            long timeInterval = System.currentTimeMillis() - currentTimeMil;
            if(timeInterval < SplashTime)
            {
                try {
                    sleep(SplashTime - timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            new AppAction().appDataInit(SplashActivity.this, userid);

            intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    /*
    //检测网络是否可用
    private boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticValues.MSG_SET_ALIAS:
                    Log.d("supai", "Set alias in handler.");
                    JPushInterface.setAlias(getApplicationContext(), (String) msg.obj, mAliasCallback);
                    break;

                default:
                    Log.i("supai", "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.v("supai", logs);
                    break;

                case 6002:
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(StaticValues.MSG_SET_ALIAS, alias), 1000 * 60);

                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.v("supai", logs);
            }

        }

    };

     */
}
