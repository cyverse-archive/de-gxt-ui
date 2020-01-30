import React, { Component } from "react";
import { AllStats } from "../../../../src/apps/admin";
import appsTableData from "./dataFiles/appsData";
import distinctLoginData from "./dataFiles/distinctLoginData";
import jobsData from "./dataFiles/jobsData";
import usersData from "./dataFiles/usersData";
import { formatDateObject } from "@cyverse-de/ui-lib/src/util/DateFormatter";

function AllStatsTest(props) {
    const [selectedStartDate, setSelectedStartDate] = React.useState(
        new Date()
    );
    const [selectedEndDate, setSelectedEndDate] = React.useState(new Date());

    const presenter = {
        handleStartDateChange: (date) => {
            let formattedDate = formatDateObject(date, "YYYY-MM-DD");
            console.log(formattedDate);
            setSelectedStartDate(formattedDate);
        },
        handleEndDateChange: (date) => {
            let formattedDate = formatDateObject(date, "YYYY-MM-DD");
            console.log(formattedDate);
            setSelectedEndDate(formattedDate);
        },
    };

    return (
        <AllStats
            presenter={presenter}
            selectedStartDate={selectedStartDate}
            selectedEndDate={selectedEndDate}
            appsData={appsTableData.apps}
            distinctLoginData={distinctLoginData}
            jobsData={jobsData.jobs}
            usersData={usersData.users}
        />
    );
}

export default AllStatsTest;
