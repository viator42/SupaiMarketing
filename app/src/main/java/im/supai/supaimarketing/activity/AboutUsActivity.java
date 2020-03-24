package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.util.CommonUtils;

public class AboutUsActivity extends AppCompatActivity {
    private Button devButton;
    private TextView versionTextView;
    private Button checkNewVersionBtn;
    private AppContext context;
    private ImageView rccodeImageView;

    //进入开发者页面的剩余次数
    private int timeToDev = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        context = (AppContext) getApplicationContext();

        versionTextView = (TextView) findViewById(R.id.version);
        versionTextView.setText(CommonUtils.getVersion(AboutUsActivity.this));
        checkNewVersionBtn = (Button) findViewById(R.id.check_new_version);
        checkNewVersionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.downloadApp();

            }
        });

        devButton = (Button)findViewById(R.id.dev);
        devButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeToDev <= 0) {
                    Intent intent = new Intent(AboutUsActivity.this, DevActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    timeToDev -= 1;
                }

            }
        });

        rccodeImageView = (ImageView) findViewById(R.id.rccode);
        rccodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUsActivity.this, ImagePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uri", "drawable://" + R.drawable.download_rccode);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

}
