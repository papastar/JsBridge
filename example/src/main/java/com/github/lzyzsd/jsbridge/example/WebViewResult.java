package com.github.lzyzsd.jsbridge.example;

import com.google.gson.Gson;

/**
 * 类描述： H5调用Native的返回信息
 * 创建人： 唐僧 Eran
 * 创建时间：2016/8/15 0015
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class WebViewResult<T> {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAIL = -1;

    private int code ; //0-成功或正常返回，非0代表具体错误码
    private String msg; //code非0时的错误描述
    private T data; //json数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResult(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
