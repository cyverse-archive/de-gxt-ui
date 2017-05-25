package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;

import com.google.gwt.core.client.GWT;

/**
 * Created by sriram on 5/25/17.
 */
public class NewToolRequestFormViewDefaultAppearance implements NewToolRequestFormView.NewToolRequestFormViewAppearance {

    private final ToolMessages toolMessages;

    public NewToolRequestFormViewDefaultAppearance() {
        this((ToolMessages)GWT.create(ToolMessages.class));
    }

    public NewToolRequestFormViewDefaultAppearance(ToolMessages messages) {
        this.toolMessages = messages;
    }

    @Override
    public String newToolRequest() {
        return toolMessages.newToolRequest();
    }

    @Override
    public String makePublicRequest() {
        return toolMessages.makePublicRequest();
    }

    @Override
    public String newToolInstruction() {
        return toolMessages.newToolInstruction();
    }

    @Override
    public String makePublicInstruction() {
        return toolMessages.makePublicInstruction();
    }
}
