/**
 @author sriram
 */
import React, {Component} from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {getCyVerseTheme} from "../../../src/lib";
import BasicDetails from "../../../src/data/details/BasicDetails";

class BasicDetailsTest extends Component {
    render() {

        const dataDetailsAppearance = {
            css: () => (
                {
                    row: () => "",
                    label: () => "",
                    value: () => "",
                    hyperlink: () => ""
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
            sendToLabel: () => "Send To"
        }

        const ids = {
            DETAILS_LAST_MODIFIED: "detailsLastModified",
            DETAILS_DATE_SUBMITTED: "detailsDateSubmited",
            DETAILS_PERMISSIONS: "detailsPermission",
            DETAILS_SHARE: "DetailsShare",
            DETAILS_SIZE: "DetailsSize",
            DETAILS_TYPE: "DetailsType",
            DETAILS_INFO_TYPE: "DetailsInfoType",
            DETAILS_MD5: "DetailsMd5",
            DETAILS_SEND_TO: " DetailsSendTo",
            DETAILS_TAGS: "DetailsTags",

        }

        const logger = this.props.logger || ((size) => {
                console.log(size);
            });

        const drUtil = {
            formatFileSize: logger,
            isTreeInfoType: () => console.log("tree type"),
            isGenomeVizInfoType: () => console.log("Genome viz type"),
            isEnsemblInfoType: () => console.log("Ensemble type")
        };

        const view = {
            fireSharingEvent: console.log("sharing event")
        };

        const types = [];
        types.push("-")
        types.push("csv");
        types.push("tsv");
        types.push("jpeg");
        types.push("png");
        types.push("fasta");
        types.push("fastq");
        types.push("gff");
        types.push("gtf");
        types.push("perl");
        types.push("python");

        const tags = [];
        tags.push({value: 'csv', id: 1});
        tags.push({value: 'tsv', id: 2});
        tags.push({value: 'perl', id: 3});
        tags.push({value: 'python', id: 4});
        tags.push({value: 'java', id: 5});
        tags.push({value: 'markdown', id: 6});
        tags.push({value: 'fasta', id: 7});
        tags.push({value: 'fastq', id: 8});
        tags.push({value: 'bed', id: 9});
        tags.push({value: 'js', id: 10});

        const presenter = {
            searchTags: (value, resultCallback) => {
                resultCallback(tags);
            },
            fetchTagsForResource: (id, fetchCallback) => {
                fetchCallback(tags.slice(2, 6));
            },
        }

        const
            data = {
                "infoType": "perl",
                "path": "/iplant/home/sriram/README.md",
                "share-count": 0,
                "date-created": 1432927857000,
                "md5": "26329f777474f1a588f89dbb097de6dd",
                "permission": "own",
                "date-modified": 1432927857000,
                "type": "file",
                "file-size": 1943,
                "label": "README.md",
                "id": "3ada61ea-0639-11e5-9df8-3c4a92e4a804",
                "content-type": "text/x-web-markdown"
            };

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <BasicDetails data={data} drUtil={drUtil} appearance={dataDetailsAppearance} view={view}
                              owner="own" infoTypes={types} selectedValue={data.infoType} presenter={presenter} ids={ids} />
            </MuiThemeProvider>
        );
    }
}

export default BasicDetailsTest;