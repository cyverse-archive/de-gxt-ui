package org.iplantc.de.apps.widgets.client.view.editors.arguments.converters;

import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;

import com.google.common.base.Strings;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.shared.Converter;

/**
 * @author jstroot
 */
public class SplittableToFileConverter implements Converter<Splittable, File> {

    private final DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
    @Override
    public Splittable convertFieldValue(File object) {
        if (object == null)
            return StringQuoter.create("");

          return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(object));
    }

    @Override
    public File convertModelValue(Splittable object) {
        if (object == null) {
            return null;
          }
        String path = object.get("path") != null ? object.get("path").asString() : "";
        if (!Strings.isNullOrEmpty(path)) {
            File file = factory.file().as();
            file.setPath(path);
            return file;
        }
        return null;
    }

}
