package org.iplantc.de.theme.base.client.diskResource.search;

import com.google.gwt.i18n.client.Messages;

/**
 * Created by jstroot on 2/12/15.
 * @author jstroot
 */
public interface SearchMessages extends Messages {
    String advancedSearchToolTip();

    String deleteSearchSuccess(String searchName);

    String saveQueryTemplateFail();

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

    String fileSizeGreaterThan();

    String fileSizeLessThan();

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

    String pathPrefix();

    String exactNameMatch();

    String owner();

    String exactUserNameMatch();

    String permissionValue();

    String permissionRecurse();

    String permissionUsers();

    String emptyDropDownText();

    String taggedWith();

    String fileSizes();
}
