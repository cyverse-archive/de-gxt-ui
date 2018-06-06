import React, { Component } from 'react';
import styles from './style';
import injectSheet from 'react-jss';


class DEHyperLink extends Component {

    render() {
        const classes = this.props.classes;
        return (
            <a onClick={this.props.onClick} className={classes.normal}>{this.props.text}</a>
        );
    }

}

export default injectSheet(styles)(DEHyperLink);