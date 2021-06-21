initializeControls();

function initializeControls() {



    jQuery3('#microserviceSeletList').change(function(){
        jQuery3('#userSelectedMicroservice').val(jQuery3('#microserviceSeletList').val());
    });
    jQuery3('#releaseSeletList').change(function(){
        jQuery3('#userSelectedRelease').val(jQuery3('#releaseTypeSelectList').val());
    });
}

function updateSelectListWithResponse(selectListJquery, jsonResponse) {
    selectListJquery.empty();
    for(const app of jsonResponse) {
        selectListJquery.append('<option value="' + app.applicationId + '">' + app.applicationName + '</option>');
    }
}

function managePickReleaseView(viewChoice) {
    if(viewChoice == "UseAppAndReleaseName") {
        showPickReleaseViewAppAndReleaseNameView();
    } else if (viewChoice == "UseReleaseId") {
        showPickReleaseViewReleaseIdView();
    } else if (viewChoice == "UseBsiToken") {
        showPickReleaseViewBsiTokenView();
    }
}

function hideAllPickReleaseViews() {
    jQuery3('#allPickReleaseViews').hide();
}

function showPickReleaseViewAppAndReleaseNameView() {
    jQuery3('#allPickReleaseViews').show();
    jQuery3('#appAndReleaseNameView').show();
    jQuery3('#releaseIdView').hide();
    jQuery3('#bsiTokenView').hide();

    if (jQuery3('#microserviceSeletList').is(':empty')){
        jQuery3('#microserviceSelectForm').hide();
    }
}

function showPickReleaseViewReleaseIdView() {
    jQuery3('#allPickReleaseViews').show();
    jQuery3('#releaseIdView').show();
    jQuery3('#bsiTokenView').hide();
    jQuery3('#appAndReleaseNameView').hide();
}

function showPickReleaseViewBsiTokenView() {
    jQuery3('#allPickReleaseViews').show();
    jQuery3('#bsiTokenView').show();
    jQuery3('#releaseIdView').hide();
    jQuery3('#appAndReleaseNameView').hide();
}
