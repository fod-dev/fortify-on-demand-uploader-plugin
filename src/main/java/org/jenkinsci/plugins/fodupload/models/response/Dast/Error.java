package org.jenkinsci.plugins.fodupload.models.response.Dast;

import java.io.Serializable;

public class Error implements Serializable {
    @SuppressWarnings("unused")
    public int errorCode;
    @SuppressWarnings("unused")
    public String message;
}
