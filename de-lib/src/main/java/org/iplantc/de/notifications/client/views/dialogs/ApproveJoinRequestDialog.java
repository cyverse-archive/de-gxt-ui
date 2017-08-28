package org.iplantc.de.notifications.client.views.dialogs;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.shared.Notifications;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

import java.util.List;

/**
 * A dialog that allows the user approving a join team request to give the requester privileges on the team
 */
public class ApproveJoinRequestDialog extends MessageBox implements IsHideable {

    private JoinTeamRequestView.JoinTeamRequestAppearance appearance;
    SimpleComboBox<PrivilegeType> privilegeCombo;

    @Inject
    public ApproveJoinRequestDialog(JoinTeamRequestView.JoinTeamRequestAppearance appearance) {
        super(appearance.setPrivilegesHeading());
        this.appearance = appearance;

        setWidth(appearance.privilegeDlgWidth());
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        privilegeCombo = new SimpleComboBox<>(createPrivilegeComboBox());
    }

    public void show(String requesterName, String teamName) {
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(new Label(appearance.setPrivilegesText(requesterName, teamName)));
        container.add(privilegeCombo);
        privilegeCombo.setValue(PrivilegeType.read);
        privilegeCombo.setWidth(appearance.privilegeComboWidth());

        add(container);

        super.show();
        ensureDebugId(Notifications.JoinRequestIds.SET_PRIVILEGE_DLG);
    }

    public PrivilegeType getPrivilegeType() {
        return privilegeCombo.getCurrentValue();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).ensureDebugId(baseID + Notifications.JoinRequestIds.OK_BTN);
        getButton(PredefinedButton.CANCEL).ensureDebugId(baseID + Notifications.JoinRequestIds.CANCEL_BTN);
    }

    ComboBoxCell<PrivilegeType> createPrivilegeComboBox() {
        ListStore<PrivilegeType> comboListStore = new ListStore<>(PrivilegeType::getLabel);
        List<PrivilegeType> types = Lists.newArrayList(PrivilegeType.admin,
                                                       PrivilegeType.readOptin,
                                                       PrivilegeType.read,
                                                       PrivilegeType.optin,
                                                       PrivilegeType.view);
        comboListStore.addAll(types);
        ComboBoxCell<PrivilegeType> combo = new ComboBoxCell<>(comboListStore, PrivilegeType::getLabel);
        combo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        combo.setForceSelection(true);
        combo.setAllowBlank(false);
        return combo;
    }
}
