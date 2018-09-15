package org.iplantc.de.diskResource.client.views.metadata;

import org.iplantc.de.client.models.diskResources.DiskResourceMetadataList;
import org.iplantc.de.client.models.diskResources.MetadataTemplate;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.diskResource.client.MetadataView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

/**
 * @author psarando
 */
public class MetadataTemplateView implements IsWidget {

    private final HTMLPanel widget;
    private final ReactMetadataViews.MetadataTemplateViewProps props;

    MetadataView.Appearance appearance;

    @Inject
    public MetadataTemplateView(MetadataView.Appearance appearance) {
        widget = new HTMLPanel("<div></div>");
        props = new ReactMetadataViews.MetadataTemplateViewProps();

        this.appearance = appearance;
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void openDialog(MetadataView.Presenter presenter,
                           MetadataTemplate template,
                           DiskResourceMetadataList metadata,
                           boolean writable) {
        Scheduler.get().scheduleFinally(() -> {
            props.open = true;
            props.writable = writable;
            props.presenter = presenter;
            props.template = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(template));
            props.metadata = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(metadata));

            CyVerseReactComponents.render(ReactMetadataViews.MetadataTemplateView, props, widget.getElement());
        });
    }

    public void closeDialog() {
        props.open = false;
        CyVerseReactComponents.render(ReactMetadataViews.MetadataTemplateView, props, widget.getElement());
    }
}
