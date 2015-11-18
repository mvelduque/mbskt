package com.example.mvelduque.myapplication;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BuscaEquipoActivity extends AppCompatActivity implements
                                                    AdapterView.OnItemSelectedListener,
                                                    Fragment_List_Equipos.EquiposListener {

   // private TextView txtCategoria;
    private TextView txtNombreEquipo;
    private String strTipoEquipo;
    private Spinner spProvincias;
    private Spinner spLocalidades;
    private Spinner spCategorias;
    private List<String> list_Categorias;
    private Fragment_List_Equipos frg_lst_Equipos;
    private String p_ParametersUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_equipo);

        //Obtenemos los combos de provincias
        spProvincias = (Spinner) findViewById(R.id.sp_Provincias);
        spLocalidades = (Spinner) findViewById(R.id.sp_Localidades);
        spCategorias = (Spinner) findViewById(R.id.sp_Categorias);
        txtNombreEquipo = (TextView) findViewById(R.id.txt_NombreEquipo);

        get_Provincias();
        get_Categorias_basket();

        Bundle b = this.getIntent().getExtras();
        strTipoEquipo = b.getString("p_TipoEquipo");

        //Obtenemos el fragmento que carga la lista de equipos
        frg_lst_Equipos = (Fragment_List_Equipos)getSupportFragmentManager().findFragmentById(R.id.frg_Equipos);
        frg_lst_Equipos.setEquiposListener(this);

        setupText();



    }


    private void setupText() {
        txtNombreEquipo.addTextChangedListener(new TextWatcher() {
                                                   @Override
                                                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                   }

                                                   @Override
                                                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                   }

                                                   @Override
                                                   public void afterTextChanged(Editable s) {
                                                       if (txtNombreEquipo.getText().toString() != "") {
                                                           BuscaEquipo();
                                                       }

                                                   }
                                            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_busca_equipo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void get_Categorias_basket(){
        list_Categorias = new ArrayList<String>();
        spCategorias = (Spinner) this.findViewById(R.id.sp_Categorias);
        list_Categorias.add("PREBENJAMIN");
        list_Categorias.add("BENJAMIN");
        list_Categorias.add("ALEVIN");
        list_Categorias.add("INFANTIL");
        list_Categorias.add("CADETE");
        list_Categorias.add("JUNIOR");
        list_Categorias.add("SENIOR");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_Categorias);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategorias.setAdapter(adaptador);
        spCategorias.setOnItemSelectedListener(this);
    }

    private void get_Provincias() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.provincias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spProvincias.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        spProvincias.setOnItemSelectedListener(this);
        spLocalidades.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_Provincias:

                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(
                        R.array.array_provincia_a_localidades);
                CharSequence[] localidades = arrayLocalidades.getTextArray(position);
                arrayLocalidades.recycle();

                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, localidades);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                this.spLocalidades.setAdapter(adapter);

                break;

            case R.id.sp_Localidades:

                break;
        }
        BuscaEquipo();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void BuscaEquipo(){
        p_ParametersUrl="?pEq_Categoria=" + spCategorias.getSelectedItem().toString() +
                        "&pEq_TipoEquipo=" + strTipoEquipo +
                        "&pEq_Provincia=" + spProvincias.getSelectedItem().toString() +
                        "&pEq_Localidad=" + spLocalidades.getSelectedItem().toString()+
                        "&pEq_Nombre=" + txtNombreEquipo.getText().toString();
        frg_lst_Equipos.getEquiposWhere(p_ParametersUrl);
    }

    @Override
    public void onEquipoSeleccionado(Equipo eq) {
        Intent i = getIntent();
        i.putExtra("p_TipoEquipoElegido", strTipoEquipo);
        i.putExtra("p_Nombre", eq.getNombre_Equipo());
        i.putExtra("p_Categoria", eq.getCategoria_equipo());
        i.putExtra("p_Id", eq.get_Id());
        setResult(RESULT_OK, i);
        finish();
    }

}
