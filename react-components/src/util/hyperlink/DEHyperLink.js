import React, { Component } from 'react';
import styles from './style';
import injectSheet from 'react-jss';
import PropTypes from "prop-types";


class DEHyperLink extends Component {

    render() {
        const {classes, text, onClick} = this.props;
        return (
            <a onClick={onClick} className={classes.normal}>{text}</a>
        );
    }

}

DEHyperLink.propTypes = {
    text: PropTypes.string.isRequired,
    onClick: PropTypes.PropTypes.func
};

export default injectSheet(styles)(DEHyperLink);