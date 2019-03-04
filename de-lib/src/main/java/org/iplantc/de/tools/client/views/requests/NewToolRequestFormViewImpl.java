package org.iplantc.de.tools.client.views.requests;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.tools.client.ReactTools;
import org.iplantc.de.tools.client.presenter.ManageToolsViewPresenter;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * A form to submit request to install new tools in condor
 *
 * @author sriram
 */
public final class NewToolRequestFormViewImpl implements NewToolRequestFormView {


    ManageToolsView.Presenter presenter;

    HTMLPanel panel;
    private ReactTools.ToolRequestProps props;

    @Inject
    NewToolRequestFormViewImpl() {
        panel = new HTMLPanel("<div></div>");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return panel;
    }


    @Override
    public void load(ManageToolsView.Presenter presenter) {
        props = new ReactTools.ToolRequestProps();
        props.dialogOpen = true;
        Scheduler.get().scheduleFinally(() -> {
            props.presenter = presenter;
            CyVerseReactComponents.render(ReactTools.NewToolRequestForm, props, panel.getElement());
        });

    }

    @Override
    public void onClose() {
        props.dialogOpen = false;
        CyVerseReactComponents.render(ReactTools.NewToolRequestForm, props, panel.getElement());
    }
}
