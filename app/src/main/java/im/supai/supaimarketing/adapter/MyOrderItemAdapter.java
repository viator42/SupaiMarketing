package im.supai.supaimarketing.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.R;

/**
 * Created by viator42 on 15/4/2.
 */
public class MyOrderItemAdapter extends BaseAdapter
{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Context context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public MyOrderItemAdapter(List<Map<String,Object>> list,Context context){
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
        return (Long)(list.get(position).get("id"));

    }

    //核心部分，返回Listview视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.myorder_item, null);
            holder.storeName=(TextView)convertView.findViewById(R.id.store_name);
            holder.createTime=(TextView)convertView.findViewById(R.id.create_time);
            holder.moneyAmount=(TextView)convertView.findViewById(R.id.money_amount);
            holder.stauts=(TextView)convertView.findViewById(R.id.status);


            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.storeName.setText(list.get(position).get("storeName").toString());
        holder.createTime.setText(list.get(position).get("createTime").toString());
        holder.moneyAmount.setText(list.get(position).get("summary").toString());
        if((Integer)list.get(position).get("readed") == 1)
        {
            convertView.setBackgroundColor(this.context.getResources().getColor(R.color.listview_highlight));

        }
        holder.stauts.setText(list.get(position).get("status").toString());

        return convertView;

    }

    static class ViewHolder {
        TextView storeName;
        TextView createTime;
        TextView moneyAmount;
        TextView stauts;

    }

    public void updateList(List<Map<String,Object>> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
