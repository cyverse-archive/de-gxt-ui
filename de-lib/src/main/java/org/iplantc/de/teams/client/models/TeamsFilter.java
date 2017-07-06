package org.iplantc.de.teams.client.models;

/**
 * An enum to represent how users can filter between personal teams and all public teams
 * @author aramsey
 */
public enum TeamsFilter {
    ALL("All Teams"), MY_TEAMS("My Teams");

    private String filter;

    TeamsFilter(String filter) {
        this.filter = filter;
    }

    public String getFilterString() {
        return toString();
    }

    @Override
    public String toString() {
        return filter;
    }
}
