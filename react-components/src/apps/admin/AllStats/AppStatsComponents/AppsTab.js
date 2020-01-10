import React from "react";
import Box from "@material-ui/core/Box";
import AppsTable from "./AppsTable";
import NumberTextfield from "./NumberTextfield";
import styles from "../AllStatsStyle";
import { withStyles } from "@material-ui/core";
import ids from "./AllStatsIDs";

function AppTab(props) {
    const { classes, intl } = props;

    return (
        <div id={props.id}>
            <div className={classes.appSelectBar} id={ids.APPS_TAB_SELECT}>
                <div>
                    <Box className={classes.appSelectText}>
                        {" "}
                        Top Apps in 24 hours:{" "}
                    </Box>
                </div>
                <NumberTextfield />
            </div>
            <AppsTable />
        </div>
    );
}

export default withStyles(styles)(AppTab);
