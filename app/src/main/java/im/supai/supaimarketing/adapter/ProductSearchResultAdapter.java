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
 * Created by viator42 on 15/9/19.
 */
public class ProductSearchResultAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文//构造器
    public ProductSearchResultAdapter(List<Map<String,Object>> list,Context context){
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.product_search_result_item, null);

            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.name=(TextView)convertView.findViewById(R.id.name);
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

        holder.name.setText(list.get(position).get("alias").toString());
        holder.price.setText(list.get(position).get("price").toString());

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView name;
        TextView price;

    }
}
