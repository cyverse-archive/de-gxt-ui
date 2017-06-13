package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectMemberList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author aramsey
 */
public class SubjectMemberListCallbackConverter extends AsyncCallbackConverter<String, List<Subject>> {


    private CollaboratorAutoBeanFactory factory;

    public SubjectMemberListCallbackConverter(AsyncCallback<List<Subject>> callback,
                                              CollaboratorAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Subject> convertFrom(String object) {
        AutoBean<SubjectMemberList> decode = AutoBeanCodex.decode(factory, SubjectMemberList.class, object);
        return decode.as().getSubjects();
    }
}
