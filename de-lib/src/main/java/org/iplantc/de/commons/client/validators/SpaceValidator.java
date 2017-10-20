package org.iplantc.de.commons.client.validators;

import org.iplantc.de.resources.client.messages.IplantValidationMessages;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;

import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;

import java.util.List;

/**
 * Validate a gxt field value if it contains space
 * 
 * Created by sriram on 10/20/17.
 */
public class SpaceValidator extends AbstractValidator<String> implements
                                                              IPlantDefaultValidator {

    private IplantValidationMessages validationMessages;

    public SpaceValidator() {
        this(GWT.create(IplantValidationMessages.class));
    }

    SpaceValidator(final IplantValidationMessages validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
       if (!Strings.isNullOrEmpty(value)) {
           if(value.contains(" ")) {
               String errMsg = validationMessages.spaceNotAllowedMsg();
               return createError(new DefaultEditorError(editor, errMsg, value));
           }
       }
       return null;
    }
}
