package org.iplantc.de.theme.base.client.admin.communities;

import com.google.gwt.i18n.client.Messages;

import java.util.List;

/**
 * @author aramsey
 */
public interface CommunityDisplayStrings extends Messages{

    String addCommunity();

    String deleteCommunity();

    String editCommunity();

    String categorize();

    String communityPanelHeader();

    String hierarchyPreviewHeader();

    String communityTreePanel();

    String hierarchyTreePanel();

    String externalAppDND(String appLabels);

    String appAddedToCommunity(String appName, String communityName);
}
