import React, { Component } from "react";
import { AllStats } from "../../../../src/apps/admin";
import appsTableData from "./dataFiles/appsData";
import distinctLoginData from "./dataFiles/distinctLoginData";
import jobsData from "./dataFiles/jobsData";
import usersData from "./dataFiles/usersData";

class AllStatsTest extends Component {
    render() {
        return (
            <AllStats
                appsData={appsTableData.apps}
                distinctLoginData={distinctLoginData}
                jobsData={jobsData.jobs}
                usersData={usersData.users}
            />
        );
    }
}

export default AllStatsTest;
