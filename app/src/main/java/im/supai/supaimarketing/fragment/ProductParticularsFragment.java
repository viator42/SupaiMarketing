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
import im.supai.supaimarketing.adapter.MyOrderProductAdaper;
import im.supai.supaimarketing.adapter.ProductParticularsAdapter;
import im.supai.supaimarketing.model.OrderDetail;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Statistics;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductParticularsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductParticularsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private AppContext context;
    private Store store;
    private TimeoutbleProgressDialog particularsDialog;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private int page;
    private ProductParticularsAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductParticularsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductParticularsFragment newInstance(String param1, String param2) {
        ProductParticularsFragment fragment = new ProductParticularsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ProductParticularsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = (AppContext) getActivity().getApplicationContext();
        store = context.getStore();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_particulars, container, false);

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

        particularsDialog = TimeoutbleProgressDialog.createProgressDialog(getActivity(), StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                particularsDialog.dismiss();

                Toast.makeText(getActivity(), "未获取到商品列表", Toast.LENGTH_SHORT).show();

            }
        });

        //没有数据时的提示
        warningLayout = (FrameLayout) view.findViewById(R.id.warning_layout);
        warningTextView = (TextView) view.findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

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

    //统计信息任务
    public class ParticularsListTask extends AsyncTask<Long, Void, ArrayList<Product>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            particularsDialog.show();
        }

        @Override
        protected ArrayList doInBackground(Long... storeId) {
            return new StatisticsAction().particulars(storeId[0], page);

        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            super.onPostExecute(result);
            particularsDialog.dismiss();

            //显示到UI
            if(result != null)
            {
                if(result.isEmpty())
                {
                    if(page == 0)
                    {
                        listView.setAdapter(null);

                        //列表为空
                        warningLayout.setVisibility(View.VISIBLE);
                        warningTextView.setText("尚未添加商品");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "没有更多", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    List listData = new ArrayList<Map<String,Object>>();
                    for(Product product: result)
                    {
                        Map line = new HashMap();

                        line.put("id", product.getId());
                        line.put("img", product.getImgUrl());
                        line.put("alias", product.getAlias());
                        line.put("price", product.getPrice());
                        line.put("count", product.getCount());
                        line.put("summary", product.getPrice() * product.getCount());

                        listData.add(line);

                    }

                    adapter = new ProductParticularsAdapter(listData, getActivity());
                    listView.setAdapter(adapter);

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

        }
    }

    //重新读取商品列表
    public void reloadLowInStoreProducts()
    {
        new ParticularsListTask().execute(store.getId());

    }

}
