package com.thirumathikart.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.AddressModel;
import com.thirumathikart.customer.models.AddressRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddAddressActivity extends AppCompatActivity {

    public static String KEY_ADDRESS = "address";

    @BindView(R.id.add_address_toolbar)
    Toolbar toolbar;

    @BindView(R.id.address_name_layout)
    TextInputLayout nameLayout;

    @BindView(R.id.address_name_value)
    TextInputEditText nameEt;

    @BindView(R.id.address_number_layout)
    TextInputLayout numberLayout;

    @BindView(R.id.address_number_value)
    TextInputEditText numberEt;

    @BindView(R.id.address_line1_layout)
    TextInputLayout line1Layout;

    @BindView(R.id.address_line1_value)
    TextInputEditText line1Et;

    @BindView(R.id.address_line2_layout)
    TextInputLayout line2Layout;

    @BindView(R.id.address_line2_value)
    TextInputEditText line2Et;

    @BindView(R.id.address_landmark_layout)
    TextInputLayout landmarkLayout;

    @BindView(R.id.address_landmark_value)
    TextInputEditText landmarkEt;

    @BindView(R.id.address_district_layout)
    TextInputLayout districtLayout;

    @BindView(R.id.address_district_value)
    TextInputEditText districtEt;

    @BindView(R.id.address_state_layout)
    TextInputLayout stateLayout;

    @BindView(R.id.address_state_value)
    TextInputEditText stateEt;

    @BindView(R.id.address_pincode_layout)
    TextInputLayout pincodeLayout;

    @BindView(R.id.address_pincode_value)
    TextInputEditText pincodeEt;

    @BindView(R.id.new_address_btn)
    Button addressBtn;

    @BindView(R.id.new_address_container)
    LinearLayout container;

    String name,number,line1,line2,landmark,district,state;
    int userId,pincode=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        userId = intent.getIntExtra(AddressSelectionActivity.KEY_CUSTOMER_ID,0);

        addressBtn.setOnClickListener(v -> {
            captureInput();
            if(validate()){
                addAddress();
            }
        });

    }

    void addAddress(){
        addressBtn.setEnabled(false);
        addressBtn.setText("Adding..");
        AddressRequest address = new AddressRequest();
        address.setName(name);
        address.setContact(number);
        address.setLineOne(line1);
        address.setLineTwo(line2);
        address.setLandmark(landmark);
        address.setDistrict(district);
        address.setState(state);
        address.setPincode(pincode);
        address.setCustomer(userId);
        Call<AddressModel> call = ApiUtil.getService().addAddress(address);

        call.enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                if(response.isSuccessful()){
                    AddressModel address = response.body();
                    Intent data = new Intent();
                    data.putExtra(KEY_ADDRESS,address);
                    setResult(RESULT_OK,data);
                    finish();
                }else {
                    addressBtn.setEnabled(true);
                    addressBtn.setText("Add Address");
                    Snackbar.make(container,"Please Check the details",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {
                addressBtn.setEnabled(true);
                addressBtn.setText("Add Address");
                Snackbar snackbar = Snackbar.make(container,"Error adding Address",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addAddress();
                            }
                        });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                snackbar.show();
            }
        });
    }

    void captureInput(){
        name = nameEt.getText().toString().trim();
        number = numberEt.getText().toString().trim();
        line1 = line1Et.getText().toString().trim();
        line2 = line2Et.getText().toString().trim();
        landmark = landmarkEt.getText().toString().trim();
        district = districtEt.getText().toString().trim();
        state = stateEt.getText().toString().trim();
        if(!pincodeEt.getText().toString().equals("")) {
            pincode = Integer.parseInt(pincodeEt.getText().toString());
        }
    }
    boolean validate(){
        boolean isValid = true;
        if(name==null || name.length()==0){
            isValid = false;
            nameLayout.setError("Name is required");
        }else {
            nameLayout.setError("");
        }

        if(number==null || number.length()==0){
            isValid = false;
            numberLayout.setError("Number is required");
        }else {
            numberLayout.setError("");
        }

        if(line1==null || line1.length()==0){
            isValid = false;
            line1Layout.setError("Line1 is required");
        }else {
            line1Layout.setError("");
        }

        if(line2==null || line2.length()==0){
            isValid = false;
            line2Layout.setError("Line2 is required");
        }else {
            line2Layout.setError("");
        }

        if(landmark==null || landmark.length()==0){
            isValid = false;
            landmarkLayout.setError("Landmark is required");
        }else {
            landmarkLayout.setError("");
        }

        if(district==null || district.length()==0){
            isValid = false;
            districtLayout.setError("District is required");
        }else {
            districtLayout.setError("");
        }

        if(state==null || state.length()==0){
            isValid = false;
            stateLayout.setError("State is required");
        }else {
            stateLayout.setError("");
        }

        if(pincode==0){
            isValid = false;
            pincodeLayout.setError("Pincode is required");
        }else {
            pincodeLayout.setError("");
        }

        return isValid;
    }
}
