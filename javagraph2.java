import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
//import java.util.StringBuffer;

class javagraph2
{
    public static void main(String args[])
    {
        System.out.println("hello world!");

        Node<Float> head1 = GenerateGraph.genFull(10);
        //System.out.println(head);
        System.out.println(GraphIterator.isCyclic(head1, new HashSet<Node>()));

        Node<Float> head2 = GenerateGraph.genSimpleTree(3);
        System.out.println(GraphIterator.isCyclic(head2, new HashSet<Node>()));
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

    //public static void 
}

class GraphIterator
{
    public static boolean isCyclic(Node n, Set<Node> set)
    {
        Set<Node> s = n.getSet();

        if(s.isEmpty())
            return false;

        boolean rval = false;
        for(Node nn : s)
            if(set.contains(nn))
                return true;
            else
            {
                set.add(nn);
                rval = rval | isCyclic(nn, set);
            } 

        return rval;
    }
}

class Node<E>
{
    private E value;
    //HashSet<Node> set = new Hashset<Node>();
    private HashSet<Node> set;

    Node(E value)
    {
        this.value = value;
        this.set = new HashSet<Node>();
    }

    public E getValue()
    {
        return this.value;
    }

    public Set<Node> getSet()
    {
        return this.set;
    }

    public boolean addEdge(Node node)
    {
        return this.set.add(node);
    }

    public boolean equals(Node<E> node)
    {
        return this.value.equals(node.getValue());
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(value + " -> ");
        for(Node n : set)
            sb.append(n.getValue() + " ");

        return sb.toString();
    }
}
