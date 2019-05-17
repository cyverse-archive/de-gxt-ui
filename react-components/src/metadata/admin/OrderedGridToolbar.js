/**
 * @author psarando
 */
import React, { Component } from "react";

import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import { build, formatMessage, withI18N } from "@cyverse-de/ui-lib";

import Fab from "@material-ui/core/Fab";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ContentAdd from "@material-ui/icons/Add";
import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";

class OrderedGridToolbar extends Component {
    static propTypes = {
        onAddItem: PropTypes.func.isRequired,
        moveUp: PropTypes.func.isRequired,
        moveDown: PropTypes.func.isRequired,
        moveUpDisabled: PropTypes.bool,
        moveDownDisabled: PropTypes.bool,
    };

    render() {
        const {
            classes,
            intl,
            title,
            error,
            parentID,
            onAddItem,
            moveUp,
            moveDown,
            moveUpDisabled,
            moveDownDisabled,
        } = this.props;

        return (
            <Toolbar className={classes.toolbar}>
                <div className={classes.actions}>
                    <Fab
                        id={build(parentID, ids.BUTTONS.ADD)}
                        size="small"
                        color="primary"
                        aria-label={formatMessage(intl, "addRow")}
                        onClick={onAddItem}
                    >
                        <ContentAdd />
                    </Fab>
                </div>
                <div className={classes.title}>
                    <Typography id={build(parentID, ids.TITLE)} variant="h6">
                        {title}
                    </Typography>
                    <Typography
                        id={build(parentID, ids.TITLE_ERR)}
                        variant="subtitle1"
                        className={classes.errorSubTitle}
                    >
                        {error}
                    </Typography>
                </div>
                <div className={classes.spacer} />
                <div className={classes.actions}>
                    <Fab
                        id={build(parentID, ids.BUTTONS.MOVE_UP)}
                        size="small"
                        color="secondary"
                        aria-label={formatMessage(intl, "moveUp")}
                        className={classes.button}
                        disabled={moveUpDisabled}
                        onClick={() => moveUp()}
                    >
                        <KeyboardArrowUp />
                    </Fab>
                </div>
                <div className={classes.actions}>
                    <Fab
                        id={build(parentID, ids.BUTTONS.MOVE_DOWN)}
                        size="small"
                        color="secondary"
                        aria-label={formatMessage(intl, "moveDown")}
                        className={classes.button}
                        disabled={moveDownDisabled}
                        onClick={() => moveDown()}
                    >
                        <KeyboardArrowDown />
                    </Fab>
                </div>
            </Toolbar>
        );
    }
}

export default withStyles(styles)(
    withI18N(injectIntl(OrderedGridToolbar), intlData)
);
