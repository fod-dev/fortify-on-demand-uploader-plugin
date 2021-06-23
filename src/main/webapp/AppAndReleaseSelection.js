jq = jQuery;

function hideAll() {
    jq('#releaseIdView').hide();
    jq('#bsiTokenView').hide();
    jq('#appAndReleaseNameView').hide();
}

function onReleaseMethodSelection() {
    hideAll();
    const viewChoice = jq('#releaseTypeSelectList').val();

    switch (viewChoice) {
        case "UseBsiToken":
            jq('#bsiTokenView').show();
            break;
        case "UseReleaseId":
            jq('#releaseIdView').show();
            break;
        case "UseAppAndReleaseName":
            initAppSelection();
            break;
    }
}

function initAppSelection() {
    jq('#microserviceSelectForm').hide();
    jq('#releaseSelectForm').hide();
    backend.retrieveApplicationList(t => {
        const applicationSelection = jq('#applicationSelectList');
        const responseJson = JSON.parse(t.responseJSON);
        applicationSelection.empty();

        for(const app of responseJson) {
            applicationSelection.append('<option hasMicroServices="' + app.hasMicroservices + '" value="' + app.applicationId + '">' + app.applicationName + '</option>');
        }

        jq('#appAndReleaseNameView').show();

        onAppSelection();
        jq('#applicationSelectList').off('change').change(() => onAppSelection());
    });
}

function onAppSelection() {
    jq('#microserviceSelectForm').hide();
    jq('#releaseSelectForm').hide();

    const hasMicroservices = jq('#applicationSelectList option:selected').attr('hasMicroServices') === 'true';
    if (hasMicroservices) {
        initMicroserviceSelection();
    }
    else {
        initReleaseSelection();
    }
}

function initMicroserviceSelection() {
    jq('#releaseSelectForm').hide();

    const appId = jq('#applicationSelectList').val();
    backend.retrieveMicroserviceList(appId, t => {
        const microserviceSelection = jq('#microserviceSelectList');
        const responseJson = JSON.parse(t.responseJSON);
        microserviceSelection.empty();

        for (const ms of responseJson) {
            microserviceSelection.append('<option value="' + ms.microserviceId + '">' + ms.microserviceName + '</option>');
        }

        jq('#microserviceSelectForm').show();

        onMicroserviceSelection();
        jq('#microserviceSelectList').off('change').change(onMicroserviceSelection);
    });
}

function onMicroserviceSelection() {
    initReleaseSelection();
}

function initReleaseSelection() {
    const appId = jq('#applicationSelectList').val();
    const hasMicroservices = jq('#applicationSelectList option:selected').attr('hasMicroServices') === 'true';

    const microserviceId = !hasMicroservices ? -1 : jq('#microserviceSelectList').val();

    backend.retrieveReleaseList(appId, microserviceId, t => {
        const releaseSelection = jq('#releaseSeletList');
        const responseJson = JSON.parse(t.responseJSON);
        releaseSelection.empty();

        for (const release of responseJson) {
            releaseSelection.append('<option value="' + release.releaseId + '">' + release.releaseName + '</option>');
        }

        jq('#releaseSelectForm').show();
    });
}

function init() {
    onReleaseMethodSelection();
    jq('#releaseTypeSelectList').off('change').change(onReleaseMethodSelection);
}

function waitForReleaseMethodToInit() {
    return new Promise((res, rej) => {
        let elementsLoaded;

        elementsLoaded = () => {
            if(jq('#releaseTypeSelectList').val()) res();
            else setTimeout(elementsLoaded, 50);
        };

        elementsLoaded();
    });
}

waitForReleaseMethodToInit().then(init);