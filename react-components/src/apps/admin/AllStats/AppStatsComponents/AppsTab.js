import React from "react";
import Box from "@material-ui/core/Box";
import SimpleSelect from "./SimpleSelect";
import AppsTable from "./AppsTable";
import NumberTextfield from "./NumberTextfield";

export default function AppTab() {
    return (
        <div>
            <div className="app-select-bar">
                <div>
                    <Box className="app-select-text">
                        {" "}
                        Top Apps in 24 hours:{" "}
                    </Box>
                </div>
                <div className="app-count-select">
                    <NumberTextfield />
                    {/*<SimpleSelect />*/}
                </div>
            </div>
            <AppsTable />
        </div>
    );
}
