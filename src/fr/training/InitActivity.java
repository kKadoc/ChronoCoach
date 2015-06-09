package fr.training;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import fr.training.core.Conf;
import fr.training.core.Model;
import fr.training.core.Period;

public class InitActivity extends Activity implements OnClickListener {

    private Button bAdd;
    private Button bStart;
    private EditText etRotations;
    private LinearLayout llComposition;

    // dialog create
    private AlertDialog createDialog;
    private View dialogView;
    private EditText etDuration;
    private EditText etDescription;
    private RadioGroup rgColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_init);

	bAdd = (Button) findViewById(R.id.bAdd);
	bStart = (Button) findViewById(R.id.bStart);
	etRotations = (EditText) findViewById(R.id.etRotations);
	llComposition = (LinearLayout) findViewById(R.id.llComposition);

	bAdd.setOnClickListener(this);
	bStart.setOnClickListener(this);

	etRotations.setText(String.valueOf(Conf.REPETITIONS_DEFAULT));

	for (Period p : Model.periods) {
	    addPeriodLabel(p);
	}
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	case R.id.bAdd:
	    showDialogAdd();
	    break;
	case R.id.bStart:
	    Model.repetitions = Integer.parseInt(etRotations.getText().toString());

	    Intent intent = new Intent(this, RunActivity.class);
	    startActivity(intent);
	    break;
	}

    }

    private void showDialogAdd() {

	if (createDialog == null) {
	    AlertDialog.Builder b = new AlertDialog.Builder(this);

	    b.setTitle("Nouvelle période").setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    RadioButton rb = (RadioButton) dialogView.findViewById(rgColors.getCheckedRadioButtonId());
		    addPeriod(etDuration.getText().toString(), etDescription.getText().toString(), rb.getText().toString());
		}
	    });
	    b.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    dialog.dismiss();
		}
	    });

	    LayoutInflater i = getLayoutInflater();

	    dialogView = i.inflate(R.layout.dialog_create, null);

	    etDuration = (EditText) dialogView.findViewById(R.id.etDuration);
	    etDescription = (EditText) dialogView.findViewById(R.id.etDescription);
	    rgColors = (RadioGroup) dialogView.findViewById(R.id.rgColors);

	    b.setView(dialogView);
	    createDialog = b.create();
	}

	etDuration.setText("");
	etDescription.setText("");
	rgColors.check(R.id.rbBlue);

	createDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    public void addPeriod(String duration, String description, String color) {
	System.out.println(duration + " - " + description + " - " + color);

	Period p = new Period(Long.parseLong(duration), getColorIndex(color), description);
	Model.periods.add(p);

	addPeriodLabel(p);

    }

    private void addPeriodLabel(Period p) {
	TextView tv = new TextView(this);
	tv.setText(p.getDuration() + "s [" + Conf.colorIndexes[p.getStyleIndex()] + "]");
	llComposition.addView(tv);
    }

    private int getColorIndex(String color) {
	int i = 0;
	for (String c : Conf.colorIndexes) {
	    if (c.equals(color)) {
		return i;
	    }
	    i++;
	}

	return 0;
    }
}
