package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import logist.simulation.Vehicle;
import logist.task.Task;

public class localSearchNode {
	
	private nextTask taskOrder;
	private timeClass timeArray;
	private vehicleClass vehicleArray;
	
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
	 * Exchange two task (PICKUP & DELIVER) between
	 * two vehicles
	 * 
	 * @param taskCar1
	 * @param taskCar2
	 * @return
	 */
	private localSearchNode ChangingVehicle(ArrayList<Object> taskCarA, ArrayList<Object> taskCarB, Vehicle vehicleA, Vehicle vehicleB) {
		localSearchNode changeVehicleNode = null;
		Task taskA = (Task) taskCarA.get(0);
		Task taskB = (Task) taskCarB.get(0);
		
		Integer taskAPickUpHash = (taskA.toString() + actionStates.PICKUP).hashCode();
		Integer taskADeliverHash = (taskA.toString() + actionStates.DELIVER).hashCode();
		Integer taskBPickUpHash = (taskB.toString() + actionStates.PICKUP).hashCode();
		Integer taskBDeliverHash = (taskB.toString() + actionStates.DELIVER).hashCode();
		
		int taskWeightA = taskA.weight;
		int taskWeightB = taskB.weight;
		
		int positionBInA = getPositionByCapacity(vehicleA, taskWeightA); // time at which we have to place task B
		int positionAInB = getPositionByCapacity(vehicleB, taskWeightB); // time at which we have to place task A
		
		if(positionBInA != -1 && positionAInB != -1) {
		
		}
		
		
		// We have to exchange Pickup and delivery!!!!!!
		
		// Change exchange tasks at specific keys.
		// Change key of the following element
		
		// Change tasks in the vehicule array
		// Change time of tasks
		
		// Check capacity constraints
		
		return changeVehicleNode;
	}
	
	/**
	 * Removes a taskObject from a vehicle and updates the keys
	 * 
	 * @param hash
	 * @param vehicle
	 */
	private void removeTaskFromList(Integer hash, Vehicle vehicle) {
		// First we need to remove the task A from the line A
		ArrayList<Object> next = taskOrder.getValue(hash);
		Integer time = timeArray.getValue(hash);
		ArrayList<Object> previous = getTaskByTimeAndVehicle(time-1, vehicle);
		Integer previousKey = createHash(previous);
		taskOrder.addKeyValue(previousKey, next);
	}
	
	/**
	 * Adds an taskElement to a vehicle and updates the keys
	 * 
	 * @param task
	 * @param vehicle
	 * @param time
	 */
	private void addTaskToList(ArrayList<Object> taskObject, Vehicle vehicle, Integer time) {
		ArrayList<Object> taskAtPosition = getTaskByTimeAndVehicle(time, vehicle);
		ArrayList<Object> previousTask = getTaskByTimeAndVehicle(time-1, vehicle);
		
		Integer key = createHash(taskObject);
		taskOrder.addKeyValue(key, taskAtPosition);
		
		Integer previousKey = createHash(previousTask);
		taskOrder.addKeyValue(previousKey, taskObject);
	}
	
	/**
	 * Creates the Hash value of a taskObject
	 * 
	 * @param taskObject
	 * @return hash
	 */
	private Integer createHash(ArrayList<Object> taskObject) {
		Task task = (Task) taskObject.get(0);
		Object taskAction = taskObject.get(1);
		
		return (task.toString() + taskAction).hashCode();
	}
	
	private ArrayList<Object> getTaskByTimeAndVehicle(Integer time, Vehicle vehicle) {
		ArrayList<ArrayList<Object>> tasks = timeArray.getTasks(time);
		for(int i = 0; i < tasks.size(); i++) {
			Task task = (Task) tasks.get(i).get(0);
			Object action = tasks.get(i).get(1);
			Integer hash = (task.toString()+action).hashCode();
			
			if(vehicleArray.getValue(hash) == vehicle) {
				return tasks.get(i);
			} 
		}
		
		return null;
	}
	
	private int getPositionByCapacity(Vehicle vehicleB, int taskWeightB) {
		// TODO Auto-generated method stub
		return 0;
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
