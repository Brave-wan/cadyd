package com.cadyd.app.comm;

import java.util.List;

/**生成固定长度的并添加数据
 * Created by xiongmao on 2016/10/29.
 */

public class FixedList<T> {

    public void fixedList(int number, List<T> list, Object object) {
        if (list.size() < number) {
            list.add((T) object);
        } else {
            list.remove(0);
            list.add((T) object);
        }
    }
}
