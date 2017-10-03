package org.iplantc.de.client.models.ontologies;

/**
 * An AutoBean model for Unified Astronomy Thesaurus search responses.
 *
 * @author psarando
 */
public interface AstroThesaurusResponse {
    /**
     * @return The UAT search results.
     */
    AstroThesaurusResult getResult();
}
