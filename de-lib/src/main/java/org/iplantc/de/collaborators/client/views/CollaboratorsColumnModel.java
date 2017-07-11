package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.models.SubjectNameComparator;
import org.iplantc.de.collaborators.client.models.SubjectProperties;
import org.iplantc.de.collaborators.client.views.cells.SubjectNameCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jstroot
 */
public class CollaboratorsColumnModel extends ColumnModel<Subject> implements GroupNameSelected.HasGroupNameSelectedHandlers {

    private final ManageCollaboratorsView.Appearance appearance;
    private HandlerManager handlerManager;

    public CollaboratorsColumnModel(final CheckBoxSelectionModel<Subject> checkBoxModel) {
        this(checkBoxModel,
             true,
             GWT.<SubjectProperties> create(SubjectProperties.class),
             GWT.<ManageCollaboratorsView.Appearance> create(ManageCollaboratorsView.Appearance.class));
    }

    public CollaboratorsColumnModel(final CheckBoxSelectionModel<Subject> checkBoxModel,
                                    boolean clickableGroupName) {
        this(checkBoxModel,
             clickableGroupName,
             GWT.<SubjectProperties> create(SubjectProperties.class),
             GWT.<ManageCollaboratorsView.Appearance> create(ManageCollaboratorsView.Appearance.class));
    }

    public CollaboratorsColumnModel(final CheckBoxSelectionModel<Subject> checkBoxModel,
                                    boolean clickableGroupName,
                                    final SubjectProperties properties,
                                    final ManageCollaboratorsView.Appearance appearance) {
        super(createColumnConfigList(checkBoxModel,
                                     clickableGroupName,
                                     properties,
                                     appearance));
        for (ColumnConfig<Subject, ?> cc :configs) {
            if (cc.getCell() instanceof SubjectNameCell) {
                ((SubjectNameCell)cc.getCell()).setHasHandlers(ensureHandlers());
            }
        }
        this.appearance = appearance;
    }

    static List<ColumnConfig<Subject, ?>> createColumnConfigList(final CheckBoxSelectionModel<Subject> checkBoxModel,
                                                                 boolean clickableGroupName,
                                                                 final SubjectProperties properties,
                                                                 final ManageCollaboratorsView.Appearance appearance) {

        List<ColumnConfig<Subject, ?>> configs = new ArrayList<>();

        if (checkBoxModel != null) {
            ColumnConfig<Subject, Subject> colCheckBox = checkBoxModel.getColumn();
            configs.add(colCheckBox);
        }

        ColumnConfig<Subject, Subject> name = new ColumnConfig<>(new IdentityValueProvider<Subject>("firstname"),
                                                                 150);
        name.setHeader(appearance.nameHeader());
        name.setCell(new SubjectNameCell(clickableGroupName));

        name.setComparator(new SubjectNameComparator());
        configs.add(name);

        ColumnConfig<Subject, String> ins = new ColumnConfig<>(properties.institution(),
                                                               150);
        ins.setHeader(appearance.institutionOrDescriptionHeader());
        configs.add(ins);

        return configs;

    }

    @Override
    public HandlerRegistration addGroupNameSelectedHandler(GroupNameSelected.GroupNameSelectedHandler handler) {
        return ensureHandlers().addHandler(GroupNameSelected.TYPE, handler);
    }

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
}
