package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.UserAction;
import im.supai.supaimarketing.model.Appeal;
import im.supai.supaimarketing.R;

public class AppealActivity extends AppCompatActivity {
    private EditText oldTelEditText;
    private EditText newTelEditText;
    private EditText nameEditText;
    private EditText addressEditText;
    private AppContext context;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appeal);

        context = (AppContext) getApplicationContext();

        oldTelEditText = (EditText) findViewById(R.id.tel_old);
        newTelEditText = (EditText) findViewById(R.id.tel_new);
        nameEditText = (EditText) findViewById(R.id.name);
        addressEditText = (EditText) findViewById(R.id.address);
        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appeal appeal = new Appeal();
                appeal.setOldTel(oldTelEditText.getText().toString());
                appeal.setNewTel(newTelEditText.getText().toString());
                appeal.setName(nameEditText.getText().toString());
                appeal.setAddress(addressEditText.getText().toString());
                appeal.setImie(context.getImie());

                if (appeal.uploadVerify()) {
                    new AppealUploadTask().execute(appeal);
                } else {
                    Toast.makeText(AppealActivity.this, appeal.getVerifyMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public class AppealUploadTask extends AsyncTask<Appeal, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Appeal... appeal) {
            return new UserAction().appeal(appeal[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppealActivity.this);
                builder.setMessage("申请已提交");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        AppealActivity.this.finish();

                    }
                });
                builder.create().show();

            }
            else
            {
                Toast.makeText(AppealActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
