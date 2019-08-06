package org.iplantc.de.client.services;

import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.client.models.viewer.Manifest;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface FileEditorServiceFacade {

    String COMMA_DELIMITER = ",";
    String TAB_DELIMITER = "\t";
    String SPACE_DELIMITER = " ";

    /**
     * Call service to retrieve the manifest for a requested file
     *  @param file desired manifest's file ID (path).
     * @param callback executes when RPC call is complete.
     */
    void getManifest(File file, DECallback<Manifest> callback);

    /**
     *
     * Return indentifier for path list file 
     * @param infoType
     * @return
     */
    String getPathListFileIdentifier(String infoType);

    /**
     * Construct a servlet download URL for the given file ID.
     * 
     * @param path the desired file path to be used in the return URL
     * @return a URL for the given file ID.
     */
    String getServletDownloadUrl(String path);

    /**
     * Reads a chunk of the given file. The file must be a CSV or TSV file.
     * @param file the CSV file to be read
     * @param delimiter the file's delimiter type
     * @param pageNumber the page number where the requested chunk will begin
     * @param chunkSize the size of the chunk to be read
     * @param callback Where you will find your stuff.
     *                 FIXME improve callback to return a data type. Clients currently have to parse content themselves.
     */
    void readCsvChunk(File file, String delimiter, int pageNumber, long chunkSize, DECallback<String> callback);

    /**
     * Reads a chunk of the given file.
     * @param file the file to be read
     * @param chunkPosition the position where the requested chunk will begin
     * @param chunkSize the size of the chunk to be read
     * @param callback Where you will find your stuff.
     */
    void readChunk(File file, long chunkPosition, long chunkSize, DECallback<String> callback);


    /**
     * Load genome in Coge
     * 
     * @param pathArray
     * @param callback
     */
    void loadGenomesInCoge(JSONObject pathArray,AsyncCallback<String> callback);

    void searchGenomesInCoge(String searchTxt, AsyncCallback<List<Genome>> callback);

    void uploadTextAsFile(String destination, String fileContents, boolean newFile, DECallback<File> callback);
    
    void importGenomeFromCoge(Integer id, boolean notify, AsyncCallback<String> callback);

}
