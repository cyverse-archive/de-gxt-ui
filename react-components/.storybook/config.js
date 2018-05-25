import {configure, addDecorator} from "@storybook/react";
import {withConsole} from "@storybook/addon-console";
import {getDefaultTheme, MuiThemeProvider} from "../src/lib";

//redirect console error / logs / warns to action logger
addDecorator((storyFn, context) => withConsole()(storyFn)(context));

//wap with mui theme
const themeDecorator = (storyFn) => (
    <MuiThemeProvider theme={getDefaultTheme()}>
        {storyFn()}
    </MuiThemeProvider>

);
addDecorator(themeDecorator);


// automatically import all files ending in *.stories.js
const req = require.context('../stories', true, /.stories.js$/);
function loadStories() {
  req.keys().forEach((filename) => req(filename));
}

configure(loadStories, module);
