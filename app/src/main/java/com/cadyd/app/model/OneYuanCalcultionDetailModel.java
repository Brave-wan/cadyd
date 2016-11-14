package com.cadyd.app.model;

import java.util.List;

/**
 * Created by xiongmao on 2016/9/26.
 */

public class OneYuanCalcultionDetailModel {
    /**
     * timesum : 12227349659
     * tbid : 8de0b5c6d6d74c1fa4153638e1e83fdd
     * lotteryperiods : 20160512024
     * basevalue : 10000001
     * luckcode : 10000110
     * lotterycode : 87490
     */

    private LotteryDetailBean lotteryDetail;
    /**
     * createtime : 2016-05-1209: 55: 14: 035
     * id : 04a8b0fa2bb449a093f52a5d19539be1
     * nickname : 15342303667
     * tbid : 8de0b5c6d6d74c1fa4153638e1e83fdd
     * timevalue : 95514035
     */

    private List<RecordDataBean> recordData;

    public LotteryDetailBean getLotteryDetail() {
        return lotteryDetail;
    }

    public void setLotteryDetail(LotteryDetailBean lotteryDetail) {
        this.lotteryDetail = lotteryDetail;
    }

    public List<RecordDataBean> getRecordData() {
        return recordData;
    }

    public void setRecordData(List<RecordDataBean> recordData) {
        this.recordData = recordData;
    }

    public static class LotteryDetailBean {
        private String timesum;
        private String tbid;
        private String lotteryperiods;
        private int basevalue;
        private String luckcode;
        private String lotterycode;

        public String getTimesum() {
            return timesum;
        }

        public void setTimesum(String timesum) {
            this.timesum = timesum;
        }

        public String getTbid() {
            return tbid;
        }

        public void setTbid(String tbid) {
            this.tbid = tbid;
        }

        public String getLotteryperiods() {
            return lotteryperiods;
        }

        public void setLotteryperiods(String lotteryperiods) {
            this.lotteryperiods = lotteryperiods;
        }

        public int getBasevalue() {
            return basevalue;
        }

        public void setBasevalue(int basevalue) {
            this.basevalue = basevalue;
        }

        public String getLuckcode() {
            return luckcode;
        }

        public void setLuckcode(String luckcode) {
            this.luckcode = luckcode;
        }

        public String getLotterycode() {
            return lotterycode;
        }

        public void setLotterycode(String lotterycode) {
            this.lotterycode = lotterycode;
        }
    }

    public static class RecordDataBean {
        private String createtime;
        private String id;
        private String nickname;
        private String tbid;
        private String timevalue;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTbid() {
            return tbid;
        }

        public void setTbid(String tbid) {
            this.tbid = tbid;
        }

        public String getTimevalue() {
            return timevalue;
        }

        public void setTimevalue(String timevalue) {
            this.timevalue = timevalue;
        }
    }
}
