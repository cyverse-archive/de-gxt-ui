/**
 *
 */
package org.iplantc.de.tools.client.presenter;

import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.DiskResourceExistMap;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.toolRequests.NewToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestAutoBeanFactory;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.ToolRequestServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.shared.DataCallback;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;
import org.iplantc.de.tools.client.views.requests.UploadForm;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.List;
import java.util.Set;

/**
 * @author sriram
 */
public class NewToolRequestFormPresenterImpl implements NewToolRequestFormView.Presenter {

    private static final DiskResourceAutoBeanFactory FS_FACTORY =
            GWT.create(DiskResourceAutoBeanFactory.class);
    private static final ToolRequestAutoBeanFactory REQ_FACTORY = GWT.create(ToolRequestAutoBeanFactory.class);

    private final DiskResourceServiceFacade fsServices =
            ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
    private final ToolRequestServiceFacade reqServices =
            ServicesInjector.INSTANCE.getToolRequestServiceProvider();

    private final NewToolRequestFormView view;
    private final Command callback;
    private NewToolRequestFormView.SELECTION_MODE toolSelectionMode;
    private NewToolRequestFormView.SELECTION_MODE testDataSelectionMode;
    private NewToolRequestFormView.SELECTION_MODE otherDataSelectionMode;

    @Inject
    DiskResourceUtil diskResourceUtil;
    private NewToolRequestFormView.Mode mode;
    private Tool tool;

    @Inject
    NewToolRequestFormView.NewToolRequestFormViewAppearance appearance;

    @Inject
    NewToolRequestFormPresenterImpl(final NewToolRequestFormView view,
                                    @Assisted final Command callbackCmd) {
        this.view = view;
        this.callback = callbackCmd;
        view.setPresenter(this);
        setToolMode(NewToolRequestFormView.SELECTION_MODE.LINK);
        setTestDataMode(NewToolRequestFormView.SELECTION_MODE.UPLOAD);
        setOtherDataMode(NewToolRequestFormView.SELECTION_MODE.UPLOAD);
        view.setToolSelectionMode();
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.commons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget)
     */
    @Override
    public void go(final HasOneWidget container) {
        container.setWidget(view);
    }

    /**
     * @see NewToolRequestFormView.Presenter#onCancelBtnClick()
     */
    @Override
    public void onCancelBtnClick() {
        executeCallback();
    }

    /**
     * @see NewToolRequestFormView.Presenter#onSubmitBtnClick()
     */
    @Override
    public void onSubmitBtnClick() {
        if (isFormValid()) {
            view.indicateSubmissionStart();
            validateUploadsNew(getUploadFormsToSubmit(), submitCmd(indicateSuccessCmd));
        }
    }

    @Override
    public void setToolMode(NewToolRequestFormView.SELECTION_MODE mode) {
        toolSelectionMode = mode;
    }


    @Override
    public void setTestDataMode(NewToolRequestFormView.SELECTION_MODE mode) {
        testDataSelectionMode = mode;
    }

    @Override
    public void setOtherDataMode(NewToolRequestFormView.SELECTION_MODE mode) {
        otherDataSelectionMode = mode;
    }

    @Override
    public void onToolSelectionModeChange() {
        view.setToolSelectionMode();
    }

    @Override
    public void onTestDataSelectionModeChange() {
        view.setTestDataSelectMode();
    }

    @Override
    public void onOtherDataSeelctionModeChange() {
        view.setOtherDataSelectMode();
    }

    @Override
    public void setViewDebugId(String baseID) {
        view.asWidget().ensureDebugId(baseID);
    }
    public void setTool(Tool tool) {
        this.tool = tool;
        view.setTool(tool);
    }

    @Override
    public void setMode(NewToolRequestFormView.Mode mode) {
        this.mode = mode;
        view.setMode(mode);
    }

    private final Command indicateSuccessCmd = new Command() {
        @Override
        public void execute() {
            view.indicateSubmissionSuccess();
            executeCallback();
        }
    };

    private Command submitCmd(final Command onSuccess) {
        return () -> {
            final List<UploadForm> UploadForms = getUploadFormsToSubmit();
            if (UploadForms.size() > 0) {
                UploadMux.startUploads(UploadForms, new Callback<Void, Iterable<UploadForm>>() {
                    @Override
                    public void onFailure(final Iterable<UploadForm> failedUploadForms) {
                        onUploadFailure(UploadForms, failedUploadForms);
                    }

                    @Override
                    public void onSuccess(Void unused) {
                        submitRequest(UploadForms, onSuccess);
                    }
                });
            } else {
                submitRequest(UploadForms, onSuccess);
            }
        };
    }

    private void validateUploadsNew(final Iterable<UploadForm> UploadForms, final Command onValid) {
        final List<String> destFiles = Lists.newArrayList();
        for (UploadForm UploadForm : UploadForms) {
            String value = UploadForm.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                destFiles.add(makeDestinationPath(value));
            }
        }
        if (destFiles.size() > 0) {
            getDiskResourceExistMap(destFiles, checkExistenceCmd(UploadForms, onValid));
        } else {
            onValid.execute();
        }
    }

    private Continuation<DiskResourceExistMap> checkExistenceCmd(final Iterable<UploadForm> UploadForms,
                                                                 final Command onNoneExist) {
        return fsExistMap -> {
            boolean allNew = true;
            String dups = "";
            for (UploadForm UploadForm : UploadForms) {
                if (fsExistMap.get(makeDestinationPath(UploadForm.getValue()))) {
                    allNew = false;
                    dups += " " + UploadForm.getValue();
                }
            }
            if (allNew) {
                onNoneExist.execute();
            } else {
                view.indicateSubmissionFailure(I18N.ERROR.fileExists(dups));
            }
        };
    }

    private void getDiskResourceExistMap(final Iterable<String> files,
                                         final Continuation<DiskResourceExistMap> checkExistence) {
        final HasPaths dto = FS_FACTORY.pathsList().as();
        dto.setPaths(Lists.newArrayList(files));
        fsServices.diskResourcesExist(dto, new DataCallback<DiskResourceExistMap>() {
            @Override
            public void onFailure(Integer statusCode, final Throwable caught) {
                view.indicateSubmissionFailure(I18N.ERROR.newToolRequestError());
            }

            @Override
            public void onSuccess(final DiskResourceExistMap fsExistMap) {
                checkExistence.execute(fsExistMap);
            }
        });
    }

    private void submitRequest(final Iterable<UploadForm> UploadForms, final Command onSuccess) {
        final NewToolRequest req = getToolRequest();
        reqServices.requestInstallation(req, new AsyncCallback<ToolRequestDetails>() {
            @Override
            public void onFailure(final Throwable caught) {
                view.indicateSubmissionFailure(I18N.ERROR.newToolRequestError());
                removeUploads(UploadForms);
            }

            @Override
            public void onSuccess(final ToolRequestDetails response) {
                onSuccess.execute();
            }
        });
    }

    private boolean isFormValid() {
        boolean valid = view.isValid();
        if (!valid) {
            view.indicateSubmissionFailure(I18N.ERROR.invalidToolRequest());
            return false;
        }
        if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            valid = valid && view.getToolBinaryUploader().isValid();
            if (!valid) {
                return valid;
            }
        }
        if (testDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            valid = valid && view.getTestDataUploader().isValid();
            if (!valid) {
                return valid;
            }

        }
        if (otherDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            valid = valid && view.getOtherDataUploader().isValid();
            if (!valid) {
                return valid;
            }
        }

        if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            if (testDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
                valid = !areUploadsSame(view.getToolBinaryUploader(), view.getTestDataUploader())
                        && valid;
                if (!valid) {
                    return valid;
                }
            }

            if (otherDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
                valid = !areUploadsSame(view.getToolBinaryUploader(), view.getOtherDataUploader())
                        && valid;
                if (!valid) {
                    return valid;
                }
            }
        }

        if (testDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
                valid = !areUploadsSame(view.getToolBinaryUploader(), view.getTestDataUploader())
                        && valid;
                if (!valid) {
                    return valid;
                }
            }

            if (otherDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
                valid = !areUploadsSame(view.getTestDataUploader(), view.getOtherDataUploader())
                        && valid;
                if (!valid) {
                    return valid;
                }
            }
        }

        if (otherDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
                valid = !areUploadsSame(view.getToolBinaryUploader(), view.getOtherDataUploader())
                        && valid;
                if (!valid) {
                    return valid;
                }
            }

            if (testDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
                valid = !areUploadsSame(view.getTestDataUploader(), view.getOtherDataUploader())
                        && valid;
                if (!valid) {
                    return valid;
                }
            }
        }

        return valid;
    }

    private boolean areUploadsSame(final UploadForm lhs, final UploadForm rhs) {
        if (Strings.isNullOrEmpty(lhs.getValue()) || Strings.isNullOrEmpty(rhs.getValue())) {
            return false;
        }

        if (lhs.getValue().equals(rhs.getValue())) {
            view.indicateSubmissionFailure(appearance.sameFileError(lhs.getValue()));
            return true;
        }
        return false;
    }

    private void onUploadFailure(final Iterable<UploadForm> allUploadForms,
                                 final Iterable<UploadForm> failedUploadForms) {
        final Set<UploadForm> succUploadForms = Sets.newHashSet(allUploadForms);
        final List<String> failedFiles = Lists.newArrayList();
        for (UploadForm failure : failedUploadForms) {
            succUploadForms.remove(failure);
            failedFiles.add(failure.getValue());
        }
        view.indicateSubmissionFailure(I18N.ERROR.fileUploadsFailed(failedFiles));
        removeUploads(succUploadForms);
    }

    private NewToolRequest getToolRequest() {
        final NewToolRequest req = REQ_FACTORY.makeNewToolRequest().as();
        if (mode.equals(NewToolRequestFormView.Mode.MAKEPUBLIC)) {
            req.setToolId(tool.getId());
        }
        req.setName(view.getNameField().getValue());
        req.setDescription(view.getDescriptionField().getValue());
        req.setAttribution(view.getAttributionField().getValue());
        if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            req.setSourceFile(makeDestinationPath(getToolBinaryName()));
        } else if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.LINK)) {
            req.setSourceURL(view.getSourceURLField().getValue());
        } else if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.SELECT)) {
            req.setSourceFile(view.getBinSelectField().getValue().getPath());
        }
        req.setDocURL(view.getDocURLField().getValue());
        req.setVersion(view.getVersionField().getValue());

        if (testDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            req.setTestDataFile(makeDestinationPath(getTestDataName()));
        } else if (testDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.SELECT)) {
            req.setTestDataFile(view.getTestDataSelectField().getValue().getPath());
        }
        req.setInstructions(view.getInstructionsField().getValue());
        if (otherDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)
            && !view.getOtherDataUploader().getValue().isEmpty()) {
            req.setAdditionalDataFile(makeDestinationPath(getOtherDataName()));
        } else if (otherDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.SELECT) && !(
                view.getOtherDataSelectField().getValue() == null)) {
            req.setAdditionalDataFile(view.getOtherDataSelectField().getValue().getPath());
        }
        req.setAdditionaInfo(view.getAdditionalInfoField().getValue());
        return req;
    }

    private void executeCallback() {
        if (callback != null) {
            callback.execute();
        }
    }

    private String getOtherDataName() {
        return view.getOtherDataUploader().getValue();
    }

    private String getTestDataName() {
        return view.getTestDataUploader().getValue();
    }

    private String getToolBinaryName() {
        return view.getToolBinaryUploader().getValue();
    }

    private String makeDestinationPath(final String file) {
        return diskResourceUtil.appendNameToPath(UserInfo.getInstance().getHomePath(), file);
    }

    private List<UploadForm> getUploadFormsToSubmit() {
        final List<UploadForm> UploadForms = Lists.newArrayList();
        if (toolSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            UploadForms.add(view.getToolBinaryUploader());
        }
        if (testDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)) {
            UploadForms.add(view.getTestDataUploader());
        }
        if (otherDataSelectionMode.equals(NewToolRequestFormView.SELECTION_MODE.UPLOAD)
            && !view.getOtherDataUploader().getValue().isEmpty()) {
            UploadForms.add(view.getOtherDataUploader());
        }
        return UploadForms;
    }

    private void removeUploads(final Iterable<UploadForm> UploadForms) {
        final List<String> filesToDelete = Lists.newArrayList();
        for (UploadForm UploadForm : UploadForms) {
            filesToDelete.add(makeDestinationPath(UploadForm.getValue()));
        }
        if (!filesToDelete.isEmpty()) {
            final HasPaths dto = FS_FACTORY.pathsList().as();
            dto.setPaths(filesToDelete);
            fsServices.deleteDiskResources(dto, new DataCallback<HasPaths>() {
                @Override
                public void onFailure(Integer statusCode, final Throwable unused) {
                }

                @Override
                public void onSuccess(final HasPaths unused) {
                }
            });
        }
    }

}
