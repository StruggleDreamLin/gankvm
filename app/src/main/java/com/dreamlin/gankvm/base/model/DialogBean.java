package com.dreamlin.gankvm.base.model;

public class DialogBean {

    private static DialogBean instance;
    private boolean isShow;
    private String msg;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static DialogBean getInstance() {
        synchronized (DialogBean.class) {
            if (instance == null) {
                instance = new DialogBean();
                instance.setShow(true);
                instance.setMsg("数据加载中...");
            }
        }
        return instance;
    }

}
