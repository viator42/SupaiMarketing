package im.supai.supaimarketing.util;

import android.os.Message;

/**
 * Created by viator42 on 15/5/4.
 * 系统常量
 */
public class StaticValues
{
    public final static int ICON_WIDTH = 255;
    public final static int ICON_HEIGHT = 255;

    public final static double PI = Math.PI; //3.14159265;  //π
    public final static double EARTH_RADIUS = 6378137;    //地球半径
    public final static double RAD = Math.PI / 180.0;   //   π/180
    public final static int STORE_COUNT = 100;  //默认库存数
    public final static int IMAGE_SEARCH_COUNT = 3;  //搜索的图片数
    public final static int CACHE_LIFE = 30;  //缓存时间

    public final static int ROUND_PIXELS = 50;  //圆角像素值
    public final static int CONNECTION_TIMEOUT = 10000; //连接超时的时间 /毫秒
    //item间隔
    public final static int margin = 8;

    public static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file

    public static final int WIDTH_LIMIT = 800;     //图片宽度

    //登录状态
    //--------------------登录状态--------------------
    public final static int USER_NOTFOUND = 1001; //用户不存在
    public final static int AUTO_PASSWORD_PASSED = 1002; //自动密码验证通过
    public final static int AUTO_PASSWORD_FAILED = 1003; //自动密码验证失败
    public final static int PASSWORD_NEEDED = 1004; //需要手动输入密码验证
    public final static int LOGIN_ERROR = 0; //未知错误

    public final static int PASSWORD_PASSED = 1005; //手动密码验证成功
    public final static int PASSWORD_FAILED = 1006; //手动密码验证失败

    //密码类型
    public final static int USER_PASSTYPE_AUTO = 1;
    public final static int USER_PASSTYPE_INDEPENDENT = 2;

    //Message
    public final static int NAME_UPDATE = 100;
    public final static int ICON_UPDATE = 101;
    public final static int ADDRESS_UPDATE = 102;
    public final static int DESCRIPTION_UPDATE = 103;
    public final static int MAP_LOCATE = 103;
    public final static int REMOVE_CART_PRODUCT = 104;

    //request code
    public final static int IMAGE_UPLOAD = 1001;
    public final static int IMAGE_UPLOADED = 1002;
    public final static int PRODUCT_EDIT = 1003;
    public final static int IMAGE_CAPTURE = 1004;
    public final static int STORE_EDIT = 1005;
    public final static int USER_EDIT = 1006;
    public final static int IMAGE_CROP = 1007;
    public final static int CHANGE_PASSWORD = 1008;
    public final static int ADD_CLERK = 1009;
    public final static int PRODUCT_SEARCH_RESULT = 1010;

    //menu id
    public final static int MENU_ORDER_HISTORY = 2000;
    public final static int MENU_ACTIVE_ORDER = 2001;
    public final static int MENU_CANCEL_ORDER = 2002;
    public final static int MENU_DELIVERED_ORDER = 2003;
    public final static int MENU_CONFIRM_ORDER = 2004;
    public final static int MENU_ORDER_POSITION = 2005;
    public final static int MENU_PROFILE_EDIT = 2006;
    public final static int MENU_ACCEPT_CANCEL_ORDER = 2007;

    public final static int MENU_LOCATE = 2007;
    public final static int MENU_ADD_FAVOURITE = 2008;
    public final static int MENU_REMOVE_FAVOURITE = 2009;
    public final static int MENU_SEARCH = 2010;
    public final static int MENU_SCAN = 2011;
    public final static int MENU_ADD_CLERK = 2012;
    public final static int MENU_CLEAR_PRODUCTS = 2013;
    public final static int MENU_SALE_HISTORY = 2014;

    //订单退货
    public final static int ORDER_RETURN_ACCEPT = 1;
    public final static int ORDER_RETURN_REJECT = 2;

    //订单类型
    public final static int ORDER_TYPE_CUSTOMER = 1;
    public final static int ORDER_TYPE_MERCHANT = 2;

    //订单状态
    public final static int ORDER_STATUS_UNPAID = 1;         //未支付
    public final static int ORDER_STATUS_READY = 2;          //待发货
    public final static int ORDER_STATUS_DELIVERING = 3;    //已发货(发送通知)
    public final static int ORDER_STATUS_SUCCEED = 4;       //交易成功
    public final static int ORDER_STATUS_CLOSED = 5;        //交易关闭
    public final static int ORDER_STATUS_RETURN_APPLY = 6;  //申请退货

    //Location Service 获取位置时间间隔 /秒
    public final static int LOCATION_TIME_INTERVAL = 300;

    //分页查询的个数
    public final static int LIMIT = 20;

    //主页Icon标题的高度
    public final static int ICON_TITLE_HEIGHT = 40;

    public final static int MSG_SET_ALIAS = 3001;

    //收费模块code
    public final static String MODULE_PRINTER = "MODULE_PRINTER";
    public final static String MODULE_STATISTICS = "MODULE_STATISTICS";
    public final static long MODULE_PRINTER_ID = 1;

    public final static long MODULE_PRO_BUNDLE_ID = 1;

    //打印机数
    public final static int MAX_PRINTER_CNT = 1;

    //关注者状态
    public final static int FOLLOWER_STATIC_MORMAL = 1;
    public final static int FOLLOWER_STATIC_BLOCKED = 2;

    //订单tab页
    public final static int ORDER_TAB_CUSTOMER = 1;
    public final static int ORDER_TAB_MERCHANT = 2;

    //条形码扫描类型
    public final static int SCAN_TYPE_ADD_PRODUCT = 1;
    public final static int SCAN_TYPE_USER = 2;
    public final static int SCAN_TYPE_SALES = 3;

    //搜索类型
    public final static int SEARCH_TYPE_BARCODE = 1;
    public final static int SEARCH_TYPE_ALIAS = 2;
    public final static int SEARCH_TYPE_STORE = 3;

    //Cache key 前缀
    public final static String CACHE_KEY_STORE_PRODUCTS = "CKSP";   //店铺商品
    public final static String CACHE_KEY_FOLLOWER = "CKF";   //关注者
    public final static String CACHE_KEY_STORE = "CKS";   //店铺详情
    public final static String CACHE_KEY_PRODUCT_DETAIL = "CPD";   //商品详情
    public final static String CACHE_KEY_SEARCH = "CS";   //根据条形码查询
    public final static String CACHE_KEY_SEARCH_BY_NAME = "CSN";   //根据名称查询
    public final static String CACHE_KEY_ORDERS = "CKO";    //订单列表
    public final static String CACHE_KEY_ORDER_DETAIL = "CKOD";    //订单详情
    public final static String CACHE_KEY_CARTS = "CKC";    //订单详情
    public final static String CACHE_KEY_SEARCH_STORE_BY_NAME = "CSBN";    //订单详情

    //商品添加状态

    //网络状态
    public final static int NETWORK_STATUS_OKAY = 1;
    public final static int NETWORK_STATUS_ERROR = 2;

    //关注者搜索类型
    public final static int FOLLOWER_SEARCH_TYPE_NAME = 1;
    public final static int FOLLOWER_SEARCH_TYPE_TEL = 2;

    //销售历史查看类型
    public final static int SALE_HISTORY_TYPE_CLERK = 1;
    public final static int SALE_HISTORY_TYPE_STORE = 2;

    //默认位置
    public final static double LONGITUDE_DEFAULT = 116.40756;
    public final static double LATITUDE_DEFAULT = 39.90486;

    //首次开启
    public final static int FIRST_OPEN_Y = 0;
    public final static int FIRST_OPEN_N = 1;

    //打印份数
    public final static int PRINT_COPY = 1;

}
