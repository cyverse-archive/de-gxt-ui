package org.iplantc.de.admin.desktop.client.metadata.view;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;

import java.util.ArrayList;
import java.util.List;

public class OntologyLookupServiceParamListEditor extends Composite implements IsEditor<Editor<List<String>>> {
    interface OntologyLookupServiceParamOntologiesEditorUiBinder
            extends UiBinder<Widget, OntologyLookupServiceParamListEditor> {
    }

    private static OntologyLookupServiceParamOntologiesEditorUiBinder uiBinder =
            GWT.create(OntologyLookupServiceParamOntologiesEditorUiBinder.class);

    @Ignore
    @UiField FieldSet header;
    @Ignore
    @UiField FieldLabel helpLabel;
    @UiField TextButton addBtn;
    @UiField TextButton delBtn;
    @UiField Grid<String> grid;
    @UiField ListStore<String> listStore;

    private GridInlineEditing<String> editing;
    private ListStoreEditor<String> listStoreEditor;

    @UiConstructor
    public OntologyLookupServiceParamListEditor(String fieldLabel, String helpLabel, String columnLabel) {
        initWidget(uiBinder.createAndBindUi(this));

        header.setHeading(fieldLabel);
        this.helpLabel.setContent(helpLabel);

        listStoreEditor = new ListStoreEditor<>(listStore);
        editing = new GridInlineEditing<>(grid);

        final ColumnConfig<String, String> columnConfig = grid.getColumnModel().getColumn(0);
        columnConfig.setHeader(columnLabel);

        final TextField editor = new TextField();
        editing.addEditor(columnConfig, editor);

        grid.getView().setAutoExpandColumn(columnConfig);
        grid.setHeight(80);
        grid.getSelectionModel().addSelectionChangedHandler(event -> {
            List<String> selection = event.getSelection();
            delBtn.setEnabled(!selection.isEmpty());
        });
    }

    @Override
    public Editor<List<String>> asEditor() {
        return listStoreEditor;
    }

    @UiFactory
    ListStore<String> buildListStore() {
        ListStore<String> listStore = new ListStore<>(item -> item);
        listStore.setAutoCommit(true);

        return listStore;
    }

    @UiFactory
    ColumnModel<String> buildColumnModel() {
        ColumnConfig<String, String> columnConfig = new ColumnConfig<>(new ValueProvider<String, String>() {
            @Override
            public String getValue(String object) {
                return object;
            }

            @Override
            public void setValue(String object, String value) {
                // Remove the old value and add the new value
                listStore.remove(object);

                // Don't allow blank values to be added
                if (!Strings.isNullOrEmpty(value)) {
                    listStore.add(value);
                }
            }

            @Override
            public String getPath() {
                return null;
            }
        });

        List<ColumnConfig<String, ?>> columnConfigList = new ArrayList<>();
        columnConfigList.add(columnConfig);

        return new ColumnModel<>(columnConfigList);
    }

    @UiHandler("addBtn")
    public void addClicked(SelectEvent event) {
        String value = "";

        editing.cancelEditing();

        listStore.add(0, value);
        int row = listStore.indexOf(value);
        editing.startEditing(new Grid.GridCell(row, 0));
    }

    @UiHandler("delBtn")
    public void deleteClicked(SelectEvent event) {
        String value = grid.getSelectionModel().getSelectedItem();
        if (value != null) {
            listStore.remove(listStore.findModelWithKey(value));
        }
    }
}
