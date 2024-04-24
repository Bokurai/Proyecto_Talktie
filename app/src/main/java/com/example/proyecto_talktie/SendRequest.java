package com.example.proyecto_talktie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SendRequest extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View rootView = inflater.inflate(R.layout.fragment_send_request, container, false);

        // Buscar los botones dentro del layout inflado
        Button btn1= rootView.findViewById(R.id.btn1);
        Button btn2 = rootView.findViewById(R.id.btn2);
        Button btn3 = rootView.findViewById(R.id.btn3);
        Button btn4 = rootView.findViewById(R.id.btn4);
        Button btn5 = rootView.findViewById(R.id.btn5);
        Button btn6 = rootView.findViewById(R.id.btn6);

        // Configurar el OnClickListener para cada botón
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deseleccionar otros botones y cambiar color
                btn1.setSelected(true);
                btn2.setSelected(false);
                btn3.setSelected(false);
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(false);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deseleccionar otros botones y cambiar color
                btn1.setSelected(false);
                btn2.setSelected(true);
                btn3.setSelected(false);
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(false);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deseleccionar otros botones y cambiar color
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(true);
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(false);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deseleccionar otros botones y cambiar color
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(false);
                btn4.setSelected(true);
                btn5.setSelected(false);
                btn6.setSelected(false);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deseleccionar otros botones y cambiar color
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(false);
                btn4.setSelected(false);
                btn5.setSelected(true);
                btn6.setSelected(false);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deseleccionar otros botones y cambiar color
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(false);
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(true);
            }
        });

        // Devolver la vista raíz del layout inflado
        return rootView;
    }
}
