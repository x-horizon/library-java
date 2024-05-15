// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.lang.collection;

import cn.srd.library.java.contract.constant.text.SuppressWarningConstant;
import cn.srd.library.java.tool.lang.object.Nil;

import java.util.List;

/**
 * the balanced multi-way search tree defined, BTree is a tree with infinite leaf nodes
 *
 * @author wjm
 * @since 2023-06-26 15:56
 */
public interface BalancedMultiWaySearchTree {

    /**
     * get children
     *
     * @param <T> the children element type
     * @return children
     */
    <T extends BalancedMultiWaySearchTree> List<T> getChildren();

    /**
     * <pre>
     * find all path from the BTree.
     *
     * example:
     *
     * the BTree defined is:
     *       1
     *     / | \
     *    2  3  4
     *   / \
     *  5   6
     *
     * all the BTree paths are:
     * [
     *   [1, 2, 5],
     *   [1, 2, 6],
     *   [1, 3],
     *   [1, 4]
     * ]
     * </pre>
     *
     * @param root the root node
     * @param <T>  the children element type
     * @return all the BTree paths
     */
    default <T extends BalancedMultiWaySearchTree> List<List<T>> getAllPaths(T root) {
        List<List<T>> paths = Collections.newArrayList();
        if (Nil.isNull(root)) {
            return paths;
        }
        dfsSupportGetAllPaths(root, Collections.newArrayList(), paths);
        return paths;
    }

    /**
     * <pre>
     * find all node and child from the BTree.
     *
     * example:
     *
     * the BTree defined is:
     *             1
     *           / | \
     *          2  3  4
     *         / \
     *        5   6
     *       / \
     *      7   8
     *     /   /
     *    9   10
     *   /
     *  11
     *
     * all node and child are:
     * [
     *   [1, 2],
     *   [2, 5],
     *   [5, 7],
     *   [7, 9],
     *   [9, 11],
     *   [5, 8],
     *   [8, 10],
     *   [2, 6],
     *   [1, 3],
     *   [1, 4],
     * ]
     * </pre>
     *
     * @param root the root node
     * @param <T>  the children element type
     * @return all node and child
     */
    default <T extends BalancedMultiWaySearchTree> List<List<T>> getAllNodeAndChild(T root) {
        List<List<T>> result = Collections.newArrayList();
        dfsSupportGetAllNodeAndChild(root, result);
        return result;
    }

    /**
     * <pre>
     * flatten the BTree.
     *
     * example:
     *
     * the BTree defined is:
     *       1
     *     / | \
     *    2  3  4
     *   / \
     *  5   6
     *
     * after flatten:
     * [1, 2, 3, 4, 5, 6]
     * </pre>
     *
     * @param root the root node
     * @param <T>  the children element type
     * @return after flatten result
     */
    default <T extends BalancedMultiWaySearchTree> List<T> flatten(T root) {
        List<T> output = Collections.newArrayList();
        dfsSupportFlatten(root, output);
        return output;
    }

    /**
     * find BTree all paths support: use depth-first traversal algorithm, the time complexity is O(mn)
     *
     * @param node  the tree node
     * @param path  one path
     * @param paths all paths
     * @param <T>   the children element type
     */
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    private <T extends BalancedMultiWaySearchTree> void dfsSupportGetAllPaths(T node, List<T> path, List<List<T>> paths) {
        path.add(node);
        if (Nil.isEmpty(node.getChildren())) {
            paths.add(Collections.ofArrayList(path));
        } else {
            node.getChildren().forEach(child -> dfsSupportGetAllPaths((T) child, path, paths));
        }
        path.remove(path.size() - 1);
    }

    /**
     * find BTree all node and child support: use depth-first traversal algorithm, the time complexity is O(mn)
     *
     * @param node   the tree node
     * @param output all node and child
     * @param <T>    the children element type
     */
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    private <T extends BalancedMultiWaySearchTree> void dfsSupportGetAllNodeAndChild(T node, List<List<T>> output) {
        if (Nil.isNull(node)) {
            return;
        }
        node.getChildren().forEach(child -> {
            List<T> nodeAndChild = Collections.newArrayList();
            nodeAndChild.add(node);
            nodeAndChild.add((T) child);
            output.add(nodeAndChild);
            dfsSupportGetAllNodeAndChild((T) child, output);
        });
    }

    /**
     * flatten BTree support: use depth-first traversal algorithm, the time complexity is O(mn)
     *
     * @param node   the tree node
     * @param output after flatten
     * @param <T>    the children element type
     */
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    private <T extends BalancedMultiWaySearchTree> void dfsSupportFlatten(T node, List<T> output) {
        if (Nil.isNull(node)) {
            return;
        }
        output.add(node);
        node.getChildren().forEach(child -> dfsSupportFlatten((T) child, output));
    }

}