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
	public capacityClass capacities;
	private List<Vehicle> vehicleList;

	private static boolean bias = true;
	private static boolean randPickUp = true;
	
	/**
	 * Constructor for a new localSearchNode
	 * 
	 * @param _taskOrder
	 * @param _timeArray
	 * @param _vehicleArray
	 * @param cc
	 * @param _vehicleList
	 */
	public localSearchNode(nextTask _taskOrder, timeClass _timeArray, vehicleClass _vehicleArray, capacityClass cc, List<Vehicle> _vehicleList) {
		taskOrder = new nextTask(_taskOrder.getNextT());
		timeArray = new timeClass(_timeArray.getTimeM());
		vehicleArray = new vehicleClass(_vehicleArray.getVehicleM());
		capacities= new capacityClass(_vehicleList, cc.getCapacities());
		vehicleList = _vehicleList;

		this.getCost();
	}

	/**
	 * Creates new neighbour solutions
	 * 
	 * @return
	 */
	public localSearchNode chooseNeighbours() {
		Random generator = new Random();
		int vehicleId = generator.nextInt(vehicleList.size());
		Vehicle choosenVehicle = vehicleList.get(vehicleId);

		ArrayList<localSearchNode> neighbours = new ArrayList<localSearchNode>();
		// Change vehicle
		for(int i = 0; i < vehicleList.size(); i++) {
			if(i != vehicleId) {
				ArrayList<Object> taskObject = taskOrder.getValue(vehicleId);
				localSearchNode newSolution = changingVehicle(taskObject, choosenVehicle, vehicleList.get(i));

				if(newSolution != null) {
					neighbours.add(newSolution);
				}
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
		if(bestNeighbour == null){
			bestNeighbour = this;
		}
		return bestNeighbour;
	}

	/**
	 * Change vehicle of a task (task with action Delivery & Pickup)
	 * 
	 * @param taskCar1
	 * @param taskCar2
	 * @return
	 */

	public localSearchNode changingVehicle(ArrayList<Object> taskObject, Vehicle vehicleA, Vehicle vehicleB) {
		localSearchNode newSolution = null;
		if(this.getTaskOrder().getValue(vehicleA.id())!= null){
			newSolution= new localSearchNode(taskOrder, timeArray, vehicleArray, capacities, vehicleList);
			ArrayList<Object> firstTaskPickUpA = newSolution.getTaskOrder().getValue(vehicleA.id());
			Task task = (Task) firstTaskPickUpA.get(0);

			ArrayList<Object> firstTaskDeliverA = new ArrayList<Object>();
			firstTaskDeliverA.add(task);
			firstTaskDeliverA.add(actionStates.DELIVER);
			Integer deliverTaskHash = (task.toString() + actionStates.DELIVER).hashCode();
			
			newSolution.removeTaskFromList(deliverTaskHash, vehicleA);
			newSolution.removeTaskFromList(createHash(firstTaskPickUpA), vehicleA);

			// Put task at beginning of other vehicle (time 0)
			newSolution.addTaskToList(firstTaskDeliverA, vehicleB, 0);
			newSolution.addTaskToList(firstTaskPickUpA, vehicleB, 0);
			newSolution.updateVehicleArray(createHash(firstTaskDeliverA), createHash(firstTaskPickUpA), vehicleB);
			
			if(!checkOverallCapacity(newSolution, vehicleB)){
				newSolution= null;
			}
		}
		
		return newSolution;
	}

	/**
	 * After a tasks changes a vehicle, the vehicleArray needs to be updated.
	 * 
	 * @param hashFirstTaskDeliverA
	 * @param hashFirstTaskPickUpA
	 * @param newV
	 */
	private void updateVehicleArray(Integer hashFirstTaskDeliverA, Integer hashFirstTaskPickUpA, Vehicle newV){
		vehicleArray.addKeyValue(hashFirstTaskDeliverA, newV);
		vehicleArray.addKeyValue(hashFirstTaskPickUpA, newV);
	}

	
	/**
	 * Returns the number of tasks a vehicle is assigned to
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
	 * Change task order inside of a vehicle
	 * 
	 * @param taskObjectA
	 * @param taskObjectB
	 */
	private localSearchNode changeTaskOrder(ArrayList<Object> taskObjectA, ArrayList<Object> taskObjectB) {
		// check time constraint
		localSearchNode newSolution = null;

		Integer taskObjectAHash = createHash(taskObjectA);
		Integer taskObjectBHash = createHash(taskObjectB);
		Vehicle vehicle = vehicleArray.getValue(taskObjectAHash);
		Integer timeA = timeArray.getValue(taskObjectAHash);
		Integer timeB = timeArray.getValue(taskObjectBHash);

		if(vehicleArray.getValue(taskObjectAHash) != vehicleArray.getValue(taskObjectBHash)) {
			newSolution = null;
		} 
		if(checkTimeConstraint(taskObjectA, taskObjectB)) {
			newSolution= new localSearchNode(taskOrder, timeArray, vehicleArray, capacities, vehicleList);
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
			if(!newSolution.checkOverallCapacity(newSolution, vehicle)) {
				newSolution = null;
			}
		}
		return newSolution;
	}
	
	/**
	 * This method enforces the overall capacity constraint
	 * 
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
	 * Removes a taskObject from a vehicle and updates the keys. The removed element has as successor null (illegal value)
	 * 
	 * @param hash
	 * @param vehicle
	 */
	public void removeTaskFromList(Integer hash, Vehicle vehicle) {
		// First we need to remove the task A from the plan A
		ArrayList<Object> next = taskOrder.getValue(hash);

		int previousKey = getPreviousKey(hash);
		ArrayList<Object> currentRemovedTask= taskOrder.getValue(previousKey);
		taskOrder.addKeyValue(previousKey, next);		
		taskOrder.addKeyValue(hash, null);	

		capacities.updateCapacitiesAfterUpdate(vehicle, timeArray.getValue(createHash(currentRemovedTask)), currentAction.REMOVE, null, null);
		updateTimesRemoving(currentRemovedTask, vehicle);
	}

	/**
	 * Adds an taskElement to a vehicle and updates the chain
	 * 
	 * @param task
	 * @param vehicle
	 * @param time
	 */
	public void addTaskToList(ArrayList<Object> taskObject, Vehicle vehicle, Integer time) {
		ArrayList<Object> taskAtPosition= null;
		Integer previousKey = null;

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
		updateTimesAdding(taskObject, vehicle);
		capacities.updateCapacitiesAfterUpdate(vehicle, time, currentAction.ADD, ((Task)taskObject.get(0)).weight, ((actionStates)taskObject.get(1)));
	}

	/**
	 * Creates the Hash value of a taskObject
	 * 
	 * @param taskObject
	 * @return hash
	 */
	public static Integer createHash(ArrayList<Object> taskObject) {
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
	 * Giving a time and vehicle, the method returns the corresponding key (hash)
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

	/**
	 * Giving a hash, the method returns the hash of the previous element in the chain.
	 * 
	 * @param hash
	 * @return
	 */
	private Integer getPreviousKey(Integer hash) {
		Integer time = timeArray.getValue(hash);
		Vehicle vehicle = vehicleArray.getValue(hash);	
		Integer newHash;
		if(time > 0) {
			newHash = getHashByTimeAndVehicle(time-1, vehicle);
		} else {
			newHash = vehicle.id();
		}
		return newHash;
	}

	/**
	 * Method enforces the time constraint
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
	public static ArrayList<Object> createTaskObject(Task task, actionStates action) {
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
	 * Method updates the time in case of removing an element. Set temporarily the time of the removed element to -1 (illegal value)
	 * 
	 * @param currentTask
	 * @param action
	 * @param v
	 */
	private void updateTimesRemoving(ArrayList<Object> currentTask, Vehicle v) {
		int key= createHash(currentTask);
		int currentTaskTime= timeArray.getValue(key);
		timeArray.addKeyValue(key, -1);					
		Integer hashNextTask= getHashByTimeAndVehicle(currentTaskTime+1 , v);
		
		while(hashNextTask!=null){
			timeArray.addKeyValue(hashNextTask, timeArray.getValue(hashNextTask)-1);
			hashNextTask= createHash(taskOrder.getValue(hashNextTask));
		}
	}
	
	/**
	 * Method updates the time in case of adding an element.
	 * 
	 * @param currentTask
	 * @param action
	 * @param v
	 */
	private void updateTimesAdding(ArrayList<Object> currentTask, Vehicle v) {
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
				key= createHash(currentValue);		
				currentValue= taskOrder.getValue(key);
			}
		}
		Integer hashNextTask= createHash(currentTask);
		
		while(hashNextTask!=null){
			currentTaskTime ++;
			timeArray.addKeyValue(hashNextTask, currentTaskTime);
			hashNextTask= createHash(taskOrder.getValue(hashNextTask));

		}
	}

	/**
	 * Chooses the best neighbour. Uses stochastic hill climbing in case there is no neighbour
	 * with lower costs than the current solution.
	 * 
	 * @param neighbours
	 * @return bestNode
	 */
	private localSearchNode localChoice(ArrayList<localSearchNode> neighbours) {
		localSearchNode bestNode = null;
		localSearchNode bestNeighbour= null;

		double bCost= this.getCost();
		double bestNeigbourCost= Double.POSITIVE_INFINITY;

		for(int i = 0; i < neighbours.size(); i++) {
			localSearchNode solution = neighbours.get(i);
			double cost = solution.getCost();

			if( cost< bestNeigbourCost){
				bestNeighbour= solution;
				bestNeigbourCost= cost;
			}
			if(cost < bCost) { 
				bestNode = solution;
				bCost = cost;
			}
		}

		// Stochastic Hill Climbing. If all the nodes are worse than the current get a neighbour with a probability p or remain with you node
		// (note that they say to get the best neighbor but this leads to convergence too early and doesn' t allow the algorithm to escape of 
		// local minimum, so the index of the neighbour is random. You canget the best neighbour simply uncommenting the two lines in the if 
		// and commenting out the first two lines inside the if)
		if(!neighbours.isEmpty() && bestNode == null) {
			if(randomChoice()) {
				if(randPickUp){
					int idx= (int)(Math.random()* neighbours.size());
					return neighbours.get(idx);
				}
				else
					return bestNeighbour;
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
		int threshold = 3 ; 			
		if(number <= threshold) {
			return true;
		}
		return false;
	}

	/**
	 * Computes the cost of the current solution
	 * cost is equal to: Sum over all vehicles( (every vehicle to first task (pickup)) * cost) + Sum over all tasks(dist((task i, action), nextTask(task i, action)*cost)
	 * 
	 * @return
	 */
	public long getCost(boolean... real) {
		boolean realCost;
		if(real.length == 0)
			realCost= false;
		else
			realCost= true;

		long totalCost = 0;
		int numVehicleWithPlan= 0;

		for(int i = 0; i < vehicleList.size(); i++) {
			Vehicle vehicle = vehicleList.get(i);
			int vehicleId = vehicle.id();
			ArrayList<Object> taskObject = taskOrder.getValue(vehicleId);
			City fromCity = vehicle.getCurrentCity();
			City toCity = null;
			int costPerKm = vehicle.costPerKm();
			boolean planFound= false;

			while(taskObject != null) {
				planFound= true;
				Task task = (Task) taskObject.get(0);
				actionStates currentAction = (actionStates) taskObject.get(1);

				if(currentAction == actionStates.PICKUP) {
					toCity = task.pickupCity;
				} else {
					toCity = task.deliveryCity;
				}
				totalCost += fromCity.distanceTo(toCity) * costPerKm;

				// Get next task
				Integer hash = createHash(taskObject);
				taskObject = taskOrder.getValue(hash);
				fromCity = toCity;
			}
			if(planFound==true)
				numVehicleWithPlan++;
		}
		
		//bias to encourage to share tasks fairly
		if(realCost==false && bias==true){
			float maxProb= (float) .6;
			float bias= vehicleList.size()+1- numVehicleWithPlan;
			float percentage= (float) (Math.exp(bias)/Math.exp(vehicleList.size()))* maxProb;
			totalCost= (long) ((totalCost* percentage)+ totalCost);
		}
		return totalCost;
	}

	/**
	 * Used as a debug function. Returns the task plan (ordered) of a vehicle
	 * 
	 * @param v
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getPlanVehicle(Vehicle v){
		ArrayList<ArrayList<Object>> vehiclePlan= new ArrayList<ArrayList<Object>>();			//when get the plan test with .isEmpty() not with .equals(null)
		int key= v.id();

		while(taskOrder.getValue(key)!= null){
			vehiclePlan.add(taskOrder.getValue(key));
			key= (taskOrder.getValue(key).get(0).toString()+ (actionStates)taskOrder.getValue(key).get(1)).hashCode();
		}
		return vehiclePlan;
	}
	
	/**
	 * Used as a debug function. Returns the tasks of a vehicle with corresponding time.
	 * 
	 * @param v
	 * @return
	 */
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
	
	/**
	 * Getter method for the taskOrder
	 * @return
	 */
	public nextTask getTaskOrder() {
		return taskOrder;
	}
	

}
