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
            String src,dest,taxiName="new_taxi",taxiPosition;
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
                        taxi.updatePosition(time,map);
                        if(taxi.isAvailable(time))
                        {
                            System.out.println(taxi);
                        }
                        else
                        {
                            System.out.println("<busy> "+taxi);
                        }
                    }
                    System.out.println();
                    break;
                case "customer":
                    // map.printGraph();
                    customerRequest(input,map);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
	}

    public void customerRequest(Scanner input, Graph map)
    {
        String src1 = input.next();
        String src2 = input.next();
        Integer srcDist = input.nextInt();
        String dest1 = input.next();
        String dest2 = input.next();
        Integer destDist = input.nextInt();
        // Position src = new Position(map.vertexId.get(src1),map.vertexId.get(src2),srcDist);
        // Position dest = new Position(map.vertexId.get(dest1),map.vertexId.get(dest2),destDist);
        Integer time = input.nextInt();
        TaxiService.currentTime = time;
        Integer src = map.getVertexId(src1,src2,srcDist);
        Integer dest = map.getVertexId(dest1,dest2,destDist);
        String taxiName = "NoName";
        if (src != null && dest != null)
        {
            Integer minDist = Integer.MAX_VALUE;
            PathNode minTaxiPath = null;
            // ShortestPath path1 = new ShortestPath(map,src.p1);
            // ShortestPath path2 = new ShortestPath(map,src.p2);
            ShortestPath path = new ShortestPath(map,src);
            if(map.taxiList.size() > 0)
            {
                Integer numTaxis = 0;
                System.out.println("Available taxis:");


                for(int i=0;i<map.taxiList.size();i++)
                {
                    Taxi taxi = map.taxiList.get(i);
                    taxi.updatePosition(time,map);

                    numTaxis++;
                    if(taxi.isAvailable(time))
                    {
                        System.out.print("Path of "+map.getTaxiName(taxi.taxiId)+": ");
                    }
                    else
                    {
                        System.out.print("Path of "+map.getTaxiName(taxi.taxiId)+" <busy> : ");
                    }
                    // System.out.println("Taxi Position - "+taxi.position+" - "+map.getVertexName(taxi.position));
                    // printRoutePath(taxi.path);
                    // System.out.println("\n-- "+taxi.isAvailable(time)+" --");
                    ArrayList<PathNode> taxi_route= taxi.getPath(path,time);
                    Integer time_end = taxi_route.get(taxi_route.size()-1).time;
                    if(time_end < minDist)
                    {
                        minDist = time_end;
                        taxiName = map.getTaxiName(taxi.taxiId);
                    }

                    printRoutePath(taxi_route);
                    System.out.println(". time taken is "+(time_end - time)+" units");
                }
                if(numTaxis > 0)
                {
                    System.out.println("\n*** Chose "+taxiName+" to service the customer request ***");
                    Taxi taxi = map.taxiList.get(map.taxiId.get(taxiName));
                    taxi.route(dest,path,time);
                    // System.out.println("TAXI - ");
                    // printRoutePath(taxi.path);
                    // System.out.println("------");
                    System.out.print("Path of customer: ");
                    Node node = path.nodes[dest];
                    Integer timePath = node.distance;
                    ArrayList<String> pathList = new ArrayList<String>();
                    while(node != null)
                    {
                        pathList.add(map.getVertexName(node.vertex));
                        if(node.parent == null)
                        {
                            break;
                        }
                        node = path.nodes[node.parent];
                    }
                    Collections.reverse(pathList);
                    printRoute(pathList);
                    System.out.println(". time taken is "+timePath+" units");
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
        System.out.println("\n");
    }

    public void printRoute(ArrayList<String> path)
    {
        int size = path.size();
        for(int i=0;i<size;i++)
        {
            String place = path.get(i);
            if(Graph.originalVertices.contains(place)|| i == 0 || i==(size-1))
            {
                System.out.print(place+", ");
            }
        }
    }

    public void printRoutePath(ArrayList<PathNode> path)
    {
        int size = path.size();
        for(int i=0;i<size;i++)
        {
            PathNode place = path.get(i);
            if(Graph.originalVerticesSet.contains(place.position)|| i == 0 || i==(size-1))
            {
                System.out.print(Graph.vertexName.get(place.position)+", ");
            }

            // System.out.print(Graph.vertexName.get(place.position)+" ("+place.time+")"+", ");
        }
    }



    // private ArrayList<PathNode> optimalPath(Position src, Position dest, ShortestPath path1, ShortestPath path2, Integer time, boolean isReverse)
    // {
    //     Integer time1 = 0,time2 = 0, time3 = 0,time4 = 0;
    //     time1 = src.dist+path1.nodes[dest.p1].distance+dest.dist;
    //     time2 = src.dist+path1.nodes[dest.p2].distance+dest.length() - dest.dist;
    //     time3 = src.length() - src.dist + path2.nodes[dest.p1].distance+dest.dist;
    //     time4 = src.length() - src.dist + path2.nodes[dest.p2].distance+dest.length() - dest.dist;
    //     ArrayList<PathNode> path = null;
    //     Integer temp1 = (time1 < time2)?time1:time2;
    //     Integer temp2 = (time3 < time4)?time3:time4;
    //     Integer minTime = (temp1 < temp2)?temp1:temp2;
    //
    //     Node node = null;
    //     if(time1 == minTime)
    //     {
    //         node = path1.nodes[dest.p1];
    //         if(isReverse)
    //         {
    //             if(dest.dist != 0)
    //             {
    //                 tempPath.add(new PathNode(dest,time));
    //                 for(int i=1;i<dest.distance;i++)
    //                 {
    //                     tempPath.add(new PathNode(new Position(dest.p1,dest.p2,dest.distance-i),time+i));
    //                 }
    //                 time+=dest.distance;
    //             }
    //             while(node != null)
    //             {
    //                 Position v = Graph.getVertex(node.vertex);
    //                 tempPath.add(new PathNode(v,time));
    //                 for(int i=1;i<v.distance;i++)
    //                 {
    //                     tempPath.add(new PathNode(new Position(v.p1,v.p2,i),time+i));
    //                 }
    //                 time += v.distance;
    //                 node = path1.nodes[node.parent];
    //             }
    //         }
    //         else
    //         {
    //             while(node != null)
    //             {
    //                 tempPath.add(new PathNode(node.vertex, time+node.distance));
    //             }
    //             Collection.reverse(tempPath);
    //         }
    //     }
    //     else if(time2 == minTime)
    //     {
    //         node = path1.nodes[dest.p2];
    //         time += src.dist;
    //     }
    //     else if(time3 == minTime)
    //     {
    //         node = path2.nodes[dest.p1];
    //         time += src.length() - src.dist;
    //     }
    //     else
    //     {
    //         node = path2.nodes[dest.p2];
    //         time += src.length() - src.dist;
    //     }
    //     Integer pathTime = node.distance;
    //     ArrayList<PathNode> tempPath = new ArrayList<PathNode>;
    //
    //     if(isReverse)
    //     {
    //         if(dest.dist != 0)
    //         {
    //
    //         }
    //         while(node != null)
    //         {
    //             tempPath.add(new PathNode(Graph.getVertex(node.vertex),time+pathTime - node.distance));
    //             node = path1.nodes[node.parent];
    //         }
    //     }
    //     else
    //     {
    //         while(node != null)
    //         {
    //             tempPath.add(new PathNode(node.vertex, time+node.distance));
    //         }
    //         Collection.reverse(tempPath);
    //     }
    //     return tempPath;
    // }
    //
    // private void routeTaxi(Taxi taxi, Integer srcId, Integer destId,ShortestPath path, Integer time)
    // {
    //     ArrayList<PathNode> taxiPathsrc = new ArrayList<PathNode>();
    //     Node node = path.nodes[taxi.position];
    //     Integer pathTime = node.distance;
    //
    //     while(node.parent != null)
    //     {
    //         taxiPathsrc.add(new PathNode(node.vertex, time+pathTime - node.distance));
    //         node = path.nodes[node.parent];
    //     }
    //     taxiPathsrc.add(new PathNode(node.vertex, time+pathTime));
    //
    //     node = path.nodes[destId];
    //     Integer timePath = node.distance;
    //
    //     taxi.timeStart = time;
    //     taxi.timeEnd = time+timePath+pathTime;
    //
    //     System.out.print("Path of customer: source, ");
    //     ArrayList<String> pathList = new ArrayList<String>();
    //     ArrayList<PathNode> taxiPathDestReverse = new ArrayList<PathNode>();
    //
    //     taxiPathDestReverse.add(new PathNode(node.vertex,pathTime+time+node.distance));
    //     node = path.nodes[node.parent];
    //     while(node.parent != null)
    //     {
    //         taxiPathDestReverse.add(new PathNode(node.vertex,pathTime+time+node.distance));
    //         pathList.add(map.getVertexName(node.vertex));
    //         node = path.nodes[node.parent];
    //     }
    //     Integer size = pathList.size();
    //     for(int i=size-1;i>=0;i--)
    //     {
    //         System.out.print(pathList.get(i)+", ");
    //     }
    //     System.out.println("destination. time taken is "+timePath+" units.");
    //
    //     PathNode[] taxipath = new PathNode[taxiPathDestReverse.size()+taxiPathsrc.size()];
    //
    //     for(int i=0;i<taxiPathsrc.size();i++)
    //     {
    //         taxipath[i] = taxiPathsrc.get(i);
    //     }
    //
    //     for(int i=taxiPathDestReverse.size()-1;i>=0;i--)
    //     {
    //         taxipath[taxipath.length - i - 1] = taxiPathDestReverse.get(i);
    //     }
    //
    //     taxi.path = taxipath;
    // }
}
