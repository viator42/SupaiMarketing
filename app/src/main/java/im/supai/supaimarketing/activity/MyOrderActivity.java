package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.OrderAction;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.fragment.CustomerOrderListFragment;
import im.supai.supaimarketing.fragment.MerchantOrderListFragment;
import im.supai.supaimarketing.model.Orders;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

import android.app.ProgressDialog;
import android.widget.Toast;

import im.supai.supaimarketing.R;

public class MyOrderActivity extends AppCompatActivity
        implements CustomerOrderListFragment.OnFragmentInteractionListener, MerchantOrderListFragment.OnFragmentInteractionListener {
    private ListView orderlistView;
    private ActionBar actionBar;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TimeoutbleProgressDialog getOrdersDialog;
    private long userid;
    private AppContext context;

    private MenuItem historyMenuItem;
    private MenuItem actioveMenuItem;

//    private LocationManagerProxy mLocationManagerProxy;

    private double longitude = 0;
    private double latitude = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    private int currentOrderType = StaticValues.ORDER_TYPE_CUSTOMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        context = (AppContext)getApplicationContext();
        userid = context.getUser().getId();

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new ListOrdersTask().execute(currentOrderType);

            }
        });

//        orderlistView = (ListView)findViewById(R.id.orderlist);
//
//        orderlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //跳转到详情页面
//                Intent intent = new Intent();
//                intent.setClass(MyOrderActivity.this, OrderDetailActivity.class);
//
//                Bundle bundle=new Bundle();
//                bundle.putLong("id", id);
//                intent.putExtras(bundle);
//
//                startActivity(intent);
//
//            }
//        });
//
//        Order order = new Order();
//        order.setCustomerId(1);
//
//        new ListOrdersTask().execute();

    }

    public void onStart()
    {
        super.onStart();
        getOrdersDialog = TimeoutbleProgressDialog.createProgressDialog(MyOrderActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getOrdersDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this);
                builder.setMessage("未获取到订单详情");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        Intent intent = new Intent(MyOrderActivity.this, MainActivity.class);
                        startActivity(intent);
                        MyOrderActivity.this.finish();

                    }
                });

                builder.create().show();
            }
        });

        currentOrderType = StaticValues.ORDER_TYPE_CUSTOMER;
        new ListOrdersTask().execute(currentOrderType);

        init();

    }

    private void init() {
//        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
//        mLocationManagerProxy.requestLocationData(
//                LocationProviderProxy.AMapNetwork, StaticValues.LOCATION_TIME_INTERVAL * 1000, 15, this);
//
//        mLocationManagerProxy.setGpsEnable(true);
    }

    public class ListOrdersTask extends AsyncTask<Integer, Void, Orders>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getOrdersDialog.show();
        }

        @Override
        protected Orders doInBackground(Integer... type) {
            return new OrderAction().activeOrders(MyOrderActivity.this, userid, type[0], 0, 0, false);

        }

        @Override
        protected void onPostExecute(final Orders result) {
            super.onPostExecute(result);

            getOrdersDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            //更新UI
            if(result.getMerchantList() != null)
            {
                actionBar = getActionBar();
                actionBar.removeAllTabs();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                //Customer list Tab
                ActionBar.Tab customerTab = actionBar.newTab();
                customerTab.setIcon(R.drawable.ic_order_in);
                customerTab.setText("收货订单");
                customerTab.setTabListener(new ActionBar.TabListener() {
                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        context.setCurrentOrderTab(StaticValues.ORDER_TAB_CUSTOMER);

                        Fragment fragment = new CustomerOrderListFragment();
                        Bundle bundle = new Bundle();

                        bundle.putParcelableArrayList("orders", result.getCustomerList());
                        bundle.putInt("type", result.getType());

                        fragment.setArguments(bundle);
                        manager = getFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.content, fragment );
                        transaction.commit();
                    }

                    @Override
                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                    }

                    @Override
                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                    }
                });
                actionBar.addTab(customerTab);

                //Merchant list Tab
                ActionBar.Tab merchantTab = actionBar.newTab();
                merchantTab.setIcon(R.drawable.ic_order_out);
                merchantTab.setText("发货订单");
                merchantTab.setTabListener(new ActionBar.TabListener() {
                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        context.setCurrentOrderTab(StaticValues.ORDER_TAB_MERCHANT);

                        Fragment fragment = new MerchantOrderListFragment();
                        Bundle bundle = new Bundle();

                        bundle.putParcelableArrayList("orders", result.getMerchantList());
                        bundle.putInt("type", result.getType());

                        fragment.setArguments(bundle);
                        manager = getFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.content, fragment );
                        transaction.commit();
                    }

                    @Override
                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                    }

                    @Override
                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                    }
                });
                actionBar.addTab(merchantTab);

                switch (context.getCurrentOrderTab())
                {
                    case StaticValues.ORDER_TAB_CUSTOMER:
                        actionBar.selectTab(customerTab);

                        break;
                    case StaticValues.ORDER_TAB_MERCHANT:
                        actionBar.selectTab(merchantTab);

                        break;
                }

                actionBar.selectTab(merchantTab);

            }
            else
            {
                //merchantList为空时不显示Tab bar
                //买家订单
                Fragment fragment = new CustomerOrderListFragment();
                Bundle bundle = new Bundle();

                bundle.putParcelableArrayList("orders", result.getCustomerList());
                bundle.putInt("type", result.getType());

                fragment.setArguments(bundle);
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.content, fragment );
                transaction.commit();

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_order, menu);
        super.onCreateOptionsMenu(menu);
        //添加菜单项
        historyMenuItem = menu.add(0, StaticValues.MENU_ORDER_HISTORY,0,"历史订单");
        actioveMenuItem = menu.add(0, StaticValues.MENU_ACTIVE_ORDER, 0, "当前订单");

        historyMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        actioveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        actioveMenuItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent();

        switch (id)
        {
            case StaticValues.MENU_ORDER_HISTORY:
                historyMenuItem.setVisible(false);
                actioveMenuItem.setVisible(true);

                currentOrderType = StaticValues.ORDER_TYPE_MERCHANT;
                new ListOrdersTask().execute(currentOrderType);
                this.setTitle("历史订单");
                break;
            case StaticValues.MENU_ACTIVE_ORDER:
                actioveMenuItem.setVisible(false);
                historyMenuItem.setVisible(true);

                currentOrderType = StaticValues.ORDER_TYPE_CUSTOMER;
                new ListOrdersTask().execute(currentOrderType);
                this.setTitle("当前订单");
                break;

            case android.R.id.home:
                this.onBackPressed();

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
