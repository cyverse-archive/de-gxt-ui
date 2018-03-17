import React, {Component} from 'react';

import Checkbox from 'material-ui/Checkbox';
import Chip from 'material-ui/Chip';
import MenuItem from 'material-ui/MenuItem';
import PropTypes from 'prop-types';
import RaisedButton from 'material-ui/RaisedButton';
import SelectField from 'material-ui/SelectField'
import TextField from 'material-ui/TextField';

import './Search.css'

class SearchForm extends Component {

    constructor(props) {
        super(props);

        this.state = {
            nameHas: '',
            nameHasNot: '',
            createdWithin: '',
            modifiedWithin: '',
            metadataAttributeHas: '',
            metadataValueHas: '',
            ownedBy: '',
            sharedWith: '',
            fileSizeGreater: '',
            fileSizeGreaterUnit: 'KB',
            fileSizeLessThan: '',
            fileSizeLessThanUnit: 'KB',
            taggedWith: '',
            tags: [],
            includeTrash: false
        };

        this.handleChangeFor = this.handleChangeFor.bind(this);
        this.handleDropDownChangeFor = this.handleDropDownChangeFor.bind(this);
        this.handleTagSubmit = this.handleTagSubmit.bind(this);
        this.handleCheckBoxChange = this.handleCheckBoxChange.bind(this);
    }

    handleChangeFor(propertyName, event) {
        let value = event.target.value;

        this.setState(function () {
            return {
                [propertyName]: value
            }
        });
    }

    // The value of the dropdown is set by the index of the menuItem
    handleDropDownChangeFor(propertyName, event, index) {
        this.setState(function () {
            return {
                [propertyName]: index
            }
        });
    }

    handleTagSubmit(event) {
        if (event.key === 'Enter') {
            let value = event.target.value;
            event.preventDefault();

            this.setState(function (prevState) {
                let newTagsArr;
                if (prevState.tags === null) {
                    newTagsArr = [value];
                } else {
                    newTagsArr = prevState.tags.slice();
                    if (!newTagsArr.includes(value)) {
                        newTagsArr.push(value);
                    }
                }

                return {
                    tags: newTagsArr,
                    taggedWith: ''
                }
            })
        }
    }

    handleCheckBoxChange(propertyName, event) {
        this.setState(function (prevState) {
            return {
                [propertyName]: !prevState.includeTrash
            }
        });
    }

    render() {
        let appearance = this.props.appearance;
        let id = this.props.id;
        let tags = this.state.tags;

        return (
            <div id={id} className='form'>
                <div className='row'>
                    <TextField floatingLabelText={appearance.nameHas}
                               fullWidth={true}
                               value={this.state.nameHas}
                               onChange={this.handleChangeFor.bind(null, 'nameHas')}/>
                    <DropDown label={appearance.createdWithin}
                              list={appearance.createdWithinItems}
                              value={this.state.createdWithin}
                              onChange={this.handleDropDownChangeFor.bind(null, 'createdWithin')}/>
                </div>

                <div className='row'>
                    <TextField floatingLabelText={appearance.nameHasNot}
                               fullWidth={true}
                               value={this.state.nameHasNot}
                               onChange={this.handleChangeFor.bind(null, 'nameHasNot')}/>
                    <DropDown label={appearance.modifiedWithin}
                              list={appearance.createdWithinItems}
                              value={this.state.modifiedWithin}
                              onChange={this.handleDropDownChangeFor.bind(null, 'modifiedWithin')}/>
                </div>

                <div className='row'>
                    <TextField floatingLabelText={appearance.metadataAttributeHas}
                               fullWidth={true}
                               value={this.state.metadataAttributeHas}
                               onChange={this.handleChangeFor.bind(null, 'metadataAttributeHas')}/>
                    <TextField floatingLabelText={appearance.ownedBy}
                               hintText={appearance.enterCyVerseUserName}
                               fullWidth={true}
                               value={this.state.ownedBy}
                               onChange={this.handleChangeFor.bind(null, 'ownedBy')}/>
                </div>

                <div className='row'>
                    <TextField floatingLabelText={appearance.metadataValueHas}
                               fullWidth={true}
                               value={this.state.metadataValueHas}
                               onChange={this.handleChangeFor.bind(null, 'metadataValueHas')}/>
                    <TextField floatingLabelText={appearance.sharedWith}
                               hintText={appearance.enterCyVerseUserName}
                               fullWidth={true}
                               value={this.state.sharedWith}
                               onChange={this.handleChangeFor.bind(null, 'sharedWith')}/>
                </div>

                <div className='row'>
                    <TextField floatingLabelText={appearance.fileSizeGreater}
                               floatingLabelFixed={true}
                               fullWidth={true}
                               value={this.state.fileSizeGreater}
                               onChange={this.handleChangeFor.bind(null, 'fileSizeGreater')}/>
                    <DropDown label=' '
                              list={appearance.fileSizes}
                              value={this.state.fileSizeGreaterUnit}
                              onChange={this.handleDropDownChangeFor.bind(null, 'fileSizeGreaterUnit')}/>

                    <TextField floatingLabelText={appearance.fileSizeLessThan}
                               floatingLabelFixed={true}
                               fullWidth={true}
                               value={this.state.fileSizeLessThan}
                               onChange={this.handleChangeFor.bind(null, 'fileSizeLessThan')}/>
                    <DropDown label=' '
                              list={appearance.fileSizes}
                              value={this.state.fileSizeLessThanUnit}
                              onChange={this.handleDropDownChangeFor.bind(null, 'fileSizeLessThanUnit')}/>
                </div>

                <div className='row'>
                    <TextField floatingLabelText={appearance.taggedWith}
                               floatingLabelFixed={true}
                               fullWidth={true}
                               value={this.state.taggedWith}
                               onKeyPress={this.handleTagSubmit}
                               onChange={this.handleChangeFor.bind(null, 'taggedWith')}/>
                    <Checkbox style={{top: '20px'}}
                              label={appearance.includeTrash}
                              onCheck={this.handleCheckBoxChange.bind(null, 'includeTrash')}
                              checked={this.state.includeTrash}/>

                </div>
                <div className='chip'>
                    {tags && tags.map(function (item) {
                        return <Chip>{item}</Chip>
                    })}
                </div>

                <div className='searchButton'>
                    <RaisedButton label={appearance.searchButton}/>
                </div>
            </div>
        )
    }
}

SearchForm.propTypes = {
    appearance: PropTypes.object.isRequired,
    id: PropTypes.string.isRequired
};

function DropDown(props) {
    let label = props.label;
    let list = props.list;
    let value = props.value;
    let onChange = props.onChange;

    return (
        <SelectField floatingLabelText={label}
                     fullWidth={true}
                     value={value}
                     onChange={onChange}>
            {list.map(function (item, index) {
                return <MenuItem key={item} value={index} primaryText={item}/>
            })}
        </SelectField>
    )
}

DropDown.propTypes = {
    label: PropTypes.string.isRequired,
    list: PropTypes.array.isRequired
};


export default SearchForm;