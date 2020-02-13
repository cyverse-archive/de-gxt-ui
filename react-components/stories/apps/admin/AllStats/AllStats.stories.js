/**
 * @author Flynn
 *
 * Description: This file is importing all the data files used in the tables.
 *              The example data is being passed from AllStats.stories to AllStats and then to
 *              their respective components.
 *              The presenter is used for mock API callbacks and is passed from AllStats.stories
 *              to AllStats where presenter, selectedStartDate and selectedEndDate are passed to
 *              their respective datePicker components.
 */

import React from "react";
import { AllStats } from "../../../../src/apps/admin";
import appsTableData from "./dataFiles/appsData";
import distinctLoginData from "./dataFiles/distinctLoginData";
import jobsData from "./dataFiles/jobsData";
import usersData from "./dataFiles/usersData";
import { formatDateObject } from "@cyverse-de/ui-lib/src/util/DateFormatter";

function AllStatsTest() {
    const [selectedStartDate, setSelectedStartDate] = React.useState(
        new Date()
    );
    const [selectedEndDate, setSelectedEndDate] = React.useState(new Date());

    const presenter = {
        handleStartDateChange: (date) => {
            let formattedDate = formatDateObject(date, "YYYY-MM-DD");
            console.log(formattedDate);
            setSelectedStartDate(date);
        },
        handleEndDateChange: (date) => {
            let formattedDate = formatDateObject(date, "YYYY-MM-DD");
            console.log(formattedDate);
            setSelectedEndDate(date);
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
