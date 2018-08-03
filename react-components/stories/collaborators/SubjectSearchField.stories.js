import SubjectSearchField from '../../src/collaborators/SubjectSearchField';

import React, { Component } from 'react';

class SubjectSearchFieldTest extends Component {
    render() {
        const handleSearch = this.props.logger || ((selection) => {
            console.log(selection);
        });

        const presenter = {
            searchCollaborators: (input, fn) => {
                fn(dataSource);
            }
        };

        const dataSource = [
            {
                email: "batman_test@iplantcollaborative.org",
                name: "Batman Test",
                last_name: "Test",
                description: "Batman Test",
                id: "batman_test",
                institution: "The university of arizona",
                first_name: "Batman",
                source_id: "ldap",
                display_name: "Batman Test"
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
                display_name: "Ipc Dev"
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
                display_name: "Ipc Test"
            },
            {
                name: "amcooksey:Legume Federation",
                description: "This team includes all members of the Legume Federation.",
                id: "546d28ce4c7a45938c4a79daeb10e1b5",
                source_id: "g:gsa",
                display_name: "iplant:de:prod:teams:amcooksey:Legume Federation"
            },
            {
                name: "Superhero List",
                description: "All the superheroes from our universe",
                id: "ed25292fb5b7483783e7b912ef3e5506",
                source_id: "g:gsa",
                display_name: "iplant:de:prod:users:aramsey:collaborator-lists:Superhero List"
            }
        ];

        const parentId = 'someParentId';

        const collaboratorsUtil = {
            getSubjectDisplayName: (subject) => subject.name.includes(':') ? subject.name.slice(subject.name.indexOf(':') + 1) : subject.name,
            isTeam: (subject) => subject.display_name.includes('teams'),
            isCollaboratorList: (subject) => subject.display_name.includes('collaborator-lists')
        };

        return (
            <SubjectSearchField presenter={presenter}
                                collaboratorsUtil={collaboratorsUtil}
                                parentId={parentId}
                                onSelect={handleSearch}/>
        )
    }
}

export default SubjectSearchFieldTest;