package org.iplantc.de.teams.client.views;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.views.CollaboratorsColumnModel;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

import java.util.List;

/**
 * @author aramsey
 */
public class TeamDetailsView extends Composite implements Editor<Group> {

    interface EditorDriver extends SimpleBeanEditorDriver<Group, TeamDetailsView> {}
    interface MyUiBinder extends UiBinder<Widget, TeamDetailsView> {}
    private static MyUiBinder uiBinder = GWT.create(TeamDetailsView.MyUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @UiField @Ignore FieldLabel teamNameLabel;
    @UiField @Ignore FieldLabel teamDescLabel;
    @UiField TextField nameEditor;
    @UiField TextArea descriptionEditor;
    @UiField ListStore<Subject> listStore;
    @UiField Grid<Subject> grid;
    @UiField GridView<Group> gridView;
    @UiField ColumnModel<Subject> cm;
    @UiField(provided = true) TeamsView.TeamsViewAppearance appearance;

    @Inject
    public TeamDetailsView(TeamsView.TeamsViewAppearance appearance) {
        this.appearance = appearance;

        initWidget(uiBinder.createAndBindUi(this));

        editorDriver.initialize(this);

        gridView.setEmptyText(appearance.detailsGridEmptyText());
    }

    public void edit(Group group) {
        editorDriver.edit(group);
    }

    @UiFactory
    ListStore<Subject> createListStore() {
        return new ListStore<>(HasId::getId);
    }

    @UiFactory
    ColumnModel<Subject> createColumnModel() {
        return new CollaboratorsColumnModel(null);
    }

    public void addMembers(List<Subject> members) {
        listStore.addAll(members);
    }
}
