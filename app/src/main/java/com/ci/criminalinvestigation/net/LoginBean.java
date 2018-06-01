package com.ci.criminalinvestigation.net;

import java.io.Serializable;

/**
 * @author: wjl
 * @date:2018/5/26
 */

public class LoginBean implements Serializable{
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
