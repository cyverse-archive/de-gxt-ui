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
        this.state = {
            value: this.props.selectedValue,
        };
    }

    handleChange = (event, index, value) => {
        this.setState({value: value});
        this.props.view.fireSetInfoTypeEvent(value);
   };

    componentWillReceiveProps(nextProps)  {
        if (this.props.selectedValue !== nextProps.selectedValue) {
            this.setState({value: nextProps.selectedValue});
       }
    }

    render() {
        const items = [];
        for (let i = 0; i < this.props.infoTypes.length; i++) {
            items.push(<MenuItem value={this.props.infoTypes[i]} key={i}
                                 primaryText={this.props.infoTypes[i]}/>);
        }
        return (
            <SelectField
                value={this.state.value}
                onChange={this.handleChange}
                maxHeight={200}
                hintText={this.props.appearance.selectInfoType()}
                style={this.props.appearance.infoTypeSelectStyle()}>
                {items}
            </SelectField>
        );
    }

}

export default InfoTypeSelectionList;
