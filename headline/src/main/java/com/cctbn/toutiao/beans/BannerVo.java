package com.cctbn.toutiao.beans;

import java.util.List;

/**
 * Created by zgjt on 2016/8/31.
 */
public class BannerVo extends BaseObject {
    private String code;

    private List<ListEntity> list;

    public void setCode(String code) {
        this.code = code;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public List<ListEntity> getList() {
        return list;
    }

    //    "pkid": 253729,
//            "title": "保定：污损号牌钻执法空子 大货车驾驶人聪明反被聪明误",
//            "source": "河北交通电视频道",
//            "flag": "0502",
//            "litpic": [
//            "http://www.cctbn.com/uploads/litimg/14HF2146-3301.jpg"
//            ],
//            "timelen": "00:01:33",
//            "video": "http://www.cctbn.com/uploads/media/14HF2146-Q41.mp4"
    public static class ListEntity {
        private int pkid;
        private String title;
        private String source;
        private String author;
        private String htmlurl;
        private String flag;
        private String time;
        private String description;
        private List<String> litpic;
        private String video;
        private String timelen;

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getTimelen() {
            return timelen;
        }

        public void setTimelen(String timelen) {
            this.timelen = timelen;
        }

        public void setPkid(int pkid) {
            this.pkid = pkid;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setHtmlurl(String htmlurl) {
            this.htmlurl = htmlurl;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setLitpic(List<String> litpic) {
            this.litpic = litpic;
        }

        public int getPkid() {
            return pkid;
        }

        public String getTitle() {
            return title;
        }

        public String getSource() {
            return source;
        }

        public String getAuthor() {
            return author;
        }

        public String getHtmlurl() {
            return htmlurl;
        }

        public String getFlag() {
            return flag;
        }

        public String getTime() {
            return time;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getLitpic() {
            return litpic;
        }
    }
}
