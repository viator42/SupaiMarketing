package im.supai.supaimarketing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import im.supai.supaimarketing.R;
import im.supai.supaimarketing.util.BitmapTools;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/4/5.
 */
public class MyOrderProductAdaper extends BaseAdapter
{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Context context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public MyOrderProductAdaper(List<Map<String,Object>> list,Context context){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub

        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return (Long)(list.get(position).get("productId"));

    }

    //核心部分，返回Listview视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.myorder_product, null);
            holder.nameTextView=(TextView)convertView.findViewById(R.id.alias);
            holder.priceTextView=(TextView)convertView.findViewById(R.id.price);
            holder.countTextView=(TextView)convertView.findViewById(R.id.count);
            holder.imageView = (ImageView)convertView.findViewById(R.id.img);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.nameTextView.setText(list.get(position).get("name").toString());
        holder.priceTextView.setText(list.get(position).get("price").toString());
        holder.countTextView.setText(list.get(position).get("count").toString());

//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(list.get(position).get("img").toString()), holder.imageView);

        return convertView;

    }

    static class ViewHolder {

        TextView nameTextView;
        TextView priceTextView;
        TextView countTextView;
        ImageView imageView;

    }

}
