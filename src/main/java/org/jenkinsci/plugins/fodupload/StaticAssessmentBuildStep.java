package org.jenkinsci.plugins.fodupload;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.security.Permission;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import jenkins.tasks.SimpleBuildStep;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jenkinsci.plugins.fodupload.models.FodEnums;
import org.jenkinsci.plugins.fodupload.models.JobModel;
import org.kohsuke.stapler.DataBoundConstructor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import javax.annotation.Nonnull;

import java.io.*;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.HashMap;

import jenkins.model.Jenkins;

import org.jenkinsci.plugins.fodupload.actions.CrossBuildAction;
import org.jenkinsci.plugins.fodupload.models.AuthenticationModel;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.QueryParameter;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.verb.POST;
import com.google.gson.Gson;


@SuppressWarnings("unused")
public class StaticAssessmentBuildStep extends Recorder implements SimpleBuildStep {

    SharedUploadBuildStep sharedBuildStep;
    String selectedReleaseType;
    String showReleaseIdOptions;


    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    // Entry point when building
    @DataBoundConstructor
    public StaticAssessmentBuildStep(String releaseId,
                                     String bsiToken,
                                     boolean overrideGlobalConfig,
                                     String username,
                                     String personalAccessToken,
                                     String tenantId,
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

        sharedBuildStep = new SharedUploadBuildStep(releaseId,
                bsiToken,
                overrideGlobalConfig,
                username,
                personalAccessToken,
                tenantId,
                purchaseEntitlements,
                entitlementPreference,
                srcLocation,
                remediationScanPreferenceType,
                inProgressScanActionType,
                inProgressBuildResultType,
                selectedReleaseType,
                userSelectedApplication,
                userSelectedMicroservice,
                userSelectedRelease);

    }


    @Override
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        return sharedBuildStep.prebuild(build, listener);
    }

    // logic run during a build
    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void perform(@Nonnull Run<?, ?> build, @Nonnull FilePath workspace,
                        @Nonnull Launcher launcher, @Nonnull TaskListener listener) {

                            PrintStream log = listener.getLogger();
        build.addAction(new CrossBuildAction());
        try{build.save();} catch(IOException ex){log.println("Error saving settings. Error message: " + ex.toString());}

        String correlationId = UUID.randomUUID().toString();

        sharedBuildStep.perform(build, workspace, launcher, listener, correlationId);

        CrossBuildAction crossBuildAction = build.getAction(CrossBuildAction.class);
        crossBuildAction.setPreviousStepBuildResult(build.getResult());
        if(Result.SUCCESS.equals(crossBuildAction.getPreviousStepBuildResult())) {
            crossBuildAction.setScanId(sharedBuildStep.getScanId());
            crossBuildAction.setCorrelationId(correlationId);
        }
        try{build.save();} catch(IOException ex){log.println("Error saving settings. Error message: " + ex.toString());}
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public StaticAssessmentStepDescriptor getDescriptor() {
        return (StaticAssessmentStepDescriptor) super.getDescriptor();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @SuppressWarnings("unused")
    public String getReleaseId() {
        return sharedBuildStep.getModel().getReleaseId();
    }

    // NOTE: The following Getters are used to return saved values in the config.jelly. Intellij
    // marks them unused, but they actually are used.
    // These getters are also named in the following format: Get<JellyField>.
    @SuppressWarnings("unused")
    public String getBsiToken() {
        return sharedBuildStep.getModel().getBsiTokenOriginal();
    }

    @SuppressWarnings("unused")
    public String getUsername() {
        return sharedBuildStep.getAuthModel().getUsername();
    }

    @SuppressWarnings("unused")
    public String getPersonalAccessToken() {
        return sharedBuildStep.getAuthModel().getPersonalAccessToken();
    }

    @SuppressWarnings("unused")
    public String getTenantId() {
        return sharedBuildStep.getAuthModel().getTenantId();
    }

    @SuppressWarnings("unused")
    public boolean getOverrideGlobalConfig() {
        return sharedBuildStep.getAuthModel().getOverrideGlobalConfig();
    }

    @SuppressWarnings("unused")
    public String getEntitlementPreference() {
        return sharedBuildStep.getModel().getEntitlementPreference();
    }

    @SuppressWarnings("unused")
    public boolean getPurchaseEntitlements() {
        return sharedBuildStep.getModel().isPurchaseEntitlements();
    }

    @SuppressWarnings("unused")
    public String getSrcLocation() {
        return sharedBuildStep.getModel().getSrcLocation();
    }

    @SuppressWarnings("unused")
    public String getRemediationScanPreferenceType() {
        return sharedBuildStep.getModel().getRemediationScanPreferenceType();
    }

    @SuppressWarnings("unused")
    public String getInProgressScanActionType() {
        return sharedBuildStep.getModel().getInProgressScanActionType();
    }

    @SuppressWarnings("unused")
    public String getInProgressBuildResultType() {
        return sharedBuildStep.getModel().getInProgressBuildResultType();
    }

    @SuppressWarnings("unused")
    public String getUserSelectedApplication() {
        return sharedBuildStep.getModel().getUserSelectedApplication();
    }

    @SuppressWarnings("unused")
    public String getUserSelectedMicroservice() {
        return sharedBuildStep.getModel().getUserSelectedMicroservice();
    }

    @SuppressWarnings("unused")
    public String getUserSelectedRelease() {
        return sharedBuildStep.getModel().getUserSelectedRelease();
    }

    @SuppressWarnings("unused")
    public String getSelectedReleaseType() {
        return sharedBuildStep.getModel().getSelectedReleaseType();
    }

    @Extension
    public static final class StaticAssessmentStepDescriptor extends BuildStepDescriptor<Publisher> {

        private String showReleaseIdOptions;
        String selectedReleaseType;
        private static final String showReleaseIdOptionsField = "showReleaseIdOptionsField";
        /**
         * In order to load the persisted global configuration, you have to
         * call load() in the constructor.
         */
        // Entry point when accessing global configuration
        public StaticAssessmentStepDescriptor() {
            super();
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public FormValidation doCheckReleaseId(@QueryParameter String releaseId, @QueryParameter String bsiToken) {
            Jenkins.get().checkPermission(Jenkins.ADMINISTER);
            return SharedUploadBuildStep.doCheckReleaseId(releaseId, bsiToken);
        }

        public FormValidation doCheckBsiToken(@QueryParameter String bsiToken, @QueryParameter String releaseId) {
            Jenkins.get().checkPermission(Jenkins.ADMINISTER);
            return SharedUploadBuildStep.doCheckBsiToken(bsiToken, releaseId);
        }

        @Override
        public String getDisplayName() {
            return "Fortify on Demand Static Assessment";
        }


        // Form validation
        @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "unused"})
        @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
        @POST
        public FormValidation doTestPersonalAccessTokenConnection(@QueryParameter(SharedUploadBuildStep.USERNAME) final String username,
                                                                  @QueryParameter(SharedUploadBuildStep.PERSONAL_ACCESS_TOKEN) final String personalAccessToken,
                                                                  @QueryParameter(SharedUploadBuildStep.TENANT_ID) final String tenantId,
                                                                  @AncestorInPath Job job) {
            job.checkPermission(Item.CONFIGURE);
            return SharedUploadBuildStep.doTestPersonalAccessTokenConnection(username, personalAccessToken, tenantId,job);
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillEntitlementPreferenceItems() {
            return SharedUploadBuildStep.doFillEntitlementPreferenceItems();
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillRemediationScanPreferenceTypeItems() {
            return SharedUploadBuildStep.doFillRemediationScanPreferenceTypeItems();
        }

        @SuppressWarnings("unused")

        public ListBoxModel doFillUsernameItems(@AncestorInPath Job job) {
            return SharedUploadBuildStep.doFillStringCredentialsItems(job);
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillPersonalAccessTokenItems(@AncestorInPath Job job) {
            return SharedUploadBuildStep.doFillStringCredentialsItems(job);
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillTenantIdItems(@AncestorInPath Job job) {
            return SharedUploadBuildStep.doFillStringCredentialsItems(job);
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillInProgressScanActionTypeItems() {
            return SharedUploadBuildStep.doFillInProgressScanActionTypeItems();
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillInProgressBuildResultTypeItems() {
            return SharedUploadBuildStep.doFillInProgressBuildResultTypeItems();
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillSelectedReleaseTypeItems() {
            return SharedUploadBuildStep.doFillSelectedReleaseTypeItems();
        }

        @JavaScriptMethod
        public String retrieveApplicationList() {
            return SharedUploadBuildStep.customFillUserSelectedApplicationList();
        }

        @JavaScriptMethod
        public String retrieveMicroserviceList(int selectedApplicationId) {
            return SharedUploadBuildStep.customFillUserSelectedMicroserviceList(selectedApplicationId);
        }

        @JavaScriptMethod
        public String retriveReleaseList(int selectedApplicationId) {
            return SharedUploadBuildStep.customFillUserSelectedReleaseList(selectedApplicationId);
        }

        @JavaScriptMethod
        public String testBsiTokenParser(String testBsiTokenString) {
            BsiTokenParser parser = new BsiTokenParser();
            org.jenkinsci.plugins.fodupload.models.BsiToken token = parser.parseBsiToken(testBsiTokenString);
            Gson gson = new Gson();
            return gson.toJson(token);
        }
    }

}
