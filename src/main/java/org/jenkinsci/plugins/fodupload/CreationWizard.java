package org.jenkinsci.plugins.fodupload;

import hudson.model.Describable;
import hudson.model.Descriptor;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import hudson.Extension;
import hudson.model.RootAction;
import java.io.IOException;
import javax.servlet.ServletException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

public class CreationWizard extends Descriptor<CreationWizard> implements
        Describable<CreationWizard> {

    public CreationWizard() {
        super(CreationWizard.class);
    }

    @RequirePOST
    public void doReceiveRequest(StaplerRequest req, StaplerResponse rsp)
        throws IOException, ServletException {
            System.out.println(req);
            rsp.forwardToPreviousPage(req);
        }

    @JavaScriptMethod
    public String submitForm(String value1, String value2, String value3, String value4) {
        return value1 + value2 + value3 +value4;
    }

    @Override
    public Descriptor<CreationWizard> getDescriptor() {
        return null;
    }
}