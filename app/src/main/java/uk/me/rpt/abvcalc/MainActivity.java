package uk.me.rpt.abvcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Get gravity value from a field
    // Check it's valid and convert to a standard value
    private long getGravity( int field)
    {
        long gravity;

        try
        {
            // Get the field as a decimal
            EditText editText = (EditText) findViewById( field );
            Double dg = Double.parseDouble(editText.getText().toString());

            // Check the possible ranges and convert to a standard number
            if( dg > 0.9 && dg < 2 )
            {
                gravity = Math.round(1000 * dg);
            }
            else if( dg > 900 && dg < 2000 )
            {
                gravity = Math.round(dg);
            }
            else if( dg >= 2 && dg < 200 )
            {
                gravity = Math.round(1000 + dg);
            }
            else
            {
                gravity = 0;
            }

            if( gravity != 0 )
            {
                String text = String.format("%d", gravity);
                editText.setText( text );
            }
        }
        catch ( NumberFormatException e )
        {
            gravity = 0;
        }

        return gravity;
    }

    private void hideSoftKeyBoard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText())
        { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void onCalcABV(View view)
    {
        String result = "";

        long og = getGravity(R.id.editText_og);
        long fg = getGravity(R.id.editText_fg);

        if( og == 0 )
        {
            result += getString(R.string.invalid_og) + "\n";
        }
        if( fg == 0 )
        {
            result += getString(R.string.invalid_fg) + "\n";
        }

        if( og !=0 && fg != 0)
        {
            result = String.format(getString(R.string.final_result), og, fg, CalculateResult( og, fg ) );
        }

        TextView textView = findViewById(R.id.textView_result);
        textView.setText( result );
    }

    /* Check the gravity is in a sensible range */
    private boolean ValidGravity( long gravity )
    {
        return gravity >= 1000 && gravity <= 1200;
    }

    private String CalculateResult( long og, long fg )
    {
        /* String form of result */
        String result;

        /* Gravities as points above 1.000 */
        int og_points, fg_points;

        int apparent_attenuation = 0;

        /* Check that the og and fg are sensible */
        if( !ValidGravity(og))
        {
            result = getString(R.string.invalid_og);
        }
        else if( !ValidGravity(fg))
        {
            result = getString(R.string.invalid_fg);
        }
        else if( fg > og )
        {
            result = getString(R.string.fg_higher_og);
        }
        else
        {
            /* Convert the gravities into points */
            og_points = (int) og - 1000;
            fg_points = (int) fg - 1000;

            /* Find the amount of attenuation */
            int attenuation = (int) og_points - (int) fg_points;

            /* Calculate the apparent attenuation as a percentage */
            apparent_attenuation = 1000 * attenuation / og_points;

            /* Calculate the ABV using the HMRC formula */
            /* First need to calculate the factor f */
            double f;
            if( attenuation < 7 )
            {
                f = 0.125;
            }
            else if ( attenuation > 7 && attenuation <= 10 )
            {
                f = 0.126;
            }
            else if ( attenuation > 10 && attenuation <= 17 )
            {
                f = 0.127;
            }
            else if ( attenuation > 17 && attenuation <= 26 )
            {
                f = 0.128;
            }
            else if ( attenuation > 26 && attenuation <= 36 )
            {
                f = 0.129;
            }
            else if ( attenuation > 36 && attenuation <= 46 )
            {
                f = 0.130;
            }
            else if ( attenuation > 46 && attenuation <= 57 )
            {
                f = 0.131;
            }
            else if ( attenuation > 57 && attenuation <= 67 )
            {
                f = 0.132;
            }
            else if ( attenuation > 67 && attenuation <= 78 )
            {
                f = 0.133;
            }
            else if ( attenuation > 78 && attenuation <= 89 )
            {
                f = 0.134;
            }
            else if ( attenuation > 89 && attenuation <= 100 )
            {
                f = 0.135;
            }
            else
            {
                /* Out of range */
                f = 0;
            }

            if( f == 0 )
            {
                result = getString(R.string.attenuation_too_great);
            }
            else
            {
                /* Calculate the alcohol by volume as a percentage */
                float abv = (og_points - fg_points) * (float) f;
                result = String.format(getString(R.string.result_string), abv, apparent_attenuation/10, apparent_attenuation % 10 );
            }
        }

        return result;
    }

    public void onCorrect(View view)
    {
        String result = "";

        long gravity = getGravity(R.id.editText_gravity);
        Double temp = 0.0;
        Double calib = 0.0;

        hideSoftKeyBoard();

        try
        {
            // Get the temperatures as decimals
            EditText editText_temp = (EditText) findViewById( R.id.editText_temp );
            temp = Double.parseDouble(editText_temp.getText().toString());
            EditText editText_calib = (EditText) findViewById( R.id.editText_calib );
            calib = Double.parseDouble(editText_calib.getText().toString());
        }
        catch ( NumberFormatException e )
        {
            gravity = 0;
        }


        if( gravity == 0 )
        {
            result += getString(R.string.invalid_input) + "\n";
        }
        else
        {
            Double densitySample = 1-(temp+288.9414)/(508929.2*(temp+68.12963))*(temp-3.9863)*(temp-3.9863);
            Double densityCalib = 1-(calib+288.9414)/(508929.2*(calib+68.12963))*(calib-3.9863)*(calib-3.9863);

            gravity += 1000*(densityCalib/densitySample-1);

            result = "Corrected gravity " + gravity;
        }

        TextView textView = findViewById(R.id.textView_correct);
        textView.setText( result );
    }

}
