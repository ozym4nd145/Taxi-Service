import java.util.*;
import java.io.*;

public class TaxiService{
    Graph map = new Graph();
	public TaxiService() {
		// ...
	}

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
                    System.out.println("\n");
                    break;

                case "taxi":
                    // map.printGraph();
                    taxiName = input.next();
                    taxiPosition = input.next();
                    if(!map.addTaxi(taxiName,taxiPosition))
                    {
                        System.out.println("No Such Position");
                    }
                    System.out.println("\n");
                    break;

                case "printTaxiPosition":
                    time = input.nextInt();
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
                    System.out.println("\n");
                    break;

                case "customer":
                    src = input.next();
                    dest = input.next();
                    srcId = map.vertexId.get(src);
                    destId = map.vertexId.get(dest);

                    if (srcId != null && destId != null)
                    {
                        time = input.nextInt();
                        minDist = Integer.MAX_VALUE;
                        ShortestPath path = new ShortestPath(map,srcId);
                        if(map.taxiList.size() > 0)
                        {
                            Integer numTaxis = 0;
                            System.out.println("Available taxis:");
                            for(int i=0;i<map.taxiList.size();i++)
                            {
                                taxi = map.taxiList.get(i);
                                taxi.updatePosition(time);
                                if(taxi.isAvailable(time))
                                {
                                    numTaxis++;
                                    System.out.print("Path of "+map.getTaxiName(taxi.taxiId)+": ");
                                    // System.out.println("Taxi Position - "+taxi.position+" - "+map.getVertexName(taxi.position));
                                    Node node = path.nodes[taxi.position];
                                    Integer distance = node.distance;
                                    if(distance < minDist)
                                    {
                                        minDist = distance;
                                        taxiName = map.getTaxiName(taxi.taxiId);
                                    }
                                    while(node.parent != null)
                                    {
                                        System.out.print(map.getVertexName(node.vertex)+", ");
                                        node = path.nodes[node.parent];
                                    }
                                    System.out.println(src+". time taken is "+distance+" units");
                                }
                            }
                            if(numTaxis > 0)
                            {
                                System.out.println("\n*** Chose "+taxiName+" to service the customer request ***");
                                taxi = map.taxiList.get(map.taxiId.get(taxiName));
                                routeTaxi(taxi,srcId,destId,path,time);
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
                    break;
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
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

        System.out.print("Path of customer: "+Graph.vertexName.get(srcId)+", ");
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
        System.out.println(Graph.vertexName.get(destId)+". time taken is "+timePath+" units.");

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
