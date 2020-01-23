/**
 * @author Flynn
 *
 * Description: This file is importing all the necessary files & everything is being rendered here.
 * Files: All the "Tab" files are imported in NavBarTabs under the TabPanels.
 *        All the "Table" files are imported under their respective "Tab" files.
 *        The data files for storybook are stored in the dataFiles folder
 *        All the components used in AllStats.js are stored in the AppsStatsComponents folder
 */

import React, { Component } from "react";
import DatePicker from "./AppStatsComponents/datePicker.js";
import ColorButton from "./AppStatsComponents/button";
import NavBarTabs from "./AppStatsComponents/NavBarTabs";
import ids from "./AppStatsComponents/AllStatsIDs";
import { withStyles } from "@material-ui/core";
import styles from "./AllStatsStyle.js";
import { getMessage, withI18N, build } from "@cyverse-de/ui-lib";
import myMessagesFile from "./AppStatsComponents/messages.js";

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
