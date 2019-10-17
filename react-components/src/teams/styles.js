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
});

export default styles;
