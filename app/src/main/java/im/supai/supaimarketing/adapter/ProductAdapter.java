package im.supai.supaimarketing.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/5/9.
 * 搜索结果列表Adaper
 */
public class ProductAdapter extends BaseAdapter
{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

//    private DisplayImageOptions options;

    //上下文//构造器
    public ProductAdapter(List<Map<String,Object>> list,Context context){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);

//        options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .build();

    }

    private Context context;

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
        // TODO Auto-generated method stub
        return (Long)(list.get(position).get("id"));

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.product_item, null);

            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.storeName=(TextView)convertView.findViewById(R.id.store_name);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            holder.productName=(TextView)convertView.findViewById(R.id.product_name);
            holder.price=(TextView)convertView.findViewById(R.id.price);

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

//        GlideApp.with(context)
//                .load(appBrandAll.logopic)
//                .placeholder(R.drawable.ic_product)
//                .centerCrop()
//                .into(holder.img);

        holder.storeName.setText(list.get(position).get("storeName").toString());
        holder.address.setText(list.get(position).get("address").toString());
        holder.productName.setText(list.get(position).get("productName").toString());
        holder.price.setText("单价 "+list.get(position).get("price").toString());

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView storeName;
        TextView address;
        TextView productName;
        TextView price;

    }
}
