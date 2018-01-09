package org.iplantc.de.theme.base.client.apps;

import org.iplantc.de.apps.client.AppsView;

import com.google.gwt.core.client.GWT;

/**
 * @author jstroot
 */
public class AppsViewDefaultAppearance implements AppsView.AppsViewAppearance {
    private final AppsMessages appsMessages;

    public AppsViewDefaultAppearance() {
        this(GWT.<AppsMessages> create(AppsMessages.class));
    }

    public AppsViewDefaultAppearance(AppsMessages appsMessages) {
        this.appsMessages = appsMessages;
    }

    @Override
    public String viewCategoriesHeader() {
        return appsMessages.viewCategoriesHeader();
    }

    @Override
    public String appsWindowWidth() {
        return "820";
    }

    @Override
    public String appsWindowHeight() {
        return "400";
    }

    @Override
    public String pipelineEdWindowWidth() {
        return "900";
    }

    @Override
    public String pipelineEdWindowHeight() {
        return "500";
    }

    @Override
    public int pipelineEdWindowMinWidth() {
        return 640;
    }

    @Override
    public int pipelineEdWindowMinHeight() {
        return 440;
    }
}
