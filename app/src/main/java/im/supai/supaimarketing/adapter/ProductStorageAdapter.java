package im.supai.supaimarketing.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.action.CartAction;
import im.supai.supaimarketing.action.ProductAction;
import im.supai.supaimarketing.action.StoreAction;
import im.supai.supaimarketing.activity.CartActivity;
import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.model.Product;
import im.supai.supaimarketing.util.CommonUtils;

/**
 * Created by viator42 on 15/9/2.
 */
public class ProductStorageAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    public ProductStorageAdapter(List<Map<String,Object>> list, Activity context)
    {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.storage_product_item, null);

            holder.alias=(TextView)convertView.findViewById(R.id.alias);
            holder.count=(TextView)convertView.findViewById(R.id.count);
            holder.increase = (Button)convertView.findViewById(R.id.increase);
            holder.img = (ImageView)convertView.findViewById(R.id.img);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.alias.setText(list.get(position).get("alias").toString());
        holder.count.setText(list.get(position).get("count").toString());

//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(list.get(position).get("img").toString()), holder.img);

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView view1 = (TextView) view;

                final EditText countEditText = new EditText(context);
                countEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("输入进货数量");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(new EditText(context));
                builder.setView(countEditText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //更新商品数量
                        Product product = new Product();

                        product.setId((Long) list.get(position).get("id"));

                        if (!countEditText.getText().toString().isEmpty()) {
                            int count = Integer.valueOf(countEditText.getText().toString());
                            if (count > 0) {
                                product.setCount(count);
                                new ProductIncreaseTask().execute(product);

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
        ImageView img;
        TextView alias;
        TextView count;
        Button increase;

    }

    //商品进货任务
    public class ProductIncreaseTask extends AsyncTask<Product, Void, Product>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Product doInBackground(Product... product) {
            return new ProductAction().countIncrease(product[0]);

        }

        @Override
        protected void onPostExecute(Product result) {
            super.onPostExecute(result);

//            context.reloadLowInStoreProducts();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addItems(List<Map<String,Object>> list)
    {
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }

}
