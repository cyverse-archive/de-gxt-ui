import React, { Component } from "react";
import DatePicker from "./AppStatsComponents/datePicker.js";
import "./AppStatsComponents/button";
import ColorButton from "./AppStatsComponents/button";
import NavBarTabs from "./AppStatsComponents/NavBarTabs";
import ids from "./AppStatsComponents/AllStatsIDs";
import { withStyles } from "@material-ui/core";
import styles from "./AllStatsStyle.js";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import myMessagesFile from "./AppStatsComponents/messages.js";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

class AllStats extends Component {
    render() {
        const { classes } = this.props;

        return (
            <div id={ids.MAIN_PAGE}>
                <header>
                    <div className={classes.datePickers}>
                        <DatePicker
                            label={getMessage("startDate")}
                            id={build(ids.MAIN_PAGE, ids.START_DATE)}
                            className={classes.datePicker}
                        />
                        <DatePicker
                            label={getMessage("endDate")}
                            id={build(ids.MAIN_PAGE, ids.END_DATE)}
                            className={classes.datePicker}
                        />
                        <ColorButton
                            variant="contained"
                            color="primary"
                            className={classes.applyFilterBtn}
                            id={build(ids.MAIN_PAGE, ids.APPLY_FILTER)}
                        >
                            {getMessage("applyFilter")}
                        </ColorButton>
                    </div>
                    <NavBarTabs id={build(ids.MAIN_PAGE, ids.NAV_TAB)} />
                </header>
            </div>
        );
    }
}

export default withI18N(withStyles(styles)(AllStats), myMessagesFile);
