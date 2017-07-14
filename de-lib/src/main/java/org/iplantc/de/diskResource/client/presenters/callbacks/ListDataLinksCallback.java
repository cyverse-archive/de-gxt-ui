package org.iplantc.de.diskResource.client.presenters.callbacks;

import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author jstroot
 */
public class ListDataLinksCallback<M> extends DataCallback<FastMap<List<DataLink>>> {

    private final Tree<M, M> tree;
    private final DiskResourceCallbackAppearance appearance = GWT.create(DiskResourceCallbackAppearance.class);

    public ListDataLinksCallback(final Tree<M, M> tree) {
        this.tree = tree;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(FastMap<List<DataLink>> result) {
        // Get tickets by resource id, add them to the tree.
        // Select all roots automatically
        result.keySet().forEach(key -> {
            M dr = null;
            // manually find the item since id's wont work
            for (M item : tree.getStore().getAll()) {
                if (((DiskResource)item).getPath().equals(key)) {
                    dr = item;
                    break;
                }
            }
            List<DataLink> dlLIst = result.get(key);
            for (DataLink dl: dlLIst) {
               tree.getStore().add(dr, (M)dl);
               tree.setChecked((M)dl, Tree.CheckState.CHECKED);
            }

        });
        tree.setCheckedSelection(tree.getStore().getAll());
        for (M m : tree.getStore().getAll()) {
            tree.setExpanded(m, true);
        }
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        ErrorHandler.post(appearance.listDataLinksError(), caught);
    }
}
