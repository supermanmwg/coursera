package com.utils.rjos;


/**
 * Created by supermanmwg on 15-10-29.
 */
public class BaseRjo {
    private transient String message = null;

    public boolean isSuccess() {
        return message == null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (null == message) {
            this.message = "null";
        } else {
            this.message = message;
        }
    }
}
