package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.models.SubjectNameComparator;
import org.iplantc.de.collaborators.client.models.SubjectProperties;
import org.iplantc.de.collaborators.client.views.cells.SubjectNameCell;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jstroot
 */
public class CollaboratorsColumnModel extends ColumnModel<Subject> {

    private final ManageCollaboratorsView.Appearance appearance;

    public CollaboratorsColumnModel(final CheckBoxSelectionModel<Subject> checkBoxModel) {
        this(checkBoxModel,
             GWT.<SubjectProperties> create(SubjectProperties.class),
             GWT.<ManageCollaboratorsView.Appearance> create(ManageCollaboratorsView.Appearance.class));
    }

    public CollaboratorsColumnModel(final CheckBoxSelectionModel<Subject> checkBoxModel,
                                    final SubjectProperties properties,
                                    final ManageCollaboratorsView.Appearance appearance) {
        super(createColumnConfigList(checkBoxModel,
                                     properties,
                                     appearance));
        this.appearance = appearance;
    }

    static List<ColumnConfig<Subject, ?>> createColumnConfigList(final CheckBoxSelectionModel<Subject> checkBoxModel,
                                                                 final SubjectProperties properties,
                                                                 final ManageCollaboratorsView.Appearance appearance) {

        List<ColumnConfig<Subject, ?>> configs = new ArrayList<>();

        ColumnConfig<Subject, Subject> colCheckBox = checkBoxModel.getColumn();
        configs.add(colCheckBox);

        ColumnConfig<Subject, Subject> name = new ColumnConfig<>(new IdentityValueProvider<Subject>("firstname"),
                                                                 150);
        name.setHeader(I18N.DISPLAY.name());
        name.setCell(new SubjectNameCell());

        name.setComparator(new SubjectNameComparator());
        configs.add(name);

        ColumnConfig<Subject, String> ins = new ColumnConfig<>(properties.institution(),
                                                               150);
        ins.setHeader(I18N.DISPLAY.institution());
        configs.add(ins);

        return configs;

    }
}
