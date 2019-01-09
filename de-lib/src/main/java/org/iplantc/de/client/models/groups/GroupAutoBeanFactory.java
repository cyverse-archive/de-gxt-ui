package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.HasMessage;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectList;
import org.iplantc.de.client.models.collaborators.SubjectMemberList;

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

    AutoBean<Privilege> getPrivilege();

    AutoBean<PrivilegeList> getPrivilegeList();

    AutoBean<UpdatePrivilegeRequest> getUpdatePrivilegeRequest();

    AutoBean<UpdatePrivilegeRequestList> getUpdatePrivilegeRequestList();

    AutoBean<CreateTeamRequest> getCreateTeamRequest();

    AutoBean<HasMessage> getHasMessage();

    AutoBean<SubjectMemberList> getSubjectMemberList();

    default Group getDefaultGroup() {
        Group group = getGroup().as();
        group.setName(Group.DEFAULT_GROUP);
        return group;
    }

    default Group convertSubjectToGroup(Subject subject) {
        Group group = getGroup().as();
        if (subject != null) {
            group.setId(subject.getId());
            group.setName(subject.getName());
            group.setDisplayName(subject.getDisplayName());
            group.setDescription(subject.getInstitution());
        }
        return group;
    }

    default Subject convertGroupToSubject(Group group) {
        Subject subject = getSubject().as();
        if (group != null) {
            subject.setId(group.getId());
            subject.setName(group.getName());
            subject.setDisplayName(group.getDisplayName());
            subject.setSourceId(Group.GROUP_IDENTIFIER);
            subject.setInstitution(group.getDescription());
        }
        return subject;
    }
}
