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
		while(iterationNbr > 0) {
			node = node.chooseNeighbours();
			iterationNbr--;
		}
		
		//node.changingVehicle(vehicles.get(0), vehicles.get(1));
		System.out.println("After: ");
		for(int i = 0; i < vehicles.size(); i++) {
			System.out.println(node.getPlanVehicle(vehicles.get(i)));
		}


		Plan planVehicle1 = naivePlan(vehicles.get(0), tasks);

		List<Plan> plans = new ArrayList<Plan>();
		plans.add(planVehicle1);
		while (plans.size() < vehicles.size())
			plans.add(Plan.EMPTY);

		return plans;
	}

	private Plan naivePlan(Vehicle vehicle, TaskSet tasks) {
		City current = vehicle.getCurrentCity();
		Plan plan = new Plan(current);

		for (Task task : tasks) {
			// move: current city => pickup location
			for (City city : current.pathTo(task.pickupCity))
				plan.appendMove(city);

			plan.appendPickup(task);

			// move: pickup location => delivery location
			for (City city : task.path())
				plan.appendMove(city);

			plan.appendDelivery(task);

			// set current city
			current = task.deliveryCity;
		}
		return plan;
	}

	public void generateSTartNode(TaskSet tasks){
		Iterator<Task> taskIterator= tasks.iterator();
		while ( taskIterator.hasNext() ){
			Task t= taskIterator.next() ;
			System.out.println( t );
		}

	}
}


