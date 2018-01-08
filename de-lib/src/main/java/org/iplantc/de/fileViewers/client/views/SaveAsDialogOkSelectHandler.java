package org.iplantc.de.fileViewers.client.views;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.fileViewers.client.callbacks.FileSaveCallback;

import com.google.gwt.event.shared.HasHandlers;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author jstroot
 */
public final class SaveAsDialogOkSelectHandler implements SelectEvent.SelectHandler {
    private final IsMaskable maskable;
    private final HasHandlers hasHandlers;
    private final SaveAsDialog saveAsDialog;
    private final String savingMaskText;
    private final String editorContent;
    private final FileEditorServiceFacade fileEditorService;
    private IsHideable hideable;
    private UserSessionServiceFacade userSessionService;

    public SaveAsDialogOkSelectHandler(final UserSessionServiceFacade userSessionService,
                                       final IsMaskable maskable,
                                       final HasHandlers hasHandlers,
                                       final SaveAsDialog saveAsDialog,
                                       final String savingMaskText,
                                       final String editorContent,
                                       final FileEditorServiceFacade fileEditorService,
                                       IsHideable hideable) {
        this.userSessionService = userSessionService;
        this.maskable = maskable;
        this.hasHandlers = hasHandlers;
        this.saveAsDialog = saveAsDialog;
        this.savingMaskText = savingMaskText;
        this.editorContent = editorContent;
        this.fileEditorService = fileEditorService;
        this.hideable = hideable;
    }

    @Override
    public void onSelect(SelectEvent event) {
        if (!saveAsDialog.isValid()) {
            return;
        }

        maskable.mask(savingMaskText);
        String destination = saveAsDialog.getSelectedFolder().getPath() + "/"
                                 + saveAsDialog.getFileName();
        fileEditorService.uploadTextAsFile(destination,
                                           editorContent,
                                           true,
                                           new FileSaveCallback(userSessionService,
                                                                destination,
                                                                true,
                                                                maskable,
                                                                hasHandlers,
                                                                hideable));
        saveAsDialog.hide();
    }
}
