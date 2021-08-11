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
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tradeapp.R;

import java.text.NumberFormat;

public class AmortizacionFragment extends Fragment {

    Button amortizar;
    Spinner spinner;
    EditText credito, periodos, cuota, tasaInteres;
    TextView amortizacion, valor_amortizacion, valor_credito, pago_total, interes_total;
    FrameLayout resumen;

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
        amortizacion = view.findViewById(R.id.txt_amortizacion);
        valor_amortizacion = view.findViewById(R.id.respuesta_amortizacion);
        valor_credito = view.findViewById(R.id.valor_credito);
        pago_total = view.findViewById(R.id.pago_total);
        interes_total = view.findViewById(R.id.interes_total);
        resumen = view.findViewById(R.id.frame_resumen);
        resumen.setVisibility(View.GONE);

        spinner = view.findViewById(R.id.spinner);

        String[] bancos = {"-", "Bancolombia", "Banco de Bogotá", "Banco de Occidente", "Banco Popular", "Banco Davivienda", "Banco BBVA", "Otra"};
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
        amortizar.setEnabled(true);

        amortizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String creditoString = credito.getText().toString();
                String periodosString = periodos.getText().toString();
                String cuotaString = cuota.getText().toString();
                String seleccion = spinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(creditoString)) {
                    Toast.makeText(getActivity(), "El valor del crédito es un campo requerido", Toast.LENGTH_LONG).show();
                }

                else if (TextUtils.isEmpty(periodosString) && TextUtils.isEmpty(cuotaString)) {
                    Toast.makeText(getActivity(), "Ingrese el número de periodos o el valor de la cuota", Toast.LENGTH_LONG).show();
                }

                else if (TextUtils.isEmpty(cuotaString)) {

                    double creditoDouble = Double.parseDouble(creditoString);
                    int periodosInt = Integer.parseInt(periodosString);

                    if (periodosInt == 0) {
                        periodos.setError("El valor ingresado no es válido");

                    }

                    else {
                        if (seleccion.equals("-")) {
                            Toast.makeText(getActivity(), "Especifique la entidad bancaria", Toast.LENGTH_LONG).show();
                        }
                        else {
                            resumen.setVisibility(View.VISIBLE);
                            if (seleccion.equals("Bancolombia")) {
                                double tasaInteres = 0.0084;
                                AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                            } else if (seleccion.equals("Banco de Bogotá")) {
                                double tasaInteres = 0.0195;
                                AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                            } else if (seleccion.equals("Banco de Occidente")) {
                                double tasaInteres = 0.0092;
                                AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                            } else if (seleccion.equals("Banco Popular")) {
                                if (creditoDouble >= 25000000) {
                                    double tasaInteres = 0.012;
                                    AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                                } else if (creditoDouble > 10000000 && creditoDouble < 25000000) {
                                    double tasaInteres = 0.015;
                                    AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                                } else {
                                    double tasaInteres = 0.017;
                                    AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                                }
                            } else if (seleccion.equals("Banco Davivienda")) {
                                double tasaInteres = 0.0228;
                                AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                            } else if (seleccion.equals("Banco BBVA")) {
                                double tasaInteres = 0.0085;
                                AmortizacionCuota(tasaInteres, creditoDouble, periodosInt);
                            } else if (seleccion.equals("Otra")) {
                                String str_tasaInteres = tasaInteres.getText().toString();

                                if (TextUtils.isEmpty(str_tasaInteres)) {
                                    Toast.makeText(getActivity(), "Ingrese el valor de la tasa de interés", Toast.LENGTH_LONG).show();
                                } else {
                                    Double tasaInteresDouble = Double.parseDouble(str_tasaInteres);
                                    Double tasa = (Math.pow(1 + (tasaInteresDouble / 100), 0.0833333)) - 1;
                                    AmortizacionCuota(tasa, creditoDouble, periodosInt);
                                }
                            }
                        }
                    }
                }

                else if (TextUtils.isEmpty(periodosString)){

                    double creditoDouble = Double.parseDouble(creditoString);
                    double cuotaDouble = Double.parseDouble(cuotaString);

                    if (cuotaDouble == 0) {
                        cuota.setError("Ingrese un valor de cuota válido");
                    }

                    else {
                        if (cuotaDouble > creditoDouble) {
                            Toast.makeText(getActivity(), "Verifique que el valor de la cuota no exceda el monto total del crédito", Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (seleccion.equals("-")) {
                                Toast.makeText(getActivity(), "Especifique la entidad bancaria", Toast.LENGTH_LONG).show();
                            }
                            else {
                                resumen.setVisibility(View.VISIBLE);
                                if (seleccion.equals("Bancolombia")) {
                                    double tasaInteres = 0.0084;
                                    AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                } else if (seleccion.equals("Banco de Bogotá")) {
                                    double tasaInteres = 0.0195;
                                    AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                } else if (seleccion.equals("Banco de Occidente")) {
                                    double tasaInteres = 0.0092;
                                    AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                } else if (seleccion.equals("Banco Popular")) {
                                    if (creditoDouble >= 25000000) {
                                        double tasaInteres = 0.012;
                                        AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                    } else if (creditoDouble > 10000000 && creditoDouble < 25000000) {
                                        double tasaInteres = 0.015;
                                        AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                    } else {
                                        double tasaInteres = 0.017;
                                        AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                    }
                                } else if (seleccion.equals("Banco Davivienda")) {
                                    double tasaInteres = 0.0228;
                                    AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                } else if (seleccion.equals("Banco BBVA")) {
                                    double tasaInteres = 0.0085;
                                    AmortizacionPeriodo(tasaInteres, creditoDouble, cuotaDouble);
                                } else if (seleccion.equals("Otra")) {
                                    String str_tasaInteres = tasaInteres.getText().toString();

                                    if (TextUtils.isEmpty(str_tasaInteres)) {
                                        Toast.makeText(getActivity(), "Ingrese el valor de la tasa de interés", Toast.LENGTH_LONG).show();
                                    } else {
                                        Double tasaInteresDouble = Double.parseDouble(str_tasaInteres);
                                        Double tasa = (Math.pow(1 + (tasaInteresDouble / 100), 0.0833333)) - 1;
                                        AmortizacionPeriodo(tasa, creditoDouble, cuotaDouble);
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getActivity(), "No puede ingresar el número de periodos y el valor de la cuota", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_amortizacion, container, false);
    }

    private void AmortizacionCuota(double tasaInteres, double credito, int periodo) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        valor_credito.setText(String.valueOf(numberFormat.format(credito)));
        double amortizacionCuota = credito * ((Math.pow(1 + tasaInteres, periodo)) * tasaInteres) / (Math.pow(1 + tasaInteres, periodo) - 1);
        String resultado = String.valueOf(numberFormat.format(amortizacionCuota));
        amortizacion.setText("La cuota que deberá pagar será de:");
        valor_amortizacion.setText(resultado);
        double pagoTotal = amortizacionCuota*periodo;
        pago_total.setText(String.valueOf(numberFormat.format(pagoTotal)));
        double interes = pagoTotal - credito;
        interes_total.setText(String.valueOf(numberFormat.format(interes)));
    }

    private void AmortizacionPeriodo(double tasaInteres, double credito, double cuota) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        valor_credito.setText(String.valueOf(numberFormat.format(credito)));
        double amortizacionPeriodos = Math.log(cuota / (cuota - (credito * tasaInteres))) / Math.log(1 + tasaInteres);
        String resultado = String.valueOf(String.format("%.2f", amortizacionPeriodos));
        amortizacion.setText("Pagando cuotas de " + numberFormat.format(cuota) + " saldará su deuda en: ");
        valor_amortizacion.setText(resultado + " periodos");
        double pagoTotal = amortizacionPeriodos*cuota;
        pago_total.setText(String.valueOf(numberFormat.format(pagoTotal)));
        double interes = pagoTotal - credito;
        interes_total.setText(String.valueOf(numberFormat.format(interes)));
    }
}