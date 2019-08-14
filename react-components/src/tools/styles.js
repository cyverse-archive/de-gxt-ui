import { palette } from "@cyverse-de/ui-lib";

const styles = (theme) => ({
    expansionDetails: {
        display: "inherit",
    },

    paper: {
        padding: "20px",
    },

    addBtn: {
        marginRight: "10px",
    },

    toolbar: {
        backgroundColor: palette.lightGray,
        "& div": {
            marginRight: "10px",
        },
        "& button": {
            marginRight: "8px",
        },
    },

    toolTypeSelector: {
        width: "200px",
    },

    container: {
        height:
            "calc(100% - " +
            theme.mixins.toolbar["@media (min-width:600px)"].minHeight +
            "px)",
        overflow: "auto",
    },

    tablePagination: {
        height: "40",
    },
});

export default styles;
