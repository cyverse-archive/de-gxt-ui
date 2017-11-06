package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.client.models.genomes.GenomeList;
import org.iplantc.de.client.models.viewer.FileViewerAutoBeanFactory;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.models.viewer.Manifest;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.services.BaseServiceCallWrapper.Type;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.core.client.util.Format;

import java.util.List;

/**
 * Facade for file editors.
 */
public class FileEditorServiceFacadeImpl implements FileEditorServiceFacade {
    private final DEClientConstants constants;
    private final DEProperties deProperties;
    private final DiscEnvApiService deServiceFacade;
    private final UserInfo userInfo;
    @Inject FileEditorServiceAutoBeanFactory factory;
    @Inject FileViewerAutoBeanFactory viewerFactory;

    interface FileEditorServiceAutoBeanFactory extends AutoBeanFactory {
        AutoBean<File> file();

        AutoBean<GenomeList> genomeList();
    }

    @Inject
    public FileEditorServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                       final DEProperties deProperties,
                                       final DEClientConstants constants,
                                       final UserInfo userInfo) {
        this.deServiceFacade = deServiceFacade;
        this.deProperties = deProperties;
        this.constants = constants;
        this.userInfo = userInfo;
    }

    @Override
    public void getManifest(File file, DECallback<Manifest> callback) {
        String address = deProperties.getDataMgmtBaseUrl() + "file/manifest?path=" //$NON-NLS-1$
                + URL.encodeQueryString(file.getPath());

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(wrapper, new DECallbackConverter<String, Manifest>(callback) {
            @Override
            protected Manifest convertFrom(String object) {
                final AutoBean<Manifest> manifest = AutoBeanCodex.decode(viewerFactory, Manifest.class, object);
                return manifest.as();
            }
        });
    }

    @Override
    public String getPathListFileIdentifier(String infoType) {
        if ((!Strings.isNullOrEmpty(infoType))) {
            if (infoType.equals(InfoType.HT_ANALYSIS_PATH_LIST.getTypeString())) {
                return deProperties.getHtPathListFileIdentifier();
            } else if (infoType.equals(InfoType.MULTI_INPUT_PATH_LIST.getTypeString())) {
                return deProperties.getMultiInputPathListFileIdentifier();
            } else {
                return null;
            }
        }
       return null;
    }

    @Override
    public String getServletDownloadUrl(final String path) {
        String address = Format.substitute("{0}?url=display-download&user={1}&path={2}", //$NON-NLS-1$
                constants.fileDownloadServlet(), userInfo.getUsername(), path);

        return URL.encode(address);
    }

    @Override
    public void readChunk(final File file, final long chunkPosition, final long chunkSize, final DECallback<String> callback){
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(file.getPath());
        String address = deProperties.getDataMgmtBaseUrl() + "read-chunk";

        Splittable splittable = StringQuoter.createSplittable();
        StringQuoter.create(file.getPath()).assign(splittable, "path");

        // Endpoint has to take these numbers as strings
        StringQuoter.create(String.valueOf(chunkPosition)).assign(splittable, "position");
        StringQuoter.create(String.valueOf(chunkSize)).assign(splittable, "chunk-size");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, splittable.getPayload());
        callService(wrapper, callback);
    }

    @Override
    public void readCsvChunk(File file, String delimiter, int pageNumber, long chunkSize,
                             DECallback<String> callback) {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(file.getPath());
        Preconditions.checkArgument(COMMA_DELIMITER.equals(delimiter)
                                        || TAB_DELIMITER.equals(delimiter)
                                        || SPACE_DELIMITER.equals(delimiter), "Unsupported delimiter: '" + delimiter + "'");
        String address = deProperties.getDataMgmtBaseUrl() + "read-csv-chunk";

        Splittable splittable = StringQuoter.createSplittable();
        StringQuoter.create(file.getPath()).assign(splittable, "path");
        StringQuoter.create(URL.encodePathSegment(delimiter)).assign(splittable, "separator");

        // Endpoint has to take these numbers as strings
        StringQuoter.create(String.valueOf(pageNumber)).assign(splittable, "page");
        StringQuoter.create(String.valueOf(chunkSize)).assign(splittable, "chunk-size");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, splittable.getPayload());
        callService(wrapper, callback);
    }

    @Override
    public void getTreeUrl(String pathToFile, boolean refresh, AsyncCallback<String> callback) {
        String address = "org.iplantc.services.buggalo.baseUrl?refresh=" + refresh + "&path=" + URL.encodeQueryString(pathToFile); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(wrapper, callback);
    }

    @Override
    public void uploadTextAsFile(String destination, String fileContents, boolean newFile,
            DECallback<File> callback) {

        String fullAddress = deProperties.getFileIoBaseUrl()
                + (newFile ? "saveas" : "save"); //$NON-NLS-1$
        JSONObject obj = new JSONObject();
        obj.put("dest", new JSONString(destination)); //$NON-NLS-1$
        obj.put("content", new JSONString(fileContents));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, fullAddress,
                obj.toString());
        callService(wrapper, new DECallbackConverter<String, File>(callback) {
            @Override
            protected File convertFrom(String object) {
                Splittable split = StringQuoter.split(object);
                final AutoBean<File> fileAutoBean = AutoBeanCodex.decode(factory, File.class, split.get("file"));
                return fileAutoBean.as();
            }
        });
    }

    /**
     * Performs the actual service call.
     * 
     * @param wrapper the wrapper used to get to the actual service via the service proxy.
     * @param callback executed when RPC call completes.
     */
    private void callService(ServiceCallWrapper wrapper, AsyncCallback<String> callback) {
        deServiceFacade.getServiceData(wrapper, callback);
    }

    private void callService(ServiceCallWrapper wrapper, DECallback<String> callback) {
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void loadGenomesInCoge(JSONObject pathArray, AsyncCallback<String> callback) {
        String address = deProperties.getUnproctedMuleServiceBaseUrl() + "coge/genomes/load";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address,
                pathArray.toString());
        callService(wrapper, callback);

    }

    @Override
    public void searchGenomesInCoge(String searchTxt, AsyncCallback<List<Genome>> callback) {
        String address = deProperties.getUnproctedMuleServiceBaseUrl() + "coge/genomes?search="
                + URL.encodeQueryString(searchTxt);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.GET, address);
        callService(wrapper, new AsyncCallbackConverter<String, List<Genome>>(callback) {
            @Override
            protected List<Genome> convertFrom(String object) {
                return AutoBeanCodex.decode(factory, GenomeList.class, object).as().getGenomes();
            }
        });
    }

    @Override
    public void importGenomeFromCoge(Integer id, boolean notify, AsyncCallback<String> callback) {
        String address = deProperties.getUnproctedMuleServiceBaseUrl() + "coge/genomes/" + id
                + "/export-fasta?notify=true";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST, address, "{}");
        callService(wrapper, callback);
    }

}
