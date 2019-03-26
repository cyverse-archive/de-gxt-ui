/**
 * @author psarando
 */
import React, { Component } from "react";
import Tree, { TreeNode } from "rc-tree";
import "rc-tree/assets/index.css";
import Book from "../../resources/images/book.png";
import Book_Open from "../../resources/images/book_open.png";
import Book_Add from "../../resources/images/book_add.png";


const Icon = (props) => {
    const {expanded, isLeaf, title} = props;
    if (expanded) {
        return <img src={Book_Open} alt={title}/>
    } else {
        if (isLeaf) {
            return <img src={Book} alt={title}/>;
        } else {
            return <img src={Book_Add} alt={title}/>;
        }

    }
};
class CategoryTree extends Component {
    renderHierarchyNode(hierarchyClass, parentKey) {
        let modelKey =
            (parentKey ? parentKey + "/" : "") + hierarchyClass.label;
        return (
            <TreeNode key={modelKey}
                      title={hierarchyClass.label}
                      icon={Icon}
                      isLeaf={hierarchyClass.subclasses ? false : true}>
                {
                    hierarchyClass.subclasses ?
                        hierarchyClass.subclasses.map( (subclass) => this.renderHierarchyNode(subclass, modelKey) )
                        : null
                }
            </TreeNode>
        );
    }
    render() {
        let hierarchies = this.props.hierarchies;

        return (
            <Tree
                id={this.props.id}
                defaultExpandAll={false}
                onExpand={this.onExpand}>
                {hierarchies.map( (hierarchyClass) => this.renderHierarchyNode(hierarchyClass) )}
            </Tree>
        );
    }
}

export default CategoryTree;
