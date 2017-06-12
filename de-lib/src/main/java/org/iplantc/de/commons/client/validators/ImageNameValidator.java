package org.iplantc.de.commons.client.validators;

import org.iplantc.de.resources.client.messages.IplantValidationMessages;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;

import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;

import java.util.Collections;
import java.util.List;

/**
 * Created by sriram on 6/9/17.
 */
public class ImageNameValidator extends AbstractValidator<String> implements IPlantDefaultValidator {

    private final IplantValidationMessages validationMessages;

    public ImageNameValidator() {
        this(GWT.create(IplantValidationMessages.class));
    }

    ImageNameValidator(IplantValidationMessages validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
        if (Strings.isNullOrEmpty(value)) {
            return createError(new DefaultEditorError(editor, validationMessages.imgNameReqd(), value));
        }

        if (value.matches("^[\\s/:].*|.*[\\s/:]$")) {
            return createError(new DefaultEditorError(editor,
                                                      validationMessages.imgNameInvalid(),
                                                      value));
        }
        return Collections.emptyList();
    }
}
