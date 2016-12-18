package com.maning.retrofitokhttpcache.bean;

import java.util.List;

/**
 * Created by maning on 16/6/16.
 */
public class GankModel {


    /**
     * error : false
     * results : [{"_id":"57620712421aa90de17180ab","createdAt":"2016-06-16T09:55:30.348Z","desc":"那些酷炫的RecyclerView开源库整理","publishedAt":"2016-06-16T12:19:00.930Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/154891851fe2","used":true,"who":"Anthony"},{"_id":"5761fd83421aa90de17180a9","createdAt":"2016-06-16T09:14:43.658Z","desc":"剪辑视频","publishedAt":"2016-06-16T12:19:00.930Z","source":"chrome","type":"Android","url":"https://github.com/knowledge4life/k4l-video-trimmer","used":true,"who":"大熊"},{"_id":"575f836d421aa940eb4e0f75","createdAt":"2016-06-14T12:09:17.974Z","desc":"指示器。","publishedAt":"2016-06-15T11:55:46.992Z","source":"chrome","type":"Android","url":"https://github.com/badoualy/stepper-indicator","used":true,"who":"大熊"},{"_id":"575ec5db421aa9297197ca9d","createdAt":"2016-06-13T22:40:27.551Z","desc":"又一个关于 Agera 的裤子👖 用法和 RxBus 基本一样的 Agera Event Bus","publishedAt":"2016-06-14T11:52:47.320Z","source":"web","type":"Android","url":"https://github.com/drakeet/agera-event-bus","used":true,"who":"drakeet"},{"_id":"575e746a421aa93009aa6488","createdAt":"2016-06-13T16:52:58.901Z","desc":"app下载安装","publishedAt":"2016-06-14T11:52:47.320Z","source":"chrome","type":"Android","url":"https://github.com/yaming116/UpdateApp","used":true,"who":"花开堪折枝"},{"_id":"575e5f70421aa9296bddf5b1","createdAt":"2016-06-13T15:23:28.926Z","desc":"Animated SVG Drawing for Android","publishedAt":"2016-06-14T11:52:47.320Z","source":"web","type":"Android","url":"https://github.com/jaredrummler/AnimatedSvgView","used":true,"who":"潇涧"},{"_id":"575e1faf421aa93009aa647f","createdAt":"2016-06-13T10:51:27.941Z","desc":"水平的滑轮","publishedAt":"2016-06-13T11:38:17.247Z","source":"chrome","type":"Android","url":"https://github.com/shchurov/HorizontalWheelView","used":true,"who":"有时放纵"},{"_id":"575e10d1421aa9297197ca89","createdAt":"2016-06-13T09:48:01.845Z","desc":"PinLockView","publishedAt":"2016-06-13T11:38:17.247Z","source":"chrome","type":"Android","url":"https://github.com/aritraroy/PinLockView","used":true,"who":"蒋朋"},{"_id":"575df33e421aa9296bddf5a3","createdAt":"2016-06-13T07:41:50.373Z","desc":"一个支持多种状态的自定义View,可以方便的切换到：加载中视图、错误视图、空数据视图、网络异常视图、内容视图","publishedAt":"2016-06-13T11:38:17.247Z","source":"chrome","type":"Android","url":"https://github.com/qyxxjd/MultipleStatusView","used":true,"who":"大熊"},{"_id":"575df2f2421aa9296bddf5a2","createdAt":"2016-06-13T07:40:34.686Z","desc":"DMGameApp是一款基于3DMGAME的一个游戏门户app","publishedAt":"2016-06-13T11:38:17.247Z","source":"chrome","type":"Android","url":"https://github.com/xiaohaibin/DMGameApp","used":true,"who":"大熊"}]
     */

    private boolean error;
    /**
     * _id : 57620712421aa90de17180ab
     * createdAt : 2016-06-16T09:55:30.348Z
     * desc : 那些酷炫的RecyclerView开源库整理
     * publishedAt : 2016-06-16T12:19:00.930Z
     * source : web
     * type : Android
     * url : http://www.jianshu.com/p/154891851fe2
     * used : true
     * who : Anthony
     */

    private List<ResultsEntity> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public static class ResultsEntity {
        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        @Override
        public String toString() {
            return "ResultsEntity{" +
                    "_id='" + _id + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", source='" + source + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", used=" + used +
                    ", who='" + who + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GankModel{" +
                "error=" + error +
                ", results=" + results.toString() +
                '}';
    }
}
