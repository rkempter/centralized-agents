package template;

import java.util.ArrayList;
import logist.simulation.Vehicle;
import logist.task.Task;

/**
 * @author lollix89
 *
 */
public class localSearchNode {

	private nextTask taskOrder;
	private timeClass timeArray;
	private vehicleClass vehicleArray;
	private capacityClass capacities;

	// CapacityArray

	public localSearchNode(nextTask _taskOrder, timeClass _timeArray, vehicleClass _vehicleArray, capacityClass cc) {
		taskOrder = _taskOrder;
		timeArray = _timeArray;
		vehicleArray = _vehicleArray;
		capacities= cc;
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
		if(taskOrder.getValue(vehicleA.id())!= null){
			ArrayList<Object> firstTaskPickUpA = taskOrder.getValue(vehicleA.id());
			Task task = (Task) firstTaskPickUpA.get(0);

			ArrayList<Object> firstTaskDeliverA = new ArrayList<Object>();
			firstTaskDeliverA.add(task);
			firstTaskDeliverA.add(actionStates.DELIVER);

			Integer deliverTaskHash = (task.toString() + actionStates.DELIVER).hashCode();

			removeTaskFromList(deliverTaskHash, vehicleA);

			removeTaskFromList(createHash(firstTaskPickUpA), vehicleA);
		
			capacities.printCapacities();
			
			System.out.println("@@@@@@@@@@@@@@");
			//inconsistent State here
			System.out.println(getPlanVehicle(vehicleA));
			System.out.println(getPlanVehicle(vehicleB));

			System.out.println("@@@@@@@@@@@@@@");
			capacities.printCapacities();

			
			// Put delivery task at beginning (time 0)
			addTaskToList(firstTaskDeliverA, vehicleB, 0);

			// Put task at beginning (time 0)
			addTaskToList(firstTaskPickUpA, vehicleB, 0);
			capacities.printCapacities();

			//System.out.println()

			System.out.println("@@@@@@@@@@@@@@");
			//inconsistent State here
			System.out.println(getPlanVehicle(vehicleA));
			System.out.println(getPlanVehicle(vehicleB));

			System.out.println("@@@@@@@@@@@@@@");

			//works till here need to update times


		}
		else{
			System.out.println("vehicleA has an empty plan!!");
		}

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
		// First we need to remove the task A from the plan A
		ArrayList<Object> next = taskOrder.getValue(hash);

		int previousKey = getPreviousKey(hash);
		ArrayList<Object> currentRemovedTask= taskOrder.getValue(previousKey);
		taskOrder.addKeyValue(previousKey, next);	//update previous entry
		taskOrder.addKeyValue(hash, null);			//inconsistent need to be updated in addTask
		
		System.out.println(currentRemovedTask);
		capacities.updateCapacitiesAfterUpdate(vehicle, timeArray.getValue(createHash(currentRemovedTask)), currentAction.REMOVE, null);
		updateTimes(currentRemovedTask, currentAction.REMOVE, vehicle);
	}

	/**
	 * Adds an taskElement to a vehicle and updates the keys
	 * 
	 * @param task
	 * @param vehicle
	 * @param time
	 */
	private void addTaskToList(ArrayList<Object> taskObject, Vehicle vehicle, Integer time) {
		ArrayList<Object> taskAtPosition = taskOrder.getValue(vehicle.id());
		Integer previousKey = null;

		if(time > 0) {			//never enters here because append to the head of the plan.
			previousKey = getPreviousKey(createHash(taskObject));
		} else {
			previousKey = vehicle.id();
		}

		Integer key = createHash(taskObject);
		taskOrder.addKeyValue(key, taskAtPosition);

		taskOrder.addKeyValue(previousKey, taskObject);
		updateTimes(taskObject, currentAction.ADD, vehicle);
		capacities.updateCapacitiesAfterUpdate(vehicle, time, currentAction.ADD, ((Task)taskObject.get(0)).weight);
	}

	/**
	 * Creates the Hash value of a taskObject
	 * 
	 * @param taskObject
	 * @return hash
	 */
	private Integer createHash(ArrayList<Object> taskObject) {
		//System.out.println("we want hash of this "+taskObject);
		if(taskObject!=null){
		Task task = (Task) taskObject.get(0);
		actionStates taskAction = (actionStates)taskObject.get(1);

		return (task.toString() + taskAction).hashCode();
		}
		else
			return null;
	}

	/**
	 * returns the task hash on specified vehicle at the specified time
	 * 
	 * @param time
	 * @param vehicle
	 * @return
	 */
	private Integer getTaskByTimeAndVehicle(Integer time, Vehicle vehicle) {
		ArrayList<Integer> tasks = timeArray.getTaskSameTime(time);
		for(int i = 0; i < tasks.size(); i++) {
			Integer hash = tasks.get(i);

			if(vehicleArray.getValue(hash).equals(vehicle)) {
				return hash;
			} 
		}
		return null;
	}

	private Integer getPreviousKey(Integer hash) {
		Integer time = timeArray.getValue(hash);
		Vehicle vehicle = vehicleArray.getValue(hash);		//returns the vehicle where the task is
		Integer newHash;
		if(time > 0) {
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
	
	/**
	 * Checks if the time constraints are held given to tasks
	 * 
	 * @param taskAObject
	 * @param taskBObject
	 * @return boolean
	 */
	private boolean checkTimeConstraint(ArrayList<Object> taskAObject, ArrayList<Object> taskBObject) {
		actionStates complementActionA = getComplementaryAction((actionStates) taskAObject.get(1));
		actionStates complementActionB = getComplementaryAction((actionStates) taskBObject.get(1));

		Integer hashAReal = createHash(taskAObject);
		Integer hashBReal = createHash(taskBObject);
		Integer hashACheck = createHash(createTaskObject((Task) taskAObject.get(0), complementActionA));
		Integer hashBCheck = createHash(createTaskObject((Task) taskAObject.get(0), complementActionB));
		
		if(taskAObject.get(1).equals(actionStates.PICKUP) && taskBObject.get(1).equals(actionStates.DELIVER)) {
			if(timeArray.getValue(hashBCheck) < timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) > timeArray.getValue(hashACheck)) {
				return true;
			}
		} else if(taskAObject.get(1).equals(actionStates.DELIVER) && taskBObject.get(1).equals(actionStates.DELIVER)) {
			if(timeArray.getValue(hashBCheck) < timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) < timeArray.getValue(hashACheck)) {
				return true;
			}
		} else if(taskAObject.get(1).equals(actionStates.PICKUP) && taskBObject.get(1).equals(actionStates.PICKUP)) {
			if(timeArray.getValue(hashBCheck) > timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) > timeArray.getValue(hashACheck)) {
				return true;
			}
		} else {
			if(timeArray.getValue(hashBCheck) > timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) < timeArray.getValue(hashACheck)) {
				return true;
			}
		}
		
		return false;
	}
	
	private ArrayList<Object> createTaskObject(Task task, actionStates action) {
		ArrayList<Object> taskObject = new ArrayList<Object>();
		taskObject.add(task);
		taskObject.add(action);
		
		return taskObject;
	}
	
	private actionStates getComplementaryAction(actionStates action) {
		if(action == actionStates.DELIVER) {
			return actionStates.PICKUP;
		} else {
			return actionStates.DELIVER;
		}
	}

	/**
	 * update time for remove and add . AAA: in remove set temporarily the time of the removed element to -1 (illegal value)
	 * 
	 * @param currentTask
	 * @param action
	 * @param v
	 */
	private void updateTimes(ArrayList<Object> currentTask, currentAction action, Vehicle v){
		if(action.equals(currentAction.REMOVE)){
			//this part will be useful only if we decide to remove a task not from the top but from a random position
			int key= createHash(currentTask);
			int currentTaskTime= timeArray.getValue(key);
			timeArray.addKeyValue(key, -1);					//setting to illegal value
			Integer hashNextTask= getTaskByTimeAndVehicle(currentTaskTime+1 , v);
			//
			while(hashNextTask!=null){
				//update time
				//System.out.println("updating time for: "+ taskOrder.getValue(hashNextTask));
				timeArray.addKeyValue(hashNextTask, timeArray.getValue(hashNextTask)-1);
				hashNextTask= createHash(taskOrder.getValue(hashNextTask));
			}	
		}
		else{
			//this part will be useful only if we decide to append the task to a random position
			Integer key= v.id();
			ArrayList<Object> currentValue= taskOrder.getValue(key);
			boolean foundPrevious= false;
			Integer currentTaskTime= null;
			while(!foundPrevious && currentValue!= null){
				if(currentValue.equals(currentTask)){
					if(key.equals(v.id()))
						currentTaskTime=-1;
					else
						currentTaskTime= timeArray.getValue(key);
					foundPrevious= true;
				}
				else{
					key= createHash(currentValue);		//key should be !=NULL otherwise something went wrong
					currentValue= taskOrder.getValue(key);
				}
			}
			//
			Integer hashNextTask= createHash(currentTask);

			while(hashNextTask!=null){
				//update time
				currentTaskTime ++;
				timeArray.addKeyValue(hashNextTask, currentTaskTime);
				hashNextTask= createHash(taskOrder.getValue(hashNextTask));
				
			}	
		}
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

	public ArrayList<Task> getPlanVehicle(Vehicle v){
		ArrayList<Task> vehiclePlan= new ArrayList<Task>();			//when get the plan test with .isEmpty() not with .equals(null)
		int key= v.id();

		while(taskOrder.getValue(key)!= null){
			//if(((actionStates)nT.getValue(key).get(1)).equals(actionStates.PICKUP)){		//in the plan just need to save one of the two action. It matters the task
			vehiclePlan.add((Task)taskOrder.getValue(key).get(0));
			key= (taskOrder.getValue(key).get(0).toString()+ (actionStates)taskOrder.getValue(key).get(1)).hashCode();
			//}
		}
		return vehiclePlan;
	}

}
