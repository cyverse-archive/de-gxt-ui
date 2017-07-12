package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;
import java.util.function.Consumer;

/**
 * This is a callback converter that will take a list of type group and convert it to a list of type subject
 *
 * This is mostly so the groups callback can be added to a liststore of type subject
 *
 * @author aramsey
 */
public class GroupListToSubjectListCallbackConverter extends AsyncCallbackConverter<String, List<Subject>> {

    private final GroupAutoBeanFactory factory;

    public GroupListToSubjectListCallbackConverter(AsyncCallback<List<Subject>> callback, GroupAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Subject> convertFrom(String object) {
        List<Group> groupList = AutoBeanCodex.decode(factory, GroupList.class, object).as().getGroups();
        List<Subject> subjectList = Lists.newArrayList();
        groupList.forEach(new Consumer<Group>() {
            @Override
            public void accept(Group group) {
                Subject subject = factory.convertGroupToSubject(group);
                subjectList.add(subject);
            }
        });

        return subjectList;
    }
}
