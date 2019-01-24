package org.iplantc.de.apps.client.views.communities;

import static com.sencha.gxt.core.client.Style.SelectionMode.SINGLE;

import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.apps.client.models.CommunityStringValueProvider;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aramsey
 */
public class CommunitiesViewImpl extends ContentPanel implements CommunitiesView,
                                                                 SelectionChangedEvent.SelectionChangedHandler<Group> {

    interface CommunitiesViewImplUiBinder extends UiBinder<Tree<Group, String>, CommunitiesViewImpl> {
    }
    @UiField Tree<Group, String> tree;
    Appearance appearance;

    private static final CommunitiesViewImplUiBinder ourUiBinder = GWT.create(CommunitiesViewImplUiBinder.class);
    private final TreeStore<Group> treeStore;

    @Inject
    CommunitiesViewImpl(final Appearance appearance,
                        @Assisted final TreeStore<Group> treeStore) {
        this.appearance = appearance;
        this.treeStore = treeStore;
        setWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public Tree<Group, String> getTree() {
        return tree;
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Group> event) {
        List<Group> communities = event.getSelection();
        if(!communities.isEmpty()) {
            fireEvent(new CommunitySelectionChangedEvent(event.getSelection().get(0),
                                                         getCommunityPath(communities)));
        }
    }

    List<String> getCommunityPath(List<Group> communities) {
        // Assuming no sub-communities for now
        return communities.stream().map(Subject::getGroupShortName).collect(Collectors.toList());
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        tree.ensureDebugId(baseID + AppsModule.Ids.COMMUNITIES_TREE);
    }

    @UiFactory
    Tree<Group, String> createTree() {
        final Tree<Group, String> tree = new Tree<>(treeStore, new CommunityStringValueProvider());
        // Set tree icons
        appearance.setTreeIcons(tree.getStyle());
        tree.getSelectionModel().setSelectionMode(SINGLE);
        tree.getSelectionModel().addSelectionChangedHandler(this);

        return tree;
    }

    @Override
    public HandlerRegistration addCommunitySelectedEventHandler(CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler handler) {
        return addHandler(handler, CommunitySelectionChangedEvent.TYPE);
    }
}
