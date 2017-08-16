package org.iplantc.de.diskResource.client.views.search;

import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceDoc;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceLoadConfig;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceProxy;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

import java.text.ParseException;
import java.util.Comparator;

/**
 * A wrapper class for creating a {@link ComboBox} form field that allows the user to enter any text into the field,
 * by creating a new {@link OntologyLookupServiceDoc} as the field's value which has the user's text as a custom label and no IRI,
 * but it can also autocomplete text with terms found using the Ontology Lookup Service.
 *
 * This class can also be used as a {@link Converter} for the {@link ComboBox} in a
 * {@link GridRowEditing#addEditor(ColumnConfig, Converter, IsField)} call.
 */
public class MetadataTermSearchField implements Converter<String, OntologyLookupServiceDoc> {

    public interface MetadataTermSearchFieldAppearance {
        void render(Context context, OntologyLookupServiceDoc OntologyLookupServiceDoc, SafeHtmlBuilder sb);
    }

    /**
     * A {@link ComboBoxCell} that allows the user to enter any text into a {@link ComboBox} field,
     * by creating a new {@link OntologyLookupServiceDoc} as the field's value,
     * which has the user's text as a custom label and no IRI.
     */
    private class FreeTextComboBoxCell extends ComboBoxCell<OntologyLookupServiceDoc> {
        FreeTextComboBoxCell(ListStore<OntologyLookupServiceDoc> store, ListView<OntologyLookupServiceDoc, OntologyLookupServiceDoc> view) {
            super(store, OntologyLookupServiceDoc::getLabel, view);

            setPropertyEditor(new PropertyEditor<OntologyLookupServiceDoc>() {
                @Override
                public OntologyLookupServiceDoc parse(CharSequence text) throws ParseException {
                    String label = text == null ? "" : text.toString();

                    OntologyLookupServiceDoc selectedClass = getByValue(label);

                    if (selectedClass == null) {
                        selectedClass = convertModelValue(label);
                    }

                    return selectedClass;
                }

                @Override
                public String render(OntologyLookupServiceDoc object) {
                    return object.getLabel();
                }
            });
        }
    }

    private final OntologyAutoBeanFactory factory;

    private ComboBox<OntologyLookupServiceDoc> combo;
    private ListView<OntologyLookupServiceDoc, OntologyLookupServiceDoc> view;

    public MetadataTermSearchField(OntologyAutoBeanFactory factory,
                                   OntologyLookupServiceProxy searchProxy,
                                   MetadataTermSearchFieldAppearance appearance) {
        this.factory = factory;

        ListStore<OntologyLookupServiceDoc> store = new ListStore<>(OntologyLookupServiceDoc::getId);
        store.addSortInfo(new Store.StoreSortInfo<>(Comparator.comparing(OntologyLookupServiceDoc::getLabel), SortDir.ASC));

        createView(store, appearance);

        createCombo(store, createLoader(searchProxy, store));
    }

    private void createView(ListStore<OntologyLookupServiceDoc> store, MetadataTermSearchFieldAppearance appearance) {
        view = new ListView<>(store, new IdentityValueProvider<>());
        view.setCell(new AbstractCell<OntologyLookupServiceDoc>() {
            @Override
            public void render(Context context, OntologyLookupServiceDoc value, SafeHtmlBuilder sb) {
                appearance.render(context, value, sb);
            }
        });
    }

    private PagingLoader<OntologyLookupServiceLoadConfig, PagingLoadResult<OntologyLookupServiceDoc>> createLoader(
            OntologyLookupServiceProxy searchProxy,
            ListStore<OntologyLookupServiceDoc> store) {
        PagingLoader<OntologyLookupServiceLoadConfig, PagingLoadResult<OntologyLookupServiceDoc>> loader =
                new PagingLoader<>(searchProxy);

        loader.useLoadConfig(new OntologyLookupServiceLoadConfig());
        loader.addLoadHandler(new LoadResultListStoreBinding<>(store));

        loader.addBeforeLoadHandler(event -> {
            String query = combo.getText();

            if (!Strings.isNullOrEmpty(query)) {
                event.getLoadConfig().setQuery(query);
            }
        });

        return loader;
    }

    private void createCombo(ListStore<OntologyLookupServiceDoc> store,
                             PagingLoader<OntologyLookupServiceLoadConfig, PagingLoadResult<OntologyLookupServiceDoc>> loader) {
        combo = new ComboBox<>(new FreeTextComboBoxCell(store, view));
        combo.setLoader(loader);
        combo.setMinChars(3);
        combo.setWidth(250);
        combo.setHideTrigger(true);
        combo.setForceSelection(false);
        combo.getCell().setPageSize(loader.getLastLoadConfig().getLimit());
    }

    @Override
    public String convertFieldValue(OntologyLookupServiceDoc object) {
        return object == null ? null : object.getLabel();
    }

    @Override
    public OntologyLookupServiceDoc convertModelValue(String label) {
        OntologyLookupServiceDoc customLabel = factory.getOntologyLookupServiceDoc().as();
        customLabel.setLabel(label);

        return customLabel;
    }

    public ComboBox<OntologyLookupServiceDoc> asField() {
        return combo;
    }

    public void setViewDebugId(String debugId) {
        view.ensureDebugId(debugId);
    }
}
