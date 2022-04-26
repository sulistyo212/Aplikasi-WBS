package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private EditText Txtnamapelapor, Txtemail,Txtno_telepon, Txtnama_instansi, TxtInisial_Instansi,TxtLaporan_Instansi,TxtStatus_Laporan,TxtDeskripsi_laporan;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Spinner spkategori_pelanggaran;
    private TextView Txttanggallapor,tvDateResult, TxtGenerateNumber, file_name,TxtFile_name,TxtFile_name3 , filename3 ,TxtGenerateNumber1;
    private ImageButton btDatePicker;
    private Button btnSimpan ,gambarKtp, gambarBukti;
    private Button stringGenerateButton;
    String file_path=null , all_file_path=null, all_file_path2=null;
    private CheckBox chkRahasiadata,chapcaa;
    private static final int ALL_FILE_REQUEST = 102;
    private static final int ALL_FILE_REQUEST_2 = 103;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY =  101;
    private Dialog dialog;
    GoogleApiClient googleApiClient;
    int method = 0;

    String SiteKey="6Lc84jgcAAAAAKldVxVqFIRTYCMnQ7M9eWtIPOlN";
    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvDateResult.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tvDateResult = (TextView) findViewById(R.id. tanggallapor);
        btDatePicker = (ImageButton) findViewById(R.id. tanggal);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tvDateResult = (TextView) findViewById(R.id.tanggallapor);
        btDatePicker = (ImageButton) findViewById(R.id.tanggal);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });



        this.initializeViews();



        populateKategori_pelanggaran();

        //Lakukan simpan
        btnSimpan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                validasiChapca();
            }
        });

        //fungsi chapcaa
        chapcaa = findViewById(R.id.chapca);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(MainActivity3.this)
                .build();
        googleApiClient.connect();

        chapcaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chapcaa.isChecked()){
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && status.isSuccess()){
                                        Toast.makeText(getApplicationContext()
                                        ,"Verifikasi Berhasil"
                                        ,Toast.LENGTH_SHORT).show();

                                        chapcaa.setTextColor(Color.GREEN);
                                    }
                                }
                            });
                } else{
                    chapcaa.setTextColor(Color.BLACK);
                }
            }
        });

        gambarKtp.setOnClickListener(MainActivity3.this);
        gambarBukti.setOnClickListener(MainActivity3.this);

        }

        // validasi chapca
        private void validasiChapca() {
            if (!chapcaa.isChecked()) {
                Toast.makeText(MainActivity3.this,"Mohon Validasi terlebih dahulu",Toast.LENGTH_SHORT).show();
            } else {
                handleClickEvents();

            }

        }

        //Request File
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gambarKtp) {
            method = 0;
            if (Build.VERSION.SDK_INT >= 24) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    filePicker(0);
                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else {
                filePicker(0);
            }

        }  if (v.getId() == R.id.gambarBukti) {
            method = 1;
            if (Build.VERSION.SDK_INT >= 24) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    filePicker(1);
                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else {
                filePicker(1);
            }
        }

    }

    //upload file
    private void uploadfile(){
        UploadTask uploadTask=new UploadTask();
        uploadTask.execute(new String[]{file_path, all_file_path});


    }

    public void filePicker(int i){
        if (i == 0) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), REQUEST_GALLERY);
        }
        if (i == 1) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, ALL_FILE_REQUEST);
        }

    }

    //Inisialisasi Variable
    private  void initializeViews() {
        file_name = (TextView) findViewById(R.id.filename);
        filename3 = (TextView) findViewById(R.id.filename3);
        TxtFile_name3 =(TextView) findViewById(R.id.filename3);
        TxtFile_name = (TextView) findViewById(R.id.filename);
        spkategori_pelanggaran=(Spinner)findViewById(R.id.kategori_pelanggaran);
        TxtDeskripsi_laporan = (EditText) findViewById(R.id.deskripsi_laporan);
        TxtStatus_Laporan = (EditText) findViewById(R.id.status_laporan);
        TxtLaporan_Instansi = (EditText) findViewById(R.id.laporan_instansi);
        TxtInisial_Instansi = (EditText) findViewById(R.id.inisial_instansi);
        TxtGenerateNumber = (TextView) findViewById(R.id.textView);
        Txtnamapelapor = (EditText) findViewById(R.id.namapelapor);
        Txtemail = (EditText) findViewById (R.id.email);
        Txtno_telepon = (EditText) findViewById (R.id.no_telepon);
        Txtnama_instansi = (EditText) findViewById (R.id.nama_instansi);
        Txttanggallapor = (TextView) findViewById (R.id.tanggallapor);
        chkRahasiadata= (CheckBox) findViewById(R.id.rahasiadata);
        gambarKtp = (Button) findViewById(R.id.gambarKtp);
        gambarBukti = (Button) findViewById(R.id.gambarBukti);
        btnSimpan = (Button) findViewById(R.id.simpan);
        chapcaa = (CheckBox) findViewById(R.id.chapca);
    }

    //Fungsi Send data
    private void handleClickEvents(){
        
                GENERATE();
                 String kategori_pelanggaran = spkategori_pelanggaran.getSelectedItem().toString();
                String deskripsi_laporan = TxtDeskripsi_laporan.getText().toString();
                String filename3 = TxtFile_name3.getText().toString();
                String file_name = TxtFile_name.getText().toString();
                String inisial_instansi = TxtInisial_Instansi.getText().toString();
                String laporan_instansi = TxtLaporan_Instansi.getText().toString();
                String status_laporan = TxtStatus_Laporan.getText().toString();
                String textView = TxtGenerateNumber.getText().toString();
                String namapelapor = Txtnamapelapor.getText().toString();
                String email = Txtemail.getText().toString();
                String no_telepon = Txtno_telepon.getText().toString();
                String nama_instansi = Txtnama_instansi.getText().toString();
                String tanggallapor = Txttanggallapor.getText().toString();
                Boolean rahasiadata = chkRahasiadata.isChecked();


                if ((
                        deskripsi_laporan.length()<1 ||
                        kategori_pelanggaran.length()<1 ||
                        file_name.length()<1 ||
                        filename3.length()<1 ||
                        namapelapor.length()<1 ||
                        no_telepon.length()<1 ||
                        email.length()<1 ||
                        nama_instansi.length()<1 ||
                        tanggallapor.length()<1 ||
                        textView.length()<1 ||
                        inisial_instansi.length()<1||
                        laporan_instansi.length()<1||
                        status_laporan.length()<1

                ))

                {
                    Toast.makeText(MainActivity3.this, "Mohon isi semua formulir dan lengkapi dokumen", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    fungsisimpan s =new fungsisimpan();
                    s.setNamapelapor(namapelapor);
                    s.setEmail(email);
                    s.setNo_telepon(no_telepon);
                    s.setNama_instansi(nama_instansi);
                    s.setTanggallapor(tanggallapor);
                    s.setRahasiadata(rahasiadata ? 1 : 0);
                    s.setInisial_Instansi(inisial_instansi);
                    s.setLaporan_Instansi(laporan_instansi);
                    s.setStatus_Laporan(status_laporan);
                    s.setTextView(textView);
                    s.setFileName(file_name);
                    s.setFileName3(filename3);
                    s.setDeskripsi_laporan(deskripsi_laporan);
                    s.setKategori_pelanggaran(kategori_pelanggaran);
                    uploadfile();




                    new MySQLClient(MainActivity3.this).add(s,Txtnamapelapor,Txtemail,Txtno_telepon,TxtDeskripsi_laporan,spkategori_pelanggaran,
                            Txtnama_instansi, TxtGenerateNumber, TxtInisial_Instansi,TxtLaporan_Instansi,TxtStatus_Laporan,TxtFile_name,TxtFile_name3
                            );


                }
        {
            Toast.makeText(MainActivity3.this, "File sedang diproses mohon tunggu...", Toast.LENGTH_LONG).show();
        }


            }

    private void GENERATE(){
        RandomString randomString = new RandomString();
        String result=randomString.generateAlphaNumeric(6);
        TxtGenerateNumber.setText(result);
    }

    private void requestPermission(String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity3.this, permission)) {
            Toast.makeText(MainActivity3.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity3.this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission(String permission){
        int result = ContextCompat.checkSelfPermission(MainActivity3.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity3.this, "Permission Successfull", Toast.LENGTH_SHORT).show();
                    filePicker(method);
                } else {
                    Toast.makeText(MainActivity3.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                if (data == null) {
                    return;
                }

                Uri uri = data.getData();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    file_path = FilePath.getFilePath(MainActivity3.this, uri);
                }
                Log.d("File Path ", " " + file_path);
                if (file_path != null) {
                    file_name.setText("" + new File(file_path).getName());
                }

            }
            if (requestCode == ALL_FILE_REQUEST) {
                if (data == null) {
                    return;
                }

                Uri uri = data.getData();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    all_file_path = FilePath.getFilePath(MainActivity3.this, uri);
                }
                Log.d("File Path ", " " + all_file_path);
                if (all_file_path != null) {
                    filename3.setText("" + new File(all_file_path).getName());
                }

            }

        }
    }

    public String getRealPathFromUri(Uri uri,Activity activity){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor=activity.getContentResolver().query(uri,proj,null,null,null);
        if(cursor==null){
            return uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int id=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public class UploadTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null){
                TxtGenerateNumber = (TextView) findViewById(R.id.textView);
                String textView = TxtGenerateNumber.getText().toString();
                Intent intent = new Intent(MainActivity3.this,MainActivity9.class);
                intent.putExtra("keyname",textView);
                startActivity(intent);

                Toast.makeText(MainActivity3.this, "LAPORAN BERHASIL TERKIRIM", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity3.this, "LAPORAN GAGAL TERKIRIM", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            File file1 = new File(strings[0]);
            File file2 = new File(strings[1]);

            try {
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files1", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1))
                        .addFormDataPart("files2", file2.getName(), RequestBody.create(MediaType.parse("*/*"), file2))
                        .addFormDataPart("some_key", "some_value")
                        .addFormDataPart("submit", "submit")
                        .build();
                Request request = new Request.Builder()
                        .url("http://skripsiombudsman.000webhostapp.com/skripsi/add.php")
                        .post(requestBody)
                        .build();

                OkHttpClient okHttpClient = new OkHttpClient();
                //now progressbar not showing properly let's fixed it
                Response response = okHttpClient.newCall(request).execute();
                if (response != null && response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void populateKategori_pelanggaran(){
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.add("TINDAK KEJAHATAN/KRIMINAL");
        adapter.add("TINDAK PELANGGARAN HUKUM");
        adapter.add("TINDAKAN PENIPUAN");
        adapter.add("TINDAKAN KORUPSI");
        adapter.add("TINDAKAN PELANGGARAN MALADMINISTRASI");
        adapter.add("LAINNYA");
        spkategori_pelanggaran.setAdapter(adapter);
        spkategori_pelanggaran.setSelection(0);
    }

    }
