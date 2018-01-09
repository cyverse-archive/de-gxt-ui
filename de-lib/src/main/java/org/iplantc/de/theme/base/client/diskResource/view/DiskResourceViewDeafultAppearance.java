package org.iplantc.de.theme.base.client.diskResource.view;

import org.iplantc.de.diskResource.client.DiskResourceView;

/**
 * Created by sriram on 1/8/18.
 */
public class DiskResourceViewDeafultAppearance implements DiskResourceView.DiskResourceViewAppearance {
    @Override
    public String windowHeight() {
        return "480";
    }

    @Override
    public String windowWidth() {
        return "900";
    }
}
