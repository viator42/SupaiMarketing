package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.adapter.MyOrderProductAdaper;
import im.supai.supaimarketing.adapter.ProductStorageAdapter;
import im.supai.supaimarketing.fragment.MainpageContentFragment;
import im.supai.supaimarketing.fragment.ProductLowInStorageFragment;
import im.supai.supaimarketing.fragment.ProductParticularsFragment;
import im.supai.supaimarketing.fragment.StatisticFragment;
import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.Store;

public class StatisticsActivity extends AppCompatActivity
        implements ProductLowInStorageFragment.OnFragmentInteractionListener,
        ProductParticularsFragment.OnFragmentInteractionListener,
        StatisticFragment.OnFragmentInteractionListener {

    private android.app.ActionBar actionBar;
    private LinearLayout tabsLayout;
    private RelativeLayout contentLayout;

    private android.app.ActionBar.Tab productInLowStorageTab;
    private android.app.ActionBar.Tab statisticsTab;
    private android.app.ActionBar.Tab particularsTab;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment fragment;

    private AppContext context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        contentLayout = (RelativeLayout) findViewById(R.id.content);

    }

    @Override
    protected void onStart() {
        super.onStart();

        actionBar = getActionBar();
        actionBar.removeAllTabs();

        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);

        //缺货商品tab
        productInLowStorageTab = actionBar.newTab();
        productInLowStorageTab.setText("缺货商品");
        productInLowStorageTab.setTabListener(new android.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                fragment = new ProductLowInStorageFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();

            }

            @Override
            public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        });
        actionBar.addTab(productInLowStorageTab);

        //统计Tab
        statisticsTab = actionBar.newTab();
        statisticsTab.setText("统计");
        statisticsTab.setTabListener(new android.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                fragment = new StatisticFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();

            }

            @Override
            public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        });
        actionBar.addTab(statisticsTab);

        //商品明细Tab
        particularsTab = actionBar.newTab();
        particularsTab.setText("商品明细");
        particularsTab.setTabListener(new android.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                fragment = new ProductParticularsFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();

            }

            @Override
            public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        });
        actionBar.addTab(particularsTab);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
