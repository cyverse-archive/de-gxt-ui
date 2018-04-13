/**
 * @author sriram
 */
import React, {Component} from "react";
import {FormattedDate, IntlProvider} from "react-intl";
import FlatButton from "material-ui-next/Button";
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
                label={appearance.okLabel()}
                primary={true}
                keyboardFocused={true}
                onClick={this.handleMd5Close}
            />,
        ];

        let sharingVal = null;
        if (isOwner) {
            sharingVal =
                <td id={this.props.DETAILS_SHARE} className={linkClass}
                    onClick={this.handleShareClick}> {(diskResource["share-count"] ?
                    diskResource["share-count"] : appearance.beginSharing())}</td>;
        } else {
            sharingVal =
                <td id={this.props.DETAILS_SHARE} className={valueClass}> appearance.emptyValue() </td>;
        }


        let sendTo = null;

        if (diskResource.infoType) {
            if (drUtil.isTreeInfoType(diskResource.infoType)) {
                sendTo =
                    <td id={this.props.DETAILS_SEND_TO} className={linkClass}
                        onClick={this.handleSendToClick}> {appearance.treeViewer()} </td>;
            } else if (drUtil.isGenomeVizInfoType(diskResource.infoType)) {
                sendTo =
                    <td id={this.props.DETAILS_SEND_TO} className={linkClass}
                        onClick={this.handleSendToClick}> {appearance.coge()} </td>;
            } else if (drUtil.isEnsemblInfoType(diskResource.infoType)) {
                sendTo =
                    <td id={this.props.DETAILS_SEND_TO} className={linkClass}
                        onClick={this.handleSendToClick}> {appearance.ensembl()} </td>;
            } else {
                sendTo = <td id={this.props.DETAILS_SEND_TO} className={valueClass}>
                    {appearance.emptyValue()} </td>;
            }
        } else {
            sendTo =
                <td id={this.props.DETAILS_SEND_TO} className={valueClass}>appearance.emptyValue() </td>;
        }
        let md5Link = null;
        if (diskResource.md5) {
            md5Link = <td id={this.props.DETAILS_MD5} className={linkClass} onClick={this.handleMd5Open}>
                {md5Link}
            </td>;
        } else {
            md5Link = <td>appearance.emptyValue()</td>;
        }

        let details = null;
        if (isFolder) {
            details = <tr>
                <td className={labelClass}> {appearance.filesFoldersLabel()}</td>
                <td> {diskResource["file-count"]} / {diskResource["dir-count"]}</td>
            </tr>;
        } else {
            details = [<tr className={rowClass}>
                <td className={labelClass}>
                    {appearance.md5CheckSum()}
                </td>
                    {md5Link}
            </tr>,
                <tr className={rowClass}>
                    <td className={labelClass}>
                        {appearance.sizeLabel()}
                    </td>
                    <td id={this.props.DETAILS_SIZE} className={valueClass}>
                        {drUtil.formatFileSize(diskResource["file-size"])}
                    </td>
                </tr>,

                <tr className={rowClass}>
                    <td className={labelClass}>
                        {appearance.typeLabel()}
                    </td>
                    <td className={valueClass}>
                        {diskResource["content-type"]}
                    </td>
                </tr>,
                <tr className={rowClass}>
                    <td className={labelClass}>
                        {appearance.infoTypeLabel()}
                    </td>
                    <td className={valueClass}>
                        <InfoTypeSelectionList id={this.props.DETAILS_INFO_TYPE} infoTypes={infoTypes}
                                               selectedValue={diskResource.infoType ? diskResource.infoType : null}
                                               view={this.props.view}
                                               appearance={this.props.appearance}/>
                    </td>
                </tr>,
                <tr className={rowClass}>
                    <td className={labelClass}>
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
                    <tr className={rowClass}>
                        <td className={labelClass}>
                            {appearance.lastModifiedLabel()}
                        </td>
                        <td className={valueClass}>
                            <IntlProvider locale="en">
                                <FormattedDate id={this.props.DETAILS_LAST_MODIFIED}
                                               value={Number(diskResource['date-modified'])}
                                               day="numeric"
                                               month="short"
                                               year="numeric"/>
                            </IntlProvider>
                        </td>
                    </tr>
                    <tr className={rowClass}>
                        <td className={labelClass}>
                            {appearance.createdDateLabel()}
                        </td>
                        <td className={valueClass}>
                            <IntlProvider locale="en">
                                <FormattedDate id={this.props.DETAILS_DATE_SUBMITTED}
                                               value={Number(diskResource['date-created'])}
                                               day="numeric"
                                               month="short"
                                               year="numeric"/>
                            </IntlProvider>
                        </td>
                    </tr>
                    <tr className={rowClass}>
                        <td className={labelClass}>
                            {appearance.permissionsLabel()}
                        </td>
                        <td id={this.props.DETAILS_PERMISSIONS} className={valueClass}>
                            {diskResource.permission}
                        </td>
                    </tr>
                    <tr className={rowClass}>
                        <td className={labelClass}>
                            {appearance.shareLabel()}
                        </td>
                        {sharingVal}
                    </tr>
                    {details}
                    </tbody>
                </table>
                <TagPanel presenter={this.props.presenter} diskResource={this.props.data}
                          detailsTag={this.props.DETAILS_TAGS} appearance={this.props.appearance}/>
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