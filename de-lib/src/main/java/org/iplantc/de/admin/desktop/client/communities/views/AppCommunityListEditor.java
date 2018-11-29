package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.ontologies.views.AppCategorizeView;
import org.iplantc.de.apps.client.presenter.communities.GroupComparator;
import org.iplantc.de.client.models.groups.Group;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author aramsey
 */
public class AppCommunityListEditor implements AppCategorizeView<Group> {

    interface AppCommunityListEditorUiBinder extends UiBinder<Widget, AppCommunityListEditor> {
    }

    private static AppCommunityListEditorUiBinder uiBinder = GWT.create(AppCommunityListEditorUiBinder.class);

    @UiField(provided = true) TreeStore<Group> treeStore;
    @UiField(provided = true) Tree<Group, String> tree;
    @UiField ContentPanel con;
    @UiField(provided = true) AdminCommunitiesView.Appearance appearance = GWT.create(AdminCommunitiesView.Appearance.class);

    final private Widget widget;

    @Inject
    public AppCommunityListEditor(final TreeStore<Group> treeStore) {
        this.treeStore = treeStore;
        initCategoryTree();
        widget = uiBinder.createAndBindUi(this);

        addClearButton();
    }

    private void addClearButton() {
        TextButton btnClear = new TextButton(appearance.clearCommunitySelection(),
                                             (SelectHandler)event -> tree.setCheckedSelection(null));

        con.getHeader().addTool(btnClear);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private void initCategoryTree() {
        initTreeStoreSorter();

        tree = new Tree<>(treeStore, new ValueProvider<Group, String>() {

            @Override
            public String getValue(Group object) {
                return object.getName();
            }

            @Override
            public void setValue(Group object, String value) {
                // do nothing intentionally
            }

            @Override
            public String getPath() {
                return "name"; //$NON-NLS-1$
            }
        });

        appearance.setTreeIcons(tree.getStyle());
        tree.setCheckable(true);
        tree.setCheckStyle(Tree.CheckCascade.NONE);
    }

    private void initTreeStoreSorter() {
        treeStore.addSortInfo(new StoreSortInfo<>(new GroupComparator(), SortDir.ASC));
    }

    @Override
    public void setItems(List<Group> communities) {
        treeStore.clear();
        treeStore.add(communities);
    }

    @Override
    public void setSelectedItems(List<Group> communities) {
        List<Group> selection = Lists.newArrayList();
        for (Group community : communities) {
            Group model = treeStore.findModel(community);
            if (model != null) {
                selection.add(model);
            }
        }

        tree.setCheckedSelection(selection);
    }

    @Override
    public void mask(String loadingMask) {
        tree.mask(loadingMask);
    }

    @Override
    public void unmask() {
        tree.unmask();
    }

    @Override
    public List<Group> getSelectedItems() {
        if (tree.isCheckable()) {
            return tree.getCheckedSelection();
        }
        return tree.getSelectionModel().getSelection();
    }
}
