/**
 * Imported in AllStats.js. This function imports the files for all the tabs that are present under TabPanels
 */

import React from "react";
import PropTypes from "prop-types";
import { AppBar, Tabs, Tab, Typography, Box } from "@material-ui/core";
import JobsTab from "./JobsTab";
import AppsTab from "./AppsTab";
import UsersTab from "./UsersTab";
import ids from "./AllStatsIDs";
import { getMessage, withI18N, build } from "@cyverse-de/ui-lib";
import myMessagesFile from "./messages.js";

function TabPanel(props) {
    const { children, value, baseId, index, ...other } = props;

    return (
        <Typography
            component="div"
            role="tabpanel"
            hidden={value !== index}
            id={`nav-tabpanel-${index}`}
            aria-labelledby={`nav-tab-${index}`}
            {...other}
        >
            <Box p={3}>{children}</Box>
        </Typography>
    );
}

TabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.any.isRequired,
    value: PropTypes.any.isRequired,
};

function a11yProps(baseId, index) {
    return {
        id: build(baseId, index),
        "aria-controls": `nav-tabpanel-${index}`,
    };
}

function NavTabs(props) {
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };
    const { baseId } = props;
    let apps = props.apps,
        distinctLoginData = props.distinctLoginData,
        jobs = props.jobs,
        users = props.users;

    return (
        <div id={baseId}>
            <AppBar position="static">
                <Tabs variant="fullWidth" value={value} onChange={handleChange}>
                    <Tab
                        label={getMessage("jobsAndLogins")}
                        {...a11yProps(baseId, ids.JOBS_TAB)}
                    />
                    <Tab
                        label={getMessage("apps")}
                        {...a11yProps(baseId, ids.APPS_TAB)}
                    />
                    <Tab
                        label={getMessage("users")}
                        {...a11yProps(baseId, ids.USERS_TAB)}
                    />
                </Tabs>
            </AppBar>
            <TabPanel value={value} index={0}>
                <JobsTab
                    distinctLoginData={distinctLoginData}
                    jobs={jobs}
                    baseId={build(baseId, ids.JOBS_TAB)}
                />
            </TabPanel>
            <TabPanel value={value} index={1}>
                <AppsTab apps={apps} baseId={build(baseId, ids.APPS_TAB)} />
            </TabPanel>
            <TabPanel value={value} index={2}>
                <UsersTab users={users} baseId={build(baseId, ids.USERS_TAB)} />
            </TabPanel>
        </div>
    );
}

export default withI18N(NavTabs, myMessagesFile);
