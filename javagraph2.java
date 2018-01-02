import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Random;
//import java.util.StringBuffer;

class javagraph2
{
    public static void main(String args[])
    {
        System.out.println("hello world!");

        //Node<Float> head = GenerateGraph.genBinarySearchTree(10);
        //System.out.println(GraphIterator.isCyclic(head));

        Node<Float> head = GenerateGraph.genCycle(10);
        GraphIterator.printEdgeList(head);
    }
}

class GenerateGraph
{
    public static Node<Float> genFull(int N)
    {
        //Hashset<Node<Float>> hs = new HashSet<Node<Float>>();
        LinkedList<Node<Float>> hs = new LinkedList<Node<Float>>();
        for(int i=0;i<N;i++)
            hs.add(new Node<Float>((float)(i+1)));

        for(Node<Float> n : hs)
            for(Node<Float> n2 : hs)
                if(n != n2)
                    n.addEdge(n2);

        return hs.getFirst();
    }

    public static Node<Float> genBinarySearchTree(int N)
    {
        Node<Float> head = new Node<Float>(Float.MAX_VALUE/2f);

        Random r = new Random();
        for(int i=0;i<N;i++)
            head.addEdgeBinarySearchTreeRules(new Node<Float>(r.nextFloat()));

        return head;
    }

    public static Node<Float> genSimpleTree(int levels)
    {
        Node<Float> n7 = new Node<Float>(7.0f);      
        Node<Float> n6 = new Node<Float>(6.0f);
        Node<Float> n5 = new Node<Float>(5.0f);
        Node<Float> n4 = new Node<Float>(4.0f);
        Node<Float> n3 = new Node<Float>(3.0f);
        n3.addEdge(n7); n3.addEdge(n6);
        Node<Float> n2 = new Node<Float>(2.0f);
        n2.addEdge(n5); n2.addEdge(n4);
        Node<Float> n1 = new Node<Float>(1.0f);
        n1.addEdge(n3); n1.addEdge(n2);
        
        return n1;
    }

    public static Node<Float> genCycle(int N)
    {
        Node<Float> curr = new Node<Float>(0f);
        Node<Float> head = curr;

        for(int i=0;i<(N-1);i++)
        {
            Node<Float> next = new Node<Float>((float)(i+1));
            curr.addEdge(next);
            curr = next;
        }
        curr.addEdge(head);

        return head;
    }

    public static <E> Node<E> genCycle(Set<Node<E>> s)
    {
        if(s == null)
            return null;

        if(s.size() < 2)
            return s.iterator().next();
    
        int counter = 0;
        Node<E> head = null, prev = null;
        for(Node<E> n : s)
        {
            if(counter == 0)
            {
                head = n;
                prev = n;
            }
            else
            {
                prev.addEdge(n);
                prev = n; 
            }
            counter++;
        }
        
        prev.addEdge(head);
        return head;
    }
}

class GraphIterator
{
    public static <E> boolean isCyclic(Node<E> n)
    {
        return isCyclic(n, new HashSet<Node<E>>());
    }
    
    private static <E> boolean isCyclic(Node<E> n, Set<Node<E>> set)
    {
        Set<Node<E>> s = n.getSet();

        if(s.isEmpty())
            return false;

        boolean rval = false;
        for(Node<E> nn : s)
            if(set.contains(nn))
                return true;
            else
            {
                set.add(nn);
                rval = rval | isCyclic(nn, set);
            } 

        return rval;
    }

    public static <E> void printEdgeList(Node<E> n)
    {
        HashSet<Node<E>> s = new HashSet<Node<E>>();
        s.add(n);

        printEdgeList(n, s);
    }

    private static <E> void printEdgeList(Node<E> n, Set<Node<E>> s)
    {
        for(Node<E> nn : n.getSet())
        {
            System.out.print(n.getValue());
            System.out.print(" -> ");
            System.out.print(nn.getValue());
            System.out.println();
        }

        HashSet<Node<E>> candidates = new HashSet<Node<E>>();
        for(Node<E> nn : n.getSet())
        {
            if( (s.contains(nn)) == false )
            {
                candidates.add(nn);
                //s.add(nn);
            }
        }

        for(Node<E> nn : candidates)
        {
            s.add(nn);
            printEdgeList(nn, s);
        }
    }
}

abstract class Pair<E>
{
    protected E n1;
    protected E n2;

    E get1()
    {
        return n1;
    }

    E get2()
    {
        return n2;
    }
}

class UnorderedPair<E> extends Pair<E>
{
    UnorderedPair(E n1, E n2)
    {
        this.n1 = n1;
        this.n2 = n2;
    }

    boolean equals(UnorderedPair<E> p)
    {
        if((this.n1 == p.get1()) && (this.n2 == p.get2())) return true;
        if((this.n1 == p.get2()) && (this.n2 == p.get1())) return true;
        return false;
    }
}

class Node<E>
{
    private E value;
    //HashSet<Node> set = new Hashset<Node>();
    private HashSet<Node<E>> set;

    Node(E value)
    {
        this.value = value;
        this.set = new HashSet<Node<E>>();
    }

    public E getValue()
    {
        return this.value;
    }

    public Set<Node<E>> getSet()
    {
        return this.set;
    }

    public boolean addEdge(Node<E> node)
    {
        return this.set.add(node);
    }

    public boolean addEdgeBinarySearchTreeRules(Node<E> node)
    {
        boolean rc = true;
        if(node.value.hashCode() < this.value.hashCode())
        {
            if(this.lesser() == null)
                rc = this.set.add(node);
            else
                this.lesser().addEdgeBinarySearchTreeRules(node);
        }
        else
        {
            if(this.greaterOrEqual() == null)
                rc = this.set.add(node);
            else
                this.greaterOrEqual().addEdgeBinarySearchTreeRules(node);
        }
        return rc;
    }

    private Node<E> lesser()
    {
        for(Node<E> n : set)
            if(n.getValue().hashCode() < this.value.hashCode())
                return n;

        return null; 
    }

    private Node<E> greaterOrEqual()
    {
        for(Node<E> n : set)
            if(n.getValue().hashCode() >= this.value.hashCode())
                return n;
        return null;
    }

    public boolean equals(Node<E> node)
    {
        return this.value.equals(node.getValue());
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(value + " -> ");
        for(Node<E> n : set)
            sb.append(n.getValue() + " ");

        return sb.toString();
    }
}
