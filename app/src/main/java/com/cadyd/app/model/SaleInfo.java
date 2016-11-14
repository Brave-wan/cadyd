package com.cadyd.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息信息
 * Created by wcy on 2016/5/12.
 */
public class SaleInfo {
    public String name;// "color", //属性英文名称
    public String aid;// "61cba6599886497790bca4e035d58184", //属性id
    public String isdict;// 1, //是否关联数据字典：0：是，1：否
    public String vid;// "1e4f9285b42540518a14ec124349f17b", //属性值 的id
    public String attrname;// "颜色", //属性中文名称
    public String attrvname;// "白色"//属性值的名称
  //  public int seq;//序号
    public List<SaleInfo> list = new ArrayList<>();

    public boolean IsSave(String vid) {
        for (SaleInfo si : list) {
            if (si.vid.equals(vid)) {
                return true;
            }
        }
        return false;
    }
}
