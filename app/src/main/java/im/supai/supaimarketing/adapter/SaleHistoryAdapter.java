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
 * Created by viator42 on 15/10/10.
 */
public class SaleHistoryAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文//构造器
    public SaleHistoryAdapter(List<Map<String,Object>> list,Context context){
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
            convertView =inflater.inflate(R.layout.sale_order_item, null);

            holder.createTimeTextView=(TextView)convertView.findViewById(R.id.create_time);
            holder.clerkNameTextView=(TextView)convertView.findViewById(R.id.clerk_name);
            holder.countTextView=(TextView)convertView.findViewById(R.id.count);
            holder.summaryTextView=(TextView)convertView.findViewById(R.id.summary);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.createTimeTextView.setText(list.get(position).get("createTime").toString());
        holder.clerkNameTextView.setText(list.get(position).get("clerkName").toString());
        holder.countTextView.setText(list.get(position).get("count").toString());
        holder.summaryTextView.setText(list.get(position).get("summary").toString());

        return convertView;
    }

    static class ViewHolder {
        TextView createTimeTextView;
        TextView clerkNameTextView;
        TextView countTextView;
        TextView summaryTextView;

    }

    public void addItems(List<Map<String,Object>> list)
    {
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }

}
