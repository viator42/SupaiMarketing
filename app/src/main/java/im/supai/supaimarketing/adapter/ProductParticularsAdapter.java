package im.supai.supaimarketing.adapter;

import android.content.Context;
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
 * Created by viator42 on 15/9/14.
 */
public class ProductParticularsAdapter extends BaseAdapter{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

//    private DisplayImageOptions options;
    private Context context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //上下文//构造器
    public ProductParticularsAdapter(List<Map<String,Object>> list,Context context){
        this.context=context;
        this.list=list;
        inflater= LayoutInflater.from(context);

//        options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .build();

    }

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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.particular_item, null);

            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.alias=(TextView)convertView.findViewById(R.id.alias);
            holder.price=(TextView)convertView.findViewById(R.id.price);
            holder.count=(TextView)convertView.findViewById(R.id.count);
            holder.summary=(TextView)convertView.findViewById(R.id.summary);

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

        holder.alias.setText(list.get(position).get("alias").toString());
        holder.price.setText(list.get(position).get("price").toString());
        holder.count.setText(list.get(position).get("count").toString());
        holder.summary.setText(list.get(position).get("summary").toString());

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView alias;
        TextView price;
        TextView count;
        TextView summary;

    }

    public void addItems(List<Map<String,Object>> list)
    {
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }

}
