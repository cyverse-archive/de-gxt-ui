package org.iplantc.de.diskResource.client.gin;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.commons.client.presenter.SharingPresenter;
import org.iplantc.de.diskResource.client.BulkMetadataView;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.client.DataSharingView;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.FileUploadByUrlView;
import org.iplantc.de.diskResource.client.GenomeSearchView;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.PathListAutomationView;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.ToolbarView;
import org.iplantc.de.diskResource.client.gin.factory.BulkMetadataViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.DataLinkDialogFactory;
import org.iplantc.de.diskResource.client.gin.factory.DataLinkPresenterFactory;
import org.iplantc.de.diskResource.client.gin.factory.DataSharingPresenterFactory;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourcePresenterFactory;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.FileUploadByUrlViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.FolderContentsRpcProxyFactory;
import org.iplantc.de.diskResource.client.gin.factory.GenomeSearchViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewPresenterFactory;
import org.iplantc.de.diskResource.client.gin.factory.NavigationViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.ShareResourcesLinkDialogFactory;
import org.iplantc.de.diskResource.client.gin.factory.ToolbarViewPresenterFactory;
import org.iplantc.de.diskResource.client.presenters.DiskResourcePresenterImpl;
import org.iplantc.de.diskResource.client.presenters.dataLink.DataLinkPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.details.DetailsViewPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.genome.GenomeSearchPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.grid.GridViewPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsRpcProxyImpl;
import org.iplantc.de.diskResource.client.presenters.metadata.MetadataPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.AstroThesaurusProxy;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceProxy;
import org.iplantc.de.diskResource.client.presenters.navigation.NavigationPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.navigation.proxy.FolderRpcProxyImpl;
import org.iplantc.de.diskResource.client.presenters.search.DataSearchPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.sharing.DataSharingPresenterImpl;
import org.iplantc.de.diskResource.client.presenters.toolbar.ToolbarViewPresenterImpl;
import org.iplantc.de.diskResource.client.views.DiskResourceViewImpl;
import org.iplantc.de.diskResource.client.views.dataLink.DataLinkViewImpl;
import org.iplantc.de.diskResource.client.views.details.DetailsViewImpl;
import org.iplantc.de.diskResource.client.views.dialogs.GenomeSearchDialog;
import org.iplantc.de.diskResource.client.views.dialogs.InfoTypeEditorDialog;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.diskResource.client.views.genome.GenomeSearchViewImpl;
import org.iplantc.de.diskResource.client.views.grid.GridViewImpl;
import org.iplantc.de.diskResource.client.views.metadata.BulkMetadataViewImpl;
import org.iplantc.de.diskResource.client.views.metadata.DiskResourceMetadataViewImpl;
import org.iplantc.de.diskResource.client.views.metadata.MetadataTemplateView;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.BulkMetadataDialog;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.ManageMetadataDialog;
import org.iplantc.de.diskResource.client.views.navigation.NavigationViewImpl;
import org.iplantc.de.diskResource.client.views.search.DiskResourceSearchField;
import org.iplantc.de.diskResource.client.views.search.cells.DiskResourceQueryForm;
import org.iplantc.de.diskResource.client.views.search.cells.DiskResourceSearchCell;
import org.iplantc.de.diskResource.client.views.search.SearchViewImpl;
import org.iplantc.de.diskResource.client.views.sharing.DataSharingViewImpl;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.DataSharingDialog;
import org.iplantc.de.diskResource.client.views.toolbar.DiskResourceViewToolbarImpl;
import org.iplantc.de.diskResource.client.views.toolbar.FileUploadByUrlViewImpl;
import org.iplantc.de.diskResource.client.views.toolbar.PathListAutomationViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.TypeLiteral;

import com.sencha.gxt.data.shared.TreeStore;

/**
 * @author jstroot
 */
public class DiskResourceGinModule extends AbstractGinModule {

    @Override
    protected void configure() {

        install(new GinFactoryModuleBuilder().build(DataLinkDialogFactory.class));
        install(new GinFactoryModuleBuilder().build(ShareResourcesLinkDialogFactory.class));

        // RPC Proxies
        bind(DiskResourceView.FolderRpcProxy.class).to(FolderRpcProxyImpl.class);
        install(new GinFactoryModuleBuilder()
                    .implement(GridView.FolderContentsRpcProxy.class, FolderContentsRpcProxyImpl.class)
                    .build(FolderContentsRpcProxyFactory.class));

        // Disk Resource Presenters
        bind(SearchView.Presenter.class).to(DataSearchPresenterImpl.class);
        install(new GinFactoryModuleBuilder()
                    .implement(DiskResourceView.Presenter.class, DiskResourcePresenterImpl.class)
                    .build(DiskResourcePresenterFactory.class));

        // Genome
        install(new GinFactoryModuleBuilder()
                        .implement(GenomeSearchView.class, GenomeSearchViewImpl.class)
                        .build(GenomeSearchViewFactory.class));
        bind(GenomeSearchView.GenomeSearchPresenter.class).to(GenomeSearchPresenterImpl.class);

        // Data Links
        install(new GinFactoryModuleBuilder()
                    .implement(DataLinkView.Presenter.class, DataLinkPresenterImpl.class)
                    .build(DataLinkPresenterFactory.class));
        bind(DataLinkView.class).to(DataLinkViewImpl.class);

        // Disk Resource Views
        bind(new TypeLiteral<TreeStore<Folder>>() {}).toProvider(DiskResourceTreeStoreProvider.class);
        install(new GinFactoryModuleBuilder()
                    .implement(DiskResourceView.class, DiskResourceViewImpl.class)
                    .build(DiskResourceViewFactory.class));

        // Disk Resource Selectors and Dialogs
        install(new GinFactoryModuleBuilder()
                    .build(DiskResourceSelectorFieldFactory.class));
        bind(DiskResourceQueryForm.class);
        bind(DiskResourceSearchCell.class);
        bind(DiskResourceSearchField.class);


        // Navigation View/Presenter
        bind(NavigationView.Presenter.class).to(NavigationPresenterImpl.class);
        install(new GinFactoryModuleBuilder()
                    .implement(NavigationView.class, NavigationViewImpl.class)
                    .build(NavigationViewFactory.class));


        // Grid View/Presenter
        install(new GinFactoryModuleBuilder()
                    .implement(GridView.Presenter.class, GridViewPresenterImpl.class)
                    .build(GridViewPresenterFactory.class));
        install(new GinFactoryModuleBuilder()
                    .implement(GridView.class, GridViewImpl.class)
                    .build(GridViewFactory.class));

        // Toolbar
        bind(ToolbarView.class).to(DiskResourceViewToolbarImpl.class);

        install(new GinFactoryModuleBuilder()
                    .implement(ToolbarView.Presenter.class, ToolbarViewPresenterImpl.class)
                    .build(ToolbarViewPresenterFactory.class));

        // Details
        bind(DetailsView.Presenter.class).to(DetailsViewPresenterImpl.class);
        bind(DetailsView.class).to(DetailsViewImpl.class);

        install(new GinFactoryModuleBuilder()
                        .implement(FileUploadByUrlView.class, FileUploadByUrlViewImpl.class)
                        .build(FileUploadByUrlViewFactory.class));

        install(new GinFactoryModuleBuilder()
                        .implement(BulkMetadataView.class, BulkMetadataViewImpl.class)
                        .build(BulkMetadataViewFactory.class));

        bind(PathListAutomationView.class).to(PathListAutomationViewImpl.class);

        bind(MetadataView.class).to(DiskResourceMetadataViewImpl.class);
        bind(MetadataView.Presenter.class).to(MetadataPresenterImpl.class);
        bind(MetadataTemplateView.class);

        bind(SearchView.class).to(SearchViewImpl.class);

        // Dialogs
        bind(InfoTypeEditorDialog.class);
        bind(ManageMetadataDialog.class);
        bind(DataSharingDialog.class);
        bind(SaveAsDialog.class);
        bind(BulkMetadataDialog.class);
        bind(GenomeSearchDialog.class);
        bind(OntologyLookupServiceProxy.class);
        bind(AstroThesaurusProxy.class);

        install(new GinFactoryModuleBuilder().implement(SharingPresenter.class,
                                                        DataSharingPresenterImpl.class)
                                             .build(DataSharingPresenterFactory.class));
        bind(DataSharingView.class).to(DataSharingViewImpl.class);

    }
}

