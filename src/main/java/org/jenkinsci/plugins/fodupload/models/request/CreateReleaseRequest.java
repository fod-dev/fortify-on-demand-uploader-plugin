package org.jenkinsci.plugins.fodupload.models.request;

public class CreateReleaseRequest {

    private int applicationId;
    private String releaseName;
    private String releaseDescription;
    private boolean copyState;
    private int copyStateReleaseId;
    private String sdlcStatusType;
    private int microserviceId;

    /**
     * Release request parameters
     * @param applicationId             Application Id
     * @param releaseName               Release Name
     * @param releaseDescription        Release Description
     * @param copyState                 Copy State
     * @param copyStateReleaseId        Copy State Release Id
     * @param sdlcStatusType            Sdlc Status Type
     * @param microserviceId            Microservice Id
     */
    public CreateReleaseRequest(int applicationId,
                                String releaseName,
                                String releaseDescription,
                                boolean copyState,
                                int copyStateReleaseId,
                                String sdlcStatusType,
                                int microserviceId) {

        this.applicationId = applicationId;
        this.releaseName = releaseName;
        this.releaseDescription = releaseDescription;
        this.copyState = copyState;
        this.copyStateReleaseId = copyStateReleaseId;
        this.sdlcStatusType = sdlcStatusType;
        this.microserviceId = microserviceId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public String getReleaseDescription() {
        return releaseDescription;
    }

    public boolean getCopyState() {
        return copyState;
    }

    public int getCopyStateReleaseId() {
        return  copyStateReleaseId;
    }

    public String getSdlcStatusType() {
        return sdlcStatusType;
    }

    public int getMicroserviceId() {
        return microserviceId;
    }
}
