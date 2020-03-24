package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.action.AreaAction;
import im.supai.supaimarketing.action.RefAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.User;
import java.util.ArrayList;
import java.util.Map;
import im.supai.supaimarketing.R;

public class RegisterActivity extends AppCompatActivity {
    private TextView telTextView;
    private Button registerBtn;
    private Activity activity;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private EditText nameTextEdit;
    private EditText addressTextEdit;
    private AppContext context;

    private static String areaCode;
    private String password;
    private String tel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = (AppContext)this.getApplication();
        activity = this;

        telTextView = (TextView)findViewById(R.id.tel);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();

                user.setTel(telTextView.getText().toString().trim());
                user.setName(nameTextEdit.getText().toString().trim());
                user.setPassword(password);
                user.setAddress(addressTextEdit.getText().toString().trim());
                user.setArea(areaCode);

                if(user.registerValidation())
                {
                    new RegisterTask().execute(user);
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, user.getValidationMsg(), Toast.LENGTH_SHORT).show();

                }

            }
        });
        provinceSpinner = (Spinner)findViewById(R.id.province);
        citySpinner = (Spinner)findViewById(R.id.city);
        nameTextEdit = (EditText)findViewById(R.id.name);
        addressTextEdit = (EditText)findViewById(R.id.address);

        //获取电话号码和密码
        password = context.getImie();

        //获取参数
        Bundle bundle = this.getIntent().getExtras();
        tel = bundle.getString("tel");
        telTextView.setText(tel);

        new GetProivinceTask().execute();

    }

    //注册事件
    public class RegisterTask extends AsyncTask<User, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerBtn.setEnabled(false);
        }

        @Override
        protected User doInBackground(User... user) {
            return new UserAction().Register(user[0]);

        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            registerBtn.setEnabled(true);
            if(result != null)
            {
                if(result.isLoginSuccess())
                {
                    //用户信息写入全局类
                    AppContext context =  (AppContext)activity.getApplication();
                    context.setUser(result);

                    //用户信息写入ref
                    new RefAction().setUser(RegisterActivity.this, result);

                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, SplashActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();

                }

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

            SimpleAdapter adapter = new SimpleAdapter(RegisterActivity.this, result, R.layout.spinner_item, new String[] {"name"}, new int[] {R.id.alias});
            provinceSpinner.setAdapter(adapter);
            provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String code = ((Map)provinceSpinner.getItemAtPosition(i)).get("id").toString();
                    new GetCityTask().execute(new Integer(code));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }

    //获取市列表事件
//    public class GetCityThread implements Runnable
//    {
//        private int pcode;
//        public GetCityThread(int pcode)
//        {
//            this.pcode = pcode;
//
//        }
//
//        @Override
//        public void run() {
//            ArrayList result =  new AreaAction().getChildren(this.pcode);
//
//        }
//    }

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

            SimpleAdapter adapter = new SimpleAdapter(RegisterActivity.this, result, R.layout.spinner_item, new String[] {"name"}, new int[] {R.id.alias});
            citySpinner.setAdapter(adapter);
            citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    RegisterActivity.areaCode = ((Map)citySpinner.getItemAtPosition(i)).get("id").toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }
}
