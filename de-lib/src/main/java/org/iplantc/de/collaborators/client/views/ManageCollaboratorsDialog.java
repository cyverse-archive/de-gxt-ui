package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.collaborators.client.views.ManageCollaboratorsView.MODE;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.gwt.user.client.ui.HTML;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import java.util.List;
import javax.inject.Inject;

/**
 * @author sriram, jstroot
 * 
 */
public class ManageCollaboratorsDialog extends IPlantDialog {


    private final ManageCollaboratorsView.Presenter presenter;
    private final ManageCollaboratorsView.Appearance appearance;


    @Inject
    ManageCollaboratorsDialog(final ManageCollaboratorsView.Appearance appearance,
                              ManageCollaboratorsView.Presenter presenter) {
        super(true);
        this.appearance = appearance;
        this.presenter = presenter;
        initDialog();
    }

    public void show(MODE mode) {
        presenter.go(this, mode);

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
        setHeading(I18N.DISPLAY.collaborators());
        addHelp(new HTML(I18N.HELP.collaboratorsHelp()));
        setPixelSize(450, 400);
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

    @Override
    protected void onHide() {
        presenter.cleanup();
        super.onHide();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "This method is not supported. Use show(MODE mode) method instead.");
    }

    public List<Collaborator> getSelectedCollaborators() {
        return presenter.getSelectedCollaborators();
    }
}
