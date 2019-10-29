import React, { Component } from "react";

import AdminAppDetails from "../../../src/apps/admin/AdminAppDetails";
import { object } from "@storybook/addon-knobs";

class AdminAppDetailsTest extends Component {
    render() {
        const presenter = {
            closeAppDetailsDlg: () => console.log("close"),

            onSaveAppSelected: (app, resolve, reject) => {
                setTimeout(() => {
                    console.log("Save app selected", app);
                    resolve();
                }, 1000);
            },

            addAppDocumentation: (systemId, appId, appDoc, resolve, reject) => {
                console.log("ADD app documentation", systemId, appId, appDoc);
                resolve();
            },

            updateAppDocumentation: (
                systemId,
                appId,
                appDoc,
                resolve,
                reject
            ) => {
                console.log(
                    "UPDATE app documentation",
                    systemId,
                    appId,
                    appDoc
                );
                resolve();
            },

            updateBetaStatus: (app, resolve, reject) => {
                console.log("Updated beta status", app);
                resolve();
            },
        };

        const app = {
            integration_date: "2018-11-28T18:20:32.000Z",
            description:
                "Jupyter Lab based on jupyter/datascience-notebook with updated iJab plugin and jupyterlab_irods",
            deleted: false,
            pipeline_eligibility: {
                is_valid: false,
                reason:
                    "Job type, interactive, canu0027t currently be included in a pipeline.",
            },
            is_favorite: false,
            integrator_name: "Upendra Kumar Devisetty",
            beta: false,
            permission: "read",
            can_favor: true,
            disabled: false,
            can_rate: true,
            name: "JupyterLab-0.0.3",
            system_id: "de",
            is_public: true,
            id: "34f2c392-9a8a-11e8-9c8e-008cfa5ae621",
            edited_date: "2019-02-28T19:12:20.000Z",
            step_count: 1,
            can_run: true,
            job_stats: {
                job_count_completed: 79,
                job_last_completed: "2019-10-25T16:23:08.000Z",
                job_count: 148,
                job_count_failed: 2,
                last_used: "2019-10-21T18:51:51.000Z",
            },
            integrator_email: "upendra@cyverse.org",
            app_type: "DE",
            rating: {
                average: 5.0,
                total: 2,
            },
        };

        const details = {
            integration_date: "2018-11-28T18:20:32.000Z",
            description:
                "Jupyter Lab" +
                " based on jupyter/datascience-notebook with updated iJab plugin and jupyterlab_irods",
            deleted: false,
            integrator_name: "Upendra Kumar Devisetty",
            disabled: false,
            suggested_categories: [],
            hierarchies: [
                {
                    iri: "http://edamontology.org/topic_0003",
                    label: "Topic",
                    subclasses: [
                        {
                            iri: "http://edamontology.org/topic_3316",
                            label: "Computer science",
                            subclasses: [
                                {
                                    iri: "http://edamontology.org/topic_0092",
                                    label: "Data visualisation",
                                },
                                {
                                    iri: "http://edamontology.org/topic_3474",
                                    label: "Machine learning",
                                },
                                {
                                    iri: "http://edamontology.org/topic_3473",
                                    label: "Data mining",
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
                            iri: "http://edamontology.org/operation_2945",
                            label: "Analysis",
                        },
                    ],
                },
            ],
            name: "JupyterLab-0.0.3",
            tools: [
                {
                    name: "jupyter-lab",
                    type: "interactive",
                    description:
                        "Cyverse Jupyter Lab beta with updated iJab plugin and jupyterlab_irods",
                    id: "5db4e2c7-7a0a-492a-bf79-09cba3801e0d",
                    container: {
                        image: {
                            name: "gims.cyverse.org:5000/jupyter-lab",
                            tag: "1.0",
                            deprecated: false,
                        },
                    },
                    version: "0.0.3",
                },
            ],
            system_id: "de",
            references: [
                "https://cyverse-visual-interactive-computing-environment.readthedocs-hosted.com/en/latest/user_guide/quick-jupyter.html",
            ],
            documentation: {
                app_id: "34f2c392-9a8a-11e8-9c8e-008cfa5ae621",
                documentation:
                    "### JupyterLab-0.0.3 \n #### Description and Quick Start \n" +
                    " Jupyter Lab based on jupyter/datascience-notebook with updated iJab plugin and" +
                    " jupyterlab_irods \n #### Test Data \n" +
                    " /iplant/home/shared/iplantcollaborative/example_data/VICE/JupyterLab \n ####" +
                    " Input File(s)\n  Any file or folder \n #### Parameters Used in App" +
                    " \n No parameters \n #### Output File(s) \n No outputs",
                references: [
                    "https://cyverse-visual-interactive-computing-environment.readthedocs-hosted.com/en/latest/user_guide/quick-jupyter.html",
                ],
                created_on: "2018-11-28T18:20:32.000Z",
                modified_on: "2019-02-28T19:12:20.000Z",
                created_by: "upendra_35@iplantcollaborative.org",
                modified_by: "upendra_35@iplantcollaborative.org",
            },
            categories: [],
            id: "34f2c392-9a8a-11e8-9c8e-008cfa5ae621",
            edited_date: "2019-02-28T19:12:20.000Z",
            job_stats: {
                job_count_completed: 79,
                job_last_completed: "2019-10-25T16:23:08.000Z",
                job_count: 148,
                job_count_failed: 2,
                last_used: "2019-10-21T18:51:51.000Z",
            },
            integrator_email: "upendra@cyverse.org",
            extra: {
                htcondor: {
                    extra_requirements: "TRUE && TRUE",
                },
            },
        };

        const restrictedChars = ":@/\\|^#;[]{}<>";
        const restrictedStartingChars = "~.$";
        const createDocWikiUrl =
            "https://wiki.cyverse.org/wiki/display/DEmanual/Sharing+your+App+or+Workflow+and+Editing+the+User+Manual#Publish";
        const documentationTemplateUrl =
            "https:qa.cyverse.org/belphegor/app-doc-template.txt";

        const parentId = "adminAppDetailsDlg";
        return (
            <AdminAppDetails
                open={true}
                parentId={parentId}
                app={object("app", app)}
                details={object("details", details)}
                restrictedChars={restrictedChars}
                restrictedStartingChars={restrictedStartingChars}
                createDocWikiUrl={createDocWikiUrl}
                documentationTemplateUrl={documentationTemplateUrl}
                presenter={presenter}
            />
        );
    }
}

export default AdminAppDetailsTest;
