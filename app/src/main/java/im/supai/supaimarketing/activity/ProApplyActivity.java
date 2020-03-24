package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CollectAction;
import im.supai.supaimarketing.action.ModuleAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.model.Module;
import im.supai.supaimarketing.model.ModuleCategory;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.R;

public class ProApplyActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText telEditText;
    private EditText licenseEditText;
    private Button applyBtn;
    private AppContext context;
    private User user;
    private long bundleId = 1;
    private String cdkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_apply);

        context = (AppContext) getApplicationContext();
        user = context.getUser();

        nameEditText = (EditText) findViewById(R.id.name);
        addressEditText = (EditText) findViewById(R.id.address);
        telEditText = (EditText) findViewById(R.id.tel);
        licenseEditText = (EditText) findViewById(R.id.cdkey);
        applyBtn = (Button) findViewById(R.id.apply);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Module module = new Module();
//                module.setCategoryId(moduleCategoryId);
                module.setUserid(user.getId());
                module.setUsername(nameEditText.getText().toString());
                module.setTel(telEditText.getText().toString());
                module.setAddress(addressEditText.getText().toString());
                module.setLicense(licenseEditText.getText().toString());
                module.setBundleId(bundleId);

                if(module.applyValidate())
                {
                    new ModuleApplyTask().execute(module);
                }
                else
                {
                    Toast.makeText(ProApplyActivity.this, module.getValidationMsg(), Toast.LENGTH_SHORT).show();

                }
            }
        });

//        Bundle bundle = this.getIntent().getExtras();
//        moduleCategoryId = bundle.getLong("type");

    }

    @Override
    protected void onStart() {
        super.onStart();

        nameEditText.setText(user.getName());
        addressEditText.setText(user.getAddress());
        telEditText.setText(user.getTel());

    }

    //提交申请
    public class ModuleApplyTask extends AsyncTask<Module, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Module... module) {
            return new ModuleAction().apply(module[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProApplyActivity.this);
                builder.setMessage("提交成功,我们会尽快与您取得联系");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

            }
            else
            {
                Toast.makeText(ProApplyActivity.this, "提交失败", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
