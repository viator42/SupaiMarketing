package im.supai.supaimarketing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.util.StaticValues;

public class PasswordChangeActivity extends AppCompatActivity {
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText newPasswordAgaginEditText;

    private TableLayout oldPasswordLayout;

    private Button cancelBtn;
    private Button confirmBtn;

    private int passtype;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        Bundle bundle = this.getIntent().getExtras();
        passtype = bundle.getInt("passtype", 1);
        password = bundle.getString("password", "");

        oldPasswordEditText = (EditText) findViewById(R.id.old_password);
        newPasswordEditText = (EditText) findViewById(R.id.new_password);
        newPasswordAgaginEditText = (EditText) findViewById(R.id.new_password_agagin);

        oldPasswordLayout = (TableLayout) findViewById(R.id.old_password_layout);

        cancelBtn = (Button) findViewById(R.id.cancel);
        confirmBtn = (Button) findViewById(R.id.confirm);

        if(passtype == 1)
        {
            oldPasswordLayout.setVisibility(View.GONE);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordChangeActivity.this.finish();

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newPassword = newPasswordEditText.getText().toString();
                final String newPasswordAgain = newPasswordAgaginEditText.getText().toString();
                if(!newPassword.isEmpty() && !newPasswordAgain.isEmpty())
                {
                    if(newPassword.equals(newPasswordAgain))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                        builder.setMessage("确定修改密码");
                        builder.setTitle("提示");

                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                //跳转回设置页面
                                Intent intent = new Intent(PasswordChangeActivity.this, ProfileEditActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("password_new", newPassword);
                                intent.putExtras(bundle);

                                setResult(StaticValues.USER_EDIT, intent);
                                finish();

                            }
                        });

                        builder.setNegativeButton("取消", null);

                        builder.create().show();

                    }
                    else
                    {
                        Toast.makeText(PasswordChangeActivity.this, "两次填写的密码必须一致", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(PasswordChangeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
