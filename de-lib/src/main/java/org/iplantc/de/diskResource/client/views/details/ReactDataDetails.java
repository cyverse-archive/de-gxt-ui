package org.iplantc.de.diskResource.client.views.details;

import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.DetailsView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 2/26/18.
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.data.details", name = "BasicDetailsWithI18N")
public class ReactDataDetails {

    @JsProperty(namespace = "CyVerseReactComponents.data.details", name = "BasicDetailsWithI18N")
    public static ReactClass<DataDetailsProps> dataDetails;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class DataDetailsProps extends BaseProps {
        public DiskResourceUtil drUtil;
        public Splittable data;
        public DetailsView.Appearance appearance;
        public String owner;
        public DetailsView view;
        public String[] infoTypes;
        public Boolean isFolder;
        public DetailsView.Presenter presenter;
        public String DETAILS_LAST_MODIFIED;
        public String DETAILS_DATE_SUBMITTED ;
        public String DETAILS_PERMISSIONS;
        public String DETAILS_SHARE;
        public String DETAILS_SIZE;
        public String DETAILS_TYPE;
        public String DETAILS_INFO_TYPE;
        public String DETAILS_MD5;
        public String DETAILS_SEND_TO;
        public String DETAILS_TAGS;
    }

}
