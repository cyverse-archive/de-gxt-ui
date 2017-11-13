package org.iplantc.de.theme.base.client.diskResource.search.cells;

import org.iplantc.de.diskResource.client.views.search.cells.DiskResourceQueryForm;
import org.iplantc.de.theme.base.client.diskResource.search.SearchMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;

public class DiskResourceQueryFormDefaultAppearance implements DiskResourceQueryForm.DiskResourceQueryFormAppearance {

    public interface HtmlLayoutContainerTemplate extends XTemplates {
        @XTemplate(source = "DiskResourceQueryFormTemplate.html")
        SafeHtml getTemplate(DiskResourceQueryForm.DiskResourceQueryFormAppearance appearance);
    }

    private HtmlLayoutContainerTemplate htmlLayoutContainerTemplate;
    private SearchMessages searchMessages;

    public DiskResourceQueryFormDefaultAppearance() {
        this(GWT.<HtmlLayoutContainerTemplate> create(HtmlLayoutContainerTemplate.class),
             GWT.<SearchMessages> create(SearchMessages.class));
    }

    public DiskResourceQueryFormDefaultAppearance(HtmlLayoutContainerTemplate htmlLayoutContainerTemplate,
                                                  SearchMessages searchMessages) {
        this.htmlLayoutContainerTemplate = htmlLayoutContainerTemplate;
        this.searchMessages = searchMessages;
    }

    @Override
    public SafeHtml getQueryTable() {
        return htmlLayoutContainerTemplate.getTemplate(this);
    }

    @Override
    public int columnFormWidth() {
        return 600;
    }

    @Override
    public int columnWidth() {
        return ((columnFormWidth()- 30) / 2) - 12;
    }

    @Override
    public String nameHas() {
        return searchMessages.nameHas();
    }

    @Override
    public String emptyText() {
        return searchMessages.emptyText();
    }

    @Override
    public String nameHasNot() {
        return searchMessages.nameHasNot();
    }

    @Override
    public String createdWithin() {
        return searchMessages.createdWithin();
    }

    @Override
    public String modifiedWithin() {
        return searchMessages.modifiedWithin();
    }

    @Override
    public String metadataAttributeHas() {
        return searchMessages.metadataAttributeHas();
    }

    @Override
    public String metadataValueHas() {
        return searchMessages.metadataValueHas();
    }

    @Override
    public String ownedBy() {
        return searchMessages.ownedBy();
    }

    @Override
    public String emptyNameText() {
        return searchMessages.emptyNameText();
    }

    @Override
    public String sharedWith() {
        return searchMessages.sharedWith();
    }

    @Override
    public String fileSizeGreater() {
        return searchMessages.fileSizeGreaterThan();
    }

    @Override
    public String fileSizeLessThan() {
        return searchMessages.fileSizeLessThan();
    }

    @Override
    public String sizeDropDownWidth() {
        return "64";
    }

    @Override
    public String includeTrash() {
        return searchMessages.includeTrash();
    }

    @Override
    public String searchBtnText() {
        return searchMessages.searchBtnText();
    }

    @Override
    public String emptyTimeText() {
        return searchMessages.emptyTimeText();
    }

    @Override
    public String fileNameClass() {
        return searchMessages.fileNameClass();
    }

    @Override
    public String createdWithinClass() {
        return searchMessages.createdWithinClass();
    }

    @Override
    public String fileNameNegateClass() {
        return searchMessages.fileNameNegateClass();
    }

    @Override
    public String modifiedWithinClass() {
        return searchMessages.modifiedWithinClass();
    }

    @Override
    public String metadataAttributeClass() {
        return searchMessages.metadataAttributeClass();
    }

    @Override
    public String ownerClass() {
        return searchMessages.ownerClass();
    }

    @Override
    public String metadataValueClass() {
        return searchMessages.metadataValueClass();
    }

    @Override
    public String sharedClass() {
        return searchMessages.sharedClass();
    }

    @Override
    public String searchClass() {
        return searchMessages.searchClass();
    }

    @Override
    public String fileSizeClass() {
        return searchMessages.fileSizeClass();
    }

    @Override
    public String tagsClass() {
        return searchMessages.tagsClass();
    }

    @Override
    public String trashAndFilterClass() {
        return searchMessages.trashAndFilterClass();
    }

    @Override
    public String tableWidth() {
        return "50%";
    }
}
