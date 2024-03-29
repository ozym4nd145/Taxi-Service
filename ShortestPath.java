import java.util.*;
public class ShortestPath
{
    Graph graph;
    Integer source;
    Node[] nodes;
    public ShortestPath(Graph graph, Integer source)
    {
        this.graph = graph;
        this.source = source;
        dijkstra();
    }
    //
    // private void print(TreeSet<Node> ha)
    // {
    //     System.out.println("Printing Unvisited Set");
    //     Iterator<Node> itr = ha.iterator();
    //     while(itr.hasNext())
    //     {
    //         System.out.println("\t"+itr.next());
    //     }
    // }

    private void dijkstra()
    {
        nodes = new Node[graph.adjList.size()];

        for(int i=0;i<graph.adjList.size();i++)
        {
            nodes[i] = new Node(i,null,Integer.MAX_VALUE);
        }
        nodes[source].distance = 0;

        Integer[] parent = new Integer[graph.adjList.size()];
        TreeSet<Node> unvisited = new TreeSet<Node>(new NodeCompare());

        for(int i=0;i<nodes.length;i++)
        {
            unvisited.add(nodes[i]);
        }

        while(unvisited.size() > 0)
        {
            // print(unvisited);
            Node min = unvisited.first();
            // System.out.println("Vertex "+min.vertex+", Parent - "+min.parent+", Distance - "+min.distance+", Size - "+unvisited.size());
            unvisited.remove(min);
            min.visited = true;
            Iterator<Edge> itr = graph.adjList.get(min.vertex).iterator();
            while(itr.hasNext())
            {
                Edge edge = itr.next();
                Integer dist = min.distance + edge.weight;
                Node neighbour = nodes[edge.end];
                // System.out.println(neighbour.vertex+", "+neighbour.distance+", "+dist);
                if(!neighbour.visited && neighbour.distance > dist)
                {
                    // System.out.println("Removing - "+neighbour);
                    unvisited.remove(neighbour);
                    // print(unvisited);
                    neighbour.distance = dist;
                    neighbour.parent = min.vertex;
                    unvisited.add(neighbour);
                    // print(unvisited);
                }
            }
        }
    }
}



class NodeCompare implements Comparator<Node>
{
    public int compare(Node a, Node b)
    {
        if (a.distance == b.distance)
        {
            return a.vertex - b.vertex;
        }
        else
        {
            return a.distance - b.distance;
        }
    }
}

class Node
{
    Integer vertex;
    Integer parent;
    //distance from source
    Integer distance;
    Boolean visited = false;

    public Node(Integer vertex, Integer parent, Integer distance)
    {
        this.vertex = vertex;
        this.parent = parent;
        this.distance = distance;
    }

    public String toString()
    {
        return vertex+", "+distance;
    }
}
