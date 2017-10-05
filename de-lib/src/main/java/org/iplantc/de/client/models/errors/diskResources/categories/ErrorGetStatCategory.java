package org.iplantc.de.client.models.errors.diskResources.categories;

import org.iplantc.de.client.models.errors.diskResources.ErrorGetStat;
import org.iplantc.de.client.util.DiskResourceUtil;

import com.google.common.base.Joiner;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * Created by sriram on 10/5/17.
 */
public class ErrorGetStatCategory {
    private static DiskResourceUtil diskResourceUtil = DiskResourceUtil.getInstance();

    public static SafeHtml generateErrorMsg(AutoBean<ErrorGetStat> instance) {
        ErrorGetStat error = instance.as();

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                Joiner.on(',').join(diskResourceUtil.parseNamesFromIdList(error.getPaths())));
    }
}
