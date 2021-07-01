package org.jenkinsci.plugins.fodupload.models.response;

public class PutStaticScanScanSetupResponse {
    private boolean success;
    private String bsiToken;
    private String[] errors;
    private String[] messages;

    public boolean isSuccess() {
        return success;
    }

    public String getBsiToken() {
        return bsiToken;
    }

    public String[] getErrors() {
        return errors;
    }

    public String[] getMessages() {
        return messages;
    }
}
