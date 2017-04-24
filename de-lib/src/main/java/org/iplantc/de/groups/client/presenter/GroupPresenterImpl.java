package org.iplantc.de.groups.client.presenter;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.groups.client.GroupView;
import org.iplantc.de.groups.client.model.GroupProperties;

import com.google.gwt.user.client.ui.HasOneWidget;

import com.sencha.gxt.data.shared.ListStore;

import javax.inject.Inject;

/**
 * @author aramsey
 */
public class GroupPresenterImpl implements GroupView.GroupPresenter {

    private final GroupAutoBeanFactory factory;
    private final GroupView.GroupViewAppearance appearance;
    private GroupServiceFacade serviceFacade;
    private final ListStore<Group> listStore;

    @Inject IplantAnnouncer announcer;

    @Inject
    public GroupPresenterImpl(GroupAutoBeanFactory factory,
                              GroupProperties properties,
                              GroupView.GroupViewAppearance appearance,
                              GroupServiceFacade serviceFacade) {
        this.listStore = createListStore(properties);
        this.factory = factory;
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
    }

    @Override
    public void go(HasOneWidget container) {
    }

    @Override
    public void setViewDebugId(String baseId) {

    }



    ListStore<Group> createListStore(GroupProperties properties) {
        final ListStore<Group> listStore = new ListStore<Group>(properties.id());
        listStore.setEnableFilters(true);
        return listStore;
    }

}
