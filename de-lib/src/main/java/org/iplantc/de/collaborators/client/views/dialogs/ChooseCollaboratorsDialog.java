package org.iplantc.de.collaborators.client.views.dialogs;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.user.client.ui.HTML;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import java.util.List;
import javax.inject.Inject;

/**
 * @author sriram, jstroot
 * 
 */
public class ChooseCollaboratorsDialog extends IPlantDialog {


    private final CollaborationView.Presenter presenter;
    private final ManageCollaboratorsView.Appearance appearance;


    @Inject
    ChooseCollaboratorsDialog(final ManageCollaboratorsView.Appearance appearance,
                              CollaborationView.Presenter presenter) {
        super(true);
        this.appearance = appearance;
        this.presenter = presenter;
        initDialog();
    }

    @Override
    public void show() {
        presenter.go(this, null);

        super.show();
        ensureDebugId(CollaboratorsModule.Ids.DIALOG);

    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        getWidget().ensureDebugId(baseID + CollaboratorsModule.Ids.VIEW);
        getOkButton().ensureDebugId(baseID + CollaboratorsModule.Ids.OK);
    }

    private void initDialog() {
        setPredefinedButtons(PredefinedButton.OK);
        setHeading(appearance.collaborators());
        addHelp(new HTML(appearance.collaboratorsHelp()));
        setPixelSize(appearance.chooseCollaboratorsWidth(), appearance.chooseCollaboratorsHeight());
        addOkButtonHandler();
        setHideOnButtonClick(true);
    }

    private void addOkButtonHandler() {
        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });
    }

    public List<Subject> getSelectedSubjects() {
        return null;
    }
}
