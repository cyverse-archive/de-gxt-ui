/**
 * @author sriram
 */
import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import styles from "../style";
import { withStyles } from "@material-ui/core/styles";

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
        const variant = minimized ? "raised" : "contained";
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
