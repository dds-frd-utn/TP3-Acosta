package dds.tp3.acosta;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnDatos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.pantPpal).setVisibility(View.GONE);
                findViewById(R.id.btnVolver).setVisibility(View.VISIBLE);
                findViewById(R.id.consulta).setVisibility(View.VISIBLE);
                TextView titulo = findViewById(R.id.titulo);
                titulo.setText("Datos Personales");
                new DatosPersonales().execute();

            }
        });

        findViewById(R.id.btnSaldo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.pantPpal).setVisibility(View.GONE);
                findViewById(R.id.btnVolver).setVisibility(View.VISIBLE);
                findViewById(R.id.consulta).setVisibility(View.VISIBLE);
                TextView titulo = findViewById(R.id.titulo);
                titulo.setText("Consultar Saldo");
                new ConsultarSaldoTask().execute();
            }
        });

        findViewById(R.id.btnVolver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btnVolver).setVisibility(View.GONE);
                findViewById(R.id.pantPpal).setVisibility(View.VISIBLE);
                findViewById(R.id.consulta).setVisibility(View.GONE);
                findViewById(R.id.tranferencias).setVisibility(View.GONE);
                findViewById(R.id.emitidas).setVisibility(View.GONE);
                findViewById(R.id.recibidas).setVisibility(View.GONE);
                findViewById(R.id.inversiones).setVisibility(View.GONE);
                TextView titulo = findViewById(R.id.titulo);
                titulo.setText("Home Banking - Banco AC");
            }
        });

        findViewById(R.id.btnTransferencias).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btnVolver).setVisibility(View.VISIBLE);
                findViewById(R.id.pantPpal).setVisibility(View.GONE);
                findViewById(R.id.tranferencias).setVisibility(View.VISIBLE);
                TextView titulo = findViewById(R.id.titulo);
                titulo.setText("Ultimas Transferencias");
                new TranferenciasEmitidasTask().execute();
            }
        });

        findViewById(R.id.btnEmitidas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.tranferencias).setVisibility(View.GONE);
                findViewById(R.id.emitidas).setVisibility(View.VISIBLE);
                TextView titulo = findViewById(R.id.titulo);
                titulo.setText("Transferencias Emitidas");
                new TranferenciasEmitidasTask().execute();
            }
        });

        findViewById(R.id.btnRecibidas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.tranferencias).setVisibility(View.GONE);
                findViewById(R.id.recibidas).setVisibility(View.VISIBLE);
                TextView titulo = findViewById(R.id.titulo);
                titulo.setText("Transferencias Recibidas");
                new TranferenciasRecibidasTask().execute();
            }
        });

        findViewById(R.id.btnInversiones).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btnVolver).setVisibility(View.VISIBLE);
                findViewById(R.id.pantPpal).setVisibility(View.GONE);
                findViewById(R.id.inversiones).setVisibility(View.VISIBLE);
                TextView titulo = findViewById(R.id.titulo);
                titulo.setText("Inversiones Realizadas");
                new InversionesTask().execute();
            }
        });
    }


    private class DatosPersonales extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            return RESTService.makeGetRequest("http://192.168.1.136:8080/acosta/rest/cliente/1");
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.length() > 0) {
                    JSONObject cliente = null;
                    try {
                        cliente = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        String nombre = cliente.getString("nombre");
                        Integer dni = cliente.getInt("dni");
                        TextView respuestaText = findViewById(R.id.respuestaText);
                        respuestaText.setText("Nombre: " + nombre + " - DNI: " + dni);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class ConsultarSaldoTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            result = RESTService.makeGetRequest("http://192.168.1.136:8080/acosta/rest/cuenta/1");

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.length() > 0) {
                    JSONObject cuenta = null;
                    try {
                        cuenta = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Integer saldo = cuenta.getInt("saldo");
                        TextView respuestaText = findViewById(R.id.respuestaText);
                        respuestaText.setText("Saldo disponible: " + saldo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class TranferenciasEmitidasTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
          return RESTService.makeGetRequest("http://192.168.1.136:8080/acosta/rest/transaccion/emisor/1");

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.length() > 0) {
                    JSONArray transacciones = null;
                    JSONObject transaccion = null;
                    try {
                        transacciones = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //fila1
                    try {
                        transaccion  = transacciones.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView destinatario1 = findViewById(R.id.destinatario1);
                        destinatario1.setText(String.valueOf(transaccion.getInt("idReceptor")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView monto1 = findViewById(R.id.monto1);
                        monto1.setText(String.valueOf(transaccion.getInt("monto")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class TranferenciasRecibidasTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RESTService.makeGetRequest("http://192.168.1.136:8080/acosta/rest/transaccion/receptor/1");

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.length() > 0) {
                    JSONArray transacciones = null;
                    JSONObject transaccion = null;
                    try {
                        transacciones = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //fila1
                    try {
                        transaccion  = transacciones.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView emisor1 = findViewById(R.id.emisor1);
                        emisor1.setText(String.valueOf(transaccion.getInt("idEmisor")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView monto1 = findViewById(R.id.montoRecibido1);
                        monto1.setText(String.valueOf(transaccion.getInt("monto")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //fila2
                    try {
                        transaccion  = transacciones.getJSONObject(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView emisor2 = findViewById(R.id.emisor2);
                        emisor2.setText(String.valueOf(transaccion.getInt("idEmisor")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView monto2 = findViewById(R.id.montoRecibido2);
                        monto2.setText(String.valueOf(transaccion.getInt("monto")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class InversionesTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RESTService.makeGetRequest("http://192.168.1.136:8080/acosta/rest/inversiones/cuenta/1");

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.length() > 0) {
                    JSONArray inversiones = null;
                    JSONObject inversion = null;
                    try {
                        inversiones = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //fila1
                    try {
                        inversion  = inversiones.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView bono1 = findViewById(R.id.bono1);
                        Integer idBono = inversion.getInt("idBono");
                        try {
                            if (idBono == 1) {
                                bono1.setText("ESFE2020");
                            }
                            if (idBono == 2) {
                                bono1.setText("ESFE2021");
                            }
                            if (idBono == 3) {
                                bono1.setText("ESFE2022");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView fecha1 = findViewById(R.id.fecha1);
                        String fechaAux = inversion.getString("fechaCreacion");
                        String fechaAux2 = fechaAux.substring(0,10);
                        fecha1.setText(fechaAux2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //fila2
                    try {
                        inversion  = inversiones.getJSONObject(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView bono2 = findViewById(R.id.bono2);
                        Integer idBono = inversion.getInt("idBono");
                        try {
                            if (idBono == 1) {
                                bono2.setText("ESFE2020");
                            }
                            if (idBono == 2) {
                                bono2.setText("ESFE2021");
                            }
                            if (idBono == 3) {
                                bono2.setText("ESFE2022");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        TextView fecha2 = findViewById(R.id.fecha2);
                        String fechaAux = inversion.getString("fechaCreacion");
                        String fechaAux2 = fechaAux.substring(0,10);
                        fecha2.setText(fechaAux2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
