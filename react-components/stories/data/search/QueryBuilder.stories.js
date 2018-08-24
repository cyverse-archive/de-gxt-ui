import { QueryBuilder } from '../../../src/data/search';

import React from 'react';

class QueryBuilderTest extends React.Component {

    render() {

        const editTagLogger = this.props.editTagLogger || ((selection) => {
            console.log(selection);
        });

        const searchLogger = this.props.searchLogger || ((selection) => {
            console.log(selection);
        });

        const saveSearchLogger = this.props.saveSearchLogger || ((selection) => {
            console.log(selection);
        });

        const addTagLogger = this.props.addTagLogger || ((selection) => {
            console.log(selection);
        });

        const presenter = {
            onAddTagSelected: addTagLogger,
            onEditTagSelected: editTagLogger,
            fetchTagSuggestions: (term, callback) => callback(suggestedTags),
            onSaveSearch: saveSearchLogger,
            onSearchBtnClicked: searchLogger,
            searchCollaborators: (input, fn) => {
                fn(collaborators);
            }
        };

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

        const suggestedTags = [
            {
                id: '1',
                value: 'apples',
                description: 'old apples'
            },
            {
                id: '2',
                value: 'oranges',
                description: 'old oranges'
            },
            {
                id: '3',
                value: 'tangerines',
                description: 'old tangerines'
            },
            {
                id: '4',
                value: 'kiwis',
                description: 'old kiwis'
            }
        ];

        const collaboratorsUtil = {
            getSubjectDisplayName: (subject) => subject.name.includes(':') ? subject.name.slice(subject.name.indexOf(':') + 1) : subject.name,
            isTeam: (subject) => subject.display_name.includes('teams'),
            isCollaboratorList: (subject) => subject.display_name.includes('collaborator-lists')
        };

        const initialValues = {
            label: 'OLDNAME',
        };

        const parentId = 'searchForm';

        return (
            <QueryBuilder presenter={presenter}
                          parentId={parentId}
                          initialValues={initialValues}
                          collaboratorsUtil={collaboratorsUtil}/>
        )
    }
}

export default QueryBuilderTest;