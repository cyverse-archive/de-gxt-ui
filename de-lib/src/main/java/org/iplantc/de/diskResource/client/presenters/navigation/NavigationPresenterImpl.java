package org.iplantc.de.diskResource.client.presenters.navigation;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.FileSavedEvent;
import org.iplantc.de.client.events.diskResources.DiskResourcesMovedEvent;
import org.iplantc.de.client.events.diskResources.FolderRefreshedEvent;
import org.iplantc.de.client.models.CommonModelAutoBeanFactory;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.events.DiskResourceNameSelectedEvent;
import org.iplantc.de.diskResource.client.events.DiskResourcePathSelectedEvent;
import org.iplantc.de.diskResource.client.events.DiskResourceRenamedEvent;
import org.iplantc.de.diskResource.client.events.DiskResourcesDeletedEvent;
import org.iplantc.de.diskResource.client.events.FileUploadedEvent;
import org.iplantc.de.diskResource.client.events.FolderCreatedEvent;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent;
import org.iplantc.de.diskResource.client.events.RequestSimpleUploadEvent;
import org.iplantc.de.diskResource.client.events.RootFoldersRetrievedEvent;
import org.iplantc.de.diskResource.client.events.SavedSearchesRetrievedEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.events.search.UpdateSavedSearchesEvent;
import org.iplantc.de.diskResource.client.events.selection.DNDDiskResourcesCompleted;
import org.iplantc.de.diskResource.client.events.selection.ImportFromUrlSelected;
import org.iplantc.de.diskResource.client.events.selection.RefreshFolderSelected;
import org.iplantc.de.diskResource.client.events.selection.SimpleUploadSelected;
import org.iplantc.de.diskResource.client.gin.factory.NavigationViewFactory;
import org.iplantc.de.diskResource.client.presenters.callbacks.DuplicateDiskResourceCallback;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsLoadConfig;
import org.iplantc.de.diskResource.client.presenters.navigation.proxy.CachedFolderTreeStoreBinding;
import org.iplantc.de.diskResource.client.presenters.navigation.proxy.SelectFolderByPathLoadHandler;
import org.iplantc.de.diskResource.client.views.navigation.NavigationViewDnDHandler;
import org.iplantc.de.diskResource.client.views.toolbar.dialogs.FileUploadByUrlDialog;
import org.iplantc.de.diskResource.client.views.toolbar.dialogs.HasPending;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.UIObject;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jstroot
 */
public class NavigationPresenterImpl implements
                                    NavigationView.Presenter,
                                    FolderSelectionEvent.FolderSelectionEventHandler,
                                    FolderRefreshedEvent.FolderRefreshedEventHandler,
                                    DiskResourcesDeletedEvent.DiskResourcesDeletedEventHandler,
                                    DiskResourceRenamedEvent.DiskResourceRenamedEventHandler,
                                    FolderCreatedEvent.FolderCreatedEventHandler,
                                    DiskResourcesMovedEvent.DiskResourcesMovedEventHandler {

    class FolderStoreDataChangeHandler implements StoreDataChangeEvent.StoreDataChangeHandler<Folder> {
        private final NavigationView.Appearance appearance;
        private final Tree<Folder, Folder> tree;

        public FolderStoreDataChangeHandler(final Tree<Folder, Folder> tree,
                                            final NavigationView.Appearance appearance) {
            this.tree = tree;
            this.appearance = appearance;
        }

        @Override
        public void onDataChange(StoreDataChangeEvent<Folder> event) {
            Folder folder = event.getParent();
            if (folder != null && tree.getStore().getAllChildren(folder) != null) {
                for (Folder f : tree.getStore().getAllChildren(folder)) {
                    if (f.isFilter()) {
                        Tree.TreeNode<Folder> tn = tree.findNode(f);
                        tree.getView()
                            .getTextElement(tn)
                            .setInnerSafeHtml(appearance.treeNodeFilterText(f.getName()));
                    }
                }
            }
        }
    }

    final class CheckDuplicatesCallback <D extends UIObject & IsHideable & HasPending<Map.Entry<Field<String>, Status>>> extends
                                                                                                                                 DuplicateDiskResourceCallback {
        private final Map<String, Field<String>> destResourceMap;
        private final Folder uploadDest;
        private final DiskResourceServiceFacade drService;
        private final D dlg;
        private final Map<Field<String>, Status> fieldToStatusMap;

        public CheckDuplicatesCallback(Map<String, Field<String>> destResourceMap, Map<Field<String>, Status> fieldToStatusMap, Folder uploadDest, DiskResourceServiceFacade drService, D dlg) {
            super(Lists.newArrayList(destResourceMap.keySet()), null);
            this.destResourceMap = destResourceMap;
            this.fieldToStatusMap = fieldToStatusMap;
            this.uploadDest = uploadDest;
            this.drService = drService;
            this.dlg = dlg;
        }

        @Override
        public void markDuplicates(Collection<String> duplicates) {
            for(Map.Entry<String, Field<String>> entry : destResourceMap.entrySet()){
                Field<String> urlField = entry.getValue();
                Status formStatus = fieldToStatusMap.get(urlField);

                if (duplicates.contains(entry.getKey())){
                    urlField.markInvalid(appearance.fileExist());
                    formStatus.clearStatus("");
                } else {
                    Map.Entry<Field<String>, Status> e = getEntry(formStatus);
                    dlg.addPending(e);
                    drService.importFromUrl(urlField.getValue(), uploadDest, new ImportFromUrlCallback<>(dlg, e));
                }
            }
        }

        private Map.Entry<Field<String>, Status> getEntry(Status status){
            for (Map.Entry<Field<String>, Status> e : fieldToStatusMap.entrySet()) {
                if (e.getValue() == status) {
                    return e;
                }
            }
            return null;
        }
    }

    final class ImportFromUrlCallback <D extends UIObject & IsHideable & HasPending<Map.Entry<Field<String>, Status>>> extends
                                                                                                                               AppsCallback<String> {
        private final D dlg;
        private final Map.Entry<Field<String>, Status> pending;

        public ImportFromUrlCallback(D dlg, Map.Entry<Field<String>, Status> pending) {
            this.dlg = dlg;
            this.pending = pending;
        }

        @Override
        public void onSuccess(String result) {
            dlg.removePending(pending);
            pending.getValue().clearStatus("");
            if(!dlg.hasPending()){
                dlg.hide();
            }
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            // TODO JDS Determine how to update the UI
            if (dlg.getNumPending() == 1) {
                // ErrorHandler.post(caught);
                // "Blink" the window on the last pending element.
                dlg.getElement().<FxElement>cast().blink();
            }

            pending.getKey().markInvalid(appearance.uploadFailErrorMessage());
            dlg.removePending(pending);
        }
    }

    final TreeStore<Folder> treeStore;
    @Inject IplantAnnouncer announcer;
    @Inject NavigationView.Presenter.Appearance appearance;
    private final DiskResourceUtil diskResourceUtil;
    @Inject UserInfo userInfo;
    private final EventBus eventBus;
    private final DiskResourceView.FolderRpcProxy folderRpcProxy;
    private final List<HandlerRegistration> handlerRegistrations;
    private final TreeLoader<Folder> treeLoader;
    private final NavigationView view;
    private IsMaskable maskable;
    private HandlerManager handlerManager;
    @Inject AsyncProviderWrapper<FileUploadByUrlDialog> urlImportDlgProvider;
    @Inject DiskResourceServiceFacade drService;
    @Inject CommonModelAutoBeanFactory factory;

    @Inject
    NavigationPresenterImpl(final NavigationViewFactory viewFactory,
                            final TreeStore<Folder> treeStore,
                            final DiskResourceView.FolderRpcProxy folderRpcProxy,
                            final DiskResourceUtil diskResourceUtil,
                            final EventBus eventBus,
                            final NavigationView.Appearance appearance) {
        this.treeStore = treeStore;
        this.folderRpcProxy = folderRpcProxy;
        this.eventBus = eventBus;
        this.diskResourceUtil = diskResourceUtil;
        treeLoader = initTreeLoader(folderRpcProxy);
        view = viewFactory.create(treeStore, treeLoader, new NavigationViewDnDHandler(diskResourceUtil,
                                                                                      this,
                                                                                      appearance));
        handlerRegistrations = Lists.newArrayList();

        view.addFolderSelectedEventHandler(this);
        this.treeStore.addStoreDataChangeHandler(new FolderStoreDataChangeHandler(view.getTree(),
                                                                                  appearance));
        this.treeLoader.addLoadHandler(new CachedFolderTreeStoreBinding(treeStore));

        // Wire up global event handlers
        handlerRegistrations.add(eventBus.addHandler(FolderRefreshedEvent.TYPE, this));
        handlerRegistrations.add(eventBus.addHandler(DiskResourcesDeletedEvent.TYPE, this));
        handlerRegistrations.add(eventBus.addHandler(DiskResourceRenamedEvent.TYPE, this));
        handlerRegistrations.add(eventBus.addHandler(FolderCreatedEvent.TYPE, this));
        handlerRegistrations.add(eventBus.addHandler(DiskResourcesMovedEvent.TYPE, this));
        handlerRegistrations.add(eventBus.addHandler(FileUploadedEvent.TYPE, this));
        handlerRegistrations.add(eventBus.addHandler(FileSavedEvent.TYPE, this));
    }

    TreeLoader<Folder> initTreeLoader(final DiskResourceView.FolderRpcProxy rpcProxy){
       return new TreeLoader<Folder>(rpcProxy) {
            @Override
            public boolean hasChildren(Folder parent) {
                return parent.hasSubDirs();
            }
        };
    }

    // <editor-fold desc="Handler Registrations">
    @Override
    public HandlerRegistration
            addRootFoldersRetrievedEventHandler(RootFoldersRetrievedEvent.RootFoldersRetrievedEventHandler handler) {
        return folderRpcProxy.addRootFoldersRetrievedEventHandler(handler);
    }

    @Override
    public HandlerRegistration
            addSavedSearchedRetrievedEventHandler(SavedSearchesRetrievedEvent.SavedSearchesRetrievedEventHandler handler) {
        return folderRpcProxy.addSavedSearchedRetrievedEventHandler(handler);
    }

    @Override
    public HandlerRegistration
            addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler handler) {
        return folderRpcProxy.addSubmitDiskResourceQueryEventHandler(handler);
    }

    // </editor-fold>

    // <editor-fold desc="Event Handlers">
    @Override
    public void onBeforeLoad(BeforeLoadEvent<FolderContentsLoadConfig> event) {
        if (getSelectedFolder() == null) {
            return;
        }

        final Folder folderToBeLoaded = event.getLoadConfig().getFolder();

        /*
         * If the loaded contents are not the contents of the currently selected folder, then cancel the
         * load.
         */
        if (!Strings.isNullOrEmpty(folderToBeLoaded.getId())
                && !folderToBeLoaded.getId().equals(getSelectedFolder().getId())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDiskResourceNameSelected(DiskResourceNameSelectedEvent event) {

        if (!(event.getSelectedItem() instanceof Folder)) {
            return;
        }
        setSelectedFolder(event.getSelectedItem());
    }

    @Override
    public void onDiskResourcePathSelected(DiskResourcePathSelectedEvent event) {
        setSelectedFolder(event.getSelectedDiskResource());
    }

    @Override
    public void onDiskResourcesDeleted(Collection<DiskResource> resources, Folder parentFolder) {
        reloadTreeStoreFolderChildren(parentFolder);
    }

    @Override
    public void onDiskResourcesMoved(DiskResourcesMovedEvent event) {
        if (event.isMoveContents()) {
            // If a folder's contents was moved, then the src and dest folders will already be refreshed.
            return;
        }

        List<DiskResource> resourcesToMove = event.getResourcesToMove();
        Folder destinationFolder = event.getDestinationFolder();
        Folder srcFolder = event.getSrcFolder();
        Folder selectedFolder = getSelectedFolder();

        if (diskResourceUtil.contains(resourcesToMove, selectedFolder)) {
            selectedFolderMovedFromNavTree(destinationFolder);
        } else {
            diskResourcesMovedFromGrid(resourcesToMove, selectedFolder, srcFolder, destinationFolder);
        }
    }

    @Override
    public void onFolderCreated(Folder parentFolder, Folder newFolder) {
        reloadTreeStoreFolderChildren(parentFolder);
    }

    @Override
    public void onFolderSelected(FolderSelectionEvent event) {
        if (event.getSelectedFolder() instanceof DiskResourceQueryTemplate) {
            // If the given query has not been saved, we need to deselect everything
            DiskResourceQueryTemplate searchQuery = (DiskResourceQueryTemplate)event.getSelectedFolder();
            if (!searchQuery.isSaved()) {
                deSelectAll();
            }
        }
    }

    @Override
    public void onImportFromUrlSelected(final ImportFromUrlSelected event) {
        Folder destinationFolder = event.getSelectedFolder();
        if (destinationFolder == null) {
            destinationFolder = getSelectedUploadFolder();
        }

        if (canUpload(destinationFolder)) {
            Folder finalDestinationFolder = destinationFolder;
            urlImportDlgProvider.get(new AsyncCallback<FileUploadByUrlDialog>() {
                @Override
                public void onFailure(Throwable caught) {}

                @Override
                public void onSuccess(FileUploadByUrlDialog dialog) {
                    dialog.show(finalDestinationFolder);
                    dialog.addCancelButtonSelectHandler(selectEvent -> dialog.hide());
                    dialog.addOkButtonSelectHandler(selectEvent -> {
                        dialog.getOkButton().setEnabled(false);
                        handleUrlImport(finalDestinationFolder, dialog.getFieldToStatusMap(), dialog);
                    });
                }
            });
        }
    }

    void handleUrlImport(Folder destinationFolder, Map<Field<String>, Status> fieldToStatusMap, FileUploadByUrlDialog dialog) {
        final FastMap<Field<String>> destResourceMap = new FastMap<>();

        for (Map.Entry<Field<String>, Status> entry : fieldToStatusMap.entrySet()) {
            Field<String> field = entry.getKey();
            if(field.getValue() == null)
                continue;
            String url = field.getValue().trim();
            if (!url.isEmpty()) {
                Status status = entry.getValue();
                status.setBusy("");
                status.show();
                field.setValue(url);
                String resourceId = destinationFolder.getPath() + "/" + diskResourceUtil.parseNameFromPath(url);
                destResourceMap.put(resourceId, field);
            } else {
                field.setEnabled(false);
            }
        }

        if (!destResourceMap.isEmpty()) {
            final HasPaths dto = factory.hasPaths().as();
            dto.setPaths(Lists.newArrayList(destResourceMap.keySet()));
            drService.diskResourcesExist(dto, new CheckDuplicatesCallback<>(destResourceMap,
                                                                            fieldToStatusMap,
                                                                            destinationFolder,
                                                                            drService,
                                                                            dialog));
        }
    }

    boolean canUpload(Folder uploadDest) {
        if (uploadDest != null && diskResourceUtil.canUploadTo(uploadDest)) {
            return true;
        } else {
            showErrorMsg();
            return false;
        }
    }

    void showErrorMsg() {
        new AlertMessageBox(appearance.permissionErrorTitle(),
                            appearance.permissionErrorMessage()).show();
    }

    @Override
    public void onRename(DiskResource originalDr, DiskResource newDr) {
        Folder parent = getFolderByPath(diskResourceUtil.parseParent(newDr.getPath()));
        if (parent != null) {
            reloadTreeStoreFolderChildren(parent);
        }
    }

    @Override
    public void onFolderRefreshed(FolderRefreshedEvent event) {
        reloadTreeStoreFolderChildren(event.getFolder());
    }

    @Override
    public void onFileUploaded(FileUploadedEvent event) {
        String path = event.getUploadDestFolder().getPath();
        refreshFolder(path);
    }

    @Override
    public void onFileSaved(FileSavedEvent event) {
        File file = event.getFile();
        String parentPath = diskResourceUtil.parseParent(file.getPath());
        refreshFolder(parentPath);
    }

    void refreshFolder(String path) {
        Folder folder = getFolderByPath(path);
        reloadTreeStoreFolderChildren(folder);
    }

    @Override
    public void onSimpleUploadSelected(SimpleUploadSelected event) {
        Folder destinationFolder = event.getSelectedFolder();
        if (destinationFolder == null) {
            destinationFolder = getSelectedUploadFolder();
        }
        eventBus.fireEvent(new RequestSimpleUploadEvent(destinationFolder));
    }

    /**
     * Ensures that the navigation window shows the given templates. These show up in the navigation
     * window as "magic folders".
     * <p/>
     * This method ensures that the only the given list of queryTemplates will be displayed in the
     * navigation pane.
     * <p/>
     * Only objects which are instances of {@link DiskResourceQueryTemplate} will be operated on. Items
     * which can't be found in the tree store will be added, and items which are already in the store and
     * are marked as dirty will be updated.
     */
    @Override
    public void onUpdateSavedSearches(UpdateSavedSearchesEvent event) {
        List<DiskResourceQueryTemplate> removedSearches = event.getRemovedSearches();
        if (removedSearches != null) {
            for (DiskResourceQueryTemplate qt : removedSearches) {
                treeStore.remove(qt);
            }
        }

        List<DiskResourceQueryTemplate> savedSearches = event.getSavedSearches();
        if (savedSearches != null) {
            for (DiskResourceQueryTemplate qt : savedSearches) {
                // If the item already exists in the store and the template is dirty, update it
                updateQueryTemplate(qt);
            }
        }
    }

    // </editor-fold>

    @Override
    public void addFolder(Folder folder) {
        if (treeStore.findModel(folder) != null) {
            // Already added
            return;
        }
        treeStore.add(folder);
    }

    @Override
    public void cleanUp() {
        for (HandlerRegistration hr : handlerRegistrations) {
            eventBus.removeHandler(hr);
        }
    }

    @Override
    public void doMoveDiskResources(Folder targetFolder, List<DiskResource> dropData) {
        ensureHandlers().fireEvent(new DNDDiskResourcesCompleted(targetFolder, dropData));
    }

    @Override
    public void expandFolder(Folder folder) {
        view.getTree().setExpanded(folder, true);
    }

    @Override
    public Tree.TreeNode<Folder> findTreeNode(Element el) {
        return view.getTree().findNode(el);
    }

    @Override
    public Folder getFolderByPath(String path) {
        if (treeStore.getRootItems() != null) {
            for (Folder folder : treeStore.getAll()) {
                if (folder.getPath() != null && folder.getPath().equals(path)) {
                    return folder;
                }
            }
        }
        return null;
    }

    @Override
    public Folder getParent(Folder child) {
        return treeStore.getParent(child);
    }

    @Override
    public Folder getSelectedFolder() {
        return view.getTree().getSelectionModel().getSelectedItem();
    }

    @Override
    public Folder getSelectedUploadFolder() {
        if (getSelectedFolder() == null) {
            return getFolderByPath(userInfo.getHomePath());
        }
        return getSelectedFolder();
    }

    @Override
    public NavigationView getView() {
        return view;
    }

    @Override
    public boolean isLoaded(Folder folder) {
        return view.getTree().findNode(folder).isLoaded();
    }

    @Override
    public boolean isPathUnderKnownRoot(String path) {
        if (!Strings.isNullOrEmpty(path)) {
            for (Folder root : treeStore.getRootItems()) {
                String rootPath = root.getPath();

                if (path.equals(rootPath) || path.startsWith(rootPath + "/")) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void reloadTreeStoreFolderChildren(Folder folder) {
        if (folder == null || treeStore.findModel(folder) == null) {
            return;
        }

        Folder selectedFolder = getSelectedFolder();
        boolean isCurrent = false;
        boolean isDescendant = false;
        if (selectedFolder != null) {
            isCurrent = folder.getId().equals(selectedFolder.getId());
            isDescendant = diskResourceUtil.isDescendantOfFolder(folder, selectedFolder);
        }

        removeChildren(folder);

        if (isCurrent || isDescendant) {
            if (!(selectedFolder instanceof DiskResourceQueryTemplate)) {
                // Re-select selectedFolder to cause a selection changed event
                // or to trigger lazy-loading
                setSelectedFolder((HasPath)selectedFolder);
            }
        }

        if (!isDescendant) {
            // Only trigger a load of the refreshed folder if selectedFolder is not its descendant,
            // otherwise the lazy-loader will handle expanding and loading the refreshed folder.
            treeLoader.load(folder);
        }
    }

    @Override
    public boolean rootsLoaded() {
        return treeStore.getRootCount() > 0;
    }

    @Override
    public void setMaskable(IsMaskable maskable) {
        this.maskable = maskable;
        this.folderRpcProxy.setMaskable(maskable);
    }

    @Override
    public void setSelectedFolder(final Folder folder) {
        if (folder == null) {
            return;
        }
        final Folder findModelWithKey = treeStore.findModelWithKey(folder.getId());
        if (findModelWithKey != null) {
            view.getTree().getSelectionModel().deselectAll();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                @Override
                public void execute() {
                    view.getTree().getSelectionModel().select(true, findModelWithKey);
                    view.getTree().scrollIntoView(findModelWithKey);
                }
            });
        }
    }

    @Override
    public void setSelectedFolder(HasPath hasPath) {
        if ((hasPath == null) || Strings.isNullOrEmpty(hasPath.getPath())) {
            return;
        }

        Folder folder = getFolderByPath(hasPath.getPath());
        if (folder != null) {
            /*
             * Trigger a selection changed event by deselecting the current folder and re-selecting it.
             */
            deSelectAll();
            setSelectedFolder(folder);
        } else {
            // Create and add the SelectFolderByIdLoadHandler to the treeLoader.
            SelectFolderByPathLoadHandler.registerFolderLoader(hasPath,
                                                               this,
                                                               ensureHandlers().getHandler(RefreshFolderSelected.TYPE, 0),
                                                               appearance,
                                                               maskable,
                                                               announcer,
                                                               treeLoader);
        }
    }

    void deSelectAll() {
        view.getTree().getSelectionModel().deselectAll();
    }

    void removeChildren(Folder folder) {
        if (folder == null || treeStore.findModel(folder) == null) {
            return;
        }

        treeStore.removeChildren(folder);

        // Set folder node as not-loaded, to prevent problems in lazy-loader logic.
        view.getTree().findNode(folder).setLoaded(false);
    }

    void updateQueryTemplate(DiskResourceQueryTemplate queryTemplate) {
        Preconditions.checkNotNull(queryTemplate);

        if (treeStore.findModel(queryTemplate) == null) {
            treeStore.add(queryTemplate);
        } else if (queryTemplate.isDirty()) {
            // Only update if it is dirty
            treeStore.update(queryTemplate);
        }
    }

    private void diskResourcesMovedFromGrid(List<DiskResource> resourcesToMove,
                                            Folder selectedFolder,
                                            Folder srcFolder,
                                            Folder destinationFolder) {
        // If a folder was moved, then the src and dest folders will already be refreshed.
        if (!diskResourceUtil.containsFolder(resourcesToMove)) {
            String selectedFolderId = selectedFolder.getId();
            if (destinationFolder.getId().equals(selectedFolderId)
                || srcFolder.getId().equals(selectedFolderId)) {
                // Refresh the selected folder's contents.
                setSelectedFolder((HasPath)selectedFolder);
            }
        }
    }

    /**
     * If the selected folder happens to be one of the moved items, then view the destination by
     * setting it as the selected folder.
     */
    private void selectedFolderMovedFromNavTree(Folder destinationFolder) {
        // View the destination folder's contents.
        setSelectedFolder((HasPath)destinationFolder);
    }

    @Override
    public HandlerRegistration addDNDDiskResourcesCompletedHandler(DNDDiskResourcesCompleted.DNDDiskResourcesCompletedHandler handler) {
        return ensureHandlers().addHandler(DNDDiskResourcesCompleted.TYPE, handler);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    @Override
    public HandlerRegistration addRefreshFolderSelectedHandler(RefreshFolderSelected.RefreshFolderSelectedHandler handler) {
        return ensureHandlers().addHandler(RefreshFolderSelected.TYPE, handler);
    }
}
