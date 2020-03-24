package im.supai.supaimarketing.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.activity.NewProductManuallyActivity;
import im.supai.supaimarketing.model.Store;

/**
 * Created by viator42 on 15/7/17.
 * 店铺详情描述 弹出窗口
 */
public class StoreDescriptionDialog
{
    private Dialog dialog;
    private TextView descriptionTextView;
    private TextView nameTextView;
    private Button closeBtn;

    private String name;
    private String description;


    public StoreDescriptionDialog(final Context context, Store store)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.store_description_dialog);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);

        descriptionTextView = (TextView) dialog.findViewById(R.id.description);
        nameTextView = (TextView) dialog.findViewById(R.id.name);

        closeBtn = (Button) dialog.findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        name = store.getName();

        nameTextView.setText(name);
        description = store.getDescription();
        if(description != null && !description.isEmpty())
        {
            descriptionTextView.setText(description);

        }

    }

    public void show() {
        dialog.show();
    }

}
