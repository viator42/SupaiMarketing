package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CartAction;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

public class ProductViewonlyActivity extends AppCompatActivity {
    protected TextView nameTextView;
    protected TextView descriptionTextView;
    protected TextView merchantTextView;
    protected TextView originTextView;
    protected TextView priceTextView;
    protected ImageView imgView;
    protected GridLayout goodsInfoLayout;
    private TextView countTextView;

    protected Button editBtn;
    private User user;
    private Product product;
    protected long id;
    private boolean editable = false;
    protected AppContext context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_viewonly);

        context = (AppContext)getApplicationContext();
        user = context.getUser();

        Bundle bundle = this.getIntent().getExtras();
//        id = bundle.getLong("id");
        //new ProductDetailTask().execute(id);
        product = bundle.getParcelable("obj");
        editable = bundle.getBoolean("editable");

        nameTextView = (TextView) findViewById(R.id.alias);
        descriptionTextView = (TextView) findViewById(R.id.description);
        merchantTextView = (TextView) findViewById(R.id.merchant);
        originTextView = (TextView) findViewById(R.id.origin);
        priceTextView = (TextView) findViewById(R.id.price);
        imgView = (ImageView) findViewById(R.id.img);
        countTextView = (TextView) findViewById(R.id.count);

        goodsInfoLayout = (GridLayout) findViewById(R.id.goods_info);
        if(product.getGoodsId() == 0)
        {
            goodsInfoLayout.setVisibility(View.GONE);
        }

        setVales();

        editBtn = (Button) findViewById(R.id.edit);
        editBtn.setText("商品修改");
        editBtn.setTextColor(getResources().getColor(R.color.button_text));
        editBtn.setBackgroundColor(getResources().getColor(R.color.button));
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductViewonlyActivity.this, ProductEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", product);
                intent.putExtras(bundle);
                startActivityForResult(intent, StaticValues.PRODUCT_EDIT);

            }
        });

    }

    //属性赋值
    private void setVales()
    {
        nameTextView.setText(product.getAlias());
        if(!CommonUtils.isValueEmpty(product.getAdditional()))
        {
            descriptionTextView.setText(product.getAdditional());
        }
        merchantTextView.setText(product.getMerchant());
        originTextView.setText(product.getOrigin());
        priceTextView.setText(Double.toString(product.getPrice()));
        countTextView.setText(Integer.toString(product.getCount()));

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_product) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_product) // resource or drawable
//                .showImageOnFail(R.drawable.ic_product) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(product.getImgUrl()), imgView, options);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode)
        {
            case StaticValues.PRODUCT_EDIT:
                Bundle bundle = data.getExtras();
                product = bundle.getParcelable("obj");
                setVales();

                break;

        }

    }
}
