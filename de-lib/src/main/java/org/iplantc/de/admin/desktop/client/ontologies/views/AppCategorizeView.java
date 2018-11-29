package org.iplantc.de.admin.desktop.client.ontologies.views;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author jstroot, aramsey
 */
public interface AppCategorizeView<T> extends IsWidget {

    String HIERARCHIES = "hierarchies";
    String COMMUNITIES = "communities";

    void setItems(List<T> items);

    List<T> getSelectedItems();

    void setSelectedItems(List<T> items);

    void mask(String loadingMask);

    void unmask();

}
