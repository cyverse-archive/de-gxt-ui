import React, {Component} from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {getCyVerseTheme} from "../../../src/lib";
import InfoTypeSelectionList from "../../../src/data/details/InfoTypeSelectionList"

class InfoTypeSelectionListTest extends Component {


    render() {
       const types = [];

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
        }
        
       types.push("-");
       types.push("csv") ;
       types.push("tsv");
       types.push("jpeg");
       types.push("png");
       types.push("fasta");
       types.push("fastq");
       types.push("gff");
       types.push("gtf");
       types.push("perl");
       types.push("python");

       return (
           <MuiThemeProvider muiTheme={getCyVerseTheme()}>
               <InfoTypeSelectionList infoTypes={types} selectedValue={"csv"} appearance={dataDetailsAppearance}/>
           </MuiThemeProvider>
       )
    }

}

export default InfoTypeSelectionListTest;
