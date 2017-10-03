package org.iplantc.de.client.models.diskResources;

/**
 * The Attribute types the UI can display in a Metadata Template form.
 *
 * @author psarando
 */
public enum MetadataTemplateAttributeType {
    BOOLEAN("Boolean"),
    ENUM("Enum"),
    INTEGER("Integer"),
    MULTILINE("Multiline Text"),
    NUMBER("Number"),
    OLS_ONTOLOGY_TERM("OLS Ontology Term"),
    STRING("String"),
    TIMESTAMP("Timestamp"),
    UAT_ONTOLOGY_TERM("UAT Ontology Term"),
    URL("URL/URI");

    private String label;

    MetadataTemplateAttributeType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
