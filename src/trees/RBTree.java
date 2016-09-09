package trees;

/**
 * Created by Никита on 09.09.2016.
 */

/**
 * RBTree
 *
 * All quoted algorithms are from: Cormen, Thomas H., Charles E. Leiserson, and
 * Robert L. Rivest. Introduction to Algorithms. Cambridge, MA: MIT, 2001.
 * Print.
 *
 *
 * Red–black tree is a kind of self-balancing binary search tree. Each node of
 * the binary tree has an extra bit, and that bit is interpreted as the color
 * (red or black) of the node.
 *
 * Properties of RBTree: 1. Node is either red or black; 2. The root is black;
 * 3. All leaves (nil) are black; 4. If a node is red, then both its children
 * are black; 5. Every path from a given node to any of its descendant NIL nodes
 * contains the same number of black nodes.
 *
 * @param <T1> Key type
 * @param <T2> Value type
 */
public class RBTree<T1 extends Comparable<T1>, T2> {

    private Node<T1, T2> nil = new Node<>(null, null, ColorEnum.BLACK);
    private Node<T1, T2> root = nil;

    protected enum ColorEnum {
        RED,
        BLACK
    };

    static class Node<T1, T2> {

        T1 key;
        T2 value;
        ColorEnum color;
        Node<T1, T2> left, right, parent;

        Node(T1 key, T2 value, ColorEnum color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    /**
     * Left rotation
     *
     * @param x Node which was rotated
     *
     * Time complexity: O(1)
     */
    private void leftRotate(Node<T1, T2> x) {
        Node<T1, T2> y = x.right;
        x.right = y.left;
        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    /**
     * Right rotation
     *
     * @param x node which was rotated
     *
     * Time complexity: O(1)
     */
    private void rightRotate(Node<T1, T2> x) {
        Node<T1, T2> y = x.left;
        x.left = y.right;
        if (y.right != nil) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    /* Inserts a Node with  to a Red-Black tree in a valid way.
     *
     * @param key is key of Node
     * @param value is value of Node

     * Time complexity: O(logn)
     */
    public void insertNodeInRBTree(T1 key, T2 value) {
        Node<T1, T2> x = root, y = nil;
        while (x != nil) {
            y = x;
            if (key.compareTo(x.key) == 0) {
                x.value = value;
                return;
            } else if (key.compareTo(x.key) > 0) {
                x = x.right;
            } else {
                x = x.left;
            }
        }
        Node<T1, T2> z = new Node<>(key, value, ColorEnum.RED);
        z.parent = y;
        if (y == nil) {
            root = z;
        } else if (key.compareTo(y.key) > 0) {
            y.right = z;
        } else {
            y.left = z;
        }
        z.left = nil;
        z.right = nil;
        //RBTree balancing
        insertRBfixUp(z);
    }

    /**
     * Fixes up tree after a insert action. Restored property №4
     *
     * @param z Inserted Node. Time complexity: O(logn)
     *
     */
    private void insertRBfixUp(Node<T1, T2> z) {
        while (z.parent.color == ColorEnum.RED) {
            if (z.parent == z.parent.parent.left) {
                Node<T1, T2> y = z.parent.parent.right;
                if (y.color == ColorEnum.RED) {
                    z.parent.color = ColorEnum.BLACK; //var1
                    y.color = ColorEnum.BLACK; //var1
                    z.parent.parent.color = ColorEnum.RED; //var1
                    z = z.parent.parent; //var1
                } else {
                    if (z == z.parent.right) {
                        z = z.parent; //var2
                        leftRotate(z); //var2
                    }
                    z.parent.color = ColorEnum.BLACK; //var3
                    z.parent.parent.color = ColorEnum.RED; //var3
                    rightRotate(z.parent.parent); //var3
                }
            } else {
                Node<T1, T2> y = z.parent.parent.left;
                if (y.color == ColorEnum.RED) {
                    z.parent.color = ColorEnum.BLACK; //var1
                    y.color = ColorEnum.BLACK; //var1
                    z.parent.parent.color = ColorEnum.RED; //var1
                    z = z.parent.parent; //var1
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = ColorEnum.BLACK;
                    z.parent.parent.color = ColorEnum.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = ColorEnum.BLACK;
    }

    /* Return the Node for the input key
     *
     * Time complexity: O(logn)
     */
    private Node<T1, T2> getNodeByKey(T1 key) {
        Node<T1, T2> x = root;
        while (x != nil) {
            if (key.compareTo(x.key) == 0) {
                return x;
            } else if (key.compareTo(x.key) > 0) {
                x = x.right;
            } else {
                x = x.left;
            }
        }
        return nil;
    }

    /**
     * It function removes a Node u and puts in its place another Node v
     *
     * Time complexity: O(1)
     */
    private void transplantRBTree(Node<T1, T2> u, Node<T1, T2> v) {
        if (u.parent == nil) {
            root = v;
        }
        if (u.parent.left == u) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    /**
     * Deletes the Node by key from the RBTree.
     *
     * Time complexity: O(logn)
     *
     * @param key key of deleted Node
     * @return boolean result
     */
    public boolean removeNodeFromRBTree(T1 key) {
        Node<T1, T2> z = getNodeByKey(key);
        Node<T1, T2> y = z, x = null;
        if (y == nil) {
            return false;
        }
        ColorEnum yOriginalColor = y.color;
        if (z.left == nil) {
            x = z.right;
            transplantRBTree(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplantRBTree(z, z.left);
        } else {
            y = getNodeWithMinimumKey(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplantRBTree(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplantRBTree(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == ColorEnum.BLACK) {
            return deleteRBfixUp(x);
        }
        return true;
    }

    /**
     * Returns Node with minimum key in the specified subRBTree with input root
     *
     * @param x	root subRBTree
     *
     * Time complexity: O(logn)
     */
    private Node<T1, T2> getNodeWithMinimumKey(Node<T1, T2> root) {
        if (root == nil) {
            return null;
        }
        Node<T1, T2> x = root;
        while (x.left != nil) {
            x = x.left;
        }
        return x;
    }

    /**
     * Fixes up tree after a delete action.
     *
     * @param x	Child node of the deleted node's successor.
     *
     * Time complexity: O(logn)
     */
    private boolean deleteRBfixUp(Node<T1, T2> x) {
        while (x != root && x.color == ColorEnum.BLACK) {
            if (x == x.parent.left) {
                Node<T1, T2> w = x.parent.right;
                if (w.color == ColorEnum.RED) {
                    w.color = ColorEnum.BLACK; //var1
                    x.parent.color = ColorEnum.RED; //var1
                    leftRotate(x.parent); //var1
                    w = x.parent.right; //var1
                }
                if (w.left.color == ColorEnum.BLACK && w.right.color == ColorEnum.BLACK) {
                    w.color = ColorEnum.RED; //var2
                    x = x.parent; //var2
                } else {
                    if (w.right.color == ColorEnum.BLACK) {
                        w.left.color = ColorEnum.BLACK;
                        w.color = ColorEnum.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = ColorEnum.BLACK;
                    w.right.color = ColorEnum.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node<T1, T2> w = x.parent.left;
                if (w.color == ColorEnum.RED) {
                    w.color = ColorEnum.BLACK; //var1
                    x.parent.color = ColorEnum.RED; //var1
                    rightRotate(x.parent); //var1
                    w = x.parent.left; //var1
                }
                if (w.right.color == ColorEnum.BLACK && w.left.color == ColorEnum.BLACK) {
                    w.color = ColorEnum.RED; //var2
                    x = x.parent; //var2
                } else {
                    if (w.left.color == ColorEnum.BLACK) {
                        w.right.color = ColorEnum.BLACK;
                        w.color = ColorEnum.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = ColorEnum.BLACK;
                    w.left.color = ColorEnum.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = ColorEnum.BLACK;
        return true;
    }

    /**
     * Print RBTree inOrder Walk
     *
     * Time complexity: O(n)
     */
    public void printRBTreeInorderWalk() {
        printRBTreeInorderWalk(root);
        System.out.println();
    }

    private void printRBTreeInorderWalk(RBTree.Node<T1, T2> x) {
        if (x != nil) {
            printRBTreeInorderWalk(x.left);
            System.out.println("Node: " + x.key + "; " + x.color + "; ParentNode: " + x.parent.key + "; " + x.parent.color + "; RightNode " + x.right.key + "; " + x.right.color + "; LeftNode " + x.left.key + ";  " + x.left.color);
            printRBTreeInorderWalk(x.right);
        }
    }

}

