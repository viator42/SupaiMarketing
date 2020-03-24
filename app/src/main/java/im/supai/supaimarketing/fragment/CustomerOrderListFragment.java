package im.supai.supaimarketing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.OrderAction;
import im.supai.supaimarketing.activity.OrderDetailActivity;
import im.supai.supaimarketing.adapter.MyOrderItemAdapter;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.Orders;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

/**
 * 客户订单列表
 */
public class CustomerOrderListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView orderListView;
    private ArrayList<Order> orderList;
    private int type;
    private AppContext context;
    private int page;

    private FrameLayout warningLayout;
    private TextView warningTextView;

    public static CustomerOrderListFragment newInstance() {
        CustomerOrderListFragment fragment = new CustomerOrderListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CustomerOrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_customer_order_list, container, false);

        context = (AppContext) getActivity().getApplicationContext();

        //获取参数
        Bundle bundle = this.getArguments();
        orderList = bundle.getParcelableArrayList("orders");
        type = bundle.getInt("type");

        //没有数据时的提示
        warningLayout = (FrameLayout) view.findViewById(R.id.warning_layout);
        warningTextView = (TextView) view.findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        orderListView = (ListView) view.findViewById(R.id.orderlist);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //跳转到详情页面
                Order order;
                for(int a=0; a<orderList.size(); a++)
                {
                    order = orderList.get(a);
                    if(order.getId() == id)
                    {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), OrderDetailActivity.class);

                        Bundle bundle=new Bundle();
                        bundle.putParcelable("obj", order);
                        bundle.putInt("type", StaticValues.ORDER_TYPE_CUSTOMER);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                }

            }
        });
        orderListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                //当不滚动时
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    //判断滚动到底部
                    if(orderListView.getLastVisiblePosition()==(orderListView.getCount()-1)){
                        new LoadmoreTask().execute(type);

                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        //列表赋值
        if(orderList!=null)
        {

            MyOrderItemAdapter adapter = new MyOrderItemAdapter(generateListviewData(orderList), getActivity());
            orderListView.setAdapter(adapter);

            page = 1;

        }
        else
        {
            //列表为空
            warningLayout.setVisibility(View.VISIBLE);
            warningTextView.setText("订单列表为空");

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

    //加载更多
    public class LoadmoreTask extends AsyncTask<Integer, Void, Orders>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Orders doInBackground(Integer... type) {
            return new OrderAction().activeOrders(getActivity(), context.getUser().getId(), type[0], page, 0, false);
        }

        @Override
        protected void onPostExecute(Orders result) {
            super.onPostExecute(result);
            //添加到listview
            if(result.getCustomerList() != null)
            {
                MyOrderItemAdapter myOrderItemAdapter = (MyOrderItemAdapter) orderListView.getAdapter();
                myOrderItemAdapter.updateList(generateListviewData(result.getCustomerList()));
                page += 1;

            }
            else
            {
                Toast.makeText(getActivity(), "没有更多", Toast.LENGTH_SHORT).show();

            }


        }
    }

    private List generateListviewData(ArrayList<Order> orderList)
    {
        List orderListData = new ArrayList<Map<String,Object>>();
        for(Order order: orderList)
        {
            Map line = new HashMap();

            line.put("id", order.getId());
            line.put("storeName", order.getStoreName());
            line.put("createTime", CommonUtils.timestampToDatetime(order.getCreateTime()));
            line.put("summary", order.getSummary());
            line.put("readed", order.getReaded());
            line.put("sn", order.getSn());
            line.put("status", order.getStatusName());

            orderListData.add(line);

        }

        return orderListData;
    }


}
