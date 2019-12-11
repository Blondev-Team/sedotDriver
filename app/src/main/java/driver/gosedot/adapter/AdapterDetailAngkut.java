package driver.gosedot.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import driver.gosedot.Kelas.DetailAngkut;
import driver.gosedot.R;

public class AdapterDetailAngkut extends RecyclerView.Adapter<AdapterDetailAngkut.MyViewHolder>{
    private Context mContext;
    private List<DetailAngkut> angkutList;
    Button btnNavigasi;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama,tvAlamat,tvPaket,tvMetode,tvPetugas;
        public LinearLayout lineLayanan;


        public MyViewHolder(View view) {
            super(view);
            tvNama = (TextView) view.findViewById(R.id.tvNamaPemesan);
            tvAlamat = (TextView) view.findViewById(R.id.tvAlamat);
            tvPaket = (TextView) view.findViewById(R.id.tvPaket);
            tvMetode = (TextView) view.findViewById(R.id.tvMetodebayar);
            tvPetugas = (TextView) view.findViewById(R.id.tvPetugas);

        }
    }

    public AdapterDetailAngkut(Context mContext, List<DetailAngkut> angkutList) {
        this.mContext = mContext;
        this.angkutList = angkutList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_angkut, parent,false);

        btnNavigasi = itemView.findViewById(R.id.btnNavigasi);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (angkutList.isEmpty()){

            Log.d("isi Angkutan: ",""+angkutList.size());
        }else {

            Resources res = mContext.getResources();

            final DetailAngkut angkut  = angkutList.get(position);
            //holder.tvKodeAngkut.setText(layanan.getKodepengangkutan());
            holder.tvNama.setText(angkut.getNmuser());
            holder.tvAlamat.setText(angkut.getAlamat());
            holder.tvPaket.setText(angkut.getPaket());
            holder.tvMetode.setText(angkut.getMetodebayar());
            holder.tvPetugas.setText(angkut.getPetugas());

            btnNavigasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+angkut.getLatitude()+","+angkut.getLongitude()+"");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                        v.getContext().startActivity(mapIntent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        //return namaWisata.length;
        return angkutList.size();
    }


}
