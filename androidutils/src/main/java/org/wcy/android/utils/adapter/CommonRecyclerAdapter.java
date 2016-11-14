package org.wcy.android.utils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 为所有ListView\GridView等提供通用adapter
 *
 * @author Altas
 * @date 2014年9月2日
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewRecyclerHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mDatas;
    protected int mItemLayoutId;

    public CommonRecyclerAdapter(Context context, List<T> datas, int itemLayoutId) {
        super();
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = datas;
        this.mItemLayoutId = itemLayoutId;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewRecyclerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mItemLayoutId, viewGroup, false);
        ViewRecyclerHolder vh = new ViewRecyclerHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewRecyclerHolder viewHolder, int position) {
        this.position = position;
        convert(viewHolder, mDatas.get(position));
    }

    /**
     * 为item设置值
     *
     * @param helper
     * @param item
     */
    public abstract void convert(ViewRecyclerHolder helper, T item);

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    protected int position;

}
