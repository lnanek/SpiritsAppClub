package club.spiritsapp;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class VarietalsPrefs {

	private static final String TYPES_KEY = VarietalsPrefs.class.getName() + ".TYPES_KEY";
	
	private final SharedPreferences prefs;
	
	public VarietalsPrefs(final Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public Set<String> getChosenTypes() {
		return prefs.getStringSet(TYPES_KEY, new HashSet<String>());
	}
	
	public void setChoseTypes(final Set<String> types) {
		final Editor edit = prefs.edit();
		edit.putStringSet(TYPES_KEY, types);
		edit.commit();
	}

}
