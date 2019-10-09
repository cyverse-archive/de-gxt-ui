import React, { Component } from "react";
import AppPublicationRequests from "../../../src/apps/admin/AppPublicationRequests";

class AppPublicationRequestsTest extends Component {
    render() {
        const requests = {
            publication_requests: [
                {
                    id: "58b83ad8-ca93-443c-b2cb-6a2acff5256e",
                    app: {
                        description: "Test tooltip for checkbox",
                        deleted: false,
                        integrator_name: "sriram",
                        disabled: false,
                        suggested_categories: [],
                        hierarchies: [
                            {
                                iri: "http://edamontology.org/operation_0004",
                                label: "Operation",
                                subclasses: [
                                    {
                                        iri:
                                            "http://edamontology.org/operation_3429",
                                        label: "Generation",
                                        subclasses: [
                                            {
                                                iri:
                                                    "http://edamontology.org/operation_2928",
                                                label: "Alignment",
                                            },
                                        ],
                                    },
                                    {
                                        iri:
                                            "http://edamontology.org/operation_2928",
                                        label: "Alignment",
                                    },
                                ],
                            },
                        ],
                        name: "ChkBoxAppEdit",
                        tools: [
                            {
                                name: "MyPython",
                                type: "executable",
                                description: "Demo",
                                id: "3f847fa6-6347-11e7-9dfa-008cfa5ae621",
                                container: {
                                    image: {
                                        name: "python",
                                        tag: "latest",
                                        url: "https://hub.docker.com/_/python/",
                                        deprecated: false,
                                    },
                                },
                                version: "3.5",
                            },
                        ],
                        system_id: "de",
                        references: [],
                        categories: [],
                        id: "09de4bba-f432-11e4-b9b8-8fce65ba51d5",
                        edited_date: "2019-10-09T13:03:49Z",
                        job_stats: {
                            job_count_completed: 0,
                        },
                        integrator_email: "sriram@iplantcollaborative.org",
                    },
                    requestor: "sriram",
                },
            ],
        };
        return (
            <AppPublicationRequests
                loading={false}
                requests={requests.publication_requests}
                parentId="appPubRequest"
            />
        );
    }
}

export default AppPublicationRequestsTest;
