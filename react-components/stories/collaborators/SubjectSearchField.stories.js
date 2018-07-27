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
            }
        ];

        return (
            <SubjectSearchField presenter={presenter}
                                onSelect={handleSearch}/>
        )
    }
}

export default SubjectSearchFieldTest;