package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
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

        SafeHtml buildRequiredFieldLabel(String label);
    }

    private TextField projectTxtField;

    private IntegerField biosampNumField;

    private IntegerField libNumField;

    private final VerticalLayoutContainer vlc = new VerticalLayoutContainer();

    @Inject
    Appearance appearance;

    @Inject
    public CreateNcbiSraFolderStructureDialog(final Appearance appearance) {
        this.appearance = appearance;
        setWidth(appearance.dialogWidth());
        setHeading(appearance.ncbiSraProject());
        initFields();
        setWidget(vlc);
    }

    private void initFields() {
        projectTxtField = new TextField();
        projectTxtField.setAllowBlank(false);
        projectTxtField.addValidator(new DiskResourceNameValidator());

        biosampNumField = new IntegerField();
        biosampNumField.setAllowBlank(false);
        biosampNumField.setAllowNegative(false);

        libNumField = new IntegerField();
        libNumField.setAllowBlank(false);
        libNumField.setAllowNegative(false);
    }

    private void build(String path) {
        vlc.add(initDestPathLabel(path), new VerticalLayoutData(1, -1));

        FieldLabel biosampNameFieldLbl = new FieldLabel(projectTxtField,
                                                        appearance.buildRequiredFieldLabel(appearance.projectName()));
        biosampNameFieldLbl.setLabelAlign(FormPanel.LabelAlign.TOP);
        vlc.add(biosampNameFieldLbl, new VerticalLayoutData(1, -1));


        FieldLabel biosampNumFieldLbl = new FieldLabel(biosampNumField,
                                                       appearance.buildRequiredFieldLabel(appearance.numberOfBioSamples()));
        biosampNumFieldLbl.setLabelAlign(FormPanel.LabelAlign.TOP);
        vlc.add(biosampNumFieldLbl, new VerticalLayoutData(1, -1));

        FieldLabel libNumFieldLbl =
                new FieldLabel(libNumField, appearance.buildRequiredFieldLabel(appearance.numberOfLib()));
        libNumFieldLbl.setLabelAlign(FormPanel.LabelAlign.TOP);
        vlc.add(libNumFieldLbl, new VerticalLayoutData(1, -1));
    }

    private HTML initDestPathLabel(String destPath) {
        HTML htmlDestText = new HTML(appearance.renderDestinationPathLabel(destPath,
                                                                           DiskResourceUtil.getInstance()
                                                                                           .parseNameFromPath(destPath)));
        return htmlDestText;
    }

    public boolean validate() {
        return projectTxtField.isValid() && biosampNumField.isValid() && libNumField.isValid()
                && biosampNumField.getValue() > 0 && libNumField.getValue() > 0;
    }

    public String getProjectTxt() {
        return projectTxtField.getValue();
    }


    public Integer getBiosampNum() {
        return biosampNumField.getValue();
    }


    public Integer getLibNum() {
        return libNumField.getValue();
    }


    public void show(Folder parentFolder) {
        initDestPathLabel(parentFolder.getPath());
        build(parentFolder.getPath());
        super.show();
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("Use show(Folder parentFolder)");
    }
}
