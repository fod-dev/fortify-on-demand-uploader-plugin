package org.jenkinsci.plugins.fodupload.models.response;

public class PollingSummaryDTO {
    private int scanId;
    private int tenantId;
    private int analysisStatusId;
    private String analysisStatusTypeValue;
    private int analysisStatusReasonId;
    private String analysisStatusReasonNotes;
    private int issueCountCritical;
    private int issueCountHigh;
    private int issueCountMedium;
    private int issueCountLow;
    private Boolean passFailStatus;
    private String passFailReasonType;
    private ScanPauseDetail[] pauseDetails;

    
    public int getScanId() {
        return scanId;
    };

    public int getTenantId() {
        return tenantId;
    };

    public int getAnalysisStatusId() {
        return analysisStatusId;
    };

    public String getAnalysisStatusTypeValue() {
        return analysisStatusTypeValue;
    };

    public int getAnalysisStatusReasonId() {
        return analysisStatusReasonId;
    };

    public String getAnalysisStatusReasonNotes() {
        return analysisStatusReasonNotes != null ? analysisStatusReasonNotes : "";
    };

    public int getIssueCountCritical() {
        return issueCountCritical;
    };

    public int getIssueCountHigh() {
        return issueCountHigh;
    };

    public int getIssueCountMedium() {
        return issueCountMedium;
    };

    public int getIssueCountLow() {
        return issueCountLow;
    };

    public Boolean getPassFailStatus() {
        return passFailStatus;
    };

    public String getPassFailReasonType() {
        return passFailReasonType;
    };

    public ScanPauseDetail[] getPauseDetails() {
        ScanPauseDetail[] returnDetails = pauseDetails;
        return returnDetails;
    };
}