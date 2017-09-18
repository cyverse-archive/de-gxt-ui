package org.iplantc.de.theme.base.client.diskResource.search;

import org.iplantc.de.client.models.ontologies.OntologyLookupServiceDoc;
import org.iplantc.de.diskResource.client.views.search.MetadataTermSearchField;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.core.client.XTemplates;

public class MetadataTermSearchFieldDefaultAppearance implements MetadataTermSearchField.MetadataTermSearchFieldAppearance {

    private final OntologySearchResultTemplate template;

    interface OntologySearchResultTemplate extends XTemplates {
        @XTemplate(source = "OntologySearchResult.html")
        SafeHtml render(String iri, String label, String ontology);
    }

    public MetadataTermSearchFieldDefaultAppearance() {
        this(GWT.create(OntologySearchResultTemplate.class));
    }

    public MetadataTermSearchFieldDefaultAppearance(OntologySearchResultTemplate template) {
        this.template = template;
    }

    @Override
    public void render(Context context, OntologyLookupServiceDoc ontologyClass, SafeHtmlBuilder sb) {
        sb.append(template.render(ontologyClass.getIri(), ontologyClass.getLabel(), ontologyClass.getOntologyPrefix()));
    }
}
