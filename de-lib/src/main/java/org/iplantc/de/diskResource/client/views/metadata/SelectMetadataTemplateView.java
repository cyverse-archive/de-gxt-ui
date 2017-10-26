package org.iplantc.de.diskResource.client.views.metadata;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.events.selection.MetadataInfoBtnSelected;
import org.iplantc.de.diskResource.client.views.metadata.cells.DownloadTemplateCell;
import org.iplantc.de.diskResource.client.views.metadata.cells.TemplateInfoCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.Arrays;
import java.util.List;

public class SelectMetadataTemplateView extends Composite implements SelectionChangedEvent.HasSelectionChangedHandlers<MetadataTemplateInfo>,
                                                                     MetadataInfoBtnSelected.HasMetadataInfoBtnSelectedHandlers {

    interface SelectMetadataTemplateViewUiBinder extends UiBinder<Widget, SelectMetadataTemplateView> {
    }
    private static final SelectMetadataTemplateViewUiBinder uiBinder = GWT.create(SelectMetadataTemplateViewUiBinder.class);

    @UiField VerticalLayoutContainer container;
    @UiField Grid<MetadataTemplateInfo> grid;
    @UiField ListStore<MetadataTemplateInfo> listStore;
    @UiField ColumnModel<MetadataTemplateInfo> cm;

    private ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo> downloadColumn;
    private MetadataView.Presenter.Appearance appearance;
    private DownloadTemplateCell downloadTemplateCell;
    private TemplateInfoCell templateInfoCell;

    @Inject
    public SelectMetadataTemplateView(MetadataView.Presenter.Appearance appearance,
                                      DownloadTemplateCell downloadTemplateCell,
                                      TemplateInfoCell templateInfoCell) {
        this.downloadTemplateCell = downloadTemplateCell;
        this.templateInfoCell = templateInfoCell;
        this.appearance = appearance;

        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
    }

    public void loadTemplates(List<MetadataTemplateInfo> templateInfoList) {
        listStore.clear();
        listStore.addAll(templateInfoList);
    }

    public void showDownloadColumn(boolean show) {
        downloadColumn.setHidden(!show);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        downloadTemplateCell.setBaseDebugId(baseID);
        templateInfoCell.setBaseDebugId(baseID);
    }

    @UiFactory
    ListStore<MetadataTemplateInfo> createListStore() {
        return new ListStore<>(HasId::getId);
    }

    @UiFactory
    ColumnModel<MetadataTemplateInfo> createColumnModel() {
        ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo> info =
                new ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo>(new ValueProvider<MetadataTemplateInfo, MetadataTemplateInfo>() {
                    @Override
                    public MetadataTemplateInfo getValue(MetadataTemplateInfo object) {
                        return object;
                    }

                    @Override
                    public void setValue(MetadataTemplateInfo object, MetadataTemplateInfo value) {
                        // left unimplemented
                    }

                    @Override
                    public String getPath() {
                        return null;
                    }
                }, appearance.infoColumnWidth(), appearance.templates());

        info.setCell(templateInfoCell);

        ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo> download =
                new ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo>(new IdentityValueProvider<MetadataTemplateInfo>(),
                                                                             appearance.downloadColumnWidth(),
                                                                             "");
        this.downloadColumn = download;
        download.setMenuDisabled(true);
        download.setSortable(false);
        download.setCell(downloadTemplateCell);
        return new ColumnModel<MetadataTemplateInfo>(Arrays.<ColumnConfig<MetadataTemplateInfo, ?>>asList(
                info,
                download));
    }

    public MetadataTemplateInfo getSelectedTemplate() {
        return grid.getSelectionModel().getSelectedItem();
    }

    @Override
    public HandlerRegistration addSelectionChangedHandler(SelectionChangedEvent.SelectionChangedHandler<MetadataTemplateInfo> handler) {
        return grid.getSelectionModel().addSelectionChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addMetadataInfoBtnSelectedHandler(MetadataInfoBtnSelected.MetadataInfoBtnSelectedHandler handler) {
        return templateInfoCell.addMetadataInfoBtnSelectedHandler(handler);
    }
}
