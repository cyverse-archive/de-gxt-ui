import React from "react";
import Box from "@material-ui/core/Box";
import AppsTable from "./AppsTable";
import NumberTextfield from "./NumberTextfield";
import styles from "../AllStatsStyle";
import { withStyles } from "@material-ui/core";
import ids from "./AllStatsIDs";
import messages from "./messages";
import { withI18N, getMessage } from "@cyverse-de/ui-lib";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

function AppTab(props) {
    const { classes } = props;
    return (
        <div id={props.id}>
            <div
                className={classes.appSelectBar}
                id={build(ids.MAIN_PAGE, ids.NAV_TAB, ids.APPS_TAB, ids.HEADER)}
            >
                <div>
                    <Box className={classes.appSelectText}>
                        {getMessage("topApps")}
                    </Box>
                </div>
                <NumberTextfield />
            </div>
            <AppsTable />
        </div>
    );
}

export default withI18N(withStyles(styles)(AppTab), messages);
