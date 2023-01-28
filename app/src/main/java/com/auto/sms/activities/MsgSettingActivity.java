package com.auto.sms.activities;

import static android.widget.LinearLayout.VERTICAL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.sms.R;
import com.auto.sms.SmsBlaster.AnaActivity;
import com.auto.sms.SmsBlaster.AnaActivityPermissionsDispatcher;
import com.auto.sms.SmsBlaster.Bilgiler;
import com.auto.sms.SmsBlaster.CustomModel;
import com.auto.sms.SmsBlaster.GuruptakiKisiler;
import com.auto.sms.SmsBlaster.KisiIDTelefonlar;
import com.auto.sms.SmsBlaster.MyClass;
import com.auto.sms.SmsBlaster.RaporActivity;
import com.auto.sms.adapters.CallLogAdapter;
import com.auto.sms.adapters.CallLogAdapter2;
import com.auto.sms.databinding.ActivityMsgSettingBinding;
import com.auto.sms.databinding.LanguageLayoutBinding;
import com.auto.sms.databinding.MessageLayout2Binding;
import com.auto.sms.interfaces.OnContactSelectListener;
import com.auto.sms.models.CallLogModel;
import com.auto.sms.models.MissedCallModel;
import com.auto.sms.utils.SharePrefs;
import com.google.android.material.textfield.TextInputLayout;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import permissions.dispatcher.NeedsPermission;

public class MsgSettingActivity extends AppCompatActivity implements OnContactSelectListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TAG = "SYSYSY";
    static ArrayList<MyClass> iletimRaporu = new ArrayList<>();
    Boolean RehberYuklendi = Boolean.valueOf(false);
    AlertDialog.Builder alertMessage = null;
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

    @Override
    public void onItemCallback(@NonNull CallLogModel callLogModel) {
        for (int i = 0; i < call_list.size(); i++) {
            if (call_list.get(i).getUser_nmbr() == callLogModel.getUser_nmbr()) {
                call_list.set(i, callLogModel);
                Log.i(TAG, "onItemCallback: callLogModel:nmbr:" + callLogModel.getUser_nmbr() + "-->days:" + callLogModel.getDays());
            }
        }
    }


    private class AsyncTaskClassRehber extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        private AsyncTaskClassRehber() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            MsgSettingActivity.this.gruplar.clear();
            MsgSettingActivity.this.kisiler.clear();
            MsgSettingActivity.this.gruptakikisiler.clear();
            MsgSettingActivity.this.kisiIDtelefonlari.clear();
            this.pDialog = new ProgressDialog(MsgSettingActivity.this);
            this.pDialog.setCancelable(false);
            this.pDialog.setTitle(MsgSettingActivity.this.bilgiler.lutfen_bekleyin);
            this.pDialog.setMessage(MsgSettingActivity.this.bilgiler.rehber_yukleniyor);
            this.pDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
            this.pDialog.show();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            MsgSettingActivity.this.callQuery();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            try {
                this.pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (MsgSettingActivity.this.grupGosterimde.booleanValue()) {
                MsgSettingActivity anaActivity = MsgSettingActivity.this;
                anaActivity.GetListGrup(anaActivity.gruplar);
                MsgSettingActivity anaActivity2 = MsgSettingActivity.this;
                StringBuilder sb = new StringBuilder();
                sb.append(MsgSettingActivity.this.bilgiler.ApplicationTitle);
                sb.append(" - ");
                sb.append(MsgSettingActivity.this.gruplar.size());
                sb.append(MsgSettingActivity.this.bilgiler.grup);
                anaActivity2.setTitle(sb.toString());
                return;
            }
            MsgSettingActivity anaActivity3 = MsgSettingActivity.this;
            anaActivity3.GetListGrup(anaActivity3.kisiler);
            MsgSettingActivity anaActivity4 = MsgSettingActivity.this;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(MsgSettingActivity.this.bilgiler.ApplicationTitle);
            sb2.append(" - ");
            sb2.append(MsgSettingActivity.this.kisiler.size());
            sb2.append(MsgSettingActivity.this.bilgiler.kisi);
            anaActivity4.setTitle(sb2.toString());
        }
    }

    private class AsyncTaskClassRehberYukle extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        private AsyncTaskClassRehberYukle() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            MsgSettingActivity.this.gruplar.clear();
            MsgSettingActivity.this.kisiler.clear();
            MsgSettingActivity.this.gruptakikisiler.clear();
            MsgSettingActivity.this.kisiIDtelefonlari.clear();
            this.pDialog = new ProgressDialog(MsgSettingActivity.this);
            this.pDialog.setCancelable(false);
            this.pDialog.setTitle(MsgSettingActivity.this.bilgiler.lutfen_bekleyin);
            this.pDialog.setMessage(MsgSettingActivity.this.bilgiler.rehber_yukleniyor);
            this.pDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
            this.pDialog.show();
            Log.d(AnaActivity.TAG, "onPreExecute:  - AsyncTaskClassRehberYukle ");
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            int i = MsgSettingActivity.this.pref.getInt("gruplar_size", 0);
            for (int i2 = 0; i2 < i; i2++) {
                SharedPreferences sharedPreferences = MsgSettingActivity.this.pref;
                StringBuilder sb = new StringBuilder();
                sb.append("gname_");
                sb.append(i2);
                String string = sharedPreferences.getString(sb.toString(), "");
                SharedPreferences sharedPreferences2 = MsgSettingActivity.this.pref;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("gvalue_");
                sb2.append(i2);
                MsgSettingActivity.this.gruplar.add(new MyClass(sharedPreferences2.getString(sb2.toString(), ""), string, false));
            }
            int i3 = MsgSettingActivity.this.pref.getInt("gruptakikisiler_size", 0);
            for (int i4 = 0; i4 < i3; i4++) {
                SharedPreferences sharedPreferences3 = MsgSettingActivity.this.pref;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("gruptakikisilerGRUPID_");
                sb3.append(i4);
                int i5 = sharedPreferences3.getInt(sb3.toString(), 0);
                SharedPreferences sharedPreferences4 = MsgSettingActivity.this.pref;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("gruptakikisilerKISIID_");
                sb4.append(i4);
                MsgSettingActivity.this.gruptakikisiler.add(new GuruptakiKisiler(i5, sharedPreferences4.getInt(sb4.toString(), 0)));
            }
            int i6 = MsgSettingActivity.this.pref.getInt("kisiIDtelefonlari_size", 0);
            for (int i7 = 0; i7 < i6; i7++) {
                SharedPreferences sharedPreferences5 = MsgSettingActivity.this.pref;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("kisiIDtelefonlariTELEFON_");
                sb5.append(i7);
                String string2 = sharedPreferences5.getString(sb5.toString(), "");
                SharedPreferences sharedPreferences6 = MsgSettingActivity.this.pref;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("kisiIDtelefonlariKISIID_");
                sb6.append(i7);
                MsgSettingActivity.this.kisiIDtelefonlari.add(new KisiIDTelefonlar(sharedPreferences6.getInt(sb6.toString(), 0), string2));
            }
            int i8 = MsgSettingActivity.this.pref.getInt("kisiler_size", 0);
            for (int i9 = 0; i9 < i8; i9++) {
                SharedPreferences sharedPreferences7 = MsgSettingActivity.this.pref;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("name_");
                sb7.append(i9);
                String string3 = sharedPreferences7.getString(sb7.toString(), "");
                SharedPreferences sharedPreferences8 = MsgSettingActivity.this.pref;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("value_");
                sb8.append(i9);
                MsgSettingActivity.this.kisiler.add(new MyClass(sharedPreferences8.getString(sb8.toString(), ""), string3, false));
            }
            if (MsgSettingActivity.this.isNetworkAvailable()) {
                MsgSettingActivity.this.GenelDuyurular();
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
            MsgSettingActivity anaActivity = MsgSettingActivity.this;
            anaActivity.GetListGrup(anaActivity.kisiler);
            MsgSettingActivity anaActivity2 = MsgSettingActivity.this;
            StringBuilder sb = new StringBuilder();
            sb.append(MsgSettingActivity.this.bilgiler.ApplicationTitle);
            sb.append(" - ");
            sb.append(MsgSettingActivity.this.kisiler.size());
            sb.append(" ");
            sb.append(MsgSettingActivity.this.bilgiler.kisi);
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
            this.pDialog = new ProgressDialog(MsgSettingActivity.this);
            this.pDialog.setCancelable(false);
            this.pDialog.setTitle(MsgSettingActivity.this.bilgiler.lutfen_bekleyin);
            this.pDialog.setMessage(MsgSettingActivity.this.bilgiler.mesajlar_gonderiliyor);
            this.pDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
            this.pDialog.setProgressStyle(1);
            this.pDialog.setProgress(0);
            this.pDialog.setMax(MsgSettingActivity.this.secilen);
            this.pDialog.show();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            MsgSettingActivity.iletimRaporu.clear();
            for (int i = MsgSettingActivity.this.index; i < MsgSettingActivity.this.gonderimListesi.size(); i++) {
                MsgSettingActivity anaActivity = MsgSettingActivity.this;
                anaActivity.gsmNo = (String) anaActivity.gonderimListesi.get(i);
                MsgSettingActivity anaActivity2 = MsgSettingActivity.this;
                anaActivity2.sendSMS(anaActivity2.gsmNo, this.message);
                MsgSettingActivity.iletimRaporu.add(new MyClass(MsgSettingActivity.this.gsmNo, MsgSettingActivity.this.bilgiler.bekleniyor, false));
                MsgSettingActivity.this.gonderilen++;
            }
            MsgSettingActivity.this.maximumGonderim -= MsgSettingActivity.this.gonderilen;
            this.pDialog.setProgress(MsgSettingActivity.this.gonderilen);
            MsgSettingActivity.this.pref.edit().putInt("maximumGonderim", MsgSettingActivity.this.maximumGonderim).apply();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            try {
                this.pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Context applicationContext = MsgSettingActivity.this.getApplicationContext();
            StringBuilder sb = new StringBuilder();
            sb.append(MsgSettingActivity.this.gonderilen);
            sb.append(" ");
            sb.append(MsgSettingActivity.this.bilgiler.mesaj_gonderildi);
            Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
            if (MsgSettingActivity.iletimRaporu.size() > 0) {
                MsgSettingActivity.this.startActivity(new Intent(MsgSettingActivity.this, RaporActivity.class));
            }
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<MyClass> {
        /* access modifiers changed from: private */
        public ArrayList<MyClass> kisiList = new ArrayList<>();
        MissedCallModel saveContactModel;
        ArrayList<String> inContactList;

        private class ViewHolder {
            CheckBox cbox;

            private ViewHolder() {
                saveContactModel = SharePrefs.getInstance(getApplication()).getContactsObject(getApplication());
            }
        }

        MyCustomAdapter(Context context, int i, ArrayList<MyClass> arrayList) {
            super(context, i, arrayList);
            this.kisiList.addAll(arrayList);
            inContactList = SharePrefs.getInstance(context).getExcludeInContactList(context);
//            if(inContactList == null){
//
//          //      String[] strings =  saveContactModel.getNumbers().split(",");
//                if(strings != null && strings.length > 0){
//                    for(String str : strings){
//                        savedNumbers.add(str);
//                    }
//                }
//
//            }
        }

        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            MsgSettingActivity.MyCustomAdapter.ViewHolder viewHolder;
            if (view == null) {
                view = ((LayoutInflater) MsgSettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.check_list, null);
                viewHolder = new MsgSettingActivity.MyCustomAdapter.ViewHolder();
                viewHolder.cbox = (CheckBox) view.findViewById(R.id.cbSecim);
                view.setTag(viewHolder);
                viewHolder.cbox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        CheckBox checkBox = (CheckBox) view;
                        ((MyClass) checkBox.getTag()).setSelected(checkBox.isChecked());
                        MsgSettingActivity.this.SecilenleriSay();
                    }
                });
            } else {
                viewHolder = (MsgSettingActivity.MyCustomAdapter.ViewHolder) view.getTag();
            }
            MyClass myClass = (MyClass) this.kisiList.get(i);
            viewHolder.cbox.setText(myClass.getName());
            viewHolder.cbox.setChecked(myClass.isSelected());
            viewHolder.cbox.setTag(myClass);
//            Log.i("TAG65","number :"+myClass.getValue());
//            Log.i("TAG65","numberas :"+saveContactModel.getNumbers());
            if (!inContactList.isEmpty()) {
                if (inContactList.contains(myClass.getValue())) {
                    viewHolder.cbox.setChecked(true);
                    ((MyClass) viewHolder.cbox.getTag()).setSelected(viewHolder.cbox.isChecked());
                    MsgSettingActivity.this.SecilenleriSay();
                }
            }
            return view;
        }
    }

    ActivityMsgSettingBinding binding;
    ArrayList<CallLogModel> call_list;
    String selectedLanguage;
    ArrayList<String> messagesList;
    String missCallMsg="";
    String receiveCallMsg="";
    ArrayAdapter<String> dataAdapter1;
    ArrayAdapter<String> dataAdapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_setting);
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
        this.alertMessage = new AlertDialog.Builder(this);
        this.pref = getSharedPreferences("SinirsizSMS", 0);
        this.RehberYuklendi = Boolean.valueOf(this.pref.getBoolean("RehberYuklendi", false));
        this.maximumGonderim = this.pref.getInt("maximumGonderim", 1000);
        this.tvListeBaslik = binding.tvListeBaslik;
        AnaActivityPermissionsDispatcher.RehberYukleWithPermissionCheck(this);
        ((CheckBox) findViewById(R.id.cbTumunuSec)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MsgSettingActivity.this.cbTumunuSec(Boolean.valueOf(z));
            }
        });




        // Spinner click listener
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                missCallMsg =  messagesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner click listener
        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receiveCallMsg =  messagesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

             setUpAdapter();

        binding.btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      showReceivedMessageDialog();
            }
        });

        binding.settingImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MsgSettingActivity.this);
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
                getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

                LanguageLayoutBinding mBinding =
                        LanguageLayoutBinding.inflate(LayoutInflater.from(MsgSettingActivity.this));
                //   LanguageLayoutBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(MsgSettingActivity.this), R.layout.language_layout, null, false);
                dialog.setContentView(mBinding.getRoot());
                dialog.setCancelable(true);
                dialog.show();

                selectedLanguage = SharePrefs.getInstance(MsgSettingActivity.this).getLANGUAGE();
                if (selectedLanguage == "en") {
                    mBinding.englishBtn.setChecked(true);
                } else if (selectedLanguage == "iw") {
                    mBinding.hibroBtn.setChecked(true);
                }
                mBinding.languageRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.english_btn:
                                selectedLanguage = "en";
                                break;
                            case R.id.hibro_btn:
                                selectedLanguage = "iw";
                                break;
                        }
                    }
                });


                mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SharePrefs.getInstance(MsgSettingActivity.this).setLANGUAGE(selectedLanguage);
//        Log.i(
//                TAG,
//                "ChangeLanguage: language:" + SharePrefs.getInstance(requireContext())!!.lANGUAGE + "-->selected_language:" + selectedLanguage
//                );
                        startActivity(new Intent(MsgSettingActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                });

            }
        });

        binding.logoutImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePrefs.getInstance(MsgSettingActivity.this).resetDB();
                startActivity(new Intent(MsgSettingActivity.this, MainActivity.class));
                finishAffinity();
                // findNavController().navigate(R.id.action_userDashboardFragment_to_selectionFragment) ;
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (binding.inputDaysLayout.getEditText().getText().toString().isEmpty()) {
                    binding.inputDaysLayout.setError(getString(R.string.required));
                    return;
                }
//
//                if (binding.missInputLayout.getEditText().getText().toString().isEmpty())
//                    binding.missInputLayout.setError(getString(R.string.required));
//
//                if (binding.receiveInputLayout.getEditText().getText().toString().isEmpty())
//                    binding.receiveInputLayout.setError(getString(R.string.required));
//
//                if (!binding.inputDaysLayout.getEditText().getText().toString().isEmpty()
//                        && !binding.missInputLayout.getEditText().getText().toString().isEmpty()
//                        && !binding.receiveInputLayout.getEditText().getText().toString().isEmpty()
//                ) {
//
//                }
                MsgSettingActivity.this.btnMesajGonder();

            }
        });

        int afterCount = SharePrefs.getInstance(this).getAfterCount(this);
        if (binding.radioButton1.isChecked()) {
            String missedCallMsg = SharePrefs.getInstance(this).getInMissedCallMsg(this);
            String receiveCallMsg = SharePrefs.getInstance(this).getInReceiveCallMsg(this);
//            binding.missInputLayout.getEditText().setText(missedCallMsg + "");
//            binding.receiveInputLayout.getEditText().setText(receiveCallMsg + "");
        } else {
            String missedCallMsg = SharePrefs.getInstance(this).getOutMissedCallMsg(this);
            String receiveCallMsg = SharePrefs.getInstance(this).getOutReceiveCallMsg(this);
//            binding.missInputLayout.getEditText().setText(missedCallMsg + "");
//            binding.receiveInputLayout.getEditText().setText(receiveCallMsg + "");
        }

        if (afterCount != 0) {
            binding.inputDaysLayout.getEditText().setText(afterCount + "");
        }

        getCallDetails();


        binding.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               // String missedCallMsg = "";
              //  String receiveCallMsg = "";
                switch (checkedId) {
                    case R.id.radioButton1:
                        binding.callLogRecyclerview.setVisibility(View.GONE);
                        binding.lvKisiler.setVisibility(View.VISIBLE);
                        binding.tv1.setVisibility(View.VISIBLE);
                        binding.tv2.setVisibility(View.GONE);
                        binding.tvListeBaslik.setVisibility(View.VISIBLE);

                      //  missedCallMsg = SharePrefs.getInstance(MsgSettingActivity.this).getInMissedCallMsg(MsgSettingActivity.this);
                      //  receiveCallMsg = SharePrefs.getInstance(MsgSettingActivity.this).getInReceiveCallMsg(MsgSettingActivity.this);
//                        binding.missInputLayout.getEditText().setText(missedCallMsg + "");
//                        binding.receiveInputLayout.getEditText().setText(receiveCallMsg + "");

                        break;
                    case R.id.radioButton2:
                        binding.lvKisiler.setVisibility(View.GONE);
                        binding.callLogRecyclerview.setVisibility(View.VISIBLE);
                        binding.tv1.setVisibility(View.GONE);
                        binding.tv2.setVisibility(View.VISIBLE);
                        binding.tvListeBaslik.setVisibility(View.GONE);

                     //   missedCallMsg = SharePrefs.getInstance(MsgSettingActivity.this).getOutMissedCallMsg(MsgSettingActivity.this);
                     //   receiveCallMsg = SharePrefs.getInstance(MsgSettingActivity.this).getOutReceiveCallMsg(MsgSettingActivity.this);
//                        binding.missInputLayout.getEditText().setText(missedCallMsg + "");
//                        binding.receiveInputLayout.getEditText().setText(receiveCallMsg + "");
                        break;

                    case R.id.radioButton3:
                        binding.lvKisiler.setVisibility(View.VISIBLE);
                        binding.callLogRecyclerview.setVisibility(View.VISIBLE);
                        binding.tv1.setVisibility(View.VISIBLE);
                        binding.tv2.setVisibility(View.VISIBLE);
                        binding.tvListeBaslik.setVisibility(View.VISIBLE);

                      //  missedCallMsg = SharePrefs.getInstance(MsgSettingActivity.this).getOutMissedCallMsg(MsgSettingActivity.this);
                      //  receiveCallMsg = SharePrefs.getInstance(MsgSettingActivity.this).getOutReceiveCallMsg(MsgSettingActivity.this);
//                        binding.missInputLayout.getEditText().setText(missedCallMsg + "");
//                        binding.receiveInputLayout.getEditText().setText(receiveCallMsg + "");
                        break;
                }
            }
        });
    }

    private void setUpAdapter() {
        messagesList = SharePrefs.getInstance(this).getMessagesList(this);



        // Creating adapter for spinner
        dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, messagesList);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        binding.spinner.setAdapter(dataAdapter1);


        // Creating adapter for spinner
        dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, messagesList);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        binding.spinner2.setAdapter(dataAdapter2);

//
//        if(!messagesList.isEmpty()){
//            if(binding.radioButton1.isChecked()){
//                String message1 = SharePrefs.getInstance(MsgSettingActivity.this).getInMissedCallMsg(MsgSettingActivity.this);
//                String message2 = SharePrefs.getInstance(MsgSettingActivity.this).getInReceiveCallMsg(MsgSettingActivity.this);
//                for (int position = 0; position < dataAdapter1.getCount(); position++) {
//                    if(dataAdapter1.getItem(position) == message1) {
//                        binding.spinner.setSelection(position);
//                       // return;
//                    }
//                }
//
//                for (int position = 0; position < dataAdapter2.getCount(); position++) {
//                    if(dataAdapter2.getItem(position) == message2) {
//                        binding.spinner2.setSelection(position);
//                       // return;
//                    }
//                }
//            }else if(binding.radioButton2.isChecked()){
//                String message1 = SharePrefs.getInstance(MsgSettingActivity.this).getOutMissedCallMsg(MsgSettingActivity.this);
//                String message2 = SharePrefs.getInstance(MsgSettingActivity.this).getOutReceiveCallMsg(MsgSettingActivity.this);
//                for (int position = 0; position < dataAdapter1.getCount(); position++) {
//                    if(dataAdapter1.getItem(position) == message1) {
//                        binding.spinner.setSelection(position);
//                        // return;
//                    }
//                }
//
//                for (int position = 0; position < dataAdapter2.getCount(); position++) {
//                    if(dataAdapter2.getItem(position) == message2) {
//                        binding.spinner2.setSelection(position);
//                        // return;
//                    }
//                }
//            }else{
//                String message1 = SharePrefs.getInstance(MsgSettingActivity.this).getInMissedCallMsg(MsgSettingActivity.this);
//                String message2 = SharePrefs.getInstance(MsgSettingActivity.this).getInReceiveCallMsg(MsgSettingActivity.this);
//                for (int position = 0; position < dataAdapter1.getCount(); position++) {
//                    if(dataAdapter1.getItem(position) == message1) {
//                        binding.spinner.setSelection(position);
//                        // return;
//                    }
//                }
//
//                for (int position = 0; position < dataAdapter2.getCount(); position++) {
//                    if(dataAdapter2.getItem(position) == message2) {
//                        binding.spinner2.setSelection(position);
//                        // return;
//                    }
//                }
//            }
//        }
    }

    private void showReceivedMessageDialog() {

        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        MessageLayout2Binding binding =
                MessageLayout2Binding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.show();


        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if(!messagesList.isEmpty()){
           binding.msg1.setText(messagesList.get(0));
           binding.msg2.setText(messagesList.get(1));
           binding.msg3.setText(messagesList.get(2));
           binding.msg4.setText(messagesList.get(3));
           binding.msg5.setText(messagesList.get(4));
        }

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> messages = new ArrayList<>();
                if (!binding.msg1.getText().toString().isEmpty()) {
                    messages.add(binding.msg1.getText().toString());
                }else{
                    messages.add("");
                }
                if (!binding.msg2.getText().toString().isEmpty()) {
                    messages.add(binding.msg2.getText().toString());
                }else{
                    messages.add("");
                }
                if (!binding.msg3.getText().toString().isEmpty()) {
                    messages.add(binding.msg3.getText().toString());
                }else{
                    messages.add("");
                }
                if (!binding.msg4.getText().toString().isEmpty()) {
                    messages.add(binding.msg4.getText().toString());
                }else{
                    messages.add("");
                }
                if (!binding.msg5.getText().toString().isEmpty()) {
                    messages.add(binding.msg5.getText().toString());
                }else{
                    messages.add("");
                }
                if(!messages.isEmpty()){
                    SharePrefs.getInstance(MsgSettingActivity.this).saveMessagesList(messages);
                    Toast.makeText(MsgSettingActivity.this, getString(R.string.save), Toast.LENGTH_SHORT).show();
                    setUpAdapter();
                }
                dialog.dismiss();


            }
        });


    }

    private String getCallDetails() {
        if (call_list == null)
            call_list = new ArrayList<>();
        if (call_list.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            Cursor managedCursor = managedQuery(
                    CallLog.Calls.CONTENT_URI, null,
                    null, null, null
            );
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            sb.append("Call Details :");
            while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

                CallLogModel callLogModel = new CallLogModel(phNumber, 0, false);

                Log.i(
                        TAG,
                        "getCallDetails: isContain:" + call_list.contains(callLogModel) + "-->nmbr:" + callLogModel.getUser_nmbr()
                );

                call_list.add(callLogModel);
                sb.append("Phone Number:--- " + phNumber + " Call Type:--- " + dir + "Call Date:--- " + "callDayTime Call duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");
            }


            managedCursor.close();
            setUpRecyclerview();
            Log.i(TAG, "getCallDetails: details:" + call_list.size());

            return sb.toString();
        } else {
            return null;
        }
    }

    private CallLogAdapter2 mAdapter;

    private void setUpRecyclerview() {
        Log.i(TAG, "setUpRecyclerview: listSize:" + call_list.size());
        HashSet<String> seen = new HashSet();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            call_list.removeIf(e -> !seen.add(e.getUser_nmbr()));
        }
        Log.i(TAG, "setUpRecyclerview: listSize:after:" + call_list.size());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        binding.callLogRecyclerview.setLayoutManager(layoutManager);
        mAdapter = new CallLogAdapter2(call_list, this, this);
        binding.callLogRecyclerview.setAdapter(mAdapter);
    }

    public void btnMesajGonder() {
        ArrayList inContactList = new ArrayList<String>();

        this.gonderimListesi.clear();
        String str = "";

        this.secilen = 0;
        for (int i2 = this.index; i2 < this.dataAdapter.getCount(); i2++) {
            String str1 = ((MyClass) this.dataAdapter.kisiList.get(i2)).getValue();
            if (str1 != null && !str1.isEmpty()) {
                inContactList.add(str1);
            }
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

        MsgSettingActivity anaActivity = MsgSettingActivity.this;
        SharePrefs.getInstance(this).saveAfterCount(this, Integer.parseInt(binding.inputDaysLayout.getEditText().getText().toString()));

        if (binding.radioButton1.isChecked()) {
            if (anaActivity.gonderimListesi.isEmpty()) {
                Toast.makeText(this, "Not Select any Contact from In Contacts!", Toast.LENGTH_LONG)
                        .show();
            } else {
                SharePrefs.getInstance(this).saveInReceiveCallMsg(this, receiveCallMsg);
                SharePrefs.getInstance(this).saveInMissedCallMsg(this, missCallMsg);
                SharePrefs.getInstance(this).saveExcludeInContactList(this, anaActivity.gonderimListesi);
                SharePrefs.getInstance(this).saveInContactList(this, inContactList);
                Toast.makeText(this, getString(R.string.save), Toast.LENGTH_LONG)
                        .show();
            }
        } else if (binding.radioButton2.isChecked()) {
            SharePrefs.getInstance(this).saveOutReceiveCallMsg(this,receiveCallMsg);
            SharePrefs.getInstance(this).saveOutMissedCallMsg(this, missCallMsg);
            saveOutContscts();
        } else {
            SharePrefs.getInstance(this).saveInReceiveCallMsg(this, receiveCallMsg);
            SharePrefs.getInstance(this).saveInMissedCallMsg(this, missCallMsg);
            SharePrefs.getInstance(this).saveInContactList(this, inContactList);

            SharePrefs.getInstance(this).saveOutReceiveCallMsg(this,receiveCallMsg);
            SharePrefs.getInstance(this).saveOutMissedCallMsg(this, missCallMsg);
            if (!anaActivity.gonderimListesi.isEmpty()) {
                SharePrefs.getInstance(this).saveExcludeInContactList(this, anaActivity.gonderimListesi);
            }
            saveOutContscts();
        }


    }

    /* access modifiers changed from: private */
//    public void btnMesajGonder() {
////        int length = this.tbMesaj.getText().toString().length();
////        int i = length > 160 ? 2 : 1;
////        if (length > 320) {
////            i++;
////        }
////        if (length > 480) {
////            i++;
////        }
////        if (length > 640) {
////            i++;
////        }
////        if (length > 800) {
////            i++;
////        }
//        this.gonderimListesi.clear();
//        String str = "";
////        if (length > 0)
////        {
//        this.secilen = 0;
//        for (int i2 = this.index; i2 < this.dataAdapter.getCount(); i2++) {
//            if (((MyClass) this.dataAdapter.kisiList.get(i2)).isSelected()) {
//                if (this.grupGosterimde.booleanValue()) {
//                    int parseInt = Integer.parseInt(((MyClass) this.dataAdapter.kisiList.get(i2)).getValue());
//                    for (int i3 = this.gruptakiKisiIndex; i3 < this.gruptakikisiler.size(); i3++) {
//                        if (((GuruptakiKisiler) this.gruptakikisiler.get(i3)).GurupID == parseInt) {
//                            int i4 = 0;
//                            while (true) {
//                                if (i4 >= this.kisiIDtelefonlari.size()) {
//                                    break;
//                                } else if (((GuruptakiKisiler) this.gruptakikisiler.get(i3)).KisiID == ((KisiIDTelefonlar) this.kisiIDtelefonlari.get(i4)).KisiID) {
//                                    str = ((KisiIDTelefonlar) this.kisiIDtelefonlari.get(i4)).Telefon;
//                                    break;
//                                } else {
//                                    i4++;
//                                }
//                            }
//                            this.gonderimListesi.add(str);
//                            this.secilen++;
//                        }
//                    }
//                } else {
//                    str = ((MyClass) this.dataAdapter.kisiList.get(i2)).getValue();
//                    this.gonderimListesi.add(str);
//                    this.secilen++;
//                }
//            }
//        }
//        int i5 = this.secilen;
//           /* if (i5 <= 0) {
//                this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(this.bilgiler.kisi_yada_grup_secilmemis).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                }).show();
//            }
//            else if (i5 > this.maximumGonderim) {
//                this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(this.bilgiler.ucretsiz_surum_uyarisi).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                }).show();
//            }
//            else {
//                StringBuilder sb = new StringBuilder();
//                sb.append(this.bilgiler.mesaj_gonderilsin_mi);
//                sb.append("\n\n");
//                sb.append(length);
//                sb.append(" ");
//                sb.append(this.bilgiler.karakter);
//                sb.append(" ");
//                sb.append(i);
//                sb.append(" SMS\n");
//                sb.append(this.secilen);
//                sb.append(" ");
//                sb.append(this.bilgiler.kisi);
//                this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(sb.toString()).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        AnaActivity anaActivity = AnaActivity.this;
//                        String access$600 = anaActivity.arrangeContactList(anaActivity.gonderimListesi);
//                        String valueOf = String.valueOf(AnaActivity.this.tbMesaj.getText());
//
//                        AnaActivity.this.composeSMSMessage(valueOf, access$600);
//                    }
//                }).setNegativeButton(R.string.btn_iptal, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                }).show();
//            }*/
//        MsgSettingActivity anaActivity = MsgSettingActivity.this;
//        SharePrefs.getInstance(this).saveAfterCount(this, Integer.parseInt(binding.inputDaysLayout.getEditText().getText().toString()));
//        if (binding.radioButton1.isChecked()) {
//            SharePrefs.getInstance(this).saveInContactList(this, anaActivity.gonderimListesi);
//            Toast.makeText(this, getString(R.string.save), Toast.LENGTH_LONG)
//                    .show();
//            finish();
//        } else if (binding.radioButton2.isChecked()) {
//            saveOutContscts();
//        } else {
//            SharePrefs.getInstance(this).saveInContactList(this, anaActivity.gonderimListesi);
//            saveOutContscts();
//            //  SharePrefs.getInstance(this).saveContactList(anaActivity.gonderimListesi);
//        }
//
//        //  String access$600 = anaActivity.arrangeContactList(anaActivity.gonderimListesi);
//
//        //     Log.i("myContactList","List :"+access$600);
//
//
//        //  String valueOf = String.valueOf(AnaActivity.this.tbMesaj.getText());
//        //   AnaActivity.this.composeSMSMessage(valueOf, access$600);
//        // sendSmsMsgFnc(access$600 ,valueOf, this);
//        //  }
////        else {
////            this.alertMessage.setIcon(R.drawable.ic_help_outline_black_24dp).setTitle(this.bilgiler.ApplicationTitle).setMessage(this.bilgiler.mesaj_yazmadiniz).setCancelable(true).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
////                public void onClick(DialogInterface dialogInterface, int i) {
////                    MsgSettingActivity.this.tbMesaj.requestFocus();
////                }
////            }).show();
////        }
//    }

    private void saveOutContscts() {
        ArrayList<String> outContactList = new ArrayList<>();
        if (binding.inputDaysLayout.getEditText().getText().toString().isEmpty()) {
            binding.inputDaysLayout.setError(getString(R.string.required));
        } else {
            boolean isContactsSelect = false;

            for (int i = 0; i < call_list.size(); i++) {
                String str1 = call_list.get(i).getUser_nmbr();
                if (str1 != null && !str1.isEmpty()) {
                    outContactList.add(str1);
                }
                if (call_list.get(i).isCheck()) {
                    isContactsSelect = true;
                    break;
                }
            }

            if (isContactsSelect) {
                ArrayList list = new ArrayList<String>();

                for (int i = 0; i < call_list.size(); i++) {
                    if (call_list.get(i).isCheck()) {
                        list.add(call_list.get(i).getUser_nmbr());
                    }
                }

//                    String nmbrs_string  = arrangeContactList(list);
                //  val valueOf: String = mBinding.messageEdittext.text.toString().trim()


                if (!list.isEmpty()) {
                    SharePrefs.getInstance(this).saveExcludeOutContactList(this, list);
                    SharePrefs.getInstance(this).saveOutContactList(this, outContactList);
                    Toast.makeText(this, getString(R.string.save), Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(this, "Not Select any Contact from Out Contacts!", Toast.LENGTH_LONG)
                            .show();
                }

//                        composeSMSMessage(valueOf, nmbrs_string)
            } else {
                Toast.makeText(this, "Not Select any Contact from Out Contacts!", Toast.LENGTH_LONG)
                        .show();
            }
        }

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

    /* access modifiers changed from: 0000 */
    @SuppressLint("MissingPermission")
    @NeedsPermission({"android.permission.READ_CONTACTS"})
    public void RehberYukle() {
        if (!this.RehberYuklendi.booleanValue()) {
            new MsgSettingActivity.AsyncTaskClassRehber().execute(new String[0]);
        } else {
            new MsgSettingActivity.AsyncTaskClassRehberYukle().execute(new String[0]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: private */
    public void GetListGrup(ArrayList<MyClass> arrayList) {
        ListView listView = binding.lvKisiler;
        this.dataAdapter = new MsgSettingActivity.MyCustomAdapter(this, R.layout.check_list, arrayList);
        listView.setAdapter(this.dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Toast.makeText(MsgSettingActivity.this.getApplicationContext(), ((MyClass) adapterView.getItemAtPosition(i)).getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void callQuery() {
        try {
            String str;
            int i;
            String str2 = "_id";
            String str3 = "title";
            Cursor query = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, "deleted = 0", null, null);
            int i2 = 1;
            char c = 0;
            if (query.getCount() > 0) {
                while (query.moveToNext()) {
                    @SuppressLint("Range") int i3 = query.getInt(query.getColumnIndex(str2));
                    @SuppressLint("Range") String string = query.getString(query.getColumnIndex(str3));
                    Uri uri = ContactsContract.Data.CONTENT_URI;
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
            Uri uri2 = ContactsContract.Contacts.CONTENT_URI;
            String str4 = "_id";
            String str5 = "display_name";
            String str6 = "has_phone_number";
            Uri uri3 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
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
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public boolean isNetworkAvailable() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
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
                        if (soapObject2.contains("|") && soapObject2.indexOf(MsgSettingActivity.this.currentLang) == 0) {
                            String substring = soapObject2.substring(soapObject2.indexOf("|") + 1);
                            Looper.prepare();
                            MsgSettingActivity.this.alertMessage.setCancelable(true).setTitle(MsgSettingActivity.this.bilgiler.ApplicationTitle).setMessage(substring).setIcon(R.drawable.ic_info_outline_black_24dp).setPositiveButton(R.string.btn_tamam, new DialogInterface.OnClickListener() {
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
                            Toast.makeText(MsgSettingActivity.this.getBaseContext(), "Genel hata", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(MsgSettingActivity.this.getBaseContext(), "Sinyal kapal�", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(MsgSettingActivity.this.getBaseContext(), "PDU bos", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(MsgSettingActivity.this.getBaseContext(), "Servis yok", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                if (MsgSettingActivity.this.tektek_gonder.booleanValue()) {
                    MsgSettingActivity.this.unregisterReceiver(this);
                }
            }
        }, new IntentFilter(str3));
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case -1:
                        Iterator it = MsgSettingActivity.iletimRaporu.iterator();
                        while (it.hasNext()) {
                            MyClass myClass = (MyClass) it.next();
                            if (myClass.value.equals(str)) {
                                myClass.name = MsgSettingActivity.this.bilgiler.iletildi;
                                CustomModel.getInstance().changeState(true);
                            }
                        }
                        break;
                    case 0:
                        Iterator it2 = MsgSettingActivity.iletimRaporu.iterator();
                        while (it2.hasNext()) {
                            MyClass myClass2 = (MyClass) it2.next();
                            if (myClass2.value.equals(str)) {
                                myClass2.name = MsgSettingActivity.this.bilgiler.basarisiz;
                                CustomModel.getInstance().changeState(false);
                            }
                        }
                        break;
                }
                MsgSettingActivity.this.isBusy = Boolean.valueOf(false);
                if (MsgSettingActivity.this.tektek_gonder.booleanValue()) {
                    MsgSettingActivity.this.unregisterReceiver(this);
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


}
