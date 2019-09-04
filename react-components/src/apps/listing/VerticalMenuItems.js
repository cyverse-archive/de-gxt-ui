import React from "react";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import MenuItem from "@material-ui/core/MenuItem";
import { makeStyles } from "@material-ui/core";

/**
 *
 * @author sriram
 *
 * Menu items for Apps Tile and table view
 *
 */
const useStyles = makeStyles((theme) => ({
    menuItem: {
        fontSize: 10,
        padding: 5,
    },
}));

function VerticalMenuItems(props) {
    const classes = useStyles();
    return (
        <React.Fragment>
            <MenuItem
                disabled={false}
                className={classes.menuItem}
                data-disabled={false}
            >
                {getMessage("appInfo")}
            </MenuItem>
            <MenuItem
                disabled={false}
                className={classes.menuItem}
                data-disabled={false}
            >
                {getMessage("addToFavorites")}
            </MenuItem>
            <MenuItem
                disabled={false}
                className={classes.menuItem}
                data-disabled={false}
            >
                {getMessage("removeFromFavorites")}
            </MenuItem>
            <MenuItem
                disabled={false}
                className={classes.menuItem}
                data-disabled={false}
            >
                {getMessage("comments")}
            </MenuItem>
            <MenuItem
                disabled={false}
                className={classes.menuItem}
                data-disabled={false}
            >
                {getMessage("notSupported")}
            </MenuItem>
        </React.Fragment>
    );
}

export default withI18N(injectIntl(VerticalMenuItems), intlData);
