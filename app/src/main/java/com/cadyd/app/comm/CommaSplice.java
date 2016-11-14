package com.cadyd.app.comm;

import java.util.List;

/**
 * Created by xiongmao on 2016/6/20.
 */
public class CommaSplice {

    public static StringBuffer Splice(List<String> lists) {

        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < lists.size(); j++) {
            if (j == lists.size() - 1) {
                buffer.append(lists.get(j));
            } else {
                buffer.append(lists.get(j));
                buffer.append(",");
            }
        }
        return buffer;
    }
}
