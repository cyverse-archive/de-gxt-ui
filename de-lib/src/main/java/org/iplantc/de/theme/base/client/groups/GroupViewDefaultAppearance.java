package org.iplantc.de.theme.base.client.groups;

import org.iplantc.de.groups.client.GroupView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author aramsey
 */
public class GroupViewDefaultAppearance implements GroupView.GroupViewAppearance {

    private IplantDisplayStrings iplantDisplayStrings;
    private IplantResources iplantResources;
    private GroupDisplayStrings displayStrings;

    public GroupViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class),
             GWT.<GroupDisplayStrings>create(GroupDisplayStrings.class));
    }

    public GroupViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                      IplantResources iplantResources,
                                      GroupDisplayStrings displayStrings) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
    }

    @Override
    public String addGroup() {
        return iplantDisplayStrings.add();
    }

    @Override
    public ImageResource addIcon() {
        return iplantResources.add();
    }

    @Override
    public String deleteGroup() {
        return iplantDisplayStrings.delete();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.deleteIcon();
    }

    @Override
    public int nameColumnWidth() {
        return 300;
    }

    @Override
    public String nameColumnLabel() {
        return iplantDisplayStrings.name();
    }

    @Override
    public int descriptionColumnWidth() {
        return 300;
    }

    @Override
    public String descriptionColumnLabel() {
        return iplantDisplayStrings.description();
    }

    @Override
    public String groupDialogHeader() {
        return displayStrings.groupDialogHeader();
    }

    @Override
    public String groupDialogWidth() {
        return "500";
    }

    @Override
    public String groupDialogHeight() {
        return "400";
    }
}
