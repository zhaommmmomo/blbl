import java.util.*;

/**
 * @author zmm
 * @date 2022/4/7 20:11
 */
class SkipList {

    private int level = 1;
    private Node header;
    private double p = 0.25;
    private Random rand;
    private int maxLevel;

    public SkipList(int maxLevel) {
        this.maxLevel = maxLevel;
        header = new Node(maxLevel);
        rand = new Random();
    }

    public boolean search(int val) {
        Node p = header;
        for (int l = level - 1; l >= 0; l--) {
            while (p.levels[l] != null && p.levels[l].val < val) {
                p = p.levels[l];
            }
            if (p.levels[l] != null && p.levels[l].val == val) {
                return true;
            }
        }
        return false;
    }

    public void insert(int val) {
        Node p = header;
        Node[] preNode = new Node[level];
        for (int l = level - 1; l >= 0; l--) {
            while (p.levels[l] != null && p.levels[l].val < val) {
                p = p.levels[l];
            }
            preNode[l] = p;
        }
        int l = randomLevel();
        Node node = new Node(val, l);
        for (int i = 0; i < l; i++) {
            if (i >= level) {
                header.levels[i] = node;
            } else {
                node.levels[i] = preNode[i].levels[i];
                preNode[i].levels[i] = node;
            }
        }
        level = Math.max(level, l);
    }

    public boolean delete(int val) {
        Node p = header;
        Node[] preNode = new Node[level];
        for (int l = level - 1; l >= 0; l--) {
            while (p.levels[l] != null && p.levels[l].val < val) {
                p = p.levels[l];
            }
            preNode[l] = p;
        }
        p = p.levels[0];
        if (p != null && p.val == val) {
            for (int i = 0; i < p.levels.length; i++) {
                preNode[i].levels[i] = p.levels[i];
            }
            return true;
        }
        return false;
    }

    private int randomLevel() {
        int l = 1;
        while (rand.nextInt(0xffff) + 1 < p * 0xffff && l < maxLevel) {
            l++;
        }
        return l;
    }

    class Node {
        int val;
        Node[] levels;

        public Node() {}

        public Node(int level) {
            this.levels = new Node[level];
        }

        public Node(int val, int level) {
            this.val = val;
            this.levels = new Node[level];
        }
    }
}