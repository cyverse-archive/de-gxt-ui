package org.iplantc.de.theme.base.client.teams;

import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;

/**
 * The default appearance that will be used for the Teams view
 * @author aramsey
 */
public class TeamsViewDefaultAppearance implements TeamsView.TeamsViewAppearance {

    private IplantDisplayStrings iplantDisplayStrings;
    private IplantResources iplantResources;
    private TeamsDisplayStrings displayStrings;

    public TeamsViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<TeamsDisplayStrings> create(TeamsDisplayStrings.class));
    }

    public TeamsViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                      IplantResources iplantResources,
                                      TeamsDisplayStrings displayStrings) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
    }
}
