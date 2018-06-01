/**
 * @author sriram
 */
import React, {Component} from "react";
import Toolbar from "@material-ui/core/Toolbar";
import styles from "../style";
import {css} from "aphrodite";
import TaskButton from "./TaskButton";


class Taskbar extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const windows = this.props.windows;
        return (
            <Toolbar className={css(styles.taskbar)}>
                {windows.map(n => {
                    return (
                        <TaskButton windowConfig={n}
                                    taskButtonClickHandler={this.props.taskButtonClickHandler}/>
                    );
                })}
            </Toolbar>
        );
    }
}

export default Taskbar;