package com.cadyd.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.model.ChatMesgInfo;
import com.cadyd.app.ui.push.gles.Config;

import java.util.ArrayList;

/**
 * Created by root on 16-9-2.
 */

public class ChatListAdatper extends BaseAdapter {
    ArrayList<ChatMesgInfo> chatMeslist;
    private Context mcontext;
    private LayoutInflater inflater;

    public ChatListAdatper(Context context, ArrayList<ChatMesgInfo> chatMeslist) {
        this.mcontext = context;
        this.chatMeslist = chatMeslist;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return chatMeslist.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMeslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(ArrayList<ChatMesgInfo> chatMeslist) {
        this.chatMeslist = chatMeslist;
        notifyDataSetChanged();
    }

    private HolderView holderView;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chatlistview, null);
            holderView = new HolderView();
            holderView.content = (TextView) convertView.findViewById(R.id.item_chatList);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        ChatMesgInfo info = chatMeslist.get(position);

        int textColor = mcontext.getResources().getColor(R.color.CREATE_ROOM);
        switch (info.getEvenType()) {
            case Config.SOCKETCREATEROOM:
                textColor = mcontext.getResources().getColor(R.color.CREATE_ROOM);
                holderView.content.setTextColor(textColor);
                holderView.content.setText(info.getContent());
                break;
            case Config.SOCKETCONNECT:
                textColor = mcontext.getResources().getColor(R.color.CREATE_ROOM);
                holderView.content.setTextColor(textColor);
                holderView.content.setText(info.getContent());
                break;
            case Config.SOCKETCHATGIFT:
                textColor = mcontext.getResources().getColor(R.color.CHATGIFT);
                holderView.content.setTextColor(textColor);
                holderView.content.setText(info.getContent());
                break;
            case Config.SOCKETCHATMSG:
                textColor = mcontext.getResources().getColor(R.color.CHATMSG);
                holderView.content.setTextColor(textColor);
                holderView.content.setText(info.getContent());
                break;

            case Config.SOCKETCONCERN:
                textColor = mcontext.getResources().getColor(R.color.CONCERN);
                holderView.content.setTextColor(textColor);
                holderView.content.setText(info.getContent());
                break;
            case Config.SOCKETUNCONCERN:
                textColor = mcontext.getResources().getColor(R.color.UN_CONCERN);
                holderView.content.setTextColor(textColor);
                holderView.content.setText(info.getContent());
                break;
            case Config.SOCKETUNCONNECT:
                textColor = mcontext.getResources().getColor(R.color.UN_CONNECT);
                holderView.content.setTextColor(textColor);
                holderView.content.setText(info.getContent());
                break;
        }
        return convertView;
    }


    public class HolderView {
        private TextView content;
    }

}
