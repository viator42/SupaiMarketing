package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.app.Activity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.adapter.ProductAdapter;
import im.supai.supaimarketing.adapter.ProductSearchResultAdapter;
import im.supai.supaimarketing.model.OrderDetail;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.util.StaticValues;

public class ProductSearchResultListActivity extends AppCompatActivity {
    private String barcode;
    private String alias;
    private long storeId;

    private ListView listView;
    private FrameLayout warningLayout;
    private TextView warningTextView;

    ArrayList<Product> products;

    private SwipeRefreshLayout swipeRefreshLayout;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search_result_list);

        listView = (ListView) findViewById(R.id.listview);

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle.getString("barcode") != null && !bundle.getString("barcode").isEmpty())
        {
            //获取条形码
            barcode = bundle.getString("barcode");
            storeId = bundle.getLong("storeId");
            //查询商品
            new BarcodeSearchProductTask().execute(barcode);

        }
        else
        {
            //获取名称
            alias = bundle.getString("alias");
            storeId = bundle.getLong("storeId");

            //查询商品
            new SearchTask().execute(alias);

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                for (int a = 0; a < products.size(); a++) {
                    Product product = products.get(a);
                    if (product.getId() == id) {
                        //跳转回
                        Intent intent = new Intent(ProductSearchResultListActivity.this, SalesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("product", product);
                        intent.putExtras(bundle);

                        setResult(StaticValues.PRODUCT_SEARCH_RESULT, intent);
                        finish();

                    }
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);


            }
        });

    }

    //条码商品查询任务
    public class BarcodeSearchProductTask extends AsyncTask<String, Void, ArrayList<Product>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.show();
        }

        @Override
        protected ArrayList<Product> doInBackground(String... barcode) {
            return new ProductAction().search(ProductSearchResultListActivity.this, barcode[0], storeId, page, false);

        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);
//            dialog.dismiss();

            if(result != null && result.size() > 0)
            {
                products = result;
                //UI显示
                List listData = new ArrayList<Map<String,Object>>();
                for(Product product: result)
                {
                    Map line = new HashMap();

                    line.put("id", product.getId());
                    line.put("alias", product.getAlias());
                    line.put("price", product.getPrice());
                    line.put("img", product.getImgUrl());

                    listData.add(line);

                }

                ProductSearchResultAdapter adapter = new ProductSearchResultAdapter(listData, ProductSearchResultListActivity.this);
                listView.setAdapter(adapter);

                warningLayout.setVisibility(View.GONE);
            }
            else
            {
                listView.setAdapter(null);

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
//            dialog.show();
        }

        @Override
        protected ArrayList<Product> doInBackground(String... alias) {
            return new ProductAction().searchByName(ProductSearchResultListActivity.this, alias[0],storeId, page, false);

        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);
//            dialog.dismiss();

            if(result != null && result.size() > 0)
            {
                products = result;
                //UI显示
                List listData = new ArrayList<Map<String,Object>>();
                for(Product product: result)
                {
                    Map line = new HashMap();

                    line.put("id", product.getId());
                    line.put("alias", product.getAlias());
                    line.put("price", product.getPrice());
                    line.put("img", product.getImgUrl());

                    listData.add(line);

                }

                ProductSearchResultAdapter adapter = new ProductSearchResultAdapter(listData, ProductSearchResultListActivity.this);
                listView.setAdapter(adapter);

                warningLayout.setVisibility(View.GONE);
            }
            else
            {
                listView.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未找到相关商品");

            }

        }
    }
}
