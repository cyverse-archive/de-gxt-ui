package org.iplantc.de.client.models.errors.diskResources.categories;

import org.iplantc.de.client.models.errors.diskResources.ErrorGetManifest;
import org.iplantc.de.client.util.DiskResourceUtil;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

/**
 *
 * A Category class that provides implementation of generateErrorMsg() method.
 *
 * Created by sriram on 10/5/17.
 */

public class ErrorGetManifestCategory {
    private static DiskResourceUtil diskResourceUtil = DiskResourceUtil.getInstance();

    public static SafeHtml generateErrorMsg(AutoBean<ErrorGetManifest> instance) {
        ErrorGetManifest error = instance.as();

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                diskResourceUtil.parseNameFromPath(error.getPath()));
    }

}