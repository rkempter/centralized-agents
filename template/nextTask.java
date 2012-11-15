//for nextT map the keys are: for task+ action = (task.toString()+ "P (or D)").hashCode(). 
//for vehicle the key is vehicle.id() (because the object vehicle does change)

package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskSet;

public class nextTask {

	private Map<Object,ArrayList<Object>> nextT = new HashMap<Object, ArrayList<Object>>();
	private Map<Object,Integer> timeM = new HashMap<Object, Integer>();
	private Map<Object,Vehicle> vehicleM =new HashMap<Object, Vehicle>();

	public nextTask(TaskSet taskDist, List<Vehicle> vehicleList){
		String[] actions= {"P", "D"};

		//copy iterator to List because can not go back and forth on an iterator!!!
		Iterator<Task> taskIter= taskDist.iterator();
		List<Task> TaskSet = new ArrayList<Task>();
		while (taskIter.hasNext())
			TaskSet.add(taskIter.next());
		//
		int t_idx= 0;
		Vehicle lastVehicle = null;
		Integer lastKey = null;

		for(int i=0; i< vehicleList.size(); i++){
			System.out.println("vehicle:"+ vehicleList.get(i).id());
			int currentVehicleCapacity= vehicleList.get(i).capacity();
			int time= 1;

			while (t_idx<TaskSet.size() && TaskSet.get(t_idx).weight<= currentVehicleCapacity){	
				System.out.println("adding task: "+ TaskSet.get(t_idx)+ " to vehicle "+ vehicleList.get(i).id());
				//adding the entry vehicle in nextT 
				if(!nextT.containsKey(vehicleList.get(i).id())){
					ArrayList<Object> firstTaskAction= new ArrayList<Object>();
					firstTaskAction.add(TaskSet.get(t_idx));
					firstTaskAction.add(actionStates.PICKUP);
					nextT.put(vehicleList.get(i).id(), firstTaskAction);
				}
				//updating the previous entry in nextT
				if(lastVehicle!= null && !lastKey.equals(null)  && vehicleList.get(i).equals(lastVehicle)){
					ArrayList<Object> taskAction= new ArrayList<Object>();
					taskAction.add(TaskSet.get(t_idx));
					taskAction.add(actionStates.PICKUP);
					nextT.put(lastKey, taskAction);
				}				
				currentVehicleCapacity-= TaskSet.get(t_idx).weight;
				for(int j=0; j< actions.length; j++){
					if(actions[j].equals(actionStates.PICKUP)){
						ArrayList<Object> taskAction= new ArrayList<Object>();
						taskAction.add(TaskSet.get(t_idx));
						taskAction.add(actionStates.DELIVER);
						//System.out.println("adding link:"+ TaskSet.get(t_idx).toString()+ actions[j]+" to " + taskAction);
						nextT.put((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), taskAction);
					}
					else{
						lastKey= (TaskSet.get(t_idx).toString()+ actions[j]).hashCode();		//save key to retrieve it on the next loop
						nextT.put(lastKey, null);
					}
					//adding entry in timeM
					timeM.put((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), time);
					time++;
					//adding entry in vehicleM
					vehicleM.put((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), vehicleList.get(i));
				}
				lastVehicle= vehicleList.get(i);				//save vehicle to compare to the next currentVehicle on the next loop
				t_idx++;
			}
			if(!nextT.containsKey(vehicleList.get(i).id())){
				nextT.put(vehicleList.get(i).id(), null);
			}
		}
		System.out.println(nextT);
	}

	/**
	 * By converting current Task into a Hashcode and use
	 * as the key, we find the next task
	 * 
	 * @param task
	 * @return next
	 */
	public ArrayList<Object> getNextT(ArrayList<Object> task) {
		Task taskElement = (Task) task.get(0);
		Object action = task.get(1);
		Integer key = (taskElement.toString() + action).hashCode();
		
		ArrayList<Object> next = nextT.get(key);
		
		return next;
	}

	public Map<Object, Integer> getTimeM() {
		return timeM;
	}

	public Map<Object, Vehicle> getVehicleM() {
		return vehicleM;
	}

	public ArrayList<Task> getPlanVehicle(Vehicle v){
		ArrayList<Task> vehiclePlan= new ArrayList<Task>();			//when get the plan test with .isEmpty() not with .equals(null)
		int key= v.id();

		while(nextT.get(key) != null){
			if(((String)nextT.get(key).get(1)).equals(actionStates.PICKUP)){		//in the plan just need to save one of the two action. It matters the task
				vehiclePlan.add((Task)nextT.get(key).get(0));
				key= (nextT.get(key).get(0).toString()+ actionStates.DELIVER).hashCode();
			}
		}
		return vehiclePlan;
	}
	
	
	
	
	//just  a debug function to make sure everything works
	public ArrayList<Integer> getTimeVehicle(Vehicle v){
		ArrayList<Integer> timeV= new ArrayList<Integer>();

		ArrayList<Object> startValue = nextT.get(v.id());
		if(startValue!=null){
			//generate key from value
			int key= (((Task)startValue.get(0)).toString()+ (String)startValue.get(1)).hashCode();
			timeV.add(timeM.get(key));
			while(nextT.get(key)!=null){

				ArrayList<Object> nextValue= nextT.get(key);
				//System.out.println(nextValue);
				key= (((Task)nextValue.get(0)).toString()+ (String)nextValue.get(1)).hashCode();	

				timeV.add(timeM.get(key));
			}
		}
		return timeV;
	}

	


}


