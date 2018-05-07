/**
 * @author sriram
 *
 */
import React, {Component} from "react";
import SelectField from "material-ui/SelectField";
import MenuItem from "material-ui/MenuItem";
import {FormattedMessage, IntlProvider} from "react-intl";

class InfoTypeSelectionList extends Component {
    constructor(props) {
        super(props);
    }

    handleChange = (event, index, value) => {
        this.props.onInfoTypeSelect(value);
   };

    render() {
        const style = {width: "100px", fontSize: "small"};
        const items = [];
        for (let i = 0; i < this.props.infoTypes.length; i++) {
            items.push(<MenuItem value={this.props.infoTypes[i]} key={this.props.infoTypes[i]}
                                 primaryText={this.props.infoTypes[i]}/>);
        }
        return (
            <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
                <SelectField
                    value={this.props.selectedValue}
                    onChange={this.handleChange}
                    maxHeight={200}
                    hintText={<FormattedMessage id="selectInfoType" />}
                    style={style}>
                    {items}
                </SelectField>
            </IntlProvider>
        );
    }

}

export default InfoTypeSelectionList;
