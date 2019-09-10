import { palette } from "@cyverse-de/ui-lib";

export default {
    main: {
        margin: 0,
        maxWidth: "lg",
        fullWidth: true,
    },

    root: {
        width: 700,
    },

    dropDownLabel: {
        float: "left",
        marginLeft: 10,
    },

    dropDownDots: {
        float: "right",
    },

    addCommentButton: {
        float: "right",
        width: 40,
        marginBottom: 8,
        marginLeft: 323,
        color: palette.blue,
    },

    addCommentTextField: {
        position: "relative",
        width: "93%",
        marginLeft: 11,
    },

    dContent: {
        width: 320,
    },

    comment: {},

    commentText: {
        width: 250,
    },

    deleteIcon: {
        color: "#a30000",
        backgroundColor: "#ffffff",
        margin: 5,
        "&:hover": {
            backgroundColor: "#d1cbcb",
        },
    },
};
