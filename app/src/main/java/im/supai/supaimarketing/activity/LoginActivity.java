package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.base.BaseActivity;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

//        context = (AppContext)this.getApplication();
//
//        activity = this;
//
//        loginDialog = TimeoutbleProgressDialog.createProgressDialog(LoginActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
//            @Override
//            public void onTimeOut(TimeoutbleProgressDialog dialog) {
//                loginDialog.dismiss();
//
//                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        telEditText = (EditText)findViewById(R.id.tel);
//        loginBtn = (Button)findViewById(R.id.loginBtn);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tel = telEditText.getText().toString().trim();
//                if(tel.isEmpty() || tel == null)
//                {
//                    Toast.makeText(LoginActivity.this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    new LoginTask().execute();
//                }
//
//            }
//        });
//
//        password = context.getImie();
//
//        versionTextView = (TextView) findViewById(R.id.version);
//        versionTextView.setText(CommonUtils.getVersion(LoginActivity.this));

    }
}
