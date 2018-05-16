package org.iplantc.de.client.models.analysis;

/**
 * Created by sriram on 3/4/16.
 */
public enum AnalysisFilter {

    ALL("All"), SHARED_WITH_ME("Analyses shared with me"), MY_ANALYSES("Only my analyses"), INTERACTIVE_ANALYSES("Only interactive analyses");

    private String filter;

    private AnalysisFilter(String label) {
           this.filter = label;
    }

    public String getFilterString() {
        return toString();
    }

    @Override
    public String toString() {
        return filter;
    }

    public static AnalysisFilter fromTypeString(String typeString) {
        if (typeString == null || typeString.isEmpty()) {
            return null;
        }
        String temp = typeString.replaceAll("\\s", "");
        return valueOf(temp.toUpperCase());
    }
}
