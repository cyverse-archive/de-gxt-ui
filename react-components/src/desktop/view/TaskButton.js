/**
 * @author sriram
 */
import React, {Component} from "react";
import Button from "@material-ui/core/Button";
import styles from "../style";
import {css} from "aphrodite";

class TaskButton extends Component {
    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
        this.getButton = this.getButton.bind(this);
    }

    onClick(event) {
       this.props.taskButtonClickHandler(this.props.windowConfig);
    }

    getButton() {
        if (this.props.windowConfig.minimized === true) {
            return (<Button variant="raised" className={css(styles.taskbarButtonMinimized)}
                            onClick={this.onClick}>
                    {this.props.windowConfig.windowTitle}
                </Button>
            );
        } else {
            return (
                <Button variant="outlined" className={css(styles.taskbarButton)} onClick={this.onClick}>
                    {this.props.windowConfig.windowTitle}
                </Button>
            );
        }
    }

    render() {
        return (
            this.getButton()
        );
    }

}

export default TaskButton;