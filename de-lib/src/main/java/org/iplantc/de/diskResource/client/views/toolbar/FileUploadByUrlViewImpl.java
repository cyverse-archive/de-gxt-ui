package org.iplantc.de.diskResource.client.views.toolbar;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.validators.ImportUrlValidator;
import org.iplantc.de.diskResource.client.FileUploadByUrlView;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

import java.util.Map;
import java.util.Set;

public class FileUploadByUrlViewImpl extends Composite implements FileUploadByUrlView {

    private static final FileUploadByUrlViewUiBinder UIBINDER = GWT.create(FileUploadByUrlViewUiBinder.class);
    interface FileUploadByUrlViewUiBinder extends UiBinder<Widget, FileUploadByUrlViewImpl> {}

    @UiField HTML htmlDestText;
    @UiField TextArea url0, url1, url2, url3, url4;
    @UiField Status formStatus0, formStatus1, formStatus2, formStatus3, formStatus4;
    @UiField(provided = true) final FileUploadByUrlView.FileUploadByUrlViewAppearance appearance;
    private DiskResourceUtil diskResourceUtil;

    private final Set<Map.Entry<Field<String>, Status>> pendingList = Sets.newHashSet();
    private final Map<Field<String>, Status> fieldToStatusMap = Maps.newHashMap();
    private final Folder uploadDest;

    @Inject
    public FileUploadByUrlViewImpl(FileUploadByUrlViewAppearance appearance,
                                   DiskResourceUtil diskResourceUtil,
                                   @Assisted Folder uploadDest) {
        this.appearance = appearance;
        this.diskResourceUtil = diskResourceUtil;
        this.uploadDest = uploadDest;

        initWidget(UIBINDER.createAndBindUi(this));

        // Load up our field to status map
        fieldToStatusMap.put(url0, formStatus0);
        fieldToStatusMap.put(url1, formStatus1);
        fieldToStatusMap.put(url2, formStatus2);
        fieldToStatusMap.put(url3, formStatus3);
        fieldToStatusMap.put(url4, formStatus4);
        String destPath = uploadDest.getPath();
        htmlDestText.setHTML(appearance.destText(destPath, diskResourceUtil.parseNameFromPath(destPath)));
    }

    @UiFactory
    SimpleContainer buildHlc() {
        SimpleContainer hlc = new SimpleContainer();
        hlc.setSize(appearance.containerWidth(), appearance.containerHeight());
        return hlc;
    }

    @UiFactory
    TextArea buildUrlField() {
        TextArea urlField = new TextArea();
        urlField.addValidator(new ImportUrlValidator());
        urlField.setAutoValidate(true);
        return urlField;
    }

    @UiFactory
    Status createFormStatus() {
        Status status = new Status();
        status.setWidth(15);
        return status;
    }

    @UiHandler({ "url0", "url1", "url2", "url3", "url4"})
    void onFieldValid(ValidEvent event) {
        fireEvent(event);
    }

    @UiHandler({"url0", "url1", "url2", "url3", "url4"})
    void onFieldInvalid(InvalidEvent event){
        fireEvent(event);
    }

    @Override
    public boolean addPending(Map.Entry<Field<String>, Status> obj) {
        return pendingList.add(obj);
    }

    @Override
    public boolean hasPending() {
        return !pendingList.isEmpty();
    }

    @Override
    public boolean removePending(Map.Entry<Field<String>, Status> obj) {
        return pendingList.remove(obj);
    }

    @Override
    public int getNumPending() {
        return pendingList.size();
    }

    @Override
    public boolean isValidForm(){
        for(Map.Entry<Field<String>, Status> entry : fieldToStatusMap.entrySet()){
            ValueBaseField<String> valueBaseField = (ValueBaseField<String>)entry.getKey();
            if ((valueBaseField.getCurrentValue() != null) && !valueBaseField.getCurrentValue().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<Field<String>, Status> getFieldToStatusMap() {
        return fieldToStatusMap;
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidEvent.InvalidHandler handler) {
        return addHandler(handler, InvalidEvent.getType());
    }

    @Override
    public HandlerRegistration addValidHandler(ValidEvent.ValidHandler handler) {
        return addHandler(handler, ValidEvent.getType());
    }
}
