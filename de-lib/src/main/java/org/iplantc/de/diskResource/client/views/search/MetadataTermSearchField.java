package org.iplantc.de.diskResource.client.views.search;

import org.iplantc.de.client.models.ontologies.MetadataTermSearchResult;
import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.MetadataTermLoadConfig;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.MetadataTermSearchProxy;

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
 * by creating a new {@link MetadataTermSearchResult} as the field's value which has the user's text as a custom label and no IRI,
 * but it can also autocomplete text with terms found using the Ontology Lookup Service.
 *
 * This class can also be used as a {@link Converter} for the {@link ComboBox} in a
 * {@link GridRowEditing#addEditor(ColumnConfig, Converter, IsField)} call.
 */
public class MetadataTermSearchField implements Converter<String, MetadataTermSearchResult> {

    public interface MetadataTermSearchFieldAppearance {
        void render(Context context, MetadataTermSearchResult OntologyLookupServiceDoc, SafeHtmlBuilder sb);
    }

    /**
     * A {@link ComboBoxCell} that allows the user to enter any text into a {@link ComboBox} field,
     * by creating a new {@link MetadataTermSearchResult} as the field's value,
     * which has the user's text as a custom label and no IRI.
     */
    private class FreeTextComboBoxCell extends ComboBoxCell<MetadataTermSearchResult> {
        FreeTextComboBoxCell(ListStore<MetadataTermSearchResult> store, ListView<MetadataTermSearchResult, MetadataTermSearchResult> view) {
            super(store, MetadataTermSearchResult::getLabel, view);

            setPropertyEditor(new PropertyEditor<MetadataTermSearchResult>() {
                @Override
                public MetadataTermSearchResult parse(CharSequence text) throws ParseException {
                    String label = text == null ? "" : text.toString();

                    MetadataTermSearchResult selectedClass = getByValue(label);

                    if (selectedClass == null) {
                        selectedClass = convertModelValue(label);
                    }

                    return selectedClass;
                }

                @Override
                public String render(MetadataTermSearchResult object) {
                    return object.getLabel();
                }
            });
        }
    }

    private final OntologyAutoBeanFactory factory;

    private ComboBox<MetadataTermSearchResult> combo;
    private ListView<MetadataTermSearchResult, MetadataTermSearchResult> view;

    public MetadataTermSearchField(OntologyAutoBeanFactory factory,
                                   MetadataTermSearchProxy searchProxy,
                                   MetadataTermLoadConfig loadConfig,
                                   MetadataTermSearchFieldAppearance appearance) {
        this.factory = factory;

        ListStore<MetadataTermSearchResult> store = new ListStore<>(MetadataTermSearchResult::getId);
        store.addSortInfo(new Store.StoreSortInfo<>(Comparator.comparing(MetadataTermSearchResult::getLabel), SortDir.ASC));

        createView(store, appearance);

        createCombo(store, createLoader(searchProxy, loadConfig, store));
    }

    private void createView(ListStore<MetadataTermSearchResult> store, MetadataTermSearchFieldAppearance appearance) {
        view = new ListView<>(store, new IdentityValueProvider<>());
        view.setCell(new AbstractCell<MetadataTermSearchResult>() {
            @Override
            public void render(Context context, MetadataTermSearchResult value, SafeHtmlBuilder sb) {
                appearance.render(context, value, sb);
            }
        });
    }

    private PagingLoader<MetadataTermLoadConfig, PagingLoadResult<MetadataTermSearchResult>> createLoader(
            MetadataTermSearchProxy searchProxy,
            MetadataTermLoadConfig loadConfig,
            ListStore<MetadataTermSearchResult> store) {
        PagingLoader<MetadataTermLoadConfig, PagingLoadResult<MetadataTermSearchResult>> loader =
                new PagingLoader<>(searchProxy);

        loader.useLoadConfig(loadConfig);
        loader.addLoadHandler(new LoadResultListStoreBinding<>(store));

        loader.addBeforeLoadHandler(event -> {
            String query = combo.getText();

            if (!Strings.isNullOrEmpty(query)) {
                event.getLoadConfig().setQuery(query);
            }
        });

        return loader;
    }

    private void createCombo(ListStore<MetadataTermSearchResult> store,
                             PagingLoader<MetadataTermLoadConfig, PagingLoadResult<MetadataTermSearchResult>> loader) {
        combo = new ComboBox<>(new FreeTextComboBoxCell(store, view));
        combo.setLoader(loader);
        combo.setMinChars(3);
        combo.setWidth(250);
        combo.setHideTrigger(true);
        combo.setForceSelection(false);
        combo.getCell().setPageSize(loader.getLastLoadConfig().getLimit());
    }

    @Override
    public String convertFieldValue(MetadataTermSearchResult object) {
        return object == null ? null : object.getLabel();
    }

    @Override
    public MetadataTermSearchResult convertModelValue(String label) {
        MetadataTermSearchResult customLabel = factory.getMetadataTermSearchResult().as();
        customLabel.setLabel(label);

        return customLabel;
    }

    public ComboBox<MetadataTermSearchResult> asField() {
        return combo;
    }

    public void setViewDebugId(String debugId) {
        view.ensureDebugId(debugId);
    }
}
