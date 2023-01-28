package com.auto.sms.SmsBlaster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.auto.sms.R;

import java.util.ArrayList;

public class RaporActivity extends Activity implements CustomModel.OnCustomStateListener {
    MyCustomAdapter dataAdapter;
    ArrayList<String> iletimDizisi;
    ListView lvSonuclar;
    ProgressDialog pDialog;
    String title = "";

    private class MyCustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> kisiList;

        private class ViewHolder {
            TextView tview;

            private ViewHolder() {
            }
        }

        MyCustomAdapter(Context context, int i, ArrayList<String> arrayList) {
            super(context, i, arrayList);
            this.kisiList = arrayList;
        }

        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = ((LayoutInflater) RaporActivity.this.getSystemService("layout_inflater")).inflate(R.layout.rapor_list, null);
                viewHolder = new ViewHolder();
                viewHolder.tview = (TextView) view.findViewById(R.id.tvSonuc);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tview.setText((String) this.kisiList.get(i));
            return view;
        }

        public int getCount() {
            return this.kisiList.size();
        }

        @Nullable
        public String getItem(int i) {
            return (String) this.kisiList.get(i);
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_rapor);
        CustomModel.getInstance().setListener(this);
        this.lvSonuclar = (ListView) findViewById(R.id.lvSonuclar);
        this.iletimDizisi = new ArrayList<>();
        this.iletimDizisi.add("İletim raporları bekleniyor");
        this.dataAdapter = new MyCustomAdapter(this, R.layout.rapor_list, this.iletimDizisi);
        this.lvSonuclar.setAdapter(this.dataAdapter);
        this.pDialog = new ProgressDialog(this);
        this.pDialog.setCancelable(false);
        this.pDialog.setTitle(getResources().getString(R.string.lutfen_bekleyin));
        this.pDialog.setMessage(getResources().getString(R.string.raporlar_yukleniyor));
        this.pDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
        this.pDialog.show();
    }

    private void GetListSonuc() {
        int size = AnaActivity.iletimRaporu.size();
        this.iletimDizisi.clear();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            ArrayList<String> arrayList = this.iletimDizisi;
            StringBuilder sb = new StringBuilder();
            sb.append(((MyClass) AnaActivity.iletimRaporu.get(i2)).value);
            sb.append(": ");
            sb.append(((MyClass) AnaActivity.iletimRaporu.get(i2)).name);
            arrayList.add(sb.toString());
            this.dataAdapter.notifyDataSetChanged();
            if (((MyClass) AnaActivity.iletimRaporu.get(i2)).name.equals(getResources().getString(R.string.iletildi))) {
                i++;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(i);
        sb2.append(" / ");
        sb2.append(size);
        this.title = sb2.toString();
        this.pDialog.dismiss();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getResources().getString(R.string.title_activity_rapor));
        sb3.append(" - ");
        sb3.append(this.title);
        this.title = sb3.toString();
        setTitle(this.title);
    }

    public void stateChanged() {
        if (CustomModel.getInstance().getState()) {
            GetListSonuc();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rapor_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.rmGeri /*2131165292*/:
                finish();
                return true;
            case R.id.rmYenile /*2131165293*/:
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
