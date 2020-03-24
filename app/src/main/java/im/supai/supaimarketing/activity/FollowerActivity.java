package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import im.supai.supaimarketing.action.FollowerAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.adapter.FollowerAdapter;
import im.supai.supaimarketing.adapter.StoreAdapter;
import im.supai.supaimarketing.model.Follower;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.R;

public class FollowerActivity extends AppCompatActivity {
    private AppContext context;
    private ListView followerListView;
    ArrayList<Follower> followerList;
    private ProgressDialog dialog;
    private Store store;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page;
    private EditText searchTextEditText;
    private String keyword;
    private int searchType;
    private Button searchBtn;
    private Button displayAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        context = (AppContext) getApplicationContext();

        page = 0;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        followerListView = (ListView) findViewById(R.id.follower_list);

        followerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int a = 0; a < followerList.size(); a++) {
                    Follower follower = followerList.get(a);
                    if (follower.getId() == id) {
                        //跳转到详情页面
                        Intent intent = new Intent();
                        intent.setClass(FollowerActivity.this, FollowerDetailActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("obj", follower);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                }

            }
        });

        searchTextEditText = (EditText) findViewById(R.id.search_text);
        searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //搜索
                keyword = searchTextEditText.getText().toString();
                if(keyword.isEmpty())
                {
                    Toast.makeText(FollowerActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if(CommonUtils.isNumber(keyword))
                    {
                        searchType = StaticValues.FOLLOWER_SEARCH_TYPE_TEL;
                    }
                    else
                    {
                        searchType = StaticValues.FOLLOWER_SEARCH_TYPE_NAME;
                    }

                    new SearchFollowerTask().execute(keyword);
                }

            }
        });

        displayAllBtn = (Button) findViewById(R.id.display_all);
        displayAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示所有
                displayAllBtn.setVisibility(View.GONE);
                new DisplayAllFollowerTask().execute();

            }
        });
        displayAllBtn.setVisibility(View.GONE);

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        store = context.getStore();

        dialog = new ProgressDialog(this);
        dialog.setMessage("读取中...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new DisplayAllFollowerTask().execute();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        new DisplayAllFollowerTask().execute();
    }


    public class DisplayAllFollowerTask extends AsyncTask<Long, Void, ArrayList<Follower>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected ArrayList<Follower> doInBackground(Long... id) {
            return new FollowerAction().follower(FollowerActivity.this, store.getId(), page, false);

        }

        @Override
        protected void onPostExecute(ArrayList<Follower> result) {
            super.onPostExecute(result);
            followerList = result;
            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            if(result.size() != 0)
            {
                //UI更新
                List listData = new ArrayList<Map<String,Object>>();
                for(Follower follower: result)
                {
                    Map line = new HashMap();

                    line.put("id", follower.getId());
                    line.put("name", follower.getName());
                    line.put("img", follower.getIcon());

                    listData.add(line);

                }

                FollowerAdapter adapter = new FollowerAdapter(listData, FollowerActivity.this);
                followerListView.setAdapter(adapter);
                page += 1;
            }
            else
            {
                followerListView.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("还没有关注用户");

            }

        }
    }

    //follower查询任务
    public class SearchFollowerTask extends AsyncTask<String, Void, ArrayList<Follower>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected ArrayList<Follower> doInBackground(String... keyword) {
            return new FollowerAction().searchFollower(FollowerActivity.this, store.getId(), keyword[0], searchType);

        }

        @Override
        protected void onPostExecute(ArrayList<Follower> result) {
            super.onPostExecute(result);
            followerList = result;
            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            displayAllBtn.setVisibility(View.VISIBLE);

            if(result.size() != 0)
            {
                //UI更新
                List listData = new ArrayList<Map<String,Object>>();
                for(Follower follower: result)
                {
                    Map line = new HashMap();

                    line.put("id", follower.getId());
                    line.put("name", follower.getName());
                    line.put("img", follower.getIcon());

                    listData.add(line);

                }

                FollowerAdapter adapter = new FollowerAdapter(listData, FollowerActivity.this);
                followerListView.setAdapter(adapter);
                page += 1;


            }
            else
            {
                followerListView.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("搜索结果为空");

            }

        }
    }
}
