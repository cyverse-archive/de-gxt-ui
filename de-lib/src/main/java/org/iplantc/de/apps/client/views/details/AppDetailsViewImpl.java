package org.iplantc.de.apps.client.views.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.selection.AppDetailsDocSelected;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.DetailsCategoryClicked;
import org.iplantc.de.apps.client.events.selection.DetailsHierarchyClicked;
import org.iplantc.de.apps.client.events.selection.SaveMarkdownSelected;
import org.iplantc.de.apps.client.views.details.doc.AppDocMarkdownDialog;
import org.iplantc.de.apps.client.views.list.cells.AppFavoriteCellWidget;
import org.iplantc.de.apps.client.views.list.cells.AppRatingCellWidget;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.desktop.client.presenter.DesktopPresenterImpl;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * @author jstroot
 */
public class AppDetailsViewImpl extends Composite implements
                                                 AppDetailsView,
                                                 SaveMarkdownSelected.SaveMarkdownSelectedHandler {

    @UiTemplate("AppDetailsViewImpl.ui.xml")
    interface AppInfoViewUiBinder extends UiBinder<TabPanel, AppDetailsViewImpl> {
    }

    static class HighlightEditor implements LeafValueEditor<String> {

        private final AppDetailsAppearance appearance;
        private final DivElement integratorNameDiv;
        private final String searchRegexPattern;

        public HighlightEditor(final AppDetailsAppearance appearance,
                               final DivElement integratorNameDiv,
                               final String searchRegexPattern) {
            this.appearance = appearance;
            this.integratorNameDiv = integratorNameDiv;
            this.searchRegexPattern = searchRegexPattern;
        }

        @Override
        public void setValue(String value) {
            integratorNameDiv.setInnerSafeHtml(appearance.highlightText(value, searchRegexPattern));
        }

        @Override
        public String getValue() {
            return null;
        }
    }

    /**
     * Editor source class for binding to App.getTools()
     */
    private class ToolEditorSource extends EditorSource<ToolDetailsView> {
        private final AccordionLayoutContainer toolsContainer;
        private String baseId;

        public ToolEditorSource(final AccordionLayoutContainer toolsContainer) {
            this.toolsContainer = toolsContainer;
        }

        @Override
        public ToolDetailsView create(int index) {
            final ToolDetailsView toolDetailsView = new ToolDetailsView();
            toolsContainer.insert(toolDetailsView.asWidget(), index);

            if (index == 0) {
                toolsContainer.setActiveWidget(toolDetailsView.asWidget());
            }

            if (!Strings.isNullOrEmpty(baseId)) {
                toolDetailsView.ensureDebugId(baseId + AppsModule.Ids.APP_TOOLS + "." + index);
            }
            return toolDetailsView;
        }

        @Override
        public void dispose(ToolDetailsView subEditor) {
            subEditor.asWidget().removeFromParent();
        }

        public void setBaseDebugId(final String baseId) {
            this.baseId = baseId;
        }
    }

    interface AppDetailsEditorDriver extends SimpleBeanEditorDriver<App, AppDetailsViewImpl> {
    }

    private final AppInfoViewUiBinder BINDER = GWT.create(AppInfoViewUiBinder.class);
    private final AppDetailsEditorDriver editorDriver = GWT.create(AppDetailsEditorDriver.class);

    @UiField
    @Path("")
    AppFavoriteCellWidget favIcon; // Bind to app

    @UiField(provided = true)
    final AppDetailsAppearance appearance;
    @UiField
    @Ignore
    DivElement integratorNameDiv;
    final HighlightEditor integratorName;
    @UiField
    InlineLabel integratorEmail;
    @UiField (provided = true)
    @Ignore
    Tree<OntologyHierarchy, SafeHtml> hierarchyTree;
    @UiField(provided = true) @Ignore TreeStore<OntologyHierarchy> hierarchyTreeStore;
    @UiField(provided = true) @Ignore Tree<AppCategory, String> categoryTree;
    @UiField(provided = true) @Ignore TreeStore<AppCategory> categoryTreeStore;
    @UiField
    @Path("")
    AppRatingCellWidget ratings; // Bind to app
    @UiField
    @Ignore
    InlineHyperlink helpLink;
    @UiField
    AccordionLayoutContainer toolsContainer;
    @UiField
    @Ignore
    DivElement descriptionElement;
    final HighlightEditor description;
    @UiField
    @Path("integrationDate")
    DateLabel publishedOn;
    @UiField
    @Ignore
    Anchor url;
    @UiField
    @Ignore
    HTMLPanel panel;

    @Path("appStats.totalCompleted")
    @UiField
    NumberLabel<Integer> completedRun;

    @Path("appStats.lastCompletedDate")
    @UiField DateLabel completedDate;

    final ListEditor<Tool, ToolDetailsView> tools;
    private final App app;
    private String searchRegexPattern;

    @Inject
    UserInfo userInfo;
    private final ToolEditorSource toolEditorSource;
    @Inject AsyncProviderWrapper<AppDocMarkdownDialog> markdownDialogProvider;

    @Inject
    AppDetailsViewImpl(final AppDetailsView.AppDetailsAppearance appearance,
                       @Assisted final App app,
                       @Assisted final String searchRegexPattern,
                       @Assisted final TreeStore<OntologyHierarchy> hierarchyTreeStore,
                       @Assisted final TreeStore<AppCategory> categoryTreeStore) {
        this.appearance = appearance;
        this.app = app;
        this.hierarchyTreeStore = hierarchyTreeStore;
        this.categoryTreeStore = categoryTreeStore;
        this.searchRegexPattern = searchRegexPattern;

        hierarchyTree = createHierarchyTree();
        categoryTree = createCategoryTree();

        initWidget(BINDER.createAndBindUi(this));

        // Set up highlighting editors
        integratorName = new HighlightEditor(appearance, integratorNameDiv, searchRegexPattern);
        description = new HighlightEditor(appearance, descriptionElement, searchRegexPattern);
        toolEditorSource = new ToolEditorSource(toolsContainer);
        this.tools = ListEditor.of(toolEditorSource);

        /*
         * Debug id has to be set before binding the editor to ensure that UI elements get the debug id
         * before they are rendered/created.
         */
        ensureDebugId(AppsModule.Ids.DETAILS_VIEW);

        // Add self so that rating cell events will fire
        ratings.setHasHandlers(this);
        editorDriver.initialize(this);
        editorDriver.edit(app);
        if (app.isPublic() || app.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
            url.setText(appearance.appUrl());
            url.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    IPlantPromptDialog ipd = new IPlantPromptDialog(appearance.appUrl(),
                                                                    1024,
                                                                    GWT.getHostPageBaseURL()
                                                                            + "?type="
                                                                            + DesktopPresenterImpl.TypeQueryValues.APPS
                                                                            + "&app-id=" + app.getId()
                                                                            + "&"
                                                                            + DesktopPresenterImpl.QueryStrings.SYSTEM_ID
                                                                            + "=" + app.getSystemId(),
                                                                    null);
                    ipd.setHeading(appearance.copyAppUrl());
                    ipd.setWidth("500px");
                    ipd.setPredefinedButtons(PredefinedButton.OK);
                    ipd.show();
                }
            });
        } else {
            helpLink.setVisible(false);
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        favIcon.setBaseDebugId(baseID);
        toolsContainer.ensureDebugId(baseID + AppsModule.Ids.APP_TOOLS);
        toolEditorSource.setBaseDebugId(baseID);
    }

    @Override
    public HandlerRegistration
            addAppDetailsDocSelectedHandler(AppDetailsDocSelected.AppDetailsDocSelectedHandler handler) {
        return addHandler(handler, AppDetailsDocSelected.TYPE);
    }

    @Override
    public HandlerRegistration
            addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return favIcon.addAppFavoriteSelectedEventHandlers(handler);
    }

    @Override
    public HandlerRegistration
            addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return addHandler(handler, AppRatingDeselected.TYPE);
    }

    @Override
    public HandlerRegistration
            addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return addHandler(handler, AppRatingSelected.TYPE);
    }

    @Override
    public HandlerRegistration
            addSaveMarkdownSelectedHandler(SaveMarkdownSelected.SaveMarkdownSelectedHandler handler) {
        return addHandler(handler, SaveMarkdownSelected.TYPE);
    }

    @Override
    public HandlerRegistration addDetailsHierarchyClickedHandler(DetailsHierarchyClicked.DetailsHierarchyClickedHandler handler) {
        return addHandler(handler, DetailsHierarchyClicked.TYPE);
    }

    @Override
    public HandlerRegistration addDetailsCategoryClickedHandler(DetailsCategoryClicked.DetailsCategoryClickedHandler handler) {
        return addHandler(handler, DetailsCategoryClicked.TYPE);
    }

    @Override
    public void onAppUpdated(final AppUpdatedEvent event) {
        editorDriver.edit(event.getApp());
        favIcon.setValue(null);
        favIcon.setValue(event.getApp());
    }

    @Override
    public void onSaveMarkdownSelected(SaveMarkdownSelected event) {
        // Forward event
        fireEvent(event);
    }

    @Override
    public void showDoc(AppDoc appDoc) {
        markdownDialogProvider.get(new AsyncCallback<AppDocMarkdownDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(AppDocMarkdownDialog result) {
                result.show(app, appDoc, userInfo);
                result.addSaveMarkdownSelectedHandler(AppDetailsViewImpl.this);
            }
        });
    }

    @UiHandler("helpLink")
    void onHelpSelected(ClickEvent event) {
        fireEvent(new AppDetailsDocSelected(app));
    }

    @UiFactory
    @Ignore
    DateLabel createDateLabel() {
        return new DateLabel(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM));
    }

    Tree<OntologyHierarchy, SafeHtml> createHierarchyTree() {
        Tree<OntologyHierarchy, SafeHtml> tree = new Tree<>(hierarchyTreeStore, new ValueProvider<OntologyHierarchy, SafeHtml>() {
            @Override
            public SafeHtml getValue(OntologyHierarchy object) {
                return appearance.highlightText(object.getLabel(), searchRegexPattern);
            }

            @Override
            public void setValue(OntologyHierarchy object, SafeHtml value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        });
        appearance.setTreeIcons(tree.getStyle());
        tree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<OntologyHierarchy>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<OntologyHierarchy> event) {
                if (event.getSelection().size() == 1) {
                    OntologyHierarchy hierarchy = event.getSelection().get(0);
                    fireEvent(new DetailsHierarchyClicked(hierarchy));
                }
            }
        });
        tree.setCell(new SafeHtmlCell());
        return tree;
    }

    Tree<AppCategory, String> createCategoryTree() {
        Tree<AppCategory, String> tree = new Tree<>(categoryTreeStore, new ValueProvider<AppCategory, String>() {
            @Override
            public String getValue(AppCategory object) {
                return object.getName();
            }

            @Override
            public void setValue(AppCategory object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        });
        appearance.setTreeIcons(tree.getStyle());
        tree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<AppCategory>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<AppCategory> event) {
                if (event.getSelection().size() == 1) {
                    AppCategory category = event.getSelection().get(0);
                    fireEvent(new DetailsCategoryClicked(category));
                }
            }
        });
        return tree;
    }

    public static native String render(String val) /*-{
		var markdown = $wnd.Markdown.getSanitizingConverter();
		return markdown.makeHtml(val);
    }-*/;

}
