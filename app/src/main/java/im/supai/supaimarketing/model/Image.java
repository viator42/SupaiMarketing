package im.supai.supaimarketing.model;

import java.io.Serializable;

/**
 * Created by viator42 on 15/4/27.
 */
public class Image
{
    private String url;
    private String msg;
    private boolean uploadSuccess;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isUploadSuccess() {
        return uploadSuccess;
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }
}
