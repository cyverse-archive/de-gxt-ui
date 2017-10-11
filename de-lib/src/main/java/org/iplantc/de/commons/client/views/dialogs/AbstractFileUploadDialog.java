package org.iplantc.de.commons.client.views.dialogs;

import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aramsey
 */
public abstract class AbstractFileUploadDialog extends IPlantDialog {


    public interface AbstractFileUploadDialogAppearance {

        String confirmAction();

        String fileUploadMaxSizeWarning();

        ImageResource arrowUndoIcon();

        SafeHtml fileUploadsFailed(List<String> strings);

        String closeConfirmMessage();

        SafeHtml renderDestinationPathLabel(String destPath, String parentPath);

        String reset();

        String upload();

        String fileUploadsSuccess(List<String> strings);

        SafeHtml fileSizeViolation(String filename);

        SafeHtml maxFileSizeExceed();

        SafeHtml fileExistTitle();

        SafeHtml fileExists(String dupeFiles);

        String invalidFileName();

        String fileNameValidationMsg();
    }

    public static final String HDN_PARENT_ID_KEY = "dest";
    public static final String HDN_USER_ID_KEY = "user";
    public static final String FILE_TYPE = "type";
    public static final String URL_FIELD = "url";
    static final String FORM_WIDTH = "475";
    static final String FORM_HEIGHT = "28";
    final DiskResourceAutoBeanFactory FS_FACTORY = GWT.create(DiskResourceAutoBeanFactory.class);
    List<FormPanel> formList;
    List<FileUpload> fufList;
    List<Status> statList;
    private ArrayList<TextButton> tblist;
    final List<FormPanel> submittedForms = Lists.newArrayList();
    final SafeUri fileUploadServlet;


    @UiField(provided = true) final AbstractFileUploadDialogAppearance appearance;
    @UiField
    HTML htmlDestText;
    @UiField FormPanel form0, form1, form2, form3, form4;
    @UiField
    FileUpload fuf0, fuf1, fuf2, fuf3, fuf4;
    @UiField Status status0, status1, status2, status3, status4;
    @UiField
    TextButton btn0, btn1, btn2, btn3, btn4;
    @UiField
    VerticalPanel con;

    public AbstractFileUploadDialog(final SafeUri fileUploadServlet) {
        this.fileUploadServlet = fileUploadServlet;
        appearance = GWT.create(AbstractFileUploadDialogAppearance.class);

        setAutoHide(false);
        setHideOnButtonClick(false);
        // Reset the "OK" button text
        getOkButton().setText(appearance.upload());
        getOkButton().setEnabled(false);
        setHeading(appearance.upload());
        addCancelButtonSelectHandler(event -> hide());
    }

    protected void afterBinding() {
        formList = Lists.newArrayList(form0, form1, form2, form3, form4);
        fufList = Lists.newArrayList(fuf0, fuf1, fuf2, fuf3, fuf4);
        statList = Lists.newArrayList(status0, status1, status2, status3, status4);
        tblist = Lists.newArrayList(btn0, btn1, btn2, btn3, btn4);
        setModal(false);
    }

    protected abstract void onSubmitComplete(List<FileUpload> fufList,
                                             List<Status> statList,
                                             List<FormPanel> submittedForms,
                                             List<FormPanel> formList,
                                             SubmitCompleteEvent event);

    protected abstract void doUpload(List<FileUpload> fufList,
                                     List<Status> statList,
                                     List<FormPanel> submittedForms,
                                     List<FormPanel> formList);

    @UiHandler({ "form0", "form1", "form2", "form3", "form4" })
    void onSubmitComplete(SubmitCompleteEvent event) {
        onSubmitComplete(fufList, statList, submittedForms, formList, event);
    }

    @UiHandler({ "btn0", "btn1", "btn2", "btn3", "btn4" })
    void onSelect(SelectEvent event) {
        FormPanel fp = formList.get(tblist.indexOf(event.getSource()));
        fp.reset();
    }

    @Override
    protected void onOkButtonClicked() {
        doUpload(fufList, statList, submittedForms, formList);
    }


    @UiFactory
    FormPanel createFormPanel() {
        FormPanel form = new FormPanel();
        form.setAction(fileUploadServlet);
        form.setMethod(FormPanel.METHOD_POST);
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setSize(FORM_WIDTH, FORM_HEIGHT);
        return form;
    }

    @UiFactory
    HorizontalLayoutContainer createHLC() {
        return new HorizontalLayoutContainer();
    }

    @UiFactory
    Status createFormStatus() {
        Status status = new Status();
        status.setWidth(15);
        return status;
    }


    @UiHandler({ "fuf0", "fuf1", "fuf2", "fuf3", "fuf4" })
    void onFieldChanged(ChangeEvent event) {
        getOkButton().setEnabled(FormPanelHelper.isValid(this, true) && isValidForm());
    }

    boolean isValidForm() {
        for (FileUpload f : fufList) {
            if (!Strings.isNullOrEmpty(f.getFilename())) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void hide() {
        if (submittedForms.size() > 0) {
            final ConfirmMessageBox cmb =
                    new ConfirmMessageBox(appearance.confirmAction(), appearance.closeConfirmMessage());

            cmb.addDialogHideHandler(event -> {
                if (PredefinedButton.YES.equals(event.getHideButton())) {
                    AbstractFileUploadDialog.super.hide();
                }
            });

            cmb.show();
        } else {
            super.hide();
        }
    }


    public String getRealFileName(String filename) {
        if ( Strings.isNullOrEmpty(filename) || !GXT.isChrome()) {
            return filename;
        }
        return filename.substring(12); //chrome always returns C:\fakepath\filename
    }

    public static native int getSize(com.google.gwt.user.client.Element element) /*-{
        input = element;
        if (!input) {
            return 0;
        }
        else if (!input.files) {
            return 0;
        }
        else if (!input.files[0]) {
            return 0;
        }
        else {
            file = input.files[0];
            return file.size;
        }

    }-*/;
}

