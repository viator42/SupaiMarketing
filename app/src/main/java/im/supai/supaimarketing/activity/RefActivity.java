package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.StaticValues;

public class RefActivity extends AppCompatActivity {
    private AppContext context;
    private User user;
    private String content;

    private TextView nameTextView;
    private TextView telTextView;
    private EditText contentEditText;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ref);

        context = (AppContext) getApplicationContext();
        user = context.getUser();

        nameTextView = (TextView) findViewById(R.id.name);
        telTextView = (TextView) findViewById(R.id.tel);
        contentEditText = (EditText) findViewById(R.id.content);
        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = contentEditText.getText().toString();
                if(content == null || content.isEmpty())
                {
                    Toast.makeText(RefActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AddRefTask().execute();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.setValue();

    }

    private void setValue()
    {
        nameTextView.setText(user.getName());
        telTextView.setText(user.getTel());

    }

    //取消收藏
    public class AddRefTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            boolean result = false;
            result =  new UserAction().ref(user.getId(), content);

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.this);
                builder.setMessage("反馈提交成功,感谢您对我们产品的支持");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        RefActivity.this.finish();

                    }
                });
                builder.create().show();

            }
            else
            {
                Toast.makeText(RefActivity.this, "反馈提交失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_order, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                this.onBackPressed();

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
