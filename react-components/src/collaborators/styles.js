const style = (theme) => ({
    searchField: {
        padding: "2px",
        "&:hover": {
            background: theme.palette.action.hover,
            cursor: "pointer",
        },
    },

    searchIcon: {
        margin: "0px 2px 0px 2px",
    },
});

export default style;
