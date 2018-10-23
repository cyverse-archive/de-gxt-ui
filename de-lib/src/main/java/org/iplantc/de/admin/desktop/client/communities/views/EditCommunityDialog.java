package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.validators.GroupNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * A dialog that can be used to create or edit a community
 */
public class EditCommunityDialog extends IPlantDialog {

    TextField name, description;
    FieldLabel nameLabel, descriptionLabel;
    private String originalName;
    private AdminCommunitiesView.Appearance appearance;

    @Inject
    public EditCommunityDialog(AdminCommunitiesView.Appearance appearance) {
        this.appearance = appearance;
        initFields();
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
    }

    void initFields() {
        name = new TextField();
        description = new TextField();
        nameLabel = new FieldLabel();
        descriptionLabel = new FieldLabel();

        nameLabel.setText(appearance.name());
        name.setAllowBlank(false);
        name.addValidator(new GroupNameValidator());
        descriptionLabel.setText(appearance.description());

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(nameLabel);
        container.add(name);
        container.add(descriptionLabel);
        container.add(description);

        add(container);
    }

    @Override
    protected void onButtonPressed(TextButton button) {
        if (button == getButton(PredefinedButton.OK)) {
            if (name.isValid()) {
                super.onButtonPressed(button);
            }
        } else {
            super.onButtonPressed(button);
        }
    }

    public void show(Group community) {
        if (community != null) {
            originalName = community.getName();
            name.setValue(community.getName());
            description.setValue(community.getDescription());
            setHeading(appearance.editCommunity());
        } else {
            setHeading(appearance.addCommunity());
        }
        super.show();
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getName() {
        return name.getCurrentValue();
    }

    public String getDescription() {
        return description.getCurrentValue();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "This method is not supported. Use 'show(Group)' instead.");
    }
}
