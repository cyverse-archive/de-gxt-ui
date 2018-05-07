/**
 * @author sriram
 */
import React, {Component} from "react";
import {FormattedMessage, IntlProvider} from "react-intl";

class Tag extends Component {
    constructor(props) {
        super(props);
    }

    handleMouseOver = () => {
        document.getElementById(this.props.tag.id).style.display = 'block';
    }

    handleMouseOut = () => {
        document.getElementById(this.props.tag.id).style.display = 'none';
    }

    render() {
        return (
            <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
                <div className={this.props.appearance.css().tagDivStyle()} onMouseOver={this.handleMouseOver}
                     onMouseOut={this.handleMouseOut}>
                    <div className={this.props.appearance.css().tagStyle()}
                         onClick={() => this.props.onClick(this.props.tag)}>{this.props.tag.value}</div>
                    <div title={<FormattedMessage id="removeTag"/>} id={this.props.tag.id}
                         className={this.props.appearance.css().tagRemoveStyle()}
                         onClick={() => this.props.removeTag(this.props.tag)}> X
                    </div>
                </div>
            </IntlProvider>
        );
    }
}

export default Tag;
