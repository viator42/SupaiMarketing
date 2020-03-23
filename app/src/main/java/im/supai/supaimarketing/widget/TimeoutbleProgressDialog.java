package im.supai.supaimarketing.widget;

import android.app.ProgressDialog;

/**
 * Created by viator42 on 15/6/4.
 * 带超时检测的ProgressDialog
 *
 */

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class TimeoutbleProgressDialog extends ProgressDialog{
    private long mTimeOut = 0;// 默认timeOut为0, 即无限大
    private OnTimeOutListener mTimeOutListener = null;// timeOut后的处理器
    private Timer mTimer = null;// 定时器

    @SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mTimeOutListener != null) {
                mTimeOutListener.onTimeOut(TimeoutbleProgressDialog.this);
                dismiss();
            }
        }
    };

    public TimeoutbleProgressDialog(Context context) {
        super(context);
        // 设置进度条风格，风格为圆形，旋转的
        this.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 标题
        this.setTitle("提示");
        // 设置ProgressDialog 提示信息
        this.setMessage("正在加载数据中，请稍后");
        // 设置ProgressDialog 的进度条是否不明确
        this.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        this.setCancelable(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mTimeOut != 0) {
            mTimer = new Timer();
            TimerTask timerTast = new TimerTask() {
                @Override
                public void run() {
                    // dismiss();
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            };
            mTimer.schedule(timerTast, mTimeOut);
        }

    }

    /**
     * 设置timeOut长度，和处理器
     *
     * @param t
     *            timeout时间长度
     * @param timeOutListener
     *            超时后的处理器
     */
    public void setTimeOut(long t, OnTimeOutListener timeOutListener) {
        mTimeOut = t;
        if (timeOutListener != null) {
            this.mTimeOutListener = timeOutListener;
        }
    }

    /**
     *
     * 处理超时的的接口
     *
     */
    public interface OnTimeOutListener {

        /**
         * 当progressDialog超时时调用此方法
         */
        abstract public void onTimeOut(TimeoutbleProgressDialog dialog);
    }

    /**
     * 通过静态Create的方式创建一个实例对象
     *
     * @param context
     * @param time
     *            timeout时间长度
     * @param listener
     *            timeOutListener 超时后的处理器
     * @return MyProgressDialog 对象
     */
    public static TimeoutbleProgressDialog createProgressDialog(Context context,
                                                        long time, OnTimeOutListener listener) {
        TimeoutbleProgressDialog progressDialog = new TimeoutbleProgressDialog(context);
        if (time != 0) {
            progressDialog.setTimeOut(time, listener);
        }
        return progressDialog;
    }

}
