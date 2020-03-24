package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
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
import im.supai.supaimarketing.action.OrderAction;
import im.supai.supaimarketing.action.SalesAction;
import im.supai.supaimarketing.adapter.ProductStorageAdapter;
import im.supai.supaimarketing.adapter.SaleHistoryAdapter;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class SalesHistoryActivity extends AppCompatActivity {
    private AppContext context;
    private ListView listview;
    private TimeoutbleProgressDialog getSaleHistoryDialog;
    private int type;
    private User user = null;
    private int page;
    private TimeoutbleProgressDialog getOrdersDialog;

    private FrameLayout warningLayout;
    private TextView warningTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SaleHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        context = (AppContext) getApplicationContext();
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (listview.getLastVisiblePosition() == listview.getCount() - 1) {
                        reloadList();

                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        getSaleHistoryDialog = TimeoutbleProgressDialog.createProgressDialog(SalesHistoryActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getSaleHistoryDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(SalesHistoryActivity.this);
                builder.setMessage("列表读取失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                builder.create().show();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadList();

            }
        });

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        getOrdersDialog = TimeoutbleProgressDialog.createProgressDialog(SalesHistoryActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getOrdersDialog.dismiss();

                Toast.makeText(SalesHistoryActivity.this, "未获取到订单列表", Toast.LENGTH_SHORT).show();

            }
        });

        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");

        user = context.getUser();

        page = 0;

        reloadList();

    }

    public void reloadList()
    {
        new GetSaleHistoryTask().execute();
    }

    //
    public class GetSaleHistoryTask extends AsyncTask<Void, Void, ArrayList<Order>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getSaleHistoryDialog.show();
        }

        @Override
        protected ArrayList<Order> doInBackground(Void... voids) {
            return new SalesAction().orders(type, user.getId(), user.getClerkOf(), page);
        }

        @Override
        protected void onPostExecute(ArrayList<Order> result) {
            getSaleHistoryDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            if(result != null)
            {
                if(result.isEmpty())
                {
                    if(page == 0)
                    {
                        listview.setAdapter(null);

                        //列表为空
                        warningLayout.setVisibility(View.VISIBLE);
                        warningTextView.setText("订单列表为空");
                    }
                    else
                    {
                        Toast.makeText(SalesHistoryActivity.this, "没有更多", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    List listData = new ArrayList<Map<String,Object>>();
                    //显示到UI
                    for(final Order order: result)
                    {
                        Map line = new HashMap();

                        line.put("id", order.getId());
                        line.put("createTime", CommonUtils.timestampToDatetime(order.getCreateTime()));
                        line.put("clerkId", order.getMerchantId());
                        line.put("clerkName", order.getMerchangName());
                        line.put("count", order.getCount());
                        line.put("summary", order.getSummary());

                        listData.add(line);

                    }

                    if(page == 0)
                    {
                        adapter = new SaleHistoryAdapter(listData, SalesHistoryActivity.this);
                        listview.setAdapter(adapter);
                    }
                    else
                    {
                        if(adapter != null)
                        {
                            adapter.addItems(listData);
                        }

                    }

                    page += 1;
                }

            }
            else
            {
                listview.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未取到商品信息");

            }

        }

    }
}
