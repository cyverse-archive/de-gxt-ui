/**
 *
 */
package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

import java.util.List;

/**
 * @author sriram, jstroot
 */
public class InfoTypeEditorDialog extends IPlantDialog {

    private final SimpleComboBox<InfoType> infoTypeCbo;
    private GridView.Appearance appearance;

    @Inject
    InfoTypeEditorDialog(final GridView.Appearance appearance) {
        this.appearance = appearance;
        setSize(appearance.infoTypeDialogWidth(), appearance.infoTypeDialogHeight());
        setHeading(appearance.infoTypeDialogHeader());
        infoTypeCbo = new SimpleComboBox<>(InfoType::toString);
        infoTypeCbo.setAllowBlank(true);
        infoTypeCbo.setEmptyText(appearance.infoTypeEmptyText());
        infoTypeCbo.setTriggerAction(TriggerAction.ALL);
        infoTypeCbo.setEditable(false);
        add(infoTypeCbo);

    }

    public InfoType getSelectedValue() {
        return infoTypeCbo.getCurrentValue();
    }

    public void addInfoTypes(List<InfoType> infoTypeList) {
        infoTypeCbo.add(infoTypeList);
    }

    public void setCurrentInfoType(InfoType currentType) {
        infoTypeCbo.setValue(currentType);
    }

    @Override
    public void show() {
        super.show();

        ensureDebugId(DiskResourceModule.Ids.INFO_TYPE_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        infoTypeCbo.ensureDebugId(baseID + DiskResourceModule.Ids.INFO_TYPE_DROPDOWN);
    }
}
