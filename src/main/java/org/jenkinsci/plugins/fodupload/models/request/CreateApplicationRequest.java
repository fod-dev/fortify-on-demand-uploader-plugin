package org.jenkinsci.plugins.fodupload.models.request;

public class CreateApplicationRequest {
    private String applicationName;
    private String applicationDescription;
    private String applicationType;
    private String releaseName;
    private String releaseDescription;
    private String emailList;
    private int ownerId;
    private ApplicationAttribute[] attributes;
    private String businessCriticalityType;
    private String sdlcStatusType;
    private boolean hasMicroservices;
    private String[] microservices;
    private String releaseMicroserviceName;
    private int[] userGroupIds;

    /**
     *
     * @param applicationName               Application Name
     * @param applicationDescription        Application Description
     * @param applicationType               Application Type
     * @param releaseName                   Release Name
     * @param releaseDescription            Release Description
     * @param emailList                     Email List
     * @param ownerId                       Owner Id
     * @param attributes                    Attributes
     * @param businessCriticalityType       Business Criticality Type
     * @param sdlcStatusType                Sdlc Status Type
     * @param hasMicroservices              Has Microservices
     * @param microservices                 Microservices
     * @param releaseMicroserviceName       Release Microservice Name
     * @param userGroupIds                  User Group Ids
     */
    public CreateApplicationRequest(String applicationName,
                                    String applicationDescription,
                                    String applicationType,
                                    String releaseName,
                                    String releaseDescription,
                                    String emailList,
                                    int ownerId,
                                    ApplicationAttribute[] attributes,
                                    String businessCriticalityType,
                                    String sdlcStatusType,
                                    boolean hasMicroservices,
                                    String[] microservices,
                                    String releaseMicroserviceName,
                                    int[] userGroupIds) {

        this.applicationName = applicationName;
        this.applicationDescription = applicationDescription;
        this.applicationType = applicationType;
        this.releaseName = releaseName;
        this.releaseDescription = releaseDescription;
        this.emailList = emailList;
        this.ownerId = ownerId;
        this.attributes = attributes;
        this.businessCriticalityType = businessCriticalityType;
        this.sdlcStatusType = sdlcStatusType;
        this.hasMicroservices = hasMicroservices;
        this.microservices = microservices;
        this.releaseMicroserviceName = releaseMicroserviceName;
        this.userGroupIds = userGroupIds;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public String getReleaseDescription() {
        return releaseDescription;
    }

    public String getEmailList() {
        return emailList;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public ApplicationAttribute[] getAttributes() {
        return attributes;
    }

    public String getBusinessCriticalityType() {
        return businessCriticalityType;
    }

    public String getSdlcStatusType() {
        return sdlcStatusType;
    }

    public boolean isHasMicroservices() {
        return hasMicroservices;
    }

    public String[] getMicroservices() {
        return microservices;
    }

    public String getReleaseMicroserviceName() {
        return releaseMicroserviceName;
    }

    public int[] getUserGroupIds() {
        return  userGroupIds;
    }
}

