package org.iplantc.de.diskResource.client.presenters.metadata;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.avu.AvuAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttributeType;
import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.AstroThesaurusLoadConfig;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.AstroThesaurusProxy;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.MetadataTermLoadConfig;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.MetadataTermSearchProxy;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceLoadConfig;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceProxy;
import org.iplantc.de.diskResource.client.views.search.MetadataTermSearchField;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

@Singleton
public class MetadataUtil {

    AvuAutoBeanFactory factory;
    OntologyAutoBeanFactory ontologyFactory;
    OntologyLookupServiceProxy olsSearchProxy;
    AstroThesaurusProxy uatSearchProxy;
    MetadataTermSearchField.MetadataTermSearchFieldAppearance searchFieldAppearance;

    private int uniqueAvuId = 0;

    @Inject
    public MetadataUtil(AvuAutoBeanFactory factory,
                        OntologyAutoBeanFactory ontologyFactory,
                        OntologyLookupServiceProxy olsSearchProxy,
                        AstroThesaurusProxy uatSearchProxy,
                        MetadataTermSearchField.MetadataTermSearchFieldAppearance searchFieldAppearance) {
        this.factory = factory;
        this.ontologyFactory = ontologyFactory;
        this.olsSearchProxy = olsSearchProxy;
        this.uatSearchProxy = uatSearchProxy;
        this.searchFieldAppearance = searchFieldAppearance;
    }

    public Avu setAvuModelKey(Avu avu) {
        if (avu != null) {
            final AutoBean<Avu> avuBean = AutoBeanUtils.getAutoBean(avu);
            avuBean.setTag(Avu.AVU_BEAN_TAG_MODEL_KEY, String.valueOf(uniqueAvuId++));
            return avuBean.as();
        }
        return null;
    }

    public Avu newMetadata(String attr, String value, String unit) {
        Avu avu = factory.getAvu().as();

        avu.setAttribute(attr);
        avu.setValue(value);
        avu.setUnit(unit);

        return avu;
    }

    public MetadataTermSearchField createMetadataTermSearchField(MetadataTemplateAttribute attribute) {
        MetadataTermSearchProxy searchProxy = null;
        MetadataTermLoadConfig loadConfig = new MetadataTermLoadConfig();

        if (attribute != null) {
            String type = attribute.getType();

            if (MetadataTemplateAttributeType.OLS_ONTOLOGY_TERM.toString().equalsIgnoreCase(type)) {
                OntologyLookupServiceQueryParams loaderSettings = null;

                if (attribute.getSettings() != null) {
                    loaderSettings = AutoBeanCodex.decode(ontologyFactory,
                                                          OntologyLookupServiceQueryParams.class,
                                                          attribute.getSettings()).as();
                }

                loadConfig = new OntologyLookupServiceLoadConfig(loaderSettings);
                searchProxy = olsSearchProxy;
            } else if (MetadataTemplateAttributeType.UAT_ONTOLOGY_TERM.toString()
                                                                      .equalsIgnoreCase(type)) {
                loadConfig = new AstroThesaurusLoadConfig();
                searchProxy = uatSearchProxy;
            }

            return new MetadataTermSearchField(ontologyFactory,
                                               searchProxy,
                                               loadConfig,
                                               searchFieldAppearance);
        }

        return null;
    }

}
