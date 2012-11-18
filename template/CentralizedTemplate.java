package template;

//the list of imports
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import logist.Measures;
import logist.behavior.AuctionBehavior;
import logist.behavior.CentralizedBehavior;
import logist.agent.Agent;
import logist.simulation.Vehicle;
import logist.plan.Plan;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;
import logist.topology.Topology.City;

/**
 * A very simple auction agent that assigns all tasks to its first vehicle and
 * handles them sequentially.
 * 
 */
@SuppressWarnings("unused")
public class CentralizedTemplate implements CentralizedBehavior {

	private Initialization init;
	private Topology topology;
	private TaskDistribution distribution;
	private Agent agent;
	private double bestCost =  Double.POSITIVE_INFINITY;


	@Override
	public void setup(Topology topology, TaskDistribution distribution,
			Agent agent) {

		this.topology = topology;
		this.distribution = distribution;
		this.agent = agent;		

	}

	@Override
	public List<Plan> plan(List<Vehicle> vehicles, TaskSet tasks) {

		init = new Initialization(tasks, vehicles);
		System.out.println("Before: ");
		System.out.println(init.getPlanVehicle(vehicles.get(0)));
		System.out.println(init.getPlanVehicle(vehicles.get(1)));

		System.out.println("*****");
		nextTask nt = init.getNextTask();
		timeClass tc = init.getTimeArray();
		vehicleClass vc = init.getVehicleArray();
		capacityClass cc= init.getCapacities();
		int iterationNbr = 1000;

		localSearchNode node = new localSearchNode(nt, tc, vc, cc, vehicles);
		int convergenceCheck = 100;			//if after convergenceCheck the cost doesn't drop, we consider that the algorithm converged.
		long oldCost= 0;
		while(iterationNbr > 0 && convergenceCheck> 0) {
			System.out.println("*****iteration "+ (1000-iterationNbr));

			node = node.chooseNeighbours();
			System.out.println("--------------");
			System.out.println(node.getCost());
			if((oldCost- node.getCost())==0){
				convergenceCheck--;
			}
			else
				convergenceCheck= 100;		//reset convergence checker;
			oldCost= node.getCost();
			for(int i = 0; i < vehicles.size(); i++) {
				System.out.println(node.getPlanVehicle(vehicles.get(i)));
			}
			iterationNbr--;
		}
		
		//node.changingVehicle(vehicles.get(0), vehicles.get(1));
		System.out.println("After: ");
		for(int i = 0; i < vehicles.size(); i++) {
			System.out.println(node.getPlanVehicle(vehicles.get(i)));
		}

		List<Plan> plans = new ArrayList<Plan>();
		
		for(int i = 0; i < vehicles.size(); i++) {
			Plan planVehicle = generatePlan(vehicles.get(i), node.getTaskOrder());
			plans.add(planVehicle);
		}

		return plans;
	}

	private Plan generatePlan(Vehicle vehicle, nextTask tasks) {
		City current = vehicle.getCurrentCity(), toCity = null;
		Plan plan = new Plan(current);
		Integer key = vehicle.id();
		ArrayList<Object> taskObject = tasks.getValue(key);

		while(taskObject != null) {
			Task task = (Task) taskObject.get(0);
			actionStates action = (actionStates) taskObject.get(1);
			
			if(action == actionStates.PICKUP) {
				toCity = task.pickupCity;
				if(!current.equals(toCity)) {
					for(City city : current.pathTo(toCity))
						plan.appendMove(city);
				}
				plan.appendPickup(task);
			} else if (action == actionStates.DELIVER) {
				toCity = task.deliveryCity;
				if(!current.equals(toCity)) {
					for(City city : current.pathTo(toCity))
						plan.appendMove(city);
				}
				plan.appendDelivery(task);
			}
			
			key = localSearchNode.createHash(taskObject);
			taskObject = tasks.getValue(key);
			current = toCity;
		}
		
		System.out.println("Plan: "+plan);
		
		return plan;
	}

	public void generateStartNode(TaskSet tasks){
		Iterator<Task> taskIterator= tasks.iterator();
		while ( taskIterator.hasNext() ){
			Task t= taskIterator.next() ;
			System.out.println( t );
		}

	}
	/**
	 * Chooses the best neighbour. Uses stochastic hill climbing in case there is no neighbour
	 * with heigher costs than the current solution.
	 * 
	 * @param neighbours
	 * @return bestNode
	 */
	private localSearchNode localChoice(localSearchNode currentNode, ArrayList<localSearchNode> neighbours) {
		localSearchNode bestNode = null;
		
		for(int i = 0; i < neighbours.size(); i++) {
			localSearchNode solution = neighbours.get(i);
			double cost = solution.getCost();

			if(cost < bestCost) {
				System.out.println("Updating bestCost: "+ bestCost +" and the new cost is: "+cost);
				bestNode = solution;
				bestCost = cost;
			}
		}

		// Stochastic Hill Climbing
		if(bestNode != null && bestNode.getCost() < currentNode.getCost()) {
			if(randomChoice()) {
				System.out.println(bestNode.getCost());
			} else {
				System.out.println(currentNode.getCost());
				bestNode= currentNode;
			}
		}
		return bestNode;
	}

	/**
	 * Simulation of stochastic process
	 * 
	 * @return true / false
	 */
	private boolean randomChoice() {
		Random generator = new Random();
		int number = generator.nextInt(10) + 1;
		int threshold = 9; 			// 0.9 probabilty of changing
		if(number <= threshold) {
			return true;
		}
		return false;
	}
	
}


