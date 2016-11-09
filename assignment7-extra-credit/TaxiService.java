import java.util.*;
import java.io.*;

public class TaxiService{
    Graph map = new Graph();
	public TaxiService() {
		// ...
	}
    public static Integer currentTime = 0;
	public void performAction(String actionMessage) {
		System.out.println("action to be performed: " + actionMessage);
        Scanner input = new Scanner(actionMessage);
        try
        {
            String action = input.next();
            String src,dest,taxiName="lala",taxiPosition;
            Integer weight,time,minDist,srcId,destId;
            Taxi taxi;
            switch(action)
            {
                case "edge":
                    src = input.next();
                    dest = input.next();
                    weight = input.nextInt();
                    map.addEdge(src,dest,weight);
                    break;

                case "taxi":
                    taxiName = input.next();
                    taxiPosition = input.next();
                    if(!map.addTaxi(taxiName,taxiPosition))
                    {
                        System.out.println("No Such Position");
                        System.out.println();
                    }
                    break;
                case "printTaxiPosition":
                    time = input.nextInt();
                    currentTime = time;
                    Integer taxiNum = map.getTaxiId(taxiName);
                    for(int i=0;i<map.taxiList.size();i++)
                    {
                        taxi = map.taxiList.get(i);
                        taxi.updatePosition(time);
                        if(taxi.isAvailable(time))
                        {
                            System.out.println(taxi);
                        }
                    }
                    System.out.println();
                    break;
                case "customer":
                    src1 = input.next();
                    src2 = input.next();
                    srcDist = input.next();
                    dest1 = input.next();
                    dest2 = input.next();
                    destDist = input.next();
                    Position src = new Position(map.vertexId.get(src1),map.vertexId.get(src2),srcDist);
                    Position dest = new Position(map.vertexId.get(dest1),map.vertexId.get(dest2),destDist);
                    time = input.nextInt();
                    currentTime = time;

                    if (src.p1 != null && src.p2 != null && dest.p1 != null && dest.p2 != null)
                    {
                        minDist = Integer.MAX_VALUE;
                        PathNode minTaxiPath = null;

                        ShortestPath path1 = new ShortestPath(map,src.p1);
                        ShortestPath path2 = new ShortestPath(map,src.p2);
                        if(map.taxiList.size() > 0)
                        {
                            Integer numTaxis = 0;
                            System.out.println("Available taxis:");
                            for(int i=0;i<map.taxiList.size();i++)
                            {
                                taxi = map.taxiList.get(i);
                                numTaxis++;
                                taxi.updatePosition(time);
                                System.out.print("Path of "+map.getTaxiName(taxi.taxiId)+": ");

                                PathNode[] taxiPath = null;

                                if(taxi.isAvailable(time))
                                {
                                    taxiPath = optimalPath(src,taxi.getPosition(),path1,path2,time,true);
                                }
                                else
                                {
                                    PathNode[] tempPath = optimalPath(src,taxi.getEndPosition(),path1,path2,taxi.timeEnd,true);
                                    taxiPath = new PathNode[tempPath.length + taxi.path.length];
                                    for(int i=0;i<taxi.path.length;i++)
                                    {
                                        taxiPath[i] = taxi.path[i];
                                    }
                                    for(int i=0;i<tempPath.length;i++)
                                    {
                                        taxiPath[taxi.path.length+i] = tempPath[i];
                                    }
                                }

                                Integer distance = taxiPath[taxiPath.length - 1].time - time;
                                if(distance < minDist)
                                {
                                    minDist = distance;
                                    taxiName = map.getTaxiName(taxi.taxiId);
                                    minTaxiPath = taxiPath;
                                }

                                for(int i=0;i<taxiPath.length-1;i++)
                                {
                                    System.out.print(map.getVertexName(taxiPath.position)+", ");
                                }
                                System.out.println("source. time taken is "+distance+" units");
                            }
                            if(numTaxis > 0)
                            {
                                System.out.println("** Chose "+taxiName+" to service the customer request ***");
                                taxi = map.taxiList.get(map.taxiId.get(taxiName));
                                PathNode[]
                            }
                            else
                            {
                                System.out.println("No Taxis Available");
                            }
                        }
                    }
                    else
                    {
                        System.out.println("No Such Source or Destination");
                    }
                    System.out.println();
                    break;
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
	}

    private ArrayList<PathNode> optimalPath(Position src, Position dest, ShortestPath path1, ShortestPath path2, Integer time, boolean isReverse)
    {
        Integer time1 = 0,time2 = 0, time3 = 0,time4 = 0;
        time1 = src.dist+path1.nodes[dest.p1].distance+dest.dist;
        time2 = src.dist+path1.nodes[dest.p2].distance+dest.length() - dest.dist;
        time3 = src.length() - src.dist + path2.nodes[dest.p1].distance+dest.dist;
        time4 = src.length() - src.dist + path2.nodes[dest.p2].distance+dest.length() - dest.dist;
        ArrayList<PathNode> path = null;
        Integer temp1 = (time1 < time2)?time1:time2;
        Integer temp2 = (time3 < time4)?time3:time4;
        Integer minTime = (temp1 < temp2)?temp1:temp2;

        Node node = null;
        if(time1 == minTime)
        {
            node = path1.nodes[dest.p1];
            if(isReverse)
            {
                if(dest.dist != 0)
                {
                    tempPath.add(new PathNode(dest,time));
                    for(int i=1;i<dest.distance;i++)
                    {
                        tempPath.add(new PathNode(new Position(dest.p1,dest.p2,dest.distance-i),time+i));
                    }
                    time+=dest.distance;
                }
                while(node != null)
                {
                    Position v = Graph.getVertex(node.vertex);
                    tempPath.add(new PathNode(v,time));
                    for(int i=1;i<v.distance;i++)
                    {
                        tempPath.add(new PathNode(new Position(v.p1,v.p2,i),time+i));
                    }
                    time += v.distance;
                    node = path1.nodes[node.parent];
                }
            }
            else
            {
                while(node != null)
                {
                    tempPath.add(new PathNode(node.vertex, time+node.distance));
                }
                Collection.reverse(tempPath);
            }
        }
        else if(time2 == minTime)
        {
            node = path1.nodes[dest.p2];
            time += src.dist;
        }
        else if(time3 == minTime)
        {
            node = path2.nodes[dest.p1];
            time += src.length() - src.dist;
        }
        else
        {
            node = path2.nodes[dest.p2];
            time += src.length() - src.dist;
        }
        Integer pathTime = node.distance;
        ArrayList<PathNode> tempPath = new ArrayList<PathNode>;

        if(isReverse)
        {
            if(dest.dist != 0)
            {

            }
            while(node != null)
            {
                tempPath.add(new PathNode(Graph.getVertex(node.vertex),time+pathTime - node.distance));
                node = path1.nodes[node.parent];
            }
        }
        else
        {
            while(node != null)
            {
                tempPath.add(new PathNode(node.vertex, time+node.distance));
            }
            Collection.reverse(tempPath);
        }
        return tempPath;
    }

    private void routeTaxi(Taxi taxi, Integer srcId, Integer destId,ShortestPath path, Integer time)
    {
        ArrayList<PathNode> taxiPathsrc = new ArrayList<PathNode>();
        Node node = path.nodes[taxi.position];
        Integer pathTime = node.distance;

        while(node.parent != null)
        {
            taxiPathsrc.add(new PathNode(node.vertex, time+pathTime - node.distance));
            node = path.nodes[node.parent];
        }
        taxiPathsrc.add(new PathNode(node.vertex, time+pathTime));

        node = path.nodes[destId];
        Integer timePath = node.distance;

        taxi.timeStart = time;
        taxi.timeEnd = time+timePath+pathTime;

        System.out.print("Path of customer: source, ");
        ArrayList<String> pathList = new ArrayList<String>();
        ArrayList<PathNode> taxiPathDestReverse = new ArrayList<PathNode>();

        taxiPathDestReverse.add(new PathNode(node.vertex,pathTime+time+node.distance));
        node = path.nodes[node.parent];
        while(node.parent != null)
        {
            taxiPathDestReverse.add(new PathNode(node.vertex,pathTime+time+node.distance));
            pathList.add(map.getVertexName(node.vertex));
            node = path.nodes[node.parent];
        }
        Integer size = pathList.size();
        for(int i=size-1;i>=0;i--)
        {
            System.out.print(pathList.get(i)+", ");
        }
        System.out.println("destination. time taken is "+timePath+" units.");

        PathNode[] taxipath = new PathNode[taxiPathDestReverse.size()+taxiPathsrc.size()];

        for(int i=0;i<taxiPathsrc.size();i++)
        {
            taxipath[i] = taxiPathsrc.get(i);
        }

        for(int i=taxiPathDestReverse.size()-1;i>=0;i--)
        {
            taxipath[taxipath.length - i - 1] = taxiPathDestReverse.get(i);
        }

        taxi.path = taxipath;
    }
}
