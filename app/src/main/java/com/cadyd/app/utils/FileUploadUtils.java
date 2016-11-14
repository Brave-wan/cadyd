package com.cadyd.app.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by SCH-1 on 2016/8/11.
 */
public class FileUploadUtils {

    private ExecutorService executors;

    private static FileUploadUtils instance;

    private Activity activity;


    public static FileUploadUtils getInstance() {
        if (instance == null) {
            instance = new FileUploadUtils();
        }
        return instance;
    }

    private FileUploadUtils() {
        executors = Executors.newCachedThreadPool();
    }

    public void post(Activity activity, String url, int intUrl, File file, UploadFileListener httpCallBack) {
        this.activity = activity;
        executors.execute(new Work(url, intUrl, file, httpCallBack));
    }

    private String sendFile(String url, int intUrl, File file) throws Exception {
        if (TextUtils.isEmpty(url) || file == null || !file.exists()) {
            return null;
        }

        String result = null;
        HttpURLConnection connection = null;
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL server = new URL(url);
            connection = (HttpURLConnection) server.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            String token;
            MyApplication application = (MyApplication) activity.getApplicationContext();
            if (application.isLogin()) {
                token = application.model.token;
                Log.e("token", token);
            } else {
                token = "0";
            }
            connection.setRequestProperty("security", token);
            connection.setRequestProperty("ver", "1.1");
            System.out.println("参数：" + intUrl);
            connection.setRequestProperty("operation", String.valueOf(intUrl));

            OutputStream out = new DataOutputStream(connection.getOutputStream());

            StringBuffer strBuf = new StringBuffer();
            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            strBuf.append("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getName() + "\"\r\n");
            strBuf.append("Content-Type:text/plain\r\n\r\n");

            out.write(strBuf.toString().getBytes());

            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            result = strBuf.toString();
            reader.close();

        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;

    }

    public interface UploadFileListener {
        public void onSuccess(String data);

        public void onFailure(String msg);
    }

    private class Work implements Runnable {

        private String url;
        private File file;
        private UploadFileListener httpCallBack;
        private int intUrl;

        public Work(String url, int intUrl, File file, UploadFileListener httpCallBack) {
            this.url = url;
            this.file = file;
            this.httpCallBack = httpCallBack;
            this.intUrl = intUrl;
        }

        @Override
        public void run() {
            String result = null;
            try {
                result = sendFile(url, intUrl, file);
                if (httpCallBack != null) {
                    httpCallBack.onSuccess(result);
                }
            } catch (Exception e) {
                if (httpCallBack != null) {
                    httpCallBack.onFailure(e.getMessage());
                }
            }
        }
    }

}
