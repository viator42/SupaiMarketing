package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.adapter.ProductAdapter;
import im.supai.supaimarketing.adapter.StoreAdapter;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class SearchActivity extends AppCompatActivity {
    private ListView resultList;
    private String barcode;
    private int searchType;
    private String keyword;
    private TimeoutbleProgressDialog getSearchResultDialog;
    private ArrayList listData;
    private long storeId = 0;
    private AppContext context;
    private EditText keywordEditText;
    private Button searchBtn;

    private FrameLayout warningLayout;
    private TextView warningTextView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private int page;

    private BaseAdapter adapter;

    private double longitude = 0;
    private double latitude = 0;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = (AppContext) getApplicationContext();

        user = context.getUser();

        getSearchResultDialog = TimeoutbleProgressDialog.createProgressDialog(SearchActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getSearchResultDialog.dismiss();

                Toast.makeText(SearchActivity.this, "未获取到搜索内容", Toast.LENGTH_SHORT).show();

            }
        });

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        //列表点击事件
        resultList = (ListView)findViewById(R.id.listview);
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (searchType)
                {
                    //点击产品
                    case StaticValues.SEARCH_TYPE_BARCODE:
                    case StaticValues.SEARCH_TYPE_ALIAS:
                        for(int a=0; a<listData.size();a++)
                        {
                            Product product = (Product) listData.get(a);
                            if(product.getId() == id)
                            {
                                //跳转到详情页面
                                Intent intent = new Intent();
                                if(context.getUser().getId() == product.getStoreId())
                                {
                                    intent.setClass(SearchActivity.this, ProductViewonlyActivity.class);
                                }
                                else
                                {
                                    intent.setClass(SearchActivity.this, ProductActivity.class);
                                }

                                Bundle bundle=new Bundle();
                                bundle.putParcelable("obj", product);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        }
                        break;

                    //点击店铺
                    case StaticValues.SEARCH_TYPE_STORE:
                        for (int a = 0; a < listData.size(); a++) {
                            if (((Store) listData.get(a)).getId() == id) {
                                //跳转到详情页面
                                Intent intent = new Intent(SearchActivity.this, StoreActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("obj", (Store) listData.get(a));
                                intent.putExtras(bundle);

                                startActivity(intent);
                                SearchActivity.this.finish();

                            }
                        }
                        break;

                }

            }
        });

        page = 0;

        Bundle bundle = this.getIntent().getExtras();
        searchType = bundle.getInt("type");
        switch (searchType)
        {
            case StaticValues.SEARCH_TYPE_BARCODE:
                //获取条形码
                barcode = bundle.getString("barcode");
                storeId = bundle.getLong("storeId");

                //查询商品
                new BarcodeSearchProductTask().execute(barcode);

                break;
            case StaticValues.SEARCH_TYPE_ALIAS:
                storeId = bundle.getLong("storeId");

                break;

            case StaticValues.SEARCH_TYPE_STORE:
                keyword = bundle.getString("keyword");

                //查询店铺
                new SearchStoreByNameTask().execute(keyword);

                break;
            default:
                this.finish();
                break;
        }

        keywordEditText = (EditText) findViewById(R.id.keyword);
        searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 0;
                //搜索
                keyword = keywordEditText.getText().toString();
                if(keyword.isEmpty())
                {
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new SearchTask().execute(keyword);

                }

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                //查询商品
                switch (searchType)
                {
                    case StaticValues.SEARCH_TYPE_BARCODE:
                        new BarcodeSearchProductTask().execute(barcode);

                        break;
                    case StaticValues.SEARCH_TYPE_ALIAS:
                        new SearchTask().execute(keyword);

                        break;
                    case StaticValues.SEARCH_TYPE_STORE:
                        new SearchStoreByNameTask().execute(keyword);

                        break;

                    default:
                        break;
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        longitude = context.getLongitude();
        latitude = context.getLatitude();

    }

    //条码商品查询任务
    public class BarcodeSearchProductTask extends AsyncTask<String, Void, ArrayList<Product>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Product> doInBackground(String... barcode) {
            return new ProductAction().search(SearchActivity.this, barcode[0], storeId, page, false);

        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);
            getSearchResultDialog.dismiss();

            if(result != null)
            {
                if(result.size() == 0 && page > 0)
                {
                    Toast.makeText(SearchActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listData = result;
                    //UI显示
                    List listData = new ArrayList<Map<String,Object>>();
                    for(Product product: result)
                    {
                        Map line = new HashMap();

                        line.put("id", product.getId());
                        line.put("merchantName", product.getMerchant());
                        line.put("address", product.getAddress());
                        line.put("productName", product.getAlias());
                        line.put("storeName", product.getStoreName());
                        line.put("price", product.getPrice());
                        line.put("img", product.getImgUrl());

                        line.put("longitude", product.getLongitude());
                        line.put("latitude", product.getLatitude());

                        listData.add(line);

                    }

                    adapter = new ProductAdapter(listData, SearchActivity.this);
                    resultList.setAdapter(adapter);

                    page += 1;

                }

                warningLayout.setVisibility(View.GONE);
            }
            else
            {
                resultList.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未找到相关商品");

            }

        }
    }

    //根据名称查询任务
    public class SearchTask extends AsyncTask<String, Void, ArrayList<Product>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getSearchResultDialog.show();
        }

        @Override
        protected ArrayList<Product> doInBackground(String... alias) {
            return new ProductAction().searchByName(SearchActivity.this, alias[0], storeId, page, false);

        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);
            getSearchResultDialog.dismiss();

            if(result != null)
            {
                if(result.size() == 0 && page > 0)
                {
                    Toast.makeText(SearchActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listData = result;
                    //UI显示
                    List listData = new ArrayList<Map<String,Object>>();
                    for(Product product: result)
                    {
                        Map line = new HashMap();

                        line.put("id", product.getId());
                        line.put("merchantName", product.getMerchant());
                        line.put("address", product.getAddress());
                        line.put("productName", product.getAlias());
                        line.put("storeName", product.getStoreName());
                        line.put("price", product.getPrice());
                        line.put("img", product.getImgUrl());

                        line.put("longitude", product.getLongitude());
                        line.put("latitude", product.getLatitude());

                        listData.add(line);

                    }

                    ProductAdapter adapter = new ProductAdapter(listData, SearchActivity.this);
                    resultList.setAdapter(adapter);

                    page += 1;

                }

                warningLayout.setVisibility(View.GONE);
            }
            else
            {
                resultList.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未找到相关商品");

            }

        }
    }

    //店铺查询任务
    public class SearchStoreByNameTask extends AsyncTask<String, Void, ArrayList<Store>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getSearchResultDialog.show();

        }

        @Override
        protected ArrayList<Store> doInBackground(String... name) {
            return new StoreAction().searchByName(SearchActivity.this, name[0], user.getId() , false);
        }

        @Override
        protected void onPostExecute(ArrayList<Store> result) {
            super.onPostExecute(result);

            getSearchResultDialog.dismiss();

            if(result.size() != 0)
            {
                //UI更新
                listData = result;
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

                StoreAdapter adapter = new StoreAdapter(listData, SearchActivity.this);
                resultList.setAdapter(adapter);

                warningLayout.setVisibility(View.GONE);
            }
            else
            {
                resultList.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未发现周边商铺");

            }

        }
    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        // back键回退至主界面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();

        }
        return true;
    }
}
