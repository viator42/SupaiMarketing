package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.SalesAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.adapter.ClerkAdapter;
import im.supai.supaimarketing.adapter.FollowerAdapter;
import im.supai.supaimarketing.model.Follower;
import im.supai.supaimarketing.model.MapMarker;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.R;

public class ClerksActivity extends AppCompatActivity {
    private ListView clerkListview;
    private AppContext context;
    private Store store;

    private MenuItem addClerkMenuItem;

    private FrameLayout warningLayout;
    private TextView warningTextView;

    ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerks);

        context = (AppContext) getApplicationContext();

        clerkListview = (ListView) findViewById(R.id.clerk_list);
        clerkListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                User user;
                for(int a=0; a<userList.size(); a++)
                {
                    user = userList.get(a);
                    if(user.getId() == id)
                    {
                        //跳转到详情页面
                        Intent intent = new Intent();
                        intent.setClass(ClerksActivity.this, ClerkDetailActivity.class);

                        Bundle bundle=new Bundle();
                        bundle.putParcelable("obj", user);

                        intent.putExtras(bundle);

                        startActivity(intent);

                    }
                }
            }
        });

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        store = context.getStore();

        new ClerksListTask().execute(store.getId());

    }

    //员工列表
    public class ClerksListTask extends AsyncTask<Long, Void, ArrayList<User>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<User> doInBackground(Long... storeId) {

            return new SalesAction().clerks(storeId[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            if(result != null && result.size() > 0)
            {
                userList = result;

                warningLayout.setVisibility(View.GONE);

                //UI更新
                List listData = new ArrayList<Map<String,Object>>();
                for(User user: result)
                {
                    Map line = new HashMap();

                    line.put("id", user.getId());
                    line.put("name", user.getName());
                    line.put("icon", user.getIconUrl());
                    line.put("tel", user.getTel());

                    listData.add(line);

                }

                ClerkAdapter adapter = new ClerkAdapter(listData, ClerksActivity.this);
                clerkListview.setAdapter(adapter);

            }
            else
            {
                clerkListview.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("员工列表为空");

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_store, menu);

        addClerkMenuItem = menu.add(0, StaticValues.MENU_ADD_CLERK,0,"搜索");

        addClerkMenuItem.setIcon(android.R.drawable.ic_input_add);

        addClerkMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        AlertDialog.Builder builder = new AlertDialog.Builder(ClerksActivity.this);

        switch (id)
        {
            case StaticValues.MENU_ADD_CLERK:
                //跳转员工添加界面
                intent.setClass(ClerksActivity.this, ClerkRegisterActivity.class);
                bundle.putLong("storeId", store.getId());
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.ADD_CLERK);
                break;

            case android.R.id.home:
                this.onBackPressed();

                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode)
        {
            case StaticValues.ADD_CLERK:
                //刷新员工列表
                new ClerksListTask().execute(store.getId());

                break;

        }

    }
}
