package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author aramsey
 */
public class PrivilegeListCallbackConverter extends AsyncCallbackConverter<String, List<Privilege>> {

    private final GroupAutoBeanFactory factory;

    public PrivilegeListCallbackConverter(AsyncCallback<List<Privilege>> callback,
                                          GroupAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Privilege> convertFrom(String object) {
        final PrivilegeList decode = AutoBeanCodex.decode(factory, PrivilegeList.class, object).as();
        return decode.getPrivileges();
    }
}
