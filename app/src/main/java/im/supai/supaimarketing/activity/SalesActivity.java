package im.supai.supaimarketing.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.action.SalesAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.adapter.ProductSearchResultAdapter;
import im.supai.supaimarketing.adapter.RroductSalesAdapter;
import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Sale;
import im.supai.supaimarketing.model.Statistics;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class SalesActivity extends AppCompatActivity {

    private AppContext context;
    private User user;
    private Store relatedStore;

    private Button scanBtn;
    private EditText searchEditText;
    private Button searchBtn;
    private TextView salesDateTextView;
    private TextView productSummaryTextView;
    private TextView productCountTextView;
    private ListView productListView;
    private Button cancelBtn;
    private Button confirmBtn;
    private TextView storeNameTextView;

    private TimeoutbleProgressDialog getProductsDialog;

    private int productCount;   //商品总数
    private double productSummary;     //总价

    private MenuItem clearMenuItem;
    private MenuItem saleHistoryMenuItem;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        context = (AppContext) getApplicationContext();
        user = context.getUser();

        scanBtn = (Button) findViewById(R.id.scan);
        searchEditText = (EditText) findViewById(R.id.search_bar);
        searchBtn = (Button) findViewById(R.id.search);
        salesDateTextView = (TextView) findViewById(R.id.sales_date);
        productSummaryTextView = (TextView) findViewById(R.id.product_summary);
        productCountTextView = (TextView) findViewById(R.id.product_count);
        productListView = (ListView) findViewById(R.id.product_list);
        cancelBtn = (Button) findViewById(R.id.cancel);
        confirmBtn = (Button) findViewById(R.id.confirm);

        storeNameTextView = (TextView) findViewById(R.id.store_name);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(SalesActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bundle.putInt("action", StaticValues.SCAN_TYPE_SALES);
                bundle.putLong("storeId", user.getClerkOf());
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.PRODUCT_SEARCH_RESULT);

            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alias = searchEditText.getText().toString();
                if(CommonUtils.isValueEmpty(alias))
                {
                    Toast.makeText(SalesActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.setClass(SalesActivity.this, ProductSearchResultListActivity.class);
                    bundle.putLong("storeId", user.getClerkOf());
                    bundle.putString("alias", alias);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, StaticValues.PRODUCT_SEARCH_RESULT);

                }

            }
        });

        getProductsDialog = TimeoutbleProgressDialog.createProgressDialog(SalesActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getProductsDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
                builder.setMessage("未获取到产品信息");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        SalesActivity.this.finish();

                    }
                });

                builder.create().show();
            }
        });

        // 生成订单
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddOrderTask().execute();

            }
        });

        new ClerkAvalibleTask().execute(user.getId());

    }

    @Override
    protected void onStart() {
        super.onStart();

        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        salesDateTextView.setText(df.format(new Date()));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode)
        {
            case StaticValues.PRODUCT_SEARCH_RESULT:
                //添加商品
                Bundle bundle = data.getExtras();
                Product product = bundle.getParcelable("product");

                Sale sale = new Sale();

                sale.setProductId(product.getId());
                sale.setClerkId(user.getId());
                sale.setStoreId(user.getClerkOf());
                sale.setPrice(product.getPrice());
                sale.setCount(1);

                new AddTask().execute(sale);

                break;

        }

    }

    //移除商品
//    public void removeProduct(long id)
//    {
//        context.removeSalesList(id);
//        this.loadProductList();
//
//    }

    //修改商品数量
//    public void productChangeCount(long id, int count)
//    {
//        context.changeSalesListProductCount(id, count);
//        this.loadProductList();
//
//    }

    //重新加载商品列表
    public void reloadList()
    {
        new ListTask().execute();
    }

//    private void setCount()
//    {
//        TextView view1 = (TextView) view;
//
//        final EditText countEditText = new EditText(context);
//        countEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        countEditText.setText(view1.getText().toString());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("修改数量");
//        builder.setIcon(android.R.drawable.ic_dialog_info);
//        builder.setView(new EditText(context));
//        builder.setView(countEditText);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //更新商品数量
//                if (!countEditText.getText().toString().isEmpty()) {
//                    int count = Integer.valueOf(countEditText.getText().toString());
//                    if (count > 0) {
//
//
//
//                    }
//
//                }
//
//            }
//        });
//        builder.setNegativeButton("取消", null);
//        builder.create().show();
//    }

    //验证员工所属任务
    public class ClerkAvalibleTask extends AsyncTask<Long, Void, Store>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Store doInBackground(Long... userid) {
            return new SalesAction().clerkAvalible(userid[0]);
        }

        @Override
        protected void onPostExecute(Store result) {
            if(result != null)
            {
                context.setRelatedStore(result);
                //UI更新
                storeNameTextView.setText(result.getName());

                //商品列表
                reloadList();

            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
                builder.setMessage("售货员验证失败,未获取到店铺信息");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SalesActivity.this.finish();

                    }
                });

                builder.create().show();

            }
        }
    }

    //商品列表
    public class ListTask extends AsyncTask<Void, Void, ArrayList<Sale>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getProductsDialog.show();

        }

        @Override
        protected ArrayList<Sale> doInBackground(Void... voids) {
            return new SalesAction().list(user.getId(), user.getClerkOf());

        }

        @Override
        protected void onPostExecute(ArrayList<Sale> result) {
            getProductsDialog.dismiss();

            productCount = 0;
            productSummary = 0;

            //刷新商品列表
            List listData = new ArrayList<Map<String,Object>>();

            for(Sale sale: result)
            {
                Map line = new HashMap();

                int count = sale.getCount();
                double price = sale.getPrice();
                double summary = price * count;

                line.put("id", sale.getId());
                line.put("img", sale.getImg());
                line.put("alias", sale.getAlias());
                line.put("price", price);
                line.put("count", count);
                line.put("summary", summary);

                listData.add(line);
                productCount += count;
                productSummary += summary;
            }

            RroductSalesAdapter adapter = new RroductSalesAdapter(listData, SalesActivity.this);
            productListView.setAdapter(adapter);

            productCountTextView.setText(Integer.toString(productCount));
            productSummaryTextView.setText(Double.toString(productSummary));
        }
    }

    //添加商品
    public class AddTask extends AsyncTask<Sale, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Sale... sale) {
            return new SalesAction().add(sale[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true)
            {
                reloadList();

            }
        }
    }

    //清空列表
    public class ClearTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... userid) {
            return new SalesAction().clear(user.getId(), user.getClerkOf());

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true)
            {
                reloadList();

            }
            else
            {
                Toast.makeText(SalesActivity.this, "清空列表失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //生成订单
    public class AddOrderTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return new SalesAction().createOrder(user.getId(), user.getClerkOf());

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true)
            {
                Toast.makeText(SalesActivity.this, "提交成功", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(SalesActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_order, menu);
        super.onCreateOptionsMenu(menu);
        //添加菜单项
        clearMenuItem = menu.add(0, StaticValues.MENU_CLEAR_PRODUCTS,0,"清空商品");
        clearMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        saleHistoryMenuItem = menu.add(0, StaticValues.MENU_SALE_HISTORY,0,"销售历史");
        saleHistoryMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
            case StaticValues.MENU_CLEAR_PRODUCTS:
                AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
                builder.setMessage("是否清空商品列表");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new ClearTask().execute();

                    }
                });
                builder.setNegativeButton("取消", null);

                builder.create().show();

                break;

            case StaticValues.MENU_SALE_HISTORY:
                Intent intent = new Intent(SalesActivity.this, SalesHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", StaticValues.SALE_HISTORY_TYPE_CLERK);
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case android.R.id.home:
                this.onBackPressed();

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}
