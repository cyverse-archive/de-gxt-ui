package org.iplantc.de.apps.widgets.client.view.editors.arguments.converters;

import org.iplantc.de.client.models.apps.refGenome.ReferenceGenome;
import org.iplantc.de.shared.DECallback;

import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.IsField;

/**
 * This is a slight modification to {@link ArgumentEditorConverter} and is specifically for
 * Reference Genome|Annotation|Sequence editors.  The intention is to make sure a
 * reference genome is always selected from the most up to date genome list
 * {@link org.iplantc.de.client.services.AppBuilderMetadataServiceFacade#getReferenceGenomes(DECallback)}
 * instead of auto-populating from a relaunch request, which may have stale data.
 *
 * See: CORE-9224
 * @param <T>
 */
public class ReferenceGenomeEditorConverter<T> extends ArgumentEditorConverter<T> {

    private final ListStore<ReferenceGenome> store;
    private IsField<T> field;

    public ReferenceGenomeEditorConverter(IsField<T> field, Converter<Splittable, T> converter) {
        super(field, converter);

        this.field = field;
        ComboBox<ReferenceGenome> comboBox = (ComboBox<ReferenceGenome>)field;
        this.store = comboBox.getListView().getStore();
        store.addStoreAddHandler(event -> {
            updateValueFromStore();
        });
    }

    @Override
    public void setValue(Splittable value) {
        super.setValue(value);
        updateValueFromStore();
    }

    void updateValueFromStore() {
        ReferenceGenome selectedGenome = ((ComboBox<ReferenceGenome>)field).getCurrentValue();
        if (selectedGenome != null) {
            ((ComboBox<ReferenceGenome>)field).setValue(store.findModelWithKey(selectedGenome.getId()));
        }
    }

}
