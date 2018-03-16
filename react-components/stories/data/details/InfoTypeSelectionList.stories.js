import React, {Component} from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {getCyVerseTheme} from "../../../src/lib";
import InfoTypeSelectionList from "../../../src/data/details/InfoTypeSelectionList"

class InfoTypeSelectionListTest extends Component {


    render() {
       const types = [];
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
               <InfoTypeSelectionList infoTypes={types} selectedValue={"csv"}/>
           </MuiThemeProvider>
       )
    }

}

export default InfoTypeSelectionListTest;