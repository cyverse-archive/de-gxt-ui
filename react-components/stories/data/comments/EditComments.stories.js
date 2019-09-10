import React, { Component } from "react";
import EditComments from "./../../../src/data/edit/EditComments";

class EditCommentsTest extends Component {
    render() {
        const presenter = {
            getComments: (input, resolve, reject) => {
                resolve(commentList);
            },

            retractComment: (commentId, resolve, reject) => {
                console.log(commentId);
            },

            createComment: (commentText, resolve, reject) => {
                console.log(commentText);
            },
        };

        const commentList = [
            {
                commenter: "ipctest",
                comment: "This is a test comment",
                id: "2bb4f5c8-729a-11e9-b1c8-5a03816fc427",
                post_time: "1557434209531",
                retracted: true,
            },
            {
                commenter: "a scientist",
                comment:
                    "I am writting this long comment because i am a scientist and that is what i do",
                id: "2bb4f5c8-729a-11e9-b1c8-5a03816fc428",
                post_time: "1557434209531",
                retracted: false,
            },
            {
                commenter: "donald j trump",
                comment: "thank you cyverse, very cool!",
                id: "2bb4f5c8-729a-11e9-b1c8-5a03816fc429",
                post_time: "1557434209531",
                retracted: false,
            },
            {
                commenter: "retracted",
                comment: "doesn't really matter",
                id: "2bb4f5c8-729a-11e9-b1c8-5a03816fc430",
                post_time: "1557434209531",
                retracted: true,
            },
            {
                commenter: "creator",
                comment:
                    "i created this file, please comment and say things. javascript",
                id: "2bb4f5c8-729a-11e9-b1c8-5a03816fc431",
                post_time: "1557434209531",
                retracted: false,
            },
        ];

        const fileName = "someFileName";

        return <EditComments presenter={presenter} fileName={fileName} />;
    }
}

export default EditCommentsTest;
