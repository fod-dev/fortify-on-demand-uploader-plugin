package org.jenkinsci.plugins.fodupload.models.response;

public class AddMicroserviceApiResponse {
    private int microserviceId;
    private boolean success;
    private String[] errors;

    public int getMicroserviceId() {
        return microserviceId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String[] getErrors() {
        return errors;
    }
}
