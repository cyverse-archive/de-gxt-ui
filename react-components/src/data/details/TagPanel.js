/**
 * @author sriram
 */
import React, {Component} from "react";
import {FormattedDate, IntlProvider} from "react-intl";
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
        if (index != -1) {
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
        }
        const panelStyle = {
            padding: '2px',
            width: '95%',
            height: '100px',
            margin: '2px',
            overflow: 'scroll',
        };

        const listStyle = {
            fontSize: '10px',
            width: '100%',
        };

        let tagItems = this.state.tags ? this.state.tags.map((tag) =>
                <Tag tag={tag} removeTag={this.handleRemoveClick} presenter={this.props.presenter}/>
            ) : [];

        return (
            <div id={this.props.DETAILS_TAGS}>
                <div>
                    <AutoComplete
                        hintText="Search Tags"
                        dataSource={this.state.dataSource}
                        onUpdateInput={this.handleTagSearch}
                        dataSourceConfig={dataSourceConfig}
                        listStyle={listStyle}
                        onNewRequest={this.handleTagSelect}/>
                </div>
                <div id="panel" style={panelStyle}>
                    { tagItems }
                </div>
            </div>
        );
    }

}


export default TagPanel;