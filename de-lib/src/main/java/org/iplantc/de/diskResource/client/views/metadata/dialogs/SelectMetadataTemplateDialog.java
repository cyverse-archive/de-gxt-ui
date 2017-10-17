package org.iplantc.de.diskResource.client.views.metadata.dialogs;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.MetadataView.Presenter.Appearance;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sriram on 5/4/16.
 */
public class SelectMetadataTemplateDialog extends IPlantDialog implements IsWidget {

    @UiTemplate("SelectMetadataTemplateDialog.ui.xml")
    interface SelectMetadataTemplateViewUiBinder extends UiBinder<Widget, SelectMetadataTemplateDialog> {
    }

    private static final SelectMetadataTemplateViewUiBinder uiBinder =
            GWT.create(SelectMetadataTemplateViewUiBinder.class);

    @UiField VerticalLayoutContainer container;
    @UiField Grid<MetadataTemplateInfo> grid;
    @UiField ListStore<MetadataTemplateInfo> listStore;
    @UiField ColumnModel<MetadataTemplateInfo> cm;

    private ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo> downloadColumn;
    private MetadataView.Presenter.Appearance appearance;

    @Inject
    public SelectMetadataTemplateDialog(Appearance appearance) {
        super();
        getOkButton().disable();
        this.appearance = appearance;
        setModal(false);
        setSize(appearance.dialogWidth(), appearance.dialogHeight());
        setHeading(appearance.selectTemplate());

        uiBinder.createAndBindUi(this);
        this.setWidget(asWidget());
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        grid.getSelectionModel()
            .addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<MetadataTemplateInfo>() {
                @Override
                public void onSelectionChanged(SelectionChangedEvent<MetadataTemplateInfo> event) {
                    if (event.getSelection().size() == 0) {
                        getOkButton().disable();
                    } else {
                        getOkButton().enable();
                    }
                }
            });

    }

    public void show(List<MetadataTemplateInfo> templates, boolean showDownloadColumn) {
        listStore.clear();
        listStore.addAll(templates);
        if (!showDownloadColumn) {
            downloadColumn.setHidden(true);
        }

        super.show();
        ensureDebugId(DiskResourceModule.MetadataIds.SELECT_TEMPLATE_BASE_ID);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getOkButton().ensureDebugId(baseID + DiskResourceModule.MetadataIds.SELECT_TEMPLATE_OK_BTN_ID);
    }

    @UiFactory
    ListStore<MetadataTemplateInfo> createListStore() {
        return new ListStore<>(new ModelKeyProvider<MetadataTemplateInfo>() {
            @Override
            public String getKey(MetadataTemplateInfo item) {
                return item.getId();
            }
        });
    }


    @Override
    public Widget asWidget() {
        return container;
    }

    @UiFactory
    ColumnModel<MetadataTemplateInfo> createColumnModel() {
        ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo> name =
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
                }, 150, appearance.templates());

        TemplateNameCell nameCell = new TemplateNameCell();
        name.setCell(nameCell);

        ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo> download =
                new ColumnConfig<MetadataTemplateInfo, MetadataTemplateInfo>(new IdentityValueProvider<MetadataTemplateInfo>(),
                                                                             30,
                                                                             "");
        this.downloadColumn = download;
        download.setMenuDisabled(true);
        download.setSortable(false);
        DownloadTemplateCell cell = new DownloadTemplateCell();
        download.setCell(cell);
        return new ColumnModel<MetadataTemplateInfo>(Arrays.<ColumnConfig<MetadataTemplateInfo, ?>>asList(
                name,
                download));
    }

    public MetadataTemplateInfo getSelectedTemplate() {
        return grid.getSelectionModel().getSelectedItem();
    }


}
