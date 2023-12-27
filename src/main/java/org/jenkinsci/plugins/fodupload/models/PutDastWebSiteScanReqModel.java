package org.jenkinsci.plugins.fodupload.models;

public class PutDastWebSiteScanReqModel extends PutDastScanSetupReqModel {

    public void setDynamicSiteUrl(String dynamicSiteUrl) {
        this.dynamicSiteUrl = dynamicSiteUrl;
    }
    String dynamicSiteUrl;
    boolean enableRedundantPageDetection;
    public boolean isEnableRedundantPageDetection() {
        return enableRedundantPageDetection;
    }
    public void setEnableRedundantPageDetection(boolean enableRedundantPageDetection) {
        this.enableRedundantPageDetection = enableRedundantPageDetection;
    }
    public void setRequiresSiteAuthentication(boolean requiresSiteAuthentication) {
        this.requiresSiteAuthentication = requiresSiteAuthentication;
    }
    boolean requiresSiteAuthentication;
    public void setRestrictToDirectoryAndSubdirectories(boolean restrictToDirectoryAndSubdirectories) {
        this.restrictToDirectoryAndSubdirectories = restrictToDirectoryAndSubdirectories;
    }

    public void setRequestFalsePositiveRemoval(boolean requestFalsePositiveRemoval) {
        this.requestFalsePositiveRemoval = requestFalsePositiveRemoval;
    }

    private  boolean restrictToDirectoryAndSubdirectories;

    private boolean requestFalsePositiveRemoval;

    public void setTimeBoxInHours(Integer timeBoxInHours) {
        this.timeBoxInHours = timeBoxInHours;
    }
    Integer timeBoxInHours;
    ExclusionsList exclusionsList;
    public class ExclusionsList {
        public String  value;
    }
    public void setLoginMacroFileId(int loginMacroFileId) {
        this.loginMacroFileId = loginMacroFileId;
    }
    public void setPolicy(String policy) {
        this.policy = policy;
    }
    String policy;
    int loginMacroFileId;

    public void setRequestLoginMacroFileCreation(boolean requestLoginMacroFileCreation) {
        this.requestLoginMacroFileCreation = requestLoginMacroFileCreation;
    }

    private  boolean requestLoginMacroFileCreation;
    private String loginMacroPrimaryUserName;

    public void setLoginMacroFileCreationDetails(LoginMacroFileCreationDetails loginMacroFileCreationDetails) {
        this.loginMacroFileCreationDetails = loginMacroFileCreationDetails;
    }

    public LoginMacroFileCreationDetails getLoginMacroFileCreationDetails() {
        return loginMacroFileCreationDetails;
    }

    private LoginMacroFileCreationDetails loginMacroFileCreationDetails;


    public class LoginMacroFileCreationDetails{
        public String primaryUsername;
        public String primaryPassword;

        public void setPrimaryUsername(String primaryUsername) {
            this.primaryUsername = primaryUsername;
        }

        public void setPrimaryPassword(String primaryPassword) {
            this.primaryPassword = primaryPassword;
        }

        public void setSecondaryUsername(String secondaryUsername) {
            this.secondaryUsername = secondaryUsername;
        }

        public void setSecondaryPassword(String secondaryPassword) {
            this.secondaryPassword = secondaryPassword;
        }

        public String secondaryUsername;
        public String secondaryPassword;
    }
}