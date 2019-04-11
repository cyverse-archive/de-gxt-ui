package org.iplantc.de.client.services;

import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.shared.DECallback;

public interface QuickLaunchServiceFacade {

    void createQuickLaunch(AppTemplate at, JobExecution je, DECallback<String> callback);
}
