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
	
	//index of a client being processed
	private int cIndex = 1; 
	//holds all of the threads in this simulation
	private List<Thread> threadList; 
	//maps the box office IDs to the number of customers in line
	private Map<String, Integer> boxOfficeMap; 
	//variable to check whether the sold out statement gets printed only once
	private boolean soldOutPrinted = false;
	//the theater where the show is playing 
	private Theater oneTheater;
	
    /**
     * BookingClient
     * Constructor for the BookingClient class
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater theater) {
    	this.oneTheater = theater; 
    	this.boxOfficeMap = office;
    }

    /**
     * Added!! Helper method for simulate()
     * constructThreadList
     * For each box office, a thread is made 
     * @return a list holding all of the threads in the simulation, which 
     * 			includes one thread for each box office 
     */
    private List<Thread> constructThreadList(){
    	//create an ArrayList of threads
    	ArrayList<Thread> list = new ArrayList<Thread>();
    	
    	//one thread for each box office
    	for(String officeString : boxOfficeMap.keySet()) {
    		BoxOffice b = new BoxOffice(officeString);
    		//create one thread for each box office 
    		Thread oneThread = new Thread(b);
    		list.add(oneThread);
    	}
    	
    	//returns list of threads used in the simulation
    	return list;
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
    	//create all of my threads in the simulation 
    	threadList = constructThreadList();
    	for(Thread oneThread : threadList) {
    		oneThread.start(); //start each of these thread
    	}
    	
    	//return list of threads used in the simulation
        return threadList;
    }
    
    /**
     * main method
     * Must initialize the offices and theater with the same data as the example output 
     * Must call simulate()
     */
    public static void main(String[] args) {
    	//create a HashMap for the box office information 
    	Map<String, Integer> hashBoxOffices = new HashMap<String, Integer>();
    	
    	//initialize the offices with the same data as the example output
    	hashBoxOffices.put("BX1", 3);
    	hashBoxOffices.put("BX3", 3);
    	hashBoxOffices.put("BX2", 4);
    	hashBoxOffices.put("BX5", 3);
    	hashBoxOffices.put("BX4", 3);
    	
    	//initialize the theater with the same data as the example output
    	Theater t = new Theater(3, 5, "Ouija");
    	
    	//create one BookingClient 
    	BookingClient oneBookingClient = new BookingClient(hashBoxOffices, t);
    	
    	//call simulate
    	oneBookingClient.simulate();
    }
    
    /**
     * Added!!
     * BoxOffice class
     * This class is used to perform each box office thread
     */
    private class BoxOffice implements Runnable {
    	//added private field
    	private String boxOffId; 
    	
    	/**
    	 * BoxOffice
    	 * Constructor for the BoxOffice class
    	 * @param name the Box Office ID of the office corresponding to the thread
    	 */
    	private BoxOffice(String name) {
    		boxOffId = name;
    	}
    	
    	/**
    	 * run
    	 * Synchronizes the box office threads 
    	 * Processes each client at a box office and assigns each a seat according to the 
    	 *  pseudocode in the assignment 6 document 
    	 * 
    	 */
    	@Override
    	public void run() {
    		//gets the number of customers in line at the box office
    		int clientsWaiting = boxOfficeMap.get(boxOffId);
    		while(clientsWaiting > 0) {
    			//synchronization 
    			synchronized(oneTheater) { //theater is accessed by a single thread 
    				//get the best available seat 
    				Seat s = oneTheater.bestAvailableSeat();
    				
    				if(!(s == null)) {
    					//the seat is now occupied
    					s.occupied = true; 
    					//print the ticket
    					oneTheater.printTicket(boxOffId, s, cIndex);
    					
    					//////////////////TESTING////////////////////
    					//System.out.println(oneTheater.getSeatLog());
    					
    					clientsWaiting = clientsWaiting - 1;
    					//go to next client index
    					cIndex = cIndex + 1;
    				}
    				
    				//when there are no remaining seats
    				if(s == null) {
    					//print sold out if it has not already been printed 
    					if(!(soldOutPrinted)) {
    						soldOutPrinted = true;
    						System.out.println("Sorry, we are sold out!");
    					}
    					break;
    				}
    			}
    			
    			
    			//print each ticket sold to the console with a small delay for human readability
    			try {
    				//sleep for the print delay
    				Thread.sleep(oneTheater.getPrintDelay());
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}
    	
    }
}








