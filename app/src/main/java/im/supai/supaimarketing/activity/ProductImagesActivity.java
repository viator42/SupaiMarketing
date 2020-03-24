package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import im.supai.supaimarketing.R;

public class ProductImagesActivity extends AppCompatActivity {

    protected List<Bitmap> imgList;
    private ArrayList<String> urls;
    protected GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_images);

        gridLayout = (GridLayout)findViewById(R.id.grid);

        //获取图片列表
        Bundle bundle = this.getIntent().getExtras();
        urls = bundle.getStringArrayList("urls");

        new GetImagesTask().execute();

    }

    //图片Task
    public class GetImagesTask extends AsyncTask<String, Void, List<Bitmap>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Bitmap> doInBackground(String... id) {
            ArrayList<Bitmap> data = new ArrayList<Bitmap>();
            Bitmap bitmap;

            for(int i = 0;i < urls.size(); i ++)
            {
                try
                {
//                    byte[] byteArray = HttpClientUtil.getImageFromWeb(urls.get(i));
//                    data.add(BitmapFactory.decodeByteArray(byteArray, 0,
//                            byteArray.length));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;

                }

            }

            return data;


        }

        @Override
        protected void onPostExecute(List<Bitmap> result) {
            super.onPostExecute(result);
            if(result != null)
            {
                ProductImagesActivity.this.imgList = result;
                //更新UI
                for(int i = 0;i < result.size(); i ++)
                {
                    ImageButton imageButton = new ImageButton(ProductImagesActivity.this);
                    imageButton.setImageBitmap(result.get(i));
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //选择图片

                        }
                    });

                    gridLayout.addView(imageButton);

                }

            }
            else
            {
                Toast.makeText(ProductImagesActivity.this, "获取列表失败.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
