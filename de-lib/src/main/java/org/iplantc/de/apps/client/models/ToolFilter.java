package org.iplantc.de.apps.client.models;

/**
 * Created by sriram on 5/4/17.
 */
public enum ToolFilter {

        ALL("All"), PUBLIC("Public tools"), MY_TOOLS("Only my tools");

        private String filter;

        private ToolFilter(String label) {
            this.filter = label;
        }

        public String getFilterString() {
            return toString();
        }

        @Override
        public String toString() {
            return filter;
        }
}
