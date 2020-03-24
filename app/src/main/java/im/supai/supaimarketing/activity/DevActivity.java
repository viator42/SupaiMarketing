package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.MasterAction;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.R;

public class DevActivity extends AppCompatActivity {
    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private AppContext context;
    private Timer timer;
    private Button refreashBtn;
    private Button deleteProfileBtn;
    private TextView useridTextView;
    private TextView storeIdTextView;
    private TextView imieTextView;
    private TextView storeLongitudeTextView;
    private TextView storeLatitudeTextView;
    private TextView snTextView;
    private TextView verionTextView;
    private Button downloadBtn;
    private Button masterBtn;

    private String imie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dev);

        context = (AppContext) getApplicationContext();
        longitudeTextView = (TextView)findViewById(R.id.longitude);
        latitudeTextView = (TextView)findViewById(R.id.latitude);
        refreashBtn = (Button)findViewById(R.id.refreash);
        refreashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longitudeTextView.setText( String.valueOf(context.getLongitude()));
                latitudeTextView.setText(String.valueOf(context.getLatitude()));
            }
        });
        useridTextView = (TextView) findViewById(R.id.userid);
        useridTextView.setText(Long.toString(context.getUser().getId()));

        storeIdTextView = (TextView) findViewById(R.id.store_id);

        imie = context.getImie();
        imieTextView = (TextView) findViewById(R.id.imie);
        imieTextView.setText(imie);

        longitudeTextView.setText(String.valueOf(context.getLongitude()));
        latitudeTextView.setText(String.valueOf(context.getLatitude()));

        snTextView = (TextView) findViewById(R.id.sn);
        verionTextView = (TextView) findViewById(R.id.version);

        //抹掉用户信息
        deleteProfileBtn = (Button)findViewById(R.id.delete);
        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        storeLongitudeTextView = (TextView) findViewById(R.id.store_longitude);
        storeLatitudeTextView = (TextView)findViewById(R.id.store_latitude);

        if(context.getStore() != null)
        {
            storeLongitudeTextView.setText(Double.toString(context.getStore().getLongitude()));
            storeLatitudeTextView.setText(Double.toString(context.getStore().getLatitude()));

            storeIdTextView.setText(Long.toString(context.getStore().getId()));

        }

        snTextView.setText(context.getUser().getSn());

        downloadBtn = (Button) findViewById(R.id.download);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.downloadApp();

            }
        });

        masterBtn = (Button) findViewById(R.id.master);
        masterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MasterAuthTask().execute();

            }
        });

//        printerBtn = (Button) findViewById(R.id.printer);
//        printerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DevActivity.this, PrinterActivity.class);
//                startActivity(intent);
//
//            }
//        });

//        downloadBtn.setOnClickListener();

//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                //获取当前位置
//                //上传位置
//                if(context.getLongitude() != 0 && context.getLatitude() != 0)
//                {
//                    longitudeTextView.setText( String.valueOf(context.getLongitude()));
//                    latitudeTextView.setText(String.valueOf(context.getLatitude()));
//
//                }
//                Looper.loop();
//            }
//        }, 5000, 5000);

    }

    protected void onStart()
    {
        super.onStart();

    }

    //中控台功能确认
    public class MasterAuthTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return new MasterAction().auth(imie);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true)
            {
                Intent intent = new Intent(DevActivity.this, MasterActivity.class);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(DevActivity.this, "登入失败", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
