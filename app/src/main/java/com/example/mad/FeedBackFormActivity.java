package com.example.mad;

import static com.example.mad.systeminfos.SystemOprations.isValidEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class FeedBackFormActivity extends AppCompatActivity {

    EditText emailAddressTxt,feedBackTxt;
    Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);
        initUD();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();

                String email = emailAddressTxt.getText().toString();
                String fb = feedBackTxt.getText().toString();
                if (!isValidEmail(email)) {
                    emailAddressTxt.setError("please provide valid email");
                    return;
                }
                else if(fb.length()<4){
                    feedBackTxt.setError("provide feedback");
                    return;
                }
                else{
                    Map<String, Object> feedback = new HashMap<>();
                    feedback.put("email",email);
                    feedback.put("FeedbackWord",fb);
                    feedback.put("Date",SystemOprations.curretDate());
                    firebaseAuthClass.saveToFireStore(feedback, "App_Feedback", SystemOprations.makeGUID().toString(), new FirebaseAuthClass.FirestoreCallback() {
                        @Override
                        public void onSuccess() {
                            SystemOprations.showMessage("Your feedback reported successful.","Submission feedback",FeedBackFormActivity.this,1);
                        }

                        @Override
                        public void onFailure(Exception error) {
                            SystemOprations.showMessage("Your feedback not reported." + error.getMessage(),"Submission feedback",FeedBackFormActivity.this,3);
                        }
                    });
                }
            }
        });
    }
    void initUD(){
        emailAddressTxt =(EditText) findViewById(R.id.txtEmail);
        feedBackTxt = (EditText) findViewById(R.id.txtFeedback);
        submitBtn  = (Button)findViewById(R.id.btnFbSubmit);
    }
}