/**
 * 
 * @author sriram
 */
package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.diskResource.client.GenomeSearchView;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Window;

public class GenomeSearchDialog extends Window {

    @Inject
    public GenomeSearchDialog(GenomeSearchView.GenomeSearchViewAppearance appearance,
                              GenomeSearchView.GenomeSearchPresenter presenter) {
        setSize(appearance.dialogWidth(), appearance.dialogHeight());
        setHeading(appearance.heading());
        setModal(true);

        presenter.go(this);
    }
}
