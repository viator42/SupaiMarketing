package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.RefAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.model.MapMarker;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.NewProductChooserDialog;
import im.supai.supaimarketing.widget.ScrollViewX;
import im.supai.supaimarketing.widget.StoreDescriptionDialog;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

public class StoreActivity extends AppCompatActivity {
    private ImageButton addProductBtn;
    protected long id;
    protected User user;
    protected AppContext context;
    protected GridLayout storeProductsGrid;
    protected Store store;

    protected TextView nameTextView;
    protected TextView addressTextView;
    protected ImageView logoImageView;
    //    protected ImageButton locateImageBtn;
//    protected Button functionalBtn;
    protected ScrollViewX storeScrollView;

    private boolean editable = false;

    protected ArrayList<Product> products;

    private TimeoutbleProgressDialog getProductsDialog;

//    private BadgeView badge;
    private int page;

    //是否滚动加载数据
    private boolean appendFlg = false;

    private TimeoutbleProgressDialog addToCollectDialog;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;

    private MenuItem searchMenuItem;
    private MenuItem scanMenuItem;
    private MenuItem locateMenuItem;
    private MenuItem addFavouriteMenuItem;
    private MenuItem removeFavouriteMenuItem;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        getProductsDialog = TimeoutbleProgressDialog.createProgressDialog(StoreActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getProductsDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
                builder.setMessage("未获取到产品信息");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        StoreActivity.this.finish();

                    }
                });

                builder.create().show();
            }
        });

        addToCollectDialog = TimeoutbleProgressDialog.createProgressDialog(StoreActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                addToCollectDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
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

        Bundle bundle = this.getIntent().getExtras();
//        id = bundle.getLong("id");
        store = bundle.getParcelable("obj");

        try
        {
            editable = bundle.getBoolean("editable");

        }catch (Exception e)
        {
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new ListProductsTask().execute(store.getId());

            }
        });

        storeProductsGrid = (GridLayout)findViewById(R.id.store_products_grid);
        nameTextView = (TextView)findViewById(R.id.alias);
        addressTextView = (TextView)findViewById(R.id.address);
        logoImageView = (ImageView)findViewById(R.id.logo);
        //店铺头像被点击
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StoreDescriptionDialog(StoreActivity.this, store).show();

            }
        });

//        functionalBtn = (Button)findViewById(R.id.functional);
//        functionalBtn.setTextColor(getResources().getColor(R.color.button_text));
//        functionalBtn.setBackgroundColor(getResources().getColor(R.color.button));
        storeScrollView = (ScrollViewX) findViewById(R.id.store_scroll);
        storeScrollView.setOnScrollListener(new ScrollViewX.OnScrollListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                if (storeScrollView.isAtBottom())
                {
                    appendFlg = true;
                    new ListProductsTask().execute(store.getId());

                }
            }

            @Override
            public void onScrollStopped() {

            }

            @Override
            public void onScrolling() {

            }
        });

    }

    public void onStart()
    {
        super.onStart();
        //删除
        storeProductsGrid.removeAllViews();

//        if(store.getUserid() == user.getId())
//        {
//            addToFavouriteBtn.setVisibility(View.INVISIBLE);
//        }

        //检测是否允许修改
        if(editable)
        {
            //添加商品
            addProcuctIcon();

//            functionalBtn.setText("店铺修改");
//            functionalBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(StoreActivity.this, StoreEditActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("obj", store);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, StaticValues.STORE_EDIT);
//                }
//            });


        }
//        else
//        {
//            //添加收藏/取消收藏
//            switch (store.getFavourite())
//            {
//                case 0:
//                    functionalBtn.setText("收藏店铺");
//                    functionalBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
//                            builder.setMessage("是否收藏此店铺？");
//                            builder.setTitle("提示");
//                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    new AddToFavouriteTask().execute();
//                                }
//                            });
//                            builder.setNegativeButton("取消", null);
//                            builder.create().show();
//                        }
//                    });
//                    break;
//                case 1:
//                    functionalBtn.setText("取消收藏");
//                    functionalBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
//                            builder.setMessage("是否取消收藏？");
//                            builder.setTitle("提示");
//                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    new UnfavouriteTask().execute();
//                                }
//                            });
//                            builder.setNegativeButton("取消", null);
//                            builder.create().show();
//                        }
//                    });
//                    break;
//                case 2:
//                    functionalBtn.setVisibility(View.INVISIBLE);
//                    break;
//            }
//
//        }

        setValues();

        page = 0;

        appendFlg = false;

        //pruduct list
        new ListProductsTask().execute(store.getId());

    }

    public void setValues()
    {
        nameTextView.setText(store.getName());
        addressTextView.setText(store.getAddress());

        //设置店铺logo
//        ImageLoader.getInstance().loadImage(CommonUtils.getImgFullpath(store.getLogoUrl()), new SimpleImageLoadingListener() {
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view,
//                                          Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                logoImageView.setImageBitmap(loadedImage);
//            }
//
//        });
//
//        //闭店icon
//        if(store.getStatus() == 2)
//        {
//            badge = new BadgeView(StoreActivity.this, logoImageView);
//            badge.setText("店铺关闭");
//            badge.show();
//
//        }
    }

//    public void onRestart()
//    {
//        super.onRestart();
//
//        //重新读取数据
//        new ListProductsTask().execute(store.getId());
//
//    }

    //添加商品Icon
    public void addProcuctIcon()
    {
        ImageView imgView;
        TextView titleTextView;
        Bitmap img;

        LinearLayout itemLayout = new LinearLayout(StoreActivity.this);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getIconWidth(), context.getIconHeight()));
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        final ImageView itemBtn = new ImageView(StoreActivity.this);
//        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_add_product , itemBtn);
//        ImageLoader.getInstance().loadImage("drawable://" + R.drawable.ic_add_product, new SimpleImageLoadingListener() {
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view,
//                                          Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                itemBtn.setImageBitmap(loadedImage);
//            }
//
//        });

        itemBtn.setAdjustViewBounds(true);
        itemBtn.setMaxWidth((context.getScreenWidth() - StaticValues.margin) / 3);
        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewProductChooserDialog(StoreActivity.this).show();

            }
        });
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(context.getIconWidth(), context.getIconHeight() - 40);
        itemBtn.setLayoutParams(para);
        itemBtn.setPadding(8, 8, 8, 8);
        itemLayout.addView(itemBtn);

        titleTextView = new TextView(StoreActivity.this);
        titleTextView.setText("添加商品");
        titleTextView.setGravity(Gravity.CENTER);
        itemLayout.addView(titleTextView);

        storeProductsGrid.addView(itemLayout);

    }

    //获取商铺信息
    /*
    public class StoreDetailTask extends AsyncTask<Long, Void, Store>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Store doInBackground(Long... id) {
            //获取store id
            return new StoreAction().detail(id[0]);

        }

        @Override
        protected void onPostExecute(Store result) {
            super.onPostExecute(result);
            //更新UI
            nameTextView.setText(result.getName());
            addressTextView.setText(result.getAddress());
            byte[] byteArray = result.getLogo();
            logoImageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0,
                    byteArray.length));

        }
    }
    */

    //获取商品列表信息
    public class ListProductsTask extends AsyncTask<Long, Void, ArrayList<Product>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!appendFlg)
            {
                getProductsDialog.show();
            }

        }

        @Override
        protected ArrayList<Product> doInBackground(Long... id) {
            return new StoreAction().productList(StoreActivity.this, id[0], page, false);
        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);

            if(!appendFlg)
            {
                getProductsDialog.dismiss();
            }

            products = result;

            if(result.size() == 0 && page > 0)
            {
                Toast.makeText(StoreActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
            }
            else
            {
                for(int a=0; a<result.size(); a++)
                {
                    Product product = result.get(a);

                    StoreActivity.this.setAddProductIconToView(product);

                }
                page += 1;

            }

        }
    }

    //商品Icon
    public void setAddProductIconToView(final Product product)
    {
        ImageView imgView;
        TextView titleTextView;
        Bitmap img;

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getIconWidth(), context.getIconHeight()));

        itemLayout.setOrientation(LinearLayout.VERTICAL);

        final ImageView itemBtn = new ImageView(this);

//        ImageLoader.getInstance().loadImage(product.getImgUrl() , new SimpleImageLoadingListener() {
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view,
//                                          Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                itemBtn.setImageBitmap(loadedImage);
//            }
//
//        });
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_product) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_product) // resource or drawable
//                .showImageOnFail(R.drawable.ic_product) // resource or drawable
//                .build();
//
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(product.getImgUrl()), itemBtn, options);

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", product);
                bundle.putBoolean("editable", editable);
                intent.putExtras(bundle);
                if (editable) {
                    intent.setClass(StoreActivity.this, ProductViewonlyActivity.class);

                } else {
                    intent.setClass(StoreActivity.this, ProductActivity.class);

                }
                startActivity(intent);
            }
        });
        itemBtn.setAdjustViewBounds(true);

        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(context.getIconWidth(), context.getIconHeight() - 40);
        itemBtn.setLayoutParams(para);
        itemBtn.setPadding(8, 8, 8, 8);

        itemLayout.addView(itemBtn);

        titleTextView = new TextView(this);
        titleTextView.setText(product.getAlias());
        titleTextView.setGravity(Gravity.CENTER);

        para = new ViewGroup.LayoutParams(context.getIconWidth(), 40);
        titleTextView.setLayoutParams(para);

        itemLayout.addView(titleTextView);

        if(editable)
        {
            //商品下架标识
            if(product.getStatus() == 2)
            {
//                BadgeView badge = new BadgeView(StoreActivity.this, itemBtn);
//                badge.setText("商品未上架");
//                badge.show();
            }
        }

        storeProductsGrid.addView(itemLayout);

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
            result = new StoreAction().addToFavourite(user.getId(), store.getId());
            //获取用户收藏任务
            context.setCollection(new CollectAction().all(StoreActivity.this, user.getId(), true));
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            addToCollectDialog.dismiss();
            if(result)
            {
                Toast.makeText(StoreActivity.this, "已添加到收藏", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(StoreActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();
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
            boolean result = false;
            result =  new StoreAction().unfavourite(user.getId(), store.getId());
            //获取用户收藏任务
            context.setCollection(new CollectAction().all(StoreActivity.this, user.getId(), true));
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                Toast.makeText(StoreActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(StoreActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //获取店铺信息
    public class GetStoreInfoTask extends AsyncTask<Long, Void, Store>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Store doInBackground(Long... id) {
            return new StoreAction().getInfo(id[0]);
        }

        @Override
        protected void onPostExecute(Store result) {
            if(result != null)
            {
                context.setStore(result);
                new RefAction().setStore(StoreActivity.this, result);

            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode)
        {
            case StaticValues.STORE_EDIT:
                Bundle bundle = data.getExtras();
                store = bundle.getParcelable("obj");

                context.setStore(store);
                //店铺信息保存到ref
                SharedPreferences storeRef = getSharedPreferences("store", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = storeRef.edit();
                editor.putLong("id", store.getId());
                editor.putString("name", store.getName());
                editor.putString("area", store.getArea());
                editor.putString("address", store.getAddress());
                editor.putString("logo", store.getLogoUrl());
                editor.putString("description", store.getDescription());
                editor.putFloat("longitude", (float)store.getLongitude());
                editor.putFloat("longitude", (float) store.getLatitude());

                editor.commit();

                setValues();

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_store, menu);

        searchMenuItem = menu.add(0, StaticValues.MENU_SEARCH,0,"搜索");
        scanMenuItem = menu.add(0, StaticValues.MENU_SCAN,0,"扫描条形码");;
        locateMenuItem = menu.add(0, StaticValues.MENU_LOCATE,0,"查看位置");
        addFavouriteMenuItem = menu.add(0, StaticValues.MENU_ADD_FAVOURITE,0,"收藏店铺");
        removeFavouriteMenuItem = menu.add(0, StaticValues.MENU_REMOVE_FAVOURITE,0,"取消收藏");

        searchMenuItem.setIcon(R.drawable.ic_search);
        scanMenuItem.setIcon(R.drawable.ic_menu_barcode);
        locateMenuItem.setIcon(R.drawable.ic_locate);
        addFavouriteMenuItem.setIcon(R.drawable.ic_menu_favoutire);
        removeFavouriteMenuItem.setIcon(R.drawable.ic_menu_unfavourite);

        searchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        scanMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        locateMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        addFavouriteMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        removeFavouriteMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        //显示收藏/取消收藏
        switch (store.getFavourite())
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);

        switch (id)
        {
            case StaticValues.MENU_SCAN:
                //跳转到商品搜索页面
                intent.setClass(StoreActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bundle.putInt("action", 2);
                bundle.putLong("storeId", store.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case StaticValues.MENU_SEARCH:
//                handleMenuSearch();
                intent.setClass(StoreActivity.this, SearchActivity.class);
                bundle.putInt("type", StaticValues.SEARCH_TYPE_ALIAS);
                bundle.putLong("storeId", store.getId());
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case StaticValues.MENU_ADD_FAVOURITE:

                builder.setMessage("是否收藏此店铺？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new AddToFavouriteTask().execute();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

                break;

            case StaticValues.MENU_REMOVE_FAVOURITE:
                builder.setMessage("是否取消收藏？");
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

            case StaticValues.MENU_LOCATE:
                //地图指示店铺位置
                if(store.getLatitude() == 0 || store.getLongitude() == 0)
                {
                    Toast.makeText(StoreActivity.this, "未获取到店铺位置", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    MapMarker marker = new MapMarker();
                    marker.setId(store.getId());
                    marker.setTitle(store.getName());
                    marker.setSnippet(store.getAddress());
                    marker.setIcon(store.getLogo());
                    marker.setLongitute(store.getLongitude());
                    marker.setLatitude(store.getLatitude());

                    bundle.putParcelable("obj", marker);
                    intent.putExtras(bundle);
                    intent.setClass(StoreActivity.this, MapActivity.class);
                    startActivity(intent);

                }

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
    private EditText edtSeach;
    protected void handleMenuSearch(){
        if(isSearchOpened)
        { //test if the search is open
            actionBar.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            actionBar.setDisplayShowTitleEnabled(true); //show the title in the action bar
            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
            //add the search icon in the action bar
            //mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));
            isSearchOpened = false;

        }
        else
        { //open the search entry
            actionBar.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            actionBar.setCustomView(R.layout.search_bar);//add the custom view
            actionBar.setDisplayShowTitleEnabled(false); //hide the title
            edtSeach = (EditText)actionBar.getCustomView().findViewById(R.id.edtSearch); //the text editor
            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch(v.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
            edtSeach.requestFocus();
            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
            //add the close icon
//            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));
            isSearchOpened = true;

        }
    }

    public void doSearch(String alias)
    {
        if(!alias.isEmpty() && alias != null)
        {
            Intent intent = new Intent(StoreActivity.this, ProductResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("alias", alias);
            bundle.putLong("storeId", store.getId());
            intent.putExtras(bundle);
            startActivity(intent);

        }

    }
    */
}
