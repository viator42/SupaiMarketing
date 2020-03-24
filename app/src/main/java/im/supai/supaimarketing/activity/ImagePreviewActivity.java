package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.R;

public class ImagePreviewActivity extends AppCompatActivity {

    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_preview);

        imgView = (ImageView) findViewById(R.id.imgView);

        Bundle bundle = this.getIntent().getExtras();
        String uri = bundle.getString("uri");

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageForEmptyUri(R.drawable.img_failed) // resource or drawable
//                .showImageOnFail(R.drawable.img_failed) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(uri, imgView, options);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePreviewActivity.this.finish();
            }
        });
    }

}
