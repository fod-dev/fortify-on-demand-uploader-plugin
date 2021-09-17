const fodpRowSelector = '.fodp-field-row, .fodp-field-row-verr';
const fodpOverrideRowsSelector = '.fodp-row-relid-ovr';


class PipelineGenerator {
    constructor() {
        this.api = new Api(null, descriptor);
        this.overrideServerSettings = false;
        this.releaseId = null;
        this.uiLoaded = false;
        this.techStacks = {};
        this.techStacksSorted = [];
    }

    showMessage(msg, isError) {
        let msgElem;

        if (isError) msgElem = jq('#fodp-error');
        else msgElem = jq('#fodp-msg');

        msgElem.text(msg);
        msgElem.show();
    }

    hideMessages(msg) {
        jq('#fodp-error').hide();
        jq('#fodp-msg').hide();
    }

    populateAssessmentsDropdown() {
        // let atsel = jq(`#ddAssessmentType`);
        //
        // atsel.find('option').remove();
        // jq(`#entitlementSelectList`).find('option').remove();
        //
        // for (let k of Object.keys(this.assessments)) {
        //     let at = this.assessments[k];
        //
        //     atsel.append(`<option value="${at.id}">${at.name}</option>`);
        // }
    }

    onAssessmentChanged() {
        // let atval = jq('#ddAssessmentType').val();
        // let entsel = jq('#entitlementSelectList');
        // let apsel = jq('#auditPreferenceSelectList');
        // let at = this.assessments[atval];
        //
        // entsel.find('option,optgroup').remove();
        //
        // if (at) {
        //     let available = at.entitlementsSorted.filter(e => e.id > 0);
        //     let forPurchase = at.entitlementsSorted.filter(e => e.id <= 0);
        //     let availableGrp = forPurchase.length > 0 ? jq(`<optgroup label="Available Entitlements"></optgroup>`) : entsel;
        //
        //     for (let e of available) {
        //         availableGrp.append(`<option value="${this.getEntitlementDropdownValue(e.id, e.frequencyId)}">${e.description}</option>`);
        //     }
        //
        //     if (forPurchase.length > 0){
        //         let grp = jq(`<optgroup label="Available For Purchase"></optgroup>`);
        //
        //         entsel.append(availableGrp);
        //         entsel.append(grp);
        //         for (let e of forPurchase) {
        //             grp.append(`<option value="${this.getEntitlementDropdownValue(0, e.frequencyId)}">${e.description}</option>`);
        //         }
        //     }
        // }
        //
        // if (at && at.name === 'Static+ Assessment') apsel.prop('disabled', false);
        // else {
        //     apsel.val('2');
        //     apsel.prop('disabled', true);
        // }
        //
        // this.onEntitlementChanged();
        // ToDo: set to unselected if selected value doesn't exist
    }

    onEntitlementChanged() {
        // let entId = '';
        // let freqId = '';
        // let val = jq('#entitlementSelectList').val();
        //
        // if (val) {
        //     let spl = val.split('-');
        //
        //     if (spl.length === 2) {
        //         entId = numberOrNull(spl[0]);
        //         freqId = numberOrNull(spl[1]);
        //     }
        // }
        //
        // jq('#entitlementId').val(entId);
        // jq('#frequencyId').val(freqId);
        // jq('#purchaseEntitlementsForm input').prop('checked', (entId <=  0));
    }

    async loadEntitlementSettings() {
        let rows = jq(fodpOverrideRowsSelector);

        this.hideMessages();

        if (!this.releaseId) {
            rows.hide();
            this.showMessage('Enter release id', true);
            return;
        }

        let fields = jq('.fodp-field.spinner-container');

        fields.addClass('spinner');
        rows.show();
        this.onScanCentralChanged();

        // ToDo: deal with overlapping calls
        // let ssp = this.api.getReleaseEntitlementSettings(this.releaseId, getAuthInfo())
        //     .then(r => this.scanSettings = r);
        // let entp = this.api.getAssessmentTypeEntitlements(this.releaseId, getAuthInfo())
        //     .then(r => this.assessments = r);
        //
        //
        // await Promise.all([ssp, entp]);
        //
        // if (this.scanSettings && this.assessments) {
        //     let assessmentId = this.scanSettings.assessmentTypeId;
        //     let entitlementId = this.scanSettings.entitlementId;
        //
        //     this.populateAssessmentsDropdown();
        //
        //     jq('#ddAssessmentType').val(assessmentId);
        //     this.onAssessmentChanged();
        //     jq('#entitlementSelectList').val(this.getEntitlementDropdownValue(entitlementId, this.scanSettings.entitlementFrequencyType));
        //     this.onEntitlementChanged();
        //     jq('#technologyStackSelectList').val(this.scanSettings.technologyStackId);
        //     this.onTechStackChanged();
        //     jq('#languageLevelSelectList').val(this.scanSettings.languageLevelId);
        //     jq('#auditPreferenceSelectList').val(this.scanSettings.auditPreferenceType);
        //     jq('#cbSonatypeEnabled').prop('checked', this.scanSettings.performOpenSourceAnalysis === true);
        //
        // } else {
        //     this.onAssessmentChanged();
        //     this.showMessage('Failed to retrieve scan settings from API', true);
        //     rows.hide();
        // }
        await new Promise((res, rej) => setTimeout(res, 1000));

        fields.removeClass('spinner');
    }

    isDotNetStack(ts) {
        // noinspection EqualityComparisonWithCoercionJS
        return ts.value == techStackConsts.dotNet || ts.value == techStackConsts.dotNetCore;
    }

    onScanCentralChanged() {
        let val = jq('#scanCentralBuildTypeForm > select').val().toLowerCase();
        let techStackFilter;

        if (val === 'none') {
            jq('.fodp-row-sc').hide();
            if (this.overrideServerSettings) jq('.fodp-row-nonsc').show();
        } else {
            let scClass = 'fodp-row-sc-' + val;

            jq('.fodp-row-nonsc').hide();

            jq('.fodp-row-sc')
                .each((i, e) => {
                    let jqe = jq(e);

                    if (jqe.hasClass(scClass)) jqe.show();
                    else jqe.hide();
                });

            switch (val) {
                case 'msbuild':
                    closestRow(jq('#technologyStackForm')).show();
                    let currVal = this.techStacks[jq('#technologyStackSelectList').val()];

                    if (!currVal || !this.isDotNetStack(currVal)) jq('#technologyStackSelectList').val(techStackConsts.none);
                    techStackFilter = this.isDotNetStack;
                    break;
                case 'maven':
                case 'gradle':
                    jq('#technologyStackSelectList').val(techStackConsts.java);
                    break;
                case 'php':
                    jq('#technologyStackSelectList').val(techStackConsts.php);
                    break;
                case 'python':
                    jq('#technologyStackSelectList').val(techStackConsts.python);
                    break;
            }
        }

        this.populateTechStackDropdown(techStackFilter);
        this.onTechStackChanged();
    }

    populateTechStackDropdown(filter) {
        // let tsSel = jq('#technologyStackSelectList');
        // let currVal = tsSel.val();
        // let currValSelected = false;
        //
        // tsSel.find('option').not(':first').remove();
        // tsSel.find('option').first().prop('selected', true);
        //
        //
        // for (let ts of this.techStacksSorted) {
        //     if (filter && filter(ts) !== true) continue;
        //
        //     // noinspection EqualityComparisonWithCoercionJS
        //     if (currVal == ts.value) {
        //         currValSelected = true;
        //         tsSel.append(`<option value="${ts.value}" selected>${ts.text}</option>`);
        //     } else tsSel.append(`<option value="${ts.value}">${ts.text}</option>`);
        // }
        //
        // if (!currValSelected) {
        //     tsSel.find('option').first().prop('selected', true);
        //     this.onTechStackChanged();
        // }
    }

    getEntitlementDropdownValue(id, freq) {
        return `${id}-${freq}`;
    }

    onTechStackChanged() {
        // let ts = this.techStacks[jq('#technologyStackSelectList').val()];
        // let llsel = jq('#languageLevelSelectList');
        // let llr = jq('.fodp-row-langLev');
        //
        // llr.show();
        // llsel.find('option').not(':first').remove();
        // llsel.find('option').first().prop('selected', true);
        //
        // // noinspection EqualityComparisonWithCoercionJS
        // if (ts && ts.value == techStackConsts.php) llr.hide();
        // else if (ts) {
        //     for (let ll of ts.levels) {
        //         llsel.append(`<option value="${ll.value}">${ll.text}</option>`);
        //     }
        // }
        //
        // this.onLangLevelChanged();
    }

    onLangLevelChanged() {
        // let bt = jq('#scanCentralBuildTypeForm > select').val();
        // let ssv = jq('#buildToolVersionForm > input');
        //
        // if (bt === 'Python'){
        //     let ll = this.techStacks[techStackConsts.python].levels.find(e => e.value == jq('#languageLevelSelectList').val());
        //
        //     if (ll && ll.text) ssv.val(ll.text.replace(' (Django)', ''));
        //
        //     ssv.data('python', true);
        // } else if (ssv.data('python')) {
        //     ssv.removeData();
        //     ssv.val('');
        // }
    }

    // ToDo: Call this on auth change
    onReleaseIdChanged() {
        if (this.overrideServerSettings) {
            this.releaseId = numberOrNull(jq('#releaseSelectionValue').val());

            if (this.releaseId < 1) this.releaseId = null;

            this.loadEntitlementSettings();
        } else {
            this.hideMessages();
            jq(fodpOverrideRowsSelector).hide();
        }
    }

    onReleaseSelectionChanged() {
        let rs = jq('#releaseSelection').val();

        jq('.fodp-row-relid').hide();
        jq('.fodp-row-relid-ovr').hide();
        jq('.fodp-row-bsi').hide();
        jq('#releaseLookup').hide();

        if (rs === '1') {
            jq('.fodp-row-bsi').show();
        } else {
            jq('.fodp-row-relid').show();
            jq('#releaseLookup').show();
            this.onReleaseIdChanged();
        }
    }

    onOverrideReleaseSettingsChanged() {
        this.overrideServerSettings = jq('#overrideReleaseSettings').prop('checked');
        this.onReleaseIdChanged();
        this.onScanCentralChanged();
    }

    preinit() {
        jq('.fodp-field')
            .each((i, e) => {
                let jqe = jq(e);
                let tr = closestRow(jqe);

                tr.addClass('fodp-field-row');
                let vtr = getValidationErrRow(tr);

                if (vtr) vtr.addClass('fodp-field-row-verr');
            });
        jq('.fodp-row-hidden')
            .each((i, e) => {
                let jqe = jq(e);
                let tr = closestRow(jqe);

                tr.hide();
                let vtr = getValidationErrRow(tr);

                if (vtr) vtr.hide();
            });
        jq(fodpRowSelector).hide();

        // Move css classes prefixed with fodp-row to the row element
        jq('.fodp-field')
            .each((i, e) => {
                let jqe = jq(e);
                let classes = jqe.attr('class').split(' ');

                for (let c of classes) {
                    if (c.startsWith('fodp-row-')) {
                        jqe.removeClass(c);
                        let tr = jqe.closest('.fodp-field-row');

                        tr.addClass(c)

                        // Copy Scan Central and BSI css classes to validation-error-area and help-area rows
                        if (c.startsWith('fodp-row-sc') ||
                            c === 'fodp-row-nonsc' ||
                            c === 'fodp-row-bsi' ||
                            c === 'fodp-row-langLev' ||
                            c === 'fodp-row-all' ||
                            c === 'fodp-row-relid') {
                            let vtr = getValidationErrRow(tr);
                            let htr = getHelpRow(tr);

                            if (vtr) vtr.addClass(c);
                            if (htr) htr.addClass(c);
                        }
                    }
                }
            });
        jq('.fodp-row-all').show();

        //
        // Hacks to fix alignment issues
        //

        // In the previous jenkins versions that use table layout,
        // this sets the field label min-width so selecting
        // Override Release Settings doesn't look janky because of Sonatype
        let jqe = jq('#scanCentralBuildTypeForm').parent();

        if (jqe.prop('tagName') === 'TD') jqe.prev('td').css('min-width', '300px');
        // &nbsp; breaks Jelly compilation
        jq('#releaseLookup').parent().append('&nbsp;');

        this.init();
    }

    async init() {
        // try {
        //     this.techStacks = await this.api.getTechStacks(getAuthInfo());
        //     for (let k of Object.keys(this.techStacks)) {
        //         let ts = this.techStacks[k];
        //
        //         // noinspection EqualityComparisonWithCoercionJS
        //         if (ts.value == techStackConsts.dotNetCore) {
        //             for (let ll of ts.levels) {
        //                 ll.text = ll.text.replace(' (.NET Core)', '');
        //             }
        //         }
        //
        //         this.techStacksSorted.push(ts);
        //     }
        //
        //     this.techStacksSorted = this.techStacksSorted.sort((a, b) => a.text.toLowerCase() < b.text.toLowerCase() ? -1 : 1);
        // } catch (err) {
        //     if (!this.unsubInit) {
        //         this.unsubInit = () => this.init();
        //         subscribeToEvent('authInfoChanged', this.unsubInit);
        //     }
        //     return;
        // }
        //
        // this.hideMessages();
        // this.showMessage('Set a release id');
        // ToDo: move this to wherever auth change is handled
        // if (this.unsubInit) unsubscribeEvent('authInfoChanged', this.unsubInit);

        jq('#releaseSelection')
            .change(_ => this.onReleaseSelectionChanged());

        jq('#releaseSelectionValue')
            .change(_ => this.onReleaseIdChanged());

        jq('#overrideReleaseSettings')
            .change(_ => this.onOverrideReleaseSettingsChanged());

        jq('#scanCentralBuildTypeForm > select')
            .change(_ => this.onScanCentralChanged());

        jq('#technologyStackSelectList')
            .change(_ => this.onTechStackChanged());

        jq('#ddAssessmentType')
            .change(_ => this.onAssessmentChanged());

        jq('#entitlementSelectList')
            .change(_ => this.onEntitlementChanged());

        jq('#languageLevelSelectList')
            .change(_ => this.onLangLevelChanged());

        this.populateTechStackDropdown();
        this.onReleaseSelectionChanged();
        this.onScanCentralChanged();

        this.uiLoaded = true;
    }

}


const pipelineGenerator = new PipelineGenerator();

spinAndWait(() => jq('#sonatypeEnabledForm').val() !== undefined)
    .then(pipelineGenerator.preinit.bind(pipelineGenerator));