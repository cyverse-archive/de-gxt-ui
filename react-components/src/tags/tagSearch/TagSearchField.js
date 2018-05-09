import React, {Component} from 'react';

import AutoComplete from 'material-ui/AutoComplete';
import PropTypes from 'prop-types';
import Tag from "./Tag";
import injectSheet from 'react-jss';
import styles from './styles';
import ids from './ids';

class TagSearchField extends Component {
    constructor(props) {
        super(props);

        this.onTagValueChange = this.onTagValueChange.bind(this);
        this.onEditTagSelected = this.onEditTagSelected.bind(this);
        this.onDeleteTagSelected = this.onDeleteTagSelected.bind(this);
        this.onTagSelected = this.onTagSelected.bind(this);
        this.fetchTagSuggestions = this.fetchTagSuggestions.bind(this);
        this.getFullId = this.getFullId.bind(this);
    }

    componentWillMount() {
        this.keyPressTimer = null;
    }

    onTagValueChange(value) {
        clearTimeout(this.keyPressTimer);

        this.props.onTagValueChange(value);

        this.keyPressTimer = setTimeout(this.fetchTagSuggestions, this.props.keyPressTimer)
    }

    fetchTagSuggestions() {
        let value = this.props.taggedWith;

        if (value.length >= 3) {
            this.props.fetchTagSuggestions(value);
        }
    }

    // Gets called if the user hits Return or selects a tag suggestion
    onTagSelected(selectedItem, dataIndex) {
        if (typeof selectedItem === 'string' || selectedItem instanceof String) {
            //Convert String -> Tag
            selectedItem = {
                value: selectedItem
            };
        }
        this.props.onTagSelected(selectedItem);
    }

    onDeleteTagSelected(selectedTag) {
        this.props.onDeleteTagSelected(selectedTag);
    }

    onEditTagSelected(selectedTag) {
        this.props.onEditTagSelected(selectedTag);
    }

    getFullId(id) {
        let parentId = this.props.parentId;
        return parentId ? parentId + id : id;
    }

    render() {
        let classes = this.props.classes;
        let label = this.props.label;
        let searchText = this.props.taggedWith;
        let suggestedTags = this.props.suggestedTags;
        let tags = this.props.tags;
        let dataSourceConfig = {
            text: 'value',
            value: 'value',
        };

        return (
            <div id={this.getFullId(ids.tagSearchField)}>
                <AutoComplete id={this.getFullId(ids.tagSearchFieldInput)}
                              floatingLabelText={label}
                              dataSource={suggestedTags}
                              dataSourceConfig={dataSourceConfig}
                              searchText={searchText}
                              onUpdateInput={this.onTagValueChange}
                              onNewRequest={this.onTagSelected}/>
                <div className={classes.chip}>
                    {tags && tags.map(function (tag, index) {
                        return (
                            <Tag key={index}
                                 tag={tag}
                                 onClick={this.onEditTagSelected}
                                 onRequestDelete={this.onDeleteTagSelected}/>
                        )
                    }, this)}
                </div>
            </div>
        )
    }
}

TagSearchField.propTypes = {
    parentId: PropTypes.string,
    keyPressTimer: PropTypes.number,
    label: PropTypes.any,
    taggedWith: PropTypes.string.isRequired,
    tags: PropTypes.arrayOf(PropTypes.shape({
        value: PropTypes.string.isRequired,
        description: PropTypes.string
    })),
    onTagValueChange: PropTypes.func.isRequired,
    onTagSelected: PropTypes.func.isRequired,
    onDeleteTagSelected: PropTypes.func.isRequired,
    onEditTagSelected: PropTypes.func.isRequired,
    fetchTagSuggestions: PropTypes.func.isRequired,
    suggestedTags: PropTypes.arrayOf(PropTypes.shape({
        value: PropTypes.string.isRequired,
        description: PropTypes.string
    }))
};

TagSearchField.defaultProps = {
    keyPressTimer: 1000
};

export default injectSheet(styles)(TagSearchField);
