/**
 * @author sriram
 */
import React, { Component } from "react";
import styles from "../style";
import { Button, withStyles } from "@material-ui/core";

class TaskButton extends Component {
    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
    }

    onClick(event) {
        this.props.taskButtonClickHandler(this.props.windowConfig);
    }

    render() {
        const {
            classes,
            windowConfig: { minimized, windowTitle },
        } = this.props;
        const variant = minimized ? "outlined" : "contained";
        const className = minimized
            ? classes.taskbarButtonMinimized
            : classes.taskbarButton;

        return (
            <Button
                variant={variant}
                className={className}
                onClick={this.onClick}
            >
                {windowTitle}
            </Button>
        );
    }
}

export default withStyles(styles)(TaskButton); //need to use withStyles here instead of injectSheet
// of react-jss to override default mui component styles.
