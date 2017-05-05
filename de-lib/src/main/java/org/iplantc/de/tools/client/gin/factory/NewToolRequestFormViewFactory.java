package org.iplantc.de.tools.client.gin.factory;

import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;
import org.iplantc.de.client.models.toolRequests.Architecture;
import org.iplantc.de.client.models.toolRequests.YesNoMaybe;

import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * @author jstroot
 */
public interface NewToolRequestFormViewFactory {
    NewToolRequestFormView createNewToolRequestFormView(ComboBox<Architecture> architectureComboBox,
                                                        ComboBox<YesNoMaybe> yesNoMaybeComboBox);
}
