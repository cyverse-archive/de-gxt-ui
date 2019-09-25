import React, { Component } from "react";

import Teams from "../../src/teams/Teams";
import { boolean } from "@storybook/addon-knobs";

class TeamsTest extends Component {
    render() {
        const logger =
            this.props.logger ||
            ((data) => {
                console.log(data);
            });

        const presenter = {
            getMyTeams: () => logger("Get My Teams"),
            getAllTeams: () => logger("Get All Teams"),
            onCreateTeamSelected: () => logger("Create Team selected"),
            searchTeams: (searchTerm) => logger("Search teams", searchTerm),
            onTeamNameSelected: (team) => logger("Team name selected", team),
            onTeamSelectionChanged: (teamList) => {
                selectedTeams = teamList;
                logger("Team selection changed", teamList);
            },
        };

        const collaboratorsUtil = {
            getGroupShortName: (subject) =>
                subject.name.includes(":")
                    ? subject.name.slice(subject.name.indexOf(":") + 1)
                    : subject.name,
            isTeam: (subject) => subject.display_name.includes("teams"),
            isCollaboratorList: (subject) =>
                subject.display_name.includes("collaborator-lists"),
        };

        const parentId = "gwt-debug-teamsWindow";

        const teamListing = [
            {
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
            },
            {
                name: "alfredo_pereira:Test",
                type: "group",
                description: "test description",
                display_extension: "Test",
                display_name: "iplant:de:prod:teams:alfredo_pereira:Test",
                extension: "Test",
                id_index: "11432",
                id: "2cee3d1bc48b4177b596d86e1fb5c3eb",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Alfredo Pizzeria",
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
            },
            {
                name: "amcooksey:Legume Federation",
                type: "group",
                description:
                    "This team includes all members of the Legume Federation.",
                display_extension: "Legume Federation",
                display_name:
                    "iplant:de:prod:teams:amcooksey:Legume Federation",
                extension: "Legume Federation",
                id_index: "10930",
                id: "546d28ce4c7a45938c4a79daeb10e1b5",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Magical Beans",
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
            },
            {
                name: "brantfaircloth:INBRE 2019",
                type: "group",
                description: "INBRE 2019 Participants",
                display_extension: "INBRE 2019",
                display_name: "iplant:de:prod:teams:brantfaircloth:INBRE 2019",
                extension: "INBRE 2019",
                id_index: "11355",
                id: "8b4fe8e665764bad8585f537fcbfbbb1",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Brant Faircloth",
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
            },
            {
                name: "carlosdenner:unb",
                type: "group",
                description: "hello world",
                display_extension: "unb",
                display_name: "iplant:de:prod:teams:carlosdenner:unb",
                extension: "unb",
                id_index: "11337",
                id: "a1287d6c8acd4ebeb17bd820cc91b379",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Carlos Denner",
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
            },
            {
                name: "ddurden:CDA",
                type: "group",
                description: "CHEESEHEAD Data Analytics team",
                display_extension: "CDA",
                display_name: "iplant:de:prod:teams:ddurden:CDA",
                extension: "CDA",
                id_index: "11316",
                id: "3619dfc50f7242d4bf751ed8bf5869ca",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Mr Cheese",
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
            },
            {
                name: "edwins:Edwins Test Team",
                type: "group",
                display_extension: "Edwins Test Team",
                display_name: "iplant:de:prod:teams:edwins:Edwins Test Team",
                extension: "Edwins Test Team",
                id_index: "11407",
                id: "8ec0a38756ab4841ad9929b0d1b03c27",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Edwin Skidmore",
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
            },
            {
                name: "fengxiao:BIENdev",
                type: "group",
                display_extension: "BIENdev",
                display_name: "iplant:de:prod:teams:fengxiao:BIENdev",
                extension: "BIENdev",
                id_index: "10973",
                id: "ca35c7f876a24724b8b9606c4c145a72",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Feng Xiao",
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
            },
            {
                name: "flaxdna:BIOL301-2017",
                type: "group",
                description: "students-2017",
                display_extension: "BIOL301-2017",
                display_name: "iplant:de:prod:teams:flaxdna:BIOL301-2017",
                extension: "BIOL301-2017",
                id_index: "10929",
                id: "2246773435d6435fa3559353557e0b12",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "Flax DNA",
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
            },
            {
                name: "jjzhao123:Scoliosis",
                type: "group",
                display_extension: "Scoliosis",
                display_name: "iplant:de:prod:teams:jjzhao123:Scoliosis",
                extension: "Scoliosis",
                id_index: "11358",
                id: "c84636713d3f4b56b558e52898aaa07b",
                detail: {
                    has_composite: false,
                    is_composite_factor: false,
                    modified_at: 1561464082403,
                    type_names: [],
                    created_by_detail: {
                        email: "asherz@email.arizona.edu",
                        name: "MS Sucks",
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
            },
        ];

        let selectedTeams = [];

        return (
            <Teams
                parentId={parentId}
                presenter={presenter}
                collaboratorsUtil={collaboratorsUtil}
                loading={false}
                selectedTeams={selectedTeams}
                teamListing={teamListing}
                isSelectable={boolean("isSelectable", false)}
            />
        );
    }
}

export default TeamsTest;
