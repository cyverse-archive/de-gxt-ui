package org.iplantc.de.client.models.tool;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface InteractiveApp extends HasName {
    String getImage();
    void setImage(String image);

    @PropertyName("frontend_url")
    String getFrontendUrl();

    @PropertyName("frontend_url")
    void setFrontendUrl(String url);

    @PropertyName("cas_url")
    String getCasUrl();

    @PropertyName("cas_url")
    void setCasUrl(String url);

    @PropertyName("cas_validate")
    String getCasValidate();

    @PropertyName("cas_validate")
    void setCasValidate(String validate);

    @PropertyName("ssl_cert_path")
    String getSslCertPath();

    @PropertyName("ssl_cert_path")
    void setSslCertPath(String path);

    @PropertyName("ssl_key_path")
    String getSslKeyPath();

    @PropertyName("ssl_key_path")
    void setSslKeyPath(String path);

}
