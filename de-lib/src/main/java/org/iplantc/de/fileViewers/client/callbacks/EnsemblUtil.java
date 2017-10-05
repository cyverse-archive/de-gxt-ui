package org.iplantc.de.fileViewers.client.callbacks;

import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.errors.diskResources.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.client.models.errors.diskResources.ErrorGetStat;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.views.dialogs.FileSelectDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DataCallback;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EnsemblUtil {

    public interface EnsemblUtilAppearance {

        String indexFileMissing();

        String indexFileMissingError();
    }

    @Inject
    EnsemblUtilAppearance appearance;
    @Inject
    DiskResourceErrorAutoBeanFactory factory;
    @Inject
    DiskResourceUtil diskResourceUtil;
    @Inject
    AsyncProviderWrapper<FileSelectDialog> dialogAsyncProviderWrapper;

    @Inject
    public EnsemblUtil() {

    }

    public void sendToEnsembl(List<DiskResource> resourcesToSend,
                              final DiskResourceServiceFacade diskResourceServiceFacade,
                              IsMaskable container) {
        final FastMap<TYPE> pathMap = new FastMap<>();

        for (DiskResource resource : resourcesToSend) {
            final String path = resource.getPath();
            final String infoType = resource.getInfoType();

            pathMap.put(path, TYPE.FILE);

            String indexFilePath = null;
            if (InfoType.BAM.toString().equals(infoType)) {
                indexFilePath = path + ".bai";
            } else if (InfoType.VCF.toString().equals(infoType)) {
                indexFilePath = path + ".tbi";
            }

            if (!Strings.isNullOrEmpty(indexFilePath)) {
                pathMap.put(indexFilePath, TYPE.FILE);
            }
        }

        diskResourceServiceFacade.getStat(pathMap,
                                          new DataCallback<FastMap<DiskResource>>() {

                                              @Override
                                              public void onFailure(Integer statusCode,
                                                                    Throwable caught) {
                                                  ErrorGetStat egs = AutoBeanCodex.decode(factory,
                                                                                          ErrorGetStat.class,
                                                                                          caught.getMessage())
                                                                                  .as();
                                                  IPlantDialog dialog = new IPlantDialog();
                                                  dialog.setHeading(SafeHtmlUtils.fromTrustedString(
                                                          appearance.indexFileMissing()));
                                                  VerticalLayoutContainer vlc =
                                                          new VerticalLayoutContainer();
                                                  vlc.add(new HTML(egs.generateErrorMsg()));
                                                  vlc.add(new HTML(appearance.indexFileMissingError()));
                                                  dialog.add(vlc);
                                                  dialog.getOkButton().setText("Select missing files");
                                                  dialog.getOkButton()
                                                        .addSelectHandler(event -> dialogAsyncProviderWrapper
                                                                .get(new FileSelectDialogAsyncCallback(
                                                                        resourcesToSend,
                                                                        diskResourceServiceFacade,
                                                                        container)));

                                                  dialog.show();
                                                  if (container != null) {
                                                      container.unmask();
                                                  }
                                              }

                                              @Override
                                              public void onSuccess(FastMap<DiskResource> result) {
                                                  shareWithAnon(diskResourceServiceFacade,
                                                                Lists.newArrayList(pathMap.keySet()),
                                                                container);
                                              }
                                          });
    }

    protected void shareWithAnon(DiskResourceServiceFacade diskResourceServiceFacade,
                                 List<String> paths,
                                 IsMaskable container) {
        HasPaths diskResourcePaths = diskResourceServiceFacade.getDiskResourceFactory().pathsList().as();
        diskResourcePaths.setPaths(paths);

        diskResourceServiceFacade.shareWithAnonymous(diskResourcePaths,
                                                     new ShareAnonymousCallback(container,
                                                                                diskResourceUtil));
    }

    private class FileSelectDialogAsyncCallback implements AsyncCallback<FileSelectDialog> {


        private final List<DiskResource> resourcesToSend;
        private final DiskResourceServiceFacade diskResourceServiceFacade;
        private final IsMaskable container;

        FileSelectDialogAsyncCallback(List<DiskResource> resourcesToSend,
                                      final DiskResourceServiceFacade diskResourceServiceFacade,
                                      IsMaskable container) {
            this.resourcesToSend = resourcesToSend;
            this.diskResourceServiceFacade = diskResourceServiceFacade;
            this.container = container;

        }

        @Override
        public void onFailure(Throwable throwable) {

        }

        @Override
        public void onSuccess(FileSelectDialog dialog1) {
            dialog1.addHideHandler(new FileDialogHideHandler(dialog1, resourcesToSend,
                                                             diskResourceServiceFacade,
                                                             container));
            dialog1.show(false, null, null, null);


        }
    }

    private class FileDialogHideHandler implements HideEvent.HideHandler {
        private final TakesValue<List<File>> takesValue;
        private final List<DiskResource> resourceToSend;
        private final DiskResourceServiceFacade diskResourceServiceFacade;
        private final IsMaskable container;

        public FileDialogHideHandler(TakesValue<List<File>> dlg, List<DiskResource> resourcesToSend,
                                     final DiskResourceServiceFacade diskResourceServiceFacade,
                                     IsMaskable container) {
            this.takesValue = dlg;
            this.resourceToSend = resourcesToSend;
            this.diskResourceServiceFacade = diskResourceServiceFacade;
            this.container = container;
        }

        @Override
        public void onHide(HideEvent event) {
            if ((takesValue.getValue() == null) || takesValue.getValue().isEmpty()
                || diskResourceUtil.containsFilteredItems(takesValue.getValue())) {
                return;
            }
            resourceToSend.addAll(takesValue.getValue());
            List<String> paths = resourceToSend.stream().map(DiskResource ::getPath).collect(Collectors.toList());
            shareWithAnon(diskResourceServiceFacade, paths, container);
        }
    }
}
