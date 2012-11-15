package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import logist.simulation.Vehicle;
import logist.task.Task;

public class localSearchNode {
	
	private nextTask taskOrder;
	private Map<Object, Integer> timeArray = new HashMap<Object, Integer>();
	private Map<Object, Vehicle> vehicleArray = new HashMap<Object, Vehicle>();
	
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
			// First we need to remove the task A from the line A
			ArrayList<Object> nextAPickup = taskOrder.getNextTaskByHash(taskAPickUpHash);
			ArrayList<Object> nextADeliver = taskOrder.getNextTaskByHash(taskBPickUpHash);
			
			Integer timeAPickUp = timeArray.getTimeByHash(taskAPickUpHash);
			Integer timeADeliver = timeArray.getTimeByHash(TaskADeliverHash);
			
			ArrayList<Object> previousAPickUp = getTaskByTimeAndVehicle(timeAPickUp-1, vehicleA);
			ArrayList<Object> previousADeliver = getTaskByTimeAndVehicle(timeADeliver-1, vehicleA);
			
			previousAPickUp.set(1, nextAPickup);
			previousADeliver.set(1, nextADeliver);
			
			
			// DO IT FOR A
			ArrayList<Object> taskBeforePickUpA = nextTask.getTaskByTimeAndVehicle()
			// Set task and adjust keys
			// Exchange tasks in vehicles
			// Update time in time
		}
		
		
		// We have to exchange Pickup and delivery!!!!!!
		
		// Change exchange tasks at specific keys.
		// Change key of the following element
		
		// Change tasks in the vehicule array
		// Change time of tasks
		
		// Check capacity constraints
		
		return changeVehicleNode;
	}
	
	private ArrayList<Object> getTaskByTimeAndVehicle(Integer time, Vehicle vehicle) {
		ArrayList<ArrayList<Object>> tasks = timeArray.getTasks(time);
		for(int i = 0; i < tasks.size(); i++) {
			Task task = (Task) tasks.get(i).get(0);
			Object action = tasks.get(i).get(1);
			Integer hash = (task.toString()+action).hashCode();
			
			if(vehicleArray.get(hash) == vehicle) {
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
