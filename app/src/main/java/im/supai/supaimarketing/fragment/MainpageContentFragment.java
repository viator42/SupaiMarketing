package im.supai.supaimarketing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.activity.AdvertisementActivity;
import im.supai.supaimarketing.activity.AroundActivity;
import im.supai.supaimarketing.activity.CartActivity;
import im.supai.supaimarketing.activity.HelpActivity;
import im.supai.supaimarketing.activity.MyOrderActivity;
import im.supai.supaimarketing.activity.NewStoreActivity;
import im.supai.supaimarketing.activity.ProIntroductionActivity;
import im.supai.supaimarketing.activity.ProductActivity;
import im.supai.supaimarketing.activity.SalesActivity;
import im.supai.supaimarketing.activity.SettingsActivity;
import im.supai.supaimarketing.activity.StoreActivity;
import im.supai.supaimarketing.model.Advertisement;
import im.supai.supaimarketing.model.Collection;
import im.supai.supaimarketing.model.Icon;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainpageContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainpageContentFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private GridLayout staticFuncGrid;
    private LinearLayout prodoctCollectionLayout;
    private LinearLayout recentBoughtLayout;
    private User user;
    private Store store;
    private byte[] byteArray;
    private AppContext context;

    private LinearLayout productCollectionEmptyLayout;
    private LinearLayout recentBoughtEmptyLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainpageContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainpageContentFragment newInstance() {
        MainpageContentFragment fragment = new MainpageContentFragment();

        return fragment;
    }

    public MainpageContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = (AppContext)getActivity().getApplication();
        user = context.getUser();
        store = context.getStore();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mainpage_content, container, false);

        staticFuncGrid = (GridLayout) view.findViewById(R.id.static_func_layout);
        prodoctCollectionLayout = (LinearLayout) view.findViewById(R.id.product_collection_layout);
        recentBoughtLayout = (LinearLayout)view.findViewById(R.id.recent_bought_layout);

        productCollectionEmptyLayout = (LinearLayout) view.findViewById(R.id.product_collection_empty);
        recentBoughtEmptyLayout = (LinearLayout) view.findViewById(R.id.recent_bought_empty);

        LinearLayout itemLayout;
        ImageView imgView;
        TextView titleTextView;
        Bitmap img;
        ImageButton itemBtn;

        //周边
        Icon surroundingIcon = new Icon();
        surroundingIcon.setText("周边");
        surroundingIcon.setImgUrl("drawable://" + R.drawable.ic_main_around);
        surroundingIcon.setDest(AroundActivity.class);
        this.addIconToView(surroundingIcon, staticFuncGrid);

        //订单
        Icon orderIcon = new Icon();
        orderIcon.setText("订单");
        orderIcon.setImgUrl("drawable://" + R.drawable.ic_main_order);
        orderIcon.setDest(MyOrderActivity.class);
        if(context.getOrders().getUnreadCount() > 0)
        {
            orderIcon.setBadge(Integer.toString(context.getOrders().getUnreadCount()));
        }
        this.addIconToView(orderIcon, staticFuncGrid);

        //设置
        final Icon userIcon = new Icon();
        userIcon.setText("我的页面");

        userIcon.setImgUrl("drawable://" + R.drawable.ic_user);

        userIcon.setDest(SettingsActivity.class);
        this.addIconToView(userIcon, staticFuncGrid);

        //购物车
        Icon cartIcon = new Icon();
        cartIcon.setText("购物车");
        cartIcon.setImgUrl("drawable://" + R.drawable.ic_cart);
        cartIcon.setDest(CartActivity.class);
        this.addIconToView(cartIcon, staticFuncGrid);

        //帮助
        Icon helpIcon = new Icon();
        helpIcon.setText("帮助");
        helpIcon.setImgUrl("drawable://" + R.drawable.ic_help);
        helpIcon.setDest(HelpActivity.class);
        this.addIconToView(helpIcon, staticFuncGrid);

        HashMap modules = context.getModules();

        if(modules.isEmpty() || modules == null)
        {
            //高级功能
            Icon proIcon = new Icon();
            proIcon.setText("高级功能");
            proIcon.setImgUrl("drawable://" + R.drawable.ic_pro);
            proIcon.setDest(ProIntroductionActivity.class);
            this.addIconToView(proIcon, staticFuncGrid);

        }


        //广告
        ArrayList<Advertisement> advertisements = context.getAdvertisements();
        for (Advertisement advertisement: advertisements)
        {
            Icon advIcon = new Icon();
            advIcon.setText(advertisement.getTitle());
            advIcon.setImgUrl(CommonUtils.getImgFullpath(advertisement.getLogo()));
            advIcon.setDest(AdvertisementActivity.class);
//            advIcon.setObj(advertisement);
            advIcon.setId(advertisement.getId());

            MainpageContentFragment.this.addIconToView(advIcon, staticFuncGrid);

        }

        //售货员图标
        if(user.getClerkOf() != 0)
        {
            Icon salesIcon = new Icon();
            salesIcon.setText("售货");
            salesIcon.setImgUrl("drawable://" + R.drawable.ic_sale);
            salesIcon.setDest(SalesActivity.class);
            MainpageContentFragment.this.addIconToView(salesIcon, staticFuncGrid);

        }

        if(store != null)
        {
            // 有店铺显示店铺tab
            //添加店铺icon
            Icon storeIcon = new Icon();
            storeIcon.setText("我的店铺");
            storeIcon.setImgUrl("drawable://" + R.drawable.ic_store);
            storeIcon.setDest(StoreActivity.class);
            storeIcon.setId(store.getId());
            storeIcon.setObj(store);
            storeIcon.setEditable(true);
            if(store.getStatus() == 2)
            {
                storeIcon.setBadge("店铺关闭");
            }
            MainpageContentFragment.this.addIconToView(storeIcon, staticFuncGrid);

        }
        else
        {
            // 没有则显示开店图标
            //我要开店
            Icon newStoreIcon = new Icon();
            newStoreIcon.setText("我要开店");
            newStoreIcon.setImgUrl("drawable://" + R.drawable.ic_store);
            newStoreIcon.setDest(NewStoreActivity.class);
            MainpageContentFragment.this.addIconToView(newStoreIcon, staticFuncGrid);

        }

        //添加最近购买商品
        new RecentBoughtTask().execute(user.getId());

        //添加收藏的商品
        Collection collection = context.getCollection();
        if(collection != null)
        {
            ArrayList<Product> defaultProducts = collection.getDefaultProducts();
            if(defaultProducts.size() > 0)
            {
                productCollectionEmptyLayout.setVisibility(View.GONE);

                for(int a=0; a<defaultProducts.size(); a++)
                {
                    Product product = defaultProducts.get(a);

                    Icon productIcon = new Icon();
                    productIcon.setText(product.getAlias());
                    productIcon.setImgUrl(CommonUtils.getImgFullpath(product.getImgUrl()));
                    productIcon.setDest(ProductActivity.class);
                    productIcon.setObj(product);
                    MainpageContentFragment.this.addIconToView(productIcon, prodoctCollectionLayout);

                }
            }
            else
            {
                //没有收藏商品
                productCollectionEmptyLayout.setVisibility(View.VISIBLE);

            }

        }


        return view;
    }

    //获取用户最近购买商品任务
    public class RecentBoughtTask extends AsyncTask<Long, Void, ArrayList<Product>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Product> doInBackground(Long... userid) {
            return new ProductAction().recent(userid[0]);

        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);

            if(result.size() > 0)
            {
                for(int a=0; a<result.size(); a++)
                {
                    recentBoughtEmptyLayout.setVisibility(View.GONE);

                    Product product = result.get(a);

                    Icon productIcon = new Icon();
                    productIcon.setText(product.getAlias());
                    productIcon.setImgUrl(CommonUtils.getImgFullpath(product.getImgUrl()));
                    productIcon.setDest(ProductActivity.class);
                    productIcon.setObj(product);
                    MainpageContentFragment.this.addIconToView(productIcon, recentBoughtLayout);

                }
            }
            else
            {
                recentBoughtEmptyLayout.setVisibility(View.VISIBLE);

            }


        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //添加View
    public void addIconToView(final Icon icon, ViewGroup destLayout)
    {
        ImageView imgView;
        TextView titleTextView;
        Bitmap img;
//        BadgeView badge;

        LinearLayout itemLayout = new LinearLayout(getActivity());
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getIconWidth(), context.getIconHeight()));
        final ImageView itemBtn = new ImageView(getActivity());

//        ImageLoader.getInstance().displayImage(icon.getImgUrl(), itemBtn);

        itemBtn.setAdjustViewBounds(true);
        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setCurrentMainPageFragment(0);

                //跳转到设置页面
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putLong("id", icon.getId());
                bundle.putDouble("longitude", icon.getLongitude());
                bundle.putDouble("latitude", icon.getLatitude());
                bundle.putParcelable("obj", (Parcelable) icon.getObj());
                bundle.putBoolean("editable", icon.getEditable());
                intent.putExtras(bundle);
                intent.setClass(getActivity(), (Class) icon.getDest());
                startActivity(intent);
            }
        });
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(context.getIconWidth(), context.getIconHeight() - StaticValues.ICON_TITLE_HEIGHT);
        itemBtn.setLayoutParams(para);
        itemBtn.setPadding(8, 8, 8, 8);
        itemLayout.addView(itemBtn);

        titleTextView = new TextView(getActivity());
        titleTextView.setText(icon.getText());
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        para = new ViewGroup.LayoutParams(context.getIconWidth(), StaticValues.ICON_TITLE_HEIGHT);
        titleTextView.setLayoutParams(para);
        itemLayout.addView(titleTextView);

        if((!icon.getBadge().isEmpty()) && (icon.getBadge() != null))
        {
//            badge = new BadgeView(getActivity(), itemBtn);
//            badge.setText(icon.getBadge());
//            badge.show();
        }

        destLayout.addView(itemLayout);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
