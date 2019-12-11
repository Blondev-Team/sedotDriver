package driver.gosedot.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import driver.gosedot.Kelas.SedotBiasa;
import driver.gosedot.R;
import driver.gosedot.adapter.AdapterSedotBiasa;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSedotBiasa extends Fragment {


    public FragmentSedotBiasa() {
        // Required empty public constructor
    }

    RecyclerView rvLayanan;
    AdapterSedotBiasa adapter;
    private List<SedotBiasa> sedotBiasaList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_sedot_biasa, container, false);

        rvLayanan       = view.findViewById(R.id.rvLayanan);
        sedotBiasaList  = new ArrayList<>();
        adapter         = new AdapterSedotBiasa(getActivity(),sedotBiasaList);

        rvLayanan.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvLayanan.setHasFixedSize(true);
        rvLayanan.setItemAnimator(new DefaultItemAnimator());
        rvLayanan.setAdapter(adapter);

        sedotBiasaList.clear();


        adapter.notifyDataSetChanged();

        return view;
    }






}
