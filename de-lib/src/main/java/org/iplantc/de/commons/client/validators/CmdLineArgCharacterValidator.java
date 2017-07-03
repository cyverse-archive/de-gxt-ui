package org.iplantc.de.commons.client.validators;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.resources.client.messages.IplantValidationMessages;

import java.util.Collections;
import java.util.List;

/**
 * @author jstroot
 */
public class CmdLineArgCharacterValidator extends AbstractValidator<String> implements
        IPlantDefaultValidator {

    private final String restrictedChars;
    IplantValidationMessages validationMessages = GWT.create(IplantValidationMessages.class);
    private final IplantValidationConstants validationConstants = GWT.create(IplantValidationConstants.class);

    public CmdLineArgCharacterValidator(String restrictedChars) {
        this.restrictedChars = restrictedChars;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        // We have an error
        char[] restrictedCharsArr = restrictedChars.toCharArray();
        StringBuilder restrictedFound = new StringBuilder();

        for (char restricted : restrictedCharsArr) {
            for (char next : value.toCharArray()) {
                if (next == restricted) {
                    restrictedFound.append(restricted);
                    break;
                }
            }
        }

        if (restrictedFound.length() > 0) {
            String errorMsg = validationMessages.unsupportedChars(restrictedChars) + (restrictedChars.contains("\n") ? validationConstants.newlineToPrint() : "") + (restrictedChars.contains("\t") ? validationConstants.tabToPrint() : "") //$NON-NLS-1$
                    + ". " + validationMessages.invalidChars(restrictedFound.toString());
            return createError(new DefaultEditorError(editor, errorMsg, value));
        }

        return Collections.emptyList();
    }

}
