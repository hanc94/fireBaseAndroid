package com.example.testfirebaseandroid;

//Paquetes a usar

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.*;

//Actividad principal
public class MainActivity extends AppCompatActivity {

    //Project name
    private final String project="xxxx";

    //Secret
    private final String secret="xxxxx";

    //Referencias a los objetos de la interfaz gráfica de usuario
    public EditText outputtext;
    public EditText refertext;
    public ProgressBar progressBar;
    Firebase myFireBase;
    public Button button;
    public Button button1;
    public double c=1;
    public int b;
    public String reference="reference";
    public String[] ref = new String[1];

    //Temporizador para la tarea periódica
    Timer timer;

    //Re-implementación del método onCreate, se invoca cuando se crea la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llama el método onCreate de la clase madre
        super.onCreate(savedInstanceState);
        //Visualiza la interfaz creada en XML, la clase R es creada durante al compilación
        // y a través de ella es posible acceder a los widgets creados en XML
        setContentView(R.layout.activity_main);

        //Se crea el objeto tipo Firebase
        myFireBase=new Firebase(project,secret);

        //Se obtienen las referencias a los widgets creados en XML
        outputtext = (EditText) findViewById(R.id.editText);
        refertext = (EditText) findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.button1);
        button1 = (Button) findViewById(R.id.button2);
        ref[0]=String.valueOf(c);

        //Se deja de mostrar la barra de progreso
        progressBar.setVisibility(View.GONE);

        //Objeto tipo Timer para leer desde thingspeak periódicamente
        timer = new Timer();


    }

    public void changeReference(View v){

            AccessFireBase task=new AccessFireBase();
            b=0;
            task.execute("");

    }
    public void downReference(View v){
        AccessFireBase task=new AccessFireBase();
        b=1;
        task.execute("");
    }

    //Callback del evento OnClick del botón que lee desde la nube (se asocia en los recursos en XML )
    public void getCloudinfo(View view) {


        //Para ejecutar periódicamente
        timer.scheduleAtFixedRate(
                //Se crea un objeto timer task
                new TimerTask() {
                    //Se reimplementa run
                    @Override
                    public void run() {

                        final String response;

                        JSONObject jobj;
                        //Método para acceder a los widgets de la UI desde una tarea en segundo plano
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }

                        );


                        //Name of document to update
                        String document="doubleint";

                        //Se lee desde firebase
                        response = myFireBase.read(document);




                        try {
                            jobj = new JSONObject(response);
                            //Se modifica el cuadro de texto con la respuesta de ThingSpeak
                            //y detiene la barra de progreso
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            outputtext.setText(response);


                                        }
                                    }

                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                },
                //Demás argumentos de scheduleAtFixedRate (ejecución cada 5 segundos)
                0,
                5000
        );

        //Se inhabilita el botón
        button.setEnabled(false);


    }

    private class AccessFireBase extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            //ref[0]= String.valueOf(1);
            switch (b){
                case 0:
                    c++;
                    break;

                case 1:
                    c--;
                    break;
            }
            ref[0]=String.valueOf(c);
            myFireBase.write(reference, ref[0]);

                 //Se modifica el cuadro de texto con la respuesta de ThingSpeak
                //y detiene la barra de progreso
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                refertext.setText(ref[0]);
                            }
                        }

                );




            return "";
        }

        @Override
        protected void onPostExecute(String result) {
         //   TextView txt = (TextView) findViewById(R.id.output);
           // txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
