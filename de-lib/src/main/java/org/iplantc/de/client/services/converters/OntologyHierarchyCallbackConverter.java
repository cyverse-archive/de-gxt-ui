package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.shared.DECallback;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author aramsey
 */
public class OntologyHierarchyCallbackConverter
        extends DECallbackConverter<String, OntologyHierarchy> {

    private final OntologyAutoBeanFactory factory;

    public OntologyHierarchyCallbackConverter(DECallback<OntologyHierarchy> callback,
                                              OntologyAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected OntologyHierarchy convertFrom(String object) {
        final AutoBean<OntologyHierarchy> decode =
                AutoBeanCodex.decode(factory, OntologyHierarchy.class, object);
        OntologyHierarchy hierarchy = decode.as();
        if (null != hierarchy.getHierarchy()) {
            return hierarchy.getHierarchy();
        }
        return hierarchy;
    }
}
