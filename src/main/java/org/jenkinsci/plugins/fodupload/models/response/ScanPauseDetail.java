package org.jenkinsci.plugins.fodupload.models.response;

public class ScanPauseDetail {
    private String PausedOn = "";
    private String Reason = "";
    private String Notes = "";

    public String getPausedOn() {
        return PausedOn;
    }

    public String getReason() {
        return Reason != null ? Reason : "";
    }

    public String getNotes() {
        return Notes != null ? Notes : "";
    }
}
