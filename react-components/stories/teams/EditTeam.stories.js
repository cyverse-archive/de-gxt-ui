import React, { Component } from "react";
import { boolean, select } from "@storybook/addon-knobs";

import EditTeamDialog from "../../src/teams/edit/EditTeamDialog";

class EditTeamTest extends Component {
    render() {
        const isEditing = boolean("Use Sample Team", true);
        const selfPrivilege = select(
            "Self Privilege",
            ["admin", "read", "view"],
            "admin"
        );
        const isMember = boolean("Is Team Member", true);

        const logger =
            this.props.logger ||
            ((data) => {
                console.log(data);
            });

        const presenter = {
            searchCollaborators: (searchTerm, resolve, reject) =>
                resolve(collaborators),
            saveTeamSelected: (
                name,
                createTeamRequest,
                privilegeUpdateList,
                memberIds
            ) =>
                logger(
                    "Save Team selected",
                    name,
                    createTeamRequest,
                    privilegeUpdateList,
                    memberIds
                ),
            updateTeam: (originalName, name, description) =>
                logger("Update Team selected", originalName, name, description),
            leaveTeamSelected: (teamName, resolve) => {
                logger("Leave Team selected", teamName);
                resolve();
            },
            deleteTeamSelected: (teamName, resolve) => {
                logger("Delete Team selected", teamName);
                resolve();
            },
            joinTeamSelected: (teamName, resolve) => {
                logger("Join Team selected", teamName);
                resolve();
            },
            sendRequestToJoin: (teamName, message, resolve, reject) => {
                logger("Send Request to join selected", teamName, message);
                resolve();
            },
            closeEditTeamDlg: () => logger("Close Edit Team dialog"),
            updatePrivilegesToTeam: (
                teamName,
                privilegeUpdateReq,
                subjectId,
                callback
            ) => {
                logger(
                    "Update privileges to team " + teamName,
                    subjectId,
                    privilegeUpdateReq
                );
                callback();
            },
            removeMemberAndPrivilege: (
                teamName,
                subjectId,
                updatePrivilegeReq,
                callback
            ) => {
                logger(
                    "Remove member " + subjectId + " from team " + teamName,
                    updatePrivilegeReq
                );
                callback();
            },
        };

        const parentId = "gwt-debug-teamsWindow";

        const team = {
            name: "a7032018:Khan Lab",
            type: "group",
            display_extension: "Khan Lab",
            display_name: "iplant:de:prod:teams:a7032018:Khan Lab",
            extension: "Khan Lab",
            id_index: "11340",
            id: "e97571146d5e48708a63495a06f839c0",
            detail: {
                has_composite: false,
                is_composite_factor: false,
                modified_at: 1561464082403,
                type_names: [],
                created_by_detail: {
                    email: "asherz@email.arizona.edu",
                    name: "Captain Kirk",
                    last_name: "Ramsey",
                    description: "Ashley Ramsey",
                    id: "aramsey",
                    institution: "University of Arizona",
                    first_name: "Ashley",
                    source_id: "ldap",
                },
                created_at: 1561464082387,
                modified_by: "aramsey",
            },
        };

        const privileges = [
            {
                type: "access",
                name: "read",
                allowed: true,
                revokable: true,
                subject: {
                    name: "iplant:de:qa:teams:aramsey:another test",
                    id: "8d5e5993bde8494ebe3c3f6e9aff27e9",
                    source_id: "g:gsa",
                },
            },
            {
                type: "access",
                name: "read",
                allowed: true,
                revokable: false,
                subject: {
                    name: "EveryEntity",
                    description: "EveryEntity",
                    id: "GrouperAll",
                    source_id: "g:isa",
                },
            },
            {
                type: "access",
                name: "admin",
                allowed: true,
                revokable: true,
                subject: {
                    email: "de-grouper@iplantcollaborative.org",
                    name: "DE Grouper",
                    last_name: "Grouper",
                    description: "DE Grouper",
                    id: "de_grouper",
                    institution: "N/A",
                    first_name: "DE",
                    source_id: "ldap",
                },
            },
            {
                type: "access",
                name: selfPrivilege,
                allowed: true,
                revokable: true,
                subject: {
                    institution: "University of Arizona",
                    description: "Ashley Ramsey",
                    email: "asherz@email.arizona.edu",
                    first_name: "Ashley",
                    name: "Ashley Ramsey",
                    id: "aramsey",
                    last_name: "Ramsey",
                    source_id: "ldap",
                },
            },
            {
                type: "access",
                name: "read",
                allowed: true,
                revokable: true,
                subject: {
                    email: "qa-test1@iplantcollaborative.org",
                    name: "Qa Test1",
                    last_name: "Test1",
                    description: "Qa Test1",
                    id: "qa-test1",
                    institution: "Not Provided",
                    first_name: "Qa",
                    source_id: "ldap",
                },
            },
            {
                type: "access",
                name: "optin",
                allowed: true,
                revokable: true,
                subject: {
                    email: "qa-test1@iplantcollaborative.org",
                    name: "Qa Test1",
                    last_name: "Test1",
                    description: "Qa Test1",
                    id: "qa-test1",
                    institution: "Not Provided",
                    first_name: "Qa",
                    source_id: "ldap",
                },
            },
            {
                type: "access",
                name: "optout",
                allowed: true,
                revokable: true,
                subject: {
                    email: "qa-test1@iplantcollaborative.org",
                    name: "Qa Test1",
                    last_name: "Test1",
                    description: "Qa Test1",
                    id: "qa-test1",
                    institution: "Not Provided",
                    first_name: "Qa",
                    source_id: "ldap",
                },
            },
            {
                type: "access",
                name: "admin",
                allowed: true,
                revokable: true,
                subject: {
                    email: "qa-test1@iplantcollaborative.org",
                    name: "Qa Test1",
                    last_name: "Test1",
                    description: "Qa Test1",
                    id: "qa-test1",
                    institution: "Not Provided",
                    first_name: "Qa",
                    source_id: "ldap",
                },
            },
            {
                type: "access",
                name: "read",
                allowed: true,
                revokable: true,
                subject: {
                    name:
                        "iplant:de:qa:users:aramsey:collaborator-lists:muhList",
                    id: "99fb479af38e442ba881c921c0cfbbaf",
                    source_id: "g:gsa",
                },
            },
        ];

        let members = [
            {
                name: "muhList",
                id: "99fb479af38e442ba881c921c0cfbbaf",
                source_id: "g:gsa",
                display_name:
                    "iplant:de:qa:users:aramsey:collaborator-lists:muhList",
            },
            {
                name: "aramsey:another test",
                id: "8d5e5993bde8494ebe3c3f6e9aff27e9",
                source_id: "g:gsa",
                display_name: "iplant:de:qa:teams:aramsey:another test",
            },
            {
                email: "qa-test1@iplantcollaborative.org",
                name: "Qa Test1",
                last_name: "Test1",
                description: "Qa Test1",
                id: "qa-test1",
                institution: "Not Provided",
                first_name: "Qa",
                source_id: "ldap",
                display_name: "Qa Test1",
            },
            {
                email: "qa-test2@iplantcollaborative.org",
                name: "Qa Test2",
                last_name: "Test2",
                description: "Qa Test2",
                id: "qa-test2",
                institution: "Not Provided",
                first_name: "Qa",
                source_id: "ldap",
                display_name: "Qa Test2",
            },
        ];

        const selfSubject = {
            institution: "University of Arizona",
            description: "Ashley Ramsey",
            email: "asherz@email.arizona.edu",
            first_name: "Ashley",
            name: "Ashley Ramsey",
            id: "aramsey",
            last_name: "Ramsey",
            source_id: "ldap",
            display_name: "Ashley Ramsey",
        };

        if (isMember) {
            members.push(selfSubject);
        }

        const collaborators = [
            {
                email: "batman_test@iplantcollaborative.org",
                name: "Batman Test",
                last_name: "Test",
                description: "Batman Test",
                id: "batman_test",
                institution: "The university of arizona",
                first_name: "Batman",
                source_id: "ldap",
                display_name: "Batman Test",
            },
            {
                email: "core-sw@iplantcollaborative.org",
                name: "Ipc Dev",
                last_name: "Dev",
                description: "Ipc Dev",
                id: "ipcdev",
                institution: "iPlant Collaborative",
                first_name: "Ipc",
                source_id: "ldap",
                display_name: "Ipc Dev",
            },
            {
                email: "ipctest@iplantcollaborative.org",
                name: "Ipc Test",
                last_name: "Test",
                description: "Ipc Test",
                id: "ipctest",
                institution: "iplant collaborative",
                first_name: "Ipc",
                source_id: "ldap",
                display_name: "Ipc Test",
            },
            {
                name: "amcooksey:Legume Federation",
                description:
                    "This team includes all members of the Legume Federation.",
                id: "546d28ce4c7a45938c4a79daeb10e1b5",
                source_id: "g:gsa",
                display_name:
                    "iplant:de:prod:teams:amcooksey:Legume Federation",
            },
            {
                name: "Superhero List",
                description: "All the superheroes from our universe",
                id: "ed25292fb5b7483783e7b912ef3e5506",
                source_id: "g:gsa",
                display_name:
                    "iplant:de:prod:users:aramsey:collaborator-lists:Superhero List",
            },
        ];

        const groupNameRestrictedChars = ":_";

        const collaboratorsUtil = {
            getSubjectDisplayName: (subject) =>
                subject.name.includes(":")
                    ? subject.name.slice(subject.name.lastIndexOf(":") + 1)
                    : subject.name,
            getGroupShortName: (subject) =>
                collaboratorsUtil.getSubjectDisplayName(subject),
            isTeam: (subject) => subject.name.includes("teams"),
            isCollaboratorList: (subject) =>
                subject.name.includes("collaborator-lists"),
        };

        const publicUsersId = "GrouperAll";

        return (
            <EditTeamDialog
                parentId={parentId}
                presenter={presenter}
                loading={false}
                open={true}
                collaboratorsUtil={collaboratorsUtil}
                team={isEditing ? team : null}
                privileges={isEditing ? privileges : []}
                members={isEditing && selfPrivilege !== "view" ? members : []}
                groupNameRestrictedChars={groupNameRestrictedChars}
                selfSubject={selfSubject}
                publicUsersId={publicUsersId}
            />
        );
    }
}

export default EditTeamTest;
