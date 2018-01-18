package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.SimpleDownloadWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * An iPlant window for displaying simple download links.
 *
 * @author psarando, jstroot
 */
public class SimpleDownloadWindow extends WindowBase {

    public interface SimpleDownloadWindowAppearance {
        String windowWidth();

        String windowHeight();
    }

    private final DiskResourceServiceFacade diskResourceServiceFacade;
    private final IplantDisplayStrings displayStrings;
    private final SimpleDownloadWindowAppearance appearance;


    @Inject
    SimpleDownloadWindow(final IplantDisplayStrings displayStrings,
                         final DiskResourceServiceFacade diskResourceServiceFacade,
                         final SimpleDownloadWindowAppearance appearance) {
        this.displayStrings = displayStrings;
        this.diskResourceServiceFacade = diskResourceServiceFacade;
        this.appearance = appearance;
        setHeading(displayStrings.download());

        setMinHeight(Integer.parseInt(appearance.windowHeight()));
        setMinWidth(Integer.parseInt(appearance.windowWidth()));

        ensureDebugId(DeModule.WindowIds.SIMPLE_DOWNLOAD);
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {

        super.show(windowConfig, tag, true);
        init((SimpleDownloadWindowConfig)windowConfig);
    }

    @Override
    public WindowConfig getWindowConfig() {
        return ConfigFactory.simpleDownloadWindowConfig();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public String getWindowType() {
        return WindowType.SIMPLE_DOWNLOAD.toString();
    }

    @Override
    public FastMap<String> getAdditionalWindowStates() {
        return null;
    }

    @Override
    public void restoreWindowState() {
        if (getStateId().equals(ws.getTag())) {
            super.restoreWindowState();
            String width = ws.getWidth();
            String height = ws.getHeight();
            setSize((Strings.isNullOrEmpty(width)) ? appearance.windowWidth() : width,
                    (Strings.isNullOrEmpty(height)) ? appearance.windowHeight() : height);
        }
    }

    private void buildLinks(SimpleDownloadWindowConfig config, VerticalLayoutContainer vlc) {
        for (final DiskResource dr : config.getResourcesToDownload()) {
            IPlantAnchor link2 = new IPlantAnchor(DiskResourceUtil.getInstance().parseNameFromPath(dr.getPath()),
                                                  120,
                                                  new ClickHandler() {

                                                      @Override
                                                      public void onClick(ClickEvent event) {
                                                          final String encodedSimpleDownloadURL = diskResourceServiceFacade.getEncodedSimpleDownloadURL(dr.getPath());
                                                          WindowUtil.open(encodedSimpleDownloadURL, "width=100,height=100");
                                                      }
                                                  });

            vlc.add(link2);
        }
    }

    private void init(SimpleDownloadWindowConfig config) {
        // Add window contents container for the simple download links
        VerticalLayoutContainer contents = new VerticalLayoutContainer();

        contents.add(new Label("" + displayStrings.simpleDownloadNotice()));
        buildLinks(config, contents);
        add(contents);

    }

}
