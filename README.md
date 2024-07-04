Asia Pacific Airport 
You have been tasked to simulate the operations of the airport. 
*******************************Basic Requirements****************************** 
• There is only 1 runway for all planes to land and depart. 
• There can only be 3 airplanes on the airport grounds, including the runway. This is to 
ensure that the aircraft does not collide with another aircraft on the runway or gates. 
• Once an aircraft obtains permission to land, it should land on the runway, coast to the 
assigned gate, dock to the gate, allow passengers to disembark, refill supplies and fuel, 
receive new passengers, undock, coast to the assigned runway and take-off.  
• Each step should take some time.  
• As the airport is small, there is no waiting area on the ground for the planes to wait for a 
gate to become available.

These events should happen concurrently: - Passengers disembarking/embarking from the 3 gates. - Refill supplies and cleaning of aircraft 
As there is only 1 refuelling truck, this event should happen exclusively: - Refuelling of aircraft 
A congested scenario should be simulated where 2 planes are waiting to land while the 2 gates 
are occupied, and a 3rd plane comes in with fuel shortage, requiring emergency landing. 
State your assumptions and how you will implement them. 
The Statistics 
At the end of the simulation, i.e., when all planes have left the airport, the ATC manager 
should do some sanity checks of the airport and print out some statistics on the run. The result 
of the sanity checks must be printed. You must   
• Check that all gates are indeed empty.  
• Print out statistics on  - Maximum/Average/Minimum waiting time for a plane. - Number of planes served/Passengers boarded. 
Page 3 of 5 
Concurrent Programming - CT074-3-2                
Asia Pacific University of Technology & Innovation      
Level 2 
Deliverables: 
For this exercise, you are to model the ATC scenario and design a Java program to simulate 
activity for the airport: 
• Altogether, 6 planes should try to land at the airport. 
• Use a random number generator, so a new airplane arrives every 0, 1, or 2 seconds. 
(This might be accomplished by an appropriate statement sleep (rand.nextInt(2000)); 
• Assume each plane can accommodate maximum 50 passengers. 
• Assume passengers are always ready to embark and disembark the terminal (i.e., no 
capacity issues inside the passenger terminals)
