package com.cadyd.app.ui.fragment.mall;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.Comment;
import com.cadyd.app.model.CommentCount;
import com.cadyd.app.model.CommentPage;
import com.cadyd.app.model.PicInfo;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.CommentFixGridLayout;
import com.cadyd.app.ui.window.BabyPopWindow;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.MyListView;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品评论列表
 *
 * @author wcy
 */
public class GoodsCommentFragment extends BaseFragement {
    @Bind(R.id.pullListView)
    PullToRefreshScrollView pullListView;
    @Bind(R.id.certification)
    CommentFixGridLayout certification;
    @Bind(R.id.detail_rg)
    RadioGroup detailRg;
    @Bind(R.id.list_view)
    MyListView listView;
    @Bind(R.id.all)
    RadioButton all;
    @Bind(R.id.good)
    RadioButton good;
    @Bind(R.id.conetent)
    RadioButton conetent;
    @Bind(R.id.bad)
    RadioButton bad;
    @Bind(R.id.image)
    RadioButton image;
    String lable = "";
    String level = "";
    int page = 1;
    private String gid;
    CommonAdapter<Comment> adapter;
    List<Comment> mlist;

    public static GoodsCommentFragment newInstance(String id, boolean isImage) {
        GoodsCommentFragment newFragment = new GoodsCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putBoolean("isImage", isImage);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.gid = args.getString("id");
            if (args.getBoolean("isImage")) {
                level = "img";
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_comment, "商品评价", true);
    }

    @Override
    protected void initView() {
        setPullListView();
        if (level.equals("img")) {
            image.setChecked(true);
        }
        detailRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.all:
                        level = "";
                        break;
                    case R.id.good:
                        level = "nice";
                        break;
                    case R.id.conetent:
                        level = "general";
                        break;
                    case R.id.bad:
                        level = "bad";
                        break;
                    case R.id.image:
                        level = "img";
                        break;
                }
                mlist.clear();
                loadingComment();
            }
        });
        mlist = new ArrayList<>();
        adapter = new CommonAdapter<Comment>(activity, mlist, R.layout.goods_comment_list_iteam) {
            @Override
            public void convert(ViewHolder helper, Comment item) {
                ImageView civ = helper.getView(R.id.user_image);
                ApiClient.displayImage(item.photo, civ, R.mipmap.user_default);
                helper.setText(R.id.user_name, item.uname);
                helper.setText(R.id.time, item.created);
                helper.setText(R.id.content, item.content);
                helper.setText(R.id.data, "购买时间：" + item.created);
                helper.setText(R.id.salemix, item.salemix);
                RatingBar rb = helper.getView(R.id.room_ratingbar);
                rb.setRating(NumberUtil.getFloat(item.startlevel));
                GridView gv = helper.getView(R.id.comment_images);
                if (item.imgList != null) {
                    gv.setVisibility(View.VISIBLE);
                    CommonAdapter<PicInfo> cententMneuAdapter = new CommonAdapter<PicInfo>(activity, item.imgList, R.layout.gridview_comment_item) {
                        @Override
                        public void convert(ViewHolder helper, PicInfo pic) {
                            ImageView iv = helper.getView(R.id.image);
                            ApiClient.displayImage(pic.thumb, iv);
                        }
                    };
                    gv.setAdapter(cententMneuAdapter);
                } else {
                    gv.setVisibility(View.GONE);
                }
            }
        };
        listView.setAdapter(adapter);
        certification.setOnItemClickListener(new BabyPopWindow.OnItemClickListener() {
            @Override
            public void onClickOKPop() {
                lable = certification.getLable();
                loadingComment();
            }
        });
        loadingCount();
    }

    private void setPullListView() {
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullDownToRefresh");
                RefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullUpToRefresh");
                RefreshComplete();
            }
        });
    }

    private void RefreshComplete() {
        pullListView.onRefreshComplete();
    }

    private void loadingCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", gid);
        ApiClient.send(activity, JConstant.GOODS_COMMENT_COUNT_, map, CommentCount.class, new DataLoader<CommentCount>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(CommentCount data) {
                if (StringUtil.hasText(data.cval)) {
                    certification.setList(data.cval);
                    certification.setVisibility(View.VISIBLE);
                } else {
                    certification.setVisibility(View.GONE);
                }
                all.setText("全部\n" + data.total);
                good.setText("好评\n" + data.nice);
                conetent.setText("中评\n" + data.general);
                bad.setText("差评\n" + data.bad);
                image.setText("有图\n" + data.img);
                loadingComment();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.GOODS_COMMENT_COUNT_);
    }

    private void loadingComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", gid);
        map.put("pageIndex", page);
        map.put("level", level);
        map.put("pageSize" , 10);
        if (StringUtil.hasText(lable) && !lable.equals("全部")) {
            map.put("label", lable);
        }
        ApiClient.send(activity, JConstant.GOODS_COMMENT_, map, CommentPage.class, new DataLoader<CommentPage>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(CommentPage data) {
                if (data != null && data.data != null) {
                    mlist.addAll(data.data);
                    adapter.notifyDataSetChanged();
                    if (page == data.totalPage) {
                        pullListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.GOODS_COMMENT_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.GOODS_COMMENT_);
        ApiClient.cancelRequest(JConstant.GOODS_COMMENT_COUNT_);
        ApiClient.cancelRequest(JConstant.GOODS_COMMENT_);
    }
}
