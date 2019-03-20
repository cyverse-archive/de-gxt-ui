package org.iplantc.de.diskResource.client.views.metadata;

import org.iplantc.de.diskResource.client.MetadataView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "metadata")
public class ReactMetadataViews {

    @JsProperty
    public static ComponentConstructorFn<EditMetadataProps> EditMetadata;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class EditMetadataProps extends BaseProps {
        public MetadataView.Presenter presenter;
        public Splittable targetResource;
        public Splittable metadata;
        public boolean open;
        public boolean editable;
        public boolean loading;
    }

    @JsProperty
    public static ComponentConstructorFn<MetadataTemplateViewProps> MetadataTemplateView;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class MetadataTemplateViewProps extends BaseProps {
        public MetadataView.Presenter presenter;
        public Splittable template;
        public Splittable metadata;
        public boolean open;
        public boolean writable;
    }
}
