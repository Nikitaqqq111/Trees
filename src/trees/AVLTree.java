package trees;

/**
 * Created by Никита on 09.09.2016.
 */

import static java.lang.Math.abs;

public class AVLTree<T1 extends Comparable<T1>, T2> {

    Node<T1, T2> nil = new Node<>(null, null);
    Node<T1, T2> root = nil;

    static class Node<T1, T2> {

        T1 key;
        T2 value;
        Node<T1, T2> parent, left, right;
        int balanceFactor;

        private Node(T1 key, T2 value) {
            this.key = key;
            this.value = value;
            this.balanceFactor = 0;
        }

    }

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

    public boolean insertNodeInAVLTree(T1 key, T2 value) {
        Node<T1, T2> x = root, y = nil;
        while (x != nil) {
            y = x;
            if (key.compareTo(x.key) == 0) {
                x.value = value;
                return true;
            } else if (key.compareTo(x.key) > 0) {
                x = x.right;
            } else {
                x = x.left;
            }
        }
        Node<T1, T2> z = new Node<>(key, value);
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
        return balanceForInsertingNode(z);
    }

    private boolean rotating(Node<T1, T2> z) {
        Node<T1, T2> y = z.parent;
        if (y.balanceFactor > 0) {
            z = y.right;
        } else {
            z = y.left;
        }
        if (y.balanceFactor > 0) {
            if (z.balanceFactor > -1) {
                if (z.balanceFactor == 1) {
                    y.balanceFactor = 0;
                    z.balanceFactor = 0;
                } else {
                    y.balanceFactor = 1;
                    z.balanceFactor = -1;
                }
                leftRotate(y);
            } else {
                if (z.left.balanceFactor == -1) {
                    y.balanceFactor = 0;
                    z.balanceFactor = 1;
                    z.balanceFactor = 0;
                } else if (z.left.balanceFactor == 1) {
                    y.balanceFactor = -1;
                    z.balanceFactor = 0;
                    z.balanceFactor = 0;
                } else {
                    y.balanceFactor = 0;
                    z.balanceFactor = 0;
                    z.balanceFactor = 0;
                }
                rightRotate(z);
                leftRotate(y);
            }
        } else {
            if (z.balanceFactor < 1) {
                if (z.balanceFactor == -1) {
                    y.balanceFactor = 0;
                    z.balanceFactor = 0;
                } else {
                    y.balanceFactor = -1;
                    z.balanceFactor = 1;
                }
                rightRotate(y);
            } else {
                if (z.right.balanceFactor == 1) {
                    y.balanceFactor = 0;
                    z.balanceFactor = -1;
                    z.balanceFactor = 0;
                } else if (z.right.balanceFactor == -1) {
                    y.balanceFactor = 1;
                    z.balanceFactor = 0;
                    z.balanceFactor = 0;
                } else {
                    y.balanceFactor = 0;
                    z.balanceFactor = 0;
                    z.balanceFactor = 0;
                }
                leftRotate(z);
                rightRotate(y);
            }
        }
        z = y.parent;
        return true;
    }

    private boolean balanceForInsertingNode(Node<T1, T2> z) {
        if (z == root) {
            return true;
        }
        Node<T1, T2> y;
        while (z != root) {
            y = z.parent;
            if (z.key.compareTo(y.key) > 0) {
                ++y.balanceFactor;
            } else {
                --y.balanceFactor;
            }
            if (y.balanceFactor == 0) {
                return true;
            }
            if (abs(y.balanceFactor) > 1) {
                rotating(z);
                if (z.balanceFactor == 0) {
                    return true;
                }
            } else {
                z = z.parent;
            }
        }
        return true;
    }

    public boolean deleteNodeFromAVLTree(T1 key) {
        Node<T1, T2> z = getNodeByKey(key);
        Node<T1, T2> y = z, x = null;
        if (y == nil) {
            return false;
        }
        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = getNodeWithMinimumKey(z.right);
            x = y.right;
            //y.right.parent = x;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            //x.balanceFactor;
        }
        return balanceForDeletingNode(x);
    }

    private boolean balanceForDeletingNode(Node<T1, T2> z) {
        Node<T1, T2> y;
        System.out.println(z.key);
        while (z != root) {
            y = z.parent;
            if (z.key.compareTo(y.key) > 0) {
                --y.balanceFactor;
            } else {
                ++y.balanceFactor;
            }
            if (abs(z.balanceFactor) == 1) {
                return true;
            }
            if (abs(y.balanceFactor) > 1) {
                rotating(z);
                if (abs(y.parent.balanceFactor) == 1) {
                    return true;
                }
            } else {
                z = z.parent;
            }
        }
        return true;
    }

    public void transplant(Node<T1, T2> u, Node<T1, T2> v) {
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

    public boolean isBalanced(Node<T1, T2> x) {
        if (abs(x.balanceFactor) > 1) {
            return false;
        } else if (x == nil) {
            return true;
        } else {
            return isBalanced(x.left) && isBalanced(x.right);
        }
    }

    public void printAVLTreeInorderWalk() {
        printAVLTreeInorderWalk(root);
        System.out.println();
    }

    private void printAVLTreeInorderWalk(Node<T1, T2> x) {
        if (x != nil) {
            printAVLTreeInorderWalk(x.left);
            System.out.println("Node: " + x.key + ";   BalanceFactor: " + x.balanceFactor + ";   Parent: " + x.parent.key);
            printAVLTreeInorderWalk(x.right);
        }
    }

}

