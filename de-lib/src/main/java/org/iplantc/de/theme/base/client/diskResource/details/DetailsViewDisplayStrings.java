package org.iplantc.de.theme.base.client.diskResource.details;

import com.google.gwt.i18n.client.Messages;

/**
 * Created by jstroot on 2/3/15.
 * @author jstroot
 */
public interface DetailsViewDisplayStrings extends Messages{

    String tagAttachError();

    String tagAttached(String tagName, String to);

    String tagDetachError();

    String tagDetached(String tagName, String from);

    String tagFetchError();

}

