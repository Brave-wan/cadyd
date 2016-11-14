package com.cadyd.app.ui.fragment.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.R;
import com.cadyd.app.comm.FixedList;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.model.LivePersonalDetailIfon;
import com.cadyd.app.model.PeopleSayInfo;
import com.cadyd.app.model.SocketMode;
import com.cadyd.app.model.SocktServiceInfo;
import com.cadyd.app.ui.activity.SendGiftActivtiy;
import com.cadyd.app.utils.StringUtils;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 大家说
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class PeopleSayFragment extends Fragment implements View.OnClickListener {
    private ListView mListView;
    private LiveDetailChatAdapter mAdapter;
    private static List<SocketMode> list = new ArrayList<>();
    private View liveDetailsView;
    private TextView liveDetails_msg;
    private LinearLayout sendGift;
    private FragmentManager mFragmentManager;
    private String channelid;
    private EditText mVcontent;
    private LinearLayout sendMsg;
    private RelativeLayout rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peoplesay, null);
        mFragmentManager = getChildFragmentManager();
        Bundle bundle = getArguments();
        if (bundle != null) {
            info = (LivePersonalDetailIfon) bundle.getSerializable("info");
        }
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }


    private void initView(View view) {
        rootView = (RelativeLayout) view.findViewById(R.id.peopleSay_rootView);
        sendMsg = (LinearLayout) view.findViewById(R.id.peopler_sendMsg);
        mVcontent = (EditText) view.findViewById(R.id.void_content);
        sendGift = (LinearLayout) view.findViewById(R.id.peopler_sendGift);
        liveDetailsView = view.findViewById(R.id.item_liveDetals);
        liveDetails_msg = (TextView) view.findViewById(R.id.liveDetails_msg);
        mListView = (ListView) view.findViewById(R.id.peopleSay_listView);
        mListView.setEmptyView(liveDetailsView);
        liveDetails_msg.setText("暂无消息");

        mAdapter = new LiveDetailChatAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);
        sendGift.setOnClickListener(this);
        sendMsg.setOnClickListener(this);
        rootView.setOnClickListener(this);
    }

    /**
     * 更新消息列表
     *
     * @param info
     */
    FixedList<SocketMode> fixedList = new FixedList<>();

    public void onEventMainThread(SocketMode info) {
        Log.i("wan", " ------大家说消息列表,正在刷新列表-----");
        if (info != null) {
            fixedList.fixedList(50, list, info);
//            list.add(info);
            mAdapter.setData(list);
            mListView.setSelection(mListView.getBottom());
            FixedList<SocktServiceInfo> list = new FixedList<>();
        }
    }


    String conversationId = "afasgafbaifewmnmfa";
    LivePersonalDetailIfon info;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.peopleSay_rootView:
                KeyBoardUtils.closeKeybord(mVcontent, getActivity());
                break;
            case R.id.peopler_sendMsg:
                if (mVcontent.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "消息内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (info != null) {
                    Log.i("wan", "conversationId------" + conversationId + "------channel" + channelid);
                    PeopleSayInfo peopleSayInfo = new PeopleSayInfo();
                    peopleSayInfo.setMsg(mVcontent.getText().toString().trim());
                    EventBus.getDefault().post(peopleSayInfo);
                    mVcontent.setText("");
                }
                break;
            case R.id.peopler_sendGift:
                if (StringUtils.isEmpty(conversationId)) return;
                SendGiftActivtiy sendGiftActivtiy = new SendGiftActivtiy();
                Bundle bundle = new Bundle();
                bundle.putString("roomCode", conversationId);
                sendGiftActivtiy.setArguments(bundle);
                sendGiftActivtiy.show(mFragmentManager, "sendGif");

                break;
        }
    }
}
