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
import android.widget.Button;
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
import im.supai.supaimarketing.activity.SalesHistoryActivity;
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
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private AppContext context;
    private Store store;

    private TextView productCountTextView;
    private TextView productValueSumTextView;
    private TextView unpaidSumTextView;
    private TextView turnoverTodayTextView;
    private TextView turnoverMonthTextView;
    private TextView turnoverYearTextView;

    private TimeoutbleProgressDialog statisticDialog;

    private Button detailBtn;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public StatisticFragment() {
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
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        productCountTextView = (TextView) view.findViewById(R.id.product_count);
        productValueSumTextView = (TextView) view.findViewById(R.id.product_value_sum);
        unpaidSumTextView = (TextView) view.findViewById(R.id.unpaid_sum);
        turnoverTodayTextView = (TextView) view.findViewById(R.id.turnover_today);
        turnoverMonthTextView = (TextView) view.findViewById(R.id.turnover_month);
        turnoverYearTextView = (TextView) view.findViewById(R.id.turnover_year);

        statisticDialog = TimeoutbleProgressDialog.createProgressDialog(getActivity(), StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                statisticDialog.dismiss();

                Toast.makeText(getActivity(), "为获取到缺货商品列表", Toast.LENGTH_SHORT).show();

            }
        });

        detailBtn = (Button) view.findViewById(R.id.detail);
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SalesHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", StaticValues.SALE_HISTORY_TYPE_STORE);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        new StatisticsInfoTask().execute(store.getId());

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
    public class StatisticsInfoTask extends AsyncTask<Long, Void, Statistics>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statisticDialog.show();
        }

        @Override
        protected Statistics doInBackground(Long... storeId) {
            return new StatisticsAction().getInfo(storeId[0]);

        }

        @Override
        protected void onPostExecute(Statistics result) {
            super.onPostExecute(result);
            statisticDialog.dismiss();

            //显示到UI
            if(result != null)
            {
                productCountTextView.setText(Integer.toString(result.getProductCount()));
                productValueSumTextView.setText(Double.toString(result.getProductValueSum()));
                unpaidSumTextView.setText(Double.toString(result.getUnpaidSum()));
                turnoverTodayTextView.setText(Double.toString(result.getTurnoverToday()));
                turnoverMonthTextView.setText(Double.toString(result.getTurnoverMonth()));
                turnoverYearTextView.setText(Double.toString(result.getTurnoverYear())); ;

            }
            else
            {

            }

        }
    }

}
