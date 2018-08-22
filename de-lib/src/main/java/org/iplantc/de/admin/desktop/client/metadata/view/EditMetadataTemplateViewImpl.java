package org.iplantc.de.admin.desktop.client.metadata.view;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

public class EditMetadataTemplateViewImpl implements EditMetadataTemplateView {
    private final HTMLPanel container;
    private final ReactMetadataAdminViews.EditMetadataTemplateProps props;

    @Inject
    EditMetadataTemplateViewImpl() {
        container = new HTMLPanel("<div></div>");
        props = new ReactMetadataAdminViews.EditMetadataTemplateProps();
    }

    @Override
    public void edit(EditMetadataTemplateView.Presenter presenter, Splittable metadataTemplate) {
        Scheduler.get().scheduleFinally(() -> {
            props.presenter = presenter;
            props.initialValues = metadataTemplate;
            props.open = true;

            CyVerseReactComponents.render(ReactMetadataAdminViews.EditMetadataTemplate, props, container.getElement());
        });
    }

    @Override
    public void closeDialog() {
        props.open = false;

        CyVerseReactComponents.render(ReactMetadataAdminViews.EditMetadataTemplate, props, container.getElement());
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
