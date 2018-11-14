package org.iplantc.de.theme.base.client.diskResource.metadata;

import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.diskResource.toolbar.ToolbarDisplayMessages;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author jstroot
 */
public class MetadataViewDefaultAppearance implements MetadataView.Appearance {



    interface MetadataHtmlTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span qtip=\"{0}\">{0}</span>")
        SafeHtml cell(String value);

        @SafeHtmlTemplates.Template("<span style='color:red; top:-5px;'>*</span>")
        SafeHtml required();

        @SafeHtmlTemplates.Template("<b>{0}:</b> <br/>")
        SafeHtml guideLabel(String name);

        @SafeHtmlTemplates.Template("<p>{0}</p><br/>")
        SafeHtml guideHelpText(String description);

    }

    private final MetadataHtmlTemplates htmlTemplates;
    private final IplantResources iplantResources;
    private final MetadataDisplayStrings displayStrings;
    private final IplantDisplayStrings iplantDisplayStrings;
    private final ToolbarDisplayMessages toolbarMessages;


    public MetadataViewDefaultAppearance() {
        this(GWT.<MetadataHtmlTemplates> create(MetadataHtmlTemplates.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<MetadataDisplayStrings> create(MetadataDisplayStrings.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.create(ToolbarDisplayMessages.class));
    }

    MetadataViewDefaultAppearance(final MetadataHtmlTemplates htmlTemplates,
                                  final IplantResources iplantResources,
                                  final MetadataDisplayStrings displayStrings,
                                  final IplantDisplayStrings iplantDisplayStrings,
                                  final ToolbarDisplayMessages toolbarMessages) {
        this.htmlTemplates = htmlTemplates;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.toolbarMessages = toolbarMessages;
    }

    @Override
    public String attribute() {
        return displayStrings.attribute();
    }

    @Override
    public String confirmAction() {
        return iplantDisplayStrings.confirmAction();
    }

    @Override
    public String newAttribute() {
        return displayStrings.newAttribute();
    }

    @Override
    public String newValue() {
        return displayStrings.newValue();
    }

    @Override
    public String newUnit() {
        return displayStrings.newUnit();
    }

    @Override
    public String paramValue() {
        return iplantDisplayStrings.paramValue();
    }

    @Override
    public void renderMetadataCell(SafeHtmlBuilder sb, String value) {
        if(Strings.isNullOrEmpty(value)){
            return;
        }
        sb.append(htmlTemplates.cell(value));
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String userMetadata() {
        return displayStrings.userMetadata();
    }

    @Override
    public String add() {
        return iplantDisplayStrings.add();
    }

    @Override
    public ImageResource addIcon() {
        return iplantResources.add();
    }

    @Override
    public String delete() {
        return iplantDisplayStrings.delete();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.delete();
    }

	@Override
	public String additionalMetadata() {
		return displayStrings.additionalMetadata();
	}

	@Override
	public String paramUnit() {
		return iplantDisplayStrings.paramUnit();
	}

    @Override
    public String selectTemplate() {
        return displayStrings.selectTemplate();
    }

    @Override
    public ImageResource editIcon() {
        return iplantResources.edit();
    }

    @Override
    public String edit() {
        return iplantDisplayStrings.edit();
    }

    @Override
    public String importMdTooltip() {
        return displayStrings.importMdTooltip();
    }

    @Override
    public String metadataLink() {
        return displayStrings.metadataLink();
    }

    @Override
    public String readMore() {
        return displayStrings.readMore();
    }

    @Override

    public ImageResource saveToFileIcon() {
        return iplantResources.fileRename();
    }

    @Override
    public String saveMetadataToFile() {
        return toolbarMessages.saveMetadataMenuItem();
    }
    
    public String loading() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String backgroundStyle() {
        return "background:#fff;";
    }

    @Override
    public String requiredGhostText() {
        return displayStrings.requiredGhostText();
    }

    @Override
    public String importUMdBtnText() {
        return displayStrings.importUMdBtnText();
    }

    @Override
    public String metadataTermDlgWidth() {
        return "350";
    }

    @Override
    public String metadataTermDlgHeight() {
        return "400";
    }

    @Override
    public SafeHtml guideLabel(String name) {
        return htmlTemplates.guideLabel(name);
    }

    @Override
    public SafeHtml guideHelpText(String description) {
        return htmlTemplates.guideHelpText(description);
    }

    @Override
    public String dialogWidth() {
        return "640px";
    }

    @Override
    public String dialogHeight() {
        return "480px";
    }

    @Override
    public int attributeColumnWidth() {
        return 150;
    }

    @Override
    public int valueColumnWidth() {
        return 150;
    }

    @Override
    public int unitColumnWidth() {
        return 150;
    }
}
