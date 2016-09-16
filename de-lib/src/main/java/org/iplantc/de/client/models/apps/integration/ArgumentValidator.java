package org.iplantc.de.client.models.apps.integration;

import com.google.web.bindery.autobean.shared.Splittable;
import org.iplantc.de.client.models.HasSettableId;

public interface ArgumentValidator extends HasSettableId {

    String VALIDATOR = "validator";
    String KEY_DOWN_HANDLER_REG = "keyDownHandlerRegistration";
    String KEY_DOWN_HANDLER = "keyDownHandler";
    String TMP_ID_TAG = "TMP_ID";

    ArgumentValidatorType getType();
    
    void setType(ArgumentValidatorType type);
    
    Splittable getParams();
    
    void setParams(Splittable params);
}
