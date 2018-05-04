/**
 * @author sriram
 */
import React, {Component} from "react";
import Tag from "./Tag";
import AutoComplete from "material-ui/AutoComplete";


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

        const tagListStyle = {fontSize: "small", width: "100%"};

        let tagItems = this.props.tags ? this.props.tags.map((tag) =>
                <Tag tag={tag} key={tag.id} removeTag={this.props.handleRemoveClick} onClick={this.props.onTagClick}
                     appearance={this.props.appearance}/>
            ) : [];

        return (
            <div id={this.props.DETAILS_TAGS}>
                <div>
                    <AutoComplete
                        hintText={this.props.appearance.searchTags()}
                        dataSource={this.props.dataSource}
                        onUpdateInput={this.props.handleTagSearch}
                        dataSourceConfig={dataSourceConfig}
                        listStyle={tagListStyle}
                        onNewRequest={this.handleTagSelect}/>
                </div>
                <div id="panel" className={this.props.appearance.css().tagPanelStyle()}>
                    { tagItems }
                </div>
            </div>
        );
    }

}


export default TagPanel;
