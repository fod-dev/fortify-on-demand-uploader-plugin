package org.jenkinsci.plugins.fodupload;

import hudson.Plugin;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Api;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;

@ExportedBean
public class AuthenticationOverride extends AbstractDescribableImpl<AuthenticationOverride>
    implements Serializable {
    AuthenticationOverride() {
    }
}
