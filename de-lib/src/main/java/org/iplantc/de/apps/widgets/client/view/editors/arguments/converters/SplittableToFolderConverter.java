package org.iplantc.de.apps.widgets.client.view.editors.arguments.converters;

import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.Folder;

import com.google.common.base.Strings;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.shared.Converter;

public class SplittableToFolderConverter implements Converter<Splittable, Folder> {

    private final DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);

    @Override
    public Splittable convertFieldValue(Folder object) {
        if (object == null)
            return StringQuoter.create("");

          return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(object));
    }

    @Override
    public Folder convertModelValue(Splittable object) {
        if (object == null) {
            return null;
          }
        String path = object.get("path") != null ? object.get("path").asString() : "";
        if (!Strings.isNullOrEmpty(path)) {
            Folder folder = factory.folder().as();
            folder.setPath(path);
            return folder;
        }
        return null;

    }

}
