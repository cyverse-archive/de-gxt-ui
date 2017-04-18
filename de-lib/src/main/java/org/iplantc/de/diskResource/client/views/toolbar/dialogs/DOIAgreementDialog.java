package org.iplantc.de.diskResource.client.views.toolbar.dialogs;

import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.ToolbarView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;


/**
 * Created by sriram on 4/18/17.
 */
public class DOIAgreementDialog extends IPlantDialog {

    private ToolbarView.Appearance appearance;

    @Inject
    public DOIAgreementDialog(ToolbarView.Appearance appearance) {
        this.appearance = appearance;
        final TextButton okButton = getOkButton();
        setHeading(appearance.requestDOI());

        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        vlc.add(new HTML(appearance.doiLinkMsg()));

        CheckBox cxb = new CheckBox();
        cxb.setHTML(appearance.doiUserAgreement());
        cxb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                if (valueChangeEvent.getValue()) {
                    okButton.enable();
                } else {
                    okButton.disable();
                }
            }
        });

        vlc.add(cxb);
        okButton.setText(appearance.needDOI());
        okButton.disable();

        setSize("500px", "120px");
        add(vlc);
    }


}
