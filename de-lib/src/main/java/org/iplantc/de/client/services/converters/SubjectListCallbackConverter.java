package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author aramsey
 */
public class SubjectListCallbackConverter extends AsyncCallbackConverter<String, List<Subject>> {

    CollaboratorAutoBeanFactory factory;

    public SubjectListCallbackConverter(AsyncCallback<List<Subject>> callback, CollaboratorAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Subject> convertFrom(String object) {
        AutoBean<SubjectList> decode = AutoBeanCodex.decode(factory, SubjectList.class, object);
        return decode.as().getSubjects();
    }
}
