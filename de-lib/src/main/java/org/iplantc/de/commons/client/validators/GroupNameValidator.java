package org.iplantc.de.commons.client.validators;

import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;

import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;

import java.util.Collections;
import java.util.List;

/**
 * @author aramsey
 */
public class GroupNameValidator extends AbstractValidator<String> {

    private final IplantValidationConstants valConstants;
    private final ManageCollaboratorsView.Appearance appearance;

    public GroupNameValidator() {
        this.valConstants = GWT.create(IplantValidationConstants.class);
        this.appearance = GWT.create(ManageCollaboratorsView.Appearance.class);
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        char[] restrictedChars = (valConstants.restrictedGroupNameChars()).toCharArray();
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
            String errorMsg = appearance.groupNameValidationMsg(new String(restrictedChars))
                              + " " + appearance.invalidChars(restrictedFound.toString()); //$NON-NLS-1$

            return createError(new DefaultEditorError(editor, errorMsg, value));
        }

        return Collections.emptyList();
    }
}
