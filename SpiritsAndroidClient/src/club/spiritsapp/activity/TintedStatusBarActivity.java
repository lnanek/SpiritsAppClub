package club.spiritsapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class TintedStatusBarActivity extends Activity {

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(this, true);
		}
		
	    // create our manager instance after the content view is set
	    SystemBarTintManager tintManager = new SystemBarTintManager(this);
	    // enable status bar tint
	    tintManager.setStatusBarTintEnabled(true);
	    // enable navigation bar tint
	    tintManager.setNavigationBarTintEnabled(true);
	    
	 // set a custom tint color for all system bars
	    tintManager.setTintColor(Color.parseColor("#6f0254"));
	}
	

	@TargetApi(19) 
	public static void setTranslucentStatus(final Activity activity, final boolean on) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
}
