-   [Commands](#commands)
    -   [`npm install`](#npm-install)
    -   [`npm run storybook`](#npm-run-storybook)
    -   [`npm test`](#npm-test)
-   [ui vs ui-lib](#ui-vs-ui-lib)
-   [Standards and Conventions](#standards-and-conventions)
    -   [Component Design and Icons - Material-UI](#component-design-and-icons---material-ui)
    -   [Storybook Development](#storybook-development)
    -   [Tests](#tests)
    -   [Internationalization (i18n)](#internationalization-i18n)
    -   [Static IDs](#static-ids)
    -   [Styling and CSS](#styling-and-css)
        -   [Colors and the CyVerse palette](#colors-and-the-cyverse-palette)
        -   [Theming](#theming)

# Discovery Environment React Components

## Commands

In the `/react-components` directory you can run:

### `npm install`

Installs the project's dependencies.

### `npm run storybook`

Launches the [Storybook](https://storybook.js.org/) application for fast development and debugging.
Generally, for any component in ui-lib there should be a story. Stories are also used to run the tests.

### `npm test`

Launches the test runner in the interactive watch mode. All tests should pass before creating a merge request.
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

---

## ui vs ui-lib

There are 2 locations where DE components live.

1. Here in the ui repo [/react-components/src](/react-components/src) - these components are typically specific to the DE.
2. In the [ui-lib](https://github.com/cyverse-de/ui-lib) repo - these components and helper functions are typically general enough to be used in other CyVerse products.

All of the below standards will apply to both repositories.

---

## Standards and Conventions

### Component Design and Icons - Material-UI

Use [material-ui](https://material-ui.com) components before attempting to design your own. Material-ui, or MUI, has most of the components we'll need, like [buttons](https://material-ui.com/components/buttons/), [dialogs](https://material-ui.com/components/dialogs/), and [grid layouts](https://material-ui.com/components/grid/). If a component is not available directly from material-ui, you can typically find a repo on github that has created a component that follows the [material-ui design spec](https://material.io/design/) for you.

Use [material icons](https://material.io/resources/icons) before asking our graphic designer to create something custom.

### Storybook Development

It is strongly recommended that all development for components is done utilizing [Storybook](https://storybook.js.org/). It allows you to view your collection of components in a browser and "develop UI components in isolation without worrying about app specific dependencies and requirements". Every time you save your component's story file, Storybook will automatically refresh the browser with your changes so you can visually inspect and iterate on it.

Add your component's story within the `/stories` folder [here](/react-components/stories). Then, import your story in the index file [here](/react-components/stories/index.stories.js). Run the storybook command listed above.

### Tests

Create a test for your components located in the `/src/__tests__` folder [here](/react-components/src/__tests__). Note that all the tests rely on rendering the story for each component.

### Internationalization (i18n)

All text displayed to the user should be internationalized. We currently use [react-intl](https://github.com/formatjs/react-intl) for internationalization of our app. There are [helper functions](https://github.com/cyverse-de/ui-lib/blob/master/src/util/I18NWrapper.js) in the [ui-lib](https://github.com/cyverse-de/ui-lib) repo that should be used.

Generally, you'll need to:

1. Create a `messages` file similar to the following with all the text your component(s) will need. Generally, the first 3 lines don't change - `locales` and `messages` must be present. Beyond that, you have your text in key-value pairs.

```
export default {
    locales: "en-US",
    messages: {
        header: "Welcome",
        namedHeader: "Welcome back {name}",
    }
}
```

2. Export your component while wrapping it with the `withI18N` [higher-order component](https://reactjs.org/docs/higher-order-components.html) from the [helper function](https://github.com/cyverse-de/ui-lib/blob/master/src/util/I18NWrapper.js) file in the `ui-lib` repo and pass in the `messages` file you created. This basically links them together so `react-intl` knows where to look up text.

```
import myMessagesFile from "./messages";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";

function MyComponent(props) {
  return (
    <span>Welcome</span>
    <span>Welcome back {props.name}</span>
    )
}

export default withI18N(MyComponent, myMessagesFile);
```

3. Update your component to fetch the internationalized text with the corresponding key you created. The [getMessage(key)](https://github.com/cyverse-de/ui-lib/blob/78880901c263c14ea697a5abd9b607fbd776ec4b/src/util/I18NWrapper.js#L29-L34) helper function covers roughly 80% of the use cases

```
import myMessagesFile from "./messages";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";

function MyComponent(props) {
  return (
    <span>{getMessage("header")}</span>
    <span>{getMessage("namedHeader", values: {name: props.name})}</span>
    )
}

export default withI18N(MyComponent, myMessagesFile);
```

### Static IDs

For robust QA testing, add static IDs to all of your components.

All ids should be in a separate `ids` file. Example file [here](/react-components/src/analysis/ids.js).

In order to ensure uniqueness of those IDs, use a pattern of `parentID.childId`. For example, a dialog may have an ID of `toolsDialog`. Every component within that dialog will then have an ID in the format `toolsDialog.myID`. If those components then have sub-components, they will take the format `toolsDialog.myID.theirID`, and so on.

To help with this, there's a `build` helper function in the `ui-lib` repo [here](https://github.com/cyverse-de/ui-lib/blob/master/src/util/DebugIDUtil.js). `build(id1, id2, id3)` will yield an id of `id1.id2.id3`.

### Styling and CSS

If your component(s) require a lot of CSS, create a separate `styles` file to keep things minimal. Example file [here](/react-components/src/analysis/style.js).

For utilizing styles, we're transitioning to using `makeStyles` from `@material-ui/core` as a hook (Historically, we have many components using `withStyles` from `@material-ui/core/styles` as a higher-order component). Here's a small example of using `makeStyles`:

```
import { makeStyles } from "@material-ui/core";
import myStylesFile from "./styles";

const useStyles = makeStyles(myStylesFile);

function myComponent(props) {
  const classes = useStyles();

  return (
    <div className={classes.myDivCSS}/>
    )
}
```

#### Colors and the CyVerse palette

Try to stick with using colors from the [CyVerse palette](https://wiki.cyverse.org/wiki/display/COM/CyVerse+Color+Palette) (in order to access that link, you must be signed in and be a CyVerse staff member).

We have defined the palette for code use in the `ui-lib` repo [here](https://github.com/cyverse-de/ui-lib/blob/master/src/util/CyVersePalette.js). There's an example of using the palette [here](/react-components/src/analysis/style.js).

#### Theming

Be aware the DE has a [customized material-ui theme](/react-components/src/lib.js) to update material-ui components to use the CyVerse palette.

The default values can be found [here](https://material-ui.com/customization/default-theme/).
