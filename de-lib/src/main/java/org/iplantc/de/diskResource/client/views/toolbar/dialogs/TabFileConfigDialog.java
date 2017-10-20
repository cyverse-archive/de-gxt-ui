package org.iplantc.de.diskResource.client.views.toolbar.dialogs;

import static com.sencha.gxt.widget.core.client.Dialog.PredefinedButton.CANCEL;
import static com.sencha.gxt.widget.core.client.Dialog.PredefinedButton.OK;

import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.SpinnerField;

/**
 * @author jstroot
 */
public class TabFileConfigDialog extends IPlantDialog {

    public interface TabFileConfigDialogAppearance {

        String heading();

        String commaRadioLabel();

        String tabRadioLabel();

        String createDelimitedFileDialogWidth();

        String createDelimitedFileDialogHeight();
    }

    private static final TabFileConfigViewUiBinder uiBinder = GWT.create(TabFileConfigViewUiBinder.class);

    @UiTemplate("TabFileConfigView.ui.xml")
    interface TabFileConfigViewUiBinder extends UiBinder<Widget, TabFileConfigDialog> {
    }

    @UiField SpinnerField<Integer> columnsSpinner;
    @UiField Radio commaRadio;
    @UiField Radio tabRadio;
    @UiField(provided = true) final TabFileConfigDialogAppearance appearance;

    private Widget widget;

    @Inject
    public TabFileConfigDialog(final TabFileConfigDialogAppearance appearance) {
        this.appearance = appearance;
        initWidget();
        setWidget(widget);
        setPredefinedButtons(OK, CANCEL);
        setModal(true);
        setHideOnButtonClick(true);
        setSize(appearance.createDelimitedFileDialogWidth(),
                appearance.createDelimitedFileDialogHeight());
    }

    private void initWidget() {
        this.widget = uiBinder.createAndBindUi(this);
        setHeading(appearance.heading());
        columnsSpinner.setMinValue(1);
        columnsSpinner.setValue(1, true);
        ToggleGroup toggle = new ToggleGroup();
        toggle.add(commaRadio);
        toggle.add(tabRadio);
    }

    @UiFactory
    SpinnerField<Integer> buildSpinnerField() {
        return new SpinnerField<>(new NumberPropertyEditor.IntegerPropertyEditor());
    }

    public int getNumberOfColumns() {
        return columnsSpinner.getValue();
    }

    public String getSeparator() {
        if (commaRadio.getValue()) {
            return ",";
        } else {
            return "\t";
        }
    }

}
