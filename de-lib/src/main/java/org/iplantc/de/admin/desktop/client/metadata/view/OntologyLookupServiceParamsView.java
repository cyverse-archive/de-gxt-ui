package org.iplantc.de.admin.desktop.client.metadata.view;

import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams.EntityTypeFilterValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class OntologyLookupServiceParamsView extends Composite implements Editor<OntologyLookupServiceQueryParams> {
    interface EditorDriver
            extends SimpleBeanEditorDriver<OntologyLookupServiceQueryParams, OntologyLookupServiceParamsView> {
    }

    interface OntologyLookupServiceParamsEditorDriver extends UiBinder<Widget, OntologyLookupServiceParamsView> {}

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    private static OntologyLookupServiceParamsEditorDriver uiBinder =
            GWT.create(OntologyLookupServiceParamsEditorDriver.class);

    @UiField OntologyLookupServiceParamListEditor ontologiesEditor;
    @UiField OntologyLookupServiceParamListEditor childrenEditor;
    @UiField OntologyLookupServiceParamListEditor allChildrenEditor;

    @UiField ComboBox<EntityTypeFilterValue> entityTypeEditor;
    @UiField ListStore<EntityTypeFilterValue> listStore;
    @UiField LabelProvider<EntityTypeFilterValue> labelProvider;

    public OntologyLookupServiceParamsView() {
        initWidget(uiBinder.createAndBindUi(this));

        listStore.add(EntityTypeFilterValue.CLASS);
        listStore.add(EntityTypeFilterValue.PROPERTY);
        listStore.add(EntityTypeFilterValue.INDIVIDUAL);
        listStore.add(EntityTypeFilterValue.ONTOLOGY);

        entityTypeEditor.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        editorDriver.initialize(this);

        ensureDebugId(Belphegor.MetadataIds.OLS_PARAMS_EDIT_DIALOG + Belphegor.MetadataIds.VIEW);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        ontologiesEditor.ensureDebugId(baseID + Belphegor.MetadataIds.ONTOLOGIES);
        childrenEditor.ensureDebugId(baseID + Belphegor.MetadataIds.ONTOLOGY_CHILDREN);
        allChildrenEditor.ensureDebugId(baseID + Belphegor.MetadataIds.ONTOLOGY_ALL_CHILDREN);
        entityTypeEditor.ensureDebugId(baseID + Belphegor.MetadataIds.ONTOLOGY_ENTITY_TYPE);
    }

    @UiFactory
    ListStore<EntityTypeFilterValue> buildListStore() {
        ListStore<EntityTypeFilterValue> listStore = new ListStore<>(EntityTypeFilterValue::toString);
        listStore.setAutoCommit(true);

        return listStore;
    }

    @UiFactory
    LabelProvider<EntityTypeFilterValue> buildLabelProvider() {
        return EntityTypeFilterValue::toString;
    }

    public void edit(OntologyLookupServiceQueryParams params) {
        editorDriver.edit(params);
    }

    public OntologyLookupServiceQueryParams getParams() {
        return editorDriver.flush();
    }
}
