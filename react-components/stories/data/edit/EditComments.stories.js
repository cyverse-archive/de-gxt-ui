import React, {Component} from "react";
import EditComments from "./../../../src/data/edit/EditComments";

class EditCommentsTest extends Component {
    render(){
        const presenter = {
            getComments: (input, resolve, reject) => {
                resolve(commentList);
            }
        };

        const commentList = [
            {
                owner: "ipctest",
                message: "This is a test comment",
                id: "1",
                date: "today",
                retracted: true,

            },
            {
                owner: "a scientist",
                message: "I am writting this long comment because i am a scientist and that is what i do",
                id: "2",
                date: "04/1/2019",

            },
            {
                owner: "donald j trump",
                message: "thank you cyverse, very cool!",
                id: "3",
                date: "MM/DD/YYYY"
            },
            {
                owner: "retracted commentet",
                message: "doesn't really matter",
                id:"4",
                date:"12/12/2012",
                retracted: true
            },
            {
                owner: "creator",
                message: "i created this file, please comment and say things. javascript",
                id: "5",
                date:"the/dawn/oftime",

            },
        ];

        const fileName = "someFileName";

        return <EditComments presenter={presenter}
                             fileName={fileName}
                            />
    }
}

export default EditCommentsTest