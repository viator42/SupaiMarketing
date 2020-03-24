package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import im.supai.supaimarketing.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.adapter.StoreAdapter;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.Toast;

public class AroundActivity extends AppCompatActivity {
    private AppContext context;
    private User user;
    private double longitude = 0;
    private double latitude = 0;
    private ListView storeListView;
    ArrayList<Store> storeList;
    private ProgressDialog dialog;

    private FrameLayout warningLayout;
    private TextView warningTextView;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around);

        dialog = new ProgressDialog(this);
        dialog.setMessage("读取中...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        context = (AppContext)getApplicationContext();
        user = context.getUser();

        /*
        //获取当前位置
        longitude = context.getLongitude();
        latitude = context.getLatitude();
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
        if(longitude == 0 || longitude == 0)
        {
            locateManually();
        }
        else
        {
            context.setLongitude(longitude);
            context.setLatitude(latitude);
            new SearchAroundStoreTask().execute(user.getId());
        }
        Log.v("supai current position", Double.toString(longitude) + ' ' + Double.toString(latitude));
        */

        storeListView = (ListView)findViewById(R.id.store_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (int a = 0; a < storeList.size(); a++) {
                    if (storeList.get(a).getId() == l) {
                        //跳转到详情页面
                        Intent intent = new Intent();
                        intent.setClass(AroundActivity.this, StoreActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("obj", storeList.get(a));
                        intent.putExtras(bundle);

                        startActivity(intent);
                        AroundActivity.this.finish();

                    }
                }

            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new SearchAroundStoreTask().execute(user.getId());

            }
        });

        new SearchAroundStoreTask().execute(user.getId());
    }

    @Override
    protected void onStart() {
        super.onStart();

        longitude = context.getLongitude();
        latitude = context.getLatitude();

    }

    //周边商铺查询任务
    public class SearchAroundStoreTask extends AsyncTask<Long, Void, ArrayList<Store>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected ArrayList<Store> doInBackground(Long... userid) {
            return new StoreAction().around(AroundActivity.this, longitude, latitude, userid[0], false);
        }

        @Override
        protected void onPostExecute(ArrayList<Store> result) {
            super.onPostExecute(result);
            storeList = result;
            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            if(result.size() != 0)
            {
                //UI更新
                List listData = new ArrayList<Map<String,Object>>();
                for(Store store: result)
                {
                    Map line = new HashMap();

                    line.put("id", store.getId());
                    line.put("name", store.getName());
                    line.put("logo", store.getLogoUrl());
                    line.put("address", store.getAddress());
                    line.put("longitude", store.getLongitude());
                    line.put("latitude", store.getLatitude());

                    //计算距离
                    if(longitude == 0 || latitude == 0 || store.getLongitude() == 0 || store.getLatitude() == 0)
                    {
                        line.put("distance", null);
                    }
                    else
                    {
                        line.put("distance", CommonUtils.getDistance(longitude, latitude, store.getLongitude(), store.getLatitude()));

                    }

                    listData.add(line);

                }

                StoreAdapter adapter = new StoreAdapter(listData, AroundActivity.this);
                storeListView.setAdapter(adapter);

                warningLayout.setVisibility(View.GONE);
            }
            else
            {
                storeListView.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未发现周边商铺");

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_around, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.menu_action_cart:
                //跳转到购物车页面
                Intent intent = new Intent(AroundActivity.this, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.menu_action_search:
                //店铺根据名称搜索
                final EditText keywordEditText = new EditText(AroundActivity.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(AroundActivity.this);
                builder.setTitle("请输入要查询的店铺名称");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(new EditText(AroundActivity.this));
                builder.setView(keywordEditText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!keywordEditText.getText().toString().isEmpty()) {
                            String keyword = keywordEditText.getText().toString();

                            Intent intent1 = new Intent(AroundActivity.this, SearchActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", StaticValues.SEARCH_TYPE_STORE);
                            bundle.putString("keyword", keyword);
                            intent1.putExtras(bundle);
                            startActivity(intent1);

                        }
                        else
                        {
                            Toast.makeText(AroundActivity.this, "搜素内容不能为空", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

                break;

            case android.R.id.home:
                this.onBackPressed();

                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /*
    //位置相关
    @Override
    public void onLocationChanged(Location location) {
        if(location.getLongitude() != 0 && location.getLatitude() != 0)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        locateManually();

    }

    private void locateManually()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AroundActivity.this);
        builder.setMessage("未获取到当前位置,是否手动指定位置？");
        builder.setTitle("提示");
        builder.setPositiveButton("手动指定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setClass(AroundActivity.this, RelocationActivity.class);
                startActivityForResult(intent, StaticValues.MAP_LOCATE);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AroundActivity.this.finish();
            }
        });
        builder.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode)
        {
            case StaticValues.MAP_LOCATE:
                Bundle bundle = data.getExtras();
                longitude = bundle.getDouble("longitude");
                latitude = bundle.getDouble("latitude");
                //获取位置存入context
                context.setLongitude(longitude);
                context.setLatitude(latitude);

                Log.v("supai position selected", Double.toString(longitude)+"  "+Double.toString(latitude));
                new SearchAroundStoreTask().execute(user.getId());

                break;

        }

    }
    */
}
