package org.iplantc.de.theme.base.client.apps.list;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author aramsey
 */
public interface TileListResources extends ClientBundle {

    public interface AppsTileStyle extends CssResource {

        String tileCell();

        String tileCellSelect();

        String infoMod();

        String commentMod();

        String nameMod();

        String favoriteMod();

        String integratorMod();

        String ratingMod();

        String statusMod();
    }

    @Source("org/iplantc/de/theme/base/client/apps/cells/AppsTile.gss")
    AppsTileStyle style();
}

