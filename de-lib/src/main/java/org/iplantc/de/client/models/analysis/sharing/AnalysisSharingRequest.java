package org.iplantc.de.client.models.analysis.sharing;

import org.iplantc.de.client.models.sharing.SharingSubject;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram on 3/8/16.
 */
public interface AnalysisSharingRequest {

    SharingSubject getSubject();

    void setSubject(SharingSubject subject);

    @AutoBean.PropertyName("analyses")
    List<AnalysisPermission> getAnalysisPermissions();

    @AutoBean.PropertyName("analyses")
    void setAnalysisPermissions(List<AnalysisPermission> appPerms);

}
