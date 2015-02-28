package club.spiritsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SignInActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sign_in);
		
		findViewById(R.id.signInButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(SignInActivity.this, QuizActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
