import React, { Component } from "react";


import { NewToolRequestForm } from "../../../src/tools/requests";


class NewToolRequestFormTest extends Component {
    render() {
        let dialogOpen = true;
        const presenter = {
            onToolRequestDialogClose: () => {
                console.log("close dialog");
            }
        };
        return (
            <NewToolRequestForm dialogOpen={dialogOpen} presenter={presenter}/>
        );
    }
}

export default NewToolRequestFormTest;

