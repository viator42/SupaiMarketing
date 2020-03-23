package im.supai.supaimarketing.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Viator42 on 2015/4/19.
 * 主界面图标
 */
public class Icon
{
    private String imgUrl;
    private String text;
    private Object dest;
    private long id = 0;
    private Object obj;
    private double longitude = 0;
    private double latitude = 0;
    private boolean editable= false;
    private String badge = "";

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getDest() {
        return dest;
    }

    public void setDest(Object dest) {
        this.dest = dest;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
