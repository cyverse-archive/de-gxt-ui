/**
 * @author Flynn
 *
 * Description: This file is importing all the necessary files & everything is being rendered here.
 * Files: All the "Tab" files are imported in NavBarTabs under the TabPanels.
 *        All the "Table" files are imported under their respective "Tab" files.
 *        The data files for storybook are stored in the dataFiles folder
 *        All the components used in AllStats.js are stored in the AppsStatsComponents folder
 */

import React, { Component } from "react";
import DatePicker from "./AppStatsComponents/datePicker.js";
import NavBarTabs from "./AppStatsComponents/NavBarTabs";
import ids from "./AppStatsComponents/AllStatsIDs";
import { withStyles, Button } from "@material-ui/core";
import styles from "./AllStatsStyle.js";
import { getMessage, withI18N, build } from "@cyverse-de/ui-lib";
import myMessagesFile from "./AppStatsComponents/messages.js";

class AllStats extends Component {
    render() {
        const { classes } = this.props;
        const baseId = ids.MAIN_PAGE,
            startDateChange = this.props.presenter.handleStartDateChange,
            endDateChange = this.props.presenter.handleEndDateChange,
            selectedStartDate = this.props.selectedStartDate,
            selectedEndDate = this.props.selectedEndDate,
            apps = this.props.appsData,
            distinctLoginData = this.props.distinctLoginData,
            jobs = this.props.jobsData,
            users = this.props.usersData;

        return (
            <header id={baseId}>
                <div className={classes.datePickers}>
                    <DatePicker
                        dateChange={startDateChange}
                        selectedDate={selectedStartDate}
                        label={getMessage("startDate")}
                        id={build(ids.MAIN_PAGE, ids.START_DATE)}
                        className={classes.datePicker}
                    />
                    <DatePicker
                        dateChange={endDateChange}
                        selectedDate={selectedEndDate}
                        label={getMessage("endDate")}
                        id={build(ids.MAIN_PAGE, ids.END_DATE)}
                        className={classes.datePicker}
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        className={classes.applyFilterBtn}
                        id={build(ids.MAIN_PAGE, ids.APPLY_FILTER)}
                    >
                        {getMessage("applyFilter")}
                    </Button>
                </div>
                <NavBarTabs
                    apps={apps}
                    distinctLoginData={distinctLoginData}
                    jobs={jobs}
                    users={users}
                    baseId={build(ids.MAIN_PAGE, ids.NAV_TAB)}
                />
            </header>
        );
    }
}

export default withI18N(withStyles(styles)(AllStats), myMessagesFile);
