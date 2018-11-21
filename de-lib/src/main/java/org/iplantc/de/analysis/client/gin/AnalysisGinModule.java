package org.iplantc.de.analysis.client.gin;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.AnalysisParametersView;
import org.iplantc.de.analysis.client.gin.factory.AnalysisSharingPresenterFactory;
import org.iplantc.de.analysis.client.presenter.AnalysesPresenterImpl;
import org.iplantc.de.analysis.client.presenter.parameters.AnalysisParametersPresenterImpl;
import org.iplantc.de.analysis.client.presenter.sharing.AnalysisSharingPresenter;
import org.iplantc.de.analysis.client.views.AnalysesViewImpl;
import org.iplantc.de.analysis.client.views.sharing.AnalysisSharingView;
import org.iplantc.de.analysis.client.views.sharing.AnalysisSharingViewImpl;
import org.iplantc.de.commons.client.presenter.SharingPresenter;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * @author jstroot
 */
public class AnalysisGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(AnalysesView.Presenter.class).to(AnalysesPresenterImpl.class);
        bind(AnalysisParametersView.Presenter.class).to(AnalysisParametersPresenterImpl.class);
        bind(AnalysesView.class).to(AnalysesViewImpl.class);
        install(new GinFactoryModuleBuilder()
                        .implement(SharingPresenter.class, AnalysisSharingPresenter.class)
                        .build(AnalysisSharingPresenterFactory.class));
        bind(AnalysisSharingView.class).to(AnalysisSharingViewImpl.class);
    }
}
