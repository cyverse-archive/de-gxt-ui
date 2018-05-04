/**
 * @author sriram
 *
 */
import React, {Component} from "react";
import SelectField from "material-ui/SelectField";
import MenuItem from "material-ui/MenuItem";

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
            items.push(<MenuItem value={this.props.infoTypes[i]} key={i}
                                 primaryText={this.props.infoTypes[i]}/>);
        }
        return (
            <SelectField
                value={this.props.selectedValue}
                onChange={this.handleChange}
                maxHeight={200}
                hintText={this.props.appearance.selectInfoType()}
                style={style}>
                {items}
            </SelectField>
        );
    }

}

export default InfoTypeSelectionList;
