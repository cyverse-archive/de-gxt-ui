package org.iplantc.de.diskResource.client.views.metadata;

import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.diskResource.client.BulkMetadataView;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.widgets.FileSelectorField;

import com.google.common.base.Strings;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class BulkMetadataViewImpl extends Composite implements BulkMetadataView {

    interface BulkMetadataUiBinder extends UiBinder<Widget, BulkMetadataViewImpl> {}
    private static final BulkMetadataUiBinder BINDER = GWT.create(BulkMetadataUiBinder.class);

    private final BULK_MODE mode;
    @UiField HorizontalLayoutContainer con;
    @UiField FormPanel form0;
    @UiField FileUpload fuf0;
    @UiField TextButton btn0;
    @UiField Status status0;
    @UiField(provided = true) FileSelectorField fileSelector;
    @UiField(provided = true) BulkMetadataView.BulkMetadataViewAppearance appearance;
    @UiField HTML upFileLbl, selLbl;
    @Inject DiskResourceNameValidator diskResourceNameValidator;

    @Inject
    public BulkMetadataViewImpl(BulkMetadataView.BulkMetadataViewAppearance appearance,
                                DiskResourceSelectorFieldFactory drSelectorFieldFactory,
                                @Assisted("mode") BulkMetadataView.BULK_MODE mode) {
        this.appearance = appearance;
        this.mode = mode;
        this.fileSelector = drSelectorFieldFactory.defaultFileSelector();
        fileSelector.setValidatePermissions(true);
        fileSelector.addValidator(new DiskResourceNameValidator());

        initWidget(BINDER.createAndBindUi(this));
        selLbl.setHTML(appearance.selectMetadataFile());

        if (mode.equals(BULK_MODE.SELECT)) {
            selectFileOption();
        } else {
            uploadFileOption();
        }
    }

    private void uploadFileOption() {
        fileSelector.hide();
        selLbl.setVisible(false);
        con.show();
        upFileLbl.setVisible(true);
    }

    private void selectFileOption() {
        fileSelector.show();
        selLbl.setVisible(true);
        con.hide();
        upFileLbl.setVisible(false);
    }

    @Override
    public boolean isValid() {
        if (mode.equals(BULK_MODE.SELECT)) {
            return fileSelector.getValue() != null;
        } else {
            return Strings.isNullOrEmpty(diskResourceNameValidator.validateAndReturnError(fuf0.getFilename()));
        }
    }

    @Override
    public String getSelectedPath() {
        return fileSelector.getValue().getPath();
    }

    @UiFactory
    FormPanel createFormPanel() {
        FormPanel form = new FormPanel();
        form.setMethod(FormPanel.METHOD_POST);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setSize(appearance.formWidth(), appearance.formHeight());
        return form;
    }

    @UiFactory
    Status createFormStatus() {
        Status status = new Status();
        status.setWidth(15);
        return status;
    }

    @UiHandler("btn0")
    void onResetClicked(SelectEvent event) {
        form0.reset();
    }

    @UiHandler("fuf0")
    void onFieldChanged(ChangeEvent event) {

    }
}
