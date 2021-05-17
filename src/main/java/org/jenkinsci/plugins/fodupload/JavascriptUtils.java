package org.jenkinsci.plugins.fodupload;

import org.kohsuke.stapler.bind.JavaScriptMethod;

public class JavascriptUtils {
    private int x;
    private String y;
    @JavaScriptMethod
    public int add(int x, int y) {
        return x+y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(String y) {
        this.y = y;
    }
}
