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

	public localSearchNode(nextTask _taskOrder, timeClass _timeArray, vehicleClass _vehicleArray) {
		taskOrder = _taskOrder;
		timeArray = _timeArray;
		vehicleArray = _vehicleArray;
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
	public void changingVehicle(Vehicle vehicleA, Vehicle vehicleB) {
		localSearchNode changeVehicleNode = null;
		
		taskOrder.printState();
		
		// THINK OF HASH COLLISIONS!!!!!!!!!!
		
		// We put taskObject from vehicle A to vehicle B
		
		System.out.println("Before in localSearchNode");
		printStates(vehicleA);
		printStates(vehicleB);
		
		ArrayList<Object> firstTaskPickUpA = taskOrder.getValue(vehicleA.id());
		Task task = (Task) firstTaskPickUpA.get(0);
		ArrayList<Object> firstTaskDeliverA = new ArrayList<Object>();
		firstTaskDeliverA.add(task);
		firstTaskDeliverA.add(actionStates.DELIVER);
		
		Integer deliverTaskHash = (task.toString() + actionStates.DELIVER).hashCode();

		// Remove first delivery task
		removeTaskFromList(deliverTaskHash, vehicleA);
		
		// Remove then pickup task
		removeTaskFromList(createHash(firstTaskPickUpA), vehicleA);
		
		System.out.println("Middle in localSearchNode");
		printStates(vehicleA);
		printStates(vehicleB);
		
		// Put delivery task at beginning (time 1)
		addTaskToList(firstTaskDeliverA, vehicleB, 0);
		
		// Put task at beginning (time 1)
		addTaskToList(firstTaskPickUpA, vehicleB, 0);
		
		System.out.println("After in localSearchNode");
		printStates(vehicleA);
		printStates(vehicleB);
		
		// We have to exchange Pickup and delivery!!!!!!
		
		// Change exchange tasks at specific keys.
		// Change key of the following element
		
		// Change tasks in the vehicule array
		// Change time of tasks
		
		// Check capacity constraints
		
	}
	
	/**
	 * Removes a taskObject from a vehicle and updates the keys
	 * 
	 * @param hash
	 * @param vehicle
	 */
	private void removeTaskFromList(Integer hash, Vehicle vehicle) {
		ArrayList<Object> next = taskOrder.getValue(hash);
		Integer previousKey = getPreviousHash(hash);
		
		taskOrder.addKeyValue(previousKey, next);
		taskOrder.addKeyValue(hash, null);
		
		printStates(vehicle);
	}
	
	/**
	 * Adds an taskElement to a vehicle and updates the keys
	 * 
	 * @param task
	 * @param vehicle
	 * @param time
	 */
	private void addTaskToList(ArrayList<Object> taskObject, Vehicle vehicle, Integer time) {
		System.out.println("------------");
		System.out.println(timeArray.getValue(vehicle.id()));
		System.out.println("Task by time and vehicle: "+getTaskByTimeAndVehicle(time, vehicle));
		ArrayList<Object> taskAtPosition = taskOrder.getValue(getTaskByTimeAndVehicle(time, vehicle));
		System.out.println("taskAtPosition: "+taskAtPosition);
		System.out.println("Time we want to put task: "+time);
		Integer previousKey = null;
		Integer key = createHash(taskObject);
		System.out.println("TaskObject key"+key);
		
		if(time > 0) {
			previousKey = getPreviousHash(key);
			System.out.println("Previous Hash: "+previousKey);
		} else {
			previousKey = vehicle.id();
		}
		
		taskOrder.addKeyValue(key, taskAtPosition);
		taskOrder.addKeyValue(previousKey, taskObject);
		System.out.println("------------");
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
	
	/**
	 * Returns hash of a taskObject given a time and a vehicle
	 * @TODO: Vehicle list is wrong, the first tasks (0, 1, 2, ...) are missing
	 * 
	 * @param time
	 * @param vehicle
	 * @return
	 */
	private Integer getTaskByTimeAndVehicle(Integer time, Vehicle vehicle) {
		System.out.println("Time: "+time);
		ArrayList<Integer> tasks = timeArray.getTaskSameTime(time);
		System.out.println("Task list for that time: "+tasks);
		for(int i = 0; i < tasks.size(); i++) {
			Integer hash = tasks.get(i);
			
			if(vehicleArray.getValue(hash) == vehicle) {
				System.out.println("vehicle: "+vehicle.id());
				return hash;
			} 
		}
		
		return null;
	}
	
	/**
	 * Returns the hash key of the previous taskObject in the chain
	 * 
	 * @param hash
	 * @return previous hash
	 */
	private Integer getPreviousHash(Integer hash) {
		System.out.println("Hash "+hash);
		Integer time = timeArray.getValue(hash);
		System.out.println("Time bug "+time);
		Vehicle vehicle = vehicleArray.getValue(hash);
		System.out.println("Vehicle bug");
		Integer newHash;
		
		if(time > 1) {
			newHash = getTaskByTimeAndVehicle(time-1, vehicle);
		} else {
			newHash = vehicle.id();
		}
		
		return newHash;
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
	
	private void printStates(Vehicle vehicle) {
		ArrayList<ArrayList<Object>> vehiclePlan = new ArrayList<ArrayList<Object>>();			//when get the plan test with .isEmpty() not with .equals(null)
		int key = vehicle.id();
		
		while(taskOrder.getValue(key)!= null){
			//in the plan just need to save one of the two action. It matters the task
			ArrayList<Object> test = new ArrayList<Object>();
			test.add(key);
			test.add((Task)taskOrder.getValue(key).get(0));
			test.add(taskOrder.getValue(key).get(1));
			vehiclePlan.add(test);
			key = (taskOrder.getValue(key).get(0).toString() + taskOrder.getValue(key).get(1)).hashCode();
		}
		System.out.println("Vehicle "+vehicle.id());
		System.out.println(vehiclePlan);
	}
	
}
