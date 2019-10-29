package org.iplantc.de.apps.client.views.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent.AppUpdatedEventHandler;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author jstroot, sriram
 */
public class AppDetailsViewImpl implements AppDetailsView,
                                           AppUpdatedEventHandler {

    HTMLPanel panel;
    @Inject
    EventBus eventBus;
    @Inject
    UserInfo userInfo;

    private ReactAppDetails.AppInfoProps props;

    @Inject
    AppDetailsViewImpl(final EventBus eventBus) {
        this.eventBus = eventBus;
        panel = new HTMLPanel("<div></div>");
        eventBus.addHandler(AppUpdatedEvent.TYPE, this);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }


    @Override
    public void load(Presenter presenter,
                     final Splittable app,
                     final Splittable appDetails,
                     boolean showQuickLaunchFirst,
                     final String searchText) {
        props = new ReactAppDetails.AppInfoProps();
        props.app = app;
        props.appDetails = appDetails;
        props.presenter = presenter;
        props.dialogOpen = true;
        props.docEditable = userInfo.getEmail().equals(app.get("integrator_email").asString());
        props.baseDebugId = AppsModule.Ids.DETAILS_VIEW;
        props.searchText = searchText != null ? searchText : "";
        props.userName = userInfo.getFullUsername();
        props.showQuickLaunchFirst = showQuickLaunchFirst;

        CyVerseReactComponents.render(ReactAppDetails.AppInfoDialog, props, panel.getElement());
    }

    @Override
    public void onClose() {
        props.dialogOpen = false;
        CyVerseReactComponents.render(ReactAppDetails.AppInfoDialog, props, panel.getElement());
    }


    @Override
    public void onAppUpdated(AppUpdatedEvent event) {
        props.app = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(event.getApp()));
        CyVerseReactComponents.render(ReactAppDetails.AppInfoDialog, props, panel.getElement());
    }
}
