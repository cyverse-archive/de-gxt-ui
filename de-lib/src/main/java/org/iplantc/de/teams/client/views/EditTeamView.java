package org.iplantc.de.teams.client.views;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Composite;

public class EditTeamView extends Composite implements Editor<Group> {

    interface EditorDriver extends SimpleBeanEditorDriver<Group, EditTeamView> {
    }

    interface MyUiBinder extends UiBinder<Widget, EditTeamView> {
    }

    private static MyUiBinder uiBinder = GWT.create(EditTeamView.MyUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(EditTeamView.EditorDriver.class);
    @UiField(provided = true) TeamsView.TeamsViewAppearance appearance;

    @Inject
    public EditTeamView(TeamsView.TeamsViewAppearance appearance) {

        this.appearance = appearance;

        initWidget(uiBinder.createAndBindUi(this));
        editorDriver.initialize(this);
    }

    public void edit(Group group) {
        editorDriver.edit(group);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
    }
}
