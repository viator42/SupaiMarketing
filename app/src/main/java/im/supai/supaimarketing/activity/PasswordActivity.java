package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.R;

public class PasswordActivity extends AppCompatActivity {
    private long userid;
    private EditText passwordEditText;
    private Button confirmBtn;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Bundle bundle = this.getIntent().getExtras();
        userid = bundle.getLong("userid", 0);

        passwordEditText = (EditText) findViewById(R.id.password);
        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = passwordEditText.getText().toString();
                if(!password.isEmpty())
                {
                    new PasswordValidateTask().execute(userid);

                }
                else
                {
                    Toast.makeText(PasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //确认密码任务
    public class PasswordValidateTask extends AsyncTask<Long, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Long... userid) {
            return new UserAction().passwordValidate(userid[0], password);

        }

        @Override
        protected void onPostExecute(User result) {

            if(result != null && result.isLoginSuccess())
            {

            }
            else
            {
                Toast.makeText(PasswordActivity.this, "登录失败", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
