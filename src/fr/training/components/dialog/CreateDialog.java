//package fr.training.components.dialog;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import fr.training.InitActivity;
//import fr.training.R;
//
//public class CreateDialog extends Dialog implements android.view.View.OnClickListener {
//
//    private InitActivity activity;
//
//    private EditText etDuration;
//    private EditText etDescription;
//    private RadioGroup rgColors;
//
//    private Button bCancel;
//    private Button bCreate;
//
//    public CreateDialog(InitActivity a) {
//	super(a);
//
//	this.activity = a;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//	requestWindowFeature(Window.FEATURE_NO_TITLE);
//	setContentView(R.layout.dialog_create);
//
//	etDuration = (EditText) findViewById(R.id.etDuration);
//	etDescription = (EditText) findViewById(R.id.etDescription);
//	rgColors = (RadioGroup) findViewById(R.id.rgColors);
//
//	bCancel = (Button) findViewById(R.id.bCancel);
//	bCreate = (Button) findViewById(R.id.bCreate);
//	bCancel.setOnClickListener(this);
//	bCreate.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//	switch (v.getId()) {
//	case R.id.bCreate:
//	    RadioButton rb = (RadioButton) findViewById(rgColors.getCheckedRadioButtonId());
//	    activity.addPeriod(etDuration.getText().toString(), etDescription.getText().toString(), rb.getText().toString());
//	    break;
//	default:
//	    break;
//	}
//	dismiss();
//    }
//
// }
