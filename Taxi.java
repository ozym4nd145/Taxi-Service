import java.util.*;
public class Taxi
{
    Integer taxiId;
    Integer position;
    Integer timeStart = 0;
    Integer timeEnd = 0;
    PathNode[] path;
    public Taxi(Integer id, Integer pos)
    {
        taxiId = id;
        position = pos;
        path = null;
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.taxiId == ((Taxi)obj).taxiId;
    }

    @Override
    public String toString()
    {
        return Graph.taxiName.get(taxiId)+": "+Graph.vertexName.get(position);
    }

    public void updatePosition(Integer time)
    {
        int i=0;
        if(path == null)
        {
            return;
        }
        for(i=0;i<path.length;i++)
        {
            if(time < path[i].time)
            {
                break;
            }
            else
            {
                position = path[i].position;
                timeStart = path[i].time;
            }
        }
        PathNode[] newPath = null;
        if(i<path.length)
        {
            newPath = new PathNode[path.length - i];
            for(int j=0;i<path.length;i++,j++)
            {
                newPath[j] = path[i];
            }
        }
        path = newPath;
    }
    public Integer getCurrentPosition(Integer time)
    {
        updatePosition(time);
        return position;
    }
    public boolean isAvailable(Integer time)
    {
        return time >= timeEnd;
    }
}

class PathNode
{
    Integer position;
    Integer time;
    public PathNode(Integer position, Integer time)
    {
        this.position = position;
        this.time = time;
    }
}
