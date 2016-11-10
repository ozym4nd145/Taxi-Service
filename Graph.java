import java.util.*;
public class Graph
{
    private Integer counterVertex;
    private Integer counterTaxi;
    public static HashMap<String, Integer> vertexId = new HashMap<String, Integer>();
    public static HashMap<String, Integer> taxiId = new HashMap<String, Integer>();
    public static HashMap<Integer, String> vertexName = new HashMap<Integer, String>();
    public static HashMap<Integer, String> taxiName = new HashMap<Integer, String>();
    ArrayList<LinkedList<Edge> > adjList = new ArrayList<LinkedList<Edge> >();
    ArrayList<Taxi > taxiList = new ArrayList<Taxi >();
    public Graph()
    {
        counterVertex = 0;
        counterTaxi = 0;
    }

    public void printGraph()
    {
        int i=0;
        for (LinkedList<Edge> edge: adjList)
        {
            System.out.print((i)+" -> ");
            for(Edge ed: edge)
            {
                System.out.print((ed.end+","+ed.weight)+" | ");
            }
            System.out.print("\n");
            i++;
        }
        System.out.println();
        System.out.println();
    }

    public String getVertexName(Integer id)
    {
        return vertexName.get(id);
    }
    public String getTaxiName(Integer id)
    {
        return taxiName.get(id);
    }
    public Integer getTaxiId(String name)
    {
        Integer id = taxiId.get(name);
        if (id == null)
        {
            taxiId.put(name,counterTaxi);
            taxiName.put(counterTaxi, name);
            id = counterTaxi;
            counterTaxi++;
        }
        return id;
    }
    public Integer getVertexId(String pos)
    {
        Integer id = vertexId.get(pos);
        // System.out.println(pos+" - "+id);
        if (id == null)
        {
            vertexId.put(pos,counterVertex);
            // System.out.println("new - "+pos+counterVertex);
            vertexName.put(counterVertex, pos);
            id = counterVertex;
            adjList.add(new LinkedList<Edge>());
            counterVertex++;
        }
        return id;
    }
    public void addEdge(String pos1, String pos2, Integer weight)
    {
        Integer prevCount = counterVertex;
        Integer id1 = getVertexId(pos1);
        Integer id2 = getVertexId(pos2);
        adjList.get(id1).add(new Edge(id1,id2,weight));
        adjList.get(id2).add(new Edge(id2,id1,weight));
    }
    public boolean addTaxi(String name, String pos)
    {
        Integer posId = vertexId.get(pos);
        if (posId != null)
        {
            Integer taxiId = getTaxiId(name);
            taxiList.add(new Taxi(taxiId,posId));
            return true;
        }
        else
        {
            return false;
        }
    }
}
