import React, {Component} from "react";
import InfoTypeSelectionList from "../../../src/data/details/InfoTypeSelectionList";

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
        const logger = this.props.logger || ((tag) => {
                console.log(tag);
            });

       return (
               <InfoTypeSelectionList infoTypes={types} selectedValue={"csv"} onInfoTypeSelect={logger}/>
       )
    }

}

export default InfoTypeSelectionListTest;
