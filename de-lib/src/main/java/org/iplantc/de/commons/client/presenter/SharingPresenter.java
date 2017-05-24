package org.iplantc.de.commons.client.presenter;

import org.iplantc.de.client.models.sharing.PermissionValue;

public interface SharingPresenter extends org.iplantc.de.commons.client.presenter.Presenter {

    interface Appearance {

        String sharingCompleteMsg();
    }

    void loadResources();

    void loadPermissions();

    PermissionValue getDefaultPermissions();

    void processRequest();

    void setViewDebugId(String debugId);
}
