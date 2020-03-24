package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.MasterAction;
import im.supai.supaimarketing.R;

public class MasterActivity extends AppCompatActivity {

    private TextView addProTelTextView;
    private Button addProBtn;
    private AppContext context;
    private String imie;
    private String addProTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        context = (AppContext) getApplicationContext();
        imie = context.getImie();

        addProTelTextView = (TextView) findViewById(R.id.add_pro_tel);
        addProBtn = (Button) findViewById(R.id.add_pro);
        addProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProTel = addProTelTextView.getText().toString();
                if(!addProTel.isEmpty())
                {
                    new AddProTask().execute();

                }
                else
                {
                    Toast.makeText(MasterActivity.this, "电话号码不能为空", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public class AddProTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return new MasterAction().addProManually(imie, addProTel, 1);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true)
            {
                Toast.makeText(MasterActivity.this, "高级功能添加成功", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MasterActivity.this, "高级功能添加失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
