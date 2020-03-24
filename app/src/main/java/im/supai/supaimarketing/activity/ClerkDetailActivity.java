package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.SalesAction;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;
import im.supai.supaimarketing.widget.TimeoutbleProgressDialog;
import im.supai.supaimarketing.R;

public class ClerkDetailActivity extends AppCompatActivity {

    private AppContext context;
    private User clerk;
    private ImageView iconImageView;
    private TextView nameTextView;
    private TextView telTextView;
    private Button removeBtn;

    private TimeoutbleProgressDialog removeClerkDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_detail);

        context = (AppContext) getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        clerk = bundle.getParcelable("obj");

        iconImageView = (ImageView) findViewById(R.id.icon);
        nameTextView = (TextView) findViewById(R.id.name);
        telTextView = (TextView) findViewById(R.id.tel);
        removeBtn = (Button) findViewById(R.id.remove);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClerkDetailActivity.this);
                builder.setMessage("是否解雇员工 "+clerk.getName());
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new RemoveClerkTask().execute(clerk.getId());

                    }
                });
                builder.setNegativeButton("取消", null);

                builder.create().show();

            }
        });

        removeClerkDialog = TimeoutbleProgressDialog.createProgressDialog(ClerkDetailActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                removeClerkDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(ClerkDetailActivity.this);

                Toast.makeText(ClerkDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void setValues()
    {
        nameTextView.setText(clerk.getName());
        telTextView.setText(clerk.getTel());

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_user) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_user) // resource or drawable
//                .showImageOnFail(R.drawable.ic_user) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(clerk.getIconUrl()), iconImageView, options);

    }

    //解雇员工任务
    public class RemoveClerkTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            removeClerkDialog.show();

        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new SalesAction().clerkRemove(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            removeClerkDialog.dismiss();

            if(result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClerkDetailActivity.this);
                builder.setMessage("操作成功");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClerkDetailActivity.this.finish();

                    }
                });

                builder.create().show();

            }
            else
            {
                Toast.makeText(ClerkDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
