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
            createdWithin: null,
            modifiedWithin: null,
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
        this.handleDeleteTag = this.handleDeleteTag.bind(this);
        this.handleSubmitForm = this.handleSubmitForm.bind(this);
    }

    handleSubmitForm() {
        this.props.presenter.onSearchBtnClicked(this.state);
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
    handleDropDownChangeFor(propertyName, event, index, value) {
        event.preventDefault();

        this.setState(function () {
            return {
                [propertyName]: value
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

    handleDeleteTag(selectedTag) {
        let tags = this.state.tags;
        let index = tags.indexOf(selectedTag);
        tags.splice(index, 1);
        this.setState(function () {
            return {
                tags: tags
            }
        })
    }

    render() {
        let dateIntervals = this.props.dateIntervals;
        let appearance = this.props.appearance;
        let id = this.props.id;
        let tags = this.state.tags;

        let sizesList = appearance.fileSizes().split(', ');

        return (
            <div id={id}>
                <table className='form'>
                    <tbody>
                    <tr>
                        <td>
                            <TextField floatingLabelText={appearance.nameHas()}
                                       fullWidth={true}
                                       value={this.state.nameHas}
                                       onChange={this.handleChangeFor.bind(null, 'nameHas')}/>
                        </td>
                        <td>
                            <DateIntervalDropDown label={appearance.createdWithin()}
                                                  list={dateIntervals}
                                                  value={this.state.createdWithin}
                                                  onChange={this.handleDropDownChangeFor.bind(null, 'createdWithin')}/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <TextField floatingLabelText={appearance.nameHasNot()}
                                       fullWidth={true}
                                       value={this.state.nameHasNot}
                                       onChange={this.handleChangeFor.bind(null, 'nameHasNot')}/>
                        </td>
                        <td>
                            <DateIntervalDropDown label={appearance.modifiedWithin()}
                                                  list={dateIntervals}
                                                  value={this.state.modifiedWithin}
                                                  onChange={this.handleDropDownChangeFor.bind(null, 'modifiedWithin')}/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <TextField floatingLabelText={appearance.metadataAttributeHas()}
                                       fullWidth={true}
                                       value={this.state.metadataAttributeHas}
                                       onChange={this.handleChangeFor.bind(null, 'metadataAttributeHas')}/>
                        </td>
                        <td>
                            <TextField floatingLabelText={appearance.ownedBy()}
                                       hintText={appearance.enterCyVerseUserName()}
                                       fullWidth={true}
                                       value={this.state.ownedBy}
                                       onChange={this.handleChangeFor.bind(null, 'ownedBy')}/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <TextField floatingLabelText={appearance.metadataValueHas()}
                                       fullWidth={true}
                                       value={this.state.metadataValueHas}
                                       onChange={this.handleChangeFor.bind(null, 'metadataValueHas')}/>
                        </td>
                        <td>
                            <TextField floatingLabelText={appearance.sharedWith()}
                                       hintText={appearance.enterCyVerseUserName()}
                                       fullWidth={true}
                                       value={this.state.sharedWith}
                                       onChange={this.handleChangeFor.bind(null, 'sharedWith')}/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <table>
                                <tbody>
                                <tr>
                                    <td style={{'width': '100%'}}>
                                        <TextField floatingLabelText={appearance.fileSizeGreater()}
                                                   floatingLabelFixed={true}
                                                   fullWidth={true}
                                                   value={this.state.fileSizeGreater}
                                                   onChange={this.handleChangeFor.bind(null, 'fileSizeGreater')}/>
                                    </td>
                                    <td>
                                        <FileSizeDropDown label=' '
                                                          list={sizesList}
                                                          value={this.state.fileSizeGreaterUnit}
                                                          onChange={this.handleDropDownChangeFor.bind(null, 'fileSizeGreaterUnit')}/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                        <td>
                            <table>
                                <tbody>
                                <tr>
                                    <td style={{'width': '100%'}}>

                                        <TextField floatingLabelText={appearance.fileSizeLessThan()}
                                                   floatingLabelFixed={true}
                                                   fullWidth={true}
                                                   value={this.state.fileSizeLessThan}
                                                   onChange={this.handleChangeFor.bind(null, 'fileSizeLessThan')}/>
                                    </td>
                                    <td>
                                        <FileSizeDropDown label=' '
                                                          list={sizesList}
                                                          value={this.state.fileSizeLessThanUnit}
                                                          onChange={this.handleDropDownChangeFor.bind(null, 'fileSizeLessThanUnit')}/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <TextField floatingLabelText={appearance.taggedWith()}
                                       floatingLabelFixed={true}
                                       fullWidth={true}
                                       value={this.state.taggedWith}
                                       onKeyPress={this.handleTagSubmit}
                                       onChange={this.handleChangeFor.bind(null, 'taggedWith')}/>
                        </td>
                        <td>
                            <Checkbox style={{top: '20px'}}
                                      label={appearance.includeTrash()}
                                      onCheck={this.handleCheckBoxChange.bind(null, 'includeTrash')}
                                      checked={this.state.includeTrash}/>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            <div className='chip'>
                                {tags && tags.map(function (tag) {
                                    return (
                                        <Chip onRequestDelete={this.handleDeleteTag.bind(null, tag)}>
                                            {tag}
                                        </Chip>
                                    )
                                }, this)}
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div className='searchButton'>
                    <RaisedButton label={appearance.searchBtn()}
                                  onClick={this.handleSubmitForm}/>
                </div>
            </div>
        )
    }
}

SearchForm.propTypes = {
    presenter: PropTypes.shape({
        onSearchBtnClicked: PropTypes.func.isRequired
    }),
    dateIntervals: PropTypes.arrayOf(
        PropTypes.shape({
            label: PropTypes.string.isRequired,
            from: PropTypes.number,
            to: PropTypes.number
        })
    ),
    appearance: PropTypes.object.isRequired,
    id: PropTypes.string.isRequired
};

function DateIntervalDropDown(props) {
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
                return <MenuItem key={index} value={item} primaryText={item.label}/>
            })}
        </SelectField>
    )
}

DateIntervalDropDown.propTypes = {
    label: PropTypes.string.isRequired,
    list: PropTypes.array.isRequired
};

function FileSizeDropDown(props) {
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
                return <MenuItem key={index} value={item} primaryText={item}/>
            })}
        </SelectField>
    )
}

FileSizeDropDown.propTypes = {
    label: PropTypes.string.isRequired,
    list: PropTypes.array.isRequired
};


export default SearchForm;