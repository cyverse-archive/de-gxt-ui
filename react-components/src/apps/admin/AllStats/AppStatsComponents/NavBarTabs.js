import React from "react";
import PropTypes from "prop-types";
import AppBar from "@material-ui/core/AppBar";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import JobsTab from "./JobsTab";
import AppsTab from "./AppsTab";
import UsersTab from "./UsersTab";
import ids from "./AllStatsIDs";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import myMessagesFile from "./messages.js";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

function TabPanel(props) {
    const { children, value, index, ...other } = props;

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

function a11yProps(index) {
    return {
        id: `nav-tab-${index}`,
        "aria-controls": `nav-tabpanel-${index}`,
    };
}

function LinkTab(props) {
    return (
        <Tab
            component="a"
            onClick={(event) => {
                event.preventDefault();
            }}
            {...props}
        />
    );
}

function NavTabs(props) {
    const classes = props;
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <div className={classes.root} id={props.id}>
            <AppBar position="static">
                <Tabs
                    variant="fullWidth"
                    value={value}
                    onChange={handleChange}
                    aria-label="nav tabs example"
                >
                    <LinkTab
                        label={getMessage("jobsAndLogins")}
                        href="/drafts"
                        {...a11yProps(0)}
                    />
                    <LinkTab
                        label={getMessage("apps")}
                        href="/trash"
                        {...a11yProps(1)}
                    />
                    <LinkTab
                        label={getMessage("users")}
                        href="/spam"
                        {...a11yProps(2)}
                    />
                </Tabs>
            </AppBar>
            <TabPanel value={value} index={0}>
                <JobsTab id={build(ids.MAIN_PAGE, ids.NAV_TAB, ids.JOBS_TAB)} />
            </TabPanel>
            <TabPanel value={value} index={1}>
                <AppsTab id={build(ids.MAIN_PAGE, ids.NAV_TAB, ids.APPS_TAB)} />
            </TabPanel>
            <TabPanel value={value} index={2}>
                <UsersTab
                    id={build(ids.MAIN_PAGE, ids.NAV_TAB, ids.USERS_TAB)}
                />
            </TabPanel>
        </div>
    );
}

export default withI18N(NavTabs, myMessagesFile);
