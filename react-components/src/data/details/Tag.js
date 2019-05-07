/**
 * @author sriram
 */
import React, { Component } from "react";
import styles from "../style";
import { withStyles } from "@material-ui/core";

class Tag extends Component {
    constructor(props) {
        super(props);
        this.state = {
            className: props.classes.hideTagRemoveStyle,
        };
        this.handleMouseOver = this.handleMouseOver.bind(this);
        this.handleMouseOut = this.handleMouseOut.bind(this);
    }

    handleMouseOver() {
        this.setState({ className: this.props.classes.displayTagRemoveStyle });
    }

    handleMouseOut() {
        this.setState({ className: this.props.classes.hideTagRemoveStyle });
    }

    render() {
        const classes = this.props.classes;
        return (
            <div
                className={classes.tagDivStyle}
                onMouseOver={this.handleMouseOver}
                onMouseOut={this.handleMouseOut}
            >
                <div
                    className={classes.tagStyle}
                    onClick={() => this.props.onClick(this.props.tag)}
                >
                    {this.props.tag.value}
                </div>
                <div
                    id={this.props.tag.id}
                    className={this.state.className}
                    onClick={() => this.props.removeTag(this.props.tag)}
                >
                    X
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(Tag);
