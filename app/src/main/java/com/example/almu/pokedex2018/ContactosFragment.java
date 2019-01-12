package com.example.almu.pokedex2018;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TextView texto;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContactosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactosFragment newInstance() {
        return new ContactosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ArrayList<String> getContacts(){
        ArrayList<String> contactos = new ArrayList<>();

        String[] datos = new String[] { ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE };

        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";

        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getActivity().getApplicationContext().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                datos,
                selectionClause,
                null,
                sortOrder);


        while(c.moveToNext()){
            String contacto =
                            c.getString(1) + ";";
            String telefono = c.getString(2);

            while(telefono.contains(" "))
            {
                telefono = telefono.replace(" ", "");
            }

            if(telefono.length() > 9)
            {
                telefono = telefono.substring(3);
            }
            contacto += telefono;
            contactos.add(contacto);
        }
        c.close();

        return contactos;
    }


    public class ObtenerTelefonos extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPostExecute(String s) {

            texto.setText(s);
            if(s.equals(""))
            {
                texto.setText("No tienes contactos que usen esta app");
            }
            else
            {
                String resultado = "";
                String[] telefonos = s.split(";");
                ArrayList<String> contactos = getContacts();

                for(String contacto : contactos)
                {
                    for(int i = 0; i < telefonos.length; i++ ) {

                        if (contacto.split(";")[1].equals(telefonos[i])) {
                            resultado += "Nombre: " + contacto.split(";")[0] +
                                    " Teléfono: " + contacto.split(";")[1] + "\n";
                        }
                    }
                }

                if(resultado.equals(""))
                    texto.setText("No tienes contactos que usen esta app");
                else
                    texto.setText(resultado);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String devuelve = "";
            try {
                URL url = new URL("http://almudenalopezsanchez.000webhostapp.com/obtener_telefonos.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // abrimos la conexion
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP"); // Necesario
                int respuesta = connection.getResponseCode(); // respuesta de la llamada
                if(respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream()); // preparo la cadena de entrada
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder result = new StringBuilder();

                    String line;
                    while((line = reader.readLine()) != null) {
                        result.append(line); // Paso la entrada al StringBuilder
                    }

                    // Creamos un objeto JSON para poder acceder a los atributos
                    JSONObject respuestaJSON = new JSONObject(result.toString());
                    String resultJSON = respuestaJSON.getString("estado"); // para ver si se realiza
                    if(resultJSON.equals("1")) {
                        JSONArray telefonosJSON = respuestaJSON.getJSONArray("telefonos");

                        for(int i = 0; i < telefonosJSON.length(); i++) {
                            devuelve += telefonosJSON.getJSONObject(i).getString("telefono") +";";
                        }
                        devuelve += "fin";

                        reader.close();
                        in.close();
                    } else {
                        devuelve = "";
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return devuelve;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactos, container, false);
        texto = view.findViewById(R.id.tv_texto);

        ObtenerTelefonos obtenerTelefonos = new ObtenerTelefonos();
        obtenerTelefonos.execute();

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
