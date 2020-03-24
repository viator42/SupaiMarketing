package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CartAction;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.model.Collection;
import im.supai.supaimarketing.model.MapMarker;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;
import im.supai.supaimarketing.R;

public class ProductActivity extends AppCompatActivity {

    protected TextView nameTextView;
    protected TextView descriptionTextView;
    protected TextView merchantTextView;
    protected TextView originTextView;
    protected TextView priceTextView;
    protected EditText countEditText;
    protected Button addToCartBtn;
    protected ImageView imgView;

    protected Button editBtn;
    protected Button addFavouriteBtn;
    private User user;
    private Product product;
    protected long id;
    private boolean editable = false;
    protected AppContext context;
    protected LinearLayout actionsLayout;

    private TimeoutbleProgressDialog addToCartDialog;
    private TimeoutbleProgressDialog addToCollectDialog;

    protected GridLayout goodsInfoLayout;

//    private android.support.v7.app.ActionBar actionBar;
    private MenuItem addFavouriteMenuItem;
    private MenuItem removeFavouriteMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        addToCartDialog = TimeoutbleProgressDialog.createProgressDialog(ProductActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                addToCartDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setMessage("添加到购物车失败");
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

        addToCollectDialog =  TimeoutbleProgressDialog.createProgressDialog(ProductActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                addToCollectDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setMessage("添加收藏失败");
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

        context = (AppContext)getApplicationContext();
        user = context.getUser();

        nameTextView = (TextView) findViewById(R.id.alias);
        descriptionTextView = (TextView) findViewById(R.id.description);
        merchantTextView = (TextView) findViewById(R.id.merchant);
        originTextView = (TextView) findViewById(R.id.origin);
        priceTextView = (TextView) findViewById(R.id.price);
        addToCartBtn = (Button) findViewById(R.id.add_to_cart);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加到购物车
                new AddToCartTask().execute(product.getId());

            }
        });
        imgView = (ImageView) findViewById(R.id.img);
        countEditText = (EditText) findViewById(R.id.count);
        actionsLayout = (LinearLayout)findViewById(R.id.actions);
        goodsInfoLayout = (GridLayout) findViewById(R.id.goods_info);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getLong("id");
        if(id != 0)
        {
            new ProductDetailTask().execute(id);

        }
        else
        {
            product = bundle.getParcelable("obj");
            editable = bundle.getBoolean("editable");

            //属性赋值
            setValues();
        }




    }

    private void setValues()
    {
        nameTextView.setText(product.getAlias());
        if(CommonUtils.isValueEmpty(product.getAdditional()))
        {
            descriptionTextView.setText("该商品尚未添加描述");
        }
        else
        {
            descriptionTextView.setText(product.getAdditional());
        }

        merchantTextView.setText(product.getMerchant());
        originTextView.setText(product.getOrigin());
        priceTextView.setText(Double.toString(product.getPrice()));

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_product) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_product) // resource or drawable
//                .showImageOnFail(R.drawable.ic_product) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(product.getImgUrl()), imgView, options);

        if(product.getGoodsId() == 0)
        {
            goodsInfoLayout.setVisibility(View.GONE);
        }

        //添加收藏/取消收藏
//        Button addFavouriteBtn = new Button(this);
//        addFavouriteBtn.setTextColor(getResources().getColor(R.color.button_text));
//        addFavouriteBtn.setBackgroundColor(getResources().getColor(R.color.button));
//        addFavouriteBtn.setPadding(8, 8, 8, 8);
//
//        switch (product.getFavourite()) {
//            case 0:
//                addFavouriteBtn.setText("收藏商品");
//                addFavouriteBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        new AddToFavouriteTask().execute();
//                    }
//                });
//                actionsLayout.addView(addFavouriteBtn);
//                break;
//            case 1:
//                addFavouriteBtn.setText("取消收藏");
//                addFavouriteBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //点击地图位置
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
//                        builder.setMessage("确定取消收藏？");
//                        builder.setTitle("提示");
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                new UnfavouriteTask().execute();
//
//                            }
//                        });
//                        builder.setNegativeButton("取消", null);
//                        builder.create().show();
//
//                    }
//                });
//                actionsLayout.addView(addFavouriteBtn);
//                break;
//            case 2:
//                addFavouriteBtn.setVisibility(View.INVISIBLE);
//                break;
//        }
    }

    //获取商品详情任务,只传入id的时候使用
    public class ProductDetailTask extends AsyncTask<Long, Void, Product>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Product doInBackground(Long... id) {
            Product result = new ProductAction().detail(ProductActivity.this, id[0], false);

            //获取图片
            try {
//                result.setImg(HttpClientUtil.getImageFromWeb(result.getImgUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Product result) {
            super.onPostExecute(result);
            product = result;

            setValues();

        }
    }


    //添加到购物车任务
    public class AddToCartTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addToCartDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            AppContext context = (AppContext)ProductActivity.this.getApplication();
            long userid = context.getUser().getId();
            long storeId = product.getStoreId();
            int count = Integer.valueOf(countEditText.getText().toString());
            double price = product.getPrice();

            return new CartAction().addToCart(userid, storeId, id[0], count, price);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            addToCartDialog.dismiss();
            if(result != null)
            {
                if(result)
                {
                    Toast.makeText(ProductActivity.this, "", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                    builder.setMessage("商品已添加到购物车");
                    builder.setTitle("提示");
                    builder.setPositiveButton("打开购物车", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                            startActivity(intent);
                            ProductActivity.this.finish();

                        }
                    });
                    builder.setNegativeButton("继续购物", null);
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(ProductActivity.this, "商品添加失败", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    //添加到收藏夹
    public class AddToFavouriteTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addToCollectDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            Boolean result = false;
            result =  new ProductAction().addFavourite(user.getId(), product.getId());
            //获取用户收藏任务
            context.setCollection(new CollectAction().all(ProductActivity.this, user.getId(), true));
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            addToCollectDialog.dismiss();
            if(result)
            {
                Toast.makeText(ProductActivity.this, "已添加到收藏", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(ProductActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //取消收藏
    public class UnfavouriteTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            Boolean result = false;
            result =  new ProductAction().unfavourite(user.getId(), product.getId());
            //获取用户收藏任务
            context.setCollection(new CollectAction().all(ProductActivity.this, user.getId(), true));
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                Toast.makeText(ProductActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(ProductActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addFavouriteMenuItem = menu.add(0, StaticValues.MENU_ADD_FAVOURITE,0,"收藏商品");
        removeFavouriteMenuItem = menu.add(0, StaticValues.MENU_REMOVE_FAVOURITE,0,"取消收藏");

        addFavouriteMenuItem.setIcon(R.drawable.ic_menu_favoutire);
        removeFavouriteMenuItem.setIcon(R.drawable.ic_menu_unfavourite);

        addFavouriteMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        removeFavouriteMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        //显示收藏/取消收藏
        switch (product.getFavourite())
        {
            case 0:
                addFavouriteMenuItem.setVisible(true);
                removeFavouriteMenuItem.setVisible(false);


                break;
            case 1:
                removeFavouriteMenuItem.setVisible(true);
                addFavouriteMenuItem.setVisible(false);


                break;
            case 2:
                addFavouriteMenuItem.setVisible(false);
                removeFavouriteMenuItem.setVisible(false);

                break;
        }

        if(editable)
        {
            addFavouriteMenuItem.setVisible(false);
            removeFavouriteMenuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);

        switch (id)
        {
            case StaticValues.MENU_ADD_FAVOURITE:
                new AddToFavouriteTask().execute();

                break;

            case StaticValues.MENU_REMOVE_FAVOURITE:
                //点击地图位置
                builder.setMessage("确定取消收藏？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new UnfavouriteTask().execute();

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
}
