package org.iplantc.de.fileViewers.client.callbacks;

import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.views.dialogs.IplantInfoBox;
import org.iplantc.de.shared.DataCallback;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.sencha.gxt.core.shared.FastMap;

import java.util.List;

public class EnsemblUtil {

    public interface EnsemblUtilAppearance {

        String indexFileMissing();

        String indexFileMissingError();
    }

    private final IsMaskable container;
    private final List<DiskResource> resourcesToSend;
    private final EnsemblUtilAppearance appearance;
    private final DiskResourceUtil diskResourceUtil;

    public EnsemblUtil(final List<DiskResource> resourcesToSend, final IsMaskable container) {
        this(resourcesToSend,
             container,
             DiskResourceUtil.getInstance(),
             GWT.<EnsemblUtilAppearance>create(EnsemblUtilAppearance.class));
    }

    EnsemblUtil(final List<DiskResource> resourcesToSend,
                final IsMaskable container,
                final DiskResourceUtil diskResourceUtil,
                final EnsemblUtilAppearance appearance) {
        this.resourcesToSend = resourcesToSend;
        this.container = container;
        this.appearance = appearance;
        this.diskResourceUtil = diskResourceUtil;
    }

    public void sendToEnsembl(final DiskResourceServiceFacade diskResourceServiceFacade) {
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
                                                  IplantInfoBox info =
                                                          new IplantInfoBox(SafeHtmlUtils.fromTrustedString(
                                                                  appearance.indexFileMissing()),
                                                                            SafeHtmlUtils.fromTrustedString(
                                                                                    appearance.indexFileMissingError()));
                                                  info.show();
                                                  if (container != null) {
                                                      container.unmask();
                                                  }
                                              }

                                              @Override
                                              public void onSuccess(FastMap<DiskResource> result) {
                                                  HasPaths diskResourcePaths =
                                                          diskResourceServiceFacade.getDiskResourceFactory()
                                                                                   .pathsList()
                                                                                   .as();
                                                  diskResourcePaths.setPaths(Lists.newArrayList(pathMap.keySet()));

                                                  diskResourceServiceFacade.shareWithAnonymous(
                                                          diskResourcePaths,
                                                          new ShareAnonymousCallback(container, diskResourceUtil));
                                              }
                                          });
    }

}
