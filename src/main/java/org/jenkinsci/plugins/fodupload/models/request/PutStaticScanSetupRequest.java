package org.jenkinsci.plugins.fodupload.models.request;

public class PutStaticScanSetupRequest {
    private int assessmentTypeId;
    private String entitlementFrequencyType;
    private int technologyStackId;
    private int languageLevelId;
    private boolean performOpenSourceAnalysis;
    private String auditPreferenceType;
    private boolean includeThirdPartyLibraries;
    private boolean useSourceControl;
    private boolean scanBinary;

    public int getAssessmentTypeId() {
        return assessmentTypeId;
    }

    public String getEntitlementFrequencyType() {
        return entitlementFrequencyType;
    }

    public int getTechnologyStackId() {
        return technologyStackId;
    }

    public int getLanguageLevelId() {
        return languageLevelId;
    }

    public boolean isPerformOpenSourceAnalysis() {
        return performOpenSourceAnalysis;
    }

    public String getAuditPreferenceType() {
        return auditPreferenceType;
    }

    public boolean isIncludeThirdPartyLibraries() {
        return includeThirdPartyLibraries;
    }

    public boolean isUseSourceControl() {
        return useSourceControl;
    }

    public boolean isScanBinary() {
        return scanBinary;
    }
}
