package im.supai.supaimarketing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import im.supai.supaimarketing.activity.LoginActivity;
import im.supai.supaimarketing.activity.SplashActivity;
import im.supai.supaimarketing.base.BaseActivity;
import im.supai.supaimarketing.model.FirstOpen;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.StaticValues;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent intent = new Intent();
        AppContext context =  (AppContext) this.getApplication();

        //获取手机的信息
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        context.setImie(tm.getDeviceId());
//        context.setPhoneNumber(tm.getLine1Number());

        //获取屏幕分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        context.setScreenWidth(metrics.widthPixels);
        context.setScreenHeight(metrics.heightPixels);

        //Icon尺寸
        context.setIconWidth((context.getScreenWidth() - 2 * StaticValues.margin) / 4);
        context.setIconHeight((context.getScreenHeight() - 2 * StaticValues.margin) / 5);

        SharedPreferences ref = getSharedPreferences("user", Context.MODE_PRIVATE);

        if(ref != null)
        {
            long id = ref.getLong("id", 0);
            if(id != 0)
            {
                //获取用户信息成功
                User user = new User();
                user.setId(id);
                user.setName(ref.getString("name", ""));
                user.setUsername(ref.getString("username", ""));
                user.setTel(ref.getString("tel", ""));
                user.setIconUrl(ref.getString("icon", ""));
                user.setAddress(ref.getString("address", ""));
                user.setArea(ref.getString("area", ""));
                user.setPasstype(ref.getInt("passtype", 1));
                user.setClerkOf(ref.getLong("clerk_of", 0));
                user.setStatus(ref.getInt("status", 1));
                user.setStatus(ref.getInt("print_copy", StaticValues.PRINT_COPY));

                context.setUser(user);

                intent.setClass(StartActivity.this, SplashActivity.class);
                startActivity(intent);
            }
            else
            {
                //用户登录
                intent.setClass(StartActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        }
        else
        {
            //用户登录
            intent.setClass(StartActivity.this, LoginActivity.class);
            startActivity(intent);

        }

        //检查是否是首次开启
        FirstOpen firstOpen = new FirstOpen();
        SharedPreferences firstOpenRef = getSharedPreferences("firstOpen", Context.MODE_PRIVATE);
        if(firstOpenRef != null)
        {
            firstOpen.setAll(firstOpenRef.getInt("all", StaticValues.FIRST_OPEN_Y));
            firstOpen.setMainPage(firstOpenRef.getInt("mainpage", StaticValues.FIRST_OPEN_Y));
            firstOpen.setStore(firstOpenRef.getInt("store", StaticValues.FIRST_OPEN_Y));
            firstOpen.setPrinter(firstOpenRef.getInt("printer", StaticValues.FIRST_OPEN_Y));
            firstOpen.setSales(firstOpenRef.getInt("sales", StaticValues.FIRST_OPEN_Y));

        }
        context.setFirstOpen(firstOpen);
    }
}
