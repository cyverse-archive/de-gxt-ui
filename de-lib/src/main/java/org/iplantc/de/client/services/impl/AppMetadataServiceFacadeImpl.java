package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.avu.AvuAutoBeanFactory;
import org.iplantc.de.client.models.avu.AvuList;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.converters.SplittableCallbackConverter;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.client.services.AppMetadataServiceFacade;
import org.iplantc.de.shared.services.BaseServiceCallWrapper.Type;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

public class AppMetadataServiceFacadeImpl implements AppMetadataServiceFacade {

    private final String APPS = "org.iplantc.services.apps";

    @Inject DEProperties deProps;
    @Inject DiscEnvApiService deServiceFacade;
    private AvuAutoBeanFactory avuAutoBeanFactory;
    private DEProperties deProperties;

    @Inject
    public AppMetadataServiceFacadeImpl(AvuAutoBeanFactory avuAutoBeanFactory,
                                        DEProperties deProperties) {
        this.avuAutoBeanFactory = avuAutoBeanFactory;
        this.deProperties = deProperties;
    }

    /**
     * Duplicated in {@link org.iplantc.de.client.services.AppUserServiceFacade#favoriteApp(org.iplantc.de.client.models.HasId, boolean, com.google.gwt.user.client.rpc.AsyncCallback)}
     */
    @Override
    public void addToFavorites(String UUID, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * Duplicated in {@link org.iplantc.de.client.services.AppUserServiceFacade#favoriteApp(org.iplantc.de.client.models.HasId, boolean, com.google.gwt.user.client.rpc.AsyncCallback)}
     */
    @Override
    public void removeFromFavorites(String UUID, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getComments(String UUID, AsyncCallback<String> callback) {
        String address = getAppsMetadataAddress(UUID) + "/comments";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.GET, address);
        callService(wrapper, callback);

    }

    /**
     * Duplicated in {@link org.iplantc.de.client.services.AppUserServiceFacade#addAppComment(String, int, String, String, String, com.google.gwt.user.client.rpc.AsyncCallback)}
     */
    @Override
    public void addComment(String UUID, String comment, AsyncCallback<String> callback) {
        String address = getAppsMetadataAddress(UUID) + "/comments";
        JSONObject obj = new JSONObject();
        obj.put("comment", new JSONString(comment));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, obj.toString());
        callService(wrapper, callback);
    }

    @Override
    public void markAsRetracted(String UUID,
                                String commentId,
                                boolean retracted,
                                AsyncCallback<String> callback) {
        String address = getAppsMetadataAddress(UUID) + "/comments/" + commentId + "?retracted="
                + retracted;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.PATCH, address, "{}");
        callService(wrapper, callback);

    }

    @Override
    public void attachTags(List<String> tags, String appId, AsyncCallback<Void> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void detachTags(List<String> tags, String appId, AsyncCallback<Void> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getTags(String id, AsyncCallback<List<Tag>> callback) {
        // TODO Auto-generated method stub

    }

    String getAppsMetadataAddress(String uuid) {
        String address = deProps.getUnproctedMuleServiceBaseUrl() + "apps/" + uuid;
        return address;
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

    @Override
    public void updateAppCommunityTags(String communityDisplayName, String appId, AsyncCallback<Splittable> callback) {
        AvuList avuList = avuAutoBeanFactory.getAvuList().as();
        avuList.setAvus(getCommunityAvuList(Lists.newArrayList(communityDisplayName)));

        String address = APPS + "/" + appId + "/communities";
        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(avuList));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        callService(wrapper, new SplittableCallbackConverter(callback));
    }

    @Override
    public void deleteAppCommunityTags(String communityDisplayName, String appId, AsyncCallback<Splittable> callback) {
        AvuList avuList = avuAutoBeanFactory.getAvuList().as();
        avuList.setAvus(getCommunityAvuList(Lists.newArrayList(communityDisplayName)));

        String address = APPS + "/" + appId + "/communities";
        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(avuList));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address, encode.getPayload());
        callService(wrapper, new SplittableCallbackConverter(callback));
    }

    List<Avu> getCommunityAvuList(List<String> communities) {
        List<Avu> avuList = Lists.newArrayList();
        for (String communityDisplayName : communities) {
            Avu avu = getCommunityAvu(communityDisplayName);
            avuList.add(avu);
        }
        return avuList;
    }

    Avu getCommunityAvu(String communityDisplayName) {
        Avu avu = avuAutoBeanFactory.getAvu().as();
        avu.setAttribute(deProperties.getCommunityAttr());
        avu.setValue(communityDisplayName);
        avu.setUnit("");

        return avu;
    }
}
