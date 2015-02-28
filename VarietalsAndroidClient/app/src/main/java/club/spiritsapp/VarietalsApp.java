package club.spiritsapp;

import android.app.Application;

public class VarietalsApp extends Application {

	public static VarietalsApp instance;
	{
		instance = this;
	}
	
	public VarietalsPrefs prefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		prefs = new VarietalsPrefs(this);
	}	

}
