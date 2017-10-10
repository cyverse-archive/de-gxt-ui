package org.iplantc.de.diskResource.client.views.genome;

import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.diskResource.client.GenomeSearchView;
import org.iplantc.de.diskResource.client.model.GenomeProperties;
import org.iplantc.de.diskResource.client.views.widgets.GenomeSearchField;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.ArrayList;

public class GenomeSearchViewImpl extends Composite implements GenomeSearchView {
    
    interface GenomeSearchViewUiBinder extends UiBinder<Widget, GenomeSearchViewImpl> {}
    private static GenomeSearchViewUiBinder BINDER = GWT.create(GenomeSearchViewUiBinder.class);

    @UiField GenomeSearchField searchField;
    @UiField Grid<Genome> grid;
    @UiField ColumnModel<Genome> cm;
    @UiField ListStore<Genome> store;
    @UiField(provided = true) GenomeSearchViewAppearance appearance;
    @UiField TextButton importBtn;
    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Genome>> loader;

    @Inject
    public GenomeSearchViewImpl(GenomeSearchViewAppearance appearance,
                                @Assisted PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Genome>> loader) {
        this.appearance = appearance;
        this.loader = loader;
        initWidget(BINDER.createAndBindUi(this));
        grid.getSelectionModel().addSelectionChangedHandler(event -> {
            if (event.getSelection().size() > 0) {
                importBtn.setEnabled(true);
            } else {
                importBtn.setEnabled(false);
            }
        });
        grid.setLoader(loader);
        loader.addLoadHandler(new LoadResultListStoreBinding<>(store));
        grid.setLoadMask(true);
        grid.getView().setEmptyText(appearance.noRecords());
    }

    @UiHandler("importBtn")
    void onImportedClicked(SelectEvent event) {
//        presenter.importGenomeFromCoge(grid.getSelectionModel().getSelectedItem().getId());
    }

    @UiFactory
    GenomeSearchField createAppSearchField() {
        return new GenomeSearchField(loader);
    }

    @UiFactory
    public ListStore<Genome> createListStore() {
        return new ListStore<>(item -> Integer.toString(item.getId()));
    }

    @UiFactory
    public ColumnModel<Genome> createColumnModel() {

        GenomeProperties props = GWT.create(GenomeProperties.class);

        ColumnConfig<Genome, String> version = new ColumnConfig<>(props.version(),
                                                                  50,
                                                                  appearance.version());

        ColumnConfig<Genome, String> seqType = new ColumnConfig<>(props.sequenceName(),
                                                                  150,
                                                                  appearance.sequenceType());

        ColumnConfig<Genome, String> organism = new ColumnConfig<>(props.organismName(),
                                                                   250,
                                                                   appearance.organismName());
        ColumnConfig<Genome, Integer> chromeCount = new ColumnConfig<>(props.chromosomeCount(),
                                                                       100,
                                                                       appearance.chromosomeCount());
        ArrayList<ColumnConfig<Genome, ?>> cols = new ArrayList<>();
        cols.add(organism);
        cols.add(version);
        cols.add(chromeCount);
        cols.add(seqType);

        return new ColumnModel<>(cols);
    }
}
