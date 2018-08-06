import cyverseMini from "../resources/images/whitelogo.png";
import desktopBcg from "../resources/images/background.png";
import cyverseHeader from "../resources/images/cyverse_icon_white.png";
import color from "../util/CyVersePalette";

export default {
    '@global body': {
        position: 'fixed',
        top: 0,
        bottom: 0,
        right: 0,
        left: 0,
    },
    desktop: {
        backgroundImage: `url(${desktopBcg})`,
        position: 'absolute',
        width: '100%!important',
        zIndex: 0,
        top: 51,
        backgroundPosition: 'top center',
        bottom: 30,
        backgroundSize: 'contain',
        backgroundColor: '#8b9bb3', // not in cyverse palette
        backgroundRepeat: 'no-repeat',
    },
    header: {
        backgroundColor: color.blue,
        borderBottom: '5px solid #57cbea',
        position: 'absolute',
        height: '51px',
        zIndex: 1,
        width: '100%',
        top: 0,
        left: 0,
        right: 0,
        boxShadow: '-1px 0 4px 0 #777',
    },

    menuIcon: {
        height: '24px',
        width: '24px',
        display: 'inline-block',
        opacity: '.8',
        right: '30px',
        backgroundSize: 'contain',
        position: 'relative',
        marginBottom: '8px',
        marginRight: '8px',
        cursor: 'pointer',
        '&:hover': {
            boxShadow: '0px 2px 3px #ccc',
        },
        borderRadius: '10px',
    },

    logoContainer: {
        color: '#eee',
        fontSize: '1.1em',
        fontStyle: 'normal',
        display: 'inline-block',
        marginTop: '10px',
        width: '100%',
        paddingLeft: '35px',
        verticalAlign: 'top',
    },


    logo: {
        backgroundImage: `url(${cyverseHeader})`,
        display: 'inline-block',
        height: '28px',
        width: '300px',
        marginTop: '12px',
        marginLeft: '12px',
        backgroundSize: 30,
        backgroundRepeat: 'no-repeat',
    },

    userMenuContainer: {
        position: 'absolute',
        bottom: 0,
        right: '15px',
    },

    data: {
        height: '72px',
        width: '72px',
        marginTop: '40px',
        backgroundSize: 'contain',
        position: 'fixed',
        left: '50px',
        top: '50px',
        cursor: 'pointer',
        '&:hover': {
            boxShadow: '3px 3px #0971ab',
        },
        borderRadius: '10px',

    },

    apps: {
        height: '72px',
        width: '72px',
        marginTop: '20px',
        backgroundSize: 'contain',
        position: 'fixed',
        left: '50px',
        top: '150px',
        cursor: 'pointer',
        '&:hover': {
            boxShadow: '3px 3px #0971ab',
        },
        borderRadius: '10px',
    },

    analyses: {
        height: '72px',
        width: '72px',
        marginTop: '20px',
        backgroundSize: 'contain',
        position: 'fixed',
        left: '50px',
        top: '230px',
        cursor: 'pointer',
        '&:hover': {
            boxShadow: '3px 3px #0971ab',
        },
        borderRadius: '10px',
    },

    notificationMenuPosition: {
        top: '60px',
        right: '150px',
        position: 'absolute',
    },

    userMenuPosition: {
        top: '70px',
        right: '120px',
        position: 'absolute',
    },

    helpMenuPosition: {
        top: '70px',
        right: '50px',
        position: 'absolute',
    },

    unSeenCount: {
        backgroundColor: color.orange,
        fontSize: '.625em',
        color: 'white',
        height: 8,
        verticalAlign: 'super',
        visibility: 'visible',
        lineHeight: '0.8em',
        float: 'left',
        padding: 2,
    },
    taskbarButton: {
        backgroundImage: `url(${cyverseMini})`,
        backgroundSize: 20,
        backgroundColor: '#0971ab',
        backgroundPosition: '5px 5px',
        backgroundRepeat: 'no-repeat',
        border: '1px #e2e2e2',
        position: 'relative',
        paddingLeft: '25px',
        color: '#fff',
        marginRight: 5,
        minHeight: 20,
        width: 150,
        fontSize: 10,
        textTransform: 'none',
        borderRadius: 4,
        '&:hover': {
           backgroundColor: color.blue,
           boxShadow: 'none',
        },
    },

    taskbarButtonMinimized: {
        backgroundImage: `url(${cyverseMini})`,
        backgroundSize: 20,
        backgroundColor: color.darkGray,
        backgroundPosition: '5px 5px',
        backgroundRepeat: 'no-repeat',
        position: 'relative',
        paddingLeft: '25px',
        color: '#fff',
        marginRight: 5,
        minHeight: 20,
        width: 150,
        fontSize: 10,
        borderRadius: 4,
        textTransform: 'none',
        '&:hover': {
            backgroundColor: color.blue,
        },
    },

    taskbar: {
        position: 'absolute',
        width: '100%',
        bottom: 0,
        minHeight: 30,
        backgroundColor: color.lightGray,
    },

    errorRetryButton: {
        backgroundColor: color.orange,
        position: 'relative',
        left: 50,
        '&:hover': {
            backgroundColor: color.blue,
        },
    },

    notificationError: {
        marginBottom: 10,
        fontSize: 10
    },

    loadingStyle: {
        outline: 'none',
        position: 'absolute',
        left: 50,
        color: color.orange,
    },

    notificationMenu: {
        width: '100%',
        zIndex: 888888,
        maxHeight: 600
    }

};
