package im.supai.supaimarketing.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import im.supai.supaimarketing.R;
import im.supai.supaimarketing.activity.CartActivity;
import im.supai.supaimarketing.activity.ClerksActivity;
import im.supai.supaimarketing.model.Cart;
import im.supai.supaimarketing.util.CommonUtils;

/**
 * Created by viator42 on 15/9/17.
 */
public class ClerkAdapter extends BaseAdapter {

    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private ClerksActivity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public ClerkAdapter(List<Map<String,Object>> list,ClerksActivity context){
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
        return (Long)(list.get(position).get("id"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null){
            holder=new ViewHolder();
            convertView =inflater.inflate(R.layout.clerk_item, null);

            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.icon = (ImageView)convertView.findViewById(R.id.icon);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.name.setText(list.get(position).get("name").toString());
//        ImageLoader.getInstance().displayImage(CommonUtils.getImgFullpath(list.get(position).get("icon").toString()), holder.icon);

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();


        return convertView;
    }

    static class ViewHolder {
        TextView name;
        ImageView icon;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
