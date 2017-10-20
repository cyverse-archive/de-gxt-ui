package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user has selected "Create NCBI SRA Submission Folder" in the Data window
 */
public class CreateNcbiSraFolderStructureSubmitted
        extends GwtEvent<CreateNcbiSraFolderStructureSubmitted.CreateNcbiSraFolderStructureSubmittedHandler> {
    public static interface CreateNcbiSraFolderStructureSubmittedHandler extends EventHandler {
        void onCreateNcbiSraFolderStructureSubmitted(CreateNcbiSraFolderStructureSubmitted event);
    }

    public interface HasCreateNcbiSraFolderStructureSubmittedHandlers {
        HandlerRegistration addCreateNcbiSraFolderStructureSubmittedHandler(
                CreateNcbiSraFolderStructureSubmittedHandler handler);
    }
    public static Type<CreateNcbiSraFolderStructureSubmittedHandler> TYPE =
            new Type<CreateNcbiSraFolderStructureSubmittedHandler>();
    private Folder selectedFolder;
    private String projectText;
    private Integer bioSampleNumber;
    private Integer libraryNumber;

    public CreateNcbiSraFolderStructureSubmitted(Folder selectedFolder,
                                                 String projectText,
                                                 Integer bioSampleNumber,
                                                 Integer libraryNumber) {
        this.selectedFolder = selectedFolder;
        this.projectText = projectText;
        this.bioSampleNumber = bioSampleNumber;
        this.libraryNumber = libraryNumber;
    }

    public Type<CreateNcbiSraFolderStructureSubmittedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNcbiSraFolderStructureSubmittedHandler handler) {
        handler.onCreateNcbiSraFolderStructureSubmitted(this);
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public String getProjectText() {
        return projectText;
    }

    public Integer getBioSampleNumber() {
        return bioSampleNumber;
    }

    public Integer getLibraryNumber() {
        return libraryNumber;
    }
}
