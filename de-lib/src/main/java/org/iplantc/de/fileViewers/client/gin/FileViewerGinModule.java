package org.iplantc.de.fileViewers.client.gin;

import org.iplantc.de.fileViewers.client.FileViewer;
import org.iplantc.de.fileViewers.client.callbacks.GenomeBrowserUtil;
import org.iplantc.de.fileViewers.client.presenter.FileViewerPresenterImpl;
import org.iplantc.de.fileViewers.client.presenter.MimeTypeViewerResolverFactory;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author jstroot
 */
public class FileViewerGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(FileViewer.Presenter.class).to(FileViewerPresenterImpl.class);
        bind(MimeTypeViewerResolverFactory.class);
        bind(GenomeBrowserUtil.class);
    }

}
