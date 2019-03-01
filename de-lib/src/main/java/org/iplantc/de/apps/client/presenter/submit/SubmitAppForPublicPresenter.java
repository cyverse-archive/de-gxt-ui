package org.iplantc.de.apps.client.presenter.submit;

import org.iplantc.de.apps.client.SubmitAppForPublicUseView;
import org.iplantc.de.apps.client.events.AppPublishedEvent;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppRefLink;
import org.iplantc.de.client.models.apps.PublishAppRequest;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.avu.AvuAutoBeanFactory;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.OntologyServiceFacade;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.DEProperties;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jstroot
 */
public class SubmitAppForPublicPresenter implements SubmitAppForPublicUseView.Presenter {

    interface SubmitAppPresenterBeanFactory extends AutoBeanFactory {
        AutoBean<AppRefLink> appRefLink();
    }

    private class HierarchiesCallback extends AppsCallback<List<OntologyHierarchy>> {
        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            ErrorHandler.post(appearance.publishFailureDefaultMessage(), caught);
        }

        @Override
        public void onSuccess(List<OntologyHierarchy> result) {
            addHierarchies(view.getCategoryTree().getStore(), null, result);
        }

        void addHierarchies(TreeStore<OntologyHierarchy> treeStore, OntologyHierarchy parent, List<OntologyHierarchy> children) {
            if ((children == null)
                || children.isEmpty()) {
                return;
            }
            if (parent == null) {
                treeStore.add(children);

            } else {
                treeStore.add(parent, children);
            }

            helperMap(children);

            for (OntologyHierarchy hierarchy : children) {
                addHierarchies(treeStore, hierarchy, hierarchy.getSubclasses());
            }
        }

        void helperMap(List<OntologyHierarchy> children) {
            for (OntologyHierarchy hierarchy : children) {
                String iri = hierarchy.getIri();
                List<OntologyHierarchy> hierarchies = iriToHierarchyMap.get(iri);
                if (hierarchies == null) {
                    hierarchies = Lists.newArrayList();
                }
                hierarchies.add(hierarchy);
                iriToHierarchyMap.put(hierarchy.getIri(), hierarchies);
            }
        }
    }

    @Inject AppUserServiceFacade appService;
    @Inject SubmitAppForPublicUseView.SubmitAppAppearance appearance;
    @Inject EventBus eventBus;
    @Inject SubmitAppPresenterBeanFactory factory;
    @Inject AppAutoBeanFactory appAutoBeanFactory;
    @Inject GroupAutoBeanFactory groupAutoBeanFactory;
    @Inject AvuAutoBeanFactory avuAutoBeanFactory;
    @Inject SubmitAppForPublicUseView view;
    private OntologyServiceFacade ontologyService;
    private GroupServiceFacade groupServiceFacade;
    private OntologyUtil ontologyUtil;
    private DEProperties properties;
    private AsyncCallback<String> callback;
    private Map<String, List<OntologyHierarchy>> iriToHierarchyMap = new FastMap<>();

    @Inject
    SubmitAppForPublicPresenter(OntologyServiceFacade ontologyService,
                                GroupServiceFacade groupServiceFacade,
                                OntologyUtil ontologyUtil,
                                DEProperties properties) {
        this.ontologyService = ontologyService;
        this.groupServiceFacade = groupServiceFacade;
        this.ontologyUtil = ontologyUtil;
        this.properties = properties;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        // Fetch Hierarchies
        ontologyService.getRootHierarchies(new HierarchiesCallback());
        // Fetch communities
        groupServiceFacade.getCommunities(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(appearance.publishFailureDefaultMessage(), caught);
            }

            @Override
            public void onSuccess(Splittable result) {
                List<Group> groupList = AutoBeanCodex.decode(groupAutoBeanFactory, GroupList.class, result).as().getGroups();
                TreeStore<Group> treeStore = view.getCommunityTree().getStore();
                treeStore.add(groupList);
            }
        });
    }

    @Override
    public void go(HasOneWidget container, App selectedApp, AsyncCallback<String> callback) {
        view.setSelectedApp(selectedApp);
        this.callback = callback;
        getAppDetails();
        go(container);
    }

    @Override
    public void onSubmit() {
        if (view.validate()) {
            publishApp(getPublishAppRequest());
        } else {
            AlertMessageBox amb = new AlertMessageBox(appearance.warning(),
                                                      appearance.completeRequiredFieldsError());
            amb.show();
        }
    }

    PublishAppRequest getPublishAppRequest() {
        PublishAppRequest appRequest = appAutoBeanFactory.publishAppRequest().as();
        App selectedApp = view.getSelectedApp();

        appRequest.setSystemId(selectedApp.getSystemId());
        appRequest.setId(selectedApp.getId());
        appRequest.setName(view.getAppName());
        appRequest.setDescription(view.getAppDescription());
        appRequest.setAvus(getAppAvus());
        appRequest.setReferences(view.getReferenceLinks());
        appRequest.setDocumentation(view.getMarkDownDocs());

        return appRequest;
    }

    private List<Avu> getAppAvus() {
        List<Avu> avus = Lists.newArrayList();
        for (OntologyHierarchy model : view.getCategoryTree().getCheckedSelection()) {
            avus.add(ontologyUtil.convertHierarchyToAvu(model));
        }
        for (Group community: view.getCommunityTree().getCheckedSelection()) {
            Avu avu = avuAutoBeanFactory.getAvu().as();
            avu.setAttribute(properties.getCommunityAttr());
            avu.setValue(community.getDisplayName());
            avu.setUnit("");

            avus.add(avu);
        }
        return avus;
    }

    private void getAppDetails() {
        appService.getAppDetails(view.getSelectedApp(), new AppsCallback<App>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(appearance.publishFailureDefaultMessage(), caught);
            }

            @Override
            public void onSuccess(App result) {
                view.loadReferences(parseRefLinks(result.getReferences()));
                List<Tool> toolsLst = result.getTools();
                List<Tool> interactiveList = toolsLst.stream()
                                                     .filter(t -> t.getType()
                                                                   .equalsIgnoreCase(AppTypeFilter.INTERACTIVE
                                                                                             .getFilterString()))
                                                     .collect(Collectors.toList());
                view.setIsInteractive(interactiveList != null && interactiveList.size() > 0);
            }
        });
    }

    private List<AppRefLink> parseRefLinks(List<String> arr) {
        List<AppRefLink> linksList = Lists.newArrayList();
        for (String ref : arr) {
            AppRefLink refLink = factory.appRefLink().as();
            refLink.setId(ref);
            refLink.setRefLink(ref);
            linksList.add(refLink);
        }

        return linksList;
    }

    private void publishApp(final PublishAppRequest publishAppRequest) {
        final AutoProgressMessageBox pmb = new AutoProgressMessageBox(appearance.submitForPublicUse(),
                                                                      appearance.submitRequest());
        pmb.setProgressText(appearance.submitting());
        pmb.setClosable(false);
        pmb.getProgressBar().setInterval(100);
        pmb.auto();
        pmb.show();

        appService.publishToWorld(publishAppRequest, new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                pmb.hide();
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(Void result) {
                pmb.hide();
                eventBus.fireEvent(new AppPublishedEvent(view.getSelectedApp()));
                if (callback != null) {
                    callback.onSuccess(publishAppRequest.getName());
                }
            }
        });
    }

}
