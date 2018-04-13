package org.iplantc.de.diskResource.client.views.search.cells;

import org.iplantc.de.client.models.HasLabel;
import org.iplantc.de.client.models.search.DateInterval;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.FileSizeRange.FileSizeUnit;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.client.util.SearchModelUtils;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.diskResource.client.events.search.SaveDiskResourceQueryClickedEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent.HasSubmitDiskResourceQueryEventHandlers;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;
import org.iplantc.de.tags.client.TagsView;
import org.iplantc.de.tags.client.events.TagAddedEvent;
import org.iplantc.de.tags.client.events.selection.RemoveTagSelected;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This form is used to construct, edit and/or save "search filters".
 * <p/>
 * <p/>
 * This form may be constructed with or without an existing query template. If a query template is
 * supplied to the constructor, the form will be initialized with given query template. If the default
 * constructor is used, a new template will be created.
 * <p/>
 * <p/>
 * When the user clicks the "Search" button;
 * <ol>
 * <li>The form will be validated
 * <ol>
 * <li>If the form is <b>invalid</b>, the validation errors will appear in the form and no other action
 * will occur.</li>
 * <li>Else, a {@link SubmitDiskResourceQueryEvent} will be fired with the form's current query template,
 * and this form will be hidden.</li>
 * </ol>
 * </li>
 * </ol>
 * <p/>
 * <p/>
 * When the user clicks the "" hyperlink;
 * <ol>
 * <li>The form will be validated
 * <ol>
 * <li>If the form is <b>invalid</b>, the validation errors will appear in the form and not other action
 * will occur.</li>
 * <li>Else, the user will be presented with a text field allowing them to set a name. Then, if the user
 * clicks "Save", a {@link org.iplantc.de.diskResource.client.events.search.SaveDiskResourceQueryClickedEvent}
 * will be fired with the form's current query template and this form will be hidden.</li>
 * </ol>
 * </li>
 * </ol>
 * FIXME Re-implement with UI-binder
 * @author jstroot
 */
public class DiskResourceQueryForm extends Composite implements
                                                     Editor<DiskResourceQueryTemplate>,
                                                     SaveDiskResourceQueryClickedEvent.HasSaveDiskResourceQueryClickedEventHandlers,
                                                     HasSubmitDiskResourceQueryEventHandlers,
                                                     SaveDiskResourceQueryClickedEvent.SaveDiskResourceQueryClickedEventHandler,
                                                     TagAddedEvent.TagAddedEventHandler,
                                                     RemoveTagSelected.RemoveTagSelectedHandler {

    public interface DiskResourceQueryFormAppearance {
        SafeHtml getQueryTable();

        int columnFormWidth();

        int columnWidth();

        String nameHas();

        String emptyText();

        String nameHasNot();

        String createdWithin();

        String modifiedWithin();

        String metadataAttributeHas();

        String metadataValueHas();

        String ownedBy();

        String emptyNameText();

        String sharedWith();

        String fileSizeGreater();

        String fileSizeLessThan();

        String sizeDropDownWidth();

        String includeTrash();

        String searchBtnText();

        String emptyTimeText();

        String fileNameClass();

        String createdWithinClass();

        String fileNameNegateClass();

        String modifiedWithinClass();

        String metadataAttributeClass();

        String ownerClass();

        String metadataValueClass();

        String sharedClass();

        String searchClass();

        String fileSizeClass();

        String tagsClass();

        String trashAndFilterClass();

        String tableWidth();
    }

    interface SearchFormEditorDriver extends SimpleBeanEditorDriver<DiskResourceQueryTemplate, DiskResourceQueryForm> { }

    protected final BaseEventPreview eventPreview;
    static final Logger LOG = Logger.getLogger(DiskResourceQueryForm.class.getName());
    final SearchFormEditorDriver editorDriver = GWT.create(SearchFormEditorDriver.class);
    IPlantAnchor createFilterLink;
    @Path("createdWithin")
    SimpleComboBox<DateInterval> createdWithinCombo;
    TextField fileQuery;
    @Path("fileSizeRange.min")
    NumberField<Double> fileSizeGreaterThan;
    @Path("fileSizeRange.max")
    NumberField<Double> fileSizeLessThan;
    FieldLabel greaterField;
    @Path("fileSizeRange.minUnit")
    SimpleComboBox<FileSizeUnit> greaterThanComboBox;
    CheckBox includeTrashItems;
    @Path("fileSizeRange.maxUnit")
    SimpleComboBox<FileSizeUnit> lessThanComboBox;
    FieldLabel lesserField;
    TextField metadataAttributeQuery;
    TextField metadataValueQuery;
    @Path("modifiedWithin")
    SimpleComboBox<DateInterval> modifiedWithinCombo;
    @Ignore DiskResourceQueryFormNamePrompt namePrompt;
    TextField negatedFileQuery;
    TextField ownedBy;
    @Ignore TextButton searchButton;
    TextField sharedWith;
    final SimpleEditor<Set<Tag>> tagQuery;
    @Ignore private final HtmlLayoutContainer con;
    private final SearchAutoBeanFactory factory;
    private final TagsView.Presenter tagsViewPresenter;
    private DiskResourceQueryFormAppearance appearance;
    @Ignore private boolean showing;
    SearchModelUtils searchModelUtils;
    @Inject IplantAnnouncer iplantAnnouncer;

    /**
     * Creates the form with a new filter.
     */
    @Inject
    DiskResourceQueryForm(final TagsView.Presenter tagsViewPresenter,
                          SearchAutoBeanFactory factory,
                          SearchModelUtils searchModelUtils,
                          DiskResourceQueryFormNamePrompt namePrompt,
                          DiskResourceQueryFormAppearance appearance) {
        this.factory = factory;
        this.tagsViewPresenter = tagsViewPresenter;
        this.searchModelUtils = searchModelUtils;
        this.namePrompt = namePrompt;
        this.appearance = appearance;
        this.tagsViewPresenter.setRemovable(true);
        this.tagsViewPresenter.setEditable(true);
        VerticalPanel vp = new VerticalPanel();
        con = new HtmlLayoutContainer(appearance.getQueryTable());
        vp.add(con);
        vp.getElement().getStyle().setBackgroundColor("#fff");
        initWidget(vp);

        this.tagsViewPresenter.getView().addTagAddedEventHandler(this);
        this.tagsViewPresenter.getView().addRemoveTagSelectedHandler(this);
        tagQuery = SimpleEditor.of();
        init();
        editorDriver.initialize(this);
        editorDriver.edit(searchModelUtils.createDefaultFilter());
        tagQuery.setValue(new HashSet<Tag>());

        eventPreview = new BaseEventPreview() {

            @Override
            protected boolean onPreview(NativePreviewEvent pe) {
                DiskResourceQueryForm.this.onPreviewEvent(pe);
                return super.onPreview(pe);
            }

            @Override
            protected void onPreviewKeyPress(NativePreviewEvent pe) {
                super.onPreviewKeyPress(pe);
                onEscape(pe);
            }

        };
        eventPreview.getIgnoreList().add(getElement());
        eventPreview.setAutoHide(false);
        addStyleName("x-ignore");
        con.setBorders(true);
        // JDS Small trial to correct placement of form in constrained views.
        this.ensureVisibilityOnSizing = true;

        List<FieldLabel> labels = FormPanelHelper.getFieldLabels(vp);
        for (FieldLabel lbl : labels) {
            lbl.setLabelAlign(LabelAlign.TOP);
        }
    }

    @Override
    public HandlerRegistration addSaveDiskResourceQueryClickedEventHandler(SaveDiskResourceQueryClickedEvent.SaveDiskResourceQueryClickedEventHandler handler) {
        return addHandler(handler, SaveDiskResourceQueryClickedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEventHandler handler) {
        return addHandler(handler, SubmitDiskResourceQueryEvent.TYPE);
    }

    /**
     * Clears search form by binding it to a new default query template
     */
    public void clearSearch() {
        tagsViewPresenter.removeAll();
        editorDriver.edit(searchModelUtils.createDefaultFilter());
    }

    public void edit(DiskResourceQueryTemplate queryTemplate) {
        if (queryTemplate.getTagQuery() == null) {
            tagQuery.setValue(new HashSet<Tag>());
            tagsViewPresenter.removeAll();
        } else {
            for (Tag it : queryTemplate.getTagQuery()) {
                tagsViewPresenter.addTag(it);
            }
        }
        editorDriver.edit(searchModelUtils.copyDiskResourceQueryTemplate(queryTemplate));
    }

    @Override
    public void hide() {
        if (showing) {
            onHide();
            RootPanel.get().remove(this);
            eventPreview.remove();
            showing = false;
            hidden = true;
            fireEvent(new HideEvent());
        }
    }

    @Override
    public void onRemoveTagSelected(RemoveTagSelected event) {
        final Tag tag = event.getTag();
        tagQuery.getValue().remove(tag);
    }

    @Override
    public void onSaveDiskResourceQueryClicked(SaveDiskResourceQueryClickedEvent event) {
        // Re-fire event
        fireEvent(event);
    }

    @Override
    public void onTagAdded(TagAddedEvent event) {
        final Tag tag = event.getTag();
        tagQuery.getValue().add(tag);
    }

    public void show(Element parent, AnchorAlignment anchorAlignment) {
        getElement().makePositionable(true);
        RootPanel.get().add(this);
        onShow();
        getElement().updateZIndex(0);

        showing = true;

        getElement().alignTo(parent, anchorAlignment, 0, 0);

        getElement().show();
        eventPreview.add();

        focus();
        fireEvent(new ShowEvent());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sencha.gxt.widget.core.client.menu.Menu#onHide()
     *
     * When this container becomes hidden, ensure that save filter container is hidden as well.
     *
     * Additionally, this will perform any desired animations when this form is hidden.
     */
    @Override
    protected void onHide() {
        namePrompt.hide();
        super.onHide();
    }

    protected void onPreviewEvent(NativePreviewEvent pe) {
        int type = pe.getTypeInt();
        switch (type) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEWHEEL:
            case Event.ONSCROLL:
            case Event.ONKEYPRESS:
                XElement target = pe.getNativeEvent().getEventTarget().cast();

                // ignore targets within a parent with x-ignore, such as the listview in
                // a combo
                if (target.findParent(".x-ignore", 10) != null) {
                    return;
                }

                if (!getElement().isOrHasChild(target) && !namePrompt.getElement().isOrHasChild(target)) {
                    hide();
                }
        }
    }

    void addTrashAndFilter() {
        VerticalPanel vp = new VerticalPanel();
        vp.add(includeTrashItems);
        vp.add(createFilterLink);
        vp.setSpacing(5);
        con.add(vp, new HtmlData("." + appearance.trashAndFilterClass()));
    }

    DateInterval createDateInterval(Date from, Date to, String label) {
        DateInterval ret = factory.dateInterval().as();
        ret.setFrom(from);
        ret.setTo(to);
        ret.setLabel(label);
        return ret;
    }

    List<FileSizeUnit> createFileSizeUnits() {
        return searchModelUtils.createFileSizeUnits();
    }

    void init() {
        this.namePrompt.addSaveDiskResourceQueryClickedEventHandler(this);
        initFileQuery();
        initNegatedFileQuery();
        initMetadataSearchFields();
        initDateRangeCombos();
        initFileSizeNumberFields();
        initFileSizeComboBoxes();
        initSizeFilterFields();
        initOwnerSharedSearchField();
        initExcludeTrashField();
        initTagField();
        initCreateFilter();
        addTrashAndFilter();
        initSearchButton();
    }

    void initCreateFilter() {
        createFilterLink = new IPlantAnchor("Create filter with this search...", -1);
        createFilterLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Flush to perform local validations
                DiskResourceQueryTemplate flushedFilter = editorDriver.flush();
                if (editorDriver.hasErrors()) {
                    return;
                }
                showNamePrompt(flushedFilter);

            }
        });
    }

    void initDateRangeCombos() {
        List<DateInterval> timeIntervals = Lists.newArrayList();
        Date now = new Date();

        DateInterval interval = createDateInterval(null, null, appearance.emptyTimeText());
        timeIntervals.add(interval);

        final DateWrapper dateWrapper = new DateWrapper(now).clearTime();
        interval = createDateInterval(dateWrapper.addDays(-1).asDate(), now, "1 day");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addDays(-3).asDate(), now, "3 days");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addDays(-7).asDate(), now, "1 week");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addDays(-14).asDate(), now, "2 weeks");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addMonths(-1).asDate(), now, "1 month");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addMonths(-2).asDate(), now, "2 months");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addMonths(-6).asDate(), now, "6 months");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addYears(-1).asDate(), now, "1 year");
        timeIntervals.add(interval);

        // Data range combos
        LabelProvider<DateInterval> dateIntervalLabelProvider = HasLabel::getLabel;
        createdWithinCombo = new SimpleComboBox<>(dateIntervalLabelProvider);
        modifiedWithinCombo = new SimpleComboBox<>(dateIntervalLabelProvider);
        createdWithinCombo.add(timeIntervals);
        modifiedWithinCombo.add(timeIntervals);

        createdWithinCombo.setEmptyText(appearance.emptyTimeText());
        modifiedWithinCombo.setEmptyText(appearance.emptyTimeText());

        createdWithinCombo.setWidth(appearance.columnWidth());
        modifiedWithinCombo.setWidth(appearance.columnWidth());

        con.add(new FieldLabel(createdWithinCombo, appearance.createdWithin()), new HtmlData("." + appearance.createdWithinClass()));
        con.add(new FieldLabel(modifiedWithinCombo, appearance.modifiedWithin()), new HtmlData("." + appearance.modifiedWithinClass()));

    }

    void initExcludeTrashField() {
        includeTrashItems = new CheckBox();
        includeTrashItems.setBoxLabel(appearance.includeTrash());
    }

    void initFileQuery() {
        fileQuery = new TextField();
        fileQuery.setWidth(appearance.columnWidth());
        fileQuery.setEmptyText(appearance.emptyText());
        con.add(new FieldLabel(fileQuery, appearance.nameHas()), new HtmlData("." + appearance.fileNameClass()));
    }

    void initFileSizeComboBoxes() {
        // File Size ComboBoxes
        LabelProvider<FileSizeUnit> fileSizeUnitLabelProvider = HasLabel::getLabel;
        greaterThanComboBox = new SimpleComboBox<>(fileSizeUnitLabelProvider);
        lessThanComboBox = new SimpleComboBox<>(fileSizeUnitLabelProvider);
        greaterThanComboBox.setWidth(appearance.sizeDropDownWidth());
        lessThanComboBox.setWidth(appearance.sizeDropDownWidth());

        greaterThanComboBox.setTriggerAction(TriggerAction.ALL);
        greaterThanComboBox.setForceSelection(true);

        lessThanComboBox.setTriggerAction(TriggerAction.ALL);
        lessThanComboBox.setForceSelection(true);

        List<FileSizeUnit> fileSizeUnitList = createFileSizeUnits();
        greaterThanComboBox.add(fileSizeUnitList);
        lessThanComboBox.add(fileSizeUnitList);

    }

    void initFileSizeNumberFields() {
        // File Size Number fields
        NumberPropertyEditor.DoublePropertyEditor doublePropertyEditor = new NumberPropertyEditor.DoublePropertyEditor();
        fileSizeGreaterThan = new NumberField<>(doublePropertyEditor);
        fileSizeLessThan = new NumberField<>(doublePropertyEditor);

        fileSizeGreaterThan.setAllowNegative(false);
        fileSizeLessThan.setAllowNegative(false);

    }

    void initMetadataSearchFields() {
        metadataAttributeQuery = new TextField();
        metadataAttributeQuery.setEmptyText(appearance.emptyText());
        metadataAttributeQuery.setWidth(appearance.columnWidth());
        con.add(new FieldLabel(metadataAttributeQuery, appearance.metadataAttributeHas()),
                new HtmlData("." + appearance.metadataAttributeClass()));

        metadataValueQuery = new TextField();
        metadataValueQuery.setEmptyText(appearance.emptyText());
        metadataValueQuery.setWidth(appearance.columnWidth());
        con.add(new FieldLabel(metadataValueQuery, appearance.metadataValueHas()),
                new HtmlData("." + appearance.metadataValueClass()));

    }

    void initNegatedFileQuery() {
        negatedFileQuery = new TextField();
        negatedFileQuery.setEmptyText(appearance.emptyText());
        negatedFileQuery.setWidth(appearance.columnWidth());
        con.add(new FieldLabel(negatedFileQuery, appearance.nameHasNot()),
                new HtmlData("." + appearance.fileNameNegateClass()));
    }

    void initOwnerSharedSearchField() {
        ownedBy = new TextField();
        ownedBy.setEmptyText(appearance.emptyNameText());
        ownedBy.setWidth(appearance.columnWidth());
        con.add(new FieldLabel(ownedBy, appearance.ownedBy()), new HtmlData("." + appearance.ownerClass()));

        sharedWith = new TextField();
        sharedWith.setEmptyText(appearance.emptyNameText());
        sharedWith.setWidth(appearance.columnWidth());
        con.add(new FieldLabel(sharedWith, appearance.sharedWith()), new HtmlData("." + appearance.sharedClass()));
    }

    void initSearchButton() {
        searchButton = new TextButton(appearance.searchBtnText());
        searchButton.addSelectHandler(event -> onSearchButtonSelect());
        Label betaLbl = new Label("(beta)");
        betaLbl.setTitle("Search functionality is currently in beta.");
        betaLbl.getElement().getStyle().setColor("#ff0000");
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(searchButton);
        hp.add(betaLbl);
        hp.setSpacing(2);
        con.add(hp, new HtmlData("." + appearance.searchClass()));
    }

    void initSizeFilterFields() {
        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.add(fileSizeGreaterThan);
        hp1.add(greaterThanComboBox);
        hp1.setSpacing(3);

        greaterField = new FieldLabel(hp1, appearance.fileSizeGreater());
        vp.add(greaterField);

        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.add(fileSizeLessThan);
        hp2.add(lessThanComboBox);
        hp2.setSpacing(3);

        lesserField = new FieldLabel(hp2, appearance.fileSizeLessThan());
        vp.add(lesserField);
        con.add(vp, new HtmlData("." + appearance.fileSizeClass()));

    }

    void initTagField() {

        VerticalPanel vp = new VerticalPanel();
        FieldLabel fl = new FieldLabel();
        fl.setText("Tagged with");

        vp.add(fl);
        vp.add(tagsViewPresenter.getView());

        con.add(vp, new HtmlData("." + appearance.tagsClass()));

    }

    @UiHandler("createFilterLink")
    void onCreateQueryTemplateClicked(ClickEvent event) {
        // Flush to perform local validations
        DiskResourceQueryTemplate flushedFilter = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }
        showNamePrompt(flushedFilter);
    }

    void onEscape(NativePreviewEvent pe) {
        if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            pe.getNativeEvent().preventDefault();
            pe.getNativeEvent().stopPropagation();
            hide();
        }
    }

    void onSearchButtonSelect() {
        // Flush to perform local validations
        DiskResourceQueryTemplate flushedQueryTemplate = editorDriver.flush();
        if (editorDriver.hasErrors() || searchModelUtils.isEmptyQuery(flushedQueryTemplate)) {
            return;
        }

        // Fire event and pass flushed query
        fireEvent(new SubmitDiskResourceQueryEvent(flushedQueryTemplate));
        hide();
    }

    void showNamePrompt(DiskResourceQueryTemplate filter) {
        namePrompt.show(filter, getElement(), new AnchorAlignment(Anchor.BOTTOM_LEFT,
                                                                  Anchor.BOTTOM_LEFT,
                                                                  true));
    }

}
