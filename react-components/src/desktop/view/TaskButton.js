/**
 * @author sriram
 */
import React, {Component} from "react";
import Button from "@material-ui/core/Button";
import styles from "../style";
import injectSheet from "react-jss";
import {withStyles} from "@material-ui/core/styles";

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
            return (<Button key={this.props.windowConfig.tag}
                            className={this.props.classes.taskbarButtonMinimized}
                            onClick={this.onClick}
                            variant="raised" >
                    {this.props.windowConfig.windowTitle}
                </Button>
            );
        } else {
            return (
                <Button key={this.props.windowConfig.tag}
                        className={this.props.classes.taskbarButton}
                        onClick={this.onClick}
                        variant="contained">
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

export default withStyles(styles)(TaskButton); //need to use withStyles here instead of injectSheet
                                              // of react-jss to override default mui component styles.