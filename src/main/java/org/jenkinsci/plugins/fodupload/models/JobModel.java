package org.jenkinsci.plugins.fodupload.models;

import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobModel {

    private String releaseId;
    private boolean purchaseEntitlements;
    private String entitlementPreference;
    private String srcLocation;
    private String remediationScanPreferenceType;
    private String inProgressScanActionType;
    private String inProgressBuildResultType;
    private String selectedReleaseType;
    private String userSelectedApplication;
    private String userSelectedMicroservice;
    private String userSelectedRelease;

    private File payload;

    /**
     * Build model used to pass values around
     * @param releaseId                     Release ID
     * @param purchaseEntitlements          purchaseEntitlements
     * @param entitlementPreference         entitlementPreference
     * @param srcLocation                   srcLocation
     * @param remediationScanPreferenceType remediationScanPreferenceType
     * @param inProgressScanActionType      inProgressScanActionType
     * @param inProgressBuildResultType     inProgressBuildResultType
     * @param selectedReleaseType           selectedReleaseType
     */
    public JobModel(String releaseId,
                    boolean purchaseEntitlements,
                    String entitlementPreference,
                    String srcLocation,
                    String remediationScanPreferenceType,
                    String inProgressScanActionType,
                    String inProgressBuildResultType,
                    String selectedReleaseType,
                    String userSelectedApplication,
                    String userSelectedMicroservice,
                    String userSelectedRelease) {

        this.releaseId = releaseId;
        this.entitlementPreference = entitlementPreference;
        this.purchaseEntitlements = purchaseEntitlements;
        this.srcLocation = srcLocation;
        this.remediationScanPreferenceType = remediationScanPreferenceType;
        this.inProgressScanActionType = inProgressScanActionType;
        this.inProgressBuildResultType = inProgressBuildResultType;
        this.selectedReleaseType = selectedReleaseType;
        this.userSelectedApplication = userSelectedApplication;
        this.userSelectedMicroservice = userSelectedMicroservice;
        this.userSelectedRelease = userSelectedRelease;
    }

    public File getPayload() {
        return payload;
    }

    public void setPayload(File payload) {
        this.payload = payload;
    }

    public String getReleaseId() { return releaseId; }

    public boolean isPurchaseEntitlements() {
        return purchaseEntitlements;
    }

    public String getEntitlementPreference() {
        return entitlementPreference;
    }

    public String getSrcLocation() {
        return srcLocation;
    }

    public String getRemediationScanPreferenceType() {
        return remediationScanPreferenceType;
    }

    public String getInProgressScanActionType() {
        return inProgressScanActionType;
    }

    public String getInProgressBuildResultType() {
        return inProgressBuildResultType;
    }

    public String getSelectedReleaseType() {
        return selectedReleaseType;
    }

    public String getUserSelectedApplication() {
        return userSelectedApplication;
    }

    public String getUserSelectedMicroservice() {
        return userSelectedMicroservice;
    }

    public String getUserSelectedRelease() {
        return userSelectedRelease;
    }

    @Override
    public String toString() {
       return String.format("Release Id: %s", releaseId);
    }

    // TODO: More validation, though this should never happen with the new format
    public boolean validate(PrintStream logger) {

        List<String> errors = new ArrayList<>();

        Integer releaseIdNum = 0;
        if (releaseId != null && !releaseId.isEmpty()) {
            try {
                releaseIdNum = Integer.parseInt(releaseId);
                if(releaseIdNum != null && releaseIdNum > 0)
                    return true;
            }
            catch (NumberFormatException ex) {
                errors.add("Release Id");
                logger.println(errors.toString());
            }
        }
        return false;
    }
}
