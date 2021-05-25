package org.jenkinsci.plugins.fodupload;

import org.kohsuke.stapler.bind.JavaScriptMethod;

public class JavascriptUtils {
    private int x;
    private int y;
    @JavaScriptMethod
    public int add(int x, int y) {
        return x+y;
    }
    public int getX() {
        return this.x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }
}
