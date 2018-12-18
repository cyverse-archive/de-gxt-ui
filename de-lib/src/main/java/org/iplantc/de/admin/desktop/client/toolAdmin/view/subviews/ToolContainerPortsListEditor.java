package org.iplantc.de.admin.desktop.client.toolAdmin.view.subviews;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolContainerPortProperties;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainerPort;
import org.iplantc.de.client.models.tool.ToolVolumesFrom;
import org.iplantc.de.commons.client.validators.UrlValidator;
import org.iplantc.de.commons.client.widgets.EmptyStringValueChangeHandler;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.AbstractGridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

import java.util.ArrayList;
import java.util.List;

public class ToolContainerPortsListEditor extends Composite implements IsEditor<Editor<List<ToolContainerPort>>> {

    private GridEditing<ToolContainerPort> editing;
    private Grid<ToolContainerPort> grid;
    private static final String TOOL_CONTAINER_PORT_MODEL_KEY = "model_key";
    private int unique_volumes_from_id;
    private ListStoreEditor<ToolContainerPort> listStoreEditor;

    @UiField ListStore<ToolContainerPort> listStore;
    @UiField(provided = true) ToolAdminView.ToolAdminViewAppearance appearance;


    @Inject
    public ToolContainerPortsListEditor(ToolAdminView.ToolAdminViewAppearance appearance,
                                        ToolContainerPortProperties toolContainerPortProperties) {
        this.appearance = appearance;
        listStore = new ListStore<>(this::getContainerPortTag);
        listStoreEditor = new ListStoreEditor<>(listStore);
        listStore.setAutoCommit(true);

        ColumnConfig<ToolContainerPort, Integer> containerPort = new ColumnConfig<>(toolContainerPortProperties.containerPort(),
                                                                              appearance.containerPortWidth(),
                                                                              appearance.containerPortLabel());
        ColumnConfig<ToolContainerPort, Integer> hostPort = new ColumnConfig<>(toolContainerPortProperties.hostPort(),
                                                                               appearance.containerHostPortWidth(),
                                                                               appearance.containerHostPortLabel());
        ColumnConfig<ToolContainerPort, Boolean> bindToHost = new ColumnConfig<>(toolContainerPortProperties.bindToHost(),
                                                                       appearance.containerBindToHostWidth(),
                                                                       appearance.containerBindToHostLabel());

        List<ColumnConfig<ToolContainerPort, ?>> columns = new ArrayList<>();
        columns.add(containerPort);
        columns.add(hostPort);
        columns.add(bindToHost);
        ColumnModel<ToolContainerPort> cm = new ColumnModel<>(columns);

        grid = new Grid<>(listStore, cm);
        grid.setHeight(100);

        editing = new GridRowEditing<>(grid);
        enableGridEditing(containerPort, hostPort, bindToHost);
        editing.addCancelEditHandler(getCancelHandler());
        ((AbstractGridEditing<ToolContainerPort>)editing).setClicksToEdit(ClicksToEdit.TWO);

        initWidget(grid);
    }

    private CancelEditEvent.CancelEditHandler<ToolContainerPort> getCancelHandler() {
        return event -> {
            int cancelRow = event.getEditCell().getRow();
            if (listStore.get(cancelRow).getHostPort() == null &&
                listStore.get(cancelRow).getContainerPort() == null) {
                listStore.remove(cancelRow);
            }
        };
    }

    private void enableGridEditing(ColumnConfig<ToolContainerPort, Integer> containerPort,
                                   ColumnConfig<ToolContainerPort, Integer> hostPort,
                                   ColumnConfig<ToolContainerPort, Boolean> bindToHost) {
        final IntegerField containerPortField = new IntegerField();
        containerPortField.setAllowBlank(false);
        editing.addEditor(containerPort, containerPortField);

        final IntegerField hostPortField = new IntegerField();
        editing.addEditor(hostPort, hostPortField);

        final CheckBox bindToHostField = new CheckBox();
        editing.addEditor(bindToHost, bindToHostField);
    }

    @Override
    public Editor<List<ToolContainerPort>> asEditor() {
        return listStoreEditor;
    }

    public void addContainerPorts() {
        ToolAutoBeanFactory factory = GWT.create(ToolAutoBeanFactory.class);
        ToolContainerPort containerPort = factory.getToolContainerPort().as();
        getContainerPortTag(containerPort);

        editing.cancelEditing();
        listStore.add(0, containerPort);
        int row = listStore.indexOf(containerPort);
        editing.startEditing(new Grid.GridCell(row, 0));
    }

    private String getContainerPortTag(ToolContainerPort containerPort){
        if (containerPort != null){
            final AutoBean<ToolContainerPort> containerPortAB = AutoBeanUtils.getAutoBean(containerPort);
            String currentTag = containerPortAB.getTag(TOOL_CONTAINER_PORT_MODEL_KEY);
            if (currentTag == null){
                containerPortAB.setTag(TOOL_CONTAINER_PORT_MODEL_KEY, String.valueOf(unique_volumes_from_id++));
            }
            return containerPortAB.getTag(TOOL_CONTAINER_PORT_MODEL_KEY);
        }
        return "";
    }

    public void deleteContainerPorts() {
        ToolContainerPort containerPort = grid.getSelectionModel().getSelectedItem();
        if (containerPort != null) {
            listStore.remove(listStore.findModelWithKey(getContainerPortTag(containerPort)));
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        grid.ensureDebugId(baseID + Belphegor.ToolAdminIds.CONTAINER_PORTS_GRID);
    }
}
