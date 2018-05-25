/**
 @author sriram
 */
import React, {Component} from "react";
import TagPanel from "../../../src/data/details/TagPanel";


class TagPanelTest extends Component {

    render() {
        const logger = this.props.logger || ((tag) => {
                console.log(tag);
            });

        const ids = {
            DETAILS_TAGS_PANEL: "DetailsTags",
        };

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


        const attachedTags = {
            tags: tags,
        };

        return (
                <TagPanel ids={ids}
                          tags={attachedTags.tags}
                          dataSource={result}
                          handleTagSearch={() => logger}
                          handleRemoveClick={() => logger}
                          onTagClick={logger}
                          handleTagSelect={logger}/>
        );

    }
}

export default TagPanelTest;
