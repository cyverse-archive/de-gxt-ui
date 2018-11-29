package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.models.groups.UpdateMemberResultList;
import org.iplantc.de.client.services.impl.GroupServiceFacadeImpl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author aramsey
 *
 * A callback converter for the response returned when updating members of a group
 */
public class UpdateMemberResultsCallbackConverter extends AsyncCallbackConverter<String, List<UpdateMemberResult>> {
    private GroupAutoBeanFactory factory;

    public UpdateMemberResultsCallbackConverter(GroupAutoBeanFactory factory,
                                                AsyncCallback<List<UpdateMemberResult>> callback) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<UpdateMemberResult> convertFrom(String object) {
        AutoBean<UpdateMemberResultList> listAutoBean = AutoBeanCodex.decode(factory, UpdateMemberResultList.class, object);
        return listAutoBean.as().getResults();
    }
}
