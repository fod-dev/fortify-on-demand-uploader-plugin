package org.jenkinsci.plugins.fodupload.models.response;

public class CreateReleaseApiResponse {
    private int releaseId;
    private boolean success;
    private String[] errors;

    public int getReleaseId() {
        return releaseId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String[] getErrors() {
        return errors;
    }
}
