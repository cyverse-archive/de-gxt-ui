/**
 @author sriram
 */
import React, {Component} from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {getCyVerseTheme} from "../../../src/lib";
import Tag from "../../../src/data/details/Tag";

class TagTest extends Component {

    render() {
        const logger = this.props.logger || ((tag) => {
                console.log(tag);
            });

        const tag = {id: 1, value: 'testag'};

        const dataDetailsAppearance = {
            css: () => (
                {
                    row: () => "",
                    label: () => "",
                    value: () => "",
                    hyperlink: () => "",
                    tagPanelStyle: () => "",
                    tagDivStyle: () => "",
                    tagStyle: () => "",
                    tagRemoveStyle: () => "",
                }
            ),
            lastModifiedLabel: () => "Last Modified:",
            createdDateLabel: () => "Created Date:",
            permissionsLabel: () => "Permissions:",
            md5CheckSum: () => "Md5:",
            sizeLabel: () => "Size:",
            shareLabel: () => "Share",
            beginSharing: () => "Begin Sharing",
            infoTypeLabel: () => "InfoType",
            typeLabel: () => "Type",
            sendToLabel: () => "Send To",
            searchTags: () => "Search Tags...",
            removeTag: () => "Remove Tag",
            selectInfoType: () => "Select Info Type...",
            tagSearchListStyle: () => {},
            infoTypeSelectStyle: () => {},
            emptyValue: () => "-",
        };

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <Tag tag={tag} removeTag={logger} onClick={logger} appearance={dataDetailsAppearance}/>
            </MuiThemeProvider>
        );
    }
}

export default TagTest;
