import React from "react";
import Box from "@material-ui/core/Box";
import SimpleSelect from "./SimpleSelect";
import { withStyles } from "@material-ui/core";
import styles from "../AllStatsStyle.js";
import ids from "./AllStatsIDs";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

function AppCount(props) {
    const { classes } = this.props;
    return (
        <div className={classes.appSelectBar}>
            <div>
                <Box className={classes.appSelectText}>
                    {" "}
                    Top Apps in 24 hours:{" "}
                </Box>
            </div>
            <div className={classes.appCountSelect}>
                <SimpleSelect
                    id={build(
                        ids.MAIN_PAGE,
                        ids.NAV_TAB,
                        ids.APPS_TAB,
                        ids.HEADER,
                        ids.SIMPLE_SELECT
                    )}
                    className={classes.appCountSelect}
                />
            </div>
        </div>
    );
}

export default withStyles(styles)(AppCount);
