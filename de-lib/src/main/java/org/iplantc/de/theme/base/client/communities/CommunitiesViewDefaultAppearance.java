package org.iplantc.de.theme.base.client.communities;

import org.iplantc.de.communities.client.CommunitiesView;

import com.google.gwt.core.client.GWT;

public class CommunitiesViewDefaultAppearance implements CommunitiesView.Appearance {

    private CommunitiesDisplayStrings displayStrings;

    public CommunitiesViewDefaultAppearance() {
        this(GWT.create(CommunitiesDisplayStrings.class));
    }

    public CommunitiesViewDefaultAppearance(CommunitiesDisplayStrings displayStrings) {
        this.displayStrings = displayStrings;
    }

    @Override
    public int windowMinWidth() {
        return 200;
    }

    @Override
    public int windowMinHeight() {
        return 300;
    }

    @Override
    public String windowHeading() {
        return displayStrings.windowHeading();
    }

    @Override
    public String communitiesHelp() {
        return displayStrings.communitiesHelp();
    }

    @Override
    public String windowWidth() {
        return "600px";
    }

    @Override
    public String windowHeight() {
        return "400px";
    }
}
