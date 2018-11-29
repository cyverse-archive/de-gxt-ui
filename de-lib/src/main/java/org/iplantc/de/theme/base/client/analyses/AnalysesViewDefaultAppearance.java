package org.iplantc.de.theme.base.client.analyses;

import org.iplantc.de.analysis.client.AnalysesView;

/**
 * @author jstroot
 */
public class AnalysesViewDefaultAppearance implements AnalysesView.Appearance {



    public AnalysesViewDefaultAppearance() {
    }

    @Override
    public String windowWidth() {
        return "670";
    }

    @Override
    public String windowHeight() {
        return "375";
    }

    @Override
    public int windowMinWidth() {
        return 590;
    }

    @Override
    public int windowMinHeight() {
        return 300;
    }
}
