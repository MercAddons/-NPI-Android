package mer.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class saludoActivity extends AppCompatActivity {
    private TextView txtSaludo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saludo);
        txtSaludo=(TextView)findViewById(R.id.TxtSaludo);
        //Recuperar informacion del intent
        Bundle bundle=this.getIntent().getExtras();
        //Mensaje a mostrar
        txtSaludo.setText("Hola "+bundle.getString("NOMBRE"));
    }

}
