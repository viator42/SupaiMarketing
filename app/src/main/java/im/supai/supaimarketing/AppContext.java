package im.supai.supaimarketing;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import im.supai.supaimarketing.model.Advertisement;
import im.supai.supaimarketing.model.Collection;
import im.supai.supaimarketing.model.FirstOpen;
import im.supai.supaimarketing.model.Icon;
import im.supai.supaimarketing.model.ModuleCategory;
import im.supai.supaimarketing.model.Orders;
import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.EnvValues;
import im.supai.supaimarketing.util.StaticValues;

public class AppContext extends Application {
    private User user = null;
    private Store store = null;
    private List<Icon> mainpageIcons;
    private CharSequence barcode;

    private double longitude;
    private double latitude;

    //设备识别码
    private String imie;
    //手机号
    private String phoneNumber;

    //屏幕宽高
    private int screenWidth;
    private int screenHeight;

    //用户收藏的商店/商品列表
    private Collection collection;

    //主界面图标尺寸
    private int iconWidth;
    private int iconHeight;

    //用户订单
    private Orders orders;

    private DownloadManager dMgr;
    private long downloadId;

//    private GpService mGpService = null;
//    private PrinterServiceConnection conn = null;
    private  int mPrinterIndex = 0;
//    private PortParameters mPortParam[] = new PortParameters[StaticValues.MAX_PRINTER_CNT];

    private long currentMainPageFragment = 0;

    //收费模块
    private HashMap<String, ModuleCategory> modules;

    //
    private int currentOrderTab = StaticValues.ORDER_TAB_CUSTOMER;

    private ArrayList<Advertisement> advertisements;

    //售货列表
//    private HashMap<Long, Product> salesList;

    //网络状态
    private int networkStatus = StaticValues.NETWORK_STATUS_OKAY;

    //售货员所属店铺
    private Store relatedStore = null;

    private FirstOpen firstOpen;

    public RequestOptions glideRequestOptions;

    @Override
    public void onCreate() {
        super.onCreate();

//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush

        //refStroe获取用户信息
        user = new User();

        SharedPreferences ref = getSharedPreferences("user", Context.MODE_PRIVATE);
        long id = ref.getLong("id", 0);
        user.setId(id);
        if(id != 0)
        {
            user.setUsername(ref.getString("username", ""));
            user.setTel(ref.getString("tel", ""));

            //获取用户信息成功

        }

        //refStore获取店铺信息

        //主屏幕Icons
        mainpageIcons = new ArrayList<Icon>();

        /*
        //Image loader设置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_launcher)//设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.ic_launcher)//设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

//        File cacheDir = StorageUtils.getCacheDirectory(this);
        File cacheDir = StorageUtils.getOwnCacheDirectory(this,
                "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(600, 480) // default = device screen dimensions
                .diskCacheExtraOptions(600, 480, null)
                .threadPoolSize(10) // default
//        config.threadPriority(Thread.NORM_PRIORITY - 2); // default
//        config.tasksProcessingOrder(QueueProcessingType.FIFO); // default
//        config.denyCacheImageMultipleSizesInMemory();
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(450 * 1024 * 1024)
                .diskCacheFileCount(300)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        */

        dMgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

//        salesList = new HashMap<Long, Product>();

        glideRequestOptions = new RequestOptions()
                .centerCrop();
    }

    /**
     * 设置user信息
     * @param user
     */
    public void setUser(User user)
    {
        this.user = user;

        //写入ref
        SharedPreferences ref = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putLong("id", user.getId());
        editor.putString("tel", user.getTel());
        editor.putString("username", user.getUsername());
        editor.commit();

    }

    public User getUser()
    {
        return user;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;

        //写入ref
        SharedPreferences ref = getSharedPreferences("store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putLong("id", store.getId());
        editor.putString("name", store.getName());

        editor.commit();

    }

    //------------------新版app下载------------------
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                ParcelFileDescriptor pfd = dMgr.openDownloadedFile(downloadId);


            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    public void downloadApp()
    {
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(EnvValues.serverPath + "release/SupaiMarketing.apk")
        );
        request.setTitle("升级包下载中...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);

        downloadId = dMgr.enqueue(request);

    }

    //------------------打印服务相关------------------

    /*
    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) { Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null; }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service); }
    };

    public void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent("com.gprinter.aidl.GpPrintService");
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService }
    }

    public void startPrinterService() {
        Intent i= new Intent(this, GpPrintService.class); startService(i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //返回打印机是否可用
    public boolean isPrinterAvaliable()
    {
        boolean result = false;

        try {
            int status = getmGpService().queryPrinterStatus(mPrinterIndex, 500);

            if (status == GpCom.STATE_NO_ERR) {
                result = true;
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return result;
    }

    //获取打印机状态
    public String getPrinterStatus()
    {
        String str = "打印机未连接";

        try {
            int status = getmGpService().queryPrinterStatus(mPrinterIndex, 500);

            if (status == GpCom.STATE_NO_ERR) {
                str = "正常";
            }
            else
            {
                str = " ";
                if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                    str  = "打印机脱机";
                }
                if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                    str = "打印机缺纸";
                }
                if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                    str  = "打印机开盖";
                }
                if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                    str = "打印机出错";
                }
                if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                    str = "查询超时";
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return str;
    }

    public boolean[] getConnectState() {
        boolean[] state = new boolean[StaticValues.MAX_PRINTER_CNT];
        for (int i = 0; i < StaticValues.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        if(this.mGpService != null)
        {
            for (int i = 0; i < StaticValues.MAX_PRINTER_CNT; i++) {
                try {
                    if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                        state[i] = true;
                    }
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return state;
        }
        else
        {
            return null;
        }

    }

    public void initPortParam() {
        boolean[] state = getConnectState();
        for (int i = 0; i < StaticValues.MAX_PRINTER_CNT; i++) {
            PortParamDataBase database = new PortParamDataBase(this);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            mPortParam[i].setPortOpenState(state[i]);
        }
    }

     */

//    //------------------售货列表操作------------------
//    public void addSalesList(Product product)
//    {
//        this.salesList.put(product.getId(), product);
//
//    }
//
//    public void removeSalesList(long id)
//    {
//        this.salesList.remove(id);
//
//    }
//
//    public void clearSalesList()
//    {
//        this.salesList.clear();
//
//    }
//
//    public void changeSalesListProductCount(long id, int count)
//    {
//        Product product = this.salesList.get(id);
//        product.setCount(count);
//        this.salesList.put(id, product);
//
//    }

    //------------------打印服务相关------------------

    public CharSequence getBarcode() {
        return barcode;
    }

    public void setBarcode(CharSequence barcode) {
        this.barcode = barcode;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public int getmPrinterIndex() {
        return mPrinterIndex;
    }

    public void setmPrinterIndex(int mPrinterIndex) {
        this.mPrinterIndex = mPrinterIndex;
    }

    public long getCurrentMainPageFragment() {
        return currentMainPageFragment;
    }

    public void setCurrentMainPageFragment(long currentMainPageFragment) {
        this.currentMainPageFragment = currentMainPageFragment;
    }

    public HashMap<String, ModuleCategory> getModules() {
        return modules;
    }

    public void setModules(HashMap<String, ModuleCategory> modules) {
        this.modules = modules;
    }

    public int getCurrentOrderTab() {
        return currentOrderTab;
    }

    public void setCurrentOrderTab(int currentOrderTab) {
        this.currentOrderTab = currentOrderTab;
    }

    public ArrayList<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(ArrayList<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public Store getRelatedStore() {
        return relatedStore;
    }

    public void setRelatedStore(Store relatedStore) {
        this.relatedStore = relatedStore;
    }
//    public HashMap<Long, Product> getSalesList() {
//        return salesList;
//    }
//
//    public void setSalesList(HashMap<Long, Product> salesList) {
//        this.salesList = salesList;
//    }

    public int getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(int networkStatus) {
        this.networkStatus = networkStatus;
    }

    public FirstOpen getFirstOpen() {
        return firstOpen;
    }

    public void setFirstOpen(FirstOpen firstOpen) {
        this.firstOpen = firstOpen;
    }
}
