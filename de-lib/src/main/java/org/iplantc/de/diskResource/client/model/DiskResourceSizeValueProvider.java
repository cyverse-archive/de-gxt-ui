package org.iplantc.de.diskResource.client.model;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * This is a value provider class which returns the size of any <code>DiskResource</code>. If the
 * <code>DiskResource</code> is a <code>Folder</code>, this provider will return null. If it is a
 * <code>File</code>, then it returns the value of the {@link File#getSize()} method as an Integer.
 *
 * @author jstroot
 *
 */
public class DiskResourceSizeValueProvider implements ValueProvider<DiskResource, Long> {
    @Override
    public Long getValue(DiskResource object) {
        if (object instanceof File) {
            return ((File) object).getSize();
        } else {
            return null;
        }
    }

    @Override
    public void setValue(DiskResource object, Long value) {
    }

    @Override
    public String getPath() {
        return "size"; //$NON-NLS-1$
    }
}
