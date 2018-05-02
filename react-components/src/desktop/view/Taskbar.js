/**
 * @author sriram
 */
import React, {Component} from "react";
import {withStyles} from "material-ui-next/styles";
import Toolbar from "material-ui-next/Toolbar";
import Button from "material-ui-next/Button";
import styles from "../style";
import {css} from "aphrodite";


class Taskbar extends Component {
    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
    }

    onClick(event) {
        document.getElementById(event.currentTarget.id).classes = css(styles.taskButtonSelected);
    }

    render() {
        const windows = this.props.windows;
        return (
            <Toolbar className={css(styles.taskbar)}>
                {windows.map(n => {
                    return (
                        <Button id='test' variant="raised" color="primary" className={css(styles.taskbarButton)} onClick={this.onClick}>
                            {n}
                        </Button>
                    )
                })}
            </Toolbar>
        );
    }
}

export default Taskbar;