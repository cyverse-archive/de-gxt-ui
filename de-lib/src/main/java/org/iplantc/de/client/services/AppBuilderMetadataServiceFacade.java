package org.iplantc.de.client.services;

import org.iplantc.de.client.models.apps.integration.DataSource;
import org.iplantc.de.client.models.apps.integration.FileInfoType;
import org.iplantc.de.client.models.apps.refGenome.ReferenceGenome;
import org.iplantc.de.shared.DECallback;

import java.util.List;

/**
 * @author jstroot
 */
public interface AppBuilderMetadataServiceFacade {

    void getDataSources(DECallback<List<DataSource>> callback);

    void getFileInfoTypes(DECallback<List<FileInfoType>> callback);
    
    void getReferenceGenomes(DECallback<List<ReferenceGenome>> callback);

}
