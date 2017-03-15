package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.ArgumentGroup;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

/**
 * @author aramsey
 */
public class AppIntegrationGroupDND extends DragSource {

    private AppIntegrationPalette view;
    private Widget widget;
    private AppTemplateAutoBeanFactory factory;
    DragSource dragSource;

    public AppIntegrationGroupDND(AppIntegrationPalette view,
                                  Widget widget) {
        this((AppTemplateAutoBeanFactory)GWT.create(AppTemplateAutoBeanFactory.class),
             view,
             widget);
    }

    public AppIntegrationGroupDND(AppTemplateAutoBeanFactory factory, AppIntegrationPalette view, Widget widget) {
        super(widget);
        this.view = view;
        this.widget = widget;
        this.factory = factory;

        addDragStartHandler(new DndDragStartEvent.DndDragStartHandler() {

            @Override
            public void onDragStart(DndDragStartEvent event) {
                if (view.getOnlyLabelEditMode()) {
                    event.getStatusProxy().setStatus(false);
                    event.getStatusProxy().update((SafeHtml)() -> "Groups cannot be added to a published app.");
                    return;
                }

                event.getStatusProxy().setStatus(true);
                event.getStatusProxy().update((SafeHtml)() -> widget.getElement().getString());
                event.setData(createNewArgumentGroup());

            }

            private ArgumentGroup createNewArgumentGroup() {
                AutoBean<ArgumentGroup> argGrpAb = factory.argumentGroup();
                // JDS Annotate as a newly created autobean
                argGrpAb.setTag(ArgumentGroup.IS_NEW, "--");

                ArgumentGroup ag = argGrpAb.as();
                ag.setArguments(Lists.<Argument> newArrayList());
                ag.setLabel("DEFAULT");
                ag.setName("DEFAULT");
                return ag;
            }
        });
    }
}
