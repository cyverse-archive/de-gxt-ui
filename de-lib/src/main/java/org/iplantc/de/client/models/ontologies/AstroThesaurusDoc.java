package org.iplantc.de.client.models.ontologies;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * {@link MetadataTermSearchResult} model for Unified Astronomy Thesaurus search results.
 *
 * @author psarando
 */
public interface AstroThesaurusDoc extends MetadataTermSearchResult {
    /**
     * @return UAT search results only include an IRI, so this model's ID will be the same as its IRI.
     */
    @PropertyName("_about")
    String getId();

    /**
     * @return UAT search results only include an IRI, so this model's ID will be the same as its IRI.
     */
    @PropertyName("_about")
    String getIri();

    /**
     * @param iri UAT search results only include an IRI, so updating this IRI also updates the ID.
     */
    @PropertyName("_about")
    void setIri(String iri);

    /**
     * @return The label object for Unified Astronomy Thesaurus search results.
     */
    AstroThesaurusDocLabel getPrefLabel();

    /**
     * @param label The label object for Unified Astronomy Thesaurus search results.
     */
    void setPrefLabel(AstroThesaurusDocLabel label);
}
