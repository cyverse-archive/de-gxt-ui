/**
 * @author sriram
 */
import React, { Component } from "react";
import PropTypes from "prop-types";
import exStyle from "../style";
import intlData from "../messages";
import ids from "../ids";
import Tag from "./Tag";

import { Autocomplete, build, getMessage, withI18N } from "@cyverse-de/ui-lib";

import { withStyles } from "@material-ui/core";

class TagPanel extends Component {
    constructor(props) {
        super(props);
        this.handleTagSelect = this.handleTagSelect.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
    }

    handleTagSelect(chosenTag) {
        this.props.handleTagSelect(chosenTag);
    }

    handleInputChange(inputValue) {
        if (inputValue && inputValue.length > 0) {
            this.props.handleTagSearch(inputValue);
            return inputValue;
        }
    }

    render() {
        const classes = this.props.classes;
        let tagItems = this.props.tags
            ? this.props.tags.map((tag, index) => (
                  <Tag
                      tag={tag}
                      key={tag.id}
                      removeTag={this.props.handleRemoveClick.bind(
                          null,
                          tag,
                          index
                      )}
                      onClick={this.props.onTagClick}
                  />
              ))
            : [];
        let { placeholder } = this.props;
        return (
            <div id={build(this.props.baseID, ids.DETAILS_TAGS_PANEL)}>
                <Autocomplete
                    variant="creatable"
                    labelKey="value"
                    valueKey="id"
                    multi={false}
                    options={this.props.dataSource}
                    onInputChange={this.handleInputChange}
                    onChange={this.handleTagSelect}
                    placeholder={placeholder}
                    promptTextCreator={(tagValue) => {
                        return getMessage("newTagPrompt", {
                            values: { tag: tagValue },
                        });
                    }}
                />
                <div
                    id={build(this.props.baseID, ids.DETAILS_TAGS)}
                    className={classes.tagPanelStyle}
                >
                    {tagItems}
                </div>
            </div>
        );
    }
}

TagPanel.propTypes = {
    baseID: PropTypes.string.isRequired,
    tags: PropTypes.array.isRequired,
    dataSource: PropTypes.array.isRequired,
    handleRemoveClick: PropTypes.func.isRequired,
    onTagClick: PropTypes.func.isRequired,
    handleTagSelect: PropTypes.func.isRequired,
    handleTagSearch: PropTypes.func.isRequired,
    placeholder: PropTypes.any,
};

TagPanel.defaultProps = {
    placeholder: getMessage("searchTags"),
};

export default withStyles(exStyle)(withI18N(TagPanel, intlData));
