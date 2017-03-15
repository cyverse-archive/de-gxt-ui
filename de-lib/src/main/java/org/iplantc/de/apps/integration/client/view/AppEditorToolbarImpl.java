package org.iplantc.de.apps.integration.client.view;

import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids;

import org.iplantc.de.apps.integration.client.events.ArgumentOrderSelected;
import org.iplantc.de.apps.integration.client.events.PreviewAppSelected;
import org.iplantc.de.apps.integration.client.events.PreviewJsonSelected;
import org.iplantc.de.apps.integration.client.events.SaveAppSelected;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class AppEditorToolbarImpl extends Composite implements AppEditorToolbar {

    @UiTemplate("AppIntegrationToolbar.ui.xml")
    interface AppIntegrationToolBarUiBinder extends UiBinder<Widget, AppEditorToolbarImpl> { }
    @UiField
    TextButton argumentOrderButton;
    @UiField
    MenuItem previewUiMenuItem, previewJsonMenuItem;
    @UiField
    TextButton saveButton;
    @UiField
    TextButton previewBtn;
    private static AppIntegrationToolBarUiBinder BINDER = GWT.create(AppIntegrationToolBarUiBinder.class);
    private AppEditorToolbar.Presenter presenter;

    public AppEditorToolbarImpl() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public void setPresenter(AppEditorToolbar.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        argumentOrderButton.ensureDebugId(baseID + Ids.ARG_ORDER);
        previewBtn.ensureDebugId(baseID + Ids.PREVIEW);
        previewUiMenuItem.ensureDebugId(baseID + Ids.PREVIEW + Ids.PREVIEW_UI);
        previewJsonMenuItem.ensureDebugId(baseID + Ids.PREVIEW + Ids.PREVIEW_JSON);
        saveButton.ensureDebugId(baseID + Ids.SAVE);
    }

    @UiHandler("argumentOrderButton")
    void onArgumentOrderButtonClicked(@SuppressWarnings("unused") SelectEvent event) {
        fireEvent(new ArgumentOrderSelected());
    }

    @UiHandler("previewJsonMenuItem")
    void onPreviewJsonClicked(@SuppressWarnings("unused") SelectionEvent<Item> event) {
        fireEvent(new PreviewJsonSelected());
    }

    @UiHandler("previewUiMenuItem")
    void onPreviewUiClicked(@SuppressWarnings("unused") SelectionEvent<Item> event) {
        fireEvent(new PreviewAppSelected());
    }

    @UiHandler("saveButton")
    void onSaveButtonClicked(@SuppressWarnings("unused") SelectEvent event) {
        fireEvent(new SaveAppSelected());
    }

    @Override
    public HandlerRegistration addArgumentOrderSelectedHandler(ArgumentOrderSelected.ArgumentOrderSelectedHandler handler) {
        return addHandler(handler, ArgumentOrderSelected.TYPE);
    }

    @Override
    public HandlerRegistration addPreviewJsonSelectedHandler(PreviewJsonSelected.PreviewJsonSelectedHandler handler) {
        return addHandler(handler, PreviewJsonSelected.TYPE);
    }

    @Override
    public HandlerRegistration addPreviewAppSelectedHandler(PreviewAppSelected.PreviewAppSelectedHandler handler) {
        return addHandler(handler, PreviewAppSelected.TYPE);
    }

    @Override
    public HandlerRegistration addSaveAppSelectedHandler(SaveAppSelected.SaveAppSelectedHandler handler) {
        return addHandler(handler, SaveAppSelected.TYPE);
    }
}
