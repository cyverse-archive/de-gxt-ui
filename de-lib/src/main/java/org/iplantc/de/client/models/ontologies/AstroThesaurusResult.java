package org.iplantc.de.client.models.ontologies;

import java.util.List;

/**
 * An AutoBean model for Unified Astronomy Thesaurus search results.
 *
 * @author psarando
 */
public interface AstroThesaurusResult {
    /**
     * @return The current page of results returned.
     */
    int getPage();

    /**
     * @return The start index of results returned.
     */
    int getStartIndex();

    /**
     * @return The list of UAT search result items.
     */
    List<AstroThesaurusDoc> getItems();

    /**
     * @param items The list of UAT search result items.
     */
    void setItems(List<AstroThesaurusDoc> items);
}
