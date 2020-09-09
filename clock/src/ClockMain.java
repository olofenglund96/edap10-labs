import clock.AlarmClockEmulator;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;
import clock.io.ClockOutput;

public class ClockMain {
    private static ClockAlarm alarmThread;
	
    public static void main(String[] args) throws InterruptedException {
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput  in  = emulator.getInput();
        ClockOutput out = emulator.getOutput();
        ClockMonitor monitor = new ClockMonitor();
        
        out.displayTime(monitor.getHour(), monitor.getMinute(), monitor.getSecond());   // arbitrary time: just an example
       
        clockHandler(in, out, monitor);

        while (true) {
        	in.getSemaphore().acquire();
            UserInput userInput = in.getUserInput();
            int choice = userInput.getChoice();
            int h = userInput.getHours();
            int m = userInput.getMinutes();
            int s = userInput.getSeconds();
            
            handleUserChoice(choice, monitor, out, h, m, s);
            
            System.out.println("choice=" + choice + " h=" + h + " m=" + m + " s=" + s);
        }
    }

	private static int incrementWithCap(int val, int cap) {
    	return (val + 1) % cap;
    }
  
    private static void clockHandler(ClockInput in, ClockOutput out, ClockMonitor monitor) {
		Thread t = new Thread(() -> {
	    	try {
	    		long t0 = System.currentTimeMillis();
	    		int steps = 0;
		    	while(true) {
		    		int hour = monitor.getHour();
		    		int minute = monitor.getMinute();
		    		int second = monitor.getSecond();
		    		
		    		out.displayTime(hour, minute, second);
		    		
		    		second = incrementWithCap(second, 60);
		    		
		    		if(second == 0) {
		    			minute = incrementWithCap(minute, 60);
		    			
		    			if(minute == 0) {
		    				hour = incrementWithCap(hour, 24);
		    			}
		    		}
		    		
		    		monitor.setTime(hour, minute, second);
		    		
		    		
		    		long sleepTime = t0 + 1000*(steps+1);
		    		long currTime = System.currentTimeMillis();
		    		
					Thread.sleep(sleepTime - currTime);
					
					if (monitor.shouldRingAlarm()) {
		    			alarmThread = new ClockAlarm(out);
		    			alarmThread.start();
		    			monitor.setAlarmRinging(true);
		    		}
					
					steps++;
		    	}
		    	
	    	} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		t.start();
    }
    
    private static void handleUserChoice(int choice, ClockMonitor monitor, ClockOutput out, int h, int m, int s) throws InterruptedException {
    	if (choice > 0) {
    		if (monitor.isAlarmRinging()) {
    			System.out.println("Cancelling alarm");
    			alarmThread.interrupt();
    			monitor.setAlarmRinging(false);
    			return;
    		}
    		
    		if (choice == 1) {
                monitor.setTime(h, m, s);
    		} else if(choice == 2) {
    			monitor.setAlarmTime(h,  m, s);
    		} else {
    			out.setAlarmIndicator(monitor.toggleAlarmState());
    		}
    	}
    }
}
