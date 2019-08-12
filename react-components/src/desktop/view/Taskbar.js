/**
 * @author sriram
 */
import React, { Component } from "react";
import styles from "../style";
import TaskButton from "./TaskButton";
import { Toolbar, withStyles } from "@material-ui/core";

class Taskbar extends Component {
    render() {
        const { windows, classes } = this.props;
        return (
            <Toolbar className={classes.taskbar}>
                {windows.map((n) => {
                    return (
                        <TaskButton
                            key={n.tag}
                            windowConfig={n}
                            taskButtonClickHandler={
                                this.props.taskButtonClickHandler
                            }
                        />
                    );
                })}
            </Toolbar>
        );
    }
}

export default withStyles(styles)(Taskbar); //need use to withStyles here instead of injectSheet
// of react-jss to override default mui component styles.
