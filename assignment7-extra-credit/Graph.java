import java.util.*;
public class Graph
{
    private Integer counterVertex=0;
    private Integer counterTaxi=0;
    public static HashMap<String, Integer> vertexId = new HashMap<String, Integer>();
    public static HashMap<String, Integer> taxiId = new HashMap<String, Integer>();
    public static HashMap<Integer, String> vertexName = new HashMap<Integer, String>();
    public static HashMap<Integer, String> taxiName = new HashMap<Integer, String>();
    public static HashMap<String ,Integer> distance = new HashMap<String, Integer>();
    ArrayList<LinkedList<Edge> > adjList = new ArrayList<LinkedList<Edge> >();
    public static Set<Integer> originalVerticesSet = new HashSet<Integer>();
    public static Set<String> originalVertices = new HashSet<String>();
    public static ArrayList<String> mainVertices = new ArrayList<String>();
    ArrayList<Taxi > taxiList = new ArrayList<Taxi >();

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

    public Integer getNearestVertex(Integer pos)
    {
        String name = vertexName.get(pos);
        // System.out.println(name);
        if(name == null)
        {
            return null;
        }
        if(originalVerticesSet.contains(pos))
        {
            return mainVertices.indexOf(vertexName.get(pos));
        }
        String[] names = name.split(" ");
        Integer pos1 = vertexId.get(names[0]);
        Integer pos2 = vertexId.get(names[1]);
        Integer dist1 = Integer.parseInt(names[2]);
        Integer dista = distance.get(names[0]+" "+names[1]);
        Integer dist2 = dista - dist1;
        // System.out.println(dist1+" "+dist2);
        if(distance == null)
        {
            throw new Error("Incorrect Input");
        }
        Integer id1 = mainVertices.indexOf(names[0]);
        Integer id2 = mainVertices.indexOf(names[1]);
        // System.out.println(id1 +" , "+id2);
        if(dist1 == dist2)
        {
            return (id1<id2)?id1:id2;
        }
        else if(dist1 < dist2)
        {
            return id1;
        }
        else
        {
            return id2;
        }
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
            originalVerticesSet.add(id);
            originalVertices.add(pos);
            mainVertices.add(pos);
            counterVertex++;
        }
        return id;
    }

    public Integer getVertexId(String p1, String p2, Integer dist)
    {
        Integer v1 = vertexId.get(p1);
        Integer v2 = vertexId.get(p2);
        // System.out.println("GVID - "+p1+" - "+v1+" ; "+p2+" - "+v2);
        Integer id = vertexId.get(p1+" "+p2+" "+dist);
        if(v1 == null || v2 == null)
        {
            return null;
        }

        if(id != null)
        {
            return id;
        }
        else
        {
            if(dist.equals(0))
            {
                return v1;
            }
            else
            {
                return v2;
            }
        }
    }



    public Integer makeFakeVertex(String pos1, String pos2, Integer distance, Integer i)
    {
        vertexId.put(pos1+" "+pos2+" "+i,counterVertex);
        vertexId.put(pos2+" "+pos1+" "+(distance-i), counterVertex);
        vertexName.put(counterVertex,pos1+" "+pos2+" "+i);
        Integer id = counterVertex;
        adjList.add(new LinkedList<Edge>());
        // isDummy.add(true);
        counterVertex++;
        return id;
    }

    public void addEdge(String pos1, String pos2, Integer weight)
    {
        String edge1 = pos1+" "+pos2;
        String edge2 = pos2+" "+pos1;
        if(distance.containsKey(edge1) || distance.containsKey(edge2))
        {
            throw new Error("Edge already Inserted");
        }
        distance.put(edge1,weight);
        distance.put(edge2,weight);

        Integer prevCount = counterVertex;
        Integer prev = getVertexId(pos1);
        for(int i=1;i<weight ;i++)
        {
            Integer curr = makeFakeVertex(pos1,pos2,weight,i);
            adjList.get(prev).add(new Edge(prev,curr,1));
            adjList.get(curr).add(new Edge(curr,prev,1));
            prev = curr;
        }
        Integer wt = (weight > 0)?(1):(0);
        Integer id2 = getVertexId(pos2);
        adjList.get(prev).add(new Edge(prev,id2,wt));
        adjList.get(id2).add(new Edge(id2,prev,wt));
        // printGraph();
    }

    public boolean addTaxi(String name, String pos)
    {
        Integer posId = vertexId.get(pos);
        if (posId != null)
        {
            Integer taxiId = getTaxiId(name);
            taxiList.add(new Taxi(taxiId,posId,this));
            return true;
        }
        else
        {
            return false;
        }
    }

    // public Integer getDistance(Integer pos1,Integer pos2)
    // {
    //     if(pos1 > adjList.size())
    //     {
    //         return null;
    //     }
    //     Iterator<Edge> itr = adjList.get(pos1).iterator();
    //     Edge edge = null;
    //     while(itr.hasNext())
    //     {
    //         edge = itr.next();
    //         if(edge.end == pos2)
    //         {
    //             return edge.weight;
    //         }
    //     }
    //     return null;
    // }
}
