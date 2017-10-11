package org.iplantc.de.commons.client.validators;

import org.iplantc.de.resources.client.constants.IplantValidationConstants;
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
 * Validates a file or folder name.
 * 
 * @author psarando, jstroot
 * 
 */
public class DiskResourceNameValidator extends AbstractValidator<String> implements
        IPlantDefaultValidator {

    private final IplantValidationConstants validationConstants;
    private final IplantValidationMessages validationMessages;
    char[] restrictedChars;

    public DiskResourceNameValidator() {
        this(GWT.<IplantValidationConstants> create(IplantValidationConstants.class),
             GWT.<IplantValidationMessages> create(IplantValidationMessages.class));
    }

    DiskResourceNameValidator(final IplantValidationConstants validationConstants,
                              final IplantValidationMessages validationMessages) {
        this.validationConstants = validationConstants;
        this.validationMessages = validationMessages;
        restrictedChars = validationConstants.restrictedDiskResourceNameChars().toCharArray();
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        String errorMsg = validateAndReturnError(value);

        if (!Strings.isNullOrEmpty(errorMsg)) {
            return createError(new DefaultEditorError(editor, errorMsg, value));
        }

        return Collections.emptyList();
    }

    /**
     * Validate diskresource name and returns error if any. Otherwiese returns null
     *
     * @param value Diskresource name
     * @return An error string
     */
    public String validateAndReturnError(String value) {
        // check for spaces at the beginning and at the end of the file name
        if (value.startsWith(" ") || value.endsWith(" ")) { //$NON-NLS-1$ //$NON-NLS-2$
            return validationMessages.drNameValidationMsg(
                    validationConstants.newlineToPrint() + validationConstants.tabToPrint() + new String(
                            restrictedChars));
        }


        //$NON-NLS-1$
        StringBuilder restrictedFound = new StringBuilder();

        for (char restricted : restrictedChars) {
            for (char next : value.toCharArray()) {
                if (next == restricted) {
                    restrictedFound.append(restricted);
                    break;
                }
            }
        }

        if (restrictedFound.length() > 0) {
            String errorMsg = (validationConstants.restrictedDiskResourceNameChars().contains("\n") ?
                               validationConstants.newlineToPrint() :
                               "") + (validationConstants.restrictedDiskResourceNameChars()
                                                         .contains("\t") ?
                                      validationConstants.tabToPrint() :
                                      "")
                              + validationMessages.drNameValidationMsg(validationConstants.restrictedDiskResourceNameChars())
                              + " " + validationMessages.invalidChars(restrictedFound.toString());

            return errorMsg;
        }
        return null;
    }
}

