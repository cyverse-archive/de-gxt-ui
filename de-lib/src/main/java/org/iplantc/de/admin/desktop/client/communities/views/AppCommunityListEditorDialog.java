package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.ontologies.events.CategorizeHierarchiesToAppEvent;
import org.iplantc.de.admin.desktop.client.ontologies.views.AppCategorizeView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

/**
 * @author aramsey
 *
 * A dialog that shows the AppCategorizeView for communities
 */
public class AppCommunityListEditorDialog extends IPlantDialog {

    private AdminCommunitiesView.Appearance appearance;
    private AppCategorizeView categorizeView;

    @Inject
    public AppCommunityListEditorDialog(AdminCommunitiesView.Appearance appearance,
                                        @Named(AppCategorizeView.COMMUNITIES) AppCategorizeView categorizeView) {
        super(true);

        this.appearance = appearance;
        this.categorizeView = categorizeView;

        setHideOnButtonClick(false);
        setResizable(true);
        setUpButtons();

        setOnEsc(false);

        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.add(categorizeView);
        add(con);
    }

    public void show(final App targetApp,
                     List<Group> allCommunities,
                     List<Avu> selectedAvus) {
        setHeading(appearance.selectCommunitiesFor(targetApp));
        categorizeView.mask(appearance.loadingMask());
        categorizeView.setItems(allCommunities);
        markTaggedCommunities(selectedAvus, allCommunities);
        categorizeView.unmask();

        super.show();
    }

    public List<Group> getSelectedCommunities() {
        return categorizeView.getSelectedItems();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported. Use 'show(App)' instead.");
    }

    private void setUpButtons() {
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        getOkButton().setText(appearance.categorize());
        addOkButtonSelectHandler(event -> {
            hide();
        });
        addCancelButtonSelectHandler(event -> hide());
    }

    void markTaggedCommunities(List<Avu> selectedAvus, List<Group> allCommunities) {
        List<Group> selectedCommunities = Lists.newArrayList();
        for (Avu avu: selectedAvus) {
            Group selectedCommunity = allCommunities.stream().filter(community -> avu.getValue().equals(community.getDisplayName())).findFirst().get();
            selectedCommunities.add(selectedCommunity);
        }
        categorizeView.setSelectedItems(selectedCommunities);
    }
}
