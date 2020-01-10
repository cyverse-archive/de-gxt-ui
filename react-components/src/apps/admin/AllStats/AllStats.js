import React, { Component } from "react";
import DatePicker from "./AppStatsComponents/datePicker.js";
import "./AppStatsComponents/button";
import ColorButton from "./AppStatsComponents/button";
import NavBarTabs from "./AppStatsComponents/NavBarTabs";
import ids from "./AppStatsComponents/AllStatsIDs";
import { makeStyles, withStyles } from "@material-ui/core";
import styles from "./AllStatsStyle.js";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import myMessagesFile from "./AppStatsComponents/messages.js";

class AllStats extends Component {
    render() {
        const { classes, intl } = this.props;

        return (
            <div id={ids.MAIN_PAGE}>
                <header>
                    <div className={classes.datePickers}>
                        <DatePicker
                            label={getMessage("startDate")}
                            id={ids.START_DATE}
                            className={classes.datePicker}
                        />
                        <DatePicker
                            label={getMessage("endDate")}
                            ids={ids.END_DATE}
                            className={classes.datePicker}
                        />
                        <ColorButton
                            variant="contained"
                            color="primary"
                            className={classes.applyFilterBtn}
                            id={ids.APPLY_FILTER}
                        >
                            {getMessage("applyFilter")}
                        </ColorButton>
                    </div>
                    <NavBarTabs id={ids.NAV_TAB} />
                </header>
            </div>
        );
    }
}

export default withI18N(withStyles(styles)(AllStats), myMessagesFile);
