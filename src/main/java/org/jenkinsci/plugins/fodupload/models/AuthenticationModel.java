package org.jenkinsci.plugins.fodupload.models;

import hudson.util.Secret;

public class AuthenticationModel {
    private boolean overrideGlobalConfig;
    private Secret username;
    private Secret personalAccessToken;
    private Secret tenantId;

    public AuthenticationModel(boolean overrideGlobalConfig,
                               Secret username,
                               Secret personalAccessToken,
                               Secret tenantId) {
        this.overrideGlobalConfig = overrideGlobalConfig;
        this.username = username;
        this.personalAccessToken = personalAccessToken;
        this.tenantId = tenantId;
    }

    public boolean getOverrideGlobalConfig() {
        return overrideGlobalConfig;
    }

    public Secret getUsername() {
        return username;
    }

    public Secret getPersonalAccessToken() {
        return personalAccessToken;
    }

    public Secret getTenantId() {
        return tenantId;
    }
}
