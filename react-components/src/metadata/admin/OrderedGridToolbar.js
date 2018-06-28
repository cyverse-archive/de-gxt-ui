/**
 * @author psarando
 */
import React from "react";

import { toolbarStyles } from "../style";

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
        title,
        onAddItem,
        moveUp,
        moveDown,
        moveUpDisabled,
        moveDownDisabled,
    } = props;

    return (
        <Toolbar
            className={classes.root}
        >
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="primary"
                        aria-label="add row"
                        onClick={onAddItem}
                >
                    <ContentAdd />
                </Button>
            </div>
            <div className={classes.title}>
                <Typography variant="title" id="tableTitle">
                    {title}
                </Typography>
            </div>
            <div className={classes.spacer} />
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="secondary"
                        aria-label="move up"
                        className={classes.button}
                        disabled={moveUpDisabled}
                        onClick={() => moveUp()}
                >
                    <KeyboardArrowUp />
                </Button>
            </div>
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="secondary"
                        aria-label="move down"
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

export default withStyles(toolbarStyles)(OrderedGridToolbar);
