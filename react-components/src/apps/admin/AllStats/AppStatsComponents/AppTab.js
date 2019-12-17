import React from "react";
import Box from "@material-ui/core/Box";
import SimpleSelect from "./SimpleSelect";

export default function AppCount() {
    return (
        <div className="app-select-bar">
            <div>
                <Box className="app-select-text"> Top Apps in 24 hours: </Box>
            </div>
            <div className="app-count-select">
                <SimpleSelect />
            </div>
        </div>
    );
}
