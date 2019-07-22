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
            integration_date: "2015-06-18T19:12:46Z",
            description:
                "Takes a Blastp output from mapping against a uniprot database and retrieves the matching GO headings.",
            deleted: false,
            pipeline_eligibility: {
                is_valid: true,
                reason: "",
            },
            is_favorite: false,
            integrator_name: "rogerab",
            beta: false,
            permission: "read",
            can_favor: true,
            disabled: false,
            can_rate: true,
            name: "Add GO to Blastp-uniprot output",
            system_id: "de",
            is_public: true,
            id: "f4818d90-13e9-11e5-ae9c-d3fdcba95883",
            edited_date: "2015-06-18T19:12:46Z",
            step_count: 1,
            can_run: true,
            job_stats: {
                job_count_completed: 0,
                job_count: 0,
                job_count_failed: 0,
            },
            integrator_email: "rogerab@email.arizona.edu",
            app_type: "DE",
            rating: {
                average: 4.75,
                total: 4,
            },
            documentation: {
                app_id: "f4818d90-13e9-11e5-ae9c-d3fdcba95883",
                documentation:
                    "### Add GO to Blastp-uniprot output \n> #### Description and Quick Start \n>> Takes a Blastp output from mapping against a uniprot database and retrieves the matching GO headings. \n> #### Test Data \n>> /iplant/home/shared/iplantcollaborative/example_data/Add_GO_to_blast-uniprot_output \n> #### Input File(s) \n>> blastp_out.txt from Blastp a subset of uniprot, or similar tab-delimited blast output, where the ids are uniprot ids. \n> #### Parameters Used in App \n>> None. \n> #### Output File(s) \n>> A non-redundant list of the blast ids used. The goa_entries.srt.txt file, which is the sorted list of blast ids matched with appropriate GO annotation headings.",
                references: [
                    "http://archive.sysbio.harvard.edu/csb/resources/computational/scriptome/UNIX/Tools/Merge.html",
                ],
                created_on: "2015-06-18T19:12:46Z",
                modified_on: "2019-07-19T21:53:27Z",
                created_by: "rogerab@iplantcollaborative.org",
                modified_by: "aramsey@iplantcollaborative.org",
            },
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
