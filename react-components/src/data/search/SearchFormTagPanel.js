import EditTagDialog from "./EditTagDialog";
import messages from "./messages";
import TagPanel from "../details/TagPanel";
import withI18N from "../../util/I18NWrapper";

import PropTypes from "prop-types";
import React, { Component } from "react";

/**
 * @author aramsey
 * A wrapper around the TagPanel component that handles editing tags, specifically designed
 * for the SearchForm with redux
 */
class SearchFormTagPanel extends Component {
    constructor(props) {
        super(props);

        this.state = {
            openEditTagDlg: false,
            selectedTag: null,
            dataSource: []
        };

        //Tags
        this.onTagClicked = this.onTagClicked.bind(this);
        this.fetchTagSuggestions = this.fetchTagSuggestions.bind(this);
        this.onTagSelected = this.onTagSelected.bind(this);
        this.appendTag = this.appendTag.bind(this);
        this.removeTag = this.removeTag.bind(this);
        this.saveTagDescription = this.saveTagDescription.bind(this);
        this.closeEditTagDlg = this.closeEditTagDlg.bind(this);
    }

    onTagClicked(tag) {
        this.setState({
            openEditTagDlg: true,
            selectedTag: tag
        })
    }

    saveTagDescription(tag) {
        this.props.presenter.onEditTagSelected(tag);
        this.setState({
            openEditTagDlg: false,
            selectedTag: null,
            dataSource: []
        });
    }

    closeEditTagDlg() {
        this.setState({
            openEditTagDlg: false,
            selectedTag: null
        })
    }

    fetchTagSuggestions(search) {
        this.props.presenter.fetchTagSuggestions(search, (data) => this.setState({dataSource: data}));
    }

    onTagSelected(tag) {
        let {
            presenter
        } = this.props;

        if (tag.id !== tag.value) {
            this.appendTag(tag);
        } else {
            presenter.onAddTagSelected(tag.value, (newTag) => this.appendTag(newTag));
        }
    }

    appendTag(tag) {
        let { array, tagQuery } = this.props;
        array.insert(tagQuery.name, 0, tag);
    }

    removeTag(tag, index) {
        let { array, tagQuery } = this.props;
        array.remove(tagQuery.name, index)
    }

    render() {
        let {
            tagQuery,
            placeholder,
            parentId
        } = this.props;

        let {
            selectedTag,
            openEditTagDlg,
            dataSource
        } = this.state;

        return (
            <div>
                <TagPanel baseID={parentId}
                          placeholder={placeholder}
                          onTagClick={this.onTagClicked}
                          handleTagSearch={this.fetchTagSuggestions}
                          dataSource={dataSource}
                          handleRemoveClick={this.removeTag}
                          handleTagSelect={this.onTagSelected}
                          tags={tagQuery.value ? tagQuery.value : []}/>
                <EditTagDialog open={openEditTagDlg}
                               tag={selectedTag}
                               handleSave={this.saveTagDescription}
                               handleClose={this.closeEditTagDlg}/>
            </div>
        )
    }
}

SearchFormTagPanel.propTypes = {
    parentId: PropTypes.string.isRequired,
    placeholder: PropTypes.any.isRequired,
    presenter: PropTypes.shape({
        onAddTagSelected: PropTypes.func.isRequired,
        fetchTagSuggestions: PropTypes.func.isRequired,
        onEditTagSelected: PropTypes.func.isRequired
    }),
    array: PropTypes.object.isRequired,
    tagQuery: PropTypes.object.isRequired
};

export default withI18N(SearchFormTagPanel, messages);