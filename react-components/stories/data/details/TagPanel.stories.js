/**
 @author sriram
 */
import React, {Component} from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {getCyVerseTheme} from "../../../src/lib";
import TagPanel from "../../../src/data/details/TagPanel";


class TagPanelTest extends Component {

    render() {
        const logger = this.props.logger || ((tag) => {
                console.log(tag);
            });

        const ids = {
            DETAILS_TAGS: "DetailsTags",
        };

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

        const result = [];
        result.push({value: 'test', id: 11});
        result.push({value: 'testtest', id: 12});
        result.push({value: 'testtesttest', id: 13});
        result.push({value: 'foo', id: 14});
        result.push({value: 'foofoo', id: 15});
        result.push({value: 'foofoofoo', id: 16});
        result.push({value: 'bar', id: 17});
        result.push({value: 'barbar', id: 18});
        result.push({value: 'barbarbar', id: 19});


        const presenter = {
            searchTags: (value, resultCallback) => {
                resultCallback(result);
            },

            fetchTagsForResource: (id, fetchCallback) => {
                fetchCallback(result.slice(2, 6));
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
                <TagPanel ids={ids}
                          tags={tags}
                          dataSource={result}
                          appearance={dataDetailsAppearance}
                          handleTagSearch={() => logger}
                          handleRemoveClick={() => logger}
                          onTagClick={logger}
                          handleTagSelect={logger}/>
            </MuiThemeProvider>
        );

    }
}

export default TagPanelTest;
