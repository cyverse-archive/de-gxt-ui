package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user has selected "Create NCBI SRA Submission Folder" in the Data window
 */
public class CreateNcbiSraFolderStructureSelected
        extends GwtEvent<CreateNcbiSraFolderStructureSelected.CreateNcbiSraFolderStructureSelectedHandler> {
    public static interface CreateNcbiSraFolderStructureSelectedHandler extends EventHandler {
        void onCreateNcbiSraFolderStructureSelected(CreateNcbiSraFolderStructureSelected event);
    }

    public interface HasCreateNcbiSraFolderStructureSelectedHandlers {
        HandlerRegistration addCreateNcbiSraFolderStructureSelectedHandler(
                CreateNcbiSraFolderStructureSelectedHandler handler);
    }
    public static Type<CreateNcbiSraFolderStructureSelectedHandler> TYPE =
            new Type<CreateNcbiSraFolderStructureSelectedHandler>();
    private Folder selectedFolder;
    private String projectText;
    private Integer bioSampleNumber;
    private Integer libraryNumber;

    public CreateNcbiSraFolderStructureSelected(Folder selectedFolder,
                                                String projectText,
                                                Integer bioSampleNumber,
                                                Integer libraryNumber) {
        this.selectedFolder = selectedFolder;
        this.projectText = projectText;
        this.bioSampleNumber = bioSampleNumber;
        this.libraryNumber = libraryNumber;
    }

    public Type<CreateNcbiSraFolderStructureSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNcbiSraFolderStructureSelectedHandler handler) {
        handler.onCreateNcbiSraFolderStructureSelected(this);
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
