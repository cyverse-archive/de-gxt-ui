package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectList;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.SubjectSortComparator;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Subject> subjects = decode.as().getSubjects();
        subjects.sort(new SubjectSortComparator());
        return getFilteredResults(subjects);
    }

    /**
     * Filter the results so that the user never sees the "default" collaborator list in their search results
     * @param result
     * @return
     */
    List<Subject> getFilteredResults(List<Subject> result) {
        return result.stream()
                     .filter(subject -> !Group.DEFAULT_GROUP.equals(subject.getName()))
                     .collect(Collectors.toList());
    }
}
