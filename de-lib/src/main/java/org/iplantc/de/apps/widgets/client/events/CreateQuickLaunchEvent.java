/**
 *
 *
 * @author sriram
 *
 */

package org.iplantc.de.apps.widgets.client.events;

import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.JobExecution;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class CreateQuickLaunchEvent extends GwtEvent<CreateQuickLaunchEvent.CreateQuickLaunchEventHandler> {
    public interface HasCreateQuickLaunchEventHandlers {
        HandlerRegistration addCreateQuickLaunchEventHandler(CreateQuickLaunchEventHandler handler);
    }

    public interface CreateQuickLaunchEventHandler extends EventHandler {
        void onCreateQuickLaunchRequest(AppTemplate appTemplate,
                                        JobExecution jobExecution);
    }

    public static final GwtEvent.Type<CreateQuickLaunchEventHandler> TYPE = new GwtEvent.Type<>();
    private final AppTemplate appTemplate;
    private final JobExecution jobExecution;

    public CreateQuickLaunchEvent(AppTemplate appTemplate, JobExecution jobExecution) {
        this.appTemplate = appTemplate;
        this.jobExecution = jobExecution;
    }

    @Override
    public GwtEvent.Type<CreateQuickLaunchEventHandler> getAssociatedType() {
        return  TYPE;
    }

    @Override
    public  void  dispatch(CreateQuickLaunchEventHandler handler) {
        handler.onCreateQuickLaunchRequest(appTemplate, jobExecution);
    }



}
