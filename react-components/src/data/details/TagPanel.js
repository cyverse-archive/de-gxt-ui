/**
 * @author sriram
 */
import React, {Component} from "react";
import Tag from "./Tag";
import AutoComplete from "material-ui/AutoComplete";


class TagPanel extends Component {

    constructor(props) {
        super(props);
        this.handleRemoveClick = this.handleRemoveClick.bind(this);
        this.handleTagSearch = this.handleTagSearch.bind(this);
        this.handleTagSelect = this.handleTagSelect.bind(this);
        this.findTag = this.findTag.bind(this);
        this.fetchTags = this.fetchTags.bind(this);
        this.state = {
            dataSource: [],
            tags: [],
            searchText: null,
        }
    }

    componentDidMount() {
        this.fetchTags();
    }

    fetchTags() {
        this.props.presenter.fetchTagsForResource(this.props.diskResource.id, (tags) => {
            this.setState({tags: tags});
        });
    }

    findTag(tag) {
        if (this.state.tags && tag) {
            return this.state.tags.indexOf(tag)
        }
        return -1;
    }

    handleRemoveClick(tag) {
        let index = this.findTag(tag);
        if (index !== -1) {
            this.props.presenter.detachTag(this.state.tags[index].id, this.state.tags[index].value, this.props.diskResource.id, (tags) => {
                this.setState({tags: tags});
            });
        }
    }


    handleTagSearch(value) {
        if (value && value.length >= 3) {
            this.props.presenter.searchTags(value, (tags) => {
                if (tags && tags.length > 0) {
                    this.setState({dataSource: tags});
                }
            });
            this.setState({searchText: value});
        }
    }

    handleTagSelect(chosenTag, index) {
        if (chosenTag.id) {
            this.props.presenter.attachTag(chosenTag.id, chosenTag.value, this.props.diskResource.id, (tags) => {
                this.setState({tags: tags});
            });
        } else {
            this.props.presenter.createTag(chosenTag, this.props.diskResource.id, (tags) => {
                this.setState({tags: tags});
            });
        }
    }


    render() {
        const dataSourceConfig = {
            text: 'value',
            value: 'id',
        };

        let tagItems = this.state.tags ? this.state.tags.map((tag) =>
                <Tag tag={tag} key={tag.id} removeTag={this.handleRemoveClick}
                     presenter={this.props.presenter} appearance={this.props.appearance}/>
            ) : [];

        return (
            <div id={this.props.DETAILS_TAGS}>
                <div>
                    <AutoComplete
                        hintText={this.props.appearance.searchTags()}
                        dataSource={this.state.dataSource}
                        onUpdateInput={this.handleTagSearch}
                        dataSourceConfig={dataSourceConfig}
                        listStyle={this.props.appearance.tagSearchListStyle()}
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
