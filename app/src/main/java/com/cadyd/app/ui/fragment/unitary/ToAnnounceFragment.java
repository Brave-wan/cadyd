package com.cadyd.app.ui.fragment.unitary;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import butterknife.Bind;
import com.cadyd.app.R;
import com.cadyd.app.model.OneGoodsModel;
import com.cadyd.app.model.RecordModleData;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.unitary.OneYuanDetailsFragment;

/**
 * 期数选择
 **/
public class ToAnnounceFragment extends BaseFragement {

    @Bind(R.id.ui_announce_grid)
    GridView gridView;

    private String tbid;


    private LayoutInflater inflater;

    public String newHasTime;

    public int HASRIME = 1;

    public static ToAnnounceFragment newInstance(String tbid) {
        ToAnnounceFragment newFragment = new ToAnnounceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tbid", tbid);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tbid = args.getString("tbid");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.activity_to_announce_fragment, "期数选择", true);
    }

    @Override
    protected void initView() {
        inflater = LayoutInflater.from(activity);
        gridView.setAdapter(adapter);
    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return HASRIME;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.list_text_item, null);
                holder.textView = (TextView) view.findViewById(R.id.list_item_text);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (position == 0) {
                holder.textView.setText("第" + (Integer.valueOf(newHasTime) - position) + "期(进行中)");
                holder.textView.setTextColor(getResources().getColor(R.color.red));
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                holder.textView.setText("第" + (Integer.valueOf(newHasTime) - position) + "期");
                holder.textView.setTextColor(getResources().getColor(R.color.gray));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        OneYuanDetailsFragment oneYuanDetailsFragment = OneYuanDetailsFragment.newInstance(tbid, newHasTime);
                        oneYuanDetailsFragment.HASRIME = (Integer.valueOf(newHasTime) - position);
                        replaceFragment(R.id.common_frame, oneYuanDetailsFragment);
                    }
                });
            }
            view.setBackgroundResource(R.color.white);

            return view;
        }
    };

    private class ViewHolder {
        TextView textView;
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }
}


