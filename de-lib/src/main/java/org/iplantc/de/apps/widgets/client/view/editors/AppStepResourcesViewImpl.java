package org.iplantc.de.apps.widgets.client.view.editors;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.AppTemplateStepLimits;
import org.iplantc.de.client.models.apps.integration.AppTemplateStepRequirements;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * @author psarando
 */
public class AppStepResourcesViewImpl extends ContentPanel implements AppStepResourcesView, Editor<AppTemplateStepRequirements> {

    private static final long ONE_GB = 1024 * 1024 * 1024;

    @Path("minCPUCores")
    @UiField (provided = true)
    SimpleComboBox<Double> minCPUCoresEditor;

    @Path("minMemoryLimit")
    @UiField (provided = true)
    SimpleComboBox<Long> memory;

    @Path("minDiskSpace")
    @UiField (provided = true)
    SimpleComboBox<Long> minDiskSpaceEditor;

    @UiField(provided = true)
    AppTemplateWizardAppearance appearance;

    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplateStepRequirements, AppStepResourcesViewImpl> {
    }

    private final AppStepResourcesViewImpl.EditorDriver editorDriver =
            GWT.create(AppStepResourcesViewImpl.EditorDriver.class);


    @UiTemplate("AppStepResourcesView.ui.xml")
    interface AppStepResourcesViewUiBinder extends UiBinder<Widget, AppStepResourcesViewImpl> {

    }

    private static final AppStepResourcesViewUiBinder uiBinder = GWT.create(AppStepResourcesViewUiBinder.class);

    @Inject
    public AppStepResourcesViewImpl(DEProperties deProperties,
                                    AppTemplateWizardAppearance appearance,
                                    AppTemplateAutoBeanFactory factory,
                                    @Assisted AppTemplateStepLimits limits) {
        this.appearance = appearance;

        buildResourceLists(limits, deProperties);

        setWidget(uiBinder.createAndBindUi(this));
        editorDriver.initialize(this);
        editorDriver.edit(buildStepRequirements(limits, factory));
    }

    private void buildResourceLists(AppTemplateStepLimits limits, DEProperties deProperties) {
        minDiskSpaceEditor = createDataSizeComboBox();
        memory = createDataSizeComboBox();
        minCPUCoresEditor = createDoubleComboBox();

        final Long minDiskSpace = limits.getMinDiskSpace();
        final long diskSpaceEditorMin = (minDiskSpace != null && minDiskSpace > 0) ? minDiskSpace : ONE_GB;
        buildResourceSizeLimitList(minDiskSpaceEditor, ONE_GB, diskSpaceEditorMin, deProperties.getToolsMaxDiskLimit());

        final Long memoryLimit = limits.getMemoryLimit();
        final Long minMemoryLimit = limits.getMinMemoryLimit();
        final long memoryEditorMin = minMemoryLimit != null ? minMemoryLimit : 2 * ONE_GB;
        final long memoryEditorMax =
                (memoryLimit != null && memoryLimit > 0) ? memoryLimit : deProperties.getToolsMaxMemLimit();
        buildResourceSizeLimitList(memory, 2 * ONE_GB, memoryEditorMin, memoryEditorMax);

        final Double maxCPUCores = limits.getMaxCPUCores();
        final Double minCPUCores = limits.getMinCPUCores();

        final double cpuEditorMin = (minCPUCores != null && minCPUCores > 0) ? minCPUCores : 1;
        final double cpuEditorMax =
                (maxCPUCores != null && maxCPUCores > 0) ? maxCPUCores : deProperties.getToolsMaxCPULimit();
        buildResourceDoubleLimitList(minCPUCoresEditor, 1, cpuEditorMin, cpuEditorMax);
    }

    private void buildResourceSizeLimitList(SimpleComboBox<Long> limitSelectionList,
                                            long startingLimit,
                                            long minLimit,
                                            long maxLimit) {
        limitSelectionList.add((long)0);

        long resourceLimit = startingLimit;
        while (resourceLimit <= maxLimit) {
            if (resourceLimit >= minLimit) {
                limitSelectionList.add(resourceLimit);
            }

            resourceLimit *= 2;
        }
    }

    private void buildResourceDoubleLimitList(SimpleComboBox<Double> limitSelectionList,
                                              double startingLimit,
                                              double minLimit,
                                              double maxLimit) {
        limitSelectionList.add((double)0);

        double resourceLimit = startingLimit;
        while (resourceLimit <= maxLimit) {
            if (resourceLimit >= minLimit) {
                limitSelectionList.add(resourceLimit);
            }

            resourceLimit *= 2;
        }
    }

    private SimpleComboBox<Long> createDataSizeComboBox() {
        // This ComboBox's LabelProvider does some int -> float math conversions
        // to ensure we display partial GB values with 0.1 precision.
        final SimpleComboBox<Long> resourceSizeSimpleComboBox =
                new SimpleComboBox<>(size -> (float)Math.round((float)(size * 10) / ONE_GB) / 10 + " GiB");

        resourceSizeSimpleComboBox.setEditable(false);
        resourceSizeSimpleComboBox.setAllowBlank(true);
        resourceSizeSimpleComboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        return resourceSizeSimpleComboBox;
    }

    private SimpleComboBox<Double> createDoubleComboBox() {
        final SimpleComboBox<Double> resourceSizeSimpleComboBox = new SimpleComboBox<>(Object::toString);

        resourceSizeSimpleComboBox.setEditable(false);
        resourceSizeSimpleComboBox.setAllowBlank(true);
        resourceSizeSimpleComboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        return resourceSizeSimpleComboBox;
    }

    private AppTemplateStepRequirements buildStepRequirements(AppTemplateStepLimits limits,
                                                              AppTemplateAutoBeanFactory factory) {
        final Splittable json = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(limits));
        final AppTemplateStepRequirements requirements =
                AutoBeanCodex.decode(factory, AppTemplateStepRequirements.class, json).as();

        // check for default settings
        final Long defaultDiskSpace = limits.getDefaultDiskSpace();
        if (defaultDiskSpace != null && defaultDiskSpace > 0) {
            requirements.setMinDiskSpace(defaultDiskSpace);
        }
        final Long defaultMemory = limits.getDefaultMemory();
        if (defaultMemory != null && defaultMemory > 0) {
            requirements.setMinMemoryLimit(defaultMemory);
        }
        final Double defaultCPUCores = limits.getDefaultCPUCores();
        if (defaultCPUCores != null && defaultCPUCores > 0) {
            requirements.setMinCPUCores(defaultCPUCores);
        }

        return requirements;
    }

    @Override
    public AppTemplateStepRequirements getRequirements() {
        return editorDriver.flush();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        memory.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_MEM);
        minCPUCoresEditor.setId(baseID + ToolsModule.EditToolIds.TOOL_CPU);
        minDiskSpaceEditor.setId(baseID + ToolsModule.EditToolIds.MIN_DISK_SPACE);
    }
}
