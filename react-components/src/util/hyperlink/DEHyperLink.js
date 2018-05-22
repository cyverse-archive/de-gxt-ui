import React, { Component } from 'react';
import styles from './style';
import {css} from 'aphrodite';


class DEHyperLink extends Component {

    render() {
        return (
            <a className={css(styles.normal)}>{this.props.text}</a>
        );
    }

}

export default DEHyperLink;