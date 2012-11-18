package template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskSet;

public class Initialization {
	private nextTask nT;
	private timeClass timeM;
	private vehicleClass vehicleM;
	private capacityClass capacitiesC;

	public Initialization(TaskSet taskDist, List<Vehicle> vehicleList){
		actionStates[] actions= {actionStates.PICKUP, actionStates.DELIVER};
		nT = new nextTask();
		timeM = new timeClass();
		vehicleM = new vehicleClass();
		capacitiesC= new capacityClass(vehicleList);


		//copy iterator to List because can not go back and forth on an iterator!!!
		Iterator<Task> taskIter= taskDist.iterator();
		List<Task> TaskSet = new ArrayList<Task>();
		while (taskIter.hasNext())
			TaskSet.add(taskIter.next());
		//
		int t_idx= 0;
		Vehicle lastVehicle = null;
		Integer lastKey = null;
		
		//random percentage
		double sum=0;
		ArrayList<Double> fillPercentage= new ArrayList<Double>();
		double maxValue= 1/(double)vehicleList.size();
		double minValue= maxValue/2;
		for(int i=0; i< vehicleList.size()-1; i++){ 
			fillPercentage.add(minValue + (Math.random() * (maxValue - minValue)));
			sum+=fillPercentage.get(i);
		}
		fillPercentage.add(1-sum);
		//System.out.println(fillPercentage);
		int last_idx=0;

		for(int i=0; i< vehicleList.size(); i++){
			System.out.println("vehicle:"+ vehicleList.get(i).id());
			int currentVehicleCapacity= vehicleList.get(i).capacity();

			int time= 0;
			last_idx= t_idx;

			while (t_idx<TaskSet.size() && TaskSet.get(t_idx).weight<= currentVehicleCapacity && ((float)(t_idx-last_idx)/(float)TaskSet.size())<fillPercentage.get(i)){
				System.out.println("adding task: "+ TaskSet.get(t_idx)+ " to vehicle "+ vehicleList.get(i).id());
				//adding the entry vehicle in nextT 
				if(!nT.checkKey(vehicleList.get(i).id())){
					ArrayList<Object> firstTaskAction= new ArrayList<Object>();
					firstTaskAction.add(TaskSet.get(t_idx));
					firstTaskAction.add(actionStates.PICKUP);
					nT.addKeyValue(vehicleList.get(i).id(), firstTaskAction);
				}
				//updating the previous entry in nextT
				if(lastVehicle!= null && !lastKey.equals(null)  && vehicleList.get(i).equals(lastVehicle)){
					ArrayList<Object> taskAction= new ArrayList<Object>();
					taskAction.add(TaskSet.get(t_idx));
					taskAction.add(actionStates.PICKUP);
					nT.addKeyValue(lastKey, taskAction);
				}
				//tryShare-= TaskSet.get(t_idx).weight;
				//currentVehicleCapacity-= TaskSet.get(t_idx).weight;
				for(int j=0; j< actions.length; j++){
					if(actions[j].equals(actionStates.PICKUP)){
						currentVehicleCapacity-= TaskSet.get(t_idx).weight;
						ArrayList<Object> taskAction= new ArrayList<Object>();
						taskAction.add(TaskSet.get(t_idx));
						taskAction.add(actionStates.DELIVER);
						//System.out.println("adding link:"+ TaskSet.get(t_idx).toString()+ actions[j]+" to " + taskAction);
						nT.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), taskAction);
					}
					else{
						currentVehicleCapacity+= TaskSet.get(t_idx).weight;
						lastKey= (TaskSet.get(t_idx).toString()+ actions[j]).hashCode();		//save key to retrieve it on the next loop
						nT.addKeyValue(lastKey, null);
					}
					//updating capacities
					capacitiesC.addCapacitySequentially(vehicleList.get(i).id(), currentVehicleCapacity);

					//adding entry in timeM
					//System.out.println("adding task "+ TaskSet.get(t_idx)+ " at time "+ time);
					timeM.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), time);
					time++;
					//adding entry in vehicleM
					vehicleM.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), vehicleList.get(i));
				}
				lastVehicle= vehicleList.get(i);				//save vehicle to compare to the next currentVehicle on the next loop
				t_idx++;
			}
			if(!nT.checkKey(vehicleList.get(i).id())){
				nT.addKeyValue(vehicleList.get(i).id(), null);
			}
		}
		capacitiesC.printCapacities();
		//System.out.println(nT.getNextTask());
	}

	public nextTask getNextTask() {
		return nT;
	}

	public timeClass getTimeArray() {
		return timeM;
	}

	public vehicleClass getVehicleArray() {
		return vehicleM;
	}
	public capacityClass getCapacities(){
		return capacitiesC;
	}

	public ArrayList<Task> getPlanVehicle(Vehicle v){
		ArrayList<Task> vehiclePlan= new ArrayList<Task>();			//when get the plan test with .isEmpty() not with .equals(null)
		int key= v.id();

		while(nT.getValue(key)!= null){
			//if(((actionStates)nT.getValue(key).get(1)).equals(actionStates.PICKUP)){		//in the plan just need to save one of the two action. It matters the task
			vehiclePlan.add((Task)nT.getValue(key).get(0));
			key= (nT.getValue(key).get(0).toString()+ (actionStates)nT.getValue(key).get(1)).hashCode();
			//}
		}
		return vehiclePlan;
	}

	//just  a debug function to make sure everything works
	public ArrayList<Integer> getTimeVehicle(Vehicle v){
		ArrayList<Integer> timeV= new ArrayList<Integer>();

		ArrayList<Object> startValue = nT.getValue(v.id());
		if(startValue!=null){
			//generate key from value
			int key= (((Task)startValue.get(0)).toString()+ (actionStates)startValue.get(1)).hashCode();
			timeV.add(timeM.getValue(key));
			while(nT.getValue(key)!=null){

				ArrayList<Object> nextValue= nT.getValue(key);
				//System.out.println(nextValue);
				key= (((Task)nextValue.get(0)).toString()+ (actionStates)nextValue.get(1)).hashCode();	

				timeV.add(timeM.getValue(key));
			}
		}
		return timeV;
	}

}
