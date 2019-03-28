/**
 * @author psarando, sriram
 */

import React, { Component } from "react";
import Tree, { TreeNode } from "rc-tree";
import "rc-tree/assets/index.css";

import Highlighter from "../../util/Highlighter";

import Book from "../../resources/images/bookIcon.png";
import Book_Open from "../../resources/images/bookIcon-open.png";
import Book_Add from "../../resources/images/bookIcon-inbetween.png";

const Icon = (props) => {
    const { expanded, isLeaf, title } = props;
    const height = 16;
    const width = 16;
    let src = Book;
    if (expanded) {
        src = Book_Open;
    } else {
        if (isLeaf) {
            src = Book;
        } else {
            src = Book_Add;
        }
    }

    return <img src={src} alt={title} height={height} width={width} />;
};
class CategoryTree extends Component {
    renderHierarchyNode(hierarchyClass, parentKey, searchRegexPattern) {
        let modelKey =
            (parentKey ? parentKey + "/" : "") + hierarchyClass.label;
        return (
            <TreeNode
                key={modelKey}
                title={
                    <Highlighter search={searchRegexPattern}>
                        {hierarchyClass.label}
                    </Highlighter>
                }
                icon={Icon}
                isLeaf={hierarchyClass.subclasses ? false : true}
            >
                {hierarchyClass.subclasses
                    ? hierarchyClass.subclasses.map((subclass) =>
                        this.renderHierarchyNode(
                            subclass,
                            modelKey,
                            searchRegexPattern
                        )
                    )
                    : null}
            </TreeNode>
        );
    }
    render() {
        const { hierarchies, searchRegexPattern } = this.props;

        return (
            <Tree
                id={this.props.id}
                defaultExpandAll={false}
            >
                {hierarchies.map((hierarchyClass) =>
                    this.renderHierarchyNode(
                        hierarchyClass,
                        "",
                        searchRegexPattern
                    )
                )}
            </Tree>
        );
    }
}

export default CategoryTree;