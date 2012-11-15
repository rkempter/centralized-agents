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
	private localSearchNode ChangingVehicle(Vehicle vehicleA, Vehicle vehicleB) {
		localSearchNode changeVehicleNode = null;
		
		// THINK OF HASH COLLISIONS!!!!!!!!!!
		
		// We put taskObject from vehicle A to vehicle B
		
		ArrayList<Object> firstTaskPickUpA = taskOrder.getValue(vehicleA.id());
		Task task = (Task) firstTaskPickUpA.get(0);
		ArrayList<Object> firstTaskDeliverA = new ArrayList<Object>();
		firstTaskDeliverA.set(0, task);
		firstTaskDeliverA.set(1, actionStates.DELIVER);
		
		Integer deliverTaskHash = (task.toString() + actionStates.DELIVER).hashCode();

		removeTaskFromList(createHash(firstTaskPickUpA), vehicleA);
		removeTaskFromList(deliverTaskHash, vehicleA);
		
		// Put delivery task at beginning (time 1)
		addTaskToList(firstTaskDeliverA, vehicleB, 1);
		
		// Put task at beginning (time 1)
		addTaskToList(firstTaskPickUpA, vehicleB, 1);
		
		
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
		ArrayList<Object> previous = getPreviousValue(hash);
		Integer previousKey = createHash(previous);
		taskOrder.addKeyValue(previousKey, next);
		taskOrder.addKeyValue(hash, null);
	}
	
	/**
	 * Adds an taskElement to a vehicle and updates the keys
	 * 
	 * @param task
	 * @param vehicle
	 * @param time
	 */
	private void addTaskToList(ArrayList<Object> taskObject, Vehicle vehicle, Integer time) {
		ArrayList<Object> taskAtPosition = taskOrder.getValue(getTaskByTimeAndVehicle(time, vehicle));
		Integer previousKey = null;
		
		if(time > 1) {
			ArrayList<Object> previousTask = getPreviousValue(createHash(taskObject));
			previousKey = createHash(previousTask);
		} else {
			previousKey = vehicle.id();
		}
		
		Integer key = createHash(taskObject);
		taskOrder.addKeyValue(key, taskAtPosition);
			
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
	
	private Integer getTaskByTimeAndVehicle(Integer time, Vehicle vehicle) {
		ArrayList<Integer> tasks = timeArray.getTaskSameTime(time);
		for(int i = 0; i < tasks.size(); i++) {
			Integer hash = tasks.get(i);
			
			if(vehicleArray.getValue(hash) == vehicle) {
				return hash;
			} 
		}
		
		return null;
	}
	
	private ArrayList<Object> getPreviousValue(Integer hash) {
		Integer time = timeArray.getValue(hash);
		Vehicle vehicle = vehicleArray.getValue(hash);
		Integer newHash = getTaskByTimeAndVehicle(time-1, vehicle);
		return taskOrder.getValue(newHash);
		
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
		
		return maxNode;
		
		//cost is equal to: Sum over all vehicules( (every vehicule to first task (pickup)) * cost) + Sum over all tasks(dist((task i, action), nextTask(task i, action)*cost)
	}
	
}
