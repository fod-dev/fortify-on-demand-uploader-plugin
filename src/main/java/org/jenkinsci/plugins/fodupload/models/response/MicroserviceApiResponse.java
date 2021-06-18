package org.jenkinsci.plugins.fodupload.models.response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

public class MicroserviceApiResponse {
    private int MicroserviceId;
    private String MicroserviceName;
    private int ReleaseId;
    
    public int getMicroserviceId() {
        return MicroserviceId;
    }

    public String getMicroserviceName() {
        return MicroserviceName;
    }

    public int getReleaseId() {
        return ReleaseId;
    }
}