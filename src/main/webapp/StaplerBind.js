initializeControls();

function initializeControls() {
    jQuery3('#releaseTypeSelectList').change(function(){
        let viewChoice = jQuery3('#releaseTypeSelectList').val();
        backend.retrieveApplicationList(function(t) {
            const applicationSelection = jQuery3('#applicationSeletList');
            const responseJson = jQuery.parseJson(t.responseObject());

            updateSelectListWithResponse(applicationSelection, responseJson);
        });
        managePickReleaseView(viewChoice);
    });
    jQuery3('#applicationSeletList').change(function(){
        let applicationSelection = jQuery3('#applicationSeletList').val();
        backend.retrieveMicroserviceList(applicationSelection, function(t) {
            const microserviceSelectList = jQuery3('#microserviceSeletList');
            const responseJson = jQuery.parseJson(t.responseObject());
            
            updateSelectListWithResponse(microserviceSelectList, responseJson);
        });

        backend.retriveReleaseList(applicationSelection, function(t) {
            const releaseSelectList = jQuery3('#releaseSelectList');
            const responseJson = jQuery.parseJson(t.responseObject());
            
            updateSelectListWithResponse(releaseSelectList, responseJson);
        });

        jQuery3('#userSelectedApplication').val(applicationSelection);
    });
    jQuery3('#microserviceSeletList').change(function(){
        jQuery3('#userSelectedMicroservice').val(jQuery3('#microserviceSeletList').val());
    });
    jQuery3('#releaseSeletList').change(function(){
        jQuery3('#userSelectedRelease').val(jQuery3('#releaseTypeSelectList').val());
    });
}

function updateSelectListWithResponse(selectListJquery, jsonResponse) {
    selectListJquery.empty();
    jsonResponse.each(function(item) {
        selectListJquery.append('<option value="' + item.Key + '">' + item.Value + '</option>');
    })
}

function managePickReleaseView(viewChoice) {
    if(viewChoice == "UseAppAndReleaseName") {
        showPickReleaseViewAppAndReleaseNameView();
    } else if (viewChoice == "UseReleaseId") {
        showPickReleaseViewReleaseIdView();
    } else if (viewChoice == "UseBsiToken") {
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
    jQuery3('#bsiTokenView').hide();

    if ($('#microserviceSeletList').is(':empty')){
        jQuery3('#microserviceSeletList').prop('disabled', true);
    }
      
    if ($('#releaseSeletList').is(':empty')){
    jQuery3('#releaseSeletList').prop('disabled', true);
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
