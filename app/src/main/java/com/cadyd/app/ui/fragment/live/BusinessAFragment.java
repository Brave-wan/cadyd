package com.cadyd.app.ui.fragment.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cadyd.app.R;

import java.util.ArrayList;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class BusinessAFragment extends Fragment {
    private ArrayList<String> noticeList = new ArrayList<>();
    private ListView listView;
    private LayoutInflater mInflater;
    private View liveDetailsView;
    private TextView liveDetails_msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_businessa, null);
        Bundle bundle = getArguments();
        if (bundle != null) {
            noticeList = bundle.getStringArrayList("notice");
        }
        mInflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        liveDetailsView = view.findViewById(R.id.item_liveDetals);
        liveDetails_msg = (TextView) view.findViewById(R.id.liveDetails_msg);
        listView = (ListView) view.findViewById(R.id.businessa_listView);
        listView.setAdapter(new BusinessAdapter());
        listView.setEmptyView(liveDetailsView);
        liveDetails_msg.setText("商家暂无公告");
    }

    class BusinessAdapter extends BaseAdapter {

        public BusinessAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return noticeList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_businessa_listview, null);
                holder = new HolderView();
                holder.content = (TextView) convertView.findViewById(R.id.business_content);
                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }
            holder.content.setText(noticeList.get(position));
            return convertView;
        }
    }

    private HolderView holder;

    class HolderView {
        private TextView content;
    }
}
