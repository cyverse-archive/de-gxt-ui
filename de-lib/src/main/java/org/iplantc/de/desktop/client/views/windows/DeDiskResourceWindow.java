package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.window.configs.DiskResourceWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.events.WindowHeadingUpdatedEvent;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourcePresenterFactory;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent.MaximizeHandler;
import com.sencha.gxt.widget.core.client.event.RestoreEvent;
import com.sencha.gxt.widget.core.client.event.RestoreEvent.RestoreHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

import java.util.List;

/**
 * @author jstroot
 */
public class DeDiskResourceWindow extends WindowBase implements FolderSelectionEvent.FolderSelectionEventHandler {

    public static final String DATA = "#data";
    public static final String DE_DATA_DETAILSPANEL_COLLAPSE = "de.data.detailspanel.collapse#";


    private final DiskResourcePresenterFactory presenterFactory;
    private final IplantDisplayStrings displayStrings;
    private final DiskResourceView.DiskResourceViewAppearance diskresourceAppearance;
    private DiskResourceView.Presenter presenter;

    @Inject
    DeDiskResourceWindow(final DiskResourcePresenterFactory presenterFactory,
                         final IplantDisplayStrings displayStrings,
                         final DiskResourceView.DiskResourceViewAppearance appearance) {
        this.presenterFactory = presenterFactory;
        this.displayStrings = displayStrings;
        this.diskresourceAppearance = appearance;
        setHeading(displayStrings.data());

        setMinWidth(Integer.parseInt(appearance.windowWidth()));
        setMinHeight(Integer.parseInt(appearance.windowHeight()));
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {

        super.show(windowConfig, tag, isMaximizable);
        // Create an empty
        List<HasId> resourcesToSelect = Lists.newArrayList();
        final DiskResourceWindowConfig diskResourceWindowConfig = (DiskResourceWindowConfig) windowConfig;
        if (diskResourceWindowConfig.getSelectedDiskResources() != null) {
            resourcesToSelect.addAll(diskResourceWindowConfig.getSelectedDiskResources());
        }

        this.presenter = presenterFactory.withSelectedResources(false,
                                                                false,
                                                                false,
                                                                false,
                                                                diskResourceWindowConfig.getSelectedFolder(),
                                                                resourcesToSelect);
        final String uniqueWindowTag = (diskResourceWindowConfig.getTag() == null) ? "" : "." + diskResourceWindowConfig.getTag();
        ensureDebugId(DeModule.WindowIds.DISK_RESOURCE_WINDOW + uniqueWindowTag);
        String minimizeDetails = null;
        if (ws.getAdditionalWindowStates() != null) {
            minimizeDetails = ws.getAdditionalWindowStates()
                                .get(WindowState.ADDITIONAL + DE_DATA_DETAILSPANEL_COLLAPSE + tag + "#"
                                     + userInfo.getUsername());
        }

        presenter.go(this, (minimizeDetails == null)? false: Boolean.valueOf(minimizeDetails));
        initHandlers();
        btnHelp = createHelpButton();
        getHeader().insertTool(btnHelp,0);
        btnHelp.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                WindowUtil.open(constants.faqUrl() + DATA);
                IntercomFacade.trackEvent(TrackingEventType.DATA_FAQ_CLICKED, null);
            }
        });
    }

    @Override
    public WindowConfig getWindowConfig() {
        DiskResourceWindowConfig config = (DiskResourceWindowConfig) this.config;
        config.setSelectedFolder(presenter.getSelectedFolder());
        List<HasId> selectedResources = Lists.newArrayList();
        selectedResources.addAll(presenter.getSelectedDiskResources());
        config.setSelectedDiskResources(selectedResources);
        return config;
    }

    @Override
    public void hide() {
        if (!isMinimized()) {
            presenter.cleanUp();
        }
        super.hide();
    }

    @Override
    public void onFolderSelected(FolderSelectionEvent event) {
        Folder selectedFolder = event.getSelectedFolder();

        if (selectedFolder == null || Strings.isNullOrEmpty(selectedFolder.getName())) {
            setHeading(displayStrings.data());
        } else {
            setHeading(displayStrings.dataWindowTitle(selectedFolder.getName()));
        }

        fireEvent(new WindowHeadingUpdatedEvent());

    }

    @Override
    public <C extends WindowConfig> void update(C config) {
        DiskResourceWindowConfig drConfig = (DiskResourceWindowConfig) config;
        if(presenter == null){
            final String uniqueWindowTag = (drConfig.getTag() == null) ? "" : "." + drConfig.getTag();
            show(config, uniqueWindowTag, true);
            return;
        }
        presenter.setSelectedFolderByPath(drConfig.getSelectedFolder());
        presenter.setSelectedDiskResourcesById(drConfig.getSelectedDiskResources());
        show();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        presenter.setViewDebugId(baseID);
    }

    @Override
    public String getWindowType() {
        return WindowType.DATA.toString();
    }

    @Override
    public FastMap<String> getAdditionalWindowStates() {
        FastMap<String> additionalData = new FastMap<>();
        additionalData.put(WindowState.ADDITIONAL + DE_DATA_DETAILSPANEL_COLLAPSE + getStateId() + "#"
                           + userInfo.getUsername(),
                presenter.isDetailsCollapsed() + "");
        return additionalData;
    }

    @Override
    public void restoreWindowState() {
        if (getStateId().equals(ws.getTag())) {
            super.restoreWindowState();
            String width = ws.getWidth();
            String height = ws.getHeight();
            setSize((width == null) ? diskresourceAppearance.windowWidth() : width,
                    (height == null) ? diskresourceAppearance.windowHeight() : height);
        }
    }
    private void initHandlers() {
        presenter.addFolderSelectedEventHandler(this);

        addRestoreHandler(new RestoreHandler() {
            @Override
            public void onRestore(RestoreEvent event) {
                maximized = false;
            }
        });

        addMaximizeHandler(new MaximizeHandler() {
            @Override
            public void onMaximize(MaximizeEvent event) {
                maximized = true;
            }
        });

        addShowHandler(new ShowHandler() {
            @Override
            public void onShow(ShowEvent event) {
                if (config != null && ((DiskResourceWindowConfig) config).isMaximized()) {
                    DeDiskResourceWindow.this.maximize();
                }
            }
        });
    }

}
