package org.iplantc.de.client.models;

/**
 * Created by sriram on 5/17/18.
 */
public enum AnalysisTypeFilter {
    ALL("All"), DE("DE"), AGAVE("Agave"), OSG("OSG"), INTERACTIVE("Interactive");

    private String filter;

    private AnalysisTypeFilter(String label) {
        this.filter = label;
    }

    public String getFilterString() {
        return toString();
    }

    @Override
    public String toString() {
        return filter;
    }

    public static AnalysisTypeFilter fromTypeString(String typeString) {
        if (typeString == null || typeString.isEmpty()) {
            return null;
        }
        String temp = typeString.replaceAll("\\s", "");
        return valueOf(temp.toUpperCase());
    }

}
