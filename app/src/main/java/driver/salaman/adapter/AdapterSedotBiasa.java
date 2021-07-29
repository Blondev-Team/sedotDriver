package driver.salaman.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import driver.salaman.Kelas.SedotBiasa;
import driver.salaman.R;
import driver.salaman.activity.PemesananPenyedotanActivity;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterSedotBiasa extends RecyclerView.Adapter<AdapterSedotBiasa.MyViewHolder> {

    private Context mContext;
    private List<SedotBiasa> sedotBiasaList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNamaLayanan,tvHarga;
        public ImageView imgLayanan;
        public LinearLayout lineLayanan;
        Button btnPesan;


        public MyViewHolder(View view) {
            super(view);
            tvNamaLayanan = view.findViewById(R.id.tvNamaLayanan);
            tvHarga= view.findViewById(R.id.tvHarga);
            imgLayanan = view.findViewById(R.id.ivLayanan);
            lineLayanan = view.findViewById(R.id.lineLayanan);
            btnPesan = view.findViewById(R.id.btnPesan);


        }
    }

    public AdapterSedotBiasa(Context mContext, List<SedotBiasa> kategoriList) {
        this.mContext = mContext;
        this.sedotBiasaList = kategoriList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layanan_biasa, parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (sedotBiasaList.isEmpty()){

            Log.d("isi Layanan: ",""+sedotBiasaList.size());
        }else {

            Resources res = mContext.getResources();

            final SedotBiasa sedotBiasa  = sedotBiasaList.get(position);

            holder.tvNamaLayanan.setText(sedotBiasa.getNamaLayanan());
            final String metodeBayar  = "COD";
            final String paket        = "sedot Biasa";

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            Double myHarga = Double.valueOf(sedotBiasa.getHarga());
            holder.tvHarga.setText(""+formatRupiah.format((double) myHarga));

            Glide.with(mContext).load(sedotBiasa.getUrlGambar())
                    .asBitmap()
                    //.fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgLayanan);

            holder.lineLayanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PemesananPenyedotanActivity.class);
                    intent.putExtra("sedotbiasa",sedotBiasa);
                    intent.putExtra("metodeBayar",metodeBayar);
                    intent.putExtra("paket",paket);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            holder.btnPesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PemesananPenyedotanActivity.class);
                    intent.putExtra("sedotbiasa",sedotBiasa);
                    intent.putExtra("metodeBayar",metodeBayar);
                    intent.putExtra("paket",paket);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });




        }

    }


    @Override
    public int getItemCount() {
        //return namaWisata.length;
        return sedotBiasaList.size();
    }
}
