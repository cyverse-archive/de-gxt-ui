package org.iplantc.de.commons.client.views.dialogs;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.share.CommonsModule;
import org.iplantc.de.diskResource.client.events.FileUploadedEvent;
import org.iplantc.de.diskResource.client.presenters.callbacks.DuplicateDiskResourceCallback;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jstroot sriram
 */
public class SimpleFileUploadDialog extends AbstractFileUploadDialog {

    private final DiskResourceAutoBeanFactory FS_FACTORY = GWT.create(DiskResourceAutoBeanFactory.class);
    private final SimpleFileUploadPanelUiBinder BINDER = GWT.create(SimpleFileUploadPanelUiBinder.class);
    @UiField(provided = true) final AbstractFileUploadDialogAppearance appearance;

    @UiTemplate("AbstractFileUploadPanel.ui.xml")
    interface SimpleFileUploadPanelUiBinder extends UiBinder<Widget, SimpleFileUploadDialog> {
    }

    private final HasPath uploadDest;
    private final DiskResourceServiceFacade drService;
    private final String userName;
    private final EventBus eventBus;
    private final DiskResourceUtil diskResourceUtil;
    private final DEClientConstants constants = GWT.create(DEClientConstants.class);
    private final DiskResourceNameValidator validator = new DiskResourceNameValidator();

    public SimpleFileUploadDialog(final HasPath uploadDest,
                                  final DiskResourceServiceFacade drService,
                                  final EventBus eventBus,
                                  final DiskResourceUtil diskResourceUtil,
                                  final SafeUri fileUploadServlet,
                                  final String userName) {
        super(fileUploadServlet);

        this.uploadDest = uploadDest;
        this.drService = drService;
        this.eventBus = eventBus;
        this.userName = userName;
        this.diskResourceUtil = diskResourceUtil;
        appearance = GWT.create(AbstractFileUploadDialogAppearance.class);

        add(BINDER.createAndBindUi(this));
        ensureDebugId(CommonsModule.UploadIds.BASE_ID);

        afterBinding(); //must be called after UIBinder

        initDestPathLabel();
    }

    void initDestPathLabel() {
        String destPath = uploadDest.getPath();
        final String parentPath = diskResourceUtil.parseNameFromPath(destPath);
        htmlDestText.setHTML(appearance.renderDestinationPathLabel(destPath, parentPath));
    }

    @Override
    HorizontalLayoutContainer createHLC() {
        HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
        hlc.add(new Hidden(HDN_PARENT_ID_KEY, uploadDest.getPath()));
        hlc.add(new Hidden(HDN_USER_ID_KEY, userName));
        return hlc;
    }


    @Override
    protected void onSubmitComplete(List<FileUpload> fufList,
                                    List<Status> statList,
                                    List<FormPanel> submittedForms,
                                    List<FormPanel> formList,
                                    SubmitCompleteEvent event) {
        if (submittedForms.contains(event.getSource())) {
            submittedForms.remove(event.getSource());
            statList.get(formList.indexOf(event.getSource())).clearStatus("");
        }

        FileUpload field = fufList.get(formList.indexOf(event.getSource()));
        String results2 = event.getResults();
        String fieldValue = appearance.getFileName(field.getFilename());

        Splittable bodySp = StringQuoter.createSplittable();
        StringQuoter.create(fieldValue).assign(bodySp, "name");
        StringQuoter.create(uploadDest.getPath()).assign(bodySp, "destination");
        IntercomFacade.trackEvent(TrackingEventType.SIMPLE_UPLOAD_SUBMITTED,bodySp);

        if (Strings.isNullOrEmpty(results2)) {
            IplantAnnouncer.getInstance()
                           .schedule(new ErrorAnnouncementConfig(appearance.fileUploadsFailed(Lists.newArrayList(
                                   fieldValue))));
        } else {
            String results = Format.stripTags(results2);
            Splittable split = StringQuoter.split(results);

            if (split == null) {
                IplantAnnouncer.getInstance()
                               .schedule(new ErrorAnnouncementConfig(appearance.fileUploadsFailed(Lists.newArrayList(
                                       fieldValue))));
            } else {
                if (split.isUndefined("file") || (split.get("file") == null)) {
                    IplantAnnouncer.getInstance()
                                   .schedule(new ErrorAnnouncementConfig(appearance.fileUploadsFailed(
                                           Lists.newArrayList(appearance.getFileName(field.getFilename())))));
                } else {
                    eventBus.fireEvent(new FileUploadedEvent(uploadDest, field.getFilename(), results));
                }
            }
        }

        if (submittedForms.size() == 0) {
            hide();
        }

    }

    @Override
    protected void doUpload(List<FileUpload> fufList,
                            List<Status> statList,
                            List<FormPanel> submittedForms,
                            List<FormPanel> formList) {
        FastMap<FileUpload> destResourceMap = new FastMap<>();

        if (checkFileSize(fufList) && checkFileNameForSplChars(fufList)) {
            for (FileUpload field : fufList) {
                String fileName = appearance.getFileName(field.getFilename()).replaceAll(".*[\\\\/]", "");
                field.setEnabled(!Strings.isNullOrEmpty(fileName) && !fileName.equalsIgnoreCase("null"));
                if (field.isEnabled()) {
                    destResourceMap.put(uploadDest.getPath() + "/" + fileName, field);
                } else {
                    field.setEnabled(false);
                }
            }
        }

        if (!destResourceMap.isEmpty()) {
            final ArrayList<String> ids = Lists.newArrayList(destResourceMap.keySet());
            final HasPaths dto = FS_FACTORY.pathsList().as();
            dto.setPaths(ids);
            final CheckDuplicatesCallback cb = new CheckDuplicatesCallback(ids, destResourceMap, statList, fufList, submittedForms, formList);
            drService.diskResourcesExist(dto, cb);
        }

    }

    private boolean checkFileSize(List<FileUpload> files) {
        List<String> filenames = files.stream()
                                      .filter(f -> getSize(f.getElement())
                                                   > constants.maxFileSizeForSimpleUpload())
                                      .map(FileUpload::getFilename)
                                      .map(name -> appearance.getFileName(name))
                                      .collect(Collectors.toList());

        if (filenames == null || filenames.size() == 0) {
            return true;
        }

        String joined = Joiner.on(',').join(filenames);
        AlertMessageBox amb = new AlertMessageBox(appearance.maxFileSizeExceed(),
                                                  appearance.fileSizeViolation(joined));
        amb.show();
        return false;
    }

    private boolean checkFileNameForSplChars(List<FileUpload> files) {
        List<String> filenames = new ArrayList<>();
        for (FileUpload file : files) {
            if (!Strings.isNullOrEmpty(validator.validateAndReturnError(file.getFilename()))) {
                filenames.add(appearance.getFileName(file.getFilename()));
            }
        }

        if (filenames == null || filenames.size() == 0) {
            return true;
        }

        String joined = Joiner.on(',').join(filenames);
        AlertMessageBox amb = new AlertMessageBox(appearance.invalidFileName(),
                                                  appearance.fileNameValidationMsg() + joined);
        amb.show();
        return false;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        con.asWidget().ensureDebugId(baseID + CommonsModule.UploadIds.BASE_ID);
        fuf0.getElement()
            .setId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.FILE_UPLOAD_FIELD
                   + CommonsModule.UploadIds.FIELD1);
        fuf1.getElement()
            .setId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.FILE_UPLOAD_FIELD
                   + CommonsModule.UploadIds.FIELD2);
        fuf2.getElement()
            .setId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.FILE_UPLOAD_FIELD
                   + CommonsModule.UploadIds.FIELD3);
        fuf3.getElement()
            .setId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.FILE_UPLOAD_FIELD
                   + CommonsModule.UploadIds.FIELD4);
        fuf4.getElement()
            .setId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.FILE_UPLOAD_FIELD
                   + CommonsModule.UploadIds.FIELD5);

        btn0.ensureDebugId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.RESET_BTN1);
        btn1.ensureDebugId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.RESET_BTN2);
        btn2.ensureDebugId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.RESET_BTN3);
        btn3.ensureDebugId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.RESET_BTN4);
        btn4.ensureDebugId(CommonsModule.UploadIds.BASE_ID + CommonsModule.UploadIds.RESET_BTN5);
    }

    private final class CheckDuplicatesCallback extends DuplicateDiskResourceCallback {
        private final FastMap<FileUpload> destResourceMap;
        private final List<Status> statList;
        private final List<FileUpload> fufList;
        private final List<FormPanel> submittedForms;
        private final List<FormPanel> formList;

        public CheckDuplicatesCallback(List<String> ids, FastMap<FileUpload> destResourceMap,
                                       List<Status> statList, List<FileUpload> fufList, List<FormPanel> submittedForms,
                                       List<FormPanel> formList) {
            super(ids, null);
            this.destResourceMap = destResourceMap;
            this.statList = statList;
            this.fufList = fufList;
            this.submittedForms = submittedForms;
            this.formList = formList;
        }

        @Override
        public void markDuplicates(Collection<String> duplicates) {
            if ((duplicates != null) && !duplicates.isEmpty()) {
                String dupeFiles =   Joiner.on(',').join(duplicates);
                AlertMessageBox amb = new AlertMessageBox(appearance.fileExistTitle(),
                                                          appearance.fileExists(dupeFiles));
                amb.show();
            } else {
                for (final FileUpload field : destResourceMap.values()) {
                    int index = fufList.indexOf(field);
                    statList.get(index).setBusy("");
                    FormPanel form = formList.get(index);
                    form.addSubmitHandler(event -> {
                        if (event.isCanceled()) {
                            IplantAnnouncer.getInstance()
                                           .schedule(new ErrorAnnouncementConfig(appearance.fileUploadsFailed(
                                                   Lists.newArrayList(field.getFilename()))));
                        }

                        getOkButton().disable();
                    });
                    try {
                        form.submit();
                    } catch(Exception e ) {
                        GWT.log("\nexception on submit\n" + e.getMessage());
                        IplantAnnouncer.getInstance()
                                       .schedule(new ErrorAnnouncementConfig(appearance.fileUploadsFailed(
                                               Lists.newArrayList(field.getFilename()))));
                    }
                    submittedForms.add(form);
                }
            }
        }
    }
}
