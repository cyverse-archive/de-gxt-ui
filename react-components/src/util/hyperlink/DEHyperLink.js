import React, { Component } from "react";
import styles from "./style";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core";
class DEHyperLink extends Component {
    render() {
        const { classes, text, onClick, ...custom } = this.props;
        return (
            <span onClick={onClick} {...custom} className={classes.normal}>
                {text}
            </span>
        );
    }
}

DEHyperLink.propTypes = {
    text: PropTypes.string.isRequired,
    onClick: PropTypes.func,
};

export default withStyles(styles)(DEHyperLink);
