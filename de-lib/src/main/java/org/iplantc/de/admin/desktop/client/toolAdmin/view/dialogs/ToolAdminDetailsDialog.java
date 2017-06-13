package org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.PublishToolEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.SaveToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.ToolAdminDetailsView;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolDevice;
import org.iplantc.de.client.models.tool.ToolImage;
import org.iplantc.de.client.models.tool.ToolImplementation;
import org.iplantc.de.client.models.tool.ToolTestData;
import org.iplantc.de.client.models.tool.ToolVolume;
import org.iplantc.de.client.models.tool.ToolVolumesFrom;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author jstroot
 * @author aramsey
 * @author sriram 
 */
public class ToolAdminDetailsDialog extends IPlantDialog implements IsHideable,
                                                                    SaveToolSelectedEvent.HasSaveToolSelectedEventHandlers,
                                                                    PublishToolEvent.HasPublishToolEventHandlers {

    private final ToolAdminDetailsView view;
    private final ToolAdminView.ToolAdminViewAppearance appearance;
    private Mode mode;

    @Inject
    ToolAutoBeanFactory factory;

    public enum Mode {
        EDIT, MAKEPUBLIC;
    }

    @Inject
    public ToolAdminDetailsDialog(final ToolAdminDetailsView view,
                                  final ToolAdminView.ToolAdminViewAppearance appearance) {
        super(true);
        this.view = view;
        this.appearance = appearance;

        setHideOnButtonClick(false);
        setHeading(appearance.dialogWindowName());
        setResizable(true);
        setPixelSize(1000, 500);
        setMinHeight(200);
        setMinWidth(500);

        setOnEsc(false);

        addHelp(new HTML(appearance.toolAdminHelp()));

        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        addCancelButtonSelectHandler(new CancelSelectHandler());
        FlowLayoutContainer container = new FlowLayoutContainer();
        container.getScrollSupport().setScrollMode(ScrollSupport.ScrollMode.AUTO);
        container.add(this.view);
        add(container);
    }

    public void show(final Tool tool, Mode mode) {
        this.mode = mode;
        addOkButtonSelectHandler(new OkSelectHandler(this, this, this.appearance, this.view, mode));

        switch (mode) {
            case MAKEPUBLIC:
                getOkButton().setText(appearance.dialogMakePublicText());
                break;
            case EDIT:
                getOkButton().setText(appearance.dialogWindowUpdateBtnText());
                break;
        }
        view.edit(tool);
        super.show();

        ensureDebugId(Belphegor.ToolAdminIds.TOOL_ADMIN_DIALOG);
    }

    @Override
    public void show() {
        final ToolContainer toolContainer = factory.getContainer().as();
        toolContainer.setDeviceList(Lists.<ToolDevice>newArrayList());
        toolContainer.setContainerVolumes(Lists.<ToolVolume>newArrayList());
        toolContainer.setContainerVolumesFrom(Lists.<ToolVolumesFrom>newArrayList());
        ToolImage image = factory.getImage().as();
        image.setName(appearance.defaultImgPrefix());
        toolContainer.setImage(image);

        final ToolImplementation toolImplementation = factory.getImplementation().as();
        final ToolTestData toolTestData = factory.getTest().as();
        toolTestData.setInputFiles(Lists.<String>newArrayList());
        toolTestData.setOutputFiles(Lists.<String>newArrayList());
        toolImplementation.setTest(toolTestData);

        final Tool tool = factory.getTool().as();
        tool.setType(appearance.toolImportTypeDefaultValue());
        tool.setContainer(toolContainer);
        tool.setImplementation(toolImplementation);

        show(tool, Mode.EDIT);
    }

    @Override
    public HandlerRegistration addSaveToolSelectedEventHandler(SaveToolSelectedEvent.SaveToolSelectedEventHandler handler) {
        return addHandler(handler, SaveToolSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addPublishToolEventHandler(PublishToolEvent.PublishToolEventHandler handler) {
        return addHandler(handler, PublishToolEvent.TYPE);
    }

    private static class OkSelectHandler implements SelectEvent.SelectHandler {
        private final IsHideable hideable;
        private final HasHandlers hasHandlers;
        private ToolAdminView.ToolAdminViewAppearance appearance;
        private ToolAdminDetailsView view;
        private Mode mode;


        private OkSelectHandler(IsHideable hideable,
                                HasHandlers hasHandlers,
                                ToolAdminView.ToolAdminViewAppearance appearance,
                                ToolAdminDetailsView view,
                                Mode mode) {
            this.hideable = hideable;
            this.hasHandlers = hasHandlers;
            this.appearance = appearance;
            this.view = view;
            this.mode = mode;
        }

        @Override
        public void onSelect(SelectEvent event) {
            if (view.isValid()) {
                switch (mode) {
                    case EDIT:
                        SaveToolSelectedEvent saveToolSelectedEvent =
                                new SaveToolSelectedEvent(view.getTool());
                        hasHandlers.fireEvent(saveToolSelectedEvent);
                        break;
                    case MAKEPUBLIC:
                        PublishToolEvent publishEvent = new PublishToolEvent(view.getTool());
                        hasHandlers.fireEvent(publishEvent);
                        break;
                }

                hideable.hide();
            } else {
                AlertMessageBox alertMsgBox =
                        new AlertMessageBox("Warning", appearance.completeRequiredFieldsError());
                alertMsgBox.show();
            }
        }
    }

    private class CancelSelectHandler implements SelectEvent.SelectHandler {
        @Override
        public void onSelect(SelectEvent event) {
            hide();
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getOkButton().ensureDebugId(baseID + Belphegor.ToolAdminIds.SAVE);

        view.ensureDebugId(baseID + Belphegor.ToolAdminIds.DETAILS_VIEW);
    }
}
