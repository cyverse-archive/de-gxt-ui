package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * A dialog that simply shows the user a public link to a disk resource for the purpose of copying the URL
 */
public class DataLinkDialog extends IPlantDialog {

    private TextField dataLinkUrl;
    private DataLinkView.Appearance appearance;

    @Inject
    public DataLinkDialog(DataLinkView.Appearance appearance) {
        this.appearance = appearance;

        setHeading(appearance.dataLinkTitle());
        setHideOnButtonClick(true);
        setResizable(false);
        setPredefinedButtons(Dialog.PredefinedButton.OK);
        setSize(appearance.copyDataLinkDlgWidth(), appearance.copyDataLinkDlgHeight());
        addLinkTextField();

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        setWidget(container);
        container.add(dataLinkUrl);
        container.add(new Label(appearance.copyPasteInstructions()));
    }

    void addLinkTextField() {
        dataLinkUrl = new TextField();
        dataLinkUrl.setWidth(appearance.copyDataLinkDlgTextBoxWidth());
        dataLinkUrl.setReadOnly(true);
    }

    public void show(String url) {
        dataLinkUrl.setValue(url);
        setFocusWidget(dataLinkUrl);
        dataLinkUrl.selectAll();

        ensureDebugId(DiskResourceModule.Ids.DATA_LINK_DLG);

        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported, use show(String). ");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);
        dataLinkUrl.ensureDebugId(baseID + DiskResourceModule.Ids.DATA_LINK_URL);
    }
}
