package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import im.supai.supaimarketing.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.SalesAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

public class ClerkRegisterActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchBtn;
    private AppContext context;
    private Store store;
    private String searchStr = null;
    private TableLayout userInfoLayout;
    private ImageView iconImageView;
    private TextView nameTextView;
    private TextView telTextView;
    private Button confirmBtn;
    private Button cancelBtn;
    private User clerk = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_register);

        context = (AppContext) getApplicationContext();
        store = context.getStore();

        userInfoLayout = (TableLayout) findViewById(R.id.user_info);
        userInfoLayout.setVisibility(View.GONE);

        iconImageView = (ImageView) findViewById(R.id.icon);
        nameTextView = (TextView) findViewById(R.id.name);
        telTextView = (TextView) findViewById(R.id.tel);

        searchEditText = (EditText) findViewById(R.id.search_bar);
        searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStr = searchEditText.getText().toString();
                if(searchStr != null && !searchStr.isEmpty())
                {
                    new FindByTelTask().execute();

                }
                else
                {
                    Toast.makeText(ClerkRegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clerk != null)
                {
                    new ClerkRegisterTask().execute();
                }
                else
                {
                    Toast.makeText(ClerkRegisterActivity.this, "先搜索用户再添加", Toast.LENGTH_SHORT).show();

                }

            }
        });

        cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //获取用户信息任务
    public class FindByTelTask extends AsyncTask<Void, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Void... voids) {
            return new UserAction().findByTel(searchStr);
        }

        @Override
        protected void onPostExecute(User result) {
            if(result != null)
            {
                clerk = result;

                userInfoLayout.setVisibility(View.VISIBLE);
//                ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(result.getIconUrl()), iconImageView, options);
                nameTextView.setText(result.getName());
                telTextView.setText(result.getTel());

            }
            else
            {
                Toast.makeText(ClerkRegisterActivity.this, "用户未找到", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //添加用户为员工任务
    public class ClerkRegisterTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return new SalesAction().clerkRegister(clerk.getId(), store.getId());

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                //跳转回员工列表页面
                Intent intent = new Intent(ClerkRegisterActivity.this, ClerksActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("result", result);
                intent.putExtras(bundle);

                setResult(StaticValues.ADD_CLERK, intent);
                finish();

            }
            else
            {
                Toast.makeText(ClerkRegisterActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
