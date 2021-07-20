jq = jQuery;

const jobSettings = new JobSettings(instance);

function hideAll() {
    jq('.releaseIdView').hide();
    jq('.bsiTokenView').hide();
    jq('.appAndReleaseNameView').hide();
    jq('.appAndReleaseNameErrorView').hide();
}

function onCredsChanged() {
    const viewChoice = jq('#releaseTypeSelectList').val();

    if (viewChoice == "UseAppAndReleaseName") {
        initAppSelection(true);
    }
}

function onApplicationCreated(applicationId, applicationName, hasMicroservices) {
    const viewChoice = jq('#releaseTypeSelectList').val();

    if (viewChoice == "UseAppAndReleaseName") {
        const applicationSelection = jq('#applicationSelectList');
        applicationSelection.append('<option hasMicroServices="' + hasMicroservices + '" value="' + applicationId + '">' + applicationName + '</option>');
        applicationSelection.val(applicationId);
        onAppSelection(false);
    }
}

function onMicroserviceCreated(microserviceId, microserviceName) {
    const viewChoice = jq('#releaseTypeSelectList').val();

    if (viewChoice == "UseAppAndReleaseName") {
        const microserviceSelection = jq('#microserviceSelectList');
        microserviceSelection.append('<option value="' + microserviceId + '">' + microserviceName + '</option>');
        microserviceSelection.val(microserviceId);
        onMicroserviceSelection(false);
    }
}

function onReleaseCreated(releaseId, releaseName) {
    const viewChoice = jq('#releaseTypeSelectList').val();

    if (viewChoice == "UseAppAndReleaseName") {
        const microserviceSelection = jq('#releaseSelectList');
        microserviceSelection.append('<option value="' + releaseId + '">' + releaseName + '</option>');
        microserviceSelection.val(releaseId);
    }
}

function onReleaseMethodSelection() {
    hideAll();
    const viewChoice = jq('#releaseTypeSelectList').val();

    switch (viewChoice) {
        case "UseBsiToken":
            initBsiToken();
            break;
        case "UseReleaseId":
            initReleaseId();
            break;
        case "UseAppAndReleaseName":
            initAppSelection(true);
            break;
    }
}

async function initBsiToken() {
    const savedBsiToken = await jobSettings.getSavedBsiToken();
    if (savedBsiToken) {
        jq('#bsiTokenField').val(savedBsiToken);
    }
    jq('.bsiTokenView').show();
}

async function initReleaseId() {
    const savedReleaseId = await jobSettings.getSavedReleaseId();
    if (savedReleaseId) {
        jq('#releaseIdField').val(savedReleaseId);
    }
    jq('.releaseIdView').show();
}

function initAppSelection(isInit, selectedValues) {
    jq('.appAndReleaseNameView').show();
    jq('#appAndReleaseNameErrorView').hide();
    jq('#microserviceSelectForm').hide();
    jq('#releaseSelectForm').hide();

    showWithSpinner('#applicationSelectForm');

    descriptor.retrieveApplicationList(getAuthInfo(), async t => {
        const applicationSelection = jq('#applicationSelectList');
        const responseJson = JSON.parse(t.responseJSON);
        if (responseJson === null) {
            return showApiRetrievalError();
        }

        applicationSelection.empty();

        for(const app of responseJson) {
            applicationSelection.append('<option hasMicroServices="' + app.hasMicroservices + '" value="' + app.applicationId + '">' + app.applicationName + '</option>');
        }

        if (isInit) {
            const savedAppId = await jobSettings.getSavedSelectedApplicationId();
            if (savedAppId) {
                jq('#applicationSelectList').val(savedAppId);
            }
        }
        else {
            if (selectedValues && selectedValues.applicationId) {
                jq('#applicationSelectList').val(selectedValues.applicationId);
            }
        }

        onAppSelection(isInit);
        hideSpinner('#applicationSelectForm');
        jq('#applicationSelectList').off('change').change(() => onAppSelection(false));
    });
}

function onAppSelection(isInit, selectedValues) {
    jq('#microserviceSelectForm').hide();
    jq('#releaseSelectForm').hide();

    const hasMicroservices = jq('#applicationSelectList option:selected').attr('hasMicroServices') === 'true';
    if (hasMicroservices) {
        initMicroserviceSelection(isInit, selectedValues);
    }
    else {
        initReleaseSelection(isInit, selectedValues);
    }
}

function initMicroserviceSelection(isInit, selectedValues) {
    jq('#releaseSelectForm').hide();
    showWithSpinner('#microserviceSelectForm');

    const appId = jq('#applicationSelectList').val();
    descriptor.retrieveMicroserviceList(appId, getAuthInfo(), async t => {
        const microserviceSelection = jq('#microserviceSelectList');
        const responseJson = JSON.parse(t.responseJSON);
        microserviceSelection.empty();

        for (const ms of responseJson) {
            microserviceSelection.append('<option value="' + ms.microserviceId + '">' + ms.microserviceName + '</option>');
        }

        if (isInit) {
            const savedMicroserviceId = await jobSettings.getSavedSelectedMicroserviceId();
            if (savedMicroserviceId) {
                jq('#microserviceSelectList').val(savedMicroserviceId);
            }
        }

        hideSpinner('#microserviceSelectForm');

        onMicroserviceSelection(isInit, selectedValues);
        jq('#microserviceSelectList').off('change').change(() => onMicroserviceSelection(false, selectedValues));
    });
}

function onMicroserviceSelection(isInit, selectedValues) {
    initReleaseSelection(isInit, selectedValues);
}

function initReleaseSelection(isInit, selectedValues) {
    showWithSpinner('#releaseSelectForm');

    const appId = jq('#applicationSelectList').val();
    const hasMicroservices = jq('#applicationSelectList option:selected').attr('hasMicroServices') === 'true';

    const microserviceId = !hasMicroservices ? -1 : jq('#microserviceSelectList').val();

    descriptor.retrieveReleaseList(appId, microserviceId, getAuthInfo(), async t => {
        const releaseSelection = jq('#releaseSelectList');
        const responseJson = JSON.parse(t.responseJSON);
        releaseSelection.empty();

        for (const release of responseJson) {
            releaseSelection.append('<option value="' + release.releaseId + '">' + release.releaseName + '</option>');
        }

        if (isInit) {
            const savedReleaseId = await jobSettings.getSavedSelectedReleaseId();
            if (savedReleaseId) {
                jq('#releaseSelectList').val(savedReleaseId);
            }
        }

        hideSpinner('#releaseSelectForm');
    });
}

function showApiRetrievalError() {
    hideAll();
    jq('#appAndReleaseNameErrorView').show();
}

function init() {
    onReleaseMethodSelection();
    jq('#releaseTypeSelectList').off('change').change(onReleaseMethodSelection);
    subscribeToEvent('authInfoChanged', () => onCredsChanged());
    subscribeToEvent('applicationCreated', x => onApplicationCreated(x.detail.applicationId, x.detail.applicationName, x.detail.hasMicroservices));
    subscribeToEvent('microserviceCreated', x => onMicroserviceCreated(x.detail.microserviceId, x.detail.microserviceName));
    subscribeToEvent('releaseCreated', x => onReleaseCreated(x.detail.releaseId, x.detail.releaseName));
}

spinAndWait(() => jq('#releaseTypeSelectList').val()).then(init);