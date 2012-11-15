package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import logist.simulation.Vehicle;

public class localSearchNode {
	
	private nextTask taskOrder;
	private Map<Object, Integer> timeArray = new HashMap<Object, Integer>();
	private Map<Object, Vehicle> vehiculeArray = new HashMap<Object, Vehicle>();
	
	// CapacityArray

	public localSearchNode(nextTask _taskOrder) {
		taskOrder = _taskOrder;
	}
	
	public static void SelectInititialSolution() {
		// Create nextTask array
		// Create time array
		// Create vehicule array
		// Create capacity array
	}
	
	private void chooseNeighbours() {
		ArrayList<localSearchNode> neighbours = new ArrayList<localSearchNode>();
		
		// Change vehicule
		
		// Change task order
		
		localSearchNode bestNeighbour = localChoice(neighbours);
		// return best one
	}
	
	/**
	 * 
	 * @param taskCar1
	 * @param taskCar2
	 * @return
	 */
	private localSearchNode ChangingVehicle(ArrayList<Object> taskCarA, ArrayList<Object> taskCarB, Vehicle vehicleA, Vehicle vehicleB) {
		localSearchNode changeVehicleNode = null;
		
		// We have to exchange Pickup and delivery!!!!!!
		
		// Change exchange tasks at specific keys.
		// Change key of the following element
		
		// Change tasks in the vehicule array
		// Change time of tasks
		
		// Check capacity constraints
		
		return changeVehicleNode;
	}
	
	private localSearchNode ChangingTaskOrder() {
		localSearchNode taskOrderNode = null;
		// Check capacity constraints
		// Exchange tasks
		// Adjust following keys
		
		// Adjust time array
		// Adjust capacity array
		return taskOrderNode;
	}
	
	private boolean checkCapacity(Integer t, Vehicle v) {
		return true;
	}
	
	private localSearchNode localChoice(ArrayList<localSearchNode> neighbours) {
		localSearchNode maxNode = null;
		// Foreach assignement, calculate:
		int sum = 0;
		// For i in nextTask
		// sum += dist(i, nextTask(i)*cost
		
		// For j in vehicule
		// sum += dist(j.startPosition, nextTask(j))*cost
		
		// return best assignement
		
		return localSearchNode;
		
		//cost is equal to: Sum over all vehicules( (every vehicule to first task (pickup)) * cost) + Sum over all tasks(dist((task i, action), nextTask(task i, action)*cost)
	}
	
}
