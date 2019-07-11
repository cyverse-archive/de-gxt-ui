package org.iplantc.de.theme.base.client.admin.toolAdmin;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;


/**
 * @author aramsey
 */
public interface ToolAdminDisplayStrings extends Messages {

    @Key("add")
    String add();

    @Key("filter")
    String filter();

    @Key("nameColumnLabel")
    String nameColumnLabel();

    @Key("descriptionColumnLabel")
    String descriptionColumnLabel();

    @Key("attributionColumnLabel")
    String attributionColumnLabel();

    @Key("locationColumnInfoLabel")
    String locationColumnInfoLabel();

    @Key("versionColumnInfoLabel")
    String versionColumnInfoLabel();

    @Key("typeColumnInfoLabel")
    String typeColumnInfoLabel();

    @Key("deleteBtnText")
    String deleteBtnText();

    @Key("deleteToolSuccessText")
    String deleteToolSuccessText();

    @Key("addToolSuccessText")
    String addToolSuccessText();

    @Key("updateToolSuccessText")
    String updateToolSuccessText();

    @Key("confirmOverwriteTitle")
    String confirmOverwriteTitle();

    @Key("confirmOverwriteBody")
    String confirmOverwriteBody();

    @Key("deletePublicToolTitle")
    String deletePublicToolTitle();

    @Key("deletePublicToolBody")
    String deletePublicToolBody();

    @Key("confirmOverwriteDangerZone")
    String confirmOverwriteDangerZone();

    @Key("publicAppNameLabel")
    String publicAppNameLabel();

    @Key("publicAppIntegratorLabel")
    String publicAppIntegratorLabel();

    @Key("publicAppIntegratorEmailLabel")
    String publicAppIntegratorEmailLabel();

    @Key("publicAppDisabledLabel")
    String publicAppDisabledLabel();
}
