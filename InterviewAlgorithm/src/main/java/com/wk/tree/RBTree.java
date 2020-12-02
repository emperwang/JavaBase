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
        return (node != null && node.color==BLACK)?true:true;
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
    对y进行    右旋
        px            px
       /             /
      y             x
     / \          /  \
    x  ry        lx   y
   / \               / \
  lx  rx            rx  ry
     */
    public void rightRotate(RBTNode<T> y){
       // 获取y的左孩子
        final RBTNode<T> x = y.left;
        // 将x的右孩子 设置为 y的左孩子
        y.left = x.right;
        // 更新孩子的父节点
        if (x.right != null){
            x.right.parent = y;
        }
        // 更新x的父节点
        x.parent = y.parent;
        if (y.parent == null){
            this.root = x;  // 如果y的父节点为空,说明y是root节点,把x设置为root
        }else {
            if (y.parent.left == y){
                y.parent.left = x;
            }else{
                y.parent.right = x;
            }
        }
        // 更新y的parent节点
        y.parent = x;
        // 将y设置为x的右孩子
        x.right = y;
    }

    /*
    红黑树插入操作
     */
    public void insert(RBTNode<T> node){
        int cmp = 0;
        RBTNode<T> y = null;
        RBTNode<T> root = this.root;
        // 1. 当前树为空树, 直接插入到根节点, 并设置为黑色
        if (root == null) {
            node.color = BLACK;
            this.root = node;
            return;
        }

        // 2. 将红黑树当做一颗二叉查找树, 将节点添加到二叉查找树中
        while (root != null){
            y = root; // 此处的y记录的相当于是 待插入节点的父节点
            cmp = node.key.compareTo(root.key);
            if (cmp < 0){ //小于
                root = root.left;
            }else if (cmp > 0){
                root = root.right;
            } else { // 相等
                break;
            }
        }
        // 2.1 当前插入节点的key已经存在
        // 设置为已存在节点的颜色,并替换节点
        if (cmp == 0){
            node.color = y.color;
            y = node;
            return;
        }
        // 3. 父节点
        node.parent = y;
        if (y != null){
            if (cmp < 0){
                y.left = node;
            }else{
                y.right = node;
            }
        }
        // 2. 设置节点的颜色为红色
        node.color = RED;
        // 3. 将它修正为一颗二叉查找树
        insertFixUp(node);
    }
    /*
    红黑树插入修正函数
    在向红黑树中插入节点之后(失去平衡), 再调用该函数
    目的是将它重新塑造为一颗红黑树
     */
    private void insertFixUp(RBTNode<T> node) {
        RBTNode<T> parent,gparent;
        // 父节点存在,且为红色
        while (((parent = parentOf(node)) != null) && isRed(parent)){
            // 得到祖父节点
            gparent = parentOf(parent);
            // 若"父节点" 是"祖父节点的左孩子"
            if (parent == gparent.left){
                // case1 : 叔叔节点是红色
                RBTNode<T> uncle = gparent.right;
                if (uncle != null && isRed(uncle)){
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }
                // case2: 叔叔节点是黑色, 且当前节点是右孩子
                if (node == parent.right && isBlack(uncle)){
                    // 左旋之后 把node和parent互换,则和 case3是一样的
                    // 即:待插入节点的父节点是红色,且待插入节点是左节点,uncle是黑色
                    leftRotate(parent);
                    // parent 和 node互换
                    RBTNode<T> tmp = parent;
                    parent = node;
                    node = tmp;
                }
                // case3: 叔叔是黑色,且当前节点是左孩子
                if (node == parent.left && isBlack(uncle)){
                    setBlack(parent);
                    setRed(gparent);
                    rightRotate(gparent);
                }
            }else{ // 若 "父节点" 是 "祖父节点" 的右孩子
                RBTNode<T> uncle = parent.left;
                // case 4 叔叔节点为红色
                if (uncle != null && isRed(uncle)){
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }
                // case 5 叔叔节点为黑色,且插入节点为父节点的 右节点
                if (node == parent.right && isBlack(uncle)){
                    // 对parent进行右旋,交换 parent 和 node 得到 case6
                    rightRotate(parent);
                    RBTNode<T> tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // case 6 叔叔节点为黑色,并且插入节点为父节点的左节点
                if (node == parent.left && isBlack(uncle)){
                    setBlack(parent);
                    setRed(gparent);
                    leftRotate(gparent);
                }

            }
        }
        // 根节点设置为黑色
        setBlack(this.root);
    }

    /*
    插入节点到 树中
     */
    public void insert(T key){
        final RBTNode<T> node = new RBTNode<T>(RED,key, null, null, null);
        if (node != null){
            insert(node);
        }
    }
    /*
    在红黑树中进行节点的查找
    递归查找
     */
    public RBTNode<T> search(RBTNode<T> root, T key){
        RBTNode<T> tmp = root;
        while (tmp != null){
            int cmp = key.compareTo(tmp.key);
            if (cmp == 0){
                return tmp;
            }else if(cmp < 0){
                return search(tmp.left,key);
            }else{
                return search(tmp.right,key);
            }
        }
        return null;
    }
    /*
     遍历查找
     */
    public RBTNode<T> searchNormal(RBTNode<T> root, T key){
        RBTNode<T> tmp = root;
        int cmp = 0;
        while (tmp != null){
            cmp = key.compareTo(tmp.key);
            if (cmp == 0){
                return tmp;
            }else if (cmp > 0){
                tmp = tmp.right;
            }else{
                tmp = tmp.left;
            }
        }
        return null;
    }

    /*
        阐述节点
     */
    public RBTNode<T> remove(T key){
        RBTNode<T> node;
        if ((node = search(this.root,key)) != null){
            remove(node);
        }
        return node;
    }

    /*
    删除节点
     */
    public void remove(RBTNode<T> node){
    RBTNode<T> child, parent;
    boolean color;
        // 删除节点的 "左右节点都不为空的情况"
        if ((node.left != null) &&(node.right != null)){
            // "被删节点" 的后继节点(称为 取代节点)
            // 用它来取代 "被删节点" 的位置, 然后再将 "被删节点" 去掉
            RBTNode<T> replace = node;
            // 获取后继节点
            replace = replace.right;
            while (replace.left != null){
                replace = replace.left;
            }
            // "node节点" 不是根节点(只有根节点不存在父节点)
            if (parentOf(node) != null){
                if (parentOf(node).left == null){
                    parentOf(node).left = replace;
                }else{
                    parentOf(node).right = replace;
                }
            }else{
                // "node节点"是根节点 更新根节点
                this.root = replace;
            }
            // child 是取代节点的 右孩子  也是需要 "调整的节点"
            // "取代节点" 肯定不存在左孩子, 因为他是一个后继节点
            child = replace.right;
            parent = parentOf(replace);
            // 保存"取代节点"De颜色
            color = colorOf(replace);

            // "被删除节点" 是 "它的后继节点的父节点"
            if (parent == node){
                parent = replace;
            }else {
                // child not empty
                if (child != null){
                    setParent(child,parent);
                }
                parent.left = child;
                replace.right = node.right;
                setParent(node.right,replace);
            }
            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;
            node.left.parent = replace;
            if (color == BLACK){
                removeFixUp(child, parent);
            }
            node = null;  // help gc
            return;
        } // 存在两个节点

        // 存在一个节点
        if (node.left != null){
            child = node.left;
        }else{
            child = node.right;
        }
        parent = node.parent;
        // 保存 "取代节点" 的颜色
        color = node.color;

        if (child != null){
            child.parent = parent;
        }
        // "node节点" 不是根节点
        if (parent != null){
            if (parent.left == node){
                parent.left = child;
            }else{
                parent.right = child;
            }
        }else{
            this.root = child;
        }
        if (color == BLACK){
            removeFixUp(child, parent);
        }
        node=null;
    }
    /*
     * 红黑树 删除修正函数
     * 在从红黑树中删除插入节点之后(红黑树失去平衡), 再调用该函数
     * 目的是将它重新塑造为一颗红黑树
     * 参数说明:
     * node:  待修正的节点,也即被删除节点的子节点
     * parent: 被删除节点的 父节点
     */
    private void removeFixUp(RBTNode<T> node, RBTNode<T> parent) {
        RBTNode<T> other;
        //
        while ((node == null || isBlack(node)) && (node != root)){
            // node是父节点的 左子节点
            if (parent.left == node){
                other = parent.right;
                // case 1: 替换节点的兄弟节点时红节点
                if (isRed(other)) {
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    // 构造为 case2的情况
                    other = parent.right;
                }
                // case2: 替换节点的兄弟节点时黑色,且兄弟节点的两个孩子都是黑色的
                if ((other.left==null || isBlack(other.left)) &&
                        (other.right == null || isBlack(other.right))){
                    setRed(other);
                    node = parent;
                    parent = parentOf(parent);
                }else{ // 到这里说明 other孩子节点 有一个不为黑色
                    // case 3: 替换节点的兄弟节点时黑色的,且兄弟节点的左孩子是红色,右孩子黑色
                    if (other.right==null || isBlack(other.right)){
                        setRed(other);
                        setBlack(other.left);
                        rightRotate(other);
                        // 此变化 就和 case4 情况一致了
                        other = parent.right;
                    }
                    // case 4: 替换节点的兄弟节点是黑色的,并且兄弟节点的右孩子为红色,左孩子任意颜色
                    //if (other.right!=null && isRed(other.right)){}
                    setColor(other,colorOf(parent));
                    setBlack(parent);
                    setBlack(other.left);
                    leftRotate(parent);
                    // 为了最后修改root节点颜色使用的
                    node = this.root;
                    break;
                }
            }else{ // node是parent的右子节点
                other = parent.left;
                if (isRed(other)){
                    // case1 替换节点的兄弟节点是红色的
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }
                if ((other.left == null || isBlack(other.left)) &&
                        (other.right == null || isBlack(other.right))){
                    // case 2: 替换节点的兄弟节点是黑色,且左右节点同样是黑色

                }else{ // 说明有一个子节点 不为黑色
                    // case 3: 兄弟节点是黑色,且兄弟节点的左孩子是黑色,右孩子为红色
                    if (other.left == null || isBlack(other.left)){
                        setBlack(other.right);
                        setRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }
                    // case 4: 兄弟节点时黑色,且兄弟节点的左孩子是红色,右孩子颜色任意
                    setColor(other,colorOf(parent));
                    setBlack(other.left);
                    setBlack(parent);
                    leftRotate(parent);
                    // 为了设置 root节点的颜色
                    node = this.root;
                    break;
                }
            }
        }
        if (node != null){
            node.color = BLACK;
        }
    }

    /*
    销毁红黑树
     */
    public void destroy(RBTNode<T> tree){
        if (tree == null){
            return;
        }
        if (tree.left != null){
            destroy(tree.left);
        }
        if (tree.right != null){
            destroy(tree.right);
        }
        tree = null;
    }

    public void clear(){
        destroy(this.root);
        this.root = null;
    }
    /*
        打印红黑树
        key -- 节点的值
        direction -- 0  根节点
                    -1  左孩子
                     1  右孩子
     */
    public void print(RBTNode<T> tree,T key, int direction){
        if (tree != null){
            if (direction == 0){
                System.out.printf("%2d(B) is root\n", tree.key);
            }else {
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.key,isRed(tree)?"R":"B",key, direction==1?"right":"left");
            }
        }
        print(tree.left,tree.key,-1);
        print(tree.right, tree.key, 1);
    }

    public void print(){
        if (this.root != null){
            print(this.root,root.key,0);
        }
    }
}















