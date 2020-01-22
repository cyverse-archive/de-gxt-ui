/**
 * This function contains the AppsTableHeader and the NumberSelect box.
 * AppsTable contains the actual table which is imported here.
 */

import React from "react";
import Box from "@material-ui/core/Box";
import AppsTable from "./AppsTable";
import NumberTextfield from "./NumberTextfield";
import styles from "../AllStatsStyle";
import { withStyles } from "@material-ui/core";
import ids from "./AllStatsIDs";
import messages from "./messages";
import { withI18N, getMessage, build } from "@cyverse-de/ui-lib";

function AppTab(props) {
    const { classes } = props;
    const { baseId } = props.id;

    return (
        <div id={baseId}>
            <div
                className={classes.appSelectBar}
                id={build(props.id, ids.HEADER)}
            >
                <div>
                    <Box className={classes.appSelectText}>
                        {getMessage("topApps")}
                    </Box>
                </div>
                <NumberTextfield />
            </div>
            <AppsTable id={build(props.id, ids.TABLE)} />
        </div>
    );
}

export default withI18N(withStyles(styles)(AppTab), messages);
