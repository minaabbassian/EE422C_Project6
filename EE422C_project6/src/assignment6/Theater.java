/* MULTITHREADING <Theater.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Mina Abbassian
 * mea2947
 * 16170
 * Slip days used: <0>
 * Fall 2020
 */
package assignment6;

import java.util.ArrayList;
import java.util.List;

public class Theater {
	
	//private fields added
	private int rows;
	private int seatsInRow;
	private String showName;
	private ArrayList<Seat> seatList;
	
    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log = new SalesLogs(); // Field in Theater class.

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Represents a seat in the theater A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;
        
        //added field whether a seat is occupied or not
        public boolean occupied;

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
            
            occupied = false; //initially, all seats are unoccupied
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        /**
         * toString
         * returns a string with the row letter appended with the seat number 
         */
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
    }

    // end of class Seat

    /**
     * Represents a paper ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;

        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol + showLine + eol + boxLine + eol + seatLine + eol + clientLine + eol + dashLine;

            return result;
        }
    }

    /**
     * SalesLogs are security wrappers around an ArrayList of Seats and one of
     * Tickets that cannot be altered, except for adding to them. getSeatLog returns
     * a copy of the internal ArrayList of Seats. getTicketLog returns a copy of the
     * internal ArrayList of Tickets.
     */
    static class SalesLogs {
        private ArrayList<Seat> seatLog;
        private ArrayList<Ticket> ticketLog;

        private SalesLogs() {
            seatLog = new ArrayList<Seat>();
            ticketLog = new ArrayList<Ticket>();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Seat> getSeatLog() {
            return (ArrayList<Seat>) seatLog.clone();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Ticket> getTicketLog() {
            return (ArrayList<Ticket>) ticketLog.clone();
        }

        public void addSeat(Seat s) { // call when seat is allocated
            seatLog.add(s);
        }

        public void addTicket(Ticket t) { // call when ticket is printed
            ticketLog.add(t);
        }

    } // end of class SeatLog

    /**
     * Theater
     * Constructor for the Theater class
     * @param numRows the number of rows N
     * @param seatsPerRow the number of seats in each row M
     * @param show the string holding the name of the show 
     */
    public Theater(int numRows, int seatsPerRow, String show) {
    	this.showName = show;
    	this.rows = numRows;
    	this.seatsInRow = seatsPerRow;
    	this.seatList = constructSeatList();
    }
    
    /**
     * bestAvailableSeat
     * Calculates the best seat not yet reserved
     * @return the best seat or null if theater is full
     */
    public Seat bestAvailableSeat() {
    	this.log.seatLog.clear();
    	
        for(Seat oneSeat : seatList) {
        	//return the first seat that is not yet reserved
        	if(!(oneSeat.occupied)) 
        		return oneSeat;
        	//add to the seatLog when a seat is allocated
        	else {
        		this.log.addSeat(oneSeat);
        	}
        }
        
        //return null if the theater is full
        return null;
    }

    /**
     * printTicket
     * Prints a ticket to the console for the client after they reserve a seat.
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        //return null if a box office failed to reserve the seat
    	if(!(seat.occupied)) 
    		return null;
    	
    	//create a new ticket to print
    	Ticket oneTicket = new Ticket(this.showName, boxOfficeId, seat, client);
    	//add to the ticketLog when a ticket is printed
    	this.log.addTicket(oneTicket); 
    	//print that ticket
    	System.out.println(oneTicket);
    	System.out.println("");
    	
    	/////////////TESTING/////////////
    	//System.out.println("TRANSACTION LOG");
		//System.out.println(getTransactionLog());

    	return oneTicket;
    }

    /**
     * getSeatLog
     * Lists all seats sold for this theater in order of purchase.
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        return this.log.seatLog;
    }

    /**
     * getTransactionLog
     * Lists all tickets sold for this theater in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
    	return this.log.ticketLog;
    }
    
    /**
     * Added!! Helper method for the Theater constructor 
     * constructSeatList
     * @return an ArrayList holding the all of the seats from best to worst 
     */
    private ArrayList<Seat> constructSeatList(){
    	//initialize an ArrayList of seats
    	ArrayList<Seat> list = new ArrayList<Seat>();
    	//go through all seats in the theater starting with the best seat
    	for(int j = 0; j < rows; j++) {
    		for(int i = 1; i <= seatsInRow; i++) {
    			//create a Seat
    			Seat oneSeat = new Seat(j, i);
    			//add the Seat to the list 
    			list.add(oneSeat);
    		}
    	}
    	return list;	
    }

}
