import java.util.concurrent.Semaphore;

public class ClockMonitor {
	private int hour = 15;
	private int minute = 2;
	private int second = 37;
	
	private int alarmHour = 0;
	private int alarmMinute = 0;
	private int alarmSecond = 0;
	
	private boolean alarmActive = false;
	private boolean alarmRinging = false;
	
	private Semaphore timeMutex = new Semaphore(1);
	private Semaphore alarmTimeMutex = new Semaphore(1);
	private Semaphore alarmMutex = new Semaphore(0);
	
	public int getHour() throws InterruptedException {
		timeMutex.acquire();
		int temp = hour;
		timeMutex.release();
		return temp;
	}
	
	public int getMinute() throws InterruptedException {
		timeMutex.acquire();
		int temp = minute;
		timeMutex.release();
		return temp;
	}
	
	public int getSecond() throws InterruptedException {
		timeMutex.acquire();
		int temp = second;
		timeMutex.release();
		return temp;
	}
	
	public int getAlarmHour() throws InterruptedException {
		alarmTimeMutex.acquire();
		int temp = alarmHour;
		alarmTimeMutex.release();
		return temp;
	}
	
	public int getAlarmMinute() throws InterruptedException {
		alarmTimeMutex.acquire();
		int temp = alarmMinute;
		alarmTimeMutex.release();
		return temp;
	}
	
	public int getAlarmSecond() throws InterruptedException {
		alarmTimeMutex.acquire();
		int temp = alarmSecond;
		alarmTimeMutex.release();
		return temp;
	}
	
	public void setTime(int hour, int minute, int second) throws InterruptedException {
		timeMutex.acquire();
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		timeMutex.release();
	}
	
	public void setAlarmTime(int hour, int minute, int second) throws InterruptedException {
		alarmTimeMutex.acquire();
		alarmHour = hour;
		alarmMinute = minute;
		alarmSecond = second;
		alarmTimeMutex.release();
	}
	
	public boolean toggleAlarmState() {
		alarmActive = !alarmActive;
		return alarmActive;
	}
	
	public boolean alarmIsActive() {
		return alarmActive;
	}
	
	public boolean isAlarmTime() {
		return alarmHour == hour && alarmMinute == minute && alarmSecond == second;
	}
	
	public boolean shouldRingAlarm() {
		return alarmIsActive() && isAlarmTime();
	}
	
	public void setAlarmRinging(boolean state) {
		alarmRinging = state;
	}
	
	public boolean isAlarmRinging() {
		return alarmRinging;
	}
	
	
}
