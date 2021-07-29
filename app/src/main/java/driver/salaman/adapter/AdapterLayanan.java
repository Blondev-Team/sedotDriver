package driver.salaman.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import driver.salaman.Kelas.LayananKu;
import driver.salaman.R;
import driver.salaman.activity.DetailPengangkutanActivity;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterLayanan extends RecyclerView.Adapter<AdapterLayanan.MyViewHolder> {

    private Context mContext;
    private List<LayananKu> layananList;
    Button btnDetail;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvKodeAngkut,tvTotalBiaya,tvTotalPesanan,tvWaktu;
        public LinearLayout lineLayanan;
        Button btnDetail;


        public MyViewHolder(View view) {
            super(view);
            tvKodeAngkut = view.findViewById(R.id.tvKodeAngkut);
            tvTotalPesanan = view.findViewById(R.id.tvTotalPesanan);
            tvTotalBiaya = view.findViewById(R.id.tvTotalBiaya);
            tvWaktu = view.findViewById(R.id.tvWaktu);
            lineLayanan = view.findViewById(R.id.lineLayanan);
            btnDetail = view.findViewById(R.id.btnDetail);


        }
    }

    public AdapterLayanan(Context mContext, List<LayananKu> layananList) {
        this.mContext = mContext;
        this.layananList = layananList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mypesanan, parent,false);

        btnDetail = itemView.findViewById(R.id.btnDetail);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (layananList.isEmpty()){
            Log.d("isi Layanan: ",""+layananList.size());
        }else {

            Resources res = mContext.getResources();

            final LayananKu layanan  = layananList.get(position);
            holder.tvKodeAngkut.setText(layanan.getKodepengangkutan());
            holder.tvTotalPesanan.setText(layanan.getTotalpesanan());
            holder.tvWaktu.setText(layanan.getWaktuangkut());
            holder.tvTotalBiaya.setText(layanan.getTotaltransaksi());

            /*holder.lineLayanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (v.getContext(), DetailPengangkutanActivity.class);
                    intent.putExtra("kodeangkut",layanan.getKodepengangkutan());
                    v.getContext().startActivity(intent);
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        //return namaWisata.length;
        return layananList.size();
    }
}
