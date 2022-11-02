package com.app.cacomplex.vaz;


import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class vistaPick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_pick);
    }
}
    class IJMath {

    static double A = 0.05857895078654250866288;
    static double B = -0.00626245895772819579;
    static double C = -0.00299946450696036814;
    static double D = 0.289389696496082416;
    static double E = 0.0539962589851632982;
    static double F = 0.00508516909930653109;
    static double G = 0.000215969713046142876;
    static double H = -0.000225663858340491571;
    static double I = -3.06833213472529049e-7;

      public static double erf(double x) {
        double x2 = sqr(x);
        if (x2 < 1e-8)
            return (2/Math.sqrt(Math.PI))*x*(1+x2*(-1./3.+x2*(1./10.)));
        double erf = x2 > 36 ? 1 :
                Math.sqrt(1 - Math.exp(-sqr(x*((2/Math.sqrt(Math.PI) - A) + A *
                        (1 + x2*x2*(B + x2*(C + x2*(H + x2*I)))) /
                        (1 + x2*(D + x2*(E + x2*(F + x2*G))))
                ))));
        return x>0 ? erf : -erf;
    }

    static double sqr(double x) {
        return x*x;
    }

}