/**
 * @author sriram
 */
import React, {Component} from "react";
import moment from "moment";
import Button from "material-ui-next/Button";
import Dialog, {DialogActions, DialogContent, DialogContentText, DialogTitle} from "material-ui-next/Dialog";
import InfoTypeSelectionList from "./InfoTypeSelectionList";
import TagPanel from "./TagPanel";
import {addLocaleData, ReactIntlLocaleData, FormattedMessage, IntlProvider} from "react-intl";
import intlData from "../messages";
import {CircularProgress} from "material-ui-next/Progress";

function SendTo(props) {
    let displayText = <FormattedMessage id="emptyValue"/>;
    let className = props.appearance.css().value();
    let onClick = null;
    let infoType = props.infoType;
    let drUtil = props.drUtil;
    let hyperLinkClassName = props.appearance.css().hyperlink();

    if (infoType) {
        if (drUtil.isTreeInfoType(infoType)) {
            displayText = <FormattedMessage id="treeViewer"/>;
            className = hyperLinkClassName;
            onClick = props.handleSendToClick;
        } else if (drUtil.isGenomeVizInfoType(infoType)) {
            displayText = <FormattedMessage id="coge"/>;
            className = hyperLinkClassName;
            onClick = props.handleSendToClick;
        } else if (drUtil.isEnsemblInfoType(infoType)) {
            displayText = <FormattedMessage id="genomeBrowser"/>;
            className = hyperLinkClassName;
            onClick = props.handleSendToClick;
        }
    }
    return (<td id={props.id} className={className} onClick={onClick}>{displayText}</td>);
}

function Md5(props) {
    if (props.md5) {
        return (<td id={props.id} className={props.appearance.css().hyperlink()}
                    onClick={props.onClick}>
            {<FormattedMessage id="view"/>}
        </td>);
    } else {
        return <td id={props.id} className={props.appearance.css().value()}>{ <FormattedMessage id="folders"/>}</td>;
    }
}

function ManageSharing(props) {
    if (props.isOwner) {
        return (
            <td id={props.id} className={props.appearance.css().hyperlink()}
                onClick={props.onClick}>
                {(props.shareCount ? props.shareCount : <FormattedMessage id="noSharing"/>)}
            </td>);
    } else {
        return <td id={props.id} className={props.appearance.css().value()}>{<FormattedMessage id="emptyValue"/>}</td>;
    }
}

class BasicDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            md5open: false,
            infoType: (props.data) ? props.data.infoType : "",
            dataSource: [],
            tags: {dataId: "", values: []},
            searchText: null,
            loading: false,
        };
        this.handleShareClick = this.handleShareClick.bind(this);
        this.handleSendToClick = this.handleSendToClick.bind(this);
        this.onInfoTypeSelect = this.onInfoTypeSelect.bind(this);
        this.fetchTags = this.fetchTags.bind(this);
        this.handleTagSearch = this.handleTagSearch.bind(this);
        this.handleTagSelect = this.handleTagSelect.bind(this);
        this.doRemove = this.doRemove.bind(this);
        this.handleRemoveClick = this.handleRemoveClick.bind(this);
        this.handleTagClick = this.handleTagClick.bind(this);
        this.doRemove = this.doRemove.bind(this);
        this.findTag = this.findTag.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        this.setState({infoType: (nextProps.data) ? nextProps.data.infoType : ""});
        if (nextProps.data) {
            this.fetchTags(nextProps.data.id);
        }
    }

    handleMd5Open = () => {
        this.setState({md5open: true});
    };

    handleMd5Close = () => {
        this.setState({md5open: false});
    };

    handleShareClick() {
        this.props.presenter.onSharingClicked();
    }

    handleSendToClick() {
        this.props.presenter.onSendToClicked(this.state.infoType)
    }

    onInfoTypeSelect(infoType) {
        this.props.presenter.onSetInfoType(infoType);
    }

    fetchTags(id) {
        if (id && id !== this.state.tags.dataId && !this.state.loading) {
            this.setState({loading: true});
            this.props.presenter.fetchTagsForResource(id, (tags) => {
                this.setState({loading: false, tags: {dataId: id, values: tags}});
            });
        }
    }

    handleTagSearch(value) {
        if (value && value.length >= 3) {
            this.props.presenter.searchTags(value, (tags) => {
                if (tags) {
                    this.setState({dataSource: tags});
                }
            });
            this.setState({searchText: value});
        }
    }

    handleTagSelect(chosenTag) {
        if (!this.state.loading) {
            this.setState({loading: true});
            if (chosenTag.id) {
                this.props.presenter.attachTag(chosenTag.id, chosenTag.value, this.props.data.id, (tags) => {
                    this.setState({tags: {dataId: this.props.data.id, values: tags}, loading: false});
                });
            } else {
                this.props.presenter.createTag(chosenTag, this.props.data.id, (tags) => {
                    this.setState({tags: {dataId: this.props.data.id, values: tags}, loading: false});
                });
            }
        }
    }

    doRemove(index) {
        if (!this.state.loading) {
            this.setState({loading: true});
            this.props.presenter.detachTag(this.state.tags.values[index].id, this.state.tags.values[index].value, this.props.data.id, (tags) => {
                this.setState({tags: {dataId: this.props.data.id, values: tags}, loading: false});
            });
        }
    }

    handleTagClick(tag) {
        let index = this.findTag(tag);
        if (index !== -1) {
            this.props.presenter.onTagSelection(this.state.tags.values[index].id, this.state.tags.values[index].value);
        }
    }

    findTag(tag) {
        if (this.state.tags && tag) {
            return this.state.tags.values.indexOf(tag)
        }
        return -1;
    }

    handleRemoveClick(tag) {
        let index = this.findTag(tag);
        if (index !== -1) {
            this.doRemove(index);
        }
    }

    render() {
        if (!this.props.data) {
            return (
                <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
                    <div>{<FormattedMessage id="noDetails"/>}</div>
                </IntlProvider>
            )
        }

        let drUtil = this.props.drUtil,
            diskResource = this.props.data,
            appearance = this.props.appearance,
            rowClass = appearance.css().row(),
            labelClass = appearance.css().label(),
            valueClass = appearance.css().value(),
            linkClass = appearance.css().hyperlink(),
            ownPermission = this.props.owner,
            infoTypes = this.props.infoTypes,
            isOwner = ownPermission === diskResource.permission,
            isFolder = this.props.isFolder,
            infoType = this.state.infoType;

        let details = null;

        if (isFolder) {
            details = <tr>
                <td className={labelClass}> {<FormattedMessage id="filesFolders"/>}</td>
                <td> {diskResource["file-count"] ? diskResource["file-count"] : 0}
                    / {diskResource["dir-count"] ? diskResource["dir-count"] : 0}</td>
            </tr>;
        } else {
            details = [
                <tr className={rowClass}>
                <td className={labelClass}>
                    {<FormattedMessage id="md5CheckSum"/>}
                </td>
                    <Md5 id={this.props.DETAILS_MD5} appearance={appearance} md5={diskResource.md5}
                         onClick={this.handleMd5Open} {...intlData}/>
                </tr>,
                <tr className={rowClass}>
                    <td className={labelClass}>
                        {<FormattedMessage id="size"/>}
                    </td>
                    <td id={this.props.DETAILS_SIZE} className={valueClass}>
                        {drUtil.formatFileSize(diskResource["file-size"])}
                    </td>
                </tr>,
                <tr className={rowClass}>
                    <td className={labelClass}>
                        {<FormattedMessage id="type"/>}
                    </td>
                    <td className={valueClass}>
                        {diskResource["content-type"]}
                    </td>
                </tr>,
                <tr className={rowClass}>
                    <td className={labelClass}>
                        {<FormattedMessage id="infoType"/>}
                    </td>
                    <td className={valueClass}>
                        <InfoTypeSelectionList id={this.props.DETAILS_INFO_TYPE} infoTypes={infoTypes}
                                               selectedValue={infoType ? infoType : null}
                                               view={this.props.view}
                                               appearance={this.props.appearance}
                                               onInfoTypeSelect={this.onInfoTypeSelect}
                                               {...intlData}/>
                    </td>
                </tr>,
                <tr className={rowClass}>
                    <td className={labelClass}>
                        {<FormattedMessage id="sendTo"/>}
                    </td>
                    <SendTo id={this.props.DETAILS_SEND_TO} appearance={appearance} infoType={infoType}
                            handleSendToClick={this.handleSendToClick} drUtil={drUtil} {...intlData}/>
                </tr>,
            ];
        }
        return (
            <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
                <div>
                    {this.state.loading &&
                        <CircularProgress size={30}/>
                    }
                    <table>
                        <tbody>
                        <tr className={rowClass}>
                            <td className={labelClass}>
                                {<FormattedMessage id="lastModified"/>}
                            </td>
                            <td className={valueClass}>
                                {diskResource['date-modified'] ? moment(Number(diskResource['date-modified']), "x").format("YYYY-MM-DD") :
                                    <FormattedMessage id="emptyValue"/>}
                            </td>
                        </tr>
                        <tr className={rowClass}>
                            <td className={labelClass}>
                                {<FormattedMessage id="createdDate"/>}
                            </td>
                            <td className={valueClass}>
                                {diskResource['date-modified'] ? moment(Number(diskResource['date-created']), "x").format("YYYY-MM-DD") :
                                    <FormattedMessage id="emptyValue"/>}
                            </td>
                        </tr>
                        <tr className={rowClass}>
                            <td className={labelClass}>
                                {<FormattedMessage id="permissions"/>}
                            </td>
                            <td id={this.props.DETAILS_PERMISSIONS} className={valueClass}>
                                {diskResource.permission}
                            </td>
                        </tr>
                        <tr className={rowClass}>
                            <td className={labelClass}>
                                {<FormattedMessage id="share"/>}
                            </td>
                            <ManageSharing id={this.props.DETAILS_SHARE} isOwner={isOwner}
                                           appearance={appearance}
                                           shareCount={diskResource["share-count"]}
                                           onClick={this.handleShareClick} {...intlData}/>
                        </tr>
                        {details}
                        </tbody>
                    </table>
                    <TagPanel
                        detailsTag={this.props.DETAILS_TAGS} appearance={this.props.appearance}
                        handleTagSearch={this.handleTagSearch} handleRemoveClick={this.handleRemoveClick}
                        tags={this.state.tags.values} dataSource={this.state.dataSource}
                        onTagClick={this.handleTagClick}
                        handleTagSelect={this.handleTagSelect} {...intlData}/>
                    <Dialog
                        open={this.state.md5open}
                        onClose={this.handleMd5Close}
                        {...intlData}>
                        <DialogTitle>{<FormattedMessage id="md5CheckSum"/>}</DialogTitle>
                        <DialogContent>
                            <DialogContentText>
                                {diskResource.md5}
                            </DialogContentText>
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={this.handleMd5Close} autoFocus>
                                {<FormattedMessage id="okLabel"/>}
                            </Button>
                        </DialogActions>

                    </Dialog>
                </div>
            </IntlProvider>
        );
    }
}

export default BasicDetails;