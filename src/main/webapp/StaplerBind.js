initializeControls();

function initializeControls() {
    jQuery3('#releaseTypeSelectList').change(function(){
        let viewChoice = jQuery3('#releaseTypeSelectList').val();
        managePickReleaseView(viewChoice);
    });
    jQuery3('#applicationSeletList').change(function(){
        backend.retrieveMicroserviceList(function(t) {
            const microserviceSelectList = jQuery3('#microserviceSeletList');
            const responseJson = jQuery.parseJson(t.responseObject());
            
            updateSelectListWithResponse(microserviceSelectList, responseJson);
        });

        backend.retriveReleaseList(function(t) {
            const releaseSelectList = jQuery3('#releaseSelectList');
            const responseJson = jQuery.parseJson(t.responseObject());
            
            updateSelectListWithResponse(releaseSelectList, responseJson);
        });

        jQuery3('#userSelectedApplication').val(jQuery3('#applicationSeletList').val());
    });
    jQuery3('#microserviceSeletList').change(function(){
        jQuery3('#userSelectedMicroservice').val(jQuery3('#microserviceSeletList').val());
    });
    jQuery3('#releaseSeletList').change(function(){
        jQuery3('#userSelectedRelease').val(jQuery3('#releaseTypeSelectList').val());
    });
}

function updateSelectListWithResponse(selectListJquery, jsonResponse) {
    jsonResponse.each(function(item) {
        selectListJquery.append('<option value="' + item.Key + '">' + item.Value + '</option>')
    })
}

function managePickReleaseView(viewChoice) {
    if(viewChoice == "UseAppAndReleaseName") {
        showPickReleaseViewAppAndReleaseNameView();
    } else if (viewChoice == "UseReleaseId") {
        showPickReleaseViewReleaseIdView();
    }
}

function hideAllPickReleaseViews() {
    jQuery3('#allPickReleaseViews').hide();
}

function showPickReleaseViewAppAndReleaseNameView() {
    jQuery3('#allPickReleaseViews').show();
    jQuery3('#appAndReleaseNameView').show();
    jQuery3('#releaseIdView').hide();
}

function showPickReleaseViewReleaseIdView() {
    jQuery3('#allPickReleaseViews').show();
    jQuery3('#releaseIdView').show();
    jQuery3('#appAndReleaseNameView').hide();
}
