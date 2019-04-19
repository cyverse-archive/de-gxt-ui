package org.iplantc.de.client.util;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.models.diskResources.Folder;

/**
 *
 * @author sriram
 *
 */

public class JobExecutionUtil {

    public static JobExecution getExecutionForAppTemplate(AppTemplate appTemplate,
                                                          UserSettings userSettings,
                                                          AppTemplateAutoBeanFactory factory) {
        final JobExecution je = factory.jobExecution().as();
        je.setSystemId(appTemplate.getSystemId());
        je.setAppTemplateId(appTemplate.getId());
        je.setRetainInputs(appTemplate.isRetainInputs());
        je.setEmailNotificationEnabled(userSettings.isEnableAnalysisEmailNotification());

        final Folder defaultOutputFolder = userSettings.getDefaultOutputFolder();
        if (defaultOutputFolder != null) {
            je.setOutputDirectory(defaultOutputFolder.getPath());
        }
        return je;
    }
}
