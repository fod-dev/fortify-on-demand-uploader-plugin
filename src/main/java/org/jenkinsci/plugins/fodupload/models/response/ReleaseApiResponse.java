package org.jenkinsci.plugins.fodupload.models.response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ReleaseApiResponse {
    private int ReleaseId;
    private String ReleaseName;
    private String ReleaseDescription;
    private Boolean Suspend;
    private String ReleaseCreatedDate;
    private String MicroserviceName;
    private int MicroserviceId;
    private int ApplicationId;
    private String ApplicationName;
    private int CurrentAnalysisStatusTypeId;
    private String CurrentAnalysisStatusType;
    private int Rating;
    private int Critical;
    private int High;
    private int Medium;
    private int Low;
    private int CurrentStaticScanId;
    private int CurrentDynamicScanId;
    private int CurrentMobileScanId;
    private String StaticAnalysisStatusType;
    private String DynamicAnalysisStatusType;
    private String MobileAnalysisStatusType;
    private int StaticAnalysisStatusTypeId;
    private int DynamicAnalysisStatusTypeId;
    private int MobileAnalysisStatusTypeId;
    private String StaticScanDate;
    private String DynamicScanDate;
    private String MobileScanDate;
    private int IssueCount;
    private Boolean IsPassed;
    private int PassFailReasonTypeId;
    private String PassFailReasonType;
    private int SldcStatusTypeId;
    private String SldcStatusType;
    private int OwnerId;

    public int getReleaseId() {
        return ReleaseId;
    };
    public String getReleaseName() {
        return ReleaseName;
    };
    public String getReleaseDescription() {
        return ReleaseDescription;
    };
    public Boolean getSuspend() {
        return Suspend;
    };
    public String getReleaseCreatedDate() {
        return ReleaseCreatedDate;
    };
    public String getMicroserviceName() {
        return MicroserviceName;
    };
    public int getMicroserviceId() {
        return MicroserviceId;
    };
    public int getApplicationId() {
        return ApplicationId;
    };
    public String getApplicationName() {
        return ApplicationName;
    };
    public int getCurrentAnalysisStatusTypeId() {
        return CurrentAnalysisStatusTypeId;
    };
    public String getCurrentAnalysisStatusType() {
        return CurrentAnalysisStatusType;
    };
    public int getRating() {
        return Rating;
    };
    public int getCritical() {
        return Critical;
    };
    public int getHigh() {
        return High;
    };
    public int getMedium() {
        return Medium;
    };
    public int getLow() {
        return Low;
    };
    public int getCurrentStaticScanId() {
        return CurrentStaticScanId;
    };
    public int getCurrentDynamicScanId() {
        return CurrentDynamicScanId;
    };
    public int getCurrentMobileScanId() {
        return CurrentMobileScanId;
    };
    public String getStaticAnalysisStatusType() {
        return StaticAnalysisStatusType;
    };
    public String getDynamicAnalysisStatusType() {
        return DynamicAnalysisStatusType;
    };
    public String getMobileAnalysisStatusType() {
        return MobileAnalysisStatusType;
    };
    public int getStaticAnalysisStatusTypeId() {
        return StaticAnalysisStatusTypeId;
    };
    public int getDynamicAnalysisStatusTypeId() {
        return DynamicAnalysisStatusTypeId;
    };
    public int getMobileAnalysisStatusTypeId() {
        return MobileAnalysisStatusTypeId;
    };
    public String getStaticScanDate() {
        return StaticScanDate;
    };
    public String getDynamicScanDate() {
        return DynamicScanDate;
    };
    public String getMobileScanDate() {
        return MobileScanDate;
    };
    public int getIssueCount() {
        return IssueCount;
    };
    public Boolean getIsPassed() {
        return IsPassed;
    };
    public int getPassFailReasonTypeId() {
        return PassFailReasonTypeId;
    };
    public String getPassFailReasonType() {
        return PassFailReasonType;
    };
    public int getSldcStatusTypeId() {
        return SldcStatusTypeId;
    };
    public String getSldcStatusType() {
        return SldcStatusType;
    };
    public int getOwnerId() {
        return OwnerId;
    };
}
