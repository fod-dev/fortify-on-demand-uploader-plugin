package org.jenkinsci.plugins.fodupload.models.response;

public class CreateApplicationApiResponse {
    private int applicationId;
    private boolean success;
    private String[] errors;

    public int getApplicationId() {
        return applicationId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String[] getErrors() {
        return errors;
    }
}
