package org.iplantc.de.analysis.client.views.status;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;

/**
 * Created by sriram on 11/18/16.
 */
public interface HelpRendererTemplates extends XTemplates {

    @XTemplates.XTemplate(source="condor-submitted.html")
    public SafeHtml renderCondorSubmitted(Analysis analysis);

    @XTemplates.XTemplate(source="agave-submitted.html")
    public SafeHtml renderAgaveSubmitted(Analysis analysis);

    @XTemplates.XTemplate(source="condor-running.html")
    public SafeHtml renderCondorRunning(Analysis analysis);

    @XTemplates.XTemplate(source="agave-running.html")
    public SafeHtml renderAgaveRunning(Analysis analysis);

    @XTemplates.XTemplate(source="completed-nooutput.html")
    public SafeHtml renderCompletedNoOutput(Analysis analysis);

    @XTemplates.XTemplate(source="completed-unexpected.html")
    public SafeHtml renderCompletedUnExpected(Analysis analysis);

    @XTemplates.XTemplate(source="failed.html")
    public SafeHtml renderFailed(Analysis analysis);
}
