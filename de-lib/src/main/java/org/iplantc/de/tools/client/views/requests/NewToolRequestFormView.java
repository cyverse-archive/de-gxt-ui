package org.iplantc.de.tools.client.views.requests;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.diskResource.client.views.widgets.FileSelectorField;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.form.IsField;

/**
 */
public interface NewToolRequestFormView extends IsWidget {

    void setMode(Mode mode);

    enum Mode {
        NEWTOOL, MAKEPUBLIC;
    }

    interface NewToolRequestFormViewAppearance  {
        String newToolRequest();

        String makePublicRequest();

        String newToolInstruction();

        String makePublicInstruction();

        SafeHtml buildRequiredFieldLabel(String label);

        SafeHtml sameFileError(String filename);

        SafeHtml alert();

        SafeHtml fileSizeViolation(String filename);

        SafeHtml maxFileSizeExceed();

        SafeHtml fileExistTitle();

        SafeHtml fileExists(String dupeFiles);

        String invalidFileName();

        String fileNameValidationMsg();

        String getFileName(String filename);
    }

    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {
        /**
         * The method to be called when the user clicks the cancel button.
         */
        void onCancelBtnClick();

        /**
         * The method to be called when the user clicks the submit button.
         */
        void onSubmitBtnClick();

        /**
         * The method to be called when the user decides whether or not to provide the tool binary
         * by uploading a file.
         * 
         */
        void onToolSelectionModeChange();

        /**
         * Set whether or not user is uploading the bin
         * 
         * @param mode
         */
        void setToolMode(SELECTION_MODE mode);

        /**
         * Set whether or not user is uploading the test data
         * 
         * @param mode
         */
        void setTestDataMode(SELECTION_MODE mode);

        /**
         * Set whether or not user is uploading the bin other data
         * 
         * @param mode
         */
        void setOtherDataMode(SELECTION_MODE mode);

        void onTestDataSelectionModeChange();

        void onOtherDataSeelctionModeChange();

        void setViewDebugId(String baseID);

        void setTool(Tool tool);

        void setMode(Mode mode);
    }

    enum SELECTION_MODE {
        UPLOAD, LINK, SELECT
    }

    void setPresenter(Presenter p);

    /**
     * @return the uploader for the other data file
     */
    UploadForm getOtherDataUploader();

    /**
     * @return the uploader for the test data file
     */
    UploadForm getTestDataUploader();

    /**
     * @return the uploader for the tool's binary file
     */
    UploadForm getToolBinaryUploader();

    /**
     * Show the user a failed submission message
     */
    void indicateSubmissionFailure(SafeHtml reason);

    /**
     * Indicate to the user that the submission has started
     */
    void indicateSubmissionStart();

    /**
     * Show the user a successful submission message
     */
    void indicateSubmissionSuccess();

    /**
     * Forces the user input to be validated.
     * 
     * @return it returns true if all of the user-provided values are valid, otherwise false.
     */
    boolean isValid();

    /**
     * Tells the view whether or not to configure itself for uploading the tool binary.
     * 
     **/
    void setToolSelectionMode();

    /**
     * @return the tool name field
     */
    IsField<String> getNameField();

    /**
     * @return the tool description field
     */
    IsField<String> getDescriptionField();

    /**
     * @return the attribution field
     */
    IsField<String> getAttributionField();

    /**
     * @return the tool binary URL field
     */
    IsField<String> getSourceURLField();

    /**
     * @return the documentation URL field
     */
    IsField<String> getDocURLField();

    /**
     * @return the tool version field
     */
    IsField<String> getVersionField();

    /**
     * @return the command line usage instructions field
     */
    IsField<String> getInstructionsField();

    /**
     * 
     * @return the bin selection field
     */

    /**
     * @return the additional information field
     */
    IsField<String> getAdditionalInfoField();

    FileSelectorField getBinSelectField();

    FileSelectorField getTestDataSelectField();

    FileSelectorField getOtherDataSelectField();

    void setTestDataSelectMode();

    void setOtherDataSelectMode();

    void setTool(Tool tool);
}

