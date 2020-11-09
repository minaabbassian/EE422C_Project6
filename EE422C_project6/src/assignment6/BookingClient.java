/* MULTITHREADING <MyClass.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Mina Abbassian
 * mea2947
 * 16170
 * Slip days used: <0>
 * Fall 2020
 */


package assignment6;

import assignment6.Theater.Seat;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Thread;

public class BookingClient {
	//added private fields
	private Map<String, Integer> office;
	private List<Thread> currentThreads;
	private Theater theater;
	private int client = 1;
	private boolean printOnce = false;

    /**
     * BookingClient
     * Constructor for the BookingClient class
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */

    public BookingClient(Map<String, Integer> office, Theater theater) {
        // TODO: Implement this constructor
    	this.office = office;
    	this.theater = theater; 
    }

    /**
     * simulate
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
        // TODO: Implement this method
    	//create your threads
    	currentThreads = getThreads();
    	//start all the threads
    	for(Thread t : currentThreads) {
    		t.start();
    	}
    	
    	//return list of threads used in the simulation
        return currentThreads;
    }

    
    /**
     * Added!!
     * getThreads
     * Makes one thread for each box office 
     * @return list of threads used in the simulation, should have as 
     * 			many threads as there are box offices
     */
    private List<Thread> getThreads(){
    	ArrayList<Thread> threads = new ArrayList<Thread>();
    	
    	//thread for each box office
    	for(String s : office.keySet()) {
    		Task task = new Task(s);
    		Thread thread = new Thread(task);
    		threads.add(thread);
    	}
    	
    	//returns list of threads used in the simulation
    	return threads;
    }
    
    /**
     * main method
     * Must initialize the offices and theater with the same data as the example output 
     * Must call simulate()
     */
    public static void main(String[] args) {
        // TODO: Initialize test data to description
    	Map<String, Integer> hmap = new HashMap<String, Integer>();
    	
    	//initialize the offices with the same data as the example output
    	hmap.put("BX1", 3);
    	hmap.put("BX3", 3);
    	hmap.put("BX2", 4);
    	hmap.put("BX5", 3);
    	hmap.put("BX4", 3);
    	
    	//initialize the theater with the same data as the example output
    	Theater theater = new Theater(3, 5, "Ouija");
    	
    	BookingClient bc = new BookingClient(hmap, theater);
    	//must call simulate
    	bc.simulate();
    }
    
    /**
     * Added!!
     * Task class
     * This class is used for each box office thread to perform 
     *
     */
    private class Task implements Runnable {
    	//added private field
    	private String officeID; 
    	
    	/**
    	 * Task
    	 * Constructor for the Task class
    	 * @param ID the ID of the box office for the thread
    	 */
    	private Task(String ID) {
    		officeID = ID;
    	}
    	
    	/**
    	 * run
    	 * Runs the actual task of assigning each client in the box office a seat
    	 * Follows the pseudocode in the document that clients are processed according to
    	 * Synchronizes threads based off the theater access
    	 */
    	@Override
    	public void run() {
    		//gets the number of customers in line at the box office
    		int customersInLine = office.get(officeID);
    		while(customersInLine > 0) {
    			//synchronize it where only one thread can access theater at a time
    			synchronized(theater) {
    				//get the best available seat
    				Seat bestSeat = theater.bestAvailableSeat();
    				if(bestSeat != null) {
    					bestSeat.taken = true;
    					theater.printTicket(officeID, bestSeat, client);
    					client++;
    					customersInLine--;
    				}
    				//if no more seats in the theater then sold out
    				if(bestSeat == null) {
    					if(printOnce == false) {
    						System.out.println("Sorry, we are sold out!");
    						printOnce = true;
    					}
    					break;
    				}
    			}
    			
    			//Delays the print delay for readability
    			try {
    				Thread.sleep(theater.getPrintDelay());
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}
    	
    }
}





