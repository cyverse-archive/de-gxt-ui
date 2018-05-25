/**
 * @author sriram
 */
import React, {Component} from "react";
import Tag from "./Tag";
import withI18N, {getMessage} from "../../util/I18NWrapper";
import intlData from "../messages";
import build from "../../util/DebugIDUtil";
import ids from "../ids";
import Select from "react-select";
import exStyle from "../style";
import {css} from "aphrodite";
import {withStyles} from "@material-ui/core/styles";

const styles = theme => ({
    // We had to use a lot of global selectors in order to style react-select.
    // We are waiting on https://github.com/JedWatson/react-select/issues/1679
    // to provide a much better implementation.
    // Also, we had to reset the default style injected by the library.
    '@global': {
        '.Select-input input': {
            cursor: 'default',
            display: 'block',
            fontFamily: 'inherit',
            fontSize: 'inherit',
            margin: 0,
            outline: 0,
            width: '100% !important',
        },
        '.Select-menu-outer': {
            backgroundColor: theme.palette.background.paper,
            boxShadow: theme.shadows[2],
            width: '100%',
            zIndex: 2,
            maxHeight: 100,
        },
        '.Select-menu': {
            maxHeight: 100,
            overflowY: 'auto',
        },
    },
});

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
        this.props.handleTagSearch(inputValue);
        return inputValue;
    }

    render() {
        let tagItems = this.props.tags ? this.props.tags.map((tag) =>
                <Tag tag={tag} key={tag.id}
                     removeTag={this.props.handleRemoveClick}
                     onClick={this.props.onTagClick}/>
            ) : [];
        return (
            <div id={build(this.props.baseID, ids.DETAILS_TAGS_PANEL)}>
                <Select.Creatable labelKey="value"
                                  valueKey="id"
                                  multi={false}
                                  options={this.props.dataSource}
                                  onInputChange={this.handleInputChange}
                                  onChange={this.handleTagSelect}
                                  placeholder={getMessage("searchTags")}
                                  promptTextCreator={(tagValue) => {
                                      return getMessage("newTagPrompt") + tagValue;
                                  } }
                />
                <div id={build(this.props.baseID, ids.DETAILS_TAGS)}
                     className={css(exStyle.tagPanelStyle)}>
                    { tagItems }
                </div>
            </div>
        );
    }

}


export default withStyles(styles)(withI18N(TagPanel, intlData));
