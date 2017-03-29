package org.iplantc.de.apps.integration.client.view.widgets;

import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.sencha.gxt.widget.core.client.Header;

public class HeaderEditor implements LeafValueEditor<String> {

    private String hasHtmlModel;
    private final Header peer;
    private final PropertyEditorAppearance appearance;

    public HeaderEditor(Header peer, PropertyEditorAppearance appearance) {
        this.peer = peer;
        this.appearance = appearance;
    }

    @Override
    public String getValue() {
        return hasHtmlModel;
    }

    @Override
    public void setValue(String value) {
        this.hasHtmlModel = value;
        peer.setHTML(appearance.createContentPanelHeaderLabel(SafeHtmlUtils.fromString(value), false));
    }

}
