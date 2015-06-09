package fr.training;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.training.core.Conf;
import fr.training.core.Model;

public class RunActivity extends Activity implements OnClickListener {

    private int cptRepetitions = 1;

    private int cptPeriod = 0;
    private long chrono;
    private boolean pause = false;
    private boolean running = false;

    private Chrono chronoThread;
    private SoundPool sound;
    private HashMap<String, Integer> soundPoolMap;

    private TextView tvChrono;
    private TextView tvDesc;
    private TextView tvAfter;
    private TextView tvRotation;

    private LinearLayout llCurrent;
    private LinearLayout llAfter;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_run);

	// pour ne pas mettre en veille
	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	tvChrono = (TextView) findViewById(R.id.tvChrono);
	tvDesc = (TextView) findViewById(R.id.tvDesc);
	tvAfter = (TextView) findViewById(R.id.tvAfter);
	tvRotation = (TextView) findViewById(R.id.tvRotation);

	llCurrent = (LinearLayout) findViewById(R.id.llCurrent);
	llAfter = (LinearLayout) findViewById(R.id.llAfter);

	llAfter.setBackgroundColor(Color.parseColor(Conf.colors[0]));
	llCurrent.setBackgroundColor(Color.parseColor(Conf.colors[0]));

	findViewById(R.id.mainLayout).setOnClickListener(this);

	this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

	sound = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
	soundPoolMap = new HashMap<String, Integer>(3);

	soundPoolMap.put("beep1", sound.load(this, R.raw.beep08b, 1));
	soundPoolMap.put("beep2", sound.load(this, R.raw.beep09, 2));
    }

    @Override
    public void onClick(View v) {
	System.out.println("which : " + v.getId());

	if (!running) {
	    startChrono();
	} else {
	    stopChrono();
	}
    }

    private void startChrono() {
	System.out.println("chrono : " + chrono + " - index : " + cptPeriod);
	// nouvelle rotation
	// on initialise
	if (!pause) {
	    if (cptPeriod >= Model.periods.size()) {
		cptPeriod = 0;
		cptRepetitions++;

		if (cptRepetitions > Model.repetitions) {
		    cptRepetitions = 1;
		    return;
		}
	    }

	    tvRotation.setText("répétition " + cptRepetitions + "/" + Model.repetitions);

	    chrono = Model.periods.get(cptPeriod).getDuration();
	    tvDesc.setText(Model.periods.get(cptPeriod).getDescription());
	    llCurrent.setBackgroundColor(Color.parseColor(Conf.colors[Model.periods.get(cptPeriod).getStyleIndex()]));

	    if (cptPeriod < Model.periods.size() - 1) {
		tvAfter.setText(Model.periods.get(cptPeriod + 1).getDuration() + " sec.");
		llAfter.setBackgroundColor(Color.parseColor(Conf.colors[Model.periods.get(cptPeriod + 1).getStyleIndex()]));
	    } else {
		tvAfter.setText("");
		llAfter.setBackgroundColor(Color.parseColor(Conf.colors[0]));
	    }
	}

	pause = false;
	running = true;

	// on lance/relance le chrono
	chronoThread = new Chrono(chrono * 1000 - 1, 200);
	chronoThread.start();
    }

    private void stopChrono() {
	chronoThread.cancel();
	running = false;
	pause = true;
    }

    @Override
    protected void onDestroy() {
	chronoThread.cancel();

	System.out.println("onDestroy");
	super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	System.out.println("onSave");

	savedInstanceState.putBoolean("pause", true);
	savedInstanceState.putBoolean("running", running);
	savedInstanceState.putInt("cptPeriod", cptPeriod);
	savedInstanceState.putInt("cptRepetitions", cptRepetitions);
	savedInstanceState.putLong("chrono", chrono);

	// Always call the superclass so it can save the view hierarchy state
	super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
	// Always call the superclass so it can restore the view hierarchy
	super.onRestoreInstanceState(savedInstanceState);

	// Restore state members from saved instance
	System.out.println("onRestore");

	System.out.println("pause : " + pause);
	System.out.println("running : " + running);

	pause = savedInstanceState.getBoolean("pause");
	running = savedInstanceState.getBoolean("running");
	cptPeriod = savedInstanceState.getInt("cptPeriod");
	cptRepetitions = savedInstanceState.getInt("cptRepetitions");
	chrono = savedInstanceState.getLong("chrono");

	if (running) {
	    startChrono();
	}
    }

    /**
     * Classe interne pour la gestion du compte à rebour. Classe interne car
     * très étroitement liée à l'activity
     * 
     * @author gtremouilles
     *
     */
    private class Chrono extends CountDownTimer {

	public Chrono(long millisInFuture, long countDownInterval) {
	    super(millisInFuture, countDownInterval);
	}

	@Override
	public void onFinish() {
	    tvChrono.setText(chrono + "");
	    running = false;
	    cptPeriod++;

	    startChrono();
	}

	@Override
	public void onTick(long millisUntilFinished) {
	    long newChrono = millisUntilFinished / 1000;
	    if (chrono - newChrono > 0) {
		if (newChrono < 6 && newChrono > 0) {
		    playSound(soundPoolMap.get("beep1"));
		}
		if (newChrono <= 0) {
		    playSound(soundPoolMap.get("beep2"));
		}
	    }
	    chrono = newChrono;
	    tvChrono.setText(chrono + "");
	}

    }

    private void playSound(Integer id) {
	AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	float volume = actVolume / maxVolume;

	sound.play(id, volume, volume, 1, 0, 1f);
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

}
