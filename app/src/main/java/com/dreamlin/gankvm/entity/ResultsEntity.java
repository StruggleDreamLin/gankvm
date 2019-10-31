package com.dreamlin.gankvm.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IdRes;

import com.google.gson.annotations.SerializedName;

public class ResultsEntity implements Parcelable {

    /**
     * _id : 5ccdbc219d212239df927a93
     * createdAt : 2019-05-04T16:21:53.523Z
     * desc : 2019-05-05
     * publishedAt : 2019-05-04T16:21:59.733Z
     * source : web
     * type : 福利
     * url : http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg
     * used : true
     * who : lijinshanmx
     */

    public boolean isHeader = false;
    @SerializedName("_id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("desc")
    private String desc;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("source")
    private String source;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;
    @SerializedName("used")
    private boolean used;
    @SerializedName("who")
    private String who;

    public ResultsEntity() {
    }

    protected ResultsEntity(Parcel in) {
        isHeader = in.readByte() != 0;
        id = in.readString();
        createdAt = in.readString();
        desc = in.readString();
        publishedAt = in.readString();
        source = in.readString();
        type = in.readString();
        url = in.readString();
        used = in.readByte() != 0;
        who = in.readString();
    }

    public static final Creator<ResultsEntity> CREATOR = new Creator<ResultsEntity>() {
        @Override
        public ResultsEntity createFromParcel(Parcel in) {
            return new ResultsEntity(in);
        }

        @Override
        public ResultsEntity[] newArray(int size) {
            return new ResultsEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isGirls() {
        return type.equals("福利");
    }

    public boolean isAndroid() {
        return type.equals("Android");
    }

    public boolean isIOS() {
        return type.equals("iOS");
    }

    public boolean isWeb(){
        return type.equals("前端");
    }

    public boolean isApp(){
        return type.equals("App");
    }

    public boolean isTv(){
        return type.equals("休息视频");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isHeader ? 1 : 0));
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(desc);
        dest.writeString(publishedAt);
        dest.writeString(source);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeByte((byte) (used ? 1 : 0));
        dest.writeString(who);
    }
}
