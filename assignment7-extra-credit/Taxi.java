import java.util.*;
public class Taxi
{
    Integer taxiId;
    Integer timeEnd = 0;
    ArrayList<PathNode> path = new ArrayList<PathNode>();
    boolean available=true;

    public Taxi(Integer id, Integer pos)
    {
        taxiId = id;
        path.add(new PathNode(pos,0));
        updatePosition(0);
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.taxiId == ((Taxi)obj).taxiId;
    }

    @Override
    public String toString()
    {
        return Graph.taxiName.get(taxiId)+": "+Graph.vertexName.get(getPosition(TaxiService.currentTime));
    }

    public void updatePosition(Integer time)
    {
        return;
    }

    public ArrayList<PathNode> getPath(ShortestPath pathShort,Integer time)
    {
        Integer position = getPosition(time);
        ArrayList<PathNode> pathList = new ArrayList<PathNode>();
        if(isAvailable(time))
        {
            Node node = pathShort.nodes[position];
            while(node != null)
            {
                pathList.add(new PathNode(node.vertex,time));
                time += 1;
                if(node.parent == null)
                {
                    break;
                }
                node = pathShort.nodes[node.parent];
            }
            // System.out.println("getPath - \n"+pathList);
            return pathList;
        }
        else
        {
            int i=0;
            for(i=0;i<path.size();i++)
            {
                if(time == path.get(i).time)
                {
                    break;
                }
            }
            for(int j=i;j<path.size();j++)
            {
                pathList.add(new PathNode(path.get(i).position,time));
                time += 1;
            }
            Node node = pathShort.nodes[path.get(path.size()-1).position];
            while(node != null)
            {
                pathList.add(new PathNode(node.vertex, time));
                time += 1;
                if(node.parent == null)
                {
                    break;
                }
                node = pathShort.nodes[node.parent];
            }
            return pathList;
        }
    }

    public Integer getPosition(Integer time)
    {
        if(time >= timeEnd)
        {
            updatePosition(time);
            return path.get(path.size()-1).position;
        }

        int i=0;
        for(i=0;i<path.size();i++)
        {
            if(time == path.get(i).time)
            {
                return path.get(i).position;
            }
        }

        return null;
    }

    public boolean isAvailable(Integer time)
    {
        if(time >= timeEnd)
        {
            available = true;
        }
        return available;
    }


    public void route(Integer dest, ShortestPath pathShort, Integer time)
    {
        ArrayList<PathNode> taxi_src = getPath(pathShort,time);
        Integer timeReachSrc = taxi_src.get(taxi_src.size()-1).time;
        ArrayList<PathNode> src_dest = new ArrayList<PathNode>();
        Node node = pathShort.nodes[dest];
        Integer timeTaken = timeReachSrc+ node.distance;
        while(node.parent != null)
        {
            src_dest.add(new PathNode(node.vertex,timeReachSrc+node.distance));
            node = pathShort.nodes[node.parent];
        }
        Collections.reverse(src_dest);
        ArrayList<PathNode> taxi_path_new = new ArrayList<PathNode>();
        taxi_path_new.addAll(taxi_src);
        taxi_path_new.addAll(src_dest);
        timeEnd = timeTaken;
        path = taxi_path_new;
        available = false;
    }
}

// class Position
// {
//     Integer p1;
//     Integer p2;
//     Integer dist;
//     public Position(Integer p1,Integer p2,Integer dist)
//     {
//         this.p1 = p1;
//         this.p2 = p2;
//         this.dist = dist;
//     }
//     public String toString()
//     {
//         return Graph.vertexName.get(p1)+", "+Graph.vertexName.get(p2)+" - "+dist;
//     }
//
//     @Override
//     public boolean equals(Object obj)
//     {
//         Position p = (Position)obj;
//         Integer distance = Graph.getDistance(p1,p2);
//         if((this.p1 == p.p1 && this.p2 == p.p2) && (this.dist == p.dist))
//         {
//             return true;
//         }
//         else if((this.p1 == p.p2 && this.p2 == p.p1)&&(this.dist == (distance - p.dist)))
//         {
//             return true;
//         }
//         else
//         {
//             return false;
//         }
//     }
// }

class PathNode
{
    Integer position;
    Integer time;
    public PathNode(Integer position, Integer time)
    {
        this.position = position;
        this.time = time;
    }

    @Override
    public String toString()
    {
        return Graph.vertexName.get(position)+" - "+time+"s";
    }

}
