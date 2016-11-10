import java.util.*;
public class Edge
{
    Integer start;
    Integer end;
    Integer weight;
    public Edge(Integer start, Integer end, Integer weight)
    {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object obj)
    {
        Edge other = (Edge) obj;
        return ((this.start.equals(other.start))&&(this.end.equals(other.end)))||((this.end.equals(other.start))&&(this.start.equals(other.end)));
    }

    @Override
    public String toString()
    {
        return "Edge- "+Graph.vertexName.get(start)+", "+Graph.vertexName.get(end)+" , "+weight;
    }
}
