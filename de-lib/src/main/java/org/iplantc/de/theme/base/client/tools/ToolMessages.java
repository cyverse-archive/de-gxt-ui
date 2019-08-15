package org.iplantc.de.theme.base.client.tools;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Created by sriram on 4/24/17.
 */
public interface ToolMessages extends Messages {

    String delete();

    String name();

    String tag();

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

    SafeHtml sameFileError(String filename);

    SafeHtml alert();

    SafeHtml fileExists(String dupeFiles);

    String toolInfoError();

}
