package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import im.supai.supaimarketing.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CartAction;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.PayOptions;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class CartConfirmActivity extends AppCompatActivity {

    private AppContext appContext;
    private TextView customerNameTextView;
    private TextView customerTelTextView;
    private EditText addressEditText;
    private EditText additionalEditText;
    private Button cancelBtn;
    private Button confitmBtn;
    private User user;
    private long cartId;
    private String address;
    private String additional;
    private int payMethod = 1;
    private int paid = 1;
    private int payAfter = 1;
    private RadioButton payDirectRadioButton;
    private RadioButton alipayRadioButton;
    private CheckBox payAfterCheckBox;

    private TimeoutbleProgressDialog createOrderDialog;

    private boolean payAfterSupport = false;

    private TableRow payAfterSupportContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_confirm);
        appContext = (AppContext) getApplicationContext();

        customerNameTextView = (TextView) findViewById(R.id.customer_name);
        customerTelTextView = (TextView) findViewById(R.id.customer_tel);
        addressEditText = (EditText) findViewById(R.id.address);
        additionalEditText = (EditText) findViewById(R.id.additional);
        cancelBtn = (Button) findViewById(R.id.cancel);
        confitmBtn = (Button) findViewById(R.id.confirm);
        payAfterSupportContainer = (TableRow) findViewById(R.id.pay_after_support_container);
        payAfterSupportContainer.setVisibility(View.GONE);

        payDirectRadioButton = (RadioButton) findViewById(R.id.direct_pay);
        alipayRadioButton = (RadioButton) findViewById(R.id.alipay);
        payAfterCheckBox = (CheckBox) findViewById(R.id.pay_after);

        user = appContext.getUser();

        createOrderDialog = TimeoutbleProgressDialog.createProgressDialog(CartConfirmActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                createOrderDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(CartConfirmActivity.this);
                builder.setMessage("添加失败");
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

        Bundle bundle = this.getIntent().getExtras();
        cartId = bundle.getLong("cartId");

        new GetPayOptionsTask().execute(cartId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customerNameTextView.setText(user.getName());
        customerTelTextView.setText(user.getTel());
        addressEditText.setText(user.getAddress());
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confitmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = addressEditText.getText().toString();
                additional = additionalEditText.getText().toString();
                if (address.isEmpty()) {
                    Toast.makeText(CartConfirmActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //提交订单
                AlertDialog.Builder builder = new AlertDialog.Builder(CartConfirmActivity.this);
                builder.setMessage("是否提交订单?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new CreateOrderTask().execute();

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        payDirectRadioButton.setChecked(true);
        alipayRadioButton.setChecked(false);

        //在线支付选项
        alipayRadioButton.setVisibility(View.GONE);

        payDirectRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    payDirectRadioButton.setChecked(true);
                    alipayRadioButton.setChecked(false);
                    payMethod = 1;
                }
            }
        });

        alipayRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    alipayRadioButton.setChecked(true);
                    payDirectRadioButton.setChecked(false);
                    payMethod = 2;
                }
            }
        });

        payAfterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    payAfter = 2;
                }
                else
                {
                    payAfter = 1;
                }
            }
        });

    }

    //购物车结算任务
    public class CreateOrderTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createOrderDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            Order order = new Order();
            order.setCustomerAddress(address.trim());
            order.setAdditional(additional.trim());
            order.setPayMethod(payMethod);
            order.setPaid(paid);
            order.setPayAfter(payAfter);
            return new CartAction().createOrder(cartId, order);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            createOrderDialog.dismiss();
            if(result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartConfirmActivity.this);
                builder.setMessage("订单已生成,点击查看");
                builder.setTitle("提示");
                builder.setPositiveButton("查看订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CartConfirmActivity.this, MyOrderActivity.class);
                        startActivity(intent);
                        CartConfirmActivity.this.finish();

                    }
                });
                builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CartConfirmActivity.this.finish();
                    }
                });
                builder.create().show();

            }
            else
            {
                Toast.makeText(CartConfirmActivity.this, "生成订单失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //支付方法获取
    public class GetPayOptionsTask extends AsyncTask<Long, Void, PayOptions>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected PayOptions doInBackground(Long... cartId) {
            return new CartAction().getPayOptions(cartId[0]);

        }

        @Override
        protected void onPostExecute(PayOptions result) {
            if(result != null)
            {
                payAfterSupport = result.isPayAfterSupport();
                if(payAfterSupport)
                {
                    payAfterSupportContainer.setVisibility(View.VISIBLE);

                }
            }
        }
    }
}
