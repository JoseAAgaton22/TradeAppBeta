package com.app.tradeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tradeapp.R;

import java.lang.reflect.Array;

public class AmortizacionFragment extends Fragment {

    Button amortizar;
    Spinner spinner;
    EditText credito, periodos, cuota, tasaInteres;
    TextView amortizacion;

    public AmortizacionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        credito = view.findViewById(R.id.txt_credito);

        tasaInteres = view.findViewById(R.id.txt_tasaInteres);
        tasaInteres.setVisibility(View.GONE);

        periodos = view.findViewById(R.id.txt_periodos);
        cuota = view.findViewById(R.id.txt_cuota);
        amortizacion = view.findViewById(R.id.amortizacion);
        spinner = view.findViewById(R.id.spinner);

        String[] bancos = {"-", "Bancolombia", "Banco de Bogotá", "Banco de Occidente", "Banco Popular", "Banco Davivienda", "Banco BBVA", "Otro"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.spinner_item_bancos, bancos);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 7){
                    tasaInteres.setVisibility(View.VISIBLE);
                }
                else{
                    tasaInteres.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        amortizar = view.findViewById(R.id.boton_amortizar);

        amortizar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String creditoString = credito.getText().toString();
                String periodosString = periodos.getText().toString();
                String cuotaString = cuota.getText().toString();

                if (TextUtils.isEmpty(creditoString)) {
                    Toast.makeText(getActivity(), "El valor del crédito es un campo requerido", Toast.LENGTH_LONG).show();
                }

                else if (TextUtils.isEmpty(periodosString) && TextUtils.isEmpty(cuotaString)) {
                    Toast.makeText(getActivity(), "Ingrese el número de periodos o el valor de la cuota", Toast.LENGTH_LONG).show();
                }

                else {

                    double creditoDouble = Double.parseDouble(creditoString);
                    int periodosInt = Integer.parseInt(periodosString);
                    double cuotaInt = Double.parseDouble(cuotaString);

                    String seleccion = spinner.getSelectedItem().toString();

                    if (seleccion.equals("-")) {
                        Toast.makeText(getActivity(), "Especifique la entidad bancaria", Toast.LENGTH_LONG).show();
                    } else if (seleccion.equals("Bancolombia")) {
                        double tasaInteres = 0.0084;
                        if (cuotaInt == 0) {
                            double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                            System.out.println("La cuota que pagará para saldar su deuda en el banco sera de: " + String.format("%.2f", amortizacionCuota));
                            String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                            amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                        } else {
                            double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                            System.out.println("Si la máxima cuota que puede pagar es de $" + cuota + " saldará su deuda con el banco en " + String.format("%.2f", amortizacionPeriodos) + " periodos.");
                            String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                            amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                        }
                    } else if (seleccion.equals("Banco de Bogotá")) {
                        double tasaInteres = 0.0195;
                        if (cuotaInt == 0) {
                            double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                            System.out.println("La cuota que pagará para saldar su deuda en el banco sera de: " + String.format("%.2f", amortizacionCuota));
                            String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                            amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                        } else {
                            double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                            System.out.println("Si la máxima cuota que puede pagar es de $" + cuota + " saldará su deuda con el banco en " + String.format("%.2f", amortizacionPeriodos) + " periodos.");
                            String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                            amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                        }
                    } else if (seleccion.equals("Banco de Occidente")) {
                        double tasaInteres = 0.0092;
                        if (cuotaInt == 0) {
                            double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                            System.out.println("La cuota que pagará para saldar su deuda en el banco sera de: " + String.format("%.2f", amortizacionCuota));
                            String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                            amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                        } else {
                            double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                            System.out.println("Si la máxima cuota que puede pagar es de $" + cuota + " saldará su deuda con el banco en " + String.format("%.2f", amortizacionPeriodos) + " periodos.");
                            String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                            amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                        }
                    } else if (seleccion.equals("Banco Popular")) {
                        if (creditoDouble >= 25000000) {
                            double tasaInteres = 0.012;
                            if (cuotaInt == 0) {
                                double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                                System.out.println("La cuota que pagará para saldar su deuda en el banco sera de: " + String.format("%.2f", amortizacionCuota));
                                String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                                amortizacion.setText("La cuota que deberá pagar será de $" + resultado);

                                while (0 < periodosInt) {
                                }
                            } else {
                                double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                                System.out.println("Si la máxima cuota que puede pagar es de $" + cuota + " saldará su deuda con el banco en " + String.format("%.2f", amortizacionPeriodos) + " periodos.");
                                String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                                amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                            }
                        } else if (creditoDouble > 10000000 && creditoDouble < 25000000) {
                            double tasaInteres = 0.015;
                            if (cuotaInt == 0) {
                                double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                                System.out.println("La cuota que pagará para saldar su deuda en el banco sera de: " + String.format("%.2f", amortizacionCuota));
                                String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                                amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                            } else {
                                double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                                System.out.println("Si la máxima cuota que puede pagar es de $" + cuota + " saldará su deuda con el banco en " + String.format("%.2f", amortizacionPeriodos) + " periodos.");
                                String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                                amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                            }
                        } else {
                            double tasaInteres = 0.017;
                            if (cuotaInt == 0) {
                                double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                                System.out.println("La cuota que pagará para saldar su deuda en el banco sera de: " + String.format("%.2f", amortizacionCuota));
                                String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                                amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                            } else {
                                double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                                System.out.println("Si la máxima cuota que puede pagar es de $" + cuota + " saldará su deuda con el banco en " + String.format("%.2f", amortizacionPeriodos) + " periodos.");
                                String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                                amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                            }
                        }
                    } else if (seleccion.equals("Banco Davivienda")) {
                        double tasaInteres = 0.0228;
                        if (cuotaInt == 0) {
                            double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                            System.out.println("La cuota que pagará para saldar su deuda en el banco sera de: " + String.format("%.2f", amortizacionCuota));
                            String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                            amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                        } else {
                            double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                            System.out.println("Si la máxima cuota que puede pagar es de $" + cuota + " saldará su deuda con el banco en " + String.format("%.2f", amortizacionPeriodos) + " periodos.");
                            String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                            amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                        }
                    } else if (seleccion.equals("Banco BBVA")) {
                        double tasaInteres = 0.0085;
                        if (cuotaInt == 0) {
                            double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasaInteres, periodosInt)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodosInt) - 1);
                            String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                            amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                        } else {
                            double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasaInteres))) / Math.log(1 + tasaInteres);
                            String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                            amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                        }
                    } else if (seleccion.equals("Otro")) {
                        String str_tasaInteres = tasaInteres.getText().toString();

                        if (TextUtils.isEmpty(str_tasaInteres)) {
                            Toast.makeText(getActivity(), "Ingrese el valor de la tasa de interés", Toast.LENGTH_LONG).show();
                        }

                        else {
                            Double tasaInteresDouble = Double.parseDouble(str_tasaInteres);
                            Double tasa = (Math.pow(1 + (tasaInteresDouble / 100), 0.0833333)) - 1;
                            if (cuotaInt == 0) {
                                double amortizacionCuota = creditoDouble * ((Math.pow(1 + tasa, periodosInt)) * tasa) / (Math.pow(1 + tasa, periodosInt) - 1);
                                String resultado = String.valueOf(String.format("%.2f", amortizacionCuota));
                                amortizacion.setText("La cuota que deberá pagar será de $" + resultado);
                            } else {
                                double amortizacionPeriodos = Math.log(cuotaInt / (cuotaInt - (creditoDouble * tasa))) / Math.log(1 + tasa);
                                String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
                                amortizacion.setText("Pagando cuotas de $" + cuotaInt + " saldará su deuda en " + resultado + " periodos");
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_amortizacion, container, false);
    }
}