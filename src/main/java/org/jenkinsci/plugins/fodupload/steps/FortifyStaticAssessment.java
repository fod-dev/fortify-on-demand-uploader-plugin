package org.jenkinsci.plugins.fodupload.steps;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.fodupload.ApiConnectionFactory;
import org.jenkinsci.plugins.fodupload.FodApiConnection;
import org.jenkinsci.plugins.fodupload.SharedUploadBuildStep;
import org.jenkinsci.plugins.fodupload.Utils;
import org.jenkinsci.plugins.fodupload.actions.CrossBuildAction;
import org.jenkinsci.plugins.fodupload.controllers.AssessmentTypesController;
import org.jenkinsci.plugins.fodupload.controllers.LookupItemsController;
import org.jenkinsci.plugins.fodupload.controllers.StaticScanController;
import org.jenkinsci.plugins.fodupload.controllers.UsersController;
import org.jenkinsci.plugins.fodupload.models.AuthenticationModel;
import org.jenkinsci.plugins.fodupload.models.FodEnums;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.verb.POST;


@SuppressFBWarnings("unused")
public class FortifyStaticAssessment extends FortifyStep {

    private static final ThreadLocal<TaskListener> taskListener = new ThreadLocal<>();

    private String releaseId;
    private String bsiToken;

    private boolean overrideGlobalConfig;
    private String username;
    private String personalAccessToken;
    private String tenantId;

    private boolean purchaseEntitlements;
    private String entitlementPreference;
    private String srcLocation;
    private String remediationScanPreferenceType;
    private String inProgressScanActionType;
    private String inProgressBuildResultType;

    private String assessmentType;
    private String entitlementId;
    private String frequencyId;
    private String auditPreference;
    private String technologyStack;
    private String languageLevel;
    private String openSourceScan;

    private String scanCentral;
    private String scanCentralIncludeTests;
    private String scanCentralSkipBuild;
    private String scanCentralBuildCommand;
    private String scanCentralBuildFile;
    private String scanCentralBuildToolVersion;
    private String scanCentralVirtualEnv;
    private String scanCentralRequirementFile;

    private String applicationName;
    private String applicationType;
    private String releaseName;
    private String owner;
    private String attributes;
    private String businessCriticality;
    private String sdlcStatus;
    private String microserviceName;
    private String isMicroservice;

    private SharedUploadBuildStep commonBuildStep;

    @DataBoundConstructor
    public FortifyStaticAssessment() {
        super();
    }

    public String getBsiToken() {
        return bsiToken;
    }

    @DataBoundSetter
    public void setBsiToken(String bsiToken) {
        this.bsiToken = bsiToken.trim();
    }

    public String getReleaseId() {
        return releaseId;
    }

    @DataBoundSetter
    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId.trim();
    }

    public boolean getOverrideGlobalConfig() {
        return overrideGlobalConfig;
    }

    @DataBoundSetter
    public void setOverrideGlobalConfig(boolean overrideGlobalConfig) {
        this.overrideGlobalConfig = overrideGlobalConfig;
    }

    public String getUsername() {
        return username;
    }

    @DataBoundSetter
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonalAccessToken() {
        return personalAccessToken;
    }

    @DataBoundSetter
    public void setPersonalAccessToken(String personalAccessToken) {
        this.personalAccessToken = personalAccessToken;
    }

    public String getTenantId() {
        return tenantId;
    }

    @DataBoundSetter
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean getPurchaseEntitlements() {
        return purchaseEntitlements;
    }

    @DataBoundSetter
    public void setPurchaseEntitlements(boolean purchaseEntitlements) {
        this.purchaseEntitlements = purchaseEntitlements;
    }

    public String getEntitlementPreference() {
        return entitlementPreference;
    }

    @DataBoundSetter
    public void setEntitlementPreference(String entitlementPreference) {
        this.entitlementPreference = entitlementPreference;
    }

    public String getSrcLocation() {
        return srcLocation;
    }

    @DataBoundSetter
    public void setSrcLocation(String srcLocation) {
        this.srcLocation = srcLocation != null ? srcLocation.trim() : "";
    }

    public String getRemediationScanPreferenceType() {
        return remediationScanPreferenceType;
    }

    @DataBoundSetter
    public void setRemediationScanPreferenceType(String remediationScanPreferenceType) {
        this.remediationScanPreferenceType = remediationScanPreferenceType;
    }

    public String getInProgressScanActionType() {
        return inProgressScanActionType;
    }

    @DataBoundSetter
    public void setInProgressScanActionType(String inProgressScanActionType) {
        this.inProgressScanActionType = inProgressScanActionType;
    }

    public String getInProgressBuildResultType() {
        return inProgressBuildResultType;
    }

    @DataBoundSetter
    public void setInProgressBuildResultType(String inProgressBuildResultType) {
        this.inProgressBuildResultType = inProgressBuildResultType;
    }

    @SuppressWarnings("unused")
    public String getScanCentral() {
        return scanCentral;
    }

    @DataBoundSetter
    public void setScanCentral(String scanCentral) {
        this.scanCentral = scanCentral;
    }

    @SuppressWarnings("unused")
    public String getScanCentralIncludeTests() {
        return scanCentralIncludeTests;
    }

    @DataBoundSetter
    public void setScanCentralIncludeTests(String scanCentralIncludeTests) {
        this.scanCentralIncludeTests = scanCentralIncludeTests;
    }

    @SuppressWarnings("unused")
    public String getScanCentralSkipBuild() {
        return scanCentralSkipBuild;
    }

    @DataBoundSetter
    public void setScanCentralSkipBuild(String scanCentralSkipBuild) {
        this.scanCentralSkipBuild = scanCentralSkipBuild;
    }

    @SuppressWarnings("unused")
    public String getScanCentralBuildCommand() {
        return scanCentralBuildCommand;
    }

    @DataBoundSetter
    public void setScanCentralBuildCommand(String scanCentralBuildCommand) {
        this.scanCentralBuildCommand = scanCentralBuildCommand;
    }

    @SuppressWarnings("unused")
    public String getScanCentralBuildFile() {
        return scanCentralBuildFile;
    }

    @DataBoundSetter
    public void setScanCentralBuildFile(String scanCentralBuildFile) {
        this.scanCentralBuildFile = scanCentralBuildFile;
    }

    @SuppressWarnings("unused")
    public String getScanCentralBuildToolVersion() {
        return scanCentralBuildToolVersion;
    }

    @DataBoundSetter
    public void setScanCentralBuildToolVersion(String scanCentralBuildToolVersion) {
        this.scanCentralBuildToolVersion = scanCentralBuildToolVersion;
    }

    @SuppressWarnings("unused")
    public String getScanCentralVirtualEnv() {
        return scanCentralVirtualEnv;
    }

    @DataBoundSetter
    public void setScanCentralVirtualEnv(String scanCentralVirtualEnv) {
        this.scanCentralVirtualEnv = scanCentralVirtualEnv;
    }

    @SuppressWarnings("unused")
    public String getScanCentralRequirementFile() {
        return scanCentralRequirementFile;
    }

    @DataBoundSetter
    public void setScanCentralRequirementFile(String scanCentralRequirementFile) {
        this.scanCentralRequirementFile = scanCentralRequirementFile;
    }

    @SuppressWarnings("unused")
    public String getApplicationName() {
        return applicationName;
    }

    @DataBoundSetter
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @SuppressWarnings("unused")
    public String getApplicationType() {
        return applicationType;
    }

    @DataBoundSetter
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    @SuppressWarnings("unused")
    public String getReleaseName() {
        return releaseName;
    }

    @DataBoundSetter
    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    @SuppressWarnings("unused")
    public String getOwner() {
        return owner;
    }

    @DataBoundSetter
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @SuppressWarnings("unused")
    public String getAttributes() {
        return attributes;
    }

    @DataBoundSetter
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @SuppressWarnings("unused")
    public String getBusinessCriticality() {
        return businessCriticality;
    }

    @DataBoundSetter
    public void setBusinessCriticality(String businessCriticality) {
        this.businessCriticality = businessCriticality;
    }

    @SuppressWarnings("unused")
    public String getSdlcStatus() {
        return sdlcStatus;
    }

    @DataBoundSetter
    public void setSdlcStatus(String sdlcStatus) {
        this.sdlcStatus = sdlcStatus;
    }

    @SuppressWarnings("unused")
    public String getMicroserviceName() {
        return microserviceName;
    }

    @DataBoundSetter
    public void setMicroserviceName(String microserviceName) {
        this.microserviceName = microserviceName;
    }

    @SuppressWarnings("unused")
    public String getIsMicroservice() {
        return isMicroservice;
    }

    @DataBoundSetter
    public void setIsMicroservice(String isMicroservice) {
        this.isMicroservice = isMicroservice;
    }


    @SuppressWarnings("unused")
    public String getAssessmentType() {
        return assessmentType;
    }

    @DataBoundSetter
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    @SuppressWarnings("unused")
    public String getEntitlementId() {
        return entitlementId;
    }

    @DataBoundSetter
    public void setEntitlementId(String entitlementId) {
        this.entitlementId = entitlementId;
    }

    @SuppressWarnings("unused")
    public String getFrequencyId() {
        return frequencyId;
    }

    @DataBoundSetter
    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    @SuppressWarnings("unused")
    public String getAuditPreference() {
        return auditPreference;
    }

    @DataBoundSetter
    public void setAuditPreference(String auditPreference) {
        this.auditPreference = auditPreference;
    }

    @SuppressWarnings("unused")
    public String getTechnologyStack() {
        return technologyStack;
    }

    @DataBoundSetter
    public void setTechnologyStack(String technologyStack) {
        this.technologyStack = technologyStack;
    }

    @SuppressWarnings("unused")
    public String getLanguageLevel() {
        return languageLevel;
    }

    @DataBoundSetter
    public void setLanguageLevel(String languageLevel) {
        this.languageLevel = languageLevel;
    }

    @SuppressWarnings("unused")
    public String getOpenSourceScan() {
        return openSourceScan;
    }

    @DataBoundSetter
    public void setOpenSourceScan(String openSourceScan) {
        this.openSourceScan = openSourceScan;
    }


    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        PrintStream log = listener.getLogger();
        log.println("Fortify on Demand Upload PreBuild Running...");
        commonBuildStep = new SharedUploadBuildStep(releaseId,
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
                scanCentral,
                scanCentralIncludeTests != null && scanCentralIncludeTests.equalsIgnoreCase("true"),
                scanCentralSkipBuild != null && scanCentralSkipBuild.equalsIgnoreCase("true"),
                scanCentralBuildCommand,
                scanCentralBuildFile,
                scanCentralBuildToolVersion,
                scanCentralVirtualEnv,
                scanCentralRequirementFile,
                assessmentType,
                entitlementId,
                frequencyId,
                auditPreference,
                technologyStack,
                languageLevel,
                openSourceScan,
                !Utils.isNullOrEmpty(applicationName),
                applicationName,
                applicationType,
                releaseName,
                owner,
                attributes,
                businessCriticality,
                sdlcStatus,
                microserviceName,
                isMicroservice);

        return true;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new Execution(this, context);
    }

    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void perform(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener) throws IOException, IllegalArgumentException {
        PrintStream log = listener.getLogger();
        log.println("Fortify on Demand Upload Running...");
        build.addAction(new CrossBuildAction());
        try {
            build.save();
        } catch (IOException ex) {
            log.println("Error saving settings. Error message: " + ex.toString());
        }


        remediationScanPreferenceType = remediationScanPreferenceType != null ? remediationScanPreferenceType : FodEnums.RemediationScanPreferenceType.RemediationScanIfAvailable.getValue();
        inProgressScanActionType = inProgressScanActionType != null ? inProgressScanActionType : FodEnums.InProgressScanActionType.DoNotStartScan.getValue();
        inProgressBuildResultType = inProgressBuildResultType != null ? inProgressBuildResultType : FodEnums.InProgressBuildResultType.FailBuild.getValue();

        String correlationId = UUID.randomUUID().toString();

        commonBuildStep = new SharedUploadBuildStep(releaseId,
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
                scanCentral,
                scanCentralIncludeTests != null && scanCentralIncludeTests.equalsIgnoreCase("true"),
                scanCentralSkipBuild != null && scanCentralSkipBuild.equalsIgnoreCase("true"),
                scanCentralBuildCommand,
                scanCentralBuildFile,
                scanCentralBuildToolVersion,
                scanCentralVirtualEnv,
                scanCentralRequirementFile,
                assessmentType,
                entitlementId,
                frequencyId,
                auditPreference,
                technologyStack,
                languageLevel,
                openSourceScan,
                !Utils.isNullOrEmpty(applicationName),
                applicationName,
                applicationType,
                releaseName,
                owner,
                attributes,
                businessCriticality,
                sdlcStatus,
                microserviceName,
                isMicroservice);

        commonBuildStep.perform(build, workspace, launcher, listener, correlationId);
        CrossBuildAction crossBuildAction = build.getAction(CrossBuildAction.class);
        crossBuildAction.setPreviousStepBuildResult(build.getResult());
        if (Result.SUCCESS.equals(crossBuildAction.getPreviousStepBuildResult())) {
            crossBuildAction.setScanId(commonBuildStep.getScanId());
            crossBuildAction.setCorrelationId(correlationId);
        }
        try {
            build.save();
        } catch (IOException ex) {
            log.println("Error saving settings. Error message: " + ex.toString());
        }
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {
        @Override
        public String getDisplayName() {
            return "Run Fortify on Demand Upload";
        }

        @Override
        public String getFunctionName() {
            return "fodStaticAssessment";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(Run.class, FilePath.class, Launcher.class, TaskListener.class);
        }

        // Form validation
        @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "unused"})
        @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
        @POST
        public FormValidation doTestPersonalAccessTokenConnection(@QueryParameter("usernameStaplerOnly") final String username,
                                                                  @QueryParameter("personalAccessTokenSelect") final String personalAccessToken,
                                                                  @QueryParameter("tenantIdStaplerOnly") final String tenantId,
                                                                  @AncestorInPath Job job) {
            job.checkPermission(Item.CONFIGURE);
            return SharedUploadBuildStep.doTestPersonalAccessTokenConnection(username, personalAccessToken, tenantId, job);


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
        public ListBoxModel doFillPersonalAccessTokenSelectItems(@AncestorInPath Job job) {
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

        @JavaScriptMethod
        public String retrieveCurrentUserSession(JSONObject authModelObject) {
            try {
                AuthenticationModel authModel = Utils.getAuthModelFromObject(authModelObject);
                FodApiConnection apiConnection = ApiConnectionFactory.createApiConnection(authModel);
                UsersController usersController = new UsersController(apiConnection, null, Utils.createCorrelationId());

                return Utils.createResponseViewModel(usersController.getCurrentUserSession());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @JavaScriptMethod
        public String retrieveAssessmentTypeEntitlements(Integer releaseId, JSONObject authModelObject) {
            try {
                AuthenticationModel authModel = Utils.getAuthModelFromObject(authModelObject);
                FodApiConnection apiConnection = ApiConnectionFactory.createApiConnection(authModel);
                AssessmentTypesController assessmentTypesController = new AssessmentTypesController(apiConnection, null, Utils.createCorrelationId());

                return Utils.createResponseViewModel(assessmentTypesController.getStaticAssessmentTypeEntitlements(releaseId));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @JavaScriptMethod
        public String retrieveStaticScanSettings(Integer releaseId, JSONObject authModelObject) {
            try {
                AuthenticationModel authModel = Utils.getAuthModelFromObject(authModelObject);
                FodApiConnection apiConnection = ApiConnectionFactory.createApiConnection(authModel);
                StaticScanController staticScanController = new StaticScanController(apiConnection, null, Utils.createCorrelationId());

                return Utils.createResponseViewModel(staticScanController.getStaticScanSettings(releaseId));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @JavaScriptMethod
        public String retrieveLookupItems(String type, JSONObject authModelObject) {
            try {
                AuthenticationModel authModel = Utils.getAuthModelFromObject(authModelObject);
                FodApiConnection apiConnection = ApiConnectionFactory.createApiConnection(authModel);
                LookupItemsController lookupItemsController = new LookupItemsController(apiConnection, null, Utils.createCorrelationId());

                return Utils.createResponseViewModel(lookupItemsController.getLookupItems(FodEnums.APILookupItemTypes.valueOf(type)));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    private static class Execution extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 1L;
        private transient FortifyStaticAssessment upload;

        protected Execution(FortifyStaticAssessment upload, StepContext context) {
            super(context);
            this.upload = upload;
        }

        @Override
        protected Void run() throws Exception {
            getContext().get(TaskListener.class).getLogger().println("Running fodStaticAssessment step");
            upload.perform(getContext().get(Run.class), getContext().get(FilePath.class),
                    getContext().get(Launcher.class), getContext().get(TaskListener.class));

            return null;
        }
    }
}