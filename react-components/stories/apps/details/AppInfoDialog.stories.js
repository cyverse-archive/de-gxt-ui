import React, { Component } from "react";
import AppInfoDialog from "../../../src/apps/details/AppInfoDialog";
import { text } from "@storybook/addon-knobs";

class AppInfoDialogTest extends Component {
    render() {
        const app = {
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
        };
        const appDetals = {
            integration_date: "2013-05-24T21:44:49.000Z",
            description:
                "This App will add existing reference annotation information to newly assembled transcripts in GFF format.",
            deleted: false,
            integrator_name: "Roger Barthelson",
            disabled: false,
            suggested_categories: [
                {
                    id: "f9f22c5a-09f5-4630-997c-4e3a00ae924b",
                    name: "Assembly Annotation",
                },
            ],
            hierarchies: [
                {
                    iri: "http://edamontology.org/topic_0003",
                    label: "Topic",
                    subclasses: [
                        {
                            iri: "http://edamontology.org/topic_3070",
                            label: "Biology",
                            subclasses: [
                                {
                                    iri: "http://edamontology.org/topic_3053",
                                    label: "Genetics",
                                    subclasses: [
                                        {
                                            iri:
                                                "http://edamontology.org/topic_3321",
                                            label: "Molecular genetics",
                                            subclasses: [
                                                {
                                                    iri:
                                                        "http://edamontology.org/topic_0203",
                                                    label: "Gene expression",
                                                    subclasses: [
                                                        {
                                                            iri:
                                                                "http://edamontology.org/topic_3308",
                                                            label:
                                                                "Transcriptomics",
                                                        },
                                                    ],
                                                },
                                            ],
                                        },
                                    ],
                                },
                            ],
                        },
                        {
                            iri: "http://edamontology.org/topic_3307",
                            label: "Computational biology",
                            subclasses: [
                                {
                                    iri: "http://edamontology.org/topic_3321",
                                    label: "Molecular genetics",
                                    subclasses: [
                                        {
                                            iri:
                                                "http://edamontology.org/topic_0203",
                                            label: "Gene expression",
                                            subclasses: [
                                                {
                                                    iri:
                                                        "http://edamontology.org/topic_3308",
                                                    label: "Transcriptomics",
                                                },
                                            ],
                                        },
                                    ],
                                },
                            ],
                        },
                        {
                            iri: "http://edamontology.org/topic_3391",
                            label: "Omics",
                            subclasses: [
                                {
                                    iri: "http://edamontology.org/topic_0622",
                                    label: "Genomics",
                                    subclasses: [
                                        {
                                            iri:
                                                "http://edamontology.org/topic_3308",
                                            label: "Transcriptomics",
                                        },
                                    ],
                                },
                            ],
                        },
                    ],
                },
                {
                    iri: "http://edamontology.org/operation_0004",
                    label: "Operation",
                    subclasses: [
                        {
                            iri: "http://edamontology.org/operation_0226",
                            label: "Annotation",
                            subclasses: [
                                {
                                    iri:
                                        "http://edamontology.org/operation_0361",
                                    label: "Sequence annotation",
                                    subclasses: [
                                        {
                                            iri:
                                                "http://edamontology.org/operation_3672",
                                            label: "Gene functional annotation",
                                        },
                                    ],
                                },
                            ],
                        },
                        {
                            iri: "http://edamontology.org/operation_2428",
                            label: "Validation",
                            subclasses: [
                                {
                                    iri:
                                        "http://edamontology.org/operation_3180",
                                    label: "Sequence assembly validation",
                                },
                            ],
                        },
                        {
                            iri: "http://edamontology.org/operation_2945",
                            label: "Analysis",
                            subclasses: [
                                {
                                    iri:
                                        "http://edamontology.org/operation_2403",
                                    label: "Sequence analysis",
                                    subclasses: [
                                        {
                                            iri:
                                                "http://edamontology.org/operation_2478",
                                            label:
                                                "Nucleic acid sequence analysis",
                                            subclasses: [
                                                {
                                                    iri:
                                                        "http://edamontology.org/operation_3180",
                                                    label:
                                                        "Sequence assembly validation",
                                                },
                                            ],
                                        },
                                    ],
                                },
                                {
                                    iri:
                                        "http://edamontology.org/operation_2501",
                                    label: "Nucleic acid analysis",
                                    subclasses: [
                                        {
                                            iri:
                                                "http://edamontology.org/operation_2478",
                                            label:
                                                "Nucleic acid sequence analysis",
                                            subclasses: [
                                                {
                                                    iri:
                                                        "http://edamontology.org/operation_3180",
                                                    label:
                                                        "Sequence assembly validation",
                                                },
                                            ],
                                        },
                                    ],
                                },
                            ],
                        },
                    ],
                },
            ],
            name: "Annotate transcripts",
            tools: [
                {
                    attribution: "rogerab",
                    name: "gffintersect_wrapper.pl",
                    type: "executable",
                    description: "gffintersect",
                    id: "66fbef16-854a-11e4-9d48-ab603f97c137",
                    location:
                        "/usr/local2/AnnotateTranscripts/annotate_transcripts",
                    container: {
                        image: {
                            name: "docker.cyverse.org/backwards-compat",
                            tag: "latest",
                            url:
                                "https://registry.hub.docker.com/u/discoenv/backwards-compat",
                            deprecated: true,
                        },
                    },
                    version: "1.0",
                },
            ],
            system_id: "de",
            references: [
                "",
                "http://trinityrnaseq.sourceforge.net/analysis/diff_expression_analysis.html",
            ],
            categories: [
                {
                    id: "f9f22c5a-09f5-4630-997c-4e3a00ae924b",
                    name: "Assembly Annotation",
                },
            ],
            id: "676846d4-854a-11e4-980d-7f0fcca75dbb",
            edited_date: "2013-05-24T20:56:03.000Z",
            job_stats: {
                job_count_completed: 88,
                job_last_completed: "2018-09-19T19:23:50.000Z",
            },
            integrator_email: "rogerab@email.arizona.edu",
            wiki_url:
                "http://pods.iplantcollaborative.org/wiki/display/DEapps/Annotate+transcripts",
        };
        const quickLaunches = [
            {
                id: "1",
                name: "Qlaunch1",
                description: "This is my first quick launch",
                creator: "sriram@iplantcollaborative.org",
                app_id: "123",
                is_public: false,
                submission: {
                    description: "string",
                    config: {},
                    "file-metadata": [
                        {
                            attr: "string",
                            value: "string",
                            unit: "string",
                        },
                    ],
                    starting_step: 0,
                    name: "string",
                    app_id: "string",
                    system_id: "string",
                    debug: true,
                    create_output_subdir: true,
                    archive_logs: true,
                    output_dir: "string",
                    uuid: "string",
                    notify: true,
                    "skip-parent-meta": true,
                    callback: "string",
                    job_id: "string",
                },
            },
            {
                id: "2",
                name: "Qlaunch2",
                description: "This is my second quick launch",
                creator: "ipctest@iplantcollaborative.org",
                app_id: "456",
                is_public: true,
                submission: {
                    description: "string",
                    config: {},
                    "file-metadata": [
                        {
                            attr: "string",
                            value: "string",
                            unit: "string",
                        },
                    ],
                    starting_step: 0,
                    name: "string",
                    app_id: "string",
                    system_id: "string",
                    debug: true,
                    create_output_subdir: true,
                    archive_logs: true,
                    output_dir: "string",
                    uuid: "string",
                    notify: true,
                    "skip-parent-meta": true,
                    callback: "string",
                    job_id: "string",
                },
            },
        ];
        const presenter = {
            getAppDoc: (id, successCallback, errCallback) => {
                successCallback();
            },
            onAppFavoriteSelected: (
                appDetails,
                successCallback,
                errCallback
            ) => {
                console.log("Favorite clicked!");
            },
            onAppRatingSelected: (
                appDetails,
                val,
                successCallback,
                errCallback
            ) => {
                console.log("Rating value: " + val);
            },
            onAppRatingDeSelected: (
                appDetails,
                successCallback,
                errCallback
            ) => {
                console.log("Rating deleted!");
            },
            getQuickLaunches: (id, handleSuccess, handleFailure) => {
                handleSuccess(quickLaunches);
            },
        };
        return (
            <AppInfoDialog
                baseDebugId="appDetails"
                searchText={text("Search Pattern", "roger")}
                app={app}
                appDetails={appDetals}
                dialogOpen={true}
                presenter={presenter}
                editable={false}
                userName="sriram@iplantcollaborative.org"
                docEditable={false}
            />
        );
    }
}
export default AppInfoDialogTest;
