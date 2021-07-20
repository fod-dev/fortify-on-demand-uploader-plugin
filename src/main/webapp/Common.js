jq = jQuery;

function dispatchEvent(type, payload) {
    document.dispatchEvent(new CustomEvent(type, { detail: payload }));
}

function subscribeToEvent(type, cb) {
    document.removeEventListener(type, cb);
    document.addEventListener(type, cb);
}

function debounce(func, wait, immediate) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
};

function spinAndWait(fn) {
    return new Promise((res, rej) => {
        let elementsLoaded;

        elementsLoaded = () => {
            if(fn()) res();
            else setTimeout(elementsLoaded, 50);
        };

        elementsLoaded();
    });
}

function createDialog(dialog) {
    spinAndWait(() => jq('#' + dialog._formId).html())
        .then(() => dialog.init());
}

class Dialog {

    constructor(dialogId, formId) {
        this._dialogId = dialogId;
        this._formId = formId;
        this._bindToWindow();
    }

    _bindToWindow() {
        window[this._dialogId] = window[this._dialogId] || {'dialog': null, 'body': null};
        window[this._dialogId].init = function () {
            if (!(window[this._dialogId].dialog)) {
                var div = document.createElement("DIV");
                document.body.appendChild(div);
                div.innerHTML = "<div id='" + this._dialogId + "'><div class='bd'></div></div>";
                window[this._dialogId].body = $(this._dialogId);
                window[this._dialogId].body.innerHTML = $(this._formId).innerHTML;
                jq('#' + this._dialogId).prepend('<div class="mask" id="modal_mask" style="z-index: 1000; height: 100%; width: 100%; display: none;"> </div>');
                window[this._dialogId].dialog = new YAHOO.widget.Panel(window[this._dialogId].body, {
                    fixedcenter: true,
                    close: true,
                    draggable: true,
                    zindex: 1000,
                    modal: true,
                    visible: false,
                    keylisteners: [
                        new YAHOO.util.KeyListener(document, {keys:27}, {
                            fn:(function() {window[this._dialogId].dialog.hide();}),
                            scope:document,
                            correctScope:false
                        })
                    ]
                });
                window[this._dialogId].dialog.render();
            }
        };
    }

    jqDialog(selector) {
        return jq('#' + this._dialogId + ' ' + selector);
    }

    init() {
        jq('#' + this._formId).hide();
        if (this.onInit) {
            this.onInit();
        }
    }

    spawnDialog(data) {
        window[this._dialogId].init.bind(this)();
        window[this._dialogId].dialog.show();

        this.jqDialog('#modal_mask').hide();

        if (this.onDialogSpawn) {
            this.onDialogSpawn(data);
        }
    }

    closeDialog() {
        window[this._dialogId].dialog.close.click();

        if (this.onDialogClose) {
            this.onDialogClose();
        }
    }

    putMask() {
        this.jqDialog('#modal_mask').show();
    }

    removeMask() {
        this.jqDialog('#modal_mask').hide();
    }
}