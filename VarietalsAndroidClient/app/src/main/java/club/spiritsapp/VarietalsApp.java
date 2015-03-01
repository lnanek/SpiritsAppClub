package club.spiritsapp;

import android.app.Application;
import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.apache.http.params.HttpConnectionParams;

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


        //Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();
        //Fabric.with(this, crashlytics);

        TwitterAuthConfig authConfig =
                new TwitterAuthConfig("1kT85mIcqtwEVpyfiYYwcYylh",
                        "UjQxPPju95A0veb2iWOkhD7kWee3lPON05wSdaBa0efXfplQud");
        //Fabric.with(this, new TwitterCore(authConfig));

        Fabric.with(this, new Twitter(authConfig));

	}	

}
