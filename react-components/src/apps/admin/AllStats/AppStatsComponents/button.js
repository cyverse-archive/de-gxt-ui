import React from "react";
import Button from "@material-ui/core/Button";
import { withStyles } from "@material-ui/core/styles";
import { purple } from "@material-ui/core/colors";

const ColorButton = withStyles((theme) => ({
    root: {
        color: theme.palette.getContrastText(purple[500]),
        backgroundColor: purple,
        "&:hover": {
            backgroundColor: purple,
        },
    },
}))(Button);

export default ColorButton;
