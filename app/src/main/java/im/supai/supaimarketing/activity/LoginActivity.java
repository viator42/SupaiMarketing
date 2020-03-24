package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.RefAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.base.BaseActivity;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class LoginActivity extends BaseActivity {
    private EditText telEditText;
    private Button loginBtn;

    private String tel;
    private String password;
    private Activity activity;
    private AppContext context;

    private TextView versionTextView;

    private TimeoutbleProgressDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        context = (AppContext)this.getApplication();

        activity = this;

        loginDialog = TimeoutbleProgressDialog.createProgressDialog(LoginActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                loginDialog.dismiss();

                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });

        telEditText = (EditText)findViewById(R.id.tel);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = telEditText.getText().toString().trim();
                if(tel.isEmpty() || tel == null)
                {
                    Toast.makeText(LoginActivity.this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new LoginTask().execute();
                }

            }
        });

        password = context.getImie();

        versionTextView = (TextView) findViewById(R.id.version);
        versionTextView.setText(CommonUtils.getVersion(LoginActivity.this));

    }

    //登录事件
    public class LoginTask extends AsyncTask<Void, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginDialog.show();
        }

        @Override
        protected User doInBackground(Void... id) {
            User user = new User();

            user.setTel(tel);
            user.setPassword(password);

            if(user.loginValidation())
            {
                user = new UserAction().Login(user);
                return user;
            }
            else
            {
                Toast.makeText(LoginActivity.this, user.getValidationMsg(), Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            loginDialog.dismiss();

            Intent intent;
            Bundle bundle;

            if(result != null)
            {
                if(result.isLoginSuccess())
                {
                    //用户信息写入ref
                    new RefAction().setUser(LoginActivity.this, result);

                    //用户信息写入全局类
                    AppContext context =  (AppContext)activity.getApplication();
                    context.setUser(result);

                    intent = new Intent();
                    intent.setClass(LoginActivity.this, SplashActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                }
                else
                {
                    switch (result.getErrCode())
                    {
                        case StaticValues.USER_NOTFOUND:
                            //跳转到注册(完善信息)
                            intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            bundle = new Bundle();
                            bundle.putString("tel", telEditText.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);

                            break;
                        case StaticValues.AUTO_PASSWORD_FAILED:
                            //跳转到申诉
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("登录失败,如果您更换了手机,请点击申诉找回账号。");
                            builder.setTitle("提示");
                            builder.setPositiveButton("账号申诉", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(LoginActivity.this, AppealActivity.class);
                                    Bundle bundle = new Bundle();

                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, StaticValues.MAP_LOCATE);
                                }
                            });
                            builder.create().show();
                            break;
                        case StaticValues.PASSWORD_NEEDED:
                            //跳转到输入密码
                            intent = new Intent(LoginActivity.this, PasswordActivity.class);
                            bundle = new Bundle();
                            bundle.putLong("userid", result.getId());
                            intent.putExtras(bundle);
                            startActivity(intent);

                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }

            }
            else
            {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出确认
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("是否退出速派？");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.this.finish();
                    System.exit(0);
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();

        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

}
