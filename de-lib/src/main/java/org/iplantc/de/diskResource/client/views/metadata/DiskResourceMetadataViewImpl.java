package org.iplantc.de.diskResource.client.views.metadata;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceMetadataList;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.diskResource.client.MetadataView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author psarando
 */
public class DiskResourceMetadataViewImpl implements MetadataView {

    private final HTMLPanel container;
    private final ReactMetadataViews.EditMetadataProps editMetadataProps;
    private DiskResource selectedResource;

    @Inject
    public DiskResourceMetadataViewImpl() {
        container = new HTMLPanel("<div />");
        editMetadataProps = new ReactMetadataViews.EditMetadataProps();
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void mask() {
        editMetadataProps.loading = true;
        renderDialog();
    }

    @Override
    public void unmask() {
        editMetadataProps.loading = false;
        renderDialog();
    }

    private void renderDialog() {
        Scheduler.get().scheduleFinally(() -> {
            CyVerseReactComponents.render(ReactMetadataViews.EditMetadata, editMetadataProps, container.getElement());
        });
    }


    @Override
    public void loadMetadata(final DiskResourceMetadataList metadata) {
        // Also reset the targetResource here to force the dialog to reset the form,
        // otherwise it will mark the form as "dirty" since the metadata is being updated.
        editMetadataProps.targetResource = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(selectedResource));
        editMetadataProps.metadata = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(metadata));
    }

    @Override
    public void init(MetadataView.Presenter presenter, boolean editable, final DiskResource selectedResource) {
        this.selectedResource = selectedResource;

        editMetadataProps.open = true;
        editMetadataProps.presenter = presenter;
        editMetadataProps.editable = editable;
        editMetadataProps.targetResource = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(selectedResource));
    }

    @Override
    public void showMetadataDialog() {
        editMetadataProps.open = true;
        renderDialog();
    }

    @Override
    public void closeMetadataDialog() {
        editMetadataProps.open = false;
        renderDialog();
    }

    @Override
    public void updateMetadataFromTemplateView(Splittable metadata) {
        editMetadataProps.metadata = metadata;
    }
}
