package com.dreamlin.gankvm.retrofit;

import com.google.gson.annotations.SerializedName;

public class ServerBean {

    /**
     * server_name : 106服务器
     * port : 8080
     * ip : 106.256.2.35
     * id : bad21b70e83945519eaa38dde5badfa7
     * create_date : 2019-10-08 17:26:20
     */

    @SerializedName("server_name")
    private String serverName;
    @SerializedName("port")
    private String port;
    @SerializedName("ip")
    private String ip;
    @SerializedName("id")
    private String id;
    @SerializedName("create_date")
    private String createDate;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
