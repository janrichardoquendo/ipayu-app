package ipayuv1.source.cfts.ipayu_v1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Reg_contact extends AppCompatActivity {

    Button btnNext;
    String fname,mname,sname,bday,gender;
    EditText email,phone,addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_contact);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("REGISTRATION");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#072183")));

        Intent getI = getIntent();

        fname = getI.getStringExtra("fname");
        mname = getI.getStringExtra("mname");
        sname = getI.getStringExtra("sname");
        bday = getI.getStringExtra("bday");
        gender = getI.getStringExtra("gender");



        btnNext = (Button)findViewById(R.id.btnNext);
        email = (EditText)findViewById(R.id.txtEmail);
        phone = (EditText)findViewById(R.id.txtPhone);
        addr = (EditText)findViewById(R.id.txtAddress);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Reg_contact.this, Reg_security.class);
                i.putExtra("fname",fname);
                i.putExtra("mname",mname);
                i.putExtra("sname",sname);
                i.putExtra("bday",bday);
                i.putExtra("gender",gender);
                i.putExtra("email",email.getText().toString());
                i.putExtra("phone",phone.getText().toString());
                i.putExtra("addr",addr.getText().toString());

                startActivity(i);
                //Reg_personal.this.finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch(item.getItemId()){
            case android.R.id.home:
                intent = new Intent(this, Reg_personal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                this.finish();
                // app icon in action bar clicked; go home
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
