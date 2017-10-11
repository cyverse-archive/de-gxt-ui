package org.iplantc.de.apps.widgets.client.view.editors.arguments;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

import com.sencha.gxt.widget.core.client.form.ComboBox;

public final class ClearComboBoxSelectionKeyDownHandler implements KeyDownHandler {

    public ClearComboBoxSelectionKeyDownHandler(ComboBox<?> selectionItemsComboBox) {
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
            //do nothing
        }
    }
}
