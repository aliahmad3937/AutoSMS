package com.auto.sms.SmsBlaster;


import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import androidx.appcompat.app.AppCompatActivity;


import com.auto.sms.R;
import com.auto.sms.activities.BaseActivity;
import com.auto.sms.models.MissedCallModel;
import com.auto.sms.utils.SharePrefs;
import com.google.android.material.textfield.TextInputLayout;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;


public class AnaActivity extends BaseActivity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TAG = "SYSYSY";
    static ArrayList<MyClass> iletimRaporu = new ArrayList<>();
    Boolean RehberYuklendi = Boolean.valueOf(false);
    Builder alertMessage = null;
    Bilgiler bilgiler = new Bilgiler();
    String cityName = "";
    String countryName = "";
    String currentLang = "";
    MyCustomAdapter dataAdapter = null;
    /* access modifiers changed from: private */
    int gonderilen;
    ArrayList<String> gonderimListesi = new ArrayList<>();
    Boolean grupGosterimde = Boolean.valueOf(false);
    ArrayList<MyClass> gruplar = new ArrayList<>();
    int gruptakiKisiIndex;
    ArrayList<GuruptakiKisiler> gruptakikisiler = new ArrayList<>();
    String gsmNo = "";
    int index;
    Boolean isBusy = Boolean.valueOf(true);
    ArrayList<KisiIDTelefonlar> kisiIDtelefonlari = new ArrayList<>();
    ArrayList<MyClass> kisiler = new ArrayList<>();
    Boolean kullaniciBilgileriniGuncelle = Boolean.valueOf(true);
    String latlon = "0,0";
    int maximumGonderim = 1000;
    SharedPreferences pref = null;
    Random r = new Random();
    int secilen = 0;
    EditText tbMesaj = null;
    Boolean tektek_gonder = Boolean.valueOf(true);
    TextView tvListeBaslik = null;
    TextInputLayout daysInputLayout;
    TimePicker datePicker;

    private class AsyncTaskClassRehber extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        private AsyncTaskClassRehber() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            AnaActivity.this.gruplar.clear();
            AnaActivity.this.kisiler.clear();
            AnaActivity.this.gruptakikisiler.clear();
            AnaActivity.this.kisiIDtelefonlari.clear();
            this.pDialog = new ProgressDialog(AnaActivity.this);
            this.pDialog.setCancelable(false);
            this.pDialog.setTitle(AnaActivity.this.bilgiler.lutfen_bekleyin);
            this.pDialog.setMessage(AnaActivity.this.bilgiler.rehber_yukleniyor);
            this.pDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
            this.pDialog.show();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            AnaActivity.this.callQuery();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            try {
                this.pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (AnaActivity.this.grupGosterimde.booleanValue()) {
                AnaActivity anaActivity = AnaActivity.this;
                anaActivity.GetListGrup(anaActivity.gruplar);
                AnaActivity anaActivity2 = AnaActivity.this;
                StringBuilder sb = new StringBuilder();
                sb.append(AnaActivity.this.bilgiler.ApplicationTitle);
                sb.append(" - ");
                sb.append(AnaActivity.this.gruplar.size());
                sb.append(AnaActivity.this.bilgiler.grup);
                anaActivity2.setTitle(sb.toString());
                return;
            }
            AnaActivity anaActivity3 = AnaActivity.this;
            anaActivity3.GetListGrup(anaActivity3.kisiler);
            AnaActivity anaActivity4 = AnaActivity.this;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(AnaActivity.this.bilgiler.ApplicationTitle);
            sb2.append(" - ");
            sb2.append(AnaActivity.this.kisiler.size());
            sb2.append(AnaActivity.this.bilgiler.kisi);
            anaActivity4.setTitle(sb2.toString());
        }
    }

    private class AsyncTaskClassRehberYukle extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        private AsyncTaskClassRehberYukle() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            AnaActivity.this.gruplar.clear();
            AnaActivity.this.kisiler.clear();
            AnaActivity.this.gruptakikisiler.clear();
            AnaActivity.this.kisiIDtelefonlari.clear();
            this.pDialog = new ProgressDialog(AnaActivity.this);
            this.pDialog.setCancelable(false);
            this.pDialog.setTitle(AnaActivity.this.bilgiler.lutfen_bekleyin);
            this.pDialog.setMessage(AnaActivity.this.bilgiler.rehber_yukleniyor);
            this.pDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
            this.pDialog.show();
            Log.d(AnaActivity.TAG, "onPreExecute:  - AsyncTaskClassRehberYukle ");
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            int i = AnaActivity.this.pref.getInt("gruplar_size", 0);
            for (int i2 = 0; i2 < i; i2++) {
                SharedPreferences sharedPreferences = AnaActivity.this.pref;
                StringBuilder sb = new StringBuilder();
                sb.append("gname_");
                sb.append(i2);
                String string = sharedPreferences.getString(sb.toString(), "");
                SharedPreferences sharedPreferences2 = AnaActivity.this.pref;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("gvalue_");
                sb2.append(i2);
                AnaActivity.this.gruplar.add(new MyClass(sharedPreferences2.getString(sb2.toString(), ""), string, false));
            }
            int i3 = AnaActivity.this.pref.getInt("gruptakikisiler_size", 0);
            for (int i4 = 0; i4 < i3; i4++) {
                SharedPreferences sharedPreferences3 = AnaActivity.this.pref;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("gruptakikisilerGRUPID_");
                sb3.append(i4);
                int i5 = sharedPreferences3.getInt(sb3.toString(), 0);
                SharedPreferences sharedPreferences4 = AnaActivity.this.pref;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("gruptakikisilerKISIID_");
                sb4.append(i4);
                AnaActivity.this.gruptakikisiler.add(new GuruptakiKisiler(i5, sharedPreferences4.getInt(sb4.toString(), 0)));
            }
            int i6 = AnaActivity.this.pref.getInt("kisiIDtelefonlari_size", 0);
            for (int i7 = 0; i7 < i6; i7++) {
                SharedPreferences sharedPreferences5 = AnaActivity.this.pref;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("kisiIDtelefonlariTELEFON_");
                sb5.append(i7);
                String string2 = sharedPreferences5.getString(sb5.toString(), "");
                SharedPreferences sharedPreferences6 = AnaActivity.this.pref;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("kisiIDtelefonlariKISIID_");
                sb6.append(i7);
                AnaActivity.this.kisiIDtelefonlari.add(new KisiIDTelefonlar(sharedPreferences6.getInt(sb6.toString(), 0), string2));
            }
            int i8 = AnaActivity.this.pref.getInt("kisiler_size", 0);
            for (int i9 = 0; i9 < i8; i9++) {
                SharedPreferences sharedPreferences7 = AnaActivity.this.pref;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("name_");
                sb7.append(i9);
                String string3 = sharedPreferences7.getString(sb7.toString(), "");
                SharedPreferences sharedPreferences8 = AnaActivity.this.pref;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("value_");
                sb8.append(i9);
                AnaActivity.this.kisiler.add(new MyClass(sharedPreferences8.getString(sb8.toString(), ""), string3, false));
            }
            if (AnaActivity.this.isNetworkAvailable()) {
                AnaActivity.this.GenelDuyurular();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            try {
                this.pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            AnaActivity anaActivity = AnaActivity.this;
            anaActivity.GetListGrup(anaActivity.kisiler);
            AnaActivity anaActivity2 = AnaActivity.this;
            StringBuilder sb = new StringBuilder();
            sb.append(AnaActivity.this.bilgiler.ApplicationTitle);
            sb.append(" - ");
            sb.append(AnaActivity.this.kisiler.size());
            sb.append(" ");
            sb.append(AnaActivity.this.bilgiler.kisi);
            anaActivity2.setTitle(sb.toString());
        }
    }

    private class AsyncTaskClassSendSMS extends AsyncTask<String, String, String> {
        String gsmNumber;
        String message;
        ProgressDialog pDialog;

        private AsyncTaskClassSendSMS(String str, String str2) {
            this.message = str;
            this.gsmNumber = str2;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.pDialog = new ProgressDialog(AnaActivity.this);
            this.pDialog.setCancelable(false);
            this.pDialog.setTitle(AnaActivity.this.bilgiler.lutfen_bekleyin);
            this.pDialog.setMessage(AnaActivity.this.bilgiler.mesajlar_gonderiliyor);
            this.pDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
            this.pDialog.setProgressStyle(1);
            this.pDialog.setProgress(0);
            this.pDialog.setMax(AnaActivity.this.secilen);
            this.pDialog.show();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            AnaActivity.iletimRaporu.clear();
            for (int i = AnaActivity.this.index; i < AnaActivity.this.gonderimListesi.size(); i++) {
                AnaActivity anaActivity = AnaActivity.this;
                anaActivity.gsmNo = (String) anaActivity.gonderimListesi.get(i);
                AnaActivity anaActivity2 = AnaActivity.this;
                anaActivity2.sendSMS(anaActivity2.gsmNo, this.message);
                AnaActivity.iletimRaporu.add(new MyClass(AnaActivity.this.gsmNo, AnaActivity.this.bilgiler.bekleniyor, false));
                AnaActivity.this.gonderilen++;
            }
            AnaActivity.this.maximumGonderim -= AnaActivity.this.gonderilen;
            this.pDialog.setProgress(AnaActivity.this.gonderilen);
            AnaActivity.this.pref.edit().putInt("maximumGonderim", AnaActivity.this.maximumGonderim).apply();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            try {
                this.pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Context applicationContext = AnaActivity.this.getApplicationContext();
            StringBuilder sb = new StringBuilder();
            sb.append(AnaActivity.this.gonderilen);
            sb.append(" ");
            sb.append(AnaActivity.this.bilgiler.mesaj_gonderildi);
            Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
            if (AnaActivity.iletimRaporu.size() > 0) {
                AnaActivity.this.startActivity(new Intent(AnaActivity.this, RaporActivity.class));
            }
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<MyClass> {
        /* access modifiers changed from: private */
        public ArrayList<MyClass> kisiList = new ArrayList<>();
        MissedCallModel saveContactModel;
        ArrayList<String> savedNumbers = new ArrayList<>();
        private class ViewHolder {
            CheckBox cbox;

            private ViewHolder() {
              saveContactModel  =  SharePrefs.getInstance(getApplication()).getContactsObject(getApplication());
            }
        }

        MyCustomAdapter(Context context, int i, ArrayList<MyClass> arrayList) {
            super(context, i, arrayList);
            this.kisiList.addAll(arrayList);
            saveContactModel =  SharePrefs.getInstance(context).getContactsObject(context);
                if(saveContactModel != null){

                    String[] strings =  saveContactModel.getNumbers().split(",");
                    if(strings != null && strings.length > 0){
                        for(String str : strings){
                            savedNumbers.add(str);
                        }
                    }

                }
        }

        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = ((LayoutInflater) AnaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.check_list, null);
                viewHolder = new ViewHolder();
                viewHolder.cbox = (CheckBox) view.findViewById(R.id.cbSecim);
                view.setTag(viewHolder);
                viewHolder.cbox.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        CheckBox checkBox = (CheckBox) view;
                        ((MyClass) checkBox.getTag()).setSelected(checkBox.isChecked());
                        AnaActivity.this.SecilenleriSay();
                    }
                });
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            MyClass myClass = (MyClass) this.kisiList.get(i);
            viewHolder.cbox.setText(myClass.getName());
            viewHolder.cbox.setChecked(myClass.isSelected());
            viewHolder.cbox.setTag(myClass);
//            Log.i("TAG65","number :"+myClass.getValue());
//            Log.i("TAG65","numberas :"+saveContactModel.getNumbers());
            if(!savedNumbers.isEmpty()){
                if(savedNumbers.contains(myClass.getValue())){
                    viewHolder.cbox.setChecked(true);
                }
            }
            return view;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_ana);
        this.bilgiler.ApplicationTitle = getResources().getString(R.string.app_name);
        this.bilgiler.grup = getResources().getString(R.string.grup);
        this.bilgiler.kisi = getResources().getString(R.string.kisi);
        this.bilgiler.gruplar = getResources().getString(R.string.gruplar);
        this.bilgiler.kisiler = getResources().getString(R.string.kisiler);
        this.bilgiler.mesaj_gonderilsin_mi = getResources().getString(R.string.mesaj_gonderilsin_mi);
        this.bilgiler.karakter = getResources().getString(R.string.karakter);
        this.bilgiler.kisi_yada_grup_secilmemis = getResources().getString(R.string.kisi_yada_grup_secilmemis);
        this.bilgiler.ucretsiz_surum_uyarisi = getResources().getString(R.string.ucretsiz_surum_uyarisi);
        this.bilgiler.mesaj_yazmadiniz = getResources().getString(R.string.mesaj_yazmadiniz);
        this.bilgiler.kayitol = getResources().getString(R.string.kayitol);
        this.bilgiler.yuksek_miktarda_mesaj_gonderimi = getResources().getString(R.string.yuksek_miktarda_mesaj_gonderimi);
        this.bilgiler.lutfen_bekleyin = getResources().getString(R.string.lutfen_bekleyin);
        this.bilgiler.rehber_yukleniyor = getResources().getString(R.string.rehber_yukleniyor);
        this.bilgiler.rehber_hizli_acilis = getResources().getString(R.string.rehber_hizli_acilis);
        this.bilgiler.konumum = getResources().getString(R.string.konumum);
        this.bilgiler.henuz_mesaj_gonderimi_yapmadiniz = getResources().getString(R.string.henuz_mesaj_gonderimi_yapmadiniz);
        this.bilgiler.bekleniyor = getResources().getString(R.string.bekleniyor);
        this.bilgiler.iletildi = getResources().getString(R.string.iletildi);
        this.bilgiler.basarisiz = getResources().getString(R.string.basarisiz);
        this.bilgiler.mesaj_gonderildi = getResources().getString(R.string.mesaj_gonderildi);
        this.bilgiler.hakkinda_mesaji = getResources().getString(R.string.hakkinda_mesaji);
        this.bilgiler.lutfen_bekleyin = getResources().getString(R.string.lutfen_bekleyin);
        this.bilgiler.mesajlar_gonderiliyor = getResources().getString(R.string.mesajlar_gonderiliyor);
        this.bilgiler.GSM_No_yazmadiniz = getResources().getString(R.string.GSM_No_yazmadiniz);
        this.bilgiler.seciniz = getResources().getString(R.string.seciniz);
        this.bilgiler.gruplari_goster = getResources().getString(R.string.gruplari_goster);
        this.bilgiler.kisileri_goster = getResources().getString(R.string.kisileri_goster);
        this.bilgiler.tektek_gonder = getResources().getString(R.string.tektek_gonder);
        this.bilgiler.tumunu_gonder = getResources().getString(R.string.tumunu_gonder);
        this.bilgiler.gonderim_yontemi = getResources().getString(R.string.gonderim_yontemi);
        this.bilgiler.hint_gsmno = getResources().getString(R.string.hint_gsmno);
        this.currentLang = Locale.getDefault().getLanguage();
        Log.i("currentLang", this.currentLang);
        getWindow().addFlags(128);
        this.tbMesaj = (EditText) findViewById(R.id.tbMesaj);
        this.tvListeBaslik = (TextView) findViewById(R.id.tvListeBaslik);
        this.daysInputLayout = (TextInputLayout) findViewById(R.id.input_days_layout);
        datePicker =(TimePicker) findViewById(R.id.datePicker1);
        datePicker.setIs24HourView(true);

        MissedCallModel saveContactModel =  SharePrefs.getInstance(getApplication()).getContactsObject(getApplication());
        if(saveContactModel != null) {
            Log.i("TAG65","hours :"+saveContactModel.getHours()+ "minutes :"+saveContactModel.getMinute());
            datePicker.setHour(saveContactModel.getHours());
            datePicker.setMinute(saveContactModel.getMinute());
            this.tbMesaj.setText(saveContactModel.getSms());
            this.daysInputLayout.getEditText().setText(String.valueOf(saveContactModel.getDays()));
        }

        this.tvListeBaslik.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new Builder(AnaActivity.this).setIcon(R.drawable.ic_supervisor_account_black_24dp).setTitle(AnaActivity.this.bilgiler.seciniz).setItems(new CharSequence[]{AnaActivity.this.bilgiler.gruplari_goster, AnaActivity.this.bilgiler.kisileri_goster}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            AnaActivity.this.grupGosterimde = Boolean.valueOf(true);
                            AnaActivity.this.GetListGrup(AnaActivity.this.gruplar);
                            AnaActivity.this.SecilenleriSay();
                            AnaActivity.this.tvListeBaslik.setText(AnaActivity.this.bilgiler.gruplar);
                            AnaActivity anaActivity = AnaActivity.this;
                            StringBuilder sb = new StringBuilder();
                            sb.append(AnaActivity.this.bilgiler.ApplicationTitle);
                            sb.append(" - ");
                            sb.append(AnaActivity.this.gruplar.size());
                            sb.append(" ");
                            sb.append(AnaActivity.this.bilgiler.grup);
                            anaActivity.setTitle(sb.toString());
                            return;
                        }
                        AnaActivity.this.grupGosterimde = Boolean.valueOf(false);
                        AnaActivity.this.GetListGrup(AnaActivity.this.kisiler);
                        AnaActivity.this.SecilenleriSay();
                        AnaActivity.this.tvListeBaslik.setText(AnaActivity.this.bilgiler.kisiler);
                        AnaActivity anaActivity2 = AnaActivity.this;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(AnaActivity.this.bilgiler.ApplicationTitle);
                        sb2.append(" - ");
                        sb2.append(AnaActivity.this.kisiler.size());
                        sb2.append(" ");
                        sb2.append(AnaActivity.this.bilgiler.kisi);
                        anaActivity2.setTitle(sb2.toString());
                    }
                }).setCancelable(true).show();
            }
        });
        this.alertMessage = new Builder(this);
        this.pref = getSharedPreferences("SinirsizSMS", 0);
        this.RehberYuklendi = Boolean.valueOf(this.pref.getBoolean("RehberYuklendi", false));
        this.maximumGonderim = this.pref.getInt("maximumGonderim", 1000);
        AnaActivityPermissionsDispatcher.RehberYukleWithPermissionCheck(this);
        ((CheckBox) findViewById(R.id.cbTumunuSec)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                AnaActivity.this.cbTumunuSec(Boolean.valueOf(z));
            }
        });
        ((ImageButton) findViewById(R.id.btnMesajGonder)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if( daysInputLayout.getEditText().getText().toString().isEmpty()){
                    daysInputLayout.setError("Required!");
                }else{
                    AnaActivity.this.btnMesajGonder();
                }

            }
        });
    }

    /* access modifiers changed from: private */
    public void SecilenleriSay() {
        this.secilen = 0;
        for (int i = this.index; i < this.dataAdapter.getCount(); i++) {
            if (((MyClass) this.dataAdapter.kisiList.get(i)).isSelected()) {
                if (this.grupGosterimde.booleanValue()) {
                    int parseInt = Integer.parseInt(((MyClass) this.dataAdapter.kisiList.get(i)).getValue());
                    for (int i2 = this.gruptakiKisiIndex; i2 < this.gruptakikisiler.size(); i2++) {
                        if (((GuruptakiKisiler) this.gruptakikisiler.get(i2)).GurupID == parseInt) {
                            int i3 = 0;
                            while (true) {
                                if (i3 >= this.kisiIDtelefonlari.size()) {
                                    break;
                                } else if (((GuruptakiKisiler) this.gruptakikisiler.get(i2)).KisiID == ((KisiIDTelefonlar) this.kisiIDtelefonlari.get(i3)).KisiID) {
                                    this.gsmNo = ((KisiIDTelefonlar) this.kisiIDtelefonlari.get(i3)).Telefon;
                                    break;
                                } else {
                                    i3++;
                                }
                            }
                            this.gonderimListesi.add(this.gsmNo);
                            this.secilen++;
                        }
                    }
                } else {
                    this.gsmNo = ((MyClass) this.dataAdapter.kisiList.get(i)).getValue();
                    this.gonderimListesi.add(this.gsmNo);
                    this.secilen++;
                }
            }
        }
        if (this.grupGosterimde.booleanValue()) {
            if (this.secilen > 0) {
                TextView textView = this.tvListeBaslik;
                StringBuilder sb = new StringBuilder();
                sb.append(this.bilgiler.gruplar);
                sb.append(" (");
                sb.append(this.secilen);
                sb.append(" ");
                sb.append(this.bilgiler.kisi);
                sb.append(")");
                textView.setText(sb.toString());
                return;
            }
            this.tvListeBaslik.setText(this.bilgiler.gruplar);
        } else if (this.secilen > 0) {
            TextView textView2 = this.tvListeBaslik;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.bilgiler.kisiler);
            sb2.append(" (");
            sb2.append(this.secilen);
            sb2.append(" ");
            sb2.append(this.bilgiler.kisi);
            sb2.append(")");
            textView2.setText(sb2.toString());
        } else {
            this.tvListeBaslik.setText(this.bilgiler.kisiler);
        }
    }

    /* access modifiers changed from: private */
    public void cbTumunuSec(Boolean bool) {
        for (int i = 0; i < this.dataAdapter.getCount(); i++) {
            ((MyClass) this.dataAdapter.kisiList.get(i)).selected = bool.booleanValue();
        }
        if (this.grupGosterimde.booleanValue()) {
            GetListGrup(this.gruplar);
        } else {
            GetListGrup(this.kisiler);
        }
        SecilenleriSay();
    }

    /* access modifiers changed from: private */
    public void btnMesajGonder() {
        int length = this.tbMesaj.getText().toString().length();
        int i = length > 160 ? 2 : 1;
        if (length > 320) {
            i++;
        }
        if (length > 480) {
            i++;
        }
        if (length > 640) {
            i++;
        }
        if (length > 800) {
            i++;
        }
        this.gonderimListesi.clear();
        String str = "";
        if (length > 0)
        {
            this.secilen = 0;
            for (int i2 = this.index; i2 < this.dataAdapter.getCount(); i2++) {
                if (((MyClass) this.dataAdapter.kisiList.get(i2)).isSelected()) {
                    if (this.grupGosterimde.booleanValue()) {
                        int parseInt = Integer.parseInt(((MyClass) this.dataAdapter.kisiList.get(i2)).getValue());
                        for (int i3 = this.gruptakiKisiIndex; i3 < this.gruptakikisiler.size(); i3++) {
                            if (((GuruptakiKisiler) this.gruptakikisiler.get(i3)).GurupID == parseInt) {
                                int i4 = 0;
                                while (true) {
                                    if (i4 >= this.kisiIDtelefonlari.size()) {
                                        break;
                                    } else if (((GuruptakiKisiler) this.gruptakikisiler.get(i3)).KisiID == ((KisiIDTelefonlar) this.kisiIDtelefonlari.get(i4)).KisiID) {
                                        str = ((KisiIDTelefonlar) this.kisiIDtelefonlari.get(i4)).Telefon;
                                        break;
                                    } else {
                                        i4++;
                                    }
                                }
                                this.gonderimListesi.add(str);
                                this.secilen++;
                            }
                        }
                    } else {
                        str = ((MyClass) this.dataAdapter.kisiList.get(i2)).getValue();
                        this.gonderimListesi.add(str);
                        this.secilen++;
                    }
                }
            }
            int i5 = this.secilen;
           /* if (i5 <= 0) {
                this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(this.bilgiler.kisi_yada_grup_secilmemis).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
            else if (i5 > this.maximumGonderim) {
                this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(this.bilgiler.ucretsiz_surum_uyarisi).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(this.bilgiler.mesaj_gonderilsin_mi);
                sb.append("\n\n");
                sb.append(length);
                sb.append(" ");
                sb.append(this.bilgiler.karakter);
                sb.append(" ");
                sb.append(i);
                sb.append(" SMS\n");
                sb.append(this.secilen);
                sb.append(" ");
                sb.append(this.bilgiler.kisi);
                this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(sb.toString()).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AnaActivity anaActivity = AnaActivity.this;
                        String access$600 = anaActivity.arrangeContactList(anaActivity.gonderimListesi);
                        String valueOf = String.valueOf(AnaActivity.this.tbMesaj.getText());

                        AnaActivity.this.composeSMSMessage(valueOf, access$600);
                    }
                }).setNegativeButton(R.string.btn_iptal, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }*/
            AnaActivity anaActivity = AnaActivity.this;
            String access$600 = anaActivity.arrangeContactList(anaActivity.gonderimListesi);

            Log.i("myContactList","List :"+access$600);
            Toast.makeText(this, getString(R.string.save), Toast.LENGTH_LONG)
                    .show();
            finish();
            SharePrefs.getInstance(this).saveContactsObject(
                  this,
                    new MissedCallModel(
                            this.tbMesaj.getText().toString(),
                            access$600,
                            Integer.parseInt(daysInputLayout.getEditText().getText().toString()),
                            0,
                            0,
                            datePicker.getHour(),
                            datePicker.getMinute()
                    ));

        //  String valueOf = String.valueOf(AnaActivity.this.tbMesaj.getText());
         //   AnaActivity.this.composeSMSMessage(valueOf, access$600);
           // sendSmsMsgFnc(access$600 ,valueOf, this);
        } else {
            this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(this.bilgiler.mesaj_yazmadiniz).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AnaActivity.this.tbMesaj.requestFocus();
                }
            }).show();
        }
    }

    /* access modifiers changed from: 0000 */
    @SuppressLint("MissingPermission")
    @NeedsPermission({"android.permission.READ_CONTACTS"})
    public void RehberYukle() {
        if (!this.RehberYuklendi.booleanValue()) {
            new AsyncTaskClassRehber().execute(new String[0]);
        } else {
            new AsyncTaskClassRehberYukle().execute(new String[0]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: private */
    public void GetListGrup(ArrayList<MyClass> arrayList) {
        ListView listView = (ListView) findViewById(R.id.lvKisiler);
        this.dataAdapter = new MyCustomAdapter(this, R.layout.check_list, arrayList);
        listView.setAdapter(this.dataAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Toast.makeText(AnaActivity.this.getApplicationContext(), ((MyClass) adapterView.getItemAtPosition(i)).getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendSMS(final String str, String str2) {
        this.isBusy = Boolean.valueOf(true);
        String str3 = "SMS_SENT";
        String str4 = "SMS_DELIVERED";
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent(str3), 0);
        PendingIntent broadcast2 = PendingIntent.getBroadcast(this, 0, new Intent(str4), 0);
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int resultCode = getResultCode();
                if (resultCode != -1) {
                    switch (resultCode) {
                        case 1:
                            Toast.makeText(AnaActivity.this.getBaseContext(), "Genel hata", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(AnaActivity.this.getBaseContext(), "Sinyal kapal�", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(AnaActivity.this.getBaseContext(), "PDU bos", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(AnaActivity.this.getBaseContext(), "Servis yok", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                if (AnaActivity.this.tektek_gonder.booleanValue()) {
                    AnaActivity.this.unregisterReceiver(this);
                }
            }
        }, new IntentFilter(str3));
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case -1:
                        Iterator it = AnaActivity.iletimRaporu.iterator();
                        while (it.hasNext()) {
                            MyClass myClass = (MyClass) it.next();
                            if (myClass.value.equals(str)) {
                                myClass.name = AnaActivity.this.bilgiler.iletildi;
                                CustomModel.getInstance().changeState(true);
                            }
                        }
                        break;
                    case 0:
                        Iterator it2 = AnaActivity.iletimRaporu.iterator();
                        while (it2.hasNext()) {
                            MyClass myClass2 = (MyClass) it2.next();
                            if (myClass2.value.equals(str)) {
                                myClass2.name = AnaActivity.this.bilgiler.basarisiz;
                                CustomModel.getInstance().changeState(false);
                            }
                        }
                        break;
                }
                AnaActivity.this.isBusy = Boolean.valueOf(false);
                if (AnaActivity.this.tektek_gonder.booleanValue()) {
                    AnaActivity.this.unregisterReceiver(this);
                }
            }
        }, new IntentFilter(str4));
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList divideMessage = smsManager.divideMessage(str2);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < divideMessage.size(); i++) {
            arrayList.add(broadcast);
            arrayList2.add(broadcast2);
        }
        smsManager.sendMultipartTextMessage(str, null, divideMessage, arrayList, arrayList2);
        if (this.tektek_gonder.booleanValue()) {
            int i2 = 1000;
            while (this.isBusy.booleanValue() && i2 > 0) {
                try {
                    Thread.sleep(10);
                    i2--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void GenelDuyurular() {
        new Thread(new Runnable() {
            public void run() {
                String str = "GetAnnouncement";
                String str2 = "http://tempuri.org/";
                String str3 = "http://api.sinirsizsms.com/WebService.asmx";
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str);
                String sb2 = sb.toString();
                try {
                    SoapObject soapObject = new SoapObject(str2, str);
                    SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(110);
                    soapSerializationEnvelope.dotNet = true;
                    soapSerializationEnvelope.setOutputSoapObject(soapObject);
                    new HttpTransportSE(str3).call(sb2, soapSerializationEnvelope);
                    if (soapSerializationEnvelope.getResponse() != null) {
                        String soapObject2 = ((SoapObject) soapSerializationEnvelope.getResponse()).toString();
                        if (soapObject2.contains("|") && soapObject2.indexOf(AnaActivity.this.currentLang) == 0) {
                            String substring = soapObject2.substring(soapObject2.indexOf("|") + 1);
                            Looper.prepare();
                            AnaActivity.this.alertMessage.setCancelable(true).setTitle(AnaActivity.this.bilgiler.ApplicationTitle).setMessage(substring).setIcon(R.drawable.ic_info_outline_black_24dp).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
                            Looper.loop();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ana_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.amGruplar /*2131165217*/:
                this.grupGosterimde = Boolean.valueOf(true);
                GetListGrup(this.gruplar);
                SecilenleriSay();
                this.tvListeBaslik.setText(this.bilgiler.gruplar);
                StringBuilder sb = new StringBuilder();
                sb.append(this.bilgiler.ApplicationTitle);
                sb.append(" - ");
                sb.append(this.gruplar.size());
                sb.append(" ");
                sb.append(this.bilgiler.grup);
                setTitle(sb.toString());
                return true;

            case R.id.amKisiler /*2131165219*/:
                this.grupGosterimde = Boolean.valueOf(false);
                GetListGrup(this.kisiler);
                SecilenleriSay();
                this.tvListeBaslik.setText(this.bilgiler.kisiler);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.bilgiler.ApplicationTitle);
                sb2.append(" - ");
                sb2.append(this.kisiler.size());
                sb2.append(" ");
                sb2.append(this.bilgiler.kisi);
                setTitle(sb2.toString());
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    /* access modifiers changed from: private */
    public boolean isNetworkAvailable() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        AnaActivityPermissionsDispatcher.onRequestPermissionsResult(this, i, iArr);
    }
    /* access modifiers changed from: 0000 */
    public void callQuery() {
        try {
            String str;
            int i;
            String str2 = "_id";
            String str3 = "title";
            Cursor query = getContentResolver().query(Groups.CONTENT_URI, null, "deleted = 0", null, null);
            int i2 = 1;
            char c = 0;
            if (query.getCount() > 0) {
                while (query.moveToNext()) {
                    @SuppressLint("Range") int i3 = query.getInt(query.getColumnIndex(str2));
                    @SuppressLint("Range") String string = query.getString(query.getColumnIndex(str3));
                    Uri uri = Data.CONTENT_URI;
                    String[] strArr = {"contact_id"};
                    ContentResolver contentResolver = getContentResolver();
                    StringBuilder sb = new StringBuilder();
                    sb.append("data1 = ");
                    sb.append(i3);
                    Cursor query2 = contentResolver.query(uri, strArr, sb.toString(), null, null);
                    if (query2.getCount() > 0) {
                        i = 0;
                        while (query2.moveToNext()) {
                            @SuppressLint("Range") int i4 = query2.getInt(query2.getColumnIndex("contact_id"));
                            if (i3 > 0 && i4 > 0) {
                                this.gruptakikisiler.add(new GuruptakiKisiler(i3, i4));
                                i++;
                            }
                        }
                    } else {
                        i = 0;
                    }
                    if (i > 0) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(i3);
                        String sb3 = sb2.toString();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(string);
                        sb4.append(" (");
                        sb4.append(i);
                        sb4.append(")");
                        this.gruplar.add(new MyClass(sb3, sb4.toString(), false));
                    }
                }
            }
            Uri uri2 = Contacts.CONTENT_URI;
            String str4 = "_id";
            String str5 = "display_name";
            String str6 = "has_phone_number";
            Uri uri3 = Phone.CONTENT_URI;
            String str7 = "contact_id";
            String str8 = "data1";
            Log.d(TAG, "callQuery: 3");
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str5);
            sb5.append(" COLLATE LOCALIZED ASC");
            String sb6 = sb5.toString();
            ContentResolver contentResolver2 = getContentResolver();
            Cursor query3 = contentResolver2.query(uri2, null, null, null, sb6);
            if (query3.getCount() > 0) {
                while (query3.moveToNext()) {
                    @SuppressLint("Range") int i5 = query3.getInt(query3.getColumnIndex(str4));
                    @SuppressLint("Range") String string2 = query3.getString(query3.getColumnIndex(str5));
                    @SuppressLint("Range") int parseInt = Integer.parseInt(query3.getString(query3.getColumnIndex(str6)));
                    Log.d(TAG, "callQuery: 4");
                    if (parseInt > 0) {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(str7);
                        sb7.append(" = ?");
                        String sb8 = sb7.toString();
                        String[] strArr2 = new String[i2];
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("");
                        sb9.append(i5);
                        strArr2[c] = sb9.toString();
                        String str9 = string2;
                        Cursor query4 = contentResolver2.query(uri3, null, sb8, strArr2, null);
                        while (query4.moveToNext()) {
                            @SuppressLint("Range") String replaceAll = query4.getString(query4.getColumnIndex(str8)).replace("(", "").replace(")", "").replace(" ", "").replace("+9", "").replace("-", "").replaceAll("\\s+", "");
                            if (replaceAll.startsWith("00905") || replaceAll.startsWith("05") || Locale.getDefault().getCountry() != "TR") {
                                if (str9.equals(replaceAll)) {
                                    str = str9;
                                } else {
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append(str9);
                                    sb10.append(" (");
                                    sb10.append(replaceAll);
                                    sb10.append(")");
                                    str = sb10.toString();
                                }
                                MyClass myClass = new MyClass(replaceAll, str, false);
                                if (!this.kisiler.contains(myClass)) {
                                    this.kisiler.add(myClass);
                                    if (i5 > 0) {
                                        this.kisiIDtelefonlari.add(new KisiIDTelefonlar(i5, replaceAll));
                                    }
                                }
                            }
                        }
                        query4.close();
                    }
                    i2 = 1;
                    c = 0;
                }
            }
            if (isNetworkAvailable()) {
                GenelDuyurular();
            }
        }catch (Exception e){}
    }

    public void sendSmsMsgFnc(String mblNumVar, String smsMsgVar, Context context) {
        Log.i("sendSmsMsgFnc", "sendSmsMsgFnc: nmbr:");

        try {
            SmsManager smsMgrVar = SmsManager.getDefault();
            smsMgrVar.sendTextMessage(mblNumVar, null, smsMsgVar, null, null);
            Toast.makeText(
                    context, context.getString(R.string.sent_sms),
                    Toast.LENGTH_LONG
            ).show();
        } catch (Exception ErrVar) {
            Log.i("TAG", "sendSmsMsgFnc: nmbr: error:" + ErrVar.getMessage());

            Toast.makeText(
                    context, ErrVar.getMessage().toString(),
                    Toast.LENGTH_LONG
            ).show();
            ErrVar.printStackTrace();
        }

    }

    public void composeSMSMessage(String str, String str2) {
        Intent intent = new Intent("android.intent.action.SENDTO");
        StringBuilder sb = new StringBuilder();
        sb.append("smsto:");
        sb.append(str2);
        intent.setData(Uri.parse(sb.toString()));
        intent.putExtra("sms_body", str);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /* access modifiers changed from: private */
    public String arrangeContactList(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            if (i == 0) {
                sb = new StringBuilder((String) arrayList.get(i));
            } else {
                sb.append(",");
                sb.append((String) arrayList.get(i));
            }
        }
        return sb.toString();
    }
}
