/**
 * 
 * @author sriram
 */
package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.diskResource.client.GenomeSearchView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Window;

public class GenomeSearchDialog extends Window {

    private GenomeSearchView.GenomeSearchPresenter presenter;

    @Inject
    public GenomeSearchDialog(GenomeSearchView.GenomeSearchViewAppearance appearance,
                              GenomeSearchView.GenomeSearchPresenter presenter) {
        this.presenter = presenter;
        setSize(appearance.dialogWidth(), appearance.dialogHeight());
        setHeading(appearance.heading());
        setModal(true);

        presenter.go(this);
    }

    @Override
    public void show() {
        super.show();

        ensureDebugId(DiskResourceModule.Ids.GENOME_SEARCH_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
    }
}
