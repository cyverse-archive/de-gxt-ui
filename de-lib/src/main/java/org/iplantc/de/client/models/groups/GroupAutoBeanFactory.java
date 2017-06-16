package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author dennis
 */
public interface GroupAutoBeanFactory extends AutoBeanFactory {

    AutoBean<Subject> getSubject();

    AutoBean<SubjectList> getSubjectList();

    AutoBean<Member> getMember();

    AutoBean<MemberList> getMemberList();

    AutoBean<MemberSaveRequest> getMemberSaveRequest();

    AutoBean<MemberSaveResult> getMemberSaveResult();

    AutoBean<Group> getGroup();

    AutoBean<GroupList> getGroupList();

    AutoBean<UpdateMemberRequest> getUpdateMemberRequest();

    AutoBean<UpdateMemberResult> getUpdateMemberResult();

    AutoBean<UpdateMemberResultList> getUpdateMemberResultList();

    default Group getDefaultGroup() {
        Group group = getGroup().as();
        group.setName(Group.DEFAULT_GROUP);
        return group;
    }
}
