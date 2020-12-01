package com.wk.tree;

/**
 * @author: wk
 * @Date: 2020/12/1 16:30
 * @Description
 */
public class RBTree<T extends Comparable<T>> {

    private RBTNode<T> root;    // 根节点
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public class RBTNode<T extends Comparable<T>> {
        boolean color;      // 颜色
        T key;              // 键值
        RBTNode<T> left;    // 左子节点
        RBTNode<T> right;   // 右子节点
        RBTNode<T> parent;  // 父节点

        public RBTNode(boolean color, T key, RBTNode<T> left, RBTNode<T> right, RBTNode<T> parent) {
            this.color = color;
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    public RBTree(){
        root = null;
    }

    private RBTNode<T> parentOf(RBTNode<T> node){
        return node != null ? node.parent:null;
    }

    private boolean colorOf(RBTNode<T> node){
        return node != null?node.color:BLACK;
    }

    private boolean isRed(RBTNode<T> node){
        return (node != null && node.color==RED)?true:false;
    }

    private boolean isBlack(RBTNode<T> node){
        return (node != null && node.color==BLACK)?true:false;
    }

    private void setBlack(RBTNode<T> node){
        if (node != null){
            node.color = BLACK;
        }
    }
    private void setRed(RBTNode<T> node){
        if (node != null){
            node.color = RED;
        }
    }

    private void setParent(RBTNode<T> node,RBTNode<T> parent){
        if (node != null){
            node.parent = parent;
        }
    }

    private void setColor(RBTNode<T> node, boolean color){
        if (node != null){
            node.color = color;
        }
    }
    /*
    * 查找最小点: 返回以node为根节点的红黑树的最小节点
    */
    private RBTNode<T> minmum(RBTNode<T> node){
        if (node == null){
            return null;
        }
        while (node != null){
            node = node.left;
        }
        return node;
    }
    // 返回当前树的最小值
    public T minmum(){
        final RBTNode<T> node = minmum(root);
        if (node != null){
            return node.key;
        }
        return null;
    }
    /*
    * 查找以node为根节点的 最大值
     */
    private RBTNode<T> maximum(RBTNode<T> node){
        if (node == null){
            return node;
        }
        while (node != null){
            node = node.right;
        }
        return node;
    }

    public T maximum(){
        final RBTNode<T> maxmum = maximum(root);
        if (maxmum != null){
            return maxmum.key;
        }
        return null;
    }
    /*
     查找后继节点,即大于node节点的 最小值,即右节点的最小值
     情况一:
        P
      /    \
     K      R
   /
  D

    情况二:
         P
      /    \
     K      R
   /  \
  D    M
     */
    public RBTNode<T> successor(RBTNode<T> node){
        if (node.right != null){
            return minmum(node.right);
        }
        /*
        node没有右孩子,则有一下两种可能
        1) node是左子节点,且没有右子节点,那其后继节点是父节点,如上图情况一中的K,其后继节点是p
        2) node是右子节点,则查找x的最低的父节点,并且该父节点要具有左孩子,该最低的父节点就是node的后继节点
            如上图情况二中的M,其后继节点为P
         */
        RBTNode<T> y = node.parent;
        while ((y != null) && (node == y.right)){
            node = y;
            y = y.parent;
        }
        return y;
    }
    /*
    查找x的前驱节点,即小于x的最大值
         情况一:
        P
      /    \
     K      R
   /
  D

    情况二:
         P
      /    \
     K      R
   /  \
  D    M
     */
    public RBTNode<T> predecessor(RBTNode<T> x){
        if (x.left != null){
            return maximum(x.left);
        }
        // 如果x没有左孩子,则x有以下有两种可能:
        // 1) x 是一个 右孩子,则x的前驱节点为 它的 父节点, 如情况一中的 R
        // 2) x是一个左孩子,则查找 x的最低父节点,并且该节点要具有右孩子,找到的整个最低的父节点
        // 就是x的前驱节点

        RBTNode<T> y = x.parent;
        while ((y != null) &&(x==y.left)){
            x = y;
            y = y.parent;
        }
        return y;
    }

    /*
     对红黑树节点进行 左旋
   对x进行  左旋
     px            px
    /             /
   x             y
  / \          /  \
 lx  y        x   ry
   /  \      / \
  ly   ry   lx  ly
     */
    public void leftRotate(RBTNode<T> x){
        RBTNode<T> y = x.right;
        // 将node的右节点 设置为 y的左节点
        x.right = y.left;
        // 设置y的左节点的父节点

        // 把x的父节点设置到 y的父节点上
        y.parent = x.parent;

        if (x.parent == null){
            this.root = y;
        }else{
            if (x.parent.left == x){
                x.parent.left = y;
            }else{
                x.parent.right = y;
            }
        }
        // 把x设置为y的左节点
        y.left = x;
        // 把x的父节点设置到y
        x.parent = y;

    }
    /*
    对红黑树进行右旋
    对x进行    右旋
        px            px
       /             /
      y             x
     / \          /  \
    x  ry        lx   y
   / \               / \
  lx  rx            rx  ry
     */
    public void rightRotate(RBTNode<T> x){

    }


}















