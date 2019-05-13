package org.iplantc.de.client.services;

import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.shared.DECallback;

import com.google.web.bindery.autobean.shared.Splittable;

public interface QuickLaunchServiceFacade {

    void createQuickLaunch(String name,
                           String description,
                           boolean isPublic,
                           AppTemplate at,
                           JobExecution je,
                           DECallback<Splittable> callback);

    void listQuickLaunches(String appId,
                           DECallback<Splittable> callback);

    void deleteQuickLaunch(String qid,
                           DECallback<Splittable> callback);

    void reLaunchInfo(String qid,
                      DECallback<AppTemplate> callback);
}
