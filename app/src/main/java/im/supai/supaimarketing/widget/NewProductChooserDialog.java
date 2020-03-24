package im.supai.supaimarketing.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.activity.NewProductManuallyActivity;

/**
 * Created by viator42 on 15/6/7.
 */
public class NewProductChooserDialog
{
    private Dialog dialog;
    ImageView barcode;
    ImageView manual;


    public NewProductChooserDialog(final Context context)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.new_product_chooser_dialog);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);

        barcode = (ImageView) dialog.findViewById(R.id.barcode);
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, im.supai.supaimarketing.zxing.CaptureActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("action", 1);
//                bundle.putLong("storeId", 0);
//                intent.putExtras(bundle);
//                context.startActivity(intent);

                dialog.dismiss();
            }
        });

        manual = (ImageView) dialog.findViewById(R.id.manual);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewProductManuallyActivity.class);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });

    }

    public void show() {
        dialog.show();
    }
}
