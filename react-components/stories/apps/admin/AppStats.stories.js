/**
 *
 * @author sriram
 *
 */
import React, {Component} from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {getCyVerseTheme} from "../../../src/lib";
import AppStats from "../../../src/apps/admin/AppStats";


class AppStatsTest extends Component {
    render() {
        const appearance = {
            name: () => "Name",
            total: () => "Total",
            completed: () => "Completed",
            failed: () => "Failed",
            rating: () => "Rating",
            lastUsed: () => "Last Used",
            lastCompleted: () => "Last Completed",
            startDate: () => "Start Date...",
            endDate: () => "End Date...",
            searchApps: () => "Search Apps...",
            emptyDate: () => "-",
            applyFilter: () => "Apply Filter",
            toolbarStyle: () => {},
            gridStyle: () => {},
            buttonStyle: () => {},
            integrator: () => "Integrator",
            beta: () => "Beta",
            system: () => "System",
        };
        const appStats =
            [{
                "integration_date": "2013-02-10T14:16:44Z",
                "description": "",
                "deleted": true,
                "pipeline_eligibility": {
                    "is_valid": false,
                    "reason": "Job type, fAPI, canu0027t currently be included in a pipeline."
                },
                "is_favorite": false,
                "integrator_name": "Matthew Vaughn",
                "beta": true,
                "permission": "read",
                "can_favor": true,
                "disabled": false,
                "can_rate": true,
                "name": "1000 Bulls GATK Genotyping",
                "system_id": "de",
                "is_public": true,
                "id": "bec33f58-bfbc-4702-bbc7-71a68657faa6",
                "edited_date": "2013-02-10T14:14:28Z",
                "step_count": 1,
                "can_run": true,
                "job_stats": {
                    "job_count_completed": 0,
                    "job_count": 0,
                    "job_count_failed": 0,
                    "job_last_completed": 1521650432000,
                     "last_used": 1521650292000,
                },
                "rating": {
                    "average": 0.0,
                    "total": 0
                }
            }];

        const presenter = {
            searchApps: (searchText, startDate, endDate, resultCallback) => {
                resultCallback(appStats)
            },
        }

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <AppStats presenter={presenter} appearance={appearance}/>
            </MuiThemeProvider>
        )

    }
}

export default AppStatsTest;