/**
 * @author sriram
 *
 */
import React, {Component} from "react";
import MenuItem from "material-ui/MenuItem";
import {FormattedMessage, IntlProvider} from "react-intl";
import {css} from "aphrodite";
import {FormControl} from "material-ui-next/Form";
import Select from "material-ui-next/Select";
import {InputLabel} from "material-ui-next/Input";
import styles from "../style";


class InfoTypeSelectionList extends Component {
    constructor(props) {
        super(props);
    }

    handleChange = (event) => {
        this.props.onInfoTypeSelect(event.target.value);
   };

    render() {
        const items = [];
        for (let i = 0; i < this.props.infoTypes.length; i++) {
            items.push(<MenuItem value={this.props.infoTypes[i]}
                                 key={this.props.infoTypes[i]}>{this.props.infoTypes[i]}</MenuItem>);
        }
        const menuProps = {
            PaperProps: {
                style: {
                    maxHeight: 200,
                    width: 250,
                },
            },
        };
        return (
            <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
                <FormControl>
                    <InputLabel>{<FormattedMessage id="selectInfoType"/>}</InputLabel>
                    <Select
                        value={this.props.selectedValue}
                        onChange={this.handleChange}
                        className={css(styles.infoTypeStyle)}
                        MenuProps={menuProps}>
                        {items}
                    </Select>
                </FormControl>
                
            </IntlProvider>
        );
    }

}

export default InfoTypeSelectionList;
