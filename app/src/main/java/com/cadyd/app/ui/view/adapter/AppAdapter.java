package com.cadyd.app.ui.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.Good;
import com.cadyd.app.ui.activity.GoodsDetailActivity;

/**
 * 用于GridView装载数据的适配器
 *
 * @author xxs
 */
public class AppAdapter extends BaseAdapter implements OnItemClickListener {
    private List<Good> mList;// 定义一个list对象
    private Context mContext;// 上下文
    public static final int APP_PAGE_SIZE = 4;// 每一页装载数据的大小
    private PackageManager pm;// 定义一个PackageManager对象

    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param list    所有APP的集合
     * @param page    当前页
     */
    public AppAdapter(Context context, List<Good> list, int page) {
        mContext = context;
        pm = context.getPackageManager();
        this.page = page;
        mList = new ArrayList<Good>();
        // 根据当前页计算装载的应用，每页只装载4个
        int i = page * APP_PAGE_SIZE;// 当前页的其实位置
        int iEnd = i + APP_PAGE_SIZE;// 所有数据的结束位置
        while ((i < list.size()) && (i < iEnd)) {
            mList.add(list.get(i));
            i++;
        }
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder appItem;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.goodes_detail_iteam, parent, false);
            appItem = new ViewHolder();
            appItem.image_view = (ImageView) convertView.findViewById(R.id.image_view);
            appItem.name = (TextView) convertView.findViewById(R.id.name);
            appItem.price = (TextView) convertView.findViewById(R.id.price);
            appItem.bought = (TextView) convertView.findViewById(R.id.bought);
            convertView.setTag(appItem);
        } else {
            appItem = (ViewHolder) convertView.getTag();
        }
        final Good appInfo = mList.get(position);
        if (appInfo != null) {
            convertView.setVisibility(View.VISIBLE);
            ApiClient.ImageLoadersRounde(appInfo.thumb, appItem.image_view,7);
            appItem.name.setText(appInfo.title);
            appItem.price.setTextColor(mContext.getResources().getColor(R.color.Orange));
            StringBuffer sb = new StringBuffer("￥");
            sb.append(appInfo.price == null ? "0.00" : appInfo.price);
            SpannableStringBuilder builder = new SpannableStringBuilder(sb.toString());
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.more_text));
            builder.setSpan(redSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(30), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(40), 1, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            appItem.price.setText(builder);
            appItem.bought.setText("已有" + appInfo.bought + "人购买");
        } else {
            convertView.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("gid", appInfo.id);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


    }

    private class ViewHolder {
        ImageView image_view;
        TextView name;
        TextView price;
        TextView bought;
    }
}
