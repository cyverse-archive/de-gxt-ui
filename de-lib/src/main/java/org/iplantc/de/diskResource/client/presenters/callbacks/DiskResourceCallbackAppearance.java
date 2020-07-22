package org.iplantc.de.diskResource.client.presenters.callbacks;

/**
 * Created by jstroot on 2/10/15.
 * @author jstroot
 */
public interface DiskResourceCallbackAppearance {
    String createDataLinksError();

    String createFolderFailed();

    String deleteDataLinksError();

    String deleteFailed();

    String diskResourceMoveSuccess();

    String listDataLinksError();

    String metadataSuccess();

    String metadataUpdateFailed();

    String moveFailed();

    String partialRestore();

    String renameFailed();

    String restoreDefaultMsg();

    String restoreMsg();

    String ncbiCreateFolderStructureSuccess();
}
