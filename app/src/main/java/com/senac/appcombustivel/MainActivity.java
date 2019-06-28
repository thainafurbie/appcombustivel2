package com.senac.appcombustivel;




import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.senac.appcombustivel.modelo.AppCombustivel;
import com.senac.appcombustivel.webservice.Api;
import com.senac.appcombustivel.webservice.RequestHandler;


public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;


    EditText editTextId;
    EditText editTextKm_Atual;
    EditText editTextTipo;
    EditText editTextLts_Combustivel;
    EditText editTextConsumo_Kml;
    Button botaosalvar;
    Button botaolista;
    ProgressBar progressBar;
    ListView listView;
    List<AppCombustivel> appcombustivelList;

    Boolean isUpdating = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.barraProgresso);
        listView = findViewById(R.id.listViewConsumo);
        editTextId = findViewById(R.id.editTextId);
        editTextKm_Atual = findViewById(R.id.editTextKm_Atual);
        editTextTipo = findViewById(R.id.editTextKm_Atual);
        editTextConsumo_Kml = findViewById(R.id.editTextConsumo_Kml);
        editTextLts_Combustivel = findViewById(R.id.editTextLts_Combustivel);
        botaosalvar = findViewById(R.id.botaosalvar);
        botaolista = findViewById(R.id.botaolista);

        appcombustivelList = new ArrayList<>();


        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdating){
                    updateAppCombustivel();
                } else {
                    //createAppCombustivel();
                }
            }
        });
        readAppCombustivel();
    }

    private void updateAppCombustivel() {
        String id = editTextId.getText().toString().trim();
        String km_atual = editTextKm_Atual.getText().toString().trim();
        String tipo = editTextTipo.getText().toString().trim();
        String lts_combustivel = editTextLts_Combustivel.getText().toString().trim();
        String consumo_kml= editTextConsumo_Kml.getText().toString().trim();

        if (TextUtils.isEmpty(km_atual)) {
            editTextKm_Atual.setError("Digite a kilometragem atual");
            editTextKm_Atual.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tipo)) {
            editTextTipo.setError("Digite o tipo do combustível");
            editTextTipo.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(lts_combustivel)) {
            editTextLts_Combustivel.setError("Digite o tipo do combustível");
            editTextLts_Combustivel.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("km_atual", km_atual);
        params.put("tipo", tipo);
        params.put("lts_combustivel", lts_combustivel);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_APPCOMBUSTIVEL, params, CODE_POST_REQUEST);
        request.execute();
        botaosalvar.setText("Salvar");
        editTextKm_Atual.setText("");
        editTextTipo.setText("");
        editTextLts_Combustivel.setText("");

        isUpdating = false;

    }

    private void readAppCombustivel() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_APPCOMBUSTIVEL, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void deleteAppCombustivel(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_APPCOMBUSTIVEL+id, null, CODE_GET_REQUEST);
    }

    private void refreshAppCombustivelList(JSONArray appcombustivel) throws JSONException {
        appcombustivelList.clear();
    }


    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class AppCombustivelAdapter extends ArrayAdapter<AppCombustivel> {
        List<AppCombustivel> appCombustivelList;

        public AppCombustivelAdapter(List<AppCombustivel> appcombustivelList) {
            super(MainActivity.this, R.layout.layout_appcombustivel_list, appcombustivelList);
            this.appCombustivelList = appcombustivelList;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_appcombustivel_list, null, true);

            TextView textViewConsumo = listViewItem.findViewById(R.id.textViewConsumo);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);
            TextView textViewAlterar = listViewItem.findViewById(R.id.textViewAlterar);

            final AppCombustivel appCombustivel = appCombustivelList.get(position);
            textViewConsumo.setText(appCombustivel.getKm_atual());
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Delete" + appCombustivel.getKm_atual())
                            .setMessage("Você quer realmente deletar?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAppCombustivel(appCombustivel.getId_abastecimento());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            textViewAlterar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isUpdating = true;
                    editTextId.setText(String.valueOf(appCombustivel.getId_abastecimento()));
                    editTextKm_Atual.setText(appCombustivel.getKm_atual());
                    editTextTipo.setText(appCombustivel.getTipo());
                    editTextLts_Combustivel.setText(appCombustivel.getLts_combustivel());
                }
            });

            return listViewItem;
        }


    }


}

