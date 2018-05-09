/**
 * @author sriram
 */
import React, {Component} from "react";
import {FormattedMessage, IntlProvider} from "react-intl";
import styles from "../style";
import {css} from "aphrodite";
import jss from "jss";

class Tag extends Component {

    handleMouseOver = () => {
        let ele = document.getElementById(this.props.tag.id);
        jss.createRule({
            display: 'block',
        }).applyTo(ele);
    };

    handleMouseOut = () => {
        let ele = document.getElementById(this.props.tag.id);
        jss.createRule({
            display: 'none',
        }).applyTo(ele);
    };


    render() {
        const removeStyle = {
            fontWeight: 'bold',
            cursor: 'pointer',
            display: 'none',
            float: 'right',
            fontSize: 12,
            borderLeft: '1px solid',
            paddingLeft: '1px',

        }; // does not work with jss classname approach

        return (
            <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
                <div className={css(styles.tagDivStyle)} onMouseOver={this.handleMouseOver}
                     onMouseOut={this.handleMouseOut}>
                    <div className={css(styles.tagStyle)}
                         onClick={() => this.props.onClick(this.props.tag)}>{this.props.tag.value}</div>
                    <div title={<FormattedMessage id="removeTag"/>} id={this.props.tag.id}
                         style={removeStyle} onClick={() => this.props.removeTag(this.props.tag)}> X
                    </div>
                </div>
            </IntlProvider>
        );
    }
}

export default Tag;
