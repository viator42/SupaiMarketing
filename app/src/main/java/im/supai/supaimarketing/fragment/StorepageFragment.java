package im.supai.supaimarketing.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.activity.ProductActivity;
import im.supai.supaimarketing.activity.StoreActivity;
import im.supai.supaimarketing.model.Icon;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/5/13.
 */
public class StorepageFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private GridLayout mainpageGrid;
    private long userid;

    private AppContext context;

    private Store store;
    private ArrayList<Product> productCollect;

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

    public StorepageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (AppContext)getActivity().getApplication();
        userid = context.getUser().getId();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_storepage_content, container, false);

        mainpageGrid = (GridLayout)view.findViewById(R.id.mainpageGrid);

        LinearLayout itemLayout;
        ImageView imgView;
        TextView titleTextView;
        Bitmap img;
        ImageButton itemBtn;

        //获取参数
        Bundle bundle = this.getArguments();
        store = (Store)bundle.getParcelable("store");

        productCollect = bundle.getParcelableArrayList("productCollect");

        //店铺主页icon
        addStroeIconToView(store);

        for(int a=0; a<productCollect.size(); a++)
        {
            setAddProductIconToView(productCollect.get(a));

        }

        return view;
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

    //商品Icon
    public void setAddProductIconToView(final Product product)
    {
        ImageView imgView;
        TextView titleTextView;
        Bitmap img;

        LinearLayout itemLayout = new LinearLayout(getActivity());
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getIconWidth(), context.getIconHeight()));

        itemLayout.setOrientation(LinearLayout.VERTICAL);
        final ImageView itemBtn = new ImageView(getActivity());
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(product.getImgUrl()), itemBtn);

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", product);
                bundle.putBoolean("editable", false);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), ProductActivity.class);

                startActivity(intent);
            }
        });
        itemBtn.setAdjustViewBounds(true);

        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(context.getIconWidth(), context.getIconHeight() - StaticValues.ICON_TITLE_HEIGHT);
        itemBtn.setLayoutParams(para);
        itemBtn.setPadding(8, 8, 8, 8);

        itemLayout.addView(itemBtn);

        titleTextView = new TextView(getActivity());
        titleTextView.setText(product.getAlias());
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        para = new ViewGroup.LayoutParams(context.getIconWidth(), StaticValues.ICON_TITLE_HEIGHT);
        titleTextView.setLayoutParams(para);

        itemLayout.addView(titleTextView);

        //商品下架标识
        mainpageGrid.addView(itemLayout);

    }

    //添加商铺Icon
    public void addStroeIconToView(final Store store)
    {
        ImageView imgView;
        TextView titleTextView;
        Bitmap img;

        LinearLayout itemLayout = new LinearLayout(getActivity());
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getIconWidth(), context.getIconHeight()));
        final ImageView itemBtn = new ImageView(getActivity());
//        byte[] byteArray = store.getLogo();
//        itemBtn.setImageBitmap(BitmapTools.getRoundedCornerBitmap(BitmapFactory.decodeByteArray(byteArray, 0,
//                byteArray.length), StaticValues.ROUND_PIXELS));

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_user) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_user) // resource or drawable
//                .showImageOnFail(R.drawable.ic_user) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(store.getLogoUrl()), itemBtn, options);

        itemBtn.setAdjustViewBounds(true);
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(context.getIconWidth(), context.getIconHeight() - StaticValues.ICON_TITLE_HEIGHT);
        itemBtn.setLayoutParams(para);
        itemBtn.setPadding(8, 8, 8, 8);
        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到设置页面
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
//                bundle.putLong("id", store.getId());
                bundle.putParcelable("obj", store);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), StoreActivity.class);
                startActivity(intent);
            }
        });
        itemLayout.addView(itemBtn);

        titleTextView = new TextView(getActivity());
        titleTextView.setText(store.getName());
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setPadding(8, 0, 8, 0);
        para = new ViewGroup.LayoutParams(context.getIconWidth(), StaticValues.ICON_TITLE_HEIGHT);
        titleTextView.setLayoutParams(para);

        itemLayout.addView(titleTextView);

        mainpageGrid.addView(itemLayout);

    }

    //店铺刷新信息Task
    public class RefreshStoreTask extends AsyncTask<Long, Void, Store>
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
            //刷新店铺信息
            mainpageGrid.removeAllViewsInLayout();

            StorepageFragment.this.addStroeIconToView(result);


        }
    }

    //刷新收藏的Task
    public class RefreshCollectionTask extends AsyncTask<Long, Void, Store>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Store doInBackground(Long... id) {

            return null;
        }

        @Override
        protected void onPostExecute(Store result) {

        }
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
