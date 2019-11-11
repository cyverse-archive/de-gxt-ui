import React, { Component } from "react";
import ToolInfoDialog from "../../src/tools/details/ToolInfoDialog";

class TooInfoDialogTest extends Component {
    render() {
        const selectedTool = {
            description:
                "Cyverse Jupyter Lab beta with updated iJab plugin and jupyterlab_irods",
            permission: "read",
            interactive: true,
            name: "jupyterlab",
            type: "interactive",
            restricted: false,
            is_public: true,
            id: "a66a456a-6f0e-4760-abd1-c7538fcbcd28",
            container: {
                image: {
                    name: "gims.cyverse.org:5000/jupyter-lab",
                    tag: "beta",
                    deprecated: false,
                },
            },
            version: "0.0.3",
            implementation: {
                implementor: "Upendra Kumar Devisetty",
                implementor_email: "upendra@cyverse.org",
            },
            time_limit_seconds: 0,
        };
        const toolInfoPublic = {
            description: "16sblaster",
            permission: "read",
            interactive: false,
            name: "16sblaster",
            type: "executable",
            restricted: false,
            is_public: true,
            id: "602b013c-bfa9-11e5-8bd0-3f681b5dbaee",
            container: {
                id: "6036e862-bfa9-11e5-8f5e-e7bd264f58a3",
                image: {
                    name: "docker.cyverse.org/16sblaster",
                    id: "603296e0-bfa9-11e5-a6e2-0763ff44c651",
                    tag: "v0.0.1",
                    url: "https://gims.iplantcollaborative.org:5000/16sblaster",
                    deprecated: false,
                    osg_image_path: null,
                },
                skip_tmp_mount: false,
            },
            attribution: "Ken Youensclark",
            version: "0.0.1",
            location: "https://iplantcollaborative.org",
            implementation: {
                implementor: "Ken Youensclark",
                implementor_email: "kyclark@email.arizona.edu",
                test: {
                    input_files: [],
                    output_files: [],
                },
            },
            time_limit_seconds: 0,
        };

        const appsUsingTool = [
            {
                integration_date: "2013-05-24T21:44:49.000Z",
                description:
                    "This App will add existing reference annotation information to newly assembled transcripts in GFF format.",
                deleted: false,
                pipeline_eligibility: {
                    is_valid: true,
                    reason: "",
                },
                is_favorite: true,
                integrator_name: "Roger Barthelson",
                beta: false,
                permission: "read",
                can_favor: true,
                disabled: false,
                can_rate: true,
                name: "Annotate transcripts",
                system_id: "de",
                is_public: true,
                id: "676846d4-854a-11e4-980d-7f0fcca75dbb",
                edited_date: "2013-05-24T20:56:03.000Z",
                step_count: 1,
                can_run: true,
                integrator_email: "rogerab@email.arizona.edu",
                app_type: "AGAVE",
                wiki_url:
                    "http://pods.iplantcollaborative.org/wiki/display/DEapps/Annotate+transcripts",
                rating: {
                    average: 0.0,
                    total: 0,
                },
            },
            {
                integration_date: "2017-03-15T19:50:02.000Z",
                description:
                    "The APPLES (Analysis of Plant Promoter-Linked Elements) software package is a set of tools to analyse promoter sequences on a genome-wide scale. The RBH tool is part of the APPLES package which searches for orthologous as Reciprocal Best Hits (RBH).",
                deleted: false,
                pipeline_eligibility: {
                    is_valid: true,
                    reason: "",
                },
                is_favorite: false,
                integrator_name: "Uk Cyverse",
                beta: true,
                permission: "read",
                can_favor: true,
                disabled: false,
                can_rate: true,
                name: "APPLES_rbh",
                system_id: "de",
                is_public: true,
                id: "99df0c66-dce5-11e6-a30f-0242ac120003",
                edited_date: "2017-03-15T19:50:02.000Z",
                step_count: 1,
                can_run: true,
                integrator_email: "admin@cyverse.warwick.ac.uk",
                app_type: "DE",
                rating: {
                    average: 2.0,
                    total: 2,
                },
            },
        ];

        const presenter = {
            getToolInfo: (id, successCallback, errCallback) => {
                console.log("fetching tool info");
                successCallback(toolInfoPublic);
            },
            getAppsForTool: (id, successCallback, errCallback) => {
                console.log("fetching apps used");
                successCallback(appsUsingTool);
            },
        };

        return (
            <ToolInfoDialog
                selectedTool={selectedTool}
                dialogOpen={true}
                presenter={presenter}
            />
        );
    }
}

export default TooInfoDialogTest;
