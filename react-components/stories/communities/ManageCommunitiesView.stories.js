import ManageCommunitiesView from "../../src/communities/view/ManageCommunitiesView";

import React, { Component } from "react";

class CommunitiesViewTest extends Component {
    render() {
        const communityAppsClickedLogger = this.props.communityAppsClickedLogger || ((selection) => {
            console.log(selection);
        });

        const confirmedDialogAction = this.props.confirmedDialogAction || ((selection) => {
            console.log(selection);
        });

        const parentId = "communities";

        const myCommunities = {
            groups: [
                {
                    name: "aramsey:Ultimate Community",
                    description: "This community of all things ultimate.",
                    id: "546d28ce4c7a45938c4a79daeb10e1b5",
                    source_id: "g:gsa",
                    creator: "Ashley Ramsey",
                    display_name: "iplant:de:prod:community:Ultimate Community",
                    member: true,
                    privileges: [
                        "optin",
                        "admin",
                        "read",
                        "optout"
                    ],
                }
            ]
        };

        const allCommunities = {
            groups: [
                {
                    name: "aramsey:Ultimate Community",
                    description: "This community of all things ultimate.",
                    id: "546d28ce4c7a45938c4a79daeb10e1b4",
                    source_id: "g:gsa",
                    creator: "Ashley Ramsey",
                    display_name: "iplant:de:prod:community:Ultimate Community",
                    member: true,
                    privileges: [
                        "optin",
                        "admin",
                        "read",
                        "optout"
                    ],
                },

                {
                    name: "amcooksey:Legume Federation",
                    description: "This community includes all members of the Legume Federation.",
                    id: "546d28ce4c7a45938c4a79daeb10e1b5",
                    source_id: "g:gsa",
                    creator: "Amanda Cooksey",
                    display_name: "iplant:de:prod:community:Legume Federation",
                    member: false,
                    privileges: [
                        "optin",
                        "read",
                        "optout"
                    ],
                },
            ]
        };

        const adminListing = {
            members:
                [
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
                        name: "amcooksey:Legume Federation",
                        description: "This team includes all members of the Legume Federation.",
                        id: "546d28ce4c7a45938c4a79daeb10e1b5",
                        source_id: "g:gsa",
                        display_name: "iplant:de:prod:teams:amcooksey:Legume Federation"
                    },
                ]
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

        const appListing = {
            total: 3,
            apps: [
                {
                    integration_date: "2017-08-07T19:22:42Z",
                    description: "BUSCO: Assessing genome assembly and annotation completeness with single-copy orthologs",
                    deleted: false,
                    pipeline_eligibility: {
                        is_valid: true,
                        reason: ""
                    },
                    is_favorite: false,
                    integrator_name: "Upendra Kumar Devisetty",
                    beta: false,
                    permission: "read",
                    can_favor: true,
                    disabled: true,
                    can_rate: true,
                    name: "BUSCO-v3.0",
                    system_id: "de",
                    is_public: true,
                    id: "7f948668-7a53-11e7-a680-008cfa5ae621",
                    edited_date: "2018-05-09T23:52:31Z",
                    step_count: 1,
                    can_run: true,
                    job_stats: {
                        job_count_completed: 0,
                        job_count: 0,
                        job_count_failed: 0
                    },
                    integrator_email: "upendra@cyverse.org",
                    app_type: "DE",
                    rating: {
                        average: 4.4,
                        total: 5
                    }
                },
                {
                    integration_date: "2017-08-07T19:22:42Z",
                    description: "Best app evar",
                    deleted: false,
                    pipeline_eligibility: {
                        is_valid: true,
                        reason: ""
                    },
                    is_favorite: false,
                    integrator_name: "Ashley Ramsey",
                    beta: true,
                    permission: "read",
                    can_favor: true,
                    disabled: false,
                    can_rate: true,
                    name: "BestAppv1.0",
                    system_id: "de",
                    is_public: true,
                    id: "7f948668-7a53-11e7-a680-008cfa5ae622",
                    edited_date: "2018-04-09T23:52:31Z",
                    step_count: 1,
                    can_run: true,
                    job_stats: {
                        job_count_completed: 0,
                        job_count: 0,
                        job_count_failed: 0
                    },
                    integrator_email: "aramsey@cyverse.org",
                    app_type: "DE",
                    rating: {
                        average: 4.4,
                        total: 5
                    }
                },
                {
                    integration_date: "2017-10-07T19:22:42Z",
                    description: "Word Count 'em Up",
                    deleted: false,
                    pipeline_eligibility: {
                        is_valid: true,
                        reason: ""
                    },
                    is_favorite: false,
                    integrator_name: "I am the Batman",
                    beta: false,
                    permission: "read",
                    can_favor: true,
                    disabled: false,
                    can_rate: true,
                    name: "Wordiest Count",
                    system_id: "de",
                    is_public: false,
                    id: "7f948668-7a53-11e7-a680-008cfa5ae623",
                    edited_date: "2018-06-09T23:52:31Z",
                    step_count: 1,
                    can_run: true,
                    job_stats: {
                        job_count_completed: 0,
                        job_count: 0,
                        job_count_failed: 0
                    },
                    integrator_email: "batman@cyverse.org",
                    app_type: "DE",
                    rating: {
                        average: 3,
                        total: 5
                    }
                },
                {
                    integration_date: "2017-10-07T19:22:42Z",
                    description: "Deploy Spider Bots",
                    deleted: false,
                    pipeline_eligibility: {
                        is_valid: true,
                        reason: ""
                    },
                    is_favorite: false,
                    integrator_name: "Spiderman",
                    beta: false,
                    permission: "read",
                    can_favor: true,
                    disabled: false,
                    can_rate: true,
                    name: "Spider Bots",
                    system_id: "de",
                    is_public: true,
                    id: "7f948668-7a53-11e7-a680-008cfa5ae628",
                    edited_date: "2018-06-09T23:52:31Z",
                    step_count: 1,
                    can_run: true,
                    job_stats: {
                        job_count_completed: 0,
                        job_count: 0,
                        job_count_failed: 0
                    },
                    integrator_email: "batman@cyverse.org",
                    app_type: "DE",
                    rating: {
                        average: 3,
                        total: 5
                    }
                },
            ]
        };

        const collaboratorsUtil = {
            getSubjectDisplayName: (subject) => subject.name.includes(':') ? subject.name.slice(subject.name.indexOf(':') + 1) : subject.name,
            isTeam: (subject) => subject.display_name.includes('teams'),
            isCollaboratorList: (subject) => subject.display_name.includes('collaborator-lists')
        };

        const newApp = {
            integration_date: "2017-10-07T19:22:42Z",
            description: "New Bacon-ings",
            deleted: false,
            pipeline_eligibility: {
                is_valid: true,
                reason: ""
            },
            is_favorite: false,
            integrator_name: "Bob Belcher",
            beta: false,
            permission: "read",
            can_favor: true,
            disabled: false,
            can_rate: true,
            name: "Burger of the Day",
            system_id: "de",
            is_public: true,
            id: "7f948668-7a53-11e7-a680-008cfa5ae828",
            edited_date: "2018-06-09T23:52:31Z",
            step_count: 1,
            can_run: true,
            job_stats: {
                job_count_completed: 0,
                job_count: 0,
                job_count_failed: 0
            },
            integrator_email: "bob@bobsburgers.org",
            app_type: "DE",
            rating: {
                average: 3,
                total: 5
            }
        };

        const currentUser = {
            name: "Ashley Ramsey",
            display_name: "Ashley Ramsey",
            id: "aramsey@cyverse.org",
        };

        const presenter = {
            fetchMyCommunities: (resolve, reject) => {
                setTimeout(() => {
                    resolve(myCommunities)
                }, 1000);
            },
            fetchAllCommunities: (resolve, reject) => {
                setTimeout(() => {
                    resolve(allCommunities)
                }, 1000);
            },
            fetchCommunityAdmins: (communityName, resolve, reject) => {
                setTimeout(() => {
                    resolve(adminListing)
                }, 1000);
            },
            fetchCommunityApps: (communityDisplayName, resolve, reject) => resolve(appListing),
            searchCollaborators: (input, resolve, reject) => {
                resolve(collaborators);
            },
            getCommunityAdmins: (communityName, resolve, reject) => {
                let isAdmin = communityName === "aramsey:Ultimate Community";
                resolve(isAdmin);
            },
            getCommunityMembers: (communityName, resolve, reject) => {
                let isMember = communityName === "aramsey:Ultimate Community";
                resolve(isMember)
            },
            removeCommunityApps: (communityDisplayName, appId, resolve, reject) => {
                setTimeout(() => {
                    resolve(appListing.apps.filter((value) => value.id !== appId))
                }, 1000);
            },
            removeCommunityAdmins: (communityName, adminIds, resolve, reject) => {
                setTimeout(() => {
                    resolve(adminListing.members.filter((value) => value.id !== adminIds.list[0].id))
                }, 1000);
            },
            addCommunityAdmins: (communityName, adminIds, resolve, reject) => {
                setTimeout(() => {
                    resolve();
                }, 1000);
            },
            onAddCommunityAppsClicked: (resolve, reject) => {
                communityAppsClickedLogger();
                resolve(newApp)
            },
            addAppToCommunity: (appId, communityDisplayName, resolve, reject) => {
                setTimeout(() => {
                    resolve()
                }, 1000);
            },
            deleteCommunity: (communityName, resolve, reject) => {
                confirmedDialogAction("Delete", communityName);
                setTimeout(() => {
                    resolve();
                }, 1000);
            },
            joinCommunity: (communityName, resolve, reject) => {
                confirmedDialogAction("Join", communityName);
                setTimeout(() => {
                    resolve();
                }, 1000);
            },
            leaveCommunity: (communityName, resolve, reject) => {
                confirmedDialogAction("Leave", communityName);
                setTimeout(() => {
                    resolve();
                }, 1000);
            },
            saveCommunity: (communityName, name, description, retag, resolve, reject) => {
                confirmedDialogAction("Save", communityName, name, description);
                setTimeout(() => {
                    let savedCommunity = communityName ? name : {name: name, description: description};
                    resolve(savedCommunity);
                }, 1000);
            }
        };

        return (
            <ManageCommunitiesView parentId={parentId}
                                   presenter={presenter}
                                   collaboratorsUtil={collaboratorsUtil}
                                   currentUser={currentUser}/>
        )
    }
}

export default CommunitiesViewTest;