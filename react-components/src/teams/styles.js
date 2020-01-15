import { palette } from "@cyverse-de/ui-lib";

const styles = (theme) => ({
    toolbar: {
        backgroundColor: palette.lightGray,
        "& div": {
            marginRight: theme.spacing(2),
        },
        "& button": {
            marginRight: theme.spacing(2),
        },
    },

    dialogToolbar: {
        marginBottom: theme.spacing(2),
    },

    deleteBtn: {
        backgroundColor: theme.palette.error.main,
        "&:hover": {
            backgroundColor: theme.palette.error.dark,
        },
    },

    grow: {
        flexGrow: 1,
    },

    teamFilter: {
        width: theme.spacing(20),
    },

    table: {
        height:
            "calc(100% - " +
            theme.mixins.toolbar["@media (min-width:600px)"].minHeight +
            "px)",
        overflow: "auto",
    },

    requestMessage: {
        marginTop: theme.spacing(3),
    },

    subjectSearch: {
        display: "inline-block",
        width: "100%",
        marginTop: theme.spacing(5),
        marginBottom: theme.spacing(2),
    },

    paper: {
        padding: theme.spacing(3, 2),
    },
});

export default styles;
