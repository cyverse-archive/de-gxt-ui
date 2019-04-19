package org.iplantc.de.apps.client.views.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent.AppUpdatedEventHandler;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

/**
 * @author jstroot, sriram
 */
public class AppDetailsViewImpl implements AppDetailsView,
                                           AppUpdatedEventHandler {


    private final App app;
    private final AppDetailsAppearance appearance;
    private String searchRegexPattern;
    HTMLPanel panel;

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
    }

    @Override
    public Widget asWidget() {
        return panel;
    }


    @Override
    public void load(Presenter presenter) {
        props = new ReactAppDetails.AppInfoProps();
        props.app = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(app));
        props.presenter = presenter;
        props.dialogOpen = true;
        props.docEditable = userInfo.getEmail().equals(app.getIntegratorEmail());
        props.baseDebugId = AppsModule.Ids.DETAILS_VIEW;
        props.searchRegexPattern = searchRegexPattern != null ? searchRegexPattern : "";
        props.userName = userInfo.getFullUsername();

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
