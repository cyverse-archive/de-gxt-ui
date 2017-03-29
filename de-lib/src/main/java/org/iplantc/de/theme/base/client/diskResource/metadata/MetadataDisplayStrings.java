package org.iplantc.de.theme.base.client.diskResource.metadata;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Created by jstroot on 2/10/15.
 * @author jstroot
 */
public interface MetadataDisplayStrings extends Messages{
    String attribute();

    String metadataTemplateConfirmRemove();

    String metadataTemplateRemove();

    String metadataTemplateSelect();

    String newAttribute();

    String newValue();

	String newUnit();

	String selectTemplate();

	SafeHtml importMd();

    String userMetadata();

    String metadataTermGuide();

	String templateListingError();

	String loadMetadataError();

	String saveMetadataError();

	String templateinfoError();

	String additionalMetadata();

	String templates();

	String error();

	String incomplete();

	SafeHtml importMdMsg();

	String importMdTooltip();

	String metadataLink();

	String readMore();

	String urlGhostText();

	String requiredGhostText();

    String importUMdBtnText();
}
