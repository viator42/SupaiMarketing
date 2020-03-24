package im.supai.supaimarketing.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CartAction;
import im.supai.supaimarketing.activity.CartActivity;
import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/4/22.
 */
public class CartAdaper extends BaseAdapter
{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private CartActivity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public CartAdaper(List<Map<String,Object>> list,CartActivity context){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    private int width;
    private int height;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //核心部分，返回Listview视图
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.cart_item, null);

            holder.name=(TextView)convertView.findViewById(R.id.alias);
            holder.price=(TextView)convertView.findViewById(R.id.price);
            holder.count=(EditText)convertView.findViewById(R.id.count);
            holder.remove = (ImageButton)convertView.findViewById(R.id.remove);
            holder.img = (ImageView)convertView.findViewById(R.id.img);

            holder.count.setKeyListener(null);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.name.setText(list.get(position).get("name").toString());
        holder.price.setText(list.get(position).get("price").toString());
        holder.count.setText(list.get(position).get("count").toString());

//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(list.get(position).get("img").toString()), holder.img);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new RemoveCartItemTask().execute((Long) list.get(position).get("id"));

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        holder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TextView view1 = (TextView) view;

                final EditText countEditText = new EditText(context);
                countEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                countEditText.setText(view1.getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("修改数量");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(new EditText(context));
                builder.setView(countEditText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //更新商品数量
                        Cart cart = new Cart();
                        cart.setId((Long) list.get(position).get("id"));
                        if (!countEditText.getText().toString().isEmpty()) {
                            int count = Integer.valueOf(countEditText.getText().toString());
                            if (count > 0) {
                                cart.setCount(count);
                                new ChangeItemCountTask().execute(cart);

                            }

                        }

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();


        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView price;
        EditText count;
        ImageButton remove;
        ImageView img;

    }

    //购物车删除任务
    public class RemoveCartItemTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new CartAction().remove(id[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                Toast.makeText(context, "商品已移除", Toast.LENGTH_SHORT).show();
//                context.reloadCartInfo();
            }
            else
            {
                Toast.makeText(context, "商品移除失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //购物车结算任务
    public class ChangeItemCountTask extends AsyncTask<Cart, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Cart... cart) {
            long id = cart[0].getId();
            int count  = cart[0].getCount();

            return new CartAction().updateCount(id, count);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

//            context.reloadCartInfo();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
