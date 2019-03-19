package org.iplantc.de.apps.client.views.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent.AppUpdatedEventHandler;
import org.iplantc.de.apps.client.events.selection.SaveMarkdownSelected;
import org.iplantc.de.apps.client.views.details.doc.AppDocMarkdownDialog;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.widget.core.client.Composite;

/**
 * @author jstroot
 */
public class AppDetailsViewImpl extends Composite implements
                                                 AppDetailsView,
                                                 SaveMarkdownSelected.SaveMarkdownSelectedHandler,
                                                 AppUpdatedEventHandler {


    private final App app;
    private final AppDetailsAppearance appearance;
    private String searchRegexPattern;
    HTMLPanel panel;

    @Inject
    AsyncProviderWrapper<AppDocMarkdownDialog> markdownDialogProvider;
    @Inject
    EventBus eventBus;
    @Inject
    UserInfo userInfo;

    private ReactAppDetails.AppInfoProps props;

    @Inject
    AppDetailsViewImpl(final AppDetailsView.AppDetailsAppearance appearance,
                       final EventBus eventBus,
                       @Assisted final App app,
                       @Assisted final String searchRegexPattern) {
        this.appearance = appearance;
        this.eventBus = eventBus;
        this.app = app;
        this.searchRegexPattern = searchRegexPattern;
        panel = new HTMLPanel("<div></div>");
        eventBus.addHandler(AppUpdatedEvent.TYPE, this);

        /*
         * Debug id has to be set before binding the editor to ensure that UI elements get the debug id
         * before they are rendered/created.
         */
        //ensureDebugId(AppsModule.Ids.DETAILS_VIEW);

        // Add self so that rating cell events will fire
        // ratings.setHasHandlers(this);

/*        if (app.isPublic() || app.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
            url.setText(appearance.appUrl());
            url.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ClipboardCopyEnabledDialog dialog = new ClipboardCopyEnabledDialog(false, false);
                    dialog.setHeading(appearance.copyAppUrl());
                    dialog.setCopyText(GWT.getHostPageBaseURL() + "?type="
                                       + DesktopPresenterImpl.TypeQueryValues.APPS + "&app-id="
                                       + app.getId() + "&" + DesktopPresenterImpl.QueryStrings.SYSTEM_ID
                                       + "=" + app.getSystemId());
                    dialog.setTextBoxId(AppsModule.Ids.APP_URL_TEXT);
                    dialog.show();
                }
            });
        } else {
            helpLink.setVisible(false);
        }*/

    }

    @Override
    public Widget asWidget() {
        return panel;
    }


    @Override
    public void load(Presenter presenter) {
        GWT.log("App==>" + AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(app)).getPayload());
        props = new ReactAppDetails.AppInfoProps();
        props.app = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(app));
        props.presenter = presenter;
        props.dialogOpen = true;
        CyVerseReactComponents.render(ReactAppDetails.AppInfoDialog, props, panel.getElement());
    }

    @Override
    public void onClose() {
        props.dialogOpen = false;
        CyVerseReactComponents.render(ReactAppDetails.AppInfoDialog, props, panel.getElement());
    }


    @Override
    public void onSaveMarkdownSelected(SaveMarkdownSelected event) {
        // Forward event
        fireEvent(event);
    }

    @Override
    public void showDoc(AppDoc appDoc) {
        markdownDialogProvider.get(new AsyncCallback<AppDocMarkdownDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(AppDocMarkdownDialog result) {
                result.show(app, appDoc, userInfo);
                result.addSaveMarkdownSelectedHandler(props.presenter);
                result.addHideHandler(event -> {
                    props.dialogOpen = true;
                    CyVerseReactComponents.render(ReactAppDetails.AppInfoDialog, props, panel.getElement());
                });
            }
        });
    }

    @Override
    public void onDetailsCategoryClicked(String modelKey) {

    }


    @Override
    public void onAppUpdated(AppUpdatedEvent event) {
        props.app = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(event.getApp()));
        CyVerseReactComponents.render(ReactAppDetails.AppInfoDialog, props, panel.getElement());
    }


    @Override
    public HandlerRegistration addSaveMarkdownSelectedHandler(SaveMarkdownSelected.SaveMarkdownSelectedHandler handler) {
        return null;
    }
}
