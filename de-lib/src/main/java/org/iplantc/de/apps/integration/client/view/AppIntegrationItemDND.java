package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.ArgumentType;
import org.iplantc.de.client.models.apps.integration.DataSourceEnum;
import org.iplantc.de.client.models.apps.integration.FileInfoTypeEnum;
import org.iplantc.de.client.models.apps.integration.FileParameters;
import org.iplantc.de.client.models.apps.integration.SelectionItem;
import org.iplantc.de.client.models.apps.integration.SelectionItemGroup;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsDefaultLabels;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Image;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * @author aramsey
 */
public class AppIntegrationItemDND extends DragSource {

    private AppsWidgetsDefaultLabels defaultLabels;
    private AppIntegrationPalette view;
    private AppTemplateWizardAppearance appearance;
    private AppTemplateUtils appTemplateUtils;
    private AppTemplateAutoBeanFactory factory;
    private Image widget;
    private ArgumentType type;

    public AppIntegrationItemDND(AppIntegrationPalette view,
                                 Image widget,
                                 ArgumentType type) {
        this((AppTemplateWizardAppearance)GWT.create(AppTemplateWizardAppearance.class),
             (AppTemplateUtils)GWT.create(AppTemplateUtils.class),
             (AppTemplateAutoBeanFactory)GWT.create(AppTemplateAutoBeanFactory.class),
             (AppsWidgetsDefaultLabels)GWT.create(AppsWidgetsDefaultLabels.class),
             view,
             widget,
             type);
    }

    public AppIntegrationItemDND(AppTemplateWizardAppearance appearance,
                                 AppTemplateUtils appTemplateUtils,
                                 AppTemplateAutoBeanFactory factory,
                                 AppsWidgetsDefaultLabels defaultLabels,
                                 AppIntegrationPalette view,
                                 Image widget,
                                 ArgumentType type) {
        super(widget);
        this.defaultLabels = defaultLabels;
        this.view = view;
        this.appearance = appearance;
        this.appTemplateUtils = appTemplateUtils;
        this.factory = factory;
        this.widget = widget;
        this.type = type;

        addDragStartHandler(new DndDragStartEvent.DndDragStartHandler() {

            @Override
            public void onDragStart(DndDragStartEvent event) {
                if (view.getOnlyLabelEditMode() && !type.equals(ArgumentType.Info)) {
                    event.getStatusProxy().setStatus(false);
                    event.getStatusProxy().update((SafeHtml)() -> "This item cannot be added to a published app.");
                    return;
                }

                event.getStatusProxy().setStatus(true);
                event.getStatusProxy().update((SafeHtml)() -> widget.getElement().getString());
                event.setData(createNewArgument(type));
            }
        });

        if (GXT.isGecko()) {
            widget.addMouseDownHandler(new MouseDownHandler() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    widget.addStyleName(appearance.grabbingClassName());
                }
            });
            widget.addMouseUpHandler(new MouseUpHandler() {
                @Override
                public void onMouseUp(MouseUpEvent event) {
                    widget.removeStyleName(appearance.grabbingClassName());
                }
            });
        }
    }


    Argument createNewArgument(ArgumentType type) {
        AutoBean<Argument> argAb = factory.argument();
        // JDS Annotate as a newly created autobean.
        argAb.setTag(Argument.IS_NEW, "--");

        Argument argument = argAb.as();
        argument.setLabel("DEFAULT");
        argument.setDescription("");
        argument.setType(type);
        argument.setName("");
        argument.setVisible(true);
        argument.setRequired(false);
        argument.setOmitIfBlank(false);

        if (appTemplateUtils.isSimpleSelectionArgumentType(type)) {
            argument.setSelectionItems(Lists.<SelectionItem> newArrayList());
        } else if (type.equals(ArgumentType.TreeSelection)) {
            SelectionItemGroup
                    sig = appTemplateUtils.addSelectionItemAutoBeanIdTag(factory.selectionItemGroup().as(), "rootId");

            sig.setSingleSelect(false);
            sig.setSelectionCascade(Tree.CheckCascade.CHILDREN);
            sig.setArguments(Lists.<SelectionItem> newArrayList());
            sig.setGroups(Lists.<SelectionItemGroup> newArrayList());
            argument.setSelectionItems(Lists.<SelectionItem> newArrayList(sig));

        } else if (appTemplateUtils.isDiskResourceArgumentType(type) || appTemplateUtils.isDiskResourceOutputType(type)) {
            FileParameters dataObj = factory.fileParameters().as();
            dataObj.setFormat("Unspecified");
            dataObj.setDataSource(DataSourceEnum.file);
            dataObj.setFileInfoType(FileInfoTypeEnum.File);
            argument.setFileParameters(dataObj);

        }
        // Special handling to initialize new arguments, for specific ArgumentTypes.
        switch (type) {
            case TextSelection:
                argument.setLabel(defaultLabels.defTextSelection());
                break;
            case IntegerSelection:
                argument.setLabel(defaultLabels.defIntegerSelection());
                break;
            case DoubleSelection:
                argument.setLabel(defaultLabels.defDoubleSelection());
                break;

            case TreeSelection:
                argument.setLabel(defaultLabels.defTreeSelection());
                break;

            case FileInput:
                argument.setLabel(defaultLabels.defFileInput());
                break;

            case FolderInput:
                argument.setLabel(defaultLabels.defFolderInput());
                break;

            case MultiFileSelector:
                argument.setLabel(defaultLabels.defMultiFileSelector());
                break;

            case Flag:
                argument.setLabel(defaultLabels.defCheckBox());
                break;

            case Text:
                argument.setLabel(defaultLabels.defTextInput());
                break;

            case MultiLineText:
                argument.setLabel(defaultLabels.defMultiLineText());
                break;

            case EnvironmentVariable:
                argument.setLabel(defaultLabels.defEnvVar());
                break;

            case Integer:
                argument.setLabel(defaultLabels.defIntegerInput());
                break;

            case Double:
                argument.setLabel(defaultLabels.defDoubleInput());
                break;

            case FileOutput:
                argument.setLabel(defaultLabels.defFileOutput());
                break;

            case FolderOutput:
                argument.setLabel(defaultLabels.defFolderOutput());
                break;

            case MultiFileOutput:
                argument.setLabel(defaultLabels.defMultiFileOutput());
                break;

            case ReferenceAnnotation:
                argument.setLabel(defaultLabels.defReferenceAnnotation());
                break;

            case ReferenceGenome:
                argument.setLabel(defaultLabels.defReferenceGenome());
                break;

            case ReferenceSequence:
                argument.setLabel(defaultLabels.defReferenceSequence());
                break;

            case Info:
                argument.setLabel(defaultLabels.defInfo());
                break;

            default:
                argument.setLabel(defaultLabels.defaultLabel());
                break;
        }
        return argument;
    }
}
