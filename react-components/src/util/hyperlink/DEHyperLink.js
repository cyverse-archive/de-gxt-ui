import React, { Component } from "react";
import styles from "./style";
import injectSheet from "react-jss";
import PropTypes from "prop-types";

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

export default injectSheet(styles)(DEHyperLink);
