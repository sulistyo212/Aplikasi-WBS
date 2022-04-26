package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

public class MySQLClient {
    private static final String DATA_INSERT_URL="http://skripsiombudsman.000webhostapp.com/skripsi/add.php";

    private final Context c;
    public MySQLClient(Context c) {
        this.c = c;
    }

    public void add(fungsisimpan s, final View...inputViews){
        if (s==null)
        {
            Toast.makeText(c, "no data to save", Toast.LENGTH_SHORT).show();
        }
        else
            {
                AndroidNetworking.post(DATA_INSERT_URL)
                        .addBodyParameter("deskripsi_laporan", s.getDeskripsi_laporan())
                        .addBodyParameter("kategori_pelanggaran", s.getKategori_pelanggaran())
                        .addBodyParameter("filename", s.getFileName())
                        .addBodyParameter("filename3", s.getFileName3())
                        .addBodyParameter("inisial_instansi", s.getInisial_Instansi())
                        .addBodyParameter("laporan_instansi", s.getLaporan_Instansi())
                        .addBodyParameter("status_laporan", s.getStatus_Laporan())
                        .addBodyParameter("textView", s.getTextView())
                        .addBodyParameter("namapelapor", s.getNamapelapor())
                        .addBodyParameter("email", s.getEmail())
                        .addBodyParameter("no_telepon", s.getNo_telepon())
                        .addBodyParameter("nama_instansi", s.getNama_instansi())
                        .addBodyParameter("tanggallapor", s.getTanggalapor())
                        .addBodyParameter("rahasiadata", String.valueOf(s.getRahasiadata()))
                        .setTag("TAG_ADD")
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if (response !=null)
                                    try {
                                        String responseString = response.get(0).toString();
                                        Toast.makeText(c, "PHP SERVER RESPONSE:" + responseString, Toast.LENGTH_SHORT).show();

                                    if (responseString.equalsIgnoreCase("")){
                                        EditText namapelaporTxt = (EditText) inputViews [0];
                                        EditText emailTxt = (EditText) inputViews [1];
                                        EditText no_teleponTxt = (EditText) inputViews [2];
                                        Spinner  spkategori_pelanggaran = (Spinner) inputViews [3];
                                        EditText nama_instansiTxt = (EditText) inputViews [4];
                                        TextView tanggallaporTxt = (TextView) inputViews [5];
                                        TextView TxtTextView = (TextView) inputViews [6];
                                        EditText inisial_instansiTxt = (EditText) inputViews [7];
                                        EditText laporan_instansiTxt = (EditText) inputViews [8];
                                        EditText status_laporanTxt = (EditText) inputViews [9];
                                        TextView filenameTxt = (TextView) inputViews [10];
                                        TextView filename3Txt = (TextView) inputViews [11];
                                        EditText deskripsi_laporanTxt = (EditText) inputViews [12];

                                        filenameTxt.setText("");
                                        filename3Txt.setText("");
                                        inisial_instansiTxt.setText("");
                                        status_laporanTxt.setText("");
                                        laporan_instansiTxt.setText("");
                                        TxtTextView.setText("");
                                        namapelaporTxt.setText("");
                                        emailTxt.setText("");
                                        no_teleponTxt.setText("");
                                        nama_instansiTxt.setText("");
                                        tanggallaporTxt.setText("");
                                        spkategori_pelanggaran.setSelection(0);
                                        deskripsi_laporanTxt.setText("");


                                    } else {
                                        Toast.makeText(c, "PHP WASN'T SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                    }
                                    } catch (JSONException e){
                                        e.printStackTrace();
                                        Toast.makeText(c, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                            }

                            @Override
                            public void onError(ANError error) {
                            }
                        });

            }
    }
}
