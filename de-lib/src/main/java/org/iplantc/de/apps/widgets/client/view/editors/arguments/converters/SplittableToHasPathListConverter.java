package org.iplantc.de.apps.widgets.client.view.editors.arguments.converters;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.client.util.DiskResourceUtil;

import com.google.common.collect.Lists;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.Converter;

import java.util.Collections;
import java.util.List;

/**
 * @author jstroot
 */
public class SplittableToHasPathListConverter implements Converter<Splittable, List<HasPath>> {

    @Override
    public Splittable convertFieldValue(List<HasPath> object) {
        return DiskResourceUtil.getInstance().createStringPathListSplittable(object);
    }

    @Override
    public List<HasPath> convertModelValue(Splittable object) {
        // Assume that the splittable is an array of strings
        if ((object == null) || object.get("path") == null || !object.get("path").isIndexed()) {
            return Collections.emptyList();
        }

        List<HasPath> hasPathList = Lists.newArrayList();
        Splittable paths = object.get("path");
        for (int i = 0; i < paths.size(); i++) {
            String asString = paths.get(i).asString();
            hasPathList.add(CommonModelUtils.getInstance().createHasPathFromString((asString)));
        }

        return hasPathList;
    }

}
