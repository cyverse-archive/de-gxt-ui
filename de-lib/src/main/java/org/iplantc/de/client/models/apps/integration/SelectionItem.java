package org.iplantc.de.client.models.apps.integration;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasDisplayText;
import org.iplantc.de.client.models.HasSettableId;

/**
 * @author jstroot
 */
public interface SelectionItem extends HasSettableId, HasName, HasDescription, TakesValue<String>, HasDisplayText {

    String TO_BE_REMOVED = "List of sub items to be removed";
    String ARGUMENT_OPTION_KEY = "name";
    String IS_DEFAULT_KEY = "isDefault";
    String TMP_ID_TAG = "TMP_ID";

    @PropertyName(IS_DEFAULT_KEY)
    boolean isDefault();

    @PropertyName(IS_DEFAULT_KEY)
    void setDefault(boolean isDefault);
}
