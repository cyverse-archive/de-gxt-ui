package org.iplantc.de.client.sharing;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.dialogs.ManageCollaboratorsDialog;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.diskResource.client.model.DataSharingKeyProvider;
import org.iplantc.de.diskResource.client.model.DataSharingProperties;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.ShareBreakDownDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CellSelectionEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author sriram, jstroot
 */
public class SharingPermissionsPanel implements SharingPermissionView {

    @UiTemplate("SharingPermissionsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, SharingPermissionsPanel> {
    }

    @UiField Grid<Sharing> grid;
    @UiField ToolBar toolbar;
    @UiField(provided = true) ListStore<Sharing> listStore;
    @UiField(provided = true) ColumnModel<Sharing> cm;
    @UiField VerticalLayoutContainer container;
    @UiField(provided = true) final SharingAppearance appearance;

    private final EventBus eventBus;
    private FastMap<List<Sharing>> originalList;
    private final FastMap<SharedResource> resources;
    private final SharingPresenter presenter;
    private FastMap<List<Sharing>> sharingMap;
    private HorizontalPanel explainPanel;
    final Widget widget;

    @Inject AsyncProviderWrapper<ManageCollaboratorsDialog> collaboratorsDialogProvider;

    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @Inject
    SharingPermissionsPanel(@Assisted SharingPresenter dataSharingPresenter,
                            @Assisted FastMap<SharedResource> resources,
                            final SharingAppearance appearance) {
        this.presenter = dataSharingPresenter;
        this.resources = resources;
        this.appearance = appearance;
        eventBus = EventBus.getInstance();
        init();
        widget = uiBinder.createAndBindUi(this);
        initToolbar();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private void init() {
        listStore = new ListStore<>(new DataSharingKeyProvider());
        cm = buildColumnModel();
        eventBus.addHandler(UserSearchResultSelected.TYPE,
                            new UserSearchResultSelected.UserSearchResultSelectedEventHandler() {

                                @Override
                                public void
                                        onUserSearchResultSelected(UserSearchResultSelected userSearchResultSelected) {
                                    if (userSearchResultSelected.getTag()
                                                                .equals(UserSearchResultSelected.USER_SEARCH_EVENT_TAG.SHARING.toString())) {
                                        addCollaborator(userSearchResultSelected.getCollaborator());
                                    }

                                }
                            });
    }

    private void initToolbar() {
        toolbar.setHorizontalSpacing(5);
        addExplainPanel();
        toolbar.add(new UserSearchField(UserSearchResultSelected.USER_SEARCH_EVENT_TAG.SHARING).asWidget());
        toolbar.add(new FillToolItem());
        toolbar.add(buildChooseCollabButton());
    }

    private TextButton buildChooseCollabButton() {
        TextButton button = new TextButton();
        button.setText(appearance.chooseFromCollab());
        button.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                collaboratorsDialogProvider.get(new AsyncCallback<ManageCollaboratorsDialog>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }

                    @Override
                    public void onSuccess(ManageCollaboratorsDialog result) {
                        result.setModal(true);
                        result.show(ManageCollaboratorsView.MODE.SELECT);
                        result.addOkButtonSelectHandler(new SelectHandler() {

                            @Override
                            public void onSelect(SelectEvent event) {
                                List<Collaborator> selected = result.getSelectedCollaborators();
                                if (selected != null && selected.size() > 0) {
                                    for (Collaborator c : selected) {
                                        addCollaborator(c);
                                    }
                                }
                            }
                        });
                    }
                });
            }

        });
        button.setToolTip(appearance.chooseFromCollab());
        button.setIcon(appearance.shareIcon());
        return button;
    }

    private ComboBoxCell<PermissionValue> buildPermissionsCombo() {
        ListStore<PermissionValue> perms = new ListStore<>(new ModelKeyProvider<PermissionValue>() {

            @Override
            public String getKey(PermissionValue item) {
                return item.toString();
            }
        });
        perms.add(PermissionValue.read);
        perms.add(PermissionValue.write);
        perms.add(PermissionValue.own);

        final ComboBoxCell<PermissionValue> permCombo = new ComboBoxCell<>(perms,
                                                                           new StringLabelProvider<PermissionValue>() {
                                                                               @Override
                                                                               public String
                                                                                       getLabel(PermissionValue value) {
                                                                                   return value.toString();
                                                                               }
                                                                           });

        permCombo.setForceSelection(true);
        permCombo.setSelectOnFocus(true);

        permCombo.setTriggerAction(TriggerAction.ALL);
        permCombo.addSelectionHandler(new SelectionHandler<PermissionValue>() {

            @Override
            public void onSelection(SelectionEvent<PermissionValue> event) {
                PermissionValue perm = event.getSelectedItem();
                CellSelectionEvent<PermissionValue> sel = (CellSelectionEvent<PermissionValue>)event;
                Sharing ds = listStore.get(sel.getContext().getIndex());
                ds.setDisplayPermission(perm);
                updatePermissions(perm, ds.getUserName());
                listStore.update(ds);
            }
        });
        return permCombo;
    }

    private void addExplainPanel() {
        explainPanel = new HorizontalPanel();
        TextButton explainBtn = new TextButton(appearance.variablePermissionsNotice() + ":"
                + appearance.explain(), new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                ArrayList<Sharing> shares = new ArrayList<>();
                for (String user : sharingMap.keySet()) {
                    shares.addAll(sharingMap.get(user));
                }

                ShareBreakDownDialog explainDlg = new ShareBreakDownDialog(shares);
                explainDlg.setHeading(appearance.whoHasAccess());
                explainDlg.show();
            }
        });
        explainBtn.setIcon(appearance.helpIcon());
        explainPanel.add(explainBtn);
        toolbar.add(explainPanel);
    }

    private void addCollaborator(Collaborator user) {
        String userName = user.getUserName();
        if (userName != null && userName.equalsIgnoreCase(UserInfo.getInstance().getUsername())) {
            AlertMessageBox amb = new AlertMessageBox(appearance.warning(),
                                                      appearance.selfShareWarning());
            amb.show();
            return;
        }

        // Only add users not already displayed in the grid.
        if (sharingMap.get(userName) == null) {
            List<Sharing> shareList = new ArrayList<>();
            Sharing displayShare = null;

            for (String id : resources.keySet()) {
                final SharedResource resource = resources.get(id);
                Sharing share = new Sharing(user,
                                            presenter.getDefaultPermissions(),
                                            resource.getSystemId(),
                                            id,
                                            resource.getName());
                shareList.add(share);

                if (displayShare == null) {
                    displayShare = share.copy();
                    grid.getStore().add(displayShare);
                }
            }

            sharingMap.put(userName, shareList);
        }
    }

    private void removeModels(Sharing model) {
        ListStore<Sharing> store = grid.getStore();

        Sharing sharing = store.findModel(model);
        if (sharing != null) {
            // Remove the shares from the sharingMap as well as the grid.
            sharingMap.put(sharing.getUserName(), null);
            store.remove(sharing);
        }
    }

    public void loadSharingData(FastMap<List<Sharing>> sharingMap) {
        this.sharingMap = sharingMap;
        originalList = new FastMap<>();

        listStore.clear();
        setExplainPanelVisibility(false);

        for (String userName : sharingMap.keySet()) {
            List<Sharing> sharingList = sharingMap.get(userName);

            if (sharingList != null && !sharingList.isEmpty()) {
                List<Sharing> newList = new ArrayList<>();
                for (Sharing share : sharingList) {
                    Sharing copyShare = share.copy();
                    newList.add(copyShare);
                }
                originalList.put(userName, newList);

                // Add a dummy display share to the grid.
                Sharing displayShare = sharingList.get(0).copy();
                if (hasVaryingPermissions(sharingList)) {
                    // Set the display permission to "varies" if this user's share list has varying
                    // permissions.
                    displayShare.setDisplayPermission(PermissionValue.varies);
                    explainPanel.setVisible(true);
                }

                listStore.add(displayShare);
            }
        }
    }

    private ColumnModel<Sharing> buildColumnModel() {
        List<ColumnConfig<Sharing, ?>> configs = new ArrayList<>();
        DataSharingProperties props = GWT.create(DataSharingProperties.class);

        ColumnConfig<Sharing, String> name = new ColumnConfig<>(new ValueProvider<Sharing, String>() {
            @Override
            public String getValue(Sharing object) {
                return object.getCollaboratorName();
            }

            @Override
            public void setValue(Sharing object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        }, appearance.nameColumnWidth(), appearance.nameColumnLabel());
        ColumnConfig<Sharing, PermissionValue> permission = buildPermissionColumn(props);
        ColumnConfig<Sharing, String> remove = buildRemoveColumn();

        configs.add(name);
        configs.add(permission);
        configs.add(remove);

        return new ColumnModel<>(configs);
    }

    @Override
    public void hidePermissionColumn() {
      for(ColumnConfig<Sharing, ?> cc: grid.getColumnModel().getColumns()) {
          if(cc.getHeader().asString().equals(appearance.permissionsColumnLabel())) {
                cc.setHidden(true);
                return;
          }
      }
    }

    @Override
    public void showPermissionColumn() {
        for(ColumnConfig<Sharing, ?> cc: grid.getColumnModel().getColumns()) {
            if(cc.getHeader().asString().equals(appearance.permissionsColumnLabel())) {
                cc.setHidden(false);
                return;
            }
        }
    }

    @Override
    public void setExplainPanelVisibility(boolean visible) {
        explainPanel.setVisible(visible);
    }

    private ColumnConfig<Sharing, PermissionValue> buildPermissionColumn(DataSharingProperties props) {
        ColumnConfig<Sharing, PermissionValue> permission = new ColumnConfig<>(props.displayPermission(),
                                                                               appearance.permissionsColumnWidth(),
                                                                               appearance.permissionsColumnLabel());
        permission.setColumnTextStyle(appearance.permissionsColumnStyle());
        permission.setFixed(true);
        permission.setCell(buildPermissionsCombo());
        permission.setComparator(new Comparator<PermissionValue>() {

            @Override
            public int compare(PermissionValue o1, PermissionValue o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        return permission;
    }

    private ColumnConfig<Sharing, String> buildRemoveColumn() {
        ColumnConfig<Sharing, String> remove = new ColumnConfig<>(new ValueProvider<Sharing, String>() {

            @Override
            public String getValue(Sharing object) {
                return "";
            }

            @Override
            public void setValue(Sharing object, String value) {
                // do nothing

            }

            @Override
            public String getPath() {
                return "";
            }
        });

        remove.setColumnTextClassName(appearance.removeColumnTextClass());
        remove.setHeader("");
        remove.setSortable(false);
        remove.setFixed(true);
        remove.setColumnStyle(appearance.removeColumnStyle());
        remove.setWidth(appearance.removeColumnWidth());
        remove.setToolTip(appearance.unshare());
        TextButtonCell button = buildRemoveButtonCell();
        remove.setCell(button);
        return remove;
    }

    private TextButtonCell buildRemoveButtonCell() {
        TextButtonCell button = new TextButtonCell();
        button.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                removeModels(grid.getSelectionModel().getSelectedItem());
            }

        });
        button.setIcon(appearance.deleteIcon());
        return button;
    }

    /**
     * 
     * 
     * @return the sharing list
     */
    @Override
    public FastMap<List<Sharing>> getSharingMap() {
        FastMap<List<Sharing>> sharingList = new FastMap<>();
        for (Sharing share : grid.getStore().getAll()) {
            String userName = share.getUserName();
            List<Sharing> updatedSharingList = getUpdatedSharingList(userName, sharingMap.get(userName));
            if (updatedSharingList != null && updatedSharingList.size() > 0) {
                sharingList.put(userName, updatedSharingList);
            }
        }

        return sharingList;
    }

    /**
     * check the list with original to see if things have changed. ignore unchanged records
     */
    private List<Sharing> getUpdatedSharingList(String userName, List<Sharing> list) {
        List<Sharing> updateList = new ArrayList<>();
        if (list != null && userName != null) {
            List<Sharing> fromOriginal = originalList.get(userName);

            if (fromOriginal == null || fromOriginal.isEmpty()) {
                updateList = list;
            } else {
                for (Sharing s : list) {
                    if (!fromOriginal.contains(s)) {
                        updateList.add(s);
                    }
                }
            }
        }

        return updateList;
    }

    private void updatePermissions(PermissionValue perm, String username) {
        List<Sharing> models = sharingMap.get(username);
        if (models != null) {
            for (Sharing share : models) {
                share.setPermission(perm);
                share.setDisplayPermission(perm);
            }
            // Check if user does not have all resources shared yet, due to varying permissions/shares.
            if (resources.size() != models.size()) {
                Collaborator user = models.get(0).getCollaborator();
                for (String id : resources.keySet()) {
                    final SharedResource resource = resources.get(id);
                    final String systemId = resource.getSystemId();
                    boolean shared = false;

                    for (Sharing existingShare : models) {
                        shared = (id.equals(existingShare.getId())
                                  && systemId.equals(existingShare.getSystemId()));
                        if (shared) {
                            break;
                        }
                    }

                    if (!shared) {
                        models.add(new Sharing(user, perm, systemId, id, resource.getName()));
                    }
                }
            }

            checkExplainPanelVisibility();
        }
    }

    /**
     * Checks if the explainPanel should be hidden after permissions have been updated or removed.
     */
    private void checkExplainPanelVisibility() {
        if (explainPanel.isVisible()) {
            boolean permsVary = false;

            for (Sharing share : grid.getStore().getAll()) {
                permsVary = hasVaryingPermissions(sharingMap.get(share.getUserName()));

                if (permsVary) {
                    // Stop checking after the first user is found with variable permissions.
                    break;
                }
            }

            if (!permsVary) {
                setExplainPanelVisibility(false);
            }
        }
    }

    /**
     * @return true if the given sharingList list has a different size than the resources list, or if not
     *         every permission in the given sharingList list is the same; false otherwise.
     */
    private boolean hasVaryingPermissions(List<Sharing> sharingList) {
        if (sharingList == null || sharingList.size() != resources.size()) {
            return true;
        } else {
            PermissionValue displayPermission = sharingList.get(0).getDisplayPermission();

            for (Sharing share : sharingList) {
                if (!displayPermission.equals(share.getDisplayPermission())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return the unshareList
     */
    @Override
    public FastMap<List<Sharing>> getUnshareList() {
        // Prepare unshared list here
        FastMap<List<Sharing>> unshareList = new FastMap<>();

        for (String userName : originalList.keySet()) {
            if (sharingMap.get(userName) == null) {
                // The username entry from the original list was removed from the sharingMap, which means
                // it was unshared.
                List<Sharing> removeList = originalList.get(userName);

                if (removeList != null && !removeList.isEmpty()) {
                    unshareList.put(userName, removeList);
                }
            }
        }

        return unshareList;
    }

    @Override
    public void mask() {
        container.mask(appearance.loadingMask());
    }

    @Override
    public void unmask() {
        container.unmask();
    }

}
