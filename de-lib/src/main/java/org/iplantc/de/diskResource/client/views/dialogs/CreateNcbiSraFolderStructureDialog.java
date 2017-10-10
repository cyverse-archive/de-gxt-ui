package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class CreateNcbiSraFolderStructureDialog extends IPlantDialog {

    public interface Appearance {
        String dialogWidth();

        String projectName();

        String numberOfBioSamples();

        String numberOfLib();

        String ncbiSraProject();

        SafeHtml renderDestinationPathLabel(String destPath, String createIn);
    }

    private final Appearance appearance;
    private DiskResourceUtil diskResourceUtil;

    private TextField projectTxtField;

    private IntegerField bioSampNumField;

    private IntegerField libNumField;

    private HTML htmlDestText;

    private VerticalLayoutContainer vlc;

    @Inject
    public CreateNcbiSraFolderStructureDialog(final Appearance appearance,
                                              DiskResourceUtil diskResourceUtil) {
        this.appearance = appearance;
        this.diskResourceUtil = diskResourceUtil;
        setHideOnButtonClick(false);
        setWidth(appearance.dialogWidth());
        setHeading(appearance.ncbiSraProject());
        initFields();
        setWidget(vlc);
    }

    private void initFields() {
        projectTxtField = new TextField();
        projectTxtField.setAllowBlank(false);
        projectTxtField.addValidator(new DiskResourceNameValidator());

        bioSampNumField = new IntegerField();
        bioSampNumField.setAllowBlank(false);
        bioSampNumField.setAllowNegative(false);

        libNumField = new IntegerField();
        libNumField.setAllowBlank(false);
        libNumField.setAllowNegative(false);

        htmlDestText = new HTML();
        vlc = new VerticalLayoutContainer();
        vlc.add(htmlDestText, new VerticalLayoutData(1, -1));
        vlc.add(new FieldLabel(projectTxtField, appearance.projectName()), new VerticalLayoutData(1, -1));
        vlc.add(new FieldLabel(bioSampNumField, appearance.numberOfBioSamples()),
                new VerticalLayoutData(1, -1));
        vlc.add(new FieldLabel(libNumField, appearance.numberOfLib()), new VerticalLayoutData(1, -1));
    }

    public void show(Folder parentFolder) {
        String destPath = parentFolder.getPath();
        htmlDestText = new HTML(appearance.renderDestinationPathLabel(destPath,
                                                                      diskResourceUtil.parseNameFromPath(destPath)));
        super.show();

        ensureDebugId(DiskResourceModule.Ids.CREATE_NCBI_STRUCTURE_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);
        projectTxtField.ensureDebugId(baseID + DiskResourceModule.Ids.NCBI_PROJECT_NAME);
        bioSampNumField.ensureDebugId(baseID + DiskResourceModule.Ids.NCBI_SAMPLE_NUM);
        libNumField.ensureDebugId(baseID + DiskResourceModule.Ids.NCBI_LIB_NUM);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported. Use 'show(Folder)' instead.");
    }

    public boolean isValid() {
        return projectTxtField.isValid() && bioSampNumField.isValid() && libNumField.isValid()
               && bioSampNumField.getValue() > 0 && libNumField.getValue() > 0;
    }

    public String getProjectTxt() {
        return projectTxtField.getValue();
    }


    public Integer getBioSampNum() {
        return bioSampNumField.getValue();
    }


    public Integer getLibNum() {
        return libNumField.getValue();
    }


}
