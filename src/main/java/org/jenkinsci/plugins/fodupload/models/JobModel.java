package org.jenkinsci.plugins.fodupload.models;

import com.fortify.fod.parser.BsiToken;
import com.fortify.fod.parser.BsiTokenParser;

import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobModel {

    private static final BsiTokenParser tokenParser = new BsiTokenParser();

    private String bsiTokenOriginal;
    private transient BsiToken bsiTokenCache;
    private boolean purchaseEntitlements;
    private int entitlementPreference;
    private boolean isRemediationPreferred;
    private String srcLocation;
    private int inProgressScanActionOption;
    private String entitlementPreferenceType;
    private boolean purchaseEntitlement;
    private String remediationScanPreferenceType;
    private String inProgressScanActionType;

    private File payload;

    public File getPayload() {
        return payload;
    }

    public void setPayload(File payload) {
        this.payload = payload;
    }

    public BsiToken getBsiToken() {
        return bsiTokenCache;
    }

    public boolean isPurchaseEntitlements() {
        return purchaseEntitlements;
    }

    public int getEntitlementPreference() {
        return entitlementPreference;
    }

    public String getBsiTokenOriginal() {
        return bsiTokenOriginal;
    }

    public boolean isRemediationPreferred() {
        return isRemediationPreferred;
    }
    
    public String getSrcLocation() {
        return srcLocation;
    }

    public int getInProgressScanActionOption() {
        return inProgressScanActionOption;
    }

    public String getEntitlementPreferenceType() {
        return entitlementPreferenceType;
    }

    public boolean getPurchaseEntitlement() {
        return purchaseEntitlement;
    }

    public String getRemediationScanPreferenceType() {
        return remediationScanPreferenceType;
    }

    public String getInProgressScanActionType() {
        return inProgressScanActionType;
    }

    public int getInProgressScanActionOption() {
        return inProgressScanActionOption;
    }

    /**
     * Build model used to pass values around
     *
     * @param bsiToken              BSI Token
     * @param purchaseEntitlements  purchaseEntitlements
     * @param entitlementPreference entitlementPreference
     * @param isRemediationPreferred isRemediationPreferred
     * @param srcLocation           srcLocation
     * @param inProgressScanActionOption inProgressScanActionOption
     * @param entitlementPreferenceType entitlementPreferenceType
     * @param purchaseEntitlement purchaseEntitlement
     * @param remediationScanPreferenceType remediationScanPreferenceType
     * @param inProgressScanActionType inProgressScanActionType
     */
    public JobModel(String bsiToken,
                    boolean purchaseEntitlements,
                    int entitlementPreference,
                    boolean isRemediationPreferred,
                    String srcLocation,
                    int inProgressScanActionOption,
                    String entitlementPreferenceType,
                    boolean purchaseEntitlement,
                    String remediationScanPreferenceType,
                    String inProgressScanActionType) {

        this.bsiTokenOriginal = bsiToken;
        this.entitlementPreference = entitlementPreference;
        this.purchaseEntitlements = purchaseEntitlements;
        this.isRemediationPreferred = isRemediationPreferred;
        this.srcLocation = srcLocation;
        this.inProgressScanActionOption = inProgressScanActionOption;
        this.entitlementPreferenceType = entitlementPreferenceType;
        this.purchaseEntitlement = purchaseEntitlement;
        this.remediationScanPreferenceType = remediationScanPreferenceType;
        this.inProgressScanActionType = inProgressScanActionType;
    }

    private Object readResolve() throws URISyntaxException, UnsupportedEncodingException {
        bsiTokenCache = tokenParser.parse(bsiTokenOriginal);
        return this;
    }

    @Override
    public String toString() {
        return String.format(
                        "Release Id:                        %s%n" +
                        "Assessment Type Id:                %s%n" +
                        "Technology Stack:                  %s%n" +
                        "Language Level:                    %s%n" +
                        "Purchase Entitlements:             %s%n" +
                        "Entitlement Preference:            %s%n" +
                        "In Progress Scan Option:           %s%n" +
                        "Entytlement Preference Type:       %s%n" +
                        "Purchase EntitlementType:          %s%n" +
                        "Remediation Scan Preference:       %s%n" +
                        "In Progress Scan Action:           %s%n",
                bsiTokenCache.getProjectVersionId(),
                bsiTokenCache.getAssessmentTypeId(),
                bsiTokenCache.getTechnologyStack(),
                bsiTokenCache.getLanguageLevel(),
                purchaseEntitlements,
                entitlementPreference,
                inProgressScanActionOption,
                entitlementPreferenceType,
                purchaseEntitlement,
                remediationScanPreferenceType,
                inProgressScanActionType);
    }

    public boolean initializeBuildModel()
    {
        try {
            this.bsiTokenCache = tokenParser.parse(bsiTokenOriginal);
        } catch (Exception ex) {
            return false;
        } 
        return (this.bsiTokenCache != null);
    }
    
    // TODO: More validation, though this should never happen with the new format
    public boolean validate(PrintStream logger) {
        
        List<String> errors = new ArrayList<>();

        if (bsiTokenCache.getAssessmentTypeId() == 0)
            errors.add("Assessment Type");

        if (bsiTokenCache.getTechnologyType() == null)
            errors.add("Technology Stack");

        if (bsiTokenCache.getProjectVersionId() == 0)
            errors.add("Release Id");

        if (errors.size() > 0) {
            logger.println("Missing the following fields from BSI Token: ");
            for (String error : errors) {
                logger.println("    " + error);
            }
            return false;
        }
        return true;
    }
}
