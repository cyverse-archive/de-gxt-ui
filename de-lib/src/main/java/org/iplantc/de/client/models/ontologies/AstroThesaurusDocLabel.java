package org.iplantc.de.client.models.ontologies;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * An AutoBean model for Unified Astronomy Thesaurus search result labels.
 *
 * @author psarando
 */
public interface AstroThesaurusDocLabel {

    /**
     * @return The string value of the label.
     */
    @PropertyName("_value")
    String getValue();

    /**
     * @param label The string value of the label.
     */
    @PropertyName("_value")
    void setValue(String label);
}
