package org.iplantc.de.preferences.client.gin;

import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.client.presenter.PreferencesPresenterImpl;
import org.iplantc.de.preferences.client.view.PreferencesViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author aramsey
 */
public class PreferencesGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(PreferencesView.class).to(PreferencesViewImpl.class);
        bind(PreferencesView.Presenter.class).to(PreferencesPresenterImpl.class);
    }
}
