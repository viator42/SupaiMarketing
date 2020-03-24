package im.supai.supaimarketing.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.SalesAction;
import im.supai.supaimarketing.activity.CartActivity;
import im.supai.supaimarketing.activity.MainActivity;
import im.supai.supaimarketing.activity.SalesActivity;
import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.model.Sale;
import im.supai.supaimarketing.util.CommonUtils;

/**
 * Created by viator42 on 15/9/21.
 */
public class RroductSalesAdapter extends BaseAdapter
{

    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

//    private DisplayImageOptions options;

    //上下文//构造器
    public RroductSalesAdapter(List<Map<String,Object>> list,SalesActivity context){
        this.context=context;
        this.list=list;
        inflater= LayoutInflater.from(context);

//        options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .build();

    }

    private SalesActivity context;

    //用来导入布局
    private LayoutInflater inflater =null;

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
        return (Long)(list.get(position).get("id"));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.product_sales_item, null);

            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.price=(TextView)convertView.findViewById(R.id.price);
            holder.count = (EditText) convertView.findViewById(R.id.count);
            holder.summary = (TextView) convertView.findViewById(R.id.summary);
            holder.removeBtn = (ImageView) convertView.findViewById(R.id.remove);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_product) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_product) // resource or drawable
//                .showImageOnFail(R.drawable.ic_product) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(list.get(position).get("img").toString()), holder.img, options);

        holder.name.setText(list.get(position).get("alias").toString());
        holder.price.setText(list.get(position).get("price").toString());
        holder.count.setText(list.get(position).get("count").toString());
        holder.summary.setText(list.get(position).get("summary").toString());

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否移除该商品");
                builder.setTitle("提示");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long id = (Long)list.get(position).get("id");

                        new RemoveTask().execute(id);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                builder.create().show();
            }
        });

        holder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        long id = (Long) list.get(position).get("id");
                        int count = Integer.valueOf(countEditText.getText().toString());
                        double price = (Double) list.get(position).get("price");

                        if (!countEditText.getText().toString().isEmpty()) {
                            Sale sale = new Sale();
                            sale.setId(id);
                            sale.setPrice(price);
                            sale.setCount(count);

                            new EditTask().execute(sale);


                        } else {
                            Toast.makeText(context, "商品数量不能为空", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView name;
        TextView price;
        EditText count;
        TextView summary;
        ImageView removeBtn;

    }

    //删除商品
    public class RemoveTask extends AsyncTask<Long, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... id) {
            return new SalesAction().remove(id[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true)
            {
                //重新读取
//                context.reloadList();

            }
        }
    }

    //商品修改
    public class EditTask extends AsyncTask<Sale, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Sale... sale) {
            return new SalesAction().edit(sale[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true)
            {
                //重新读取
//                context.reloadList();

            }
        }
    }
}
