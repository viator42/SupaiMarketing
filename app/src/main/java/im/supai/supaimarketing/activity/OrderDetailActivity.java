package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import im.supai.supaimarketing.action.PrinterAction;
import im.supai.supaimarketing.adapter.MyOrderProductAdaper;
import im.supai.supaimarketing.model.MapMarker;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.OrderDetail;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;
import im.supai.supaimarketing.R;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView orderTimeTextView;
    private TextView storeNameTextView;
    private TextView statusTextView;
    private TextView nameTextView;
    private TextView telTextView;
    private TextView orderSnTextView;
    private TextView summaryTextView;
    private TextView addressTextView;
    private TextView additionalTextView;

    private ListView productListView;
    private Order order;
    private int orderType = 1;

    private Button cancelBtn;
    private Button confirmBtn;
    private Button positionBtn;
    private Button deliveredBtn;
    private Button printBtn;
    private Button payOrderBtn;

    private AlertDialog.Builder builder;

    private TimeoutbleProgressDialog getProductsDialog;

    private AppContext context;

    private HashMap modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        context = (AppContext) getApplicationContext();

        modules = context.getModules();

        //获取参数
        Bundle bundle = this.getIntent().getExtras();
        order = bundle.getParcelable("obj");
        orderType = bundle.getInt("type");

        getProductsDialog = TimeoutbleProgressDialog.createProgressDialog(OrderDetailActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getProductsDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setMessage("未获取到订单详情");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                        OrderDetailActivity.this.finish();

                    }
                });

                builder.create().show();
            }
        });

        orderTimeTextView = (TextView)findViewById(R.id.order_time);
        storeNameTextView = (TextView)findViewById(R.id.store_name);
        statusTextView = (TextView)findViewById(R.id.status);
        productListView = (ListView)findViewById(R.id.product_list);
        nameTextView = (TextView)findViewById(R.id.alias);
        telTextView = (TextView)findViewById(R.id.tel);
        orderSnTextView = (TextView) findViewById(R.id.order_sn);
        summaryTextView  = (TextView) findViewById(R.id.summary);
        addressTextView = (TextView) findViewById(R.id.address);
        additionalTextView = (TextView) findViewById(R.id.additional);

        cancelBtn = (Button) findViewById(R.id.cancel);
        confirmBtn = (Button) findViewById(R.id.confirm);
        deliveredBtn = (Button) findViewById(R.id.delivered);
        printBtn = (Button) findViewById(R.id.print);
        positionBtn = (Button) findViewById(R.id.position);
        payOrderBtn = (Button) findViewById(R.id.pay_order);

        cancelBtn.setVisibility(View.GONE);
        confirmBtn.setVisibility(View.GONE);
        deliveredBtn.setVisibility(View.GONE);
        printBtn.setVisibility(View.GONE);
        positionBtn.setVisibility(View.GONE);
        payOrderBtn.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setValue();

        //操作Button设置
        switch (order.getStatus())
        {
            case StaticValues.ORDER_STATUS_UNPAID:
                //未支付
                if(orderType == StaticValues.ORDER_TYPE_CUSTOMER)
                {
                    payOrderBtn.setVisibility(View.VISIBLE);
                    payOrderBtn.setOnClickListener(PayOrderListener);

                }

                break;
            case StaticValues.ORDER_STATUS_READY:
            case StaticValues.ORDER_STATUS_DELIVERING:
                //已发货(发送通知)
                cancelBtn.setVisibility(View.VISIBLE);
                if(orderType == StaticValues.ORDER_TYPE_CUSTOMER)
                {
                    cancelBtn.setText("申请退货");
                    cancelBtn.setOnClickListener(cancelApplyListener);
                    deliveredBtn.setVisibility(View.VISIBLE);
                    deliveredBtn.setOnClickListener(DeliveredListener);

                }
                if(orderType == StaticValues.ORDER_TYPE_MERCHANT)
                {

                    cancelBtn.setText("取消订单");
                    cancelBtn.setOnClickListener(cancelOrderListener);

                }

                positionBtn.setVisibility(View.VISIBLE);
                positionBtn.setOnClickListener(PositionListener);

                break;
            case StaticValues.ORDER_STATUS_SUCCEED:
                //交易成功
                if(orderType == StaticValues.ORDER_TYPE_CUSTOMER)
                {
                    payOrderBtn.setVisibility(View.VISIBLE);
                    payOrderBtn.setOnClickListener(PayOrderListener);

                }
                if(orderType == StaticValues.ORDER_TYPE_MERCHANT)
                {



                }
                //未支付的显示支付按钮

                break;
            case StaticValues.ORDER_STATUS_CLOSED:
                //交易关闭
                break;
            case StaticValues.ORDER_STATUS_RETURN_APPLY:
                //申请退货
                positionBtn.setVisibility(View.VISIBLE);
                positionBtn.setOnClickListener(PositionListener);

                if(orderType == StaticValues.ORDER_TYPE_MERCHANT)
                {
                    cancelBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setText("退货确认");
                    cancelBtn.setOnClickListener(AcceptCancelListener);
                }

                break;
            default:
                break;

        }

        if(modules != null && !modules.isEmpty())
        {
            //打印订单
            if(orderType == StaticValues.ORDER_TYPE_MERCHANT)
            {
                printBtn.setVisibility(View.VISIBLE);
                printBtn.setOnClickListener(PrintListener);
            }

        }

    }

    private void setValue()
    {
        orderTimeTextView.setText(CommonUtils.timestampToDate(order.getCreateTime()));
        storeNameTextView.setText(order.getStoreName());
        statusTextView.setText(order.getStatusName());

        orderSnTextView.setText(order.getSn());
        summaryTextView.setText(Double.toString(order.getSummary()));

        if(order.getAdditional() != null && !order.getAdditional().isEmpty() && !order.getAdditional().equals("null"))
        {
            additionalTextView.setText(order.getAdditional());
        }

        switch (orderType)
        {
            case StaticValues.ORDER_TYPE_CUSTOMER:
                nameTextView.setText(order.getMerchangName());
                telTextView.setText(order.getMerchantTel());
                addressTextView.setText(order.getMerchantAddress());

                break;
            case StaticValues.ORDER_TYPE_MERCHANT:
                nameTextView.setText(order.getCustomerName());
                telTextView.setText(order.getCustomerTel());
                addressTextView.setText(order.getCustomerAddress());

                break;
        }

        new OrderDetailTask().execute(order.getId());
        new setReadedTask().execute(order.getId());

    }

    //------------------------------------Button listener------------------------------------
    //直接取消订单
    private Button.OnClickListener cancelOrderListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(OrderDetailActivity.this);
            builder.setMessage("是否取消该订单?");
            builder.setTitle("提示");
            builder.setPositiveButton("取消订单", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new CancelOrderTask().execute(order.getId());
                }
            });
            builder.setNegativeButton("关闭窗口", null);
            builder.create().show();
        }
    };

    //申请取消订单
    private Button.OnClickListener cancelApplyListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
            builder.setMessage("是否申请取消该订单?");
            builder.setTitle("提示");
            builder.setPositiveButton("申请取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new CancelApplyTask().execute(order.getId());
                }
            });
            builder.setNegativeButton("关闭窗口", null);
            builder.create().show();
        }
    };

    //订单取消对应
    private Button.OnClickListener AcceptCancelListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(OrderDetailActivity.this);
            builder.setMessage("是否同意客户取消订单的请求?");
            builder.setTitle("提示");
            builder.setPositiveButton("取消订单", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new ReturnOrderTask().execute(StaticValues.ORDER_RETURN_ACCEPT);

                }
            });
            builder.setNegativeButton("拒绝请求", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new ReturnOrderTask().execute(StaticValues.ORDER_RETURN_REJECT);

                }
            });
            builder.create().show();
        }
    };

    //订单确认
    private Button.OnClickListener ConfirmListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(OrderDetailActivity.this);
            builder.setMessage("是否将订单设置为已发货?");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new ConfirmOrderTask().execute(order.getId());
                }
            });
            builder.setNegativeButton("关闭", null);
            builder.create().show();
        }
    };

    //确认收货
    private Button.OnClickListener DeliveredListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(OrderDetailActivity.this);
            builder.setMessage("是否确认收货?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认收货", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new DeliveredOrderTask().execute(order.getId());
                }
            });
            builder.setNegativeButton("关闭窗口", null);
            builder.create().show();
        }
    };

    //位置确认
    private Button.OnClickListener PositionListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(CommonUtils.isPositionAvalible(order.getMerchantLongitude(), order.getMerchantLatitude()))
            {
                MapMarker marker = new MapMarker();
                marker.setTitle(order.getStoreName());
                marker.setSnippet("");
                marker.setLongitute(order.getMerchantLongitude());
                marker.setLatitude(order.getMerchantLatitude());

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", marker);
                intent.putExtras(bundle);
                intent.setClass(OrderDetailActivity.this, MapActivity.class);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, "未取到位置信息", Toast.LENGTH_SHORT).show();

            }
        }
    };

    //打印
    private Button.OnClickListener PrintListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
//            new PrinterAction(OrderDetailActivity.this, context).printOrder(order);
        }
    };

    //支付订单
    private Button.OnClickListener PayOrderListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };

    //设置为已支付
    private Button.OnClickListener SetPaidListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(OrderDetailActivity.this);
            builder.setMessage("是否将该订单设置为已支付?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认支付", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new setOrderPaidTask().execute(order.getId());
                }
            });
            builder.setNegativeButton("关闭窗口", null);
            builder.create().show();

        }
    };



    //------------------------------------Button listener------------------------------------

    //获取订单详情任务
    public class OrderDetailTask extends AsyncTask<Long, Void, Order>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getProductsDialog.show();
        }

        @Override
        protected Order doInBackground(Long... id) {
            return new OrderAction().detail(OrderDetailActivity.this, id[0], false);

        }

        @Override
        protected void onPostExecute(final Order result) {
            getProductsDialog.dismiss();
            if(result != null)
            {
                //订单商品列表
                List listData = new ArrayList<Map<String,Object>>();
                for(OrderDetail orderDetail: result.getOrderDetails())
                {
                    Map line = new HashMap();

                    line.put("id", orderDetail.getId());
                    line.put("productId", orderDetail.getProductId());
                    line.put("name", orderDetail.getGoodsName());
                    line.put("price", orderDetail.getPrice());
                    line.put("count", orderDetail.getCount());
                    line.put("img", orderDetail.getImgUrl());

                    listData.add(line);

                }
                MyOrderProductAdaper adapter = new MyOrderProductAdaper(listData, OrderDetailActivity.this);
                productListView.setAdapter(adapter);
                productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                        //跳转到详情页面
                        OrderDetail orderDetail;
                        for (int a = 0; a < result.getOrderDetails().size(); a++) {
                            orderDetail = result.getOrderDetails().get(a);
                            if (orderDetail.getProductId() == id) {
                                Intent intent = new Intent(OrderDetailActivity.this, ProductActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("id", id);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        }

                    }
                });

            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, "获取订单详情失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //订单取消申请
    public class CancelApplyTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new OrderAction().returnApply(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setMessage("订单取消申请已提交");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        OrderDetailActivity.this.finish();

                    }
                });
                builder.create().show();

            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, "申请提交失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //订单取消确认任务
    public class ReturnOrderTask extends AsyncTask<Integer, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... option) {
            return new OrderAction().returnOrder(order.getId(), option[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                Intent intent = new Intent();
                intent.setClass(OrderDetailActivity.this, MainActivity.class);
                OrderDetailActivity.this.startActivity(intent);
                OrderDetailActivity.this.finish();
                //Toast.makeText(OrderDetailForMerchantActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, "订单操作失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //确认收货任务
    public class DeliveredOrderTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new OrderAction().delivered(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                //收货按钮隐藏
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setMessage("收货确认设置成功");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        OrderDetailActivity.this.finish();

                    }
                });
                builder.create().show();

            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, "订单设置失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //订单取消任务
    public class CancelOrderTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new OrderAction().cancel(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                Intent intent = new Intent();
                intent.setClass(OrderDetailActivity.this, MainActivity.class);
                OrderDetailActivity.this.startActivity(intent);
                OrderDetailActivity.this.finish();
                //Toast.makeText(OrderDetailForMerchantActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, "取消订单失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //订单发货任务
    public class ConfirmOrderTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new OrderAction().confirm(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                Toast.makeText(OrderDetailActivity.this, "订单设置为已发货", Toast.LENGTH_SHORT).show();
                //订单发货按钮隐藏

            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, "订单设置失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //设置为已读任务
    public class setReadedTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new OrderAction().setReaded(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                context.getOrders().unreadMinusOne();

            }

        }
    }

    //设置订单为已支付
    public class setOrderPaidTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new OrderAction().setPaid(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                context.getOrders().unreadMinusOne();

            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode,KeyEvent event) {

        OrderDetailActivity.this.finish();

        return false;
    }
}
