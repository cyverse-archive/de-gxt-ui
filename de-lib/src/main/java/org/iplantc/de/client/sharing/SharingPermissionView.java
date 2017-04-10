package org.iplantc.de.client.sharing;

import org.iplantc.de.client.models.sharing.Sharing;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.core.shared.FastMap;

import java.util.List;

/**
 * @author aramsey
 */
public interface SharingPermissionView extends IsWidget {

    void showPermissionColumn();

    void hidePermissionColumn();

    void loadSharingData(FastMap<List<Sharing>> sharingMap);

    void setExplainPanelVisibility(boolean visible);

    FastMap<List<Sharing>> getSharingMap();

    FastMap<List<Sharing>> getUnshareList();

    void mask();

    void unmask();

}
