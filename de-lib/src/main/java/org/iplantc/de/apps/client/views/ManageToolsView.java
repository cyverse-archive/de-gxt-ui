package org.iplantc.de.apps.client.views;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * Created by sriram on 4/20/17.
 */
public interface ManageToolsView extends IsWidget, IsMaskable {


    public interface ManageToolsViewAppearance {
        String name();

        String version();

        String imaName();

        String status();

        String mask();

        int nameWidth();

        int imgNameWidth();

        int tagWidth();
    }


    void loadTools(List<Tool> tools);

    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter{
          void loadTools();
    }
}
