package org.iplantc.de.communities.client.views;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.communities.client.ManageCommunitiesView;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ManageCommunitiesViewImpl implements ManageCommunitiesView {

    HTMLPanel panel;

    @Inject
    public ManageCommunitiesViewImpl() {
        panel = new HTMLPanel("<div></div>");
    }


    @Override
    public void show(ReactCommunities.CommunitiesProps props) {
        CyVerseReactComponents.render(ReactCommunities.CommunitiesView,
                                      props,
                                      panel.getElement());
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
}
