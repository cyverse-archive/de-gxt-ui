package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.diskResource.client.views.toolbar.dialogs.HasPending;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.form.Field;

import java.util.Map;

/**
 * An interface for the Import by URL feature
 */
public interface FileUploadByUrlView extends IsWidget,
                                             IsMaskable,
                                             HasPending<Map.Entry<Field<String>, Status>>,
                                             InvalidEvent.HasInvalidHandlers,
                                             ValidEvent.HasValidHandlers {

    /**
     * Appearance class for the FileUploadByUrlView
     */
    interface FileUploadByUrlViewAppearance {

        String importLabel();

        String uploadingToFolder(String path);

        String urlImport();

        String urlPrompt();

        String containerWidth();

        String containerHeight();

        SafeHtml destText(String destPath, String destName);
    }

    Map<Field<String>, Status> getFieldToStatusMap();

    boolean isValidForm();
}
