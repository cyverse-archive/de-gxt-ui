package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.SelectedHierarchyNotFound;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * @author aramsey
 */
public interface OntologyHierarchiesView extends IsWidget,
                                                 IsMaskable,
                                                 OntologyHierarchySelectionChangedEvent.HasOntologyHierarchySelectionChangedEventHandlers {

    interface OntologyHierarchiesAppearance extends AppCategoriesView.AppCategoriesAppearance {

        String hierarchyLabelName(OntologyHierarchy hierarchy);

        String ontologyAttrMatchingFailure();
    }

    interface Presenter extends AppInfoSelectedEvent.AppInfoSelectedEventHandler,
                                AppSearchResultLoadEvent.AppSearchResultLoadEventHandler,
                                OntologyHierarchySelectionChangedEvent.HasOntologyHierarchySelectionChangedEventHandlers,
                                SelectedHierarchyNotFound.HasSelectedHierarchyNotFoundHandlers {

        void go(OntologyHierarchy selectedHierarchy, DETabPanel tabPanel);

        void setViewDebugId(String baseID);

        OntologyHierarchy getSelectedHierarchy();
    }

    Tree<OntologyHierarchy, String> getTree();

    void setRoot(OntologyHierarchy hierarchy);

    OntologyHierarchy getRoot();

}
