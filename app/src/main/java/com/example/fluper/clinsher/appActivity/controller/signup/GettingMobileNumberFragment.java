package com.example.fluper.clinsher.appActivity.controller.signup;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;
import com.example.fluper.clinsher.R;
import com.example.fluper.clinsher.appActivity.controller.model.User;
import com.example.fluper.clinsher.appActivity.controller.retrofit.APiClient;
import com.example.fluper.clinsher.appActivity.controller.retrofit.ApiInterface;
import com.example.fluper.clinsher.appActivity.controller.retrofit.ServerResponse;
import java.io.IOException;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class GettingMobileNumberFragment extends Fragment {

    private String countryName;
    private String countryPhoneCode;
    private String countryCode;
    private EditText etChooseCountryCode;
    private EditText etCountryCode;
    private  View view;
    private Button btnContinue;
    private  String accessToken;
    private EditText mobileNumber;
    private SignUpActivity signUpActivity;
    private String otp;


    public GettingMobileNumberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach (context);
        signUpActivity = (SignUpActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate (R.layout.fragment_getting_mobile_number, container,
                false);
        gettingId ();
        settingDataOnLayout ();

        return view;
    }

    //getting all id
    public void gettingId(){

        etChooseCountryCode = view.findViewById (R.id.et_country_code_text);
        etCountryCode = view.findViewById (R.id.et_country_code);
        btnContinue  = view.findViewById (R.id.btn_continoue_getting_mobile);
        mobileNumber = view.findViewById (R.id.et_mobile_number);
    }

    //setDataOn Layout
    public void settingDataOnLayout(){

        //getting log in Access token from shared preferences

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences
                ("logInAccessToken", Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString("acessToken","data not found");
        // Toast.makeText (signUpActivity, "Access Token"+accessToken, Toast.LENGTH_SHORT).show ();


        etChooseCountryCode.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                getCountryCode ();
            }
        });

        btnContinue.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {


                updateUserMobile (accessToken, mobileNumber.getText ().toString ().trim (),
                        countryPhoneCode);

            }
        });
    }



    public void getCountryCode() {
        final CountryPicker picker = CountryPicker.newInstance ("Select Country");
        picker.setListener (new CountryPickerListener () {
            @Override
            public void onSelectCountry(String name, String code) {
                // Log.i ("phone code", GetCountryPhoneCode (code));
                countryPhoneCode = "+" + GetCountryPhoneCode (code);
                etCountryCode.setText (countryPhoneCode);
                Locale locale = new Locale ("",""+countryCode);
                countryName = locale.getDisplayCountry ();
                etChooseCountryCode.setText (countryName);
                // country_btn.setText(country_code);
                picker.dismiss ();
            }
        });
        picker.show (getActivity ().getSupportFragmentManager (), "COUNTRY_PICKER");
    }


    String GetCountryPhoneCode(String CountryID) {
        String CountryZipCode = "";
        //Log.e("CountryID", "=" + CountryID);
        String[] rl = this.getResources ().getStringArray (R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split (",");
            if (g[1].trim ().equals (CountryID.trim ())) {
                countryCode = g[1];
                CountryZipCode = g[0];
                return CountryZipCode;
            }
        }
        return "";
    }



    //Retrofit (API hit)
    private void updateUserMobile(String accessToken, String mobileNumber,String mobileCode ) {

        ApiInterface apiService = APiClient.getClient().create(ApiInterface.class);

        Call<ServerResponse> call = apiService.updateUserMobile (accessToken,mobileNumber,mobileCode);

        call.enqueue(new Callback<ServerResponse> () {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext (), "your number is Valid ",
                            Toast.LENGTH_SHORT).show();
                    ServerResponse serverResponse = response.body();
                    User user = serverResponse.user;
                    otp = user.getOtp ();
                    Log.d ("test","otp = "+otp);
                    if(otp != null) {
                        signUpActivity.onBtnClick (R.id.fragment_container_signup,
                                new MObileOtpFragment (), otp);
                    }else
                        Toast.makeText (signUpActivity, "otp not found",
                                Toast.LENGTH_SHORT).show ();



                }else{
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("test", "Error : " + errorMessage);
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("test","error "+t.getMessage());
                t.printStackTrace();
            }
        });

    }


}




