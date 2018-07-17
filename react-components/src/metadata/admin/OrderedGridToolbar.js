/**
 * @author psarando
 */
import React from "react";
import { injectIntl } from "react-intl";

import build from "../../util/DebugIDUtil";
import withI18N, { formatMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import Button from "@material-ui/core/Button";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ContentAdd from "@material-ui/icons/Add";
import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";

let OrderedGridToolbar = props => {
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
    } = props;

    return (
        <Toolbar
            className={classes.toolbar}
        >
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="primary"
                        id={build(parentID, ids.BUTTONS.ADD)}
                        aria-label={formatMessage(intl, "addRow")}
                        onClick={onAddItem}
                >
                    <ContentAdd />
                </Button>
            </div>
            <div className={classes.title}>
                <Typography variant="title" id={build(parentID, ids.TITLE)}>
                    {title}
                </Typography>
                <Typography variant="subheading" className={classes.errorSubTitle} id={build(parentID, ids.TITLE_ERR)}>
                    {error}
                </Typography>
            </div>
            <div className={classes.spacer} />
            <div className={classes.actions}>
                <Button id={build(parentID, ids.BUTTONS.MOVE_UP)}
                        variant="fab"
                        mini
                        color="secondary"
                        aria-label={formatMessage(intl, "moveUp")}
                        className={classes.button}
                        disabled={moveUpDisabled}
                        onClick={() => moveUp()}
                >
                    <KeyboardArrowUp />
                </Button>
            </div>
            <div className={classes.actions}>
                <Button id={build(parentID, ids.BUTTONS.MOVE_DOWN)}
                        variant="fab"
                        mini
                        color="secondary"
                        aria-label={formatMessage(intl, "moveDown")}
                        className={classes.button}
                        disabled={moveDownDisabled}
                        onClick={() => moveDown()}
                >
                    <KeyboardArrowDown />
                </Button>
            </div>
        </Toolbar>
    );
};

export default withStyles(styles)(withI18N(injectIntl(OrderedGridToolbar), intlData));
