package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.GroupCallbackConverter;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;
import javax.inject.Inject;

/**
 * @author aramsey
 */
public class GroupServiceFacadeImpl implements GroupServiceFacade {

    private final String GROUPS = "org.iplantc.services.groups";

    private GroupAutoBeanFactory factory;
    private DiscEnvApiService deService;

    @Inject
    public GroupServiceFacadeImpl(GroupAutoBeanFactory factory, DiscEnvApiService deService) {

        this.factory = factory;
        this.deService = deService;
    }

    @Override
    public void getGroups(String searchTerm, AsyncCallback<List<Group>> callback) {
        String address = GROUPS + "?search=" + URL.encodeQueryString(searchTerm);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<Group>>(callback) {
            @Override
            protected List<Group> convertFrom(String object) {
                AutoBean<GroupList> decode = AutoBeanCodex.decode(factory, GroupList.class, object);
                return decode.as().getGroups();
            }
        });
    }

    @Override
    public void addGroup(Group group, AsyncCallback<Group> callback) {
        String address = GROUPS;

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(group));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void deleteGroup(String name, AsyncCallback<Group> callback) {
        String address = GROUPS + "/" + URL.encodeQueryString(name);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }


}
