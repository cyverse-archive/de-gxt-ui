/**
 * @author sriram
 */
import React, {Component} from "react";
import {FormattedDate, IntlProvider} from "react-intl";
import FlatButton from "material-ui/FlatButton";
import Dialog from "material-ui/Dialog";
import InfoTypeSelectionList from "./InfoTypeSelectionList";
import TagPanel from "./TagPanel";

class BasicDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            md5open: false,
        };
        this.handleShareClick = this.handleShareClick.bind(this);
        this.handleSendToClick = this.handleSendToClick.bind(this);
    }

    handleMd5Open = () => {
        this.setState({md5open: true});
    };

    handleMd5Close = () => {
        this.setState({md5open: false});
    };

    handleShareClick() {
        this.props.view.fireSharingEvent();
    }

    handleSendToClick() {
        this.props.view.onSendToClicked(this.props.data.infoType)
    }

    render() {

        if (!this.props.data) {
            return <div></div>
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
            isFolder = this.props.isFolder;

        const actions = [
            <FlatButton
                label="Ok"
                primary={true}
                keyboardFocused={true}
                onClick={this.handleMd5Close}
            />,
        ];

        let sharingVal = null;
        if (isOwner) {
            sharingVal =
                <td id={this.props.DETAILS_SHARE} class={linkClass}
                    onClick={this.handleShareClick}> {(diskResource["share-count"] ?
                    diskResource["share-count"] : appearance.beginSharing())}</td>;
        } else {
            sharingVal = <td id={this.props.DETAILS_SHARE} class={valueClass}> - </td>;
        }


        let sendTo = null;

        if (diskResource.infoType) {
            if (drUtil.isTreeInfoType(diskResource.infoType)) {
                sendTo =
                    <td id={this.props.DETAILS_SEND_TO} class={linkClass}
                        onClick={this.handleSendToClick}> {appearance.treeViewer()} </td>;
            } else if (drUtil.isGenomeVizInfoType(diskResource.infoType)) {
                sendTo =
                    <td id={this.props.DETAILS_SEND_TO} class={linkClass}
                        onClick={this.handleSendToClick}> {appearance.coge()} </td>;
            } else if (drUtil.isEnsemblInfoType(diskResource.infoType)) {
                sendTo =
                    <td id={this.props.DETAILS_SEND_TO} class={linkClass}
                        onClick={this.handleSendToClick}> {appearance.ensemble()} </td>;
            } else {
                sendTo = <td id={this.props.DETAILS_SEND_TO} class={valueClass}> - </td>;
            }
        } else {
            sendTo = <td id={this.props.DETAILS_SEND_TO} class={valueClass}> - </td>;
        }
        let md5Link = null;
        if (diskResource.md5) {
            md5Link = <td id={this.props.DETAILS_MD5} class={linkClass} onClick={this.handleMd5Open}>
                {md5Link}
            </td>;
        } else {
            md5Link = "-";
        }

        let details = null;
        if (isFolder) {
            details = <tr>
                <td class={labelClass}> {appearance.filesFoldersLabel()}</td>
                <td> {diskResource["file-count"]} / {diskResource["dir-count"]}</td>
            </tr>;
        } else {
            details = [<tr class={rowClass}>
                <td class={labelClass}>
                    {appearance.md5CheckSum()}
                </td>
                <td class={linkClass} onClick={this.handleMd5Open}>
                    {md5Link}
                </td>
            </tr>,

                <tr class={rowClass}>
                    <td class={labelClass}>
                        {appearance.sizeLabel()}
                    </td>
                    <td id={this.props.DETAILS_SIZE} class={valueClass}>
                        {drUtil.formatFileSize(diskResource["file-size"])}
                    </td>
                </tr>,

                <tr class={rowClass}>
                    <td class={labelClass}>
                        {appearance.typeLabel()}
                    </td>
                    <td class={valueClass}>
                        {diskResource["content-type"]}
                    </td>
                </tr>,
                <tr class={rowClass}>
                    <td class={labelClass}>
                        {appearance.infoTypeLabel()}
                    </td>
                    <td class={valueClass}>
                        <InfoTypeSelectionList id={this.props.DETAILS_INFO_TYPE} infoTypes={infoTypes}
                                               selectedValue={diskResource.infoType ? diskResource.infoType : null}
                                               view={this.props.view}/>
                    </td>
                </tr>,
                <tr class={rowClass}>
                    <td class={labelClass}>
                        {appearance.sendToLabel()}
                    </td>
                    {sendTo}
                </tr>,
            ];


        }

        return (
            <div>
                <table>
                    <tbody>
                    <tr class={rowClass}>
                        <td class={labelClass}>
                            {appearance.lastModifiedLabel()}
                        </td>
                        <td class={valueClass}>
                            <IntlProvider locale="en">
                                <FormattedDate id={this.props.DETAILS_LAST_MODIFIED}
                                               value={Number(diskResource['date-modified'])}
                                               day="numeric"
                                               month="short"
                                               year="numeric"/>
                            </IntlProvider>
                        </td>
                    </tr>
                    <tr class={rowClass}>
                        <td class={labelClass}>
                            {appearance.createdDateLabel()}
                        </td>
                        <td class={valueClass}>
                            <IntlProvider locale="en">
                                <FormattedDate id={this.props.DETAILS_DATE_SUBMITTED}
                                               value={Number(diskResource['date-created'])}
                                               day="numeric"
                                               month="short"
                                               year="numeric"/>
                            </IntlProvider>
                        </td>
                    </tr>
                    <tr class={rowClass}>
                        <td class={labelClass}>
                            {appearance.permissionsLabel()}
                        </td>
                        <td id={this.props.DETAILS_PERMISSIONS} class={valueClass}>
                            {diskResource.permission}
                        </td>
                    </tr>
                    <tr class={rowClass}>
                        <td class={labelClass}>
                            {appearance.shareLabel()}
                        </td>
                        {sharingVal}
                    </tr>
                    {details}
                    </tbody>
                </table>
                <TagPanel presenter={this.props.presenter} diskResource={this.props.data} detailsTag={this.props.DETAILS_TAGS}/>
                <Dialog
                    title={appearance.md5CheckSum()}
                    actions={actions}
                    modal={false}
                    open={this.state.md5open}
                    onRequestClose={this.handleMd5Close}>{diskResource.md5} </Dialog>
            </div>
        );
    }
}

export default BasicDetails;