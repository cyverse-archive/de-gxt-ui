/**
 * This function contains the AppsTableHeader and the NumberSelect box.
 * AppsTable contains the actual table which is imported here.
 */

import React from "react";
import AppsTable from "./AppsTable";
import NumberTextfield from "./NumberTextfield";
import styles from "../AllStatsStyle";
import { withStyles, Box } from "@material-ui/core";
import ids from "./AllStatsIDs";
import messages from "./messages";
import { withI18N, getMessage, build } from "@cyverse-de/ui-lib";

function AppTab(props) {
    const classes = props.classes,
        baseId = props.baseId,
        apps = props.apps;

    return (
        <div className={classes.appSelectBar} id={build(baseId, ids.HEADER)}>
            <div>
                <Box className={classes.appSelectText}>
                    {getMessage("topApps")}
                </Box>
            </div>
            <NumberTextfield baseId={build(baseId, ids.NUMBER_TEXT)} />
            <AppsTable appsData={apps} baseId={build(baseId, ids.TABLE)} />
        </div>
    );
}

export default withI18N(withStyles(styles)(AppTab), messages);
