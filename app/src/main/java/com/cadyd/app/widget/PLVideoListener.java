package com.cadyd.app.widget;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pili.pldroid.player.PLMediaPlayer;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class PLVideoListener implements PLMediaPlayer.OnErrorListener,PLMediaPlayer.OnInfoListener ,PLMediaPlayer.OnCompletionListener{
    private boolean mIsActivityPaused = true;
    private String TAG = "PLVideoListener";
    private Context mContext;

    public PLVideoListener(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        Log.d(TAG, "Play Completed !");
        showToastTips("Play Completed !");
    }
    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
        Log.d(TAG, "onInfo: " + what + ", " + extra);
        return false;
    }


    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
        Log.e(TAG, "Error happened, errorCode = " + errorCode);
        switch (errorCode) {
            case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                showToastTips("Invalid URL !");

                break;
            case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                showToastTips("404 resource not found !");

                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                showToastTips("Connection refused !");

                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                showToastTips("Connection timeout !");

                break;
            case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                showToastTips("Empty playlist !");

                break;
            case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                showToastTips("Stream disconnected !");

                break;
            case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                showToastTips("Network IO Error !");

                break;

            case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                showToastTips("Unauthorized Error !");
                break;

            case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                showToastTips("请求超时");

                break;

            case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                showToastTips("请求超时");
                break;

            case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:

                showToastTips("MEDIA_ERROR_UNKNOWN");
                break;

            default:
                showToastTips("主播还未开播");
                break;
        }
        return true;
    }

    private void showToastTips(final String tips) {
        Toast.makeText(mContext, tips, Toast.LENGTH_SHORT);
    }

}
