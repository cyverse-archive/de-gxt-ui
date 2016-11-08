package org.iplantc.de.apps.client.presenter.callbacks;

import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppFeedback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.AppsCallback;

/**
 * @author jstroot
 */
public class DeleteRatingCallback extends AppsCallback<AppFeedback> {
    private final App appToUnRate;
    private final EventBus eventBus;

    public DeleteRatingCallback(final App appToUnRate,
                                final EventBus eventBus) {
        this.appToUnRate = appToUnRate;
        this.eventBus = eventBus;
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        ErrorHandler.post(caught);
    }

    @Override
    public void onSuccess(AppFeedback result) {
        appToUnRate.setRating(result);

        eventBus.fireEvent(new AppUpdatedEvent(appToUnRate));
    }
}
