package im.supai.supaimarketing.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import im.supai.supaimarketing.action.StatisticsAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.adapter.ProductStorageAdapter;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductLowInStorageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductLowInStorageFragment extends Fragment {
    private ListView listView;
    private AppContext context;
    private Store store;

    private OnFragmentInteractionListener mListener;
    private TimeoutbleProgressDialog getProductsDialog;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ProductStorageAdapter adapter;
    private int page;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductLowInStorageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductLowInStorageFragment newInstance() {
        ProductLowInStorageFragment fragment = new ProductLowInStorageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ProductLowInStorageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_low_in_storage, container, false);

        context = (AppContext) getActivity().getApplicationContext();
        store = context.getStore();

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() == listView.getCount() - 1) {
                        reloadLowInStoreProducts();

                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        //没有数据时的提示
        warningLayout = (FrameLayout) view.findViewById(R.id.warning_layout);
        warningTextView = (TextView) view.findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        getProductsDialog = TimeoutbleProgressDialog.createProgressDialog(getActivity(), StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getProductsDialog.dismiss();

                Toast.makeText(getActivity(), "未获取到缺货商品列表", Toast.LENGTH_SHORT).show();

            }
        });

        page = 0;

        reloadLowInStoreProducts();

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

    //缺货商品列表
    public class ProductLowInStoreTask extends AsyncTask<Void, Void, ArrayList<Product>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getProductsDialog.show();

        }

        @Override
        protected ArrayList<Product> doInBackground(Void... voids) {
            return new StatisticsAction().productLowInStore(store.getId(), page);

        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);
            getProductsDialog.dismiss();

            if(result != null)
            {
                if(result.isEmpty())
                {
                    if(page == 0)
                    {
                        listView.setAdapter(null);

                        //列表为空
                        warningLayout.setVisibility(View.VISIBLE);
                        warningTextView.setText("没有缺货商品");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "没有更多", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    List listData = new ArrayList<Map<String,Object>>();
                    //显示到UI
                    for(final Product product: result)
                    {
                        Map line = new HashMap();

                        line.put("id", product.getId());
                        line.put("alias", product.getAlias());
                        line.put("price", product.getPrice());
                        line.put("count", product.getCount());
                        line.put("img", product.getImgUrl());

                        listData.add(line);

                    }

                    if(page == 0)
                    {
                        adapter = new ProductStorageAdapter(listData, getActivity());
                        listView.setAdapter(adapter);
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
                listView.setAdapter(null);

                //列表为空
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未取到商品信息");

            }

//            context.reloadCartInfo();
        }
    }

    //重新读取商品列表
    public void reloadLowInStoreProducts()
    {
        new ProductLowInStoreTask().execute();

    }

}
