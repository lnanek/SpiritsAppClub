package club.spiritsapp;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class VarietalsPrefs {
	
	private static String TAG = VarietalsPrefs.class.getSimpleName();

	private static final String TYPES_KEY = VarietalsPrefs.class.getName() + ".TYPES_KEY";

	private static final String VARIETALS_KEY = VarietalsPrefs.class.getName() + ".VARIETALS_KEY";
	
	private final SharedPreferences prefs;
	
	public VarietalsPrefs(final Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public Set<String> getChosenTypes() {
		final Set<String> chosenTypes = prefs.getStringSet(TYPES_KEY, new HashSet<String>());
		Log.i(TAG, "getChosenTypes returning: " + chosenTypes);
		return chosenTypes;
	}
	
	public void setChosenTypes(final Set<String> types) {
		final Editor edit = prefs.edit();
		edit.putStringSet(TYPES_KEY, types);
		edit.commit();
	}

	public Set<String> getChosenVarietals() {
		final Set<String> chosenVarietals = prefs.getStringSet(VARIETALS_KEY, new HashSet<String>());
		Log.i(TAG, "getChosenVarietals returning: " + chosenVarietals);
		return chosenVarietals;
	}
	
	public void setChosenVarietals(final Set<String> varietals) {
		final Editor edit = prefs.edit();
		edit.putStringSet(VARIETALS_KEY, varietals);
		edit.commit();
	}
	
}
