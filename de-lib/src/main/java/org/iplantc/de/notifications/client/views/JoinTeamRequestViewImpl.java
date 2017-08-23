/**
 *
 */
package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.notifications.shared.Notifications;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

/**
 * The view to allow a team admin to approve or deny a join request from another user
 *
 * @author aramsey
 */
public class JoinTeamRequestViewImpl extends Composite implements Editor<PayloadTeam>,
                                                                  JoinTeamRequestView {

    interface JoinTeamRequestViewImplUiBinder extends UiBinder<Widget, JoinTeamRequestViewImpl> {
    }

    interface EditorDriver extends SimpleBeanEditorDriver<PayloadTeam, JoinTeamRequestViewImpl> {}

    private static JoinTeamRequestViewImplUiBinder uiBinder = GWT.create(JoinTeamRequestViewImplUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @UiField @Ignore VerticalLayoutContainer layoutContainer;
    @UiField @Ignore HTML joinRequestIntro;
    @UiField @Ignore ButtonBar buttonBar;
    @UiField @Ignore FieldLabel teamLabel;
    @UiField Label teamNameEditor;
    @UiField @Ignore FieldLabel nameLabel;
    @UiField Label requesterNameEditor;
    @UiField @Ignore FieldLabel emailLabel;
    @UiField Label requesterEmailEditor;
    @UiField @Ignore FieldLabel messageLabel;
    @UiField Label requesterMessageEditor;
    @UiField @Ignore TextButton approveBtn;
    @UiField @Ignore TextButton rejectBtn;
    @UiField(provided = true) JoinTeamRequestAppearance appearance;

    @Inject
    public JoinTeamRequestViewImpl(JoinTeamRequestAppearance appearance) {
        this.appearance = appearance;
        initWidget(uiBinder.createAndBindUi(this));

        editorDriver.initialize(this);
    }

    @Override
    public void edit(PayloadTeam payloadTeam) {
        editorDriver.edit(payloadTeam);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        approveBtn.ensureDebugId(baseID + Notifications.JoinRequestIds.APPROVE_BTN);
        rejectBtn.ensureDebugId(baseID + Notifications.JoinRequestIds.REJECT_BTN);
    }
}
