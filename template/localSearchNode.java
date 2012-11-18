package template;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.topology.Topology.City;


public class localSearchNode {

	private nextTask taskOrder;
	private timeClass timeArray;
	private vehicleClass vehicleArray;
	private capacityClass capacities;
	private List<Vehicle> vehicleList;

	public localSearchNode(nextTask _taskOrder, timeClass _timeArray, vehicleClass _vehicleArray, capacityClass cc, List<Vehicle> _vehicleList) {
		taskOrder = new nextTask(_taskOrder.getNextT());
		timeArray = new timeClass(_timeArray.getTimeM());
		vehicleArray = new vehicleClass(_vehicleArray.getVehicleM());
		capacities= new capacityClass(_vehicleList, cc.getCapacities());
		vehicleList = _vehicleList;
	}

	public localSearchNode chooseNeighbours() {
		Random generator = new Random();
		int vehicleId = 0;//generator.nextInt(vehicleList.size());
		Vehicle choosenVehicle = vehicleList.get(vehicleId);

		ArrayList<localSearchNode> neighbours = new ArrayList<localSearchNode>();

		// Change vehicle
		for(int i = 0; i < vehicleList.size(); i++) {
			if(i != vehicleId) {
				ArrayList<Object> taskObject = taskOrder.getValue(vehicleId);				
				neighbours.add(changingVehicle(taskObject, choosenVehicle, vehicleList.get(i)));
			}
		}
		// Change task order
		int length = getTaskArrayLength(choosenVehicle);
		for(int i = 0; i < length; i++) {
			for(int j = i+1; j < length; j++) {
				ArrayList<Object> taskObjectA = getTaskByTimeAndVehicle(i, choosenVehicle);
				ArrayList<Object> taskObjectB = getTaskByTimeAndVehicle(j, choosenVehicle);
				if(taskObjectA == null || taskObjectB == null) {
					continue;
				}
				localSearchNode newSolution = changeTaskOrder(taskObjectA, taskObjectB);
				if(newSolution != null) {
					neighbours.add(newSolution);
				}
			}
		}

		localSearchNode bestNeighbour = localChoice(neighbours);
		// return best one
		return bestNeighbour;
	}

	/**
	 * Gives you the number of tasks a vehicle is assigned to
	 * 
	 * @param vehicle
	 * @return int
	 */
	public int getTaskArrayLength(Vehicle vehicle) {
		ArrayList<Object> taskObject = taskOrder.getValue(vehicle.id());
		int count = 9;
		while(taskObject != null) {
			Integer taskHash = createHash(taskObject);
			taskObject = taskOrder.getValue(taskHash);
			count++;
		}

		return count;
	}

	/**
	 * Exchange two task (PICKUP & DELIVER) between
	 * two vehicles
	 * 
	 * @param taskCar1
	 * @param taskCar2
	 * @return
	 */

	public localSearchNode changingVehicle(ArrayList<Object> taskObject, Vehicle vehicleA, Vehicle vehicleB) {


		localSearchNode newSolution = new localSearchNode(taskOrder, timeArray, vehicleArray, capacities, vehicleList);

		// We put taskObject from vehicle A to vehicle B
		if(newSolution.getTaskOrder().getValue(vehicleA.id())!= null){
			ArrayList<Object> firstTaskPickUpA = newSolution.getTaskOrder().getValue(vehicleA.id());
			Task task = (Task) firstTaskPickUpA.get(0);

			ArrayList<Object> firstTaskDeliverA = new ArrayList<Object>();
			firstTaskDeliverA.add(task);
			firstTaskDeliverA.add(actionStates.DELIVER);
			
			Integer deliverTaskHash = (task.toString() + actionStates.DELIVER).hashCode();
			
			//print out the plan before we change vehicles
			System.out.println("*******************");
			System.out.println("plan of vehicle before changing "+vehicleA.id() +" is "+newSolution.getPlanVehicle(vehicleA));
			System.out.println("plan of vehicle before changing"+vehicleB.id() +" is "+newSolution.getPlanVehicle(vehicleB));
			newSolution.capacities.printCapacities();
			System.out.println("Time for tasks of vehicle "+ vehicleA.id()+ " is "+ newSolution.getTimeOfPlan(vehicleA));
			System.out.println("Time for tasks of vehicle "+ vehicleB.id()+ " is "+ newSolution.getTimeOfPlan(vehicleB));

			
			newSolution.removeTaskFromList(deliverTaskHash, vehicleA);
			newSolution.removeTaskFromList(createHash(firstTaskPickUpA), vehicleA);

			// Put delivery task at beginning (time 0)
			newSolution.addTaskToList(firstTaskDeliverA, vehicleB, 0);

			// Put task at beginning (time 0)
			newSolution.addTaskToList(firstTaskPickUpA, vehicleB, 0);
			newSolution.capacities.printCapacities();
			
			System.out.println("plan of vehicle after changing"+vehicleA.id() +" is "+newSolution.getPlanVehicle(vehicleA));
			System.out.println("plan of vehicle after changing"+vehicleB.id() +" is "+newSolution.getPlanVehicle(vehicleB));
			capacities.printCapacities();
			System.out.println("Time for tasks of vehicle "+ vehicleA.id()+ " is "+ newSolution.getTimeOfPlan(vehicleA));
			System.out.println("Time for tasks of vehicle "+ vehicleB.id()+ " is "+ newSolution.getTimeOfPlan(vehicleB));
			System.out.println("*******************");

		}
		else{
			System.out.println("vehicleA has an empty plan!!");
		}
		return newSolution;
	}

	private nextTask getTaskOrder() {
		return taskOrder;
	}

	/**
	 * Exchange two tasks
	 * 
	 * @param taskObjectA
	 * @param taskObjectB
	 */

	private localSearchNode changeTaskOrder(ArrayList<Object> taskObjectA, ArrayList<Object> taskObjectB) {
		// check time constraint

		
		Integer taskObjectAHash = createHash(taskObjectA);
		Integer taskObjectBHash = createHash(taskObjectB);
		Vehicle vehicle = vehicleArray.getValue(taskObjectAHash);
		Integer timeA = timeArray.getValue(taskObjectAHash);
		Integer timeB = timeArray.getValue(taskObjectBHash);

		if(vehicleArray.getValue(taskObjectAHash) != vehicleArray.getValue(taskObjectBHash)) {
			return null;
		}

		if(checkTimeConstraint(taskObjectA, taskObjectB)) {
			System.out.println("-------------");
			System.out.println("TaskObjectA: "+taskObjectA);
			System.out.println("TaskObjectB: "+taskObjectB);
			System.out.println("Time A: "+timeA);
			System.out.println("Time B: "+timeB);
			localSearchNode newSolution = new localSearchNode(taskOrder, timeArray, vehicleArray, capacities, vehicleList);
			System.out.println("Before changing order: ");
			System.out.println(newSolution.getPlanVehicle(vehicle));
			
			newSolution.removeTaskFromList(taskObjectAHash, vehicle);
			newSolution.removeTaskFromList(taskObjectBHash, vehicle);
			if(timeB< timeA){
				newSolution.addTaskToList(taskObjectA, vehicle, timeB);
				newSolution.addTaskToList(taskObjectB, vehicle, timeA);
			}
			else{
				newSolution.addTaskToList(taskObjectB, vehicle, timeA);
				newSolution.addTaskToList(taskObjectA, vehicle, timeB);
			}
			System.out.println("After changing order: ");
			System.out.println(newSolution.getPlanVehicle(vehicle));
			System.out.println("------------");

			if(!newSolution.checkOverallCapacity(newSolution, vehicle)) {
				return null;
			}

			return newSolution;
		}

		return null;
	}
	/**
	 * check if capacity never drops below zero
	 * @param newSolution, v
	 * @return return true if the capacity never drops below zero
	 */
	public boolean checkOverallCapacity(localSearchNode newSolution, Vehicle v){
		if(newSolution.capacities.checkIfBelowZero(v))
			return false;
		else
			return true;
	}

	/**
	 * Removes a taskObject from a vehicle and updates the keys
	 * 
	 * @param hash
	 * @param vehicle
	 */
	public void removeTaskFromList(Integer hash, Vehicle vehicle) {
		// First we need to remove the task A from the plan A
		ArrayList<Object> next = taskOrder.getValue(hash);

		int previousKey = getPreviousKey(hash);
		ArrayList<Object> currentRemovedTask= taskOrder.getValue(previousKey);
		taskOrder.addKeyValue(previousKey, next);		//update previous entry
		taskOrder.addKeyValue(hash, null);				//inconsistent need to be updated in addTask

		System.out.println("removing task:"+ currentRemovedTask);
		System.out.println("plan is: "+ getPlanVehicle(vehicle));
		capacities.updateCapacitiesAfterUpdate(vehicle, timeArray.getValue(createHash(currentRemovedTask)), currentAction.REMOVE, null);
		updateTimes(currentRemovedTask, currentAction.REMOVE, vehicle);
		System.out.println("time is: "+ getTimeOfPlan(vehicle));

	}

	/**
	 * Adds an taskElement to a vehicle and updates the keys
	 * 
	 * @param task
	 * @param vehicle
	 * @param time
	 */
	public void addTaskToList(ArrayList<Object> taskObject, Vehicle vehicle, Integer time) {
		ArrayList<Object> taskAtPosition= null;
		Integer previousKey = null;
		System.out.println("Adding task: "+ taskObject);

		if(time > 0) {
			previousKey= getHashByTimeAndVehicle(time-1, vehicle);
			taskAtPosition = taskOrder.getValue(previousKey);
		} else {
			previousKey = vehicle.id();
			taskAtPosition = taskOrder.getValue(vehicle.id());
		}
		Integer key = createHash(taskObject);
		taskOrder.addKeyValue(key, taskAtPosition);
		taskOrder.addKeyValue(previousKey, taskObject);
		System.out.println("current plan:"+ getPlanVehicle(vehicle));
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
	private Integer getHashByTimeAndVehicle(Integer time, Vehicle vehicle) {
		ArrayList<Integer> tasks = timeArray.getTaskSameTime(time);
		for(int i = 0; i < tasks.size(); i++) {
			Integer hash = tasks.get(i);

			if(vehicleArray.getValue(hash).equals(vehicle)) {
				return hash;
			} 
		}
		return null;
	}

	/**
	 * returns the task hash on specified vehicle at the specified time
	 * 
	 * @param time
	 * @param vehicle
	 * @return
	 */
	private ArrayList<Object> getTaskByTimeAndVehicle(Integer time, Vehicle vehicle) {
		ArrayList<Integer> tasks = timeArray.getTaskSameTime(time);
		for(int i = 0; i < tasks.size(); i++) {
			Integer hash = tasks.get(i);

			if(vehicleArray.getValue(hash).equals(vehicle)) {
				Integer previousKey = getPreviousKey(hash);
				return taskOrder.getValue(previousKey);
			} 
		}
		return null;
	}

	private Integer getPreviousKey(Integer hash) {
		Integer time = timeArray.getValue(hash);
		Vehicle vehicle = vehicleArray.getValue(hash);		//returns the vehicle where the task is
		Integer newHash;
		if(time > 0) {
			newHash = getHashByTimeAndVehicle(time-1, vehicle);
		} else {
			newHash = vehicle.id();
		}

		return newHash;
	}

	/**
	 * Checks if the time constraints are held given to tasks
	 * 
	 * @param taskAObject
	 * @param taskBObject
	 * @return boolean
	 */
	private boolean checkTimeConstraint(ArrayList<Object> taskAObject, ArrayList<Object> taskBObject) {
		if(taskAObject.get(0).equals(taskBObject.get(0))) {
			return false;
		}
		actionStates complementActionA = getComplementaryAction((actionStates) taskAObject.get(1));
		actionStates complementActionB = getComplementaryAction((actionStates) taskBObject.get(1));

		Integer hashAReal = createHash(taskAObject);
		Integer hashBReal = createHash(taskBObject);
		Integer hashACheck = createHash(createTaskObject((Task) taskAObject.get(0), complementActionA));
		Integer hashBCheck = createHash(createTaskObject((Task) taskBObject.get(0), complementActionB));
		System.out.println("Time A Real: "+timeArray.getValue(hashAReal)+" "+taskAObject.get(1));
		System.out.println("Time B Real: "+timeArray.getValue(hashBReal)+" "+taskBObject.get(1));
		System.out.println("Time A Checker: "+timeArray.getValue(hashACheck)+" "+complementActionA);
		System.out.println("Time B Checker: "+timeArray.getValue(hashBCheck)+" "+complementActionB);

		if(taskAObject.get(1).equals(actionStates.PICKUP) && taskBObject.get(1).equals(actionStates.DELIVER)) {
			if(timeArray.getValue(hashBCheck) < timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) < timeArray.getValue(hashACheck)) {
				
				return true;
			}
		} else if(taskAObject.get(1).equals(actionStates.DELIVER) && taskBObject.get(1).equals(actionStates.DELIVER)) {
			if(timeArray.getValue(hashBCheck) < timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) > timeArray.getValue(hashACheck)) {
				return true;
			}
		} else if(taskAObject.get(1).equals(actionStates.PICKUP) && taskBObject.get(1).equals(actionStates.PICKUP)) {
			if(timeArray.getValue(hashBCheck) > timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) < timeArray.getValue(hashACheck)) {
				return true;
			}
		} else {
			if(timeArray.getValue(hashBCheck) > timeArray.getValue(hashAReal) && timeArray.getValue(hashBReal) > timeArray.getValue(hashACheck)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Creates a taskObject based on a task and an action
	 * 
	 * @param task
	 * @param action
	 * @return ArrayList<Object>
	 */
	private ArrayList<Object> createTaskObject(Task task, actionStates action) {
		ArrayList<Object> taskObject = new ArrayList<Object>();
		taskObject.add(task);
		taskObject.add(action);

		return taskObject;
	}

	/**
	 * Returns the contrary action
	 * @param action
	 * @return actionStates
	 */
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
			Integer hashNextTask= getHashByTimeAndVehicle(currentTaskTime+1 , v);
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

	/**
	 * Chooses the best neighbour. Uses stochastic hill climbing in case there is no neighbour
	 * with heigher costs than the current solution.
	 * 
	 * @param neighbours
	 * @return bestNode
	 */
	private localSearchNode localChoice(ArrayList<localSearchNode> neighbours) {
		localSearchNode bestNode = null;
		long bestCost = 0;

		for(int i = 0; i < neighbours.size(); i++) {
			localSearchNode solution = neighbours.get(i);
			long cost = solution.getCost();

			if(cost > bestCost) {
				bestNode = solution;
			}
		}

		// Stochastic Hill Climbing
		if(bestNode.getCost() < this.getCost()) {
			if(randomChoice()) {
				return bestNode;
			} else {
				return this;
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
		int threshold = 4; // 0.4 probabilty of changing

		if(number <= threshold) {
			return true;
		}

		return false;
	}

	/**
	 * Computes the cost of the current solution
	 * 
	 * @return
	 */
	public long getCost() {
		long totalCost = 0;

		for(int i = 0; i < vehicleList.size(); i++) {
			Vehicle vehicle = vehicleList.get(i);
			int vehicleId = vehicle.id();
			ArrayList<Object> taskObject = taskOrder.getValue(vehicleId);
			City fromCity = vehicle.getCurrentCity();
			City toCity = null;
			int costPerKm = vehicle.costPerKm();

			while(taskObject != null) {
				long reward = 0;

				Task task = (Task) taskObject.get(0);
				actionStates currentAction = (actionStates) taskObject.get(1);

				if(currentAction == actionStates.PICKUP) {
					toCity = task.pickupCity;
				} else {
					toCity = task.deliveryCity;
					reward = task.reward;
				}

				totalCost += -fromCity.distanceTo(toCity) * costPerKm + reward;

				// Get next task
				Integer hash = createHash(taskObject);
				taskObject = taskOrder.getValue(hash);
				fromCity = toCity;
			}


		}

		return totalCost;

		//cost is equal to: Sum over all vehicules( (every vehicule to first task (pickup)) * cost) + Sum over all tasks(dist((task i, action), nextTask(task i, action)*cost)
	}

	public ArrayList<ArrayList<Object>> getPlanVehicle(Vehicle v){
		ArrayList<ArrayList<Object>> vehiclePlan= new ArrayList<ArrayList<Object>>();			//when get the plan test with .isEmpty() not with .equals(null)
		int key= v.id();

		while(taskOrder.getValue(key)!= null){
			//if(((actionStates)nT.getValue(key).get(1)).equals(actionStates.PICKUP)){		//in the plan just need to save one of the two action. It matters the task
			vehiclePlan.add(taskOrder.getValue(key));
			key= (taskOrder.getValue(key).get(0).toString()+ (actionStates)taskOrder.getValue(key).get(1)).hashCode();
			//}
		}
		return vehiclePlan;
	}
	public ArrayList<ArrayList<Integer>> getTimeOfPlan(Vehicle v){
		ArrayList<ArrayList<Integer>> time= new ArrayList<ArrayList<Integer>>();
		int key= v.id();
		while(taskOrder.getValue(key)!= null){
			ArrayList<Object> value= taskOrder.getValue(key);
			ArrayList<Integer> hashTime = new ArrayList<Integer>();
			hashTime.add(createHash(value));
			hashTime.add(timeArray.getValue(createHash(value)));
			time.add(hashTime);
			key= createHash(value);
		}
		return time;
	}

}
