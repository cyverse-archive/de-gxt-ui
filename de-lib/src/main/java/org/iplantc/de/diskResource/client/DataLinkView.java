package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.diskResource.client.events.selection.AdvancedSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateDataLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.DeleteDataLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.ShowDataLinkSelected;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * Created by jstroot on 2/10/15.
 * @author jstroot
 */
public interface DataLinkView extends IsWidget,
                                      IsMaskable,
                                      CreateDataLinkSelected.HasCreateDataLinkSelectedHandlers,
                                      AdvancedSharingSelected.HasAdvancedSharingSelectedHandlers,
                                      ShowDataLinkSelected.HasShowDataLinkSelectedHandlers,
                                      DeleteDataLinkSelected.HasDeleteDataLinkSelectedHandlers {
    interface Appearance {

        String dataLinkTitle();

        String copyDataLinkDlgHeight();

        int copyDataLinkDlgTextBoxWidth();

        String copyDataLinkDlgWidth();

        String copyPasteInstructions();

        String loadingMask();

        String create();

        ImageResource linkAddIcon();

        String expandAll();

        ImageResource treeExpandIcon();

        String collapseAll();

        ImageResource treeCollapseIcon();

        String showLink();

        ImageResource pasteIcon();

        String advancedSharing();

        ImageResource infoIcon();

        ImageResource exclamationIcon();

        String dataLinkInfoImgClass();

        String dataLinkWarning();

        String backgroundClass();

        String dataLinkWarningClass();

        ImageResourcePrototype emptyTreeNodeIcon();

        String manageDataLinks();

        int manageDataLinksDialogWidth();

        int manageDataLinksDialogHeight();

        String done();

        String manageDataLinksHelp();
    }

    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {

        void setViewDebugId(String baseID);
    }

    void addRoots(List<DiskResource> roots);

    Tree<DiskResource, DiskResource> getTree();

}
