import clock.io.ClockOutput;

public class ClockAlarm extends Thread {
	private ClockOutput out;
	private boolean shouldContinue = true;
	
	public ClockAlarm(ClockOutput out) {
		this.out = out;
	}
	
	private long correctSleepTime(long t0, int i) {
		long sleepTime = t0 + 1000*(i+1);
		long currTime = System.currentTimeMillis();
		
		return sleepTime - currTime;
	}
	
	public void run() {
		try {
			long t0 = System.currentTimeMillis();
			for (int i = 0; i < 20; i++) {
				if (!shouldContinue) {
					break;
				}
				out.alarm();
				Thread.sleep(correctSleepTime(t0, i));
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void interrupt() {
		shouldContinue = false;
	}
}
