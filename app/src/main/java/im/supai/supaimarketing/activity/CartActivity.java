package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import im.supai.supaimarketing.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CartAction;
import im.supai.supaimarketing.action.ImageAction;
import im.supai.supaimarketing.adapter.CartAdaper;
import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.model.CartDetail;
import im.supai.supaimarketing.model.Image;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

import android.app.ProgressDialog;

public class CartActivity extends AppCompatActivity {

    private long userid;
    private LinearLayout cartContainer;
    private ProgressDialog dialog;

    private FrameLayout warningLayout;
    private TextView warningTextView;

    private SwipeRefreshLayout swipeRefreshLayout;

    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            //获取Message消息
            if(msg.what == StaticValues.REMOVE_CART_PRODUCT)
            {
                Toast.makeText(CartActivity.this, "商品已移除", Toast.LENGTH_SHORT).show();

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartContainer = (LinearLayout)findViewById(R.id.cart_container);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        AppContext context = (AppContext)this.getApplication();
        userid = context.getUser().getId();

        dialog = new ProgressDialog(this);
        dialog.setMessage("读取中...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);

        new GetCartListTask().execute(userid);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new GetCartListTask().execute(userid);

            }
        });
    }

    //获取购物车任务
    public class GetCartListTask extends AsyncTask<Long, Void, List<Cart>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected  List<Cart> doInBackground(Long... id) {
            return new CartAction().list(CartActivity.this, id[0], true);

        }

        @Override
        protected void onPostExecute( List<Cart> result) {
            super.onPostExecute(result);

            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            if(result.isEmpty())
            {
                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("购物车内没有商品");

            }
            cartContainer.removeAllViewsInLayout();

            //显示到UI
            for(final Cart cart: result)
            {
                double summary = 0;

                //Store名称
                TextView textView = new TextView(CartActivity.this);
                textView.setText(cart.getStoreName());
                textView.setTextAppearance(CartActivity.this, android.R.style.TextAppearance_Medium);
                textView.setPadding(8,8,8,8);
                cartContainer.addView(textView);

                //购物车商品信息
                ListView listView = new ListView(CartActivity.this);

                List listData = new ArrayList<Map<String,Object>>();
                for(CartDetail cartDetail: cart.getDetailList())
                {
                    Map line = new HashMap();

                    line.put("id", cartDetail.getId());
                    line.put("name", cartDetail.getGoodsName());
                    line.put("price", cartDetail.getPrice());
                    line.put("count", cartDetail.getCount());
                    line.put("img", cartDetail.getImgUrl());

                    listData.add(line);

                    summary += cartDetail.getPrice() * cartDetail.getCount();
                }

                CartAdaper adapter = new CartAdaper(listData, CartActivity.this);
                listView.setAdapter(adapter);

                listView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, cart.getDetailList().size() * 180));

                cartContainer.addView(listView);

                //总价
                TextView priceView = new TextView(CartActivity.this);
                priceView.setText("总价 " + Double.toString(summary));
                priceView.setTextAppearance(CartActivity.this, android.R.style.TextAppearance_Medium);
                priceView.setPadding(8, 8, 8, 8);
                priceView.setGravity(Gravity.RIGHT);
                cartContainer.addView(priceView);

                //结算button
                Button createBtn = new Button(CartActivity.this);
                createBtn.setText("结算");
                createBtn.setTextColor(getResources().getColor(R.color.button_text));
                createBtn.setBackgroundColor(getResources().getColor(R.color.btn_active));
                createBtn.setPadding(8, 8, 8, 8);
                createBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartActivity.this, CartConfirmActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("cartId", cart.getId());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
                cartContainer.addView(createBtn);
            }

        }
    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        // back键回退至主界面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(CartActivity.this, MainActivity.class);
//            startActivity(intent);
            this.finish();

        }
        return true;
    }

    //重新读取购物车
    public void reloadCartInfo()
    {
        new GetCartListTask().execute(userid);

    }
}
