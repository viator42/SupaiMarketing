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
import im.supai.supaimarketing.util.CommonUtils;

/**
 * Created by viator42 on 15/5/10.
 * 商铺搜索列表Adaper
 */
public class StoreAdapter  extends BaseAdapter
{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();


    //上下文//构造器
    public StoreAdapter(List<Map<String,Object>> list,Context context){
        this.context=context;
        this.list=list;
        inflater= LayoutInflater.from(context);
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
        return (Long)(list.get(position).get("id"));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.store_item, null);

            holder.logo=(ImageView)convertView.findViewById(R.id.logo);
            holder.name=(TextView)convertView.findViewById(R.id.alias);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            holder.distance=(TextView)convertView.findViewById(R.id.distance);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.ic_store_default) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_store_default) // resource or drawable
//                .showImageOnFail(R.drawable.ic_store_default) // resource or drawable
//                .build();
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(list.get(position).get("logo").toString()), holder.logo, options);

        holder.name.setText(list.get(position).get("name").toString());
        holder.address.setText(list.get(position).get("address").toString());
        if(list.get(position).get("distance") != null)
        {
            holder.distance.setText(CommonUtils.distanceFormat((Double) list.get(position).get("distance"))+"米");

        }

        return convertView;
    }

    static class ViewHolder {
        ImageView logo;
        TextView name;
        TextView address;
        TextView distance;

    }

}
