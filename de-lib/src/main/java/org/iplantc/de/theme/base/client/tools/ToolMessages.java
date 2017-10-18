package org.iplantc.de.theme.base.client.tools;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Created by sriram on 4/24/17.
 */
public interface ToolMessages extends Messages {

    String manageTools();

    String tools();

    String requestTool();

    String edit();

    String delete();

    String useInApp();

    String shareCollab();

    String share();

    String name();

    String version();

    String imaName();

    String status();

    String submitForUse();

    String refresh();

    String searchTools();

    String addTool();

    String toolName();

    String description();

    String imgName();

    String tag();

    String dockerUrl();

    String pidsLimit();

    String memLimit();

    String nwMode();

    String timeLimit();

    String create();

    String restrictions();

    String toolInfo();

    String shareTools();
    
    String confirmDelete();

    String newToolRequest();

    String makePublicRequest();

    String newToolInstruction();

    String makePublicInstruction();

    String appsLoadError();

    String toolAdded(String name);

    String toolUpdated(String name);

    String toolDeleted(String name);

    String manageSharing();

    String done();

    /**
     * The field label for entrypoint
     *
     * @return  label for entrypoint field
     */
    String entryPoint();

    SafeHtml sameFileError(String filename);

    SafeHtml alert();

    SafeHtml fileExists(String dupeFiles);
}


