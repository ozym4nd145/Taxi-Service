import java.util.*;
public class Taxi
{
    Integer taxiId;
    Integer timeStart = 0;
    Integer timeEnd = 0;
    ArrayList<PathNode> path;
    boolean available=true;

    public Taxi(Integer id, Position pos)
    {
        taxiId = id;
        endPosition = pos;
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
        return Graph.taxiName.get(taxiId)+": "+getPosition(TaxiService.currentTime);
    }

    public ArrayList<PathNode> getPath(Integer time)
    {
        Position
    }


    public Position getPosition(Integer time)
    {
        Position position = null;
        if(time >= timeEnd)
        {
            return endPosition;
        }
        else
        {
            int i=0;
            for(i=0;i<path.length;i++)
            {
                if(time < path.get(i).time)
                {
                    break;
                }
                else
                {
                    position = path.get(i).position;
                    position.dist = time - path.get(i).time;
                }
            }
            if(position.dist > 0)
            {
                position.p2 = path.get(i).position.p2;
            }
        }
        return position;
    }
    public Position getEndPosition(Integer time)
    {
        return endPosition;
    }

    public boolean isAvailable(Integer time)
    {
        if(time >= timeEnd)
        {
            available = true;
        }
        return available;
    }
}

class Position
{
    Integer p1;
    Integer p2;
    Integer dist;
    public Position(Integer p1,Integer p2,Integer dist)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.dist = dist;
    }
    public String toString()
    {
        return Graph.vertexName.get(p1)+", "+Graph.vertexName.get(p2)+" - "+dist;
    }

    @Override
    public boolean equals(Object obj)
    {
        Position p = (Position)obj;
        Integer distance = Graph.getDistance(p1,p2);
        if((this.p1 == p.p1 && this.p2 == p.p2) && (this.dist == p.dist))
        {
            return true;
        }
        else if((this.p1 == p.p2 && this.p2 == p.p1)&&(this.dist == (distance - p.dist)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

class PathNode
{
    Position position;
    Integer time;
    public PathNode(Position position, Integer time)
    {
        this.position = position;
        this.time = time;
    }

}
