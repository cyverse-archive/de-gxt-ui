package org.iplantc.de.client.models.diskResources;

public enum MetadataTemplateAttributeType {
    BOOLEAN("Boolean"),
    ENUM("Enum"),
    INTEGER("Integer"),
    MULTILINE("Multiline Text"),
    NUMBER("Number"),
    OLS_ONTOLOGY_TERM("OLS Ontology Term"),
    STRING("String"),
    TIMESTAMP("Timestamp"),
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
