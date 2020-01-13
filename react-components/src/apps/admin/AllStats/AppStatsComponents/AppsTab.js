import React from "react";
import Box from "@material-ui/core/Box";
import AppsTable from "./AppsTable";
import NumberTextfield from "./NumberTextfield";
import styles from "../AllStatsStyle";
import { withStyles } from "@material-ui/core";
import ids from "./AllStatsIDs";
import messages from "./messages";
import { injectIntl } from "react-intl";
import { withI18N, getMessage, formatMessage } from "@cyverse-de/ui-lib";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

function AppTab(props) {
    const { classes, intl } = props;
    const duration = "24 hours";
    return (
        <div id={props.id}>
            <div
                className={classes.appSelectBar}
                id={build(ids.MAIN_PAGE, ids.NAV_TAB, ids.APPS_TAB, ids.HEADER)}
            >
                <div>
                    <Box className={classes.appSelectText}>
                        {getMessage("topApps", {
                            values: {
                                duration: duration,
                            },
                        })}
                    </Box>
                </div>
                <NumberTextfield />
            </div>
            <AppsTable />
        </div>
    );
}

export default withStyles(styles)(AppTab);
