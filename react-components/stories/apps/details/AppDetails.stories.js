import React, {Component} from "react";
import AppDetails from "../../../src/apps/details/AppDetails";


class AppDetailsTest extends Component {

    render() {
        const appDetals = {
            "integration_date": "2015-08-04T16:30:16Z",
            "description": "Counts the number of words in a file",
            "deleted": false,
            "integrator_name": "Default DE Tools",
            "disabled": false,
            "suggested_categories": [],
            "hierarchies": [{
                "iri": "http://edamontology.org/operation_0004",
                "label": "Operation",
                "subclasses": [{
                    "iri": "http://edamontology.org/operation_2409",
                    "label": "Data handling"
                }]
            }],
            "name": "DE Word Count",
            "tools": [{
                "name": "wc",
                "type": "executable",
                "description": "Word Count",
                "id": "85cf7a33-386b-46fe-87c7-8c9d59972624",
                "location": "",
                "container": {
                    "image": {
                        "name": "discoenv/url-import",
                        "tag": "latest",
                        "url": "https://registry.hub.docker.com/u/discoenv/url-import/",
                        "deprecated": false
                    }
                },
                "version": "0.0.1"
            }],
            "system_id": "de",
            "references": [],
            "categories": [{
                "id": "5401bd14-6c14-4470-aedd-57b47ea1b979",
                "name": "Beta"
            }],
            "id": "67d15627-22c5-42bd-8daf-9af5deecceab",
            "edited_date": "2019-02-19T17:02:42Z",
            "job_stats": {
                "job_count_completed": 32,
                "job_last_completed": "2019-03-07T08:03:23Z"
            },
            "integrator_email": "support@iplantcollaborative.org"
        };

        return (
            <AppDetails details={appDetals}/>
        );
    }
}
export default AppDetailsTest;

