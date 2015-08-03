package com.ospinet.app;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;

public class Validation  {


	//*******************isvaild_Name***********
		public boolean Is_Valid_Person_Name(EditText edt) throws NumberFormatException {
			if (edt.getText().toString().length() == 0) {
				edt.requestFocus();
				edt.setError(Html
						.fromHtml("<font color='white'>FIELD CANNOT BE EMPTY</font>"));

			} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
				edt.requestFocus();
				edt.setError(Html
						.fromHtml("<font color='white'>ENTER ONLY ALPHABETICAL CHARACTER</font>"));
			}else{
				edt.setError(null);
				return true;
			}
			return false;
		}
		
	  //**************************isVaild_Mobile***************
		public boolean Is_Valid_MobileNo(EditText edt) throws NumberFormatException {
			if (edt.getText().toString().length() == 0){ 
				edt.requestFocus();
				edt.setError(Html
						.fromHtml("<font color='white'>FIELD CANNOT BE EMPTY</font>"));
			} else if (edt.getText().toString().length() != 10) {
				edt.requestFocus();
				edt.setError(Html
						.fromHtml("<font color='white'>ENTER 10 DIGIT NUMBER</font>"));
			} else{
				edt.setError(null);
				
				return true;
			}
			return false;
		}
		
		//*********************isVaild_Email*************
		public boolean Is_Valid_Email(EditText edt) throws NumberFormatException {
			if (edt.getText().toString().length() == 0) {
				edt.requestFocus();
				edt.setError(Html
						.fromHtml("<font color='white'>FIELD CANNOT BE EMPTY</font>"));
			}

			else if (!edt.getText().toString().matches("[a-zA-Z0-9+._%-+]{1,256}" + "@"+"[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "("
									+ "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}"
									+ ")+")) {
				edt.requestFocus();
				edt.setError(Html
						.fromHtml("<font color='white'>INVAILD EMAIL ADDRES</font>"));
			}else{
				edt.setError(null);
				return true;
			}
			return false;
			
		}
	//**********isVaild_Location//password****************
		public boolean Is_Valid_Empty(EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() == 0) {
			edt.requestFocus();
			edt.setError(Html
					.fromHtml("<font color='white'>FIELD CANNOT BE EMPTY</font>"));
		}else{
			edt.setError(null);
			return true;
		}
		return false;
		}
			
		
	//****************button validation************
public boolean Is_Valid_Empty_Button(Button but) throws NumberFormatException {
	if (but.getText().equals("Reg.Dt")) {
		but.requestFocus();
		but.setError(Html
				.fromHtml("<font color='white'>Please Select Date</font>"));
	}else{
		but.setError(null);
		return true;
	}
	return false;
	}
		
	}
	

		
