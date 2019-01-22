package org.iplantc.de.theme.base.client.communities;

import org.iplantc.de.communities.client.CommunitiesView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public class CommunitiesViewDefaultAppearance implements CommunitiesView.Appearance {

    public interface Resources extends ClientBundle {

        interface Style extends CssResource {
            String whiteBackground();
        }

        @Source("CommunitiesStyle.gss")
        Style style();
    }

    private CommunitiesDisplayStrings displayStrings;
    private final Resources.Style style;

    public CommunitiesViewDefaultAppearance() {
        this(GWT.create(CommunitiesDisplayStrings.class),
             GWT.create(Resources.class));
    }

    public CommunitiesViewDefaultAppearance(CommunitiesDisplayStrings displayStrings,
                                            Resources resources) {
        this.displayStrings = displayStrings;
        this.style = resources.style();
        style.ensureInjected();
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

    @Override
    public String windowBackground() {
        return style.whiteBackground();
    }
}
