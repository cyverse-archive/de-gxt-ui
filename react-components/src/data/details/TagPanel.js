/**
 * @author sriram
 */
import React, {Component} from "react";
import Tag from "./Tag";
import AutoComplete from "material-ui/AutoComplete";
import styles from "../style";
import {css} from "aphrodite";
import withI18N, {getMessage} from "../../util/I18NWrapper";
import intlData from "../messages";
import build from "../../util/DebugIDUtil";
import ids from "../ids";


class TagPanel extends Component {

    constructor(props) {
        super(props);
        this.handleTagSelect = this.handleTagSelect.bind(this);
    }

    handleTagSelect(chosenTag, index) {
        this.props.handleTagSelect(chosenTag);
    }

    render() {
        const dataSourceConfig = {
            text: 'value',
            value: 'id',
        };

        let tagItems = this.props.tags ? this.props.tags.map((tag) =>
                <Tag tag={tag} key={tag.id}
                     removeTag={this.props.handleRemoveClick}
                     onClick={this.props.onTagClick}/>
            ) : [];

        return (
            <div id={build(this.props.baseID, ids.DETAILS_TAGS_PANEL)}>
                    <div>
                        <AutoComplete
                            hintText={getMessage("searchTags")}
                            dataSource={this.props.dataSource}
                            onUpdateInput={this.props.handleTagSearch}
                            dataSourceConfig={dataSourceConfig}
                            listStyle={css(styles.tagListStyle)}
                            onNewRequest={this.handleTagSelect}/>
                    </div>
                <div id={build(this.props.baseID, ids.DETAILS_TAGS)}
                     className={css(styles.tagPanelStyle)}>
                        { tagItems }
                    </div>
                </div>
        );
    }

}


export default withI18N(TagPanel, intlData);
