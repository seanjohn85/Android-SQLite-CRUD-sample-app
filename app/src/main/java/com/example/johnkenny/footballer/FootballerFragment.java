package com.example.johnkenny.footballer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.johnkenny.footballer.model.FootballerModel;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.johnkenny.footballer.helperClass.imageConvertor.getBytes;
import static com.example.johnkenny.footballer.helperClass.imageConvertor.getImage;

/**
 * Created by johnkenny on 09/11/2016.
 */

public class FootballerFragment extends Fragment {

    public static final String FB_ID = "com.example.johnkenny.footballer.FOOTBALLER_ID";

    //display widgets
    private Footballer mFootballer;
    private EditText mTitleField;
    private EditText mNumberField;
    private RadioGroup mPositon;
    private DatePicker mPicker;
    private Spinner mTeam;
    private Button btn;
    private CheckBox mCurrentlyPlaying;
    private Context thisConn;
    private Button mphoto;
    private RadioButton radioButton;
    private ImageView imageView;

    //used for the date
    private String mday, mmonth, myear, mDate = "21/05/1990";
    private int year = 1990, month = 5, day = 21;


    //teams array used for the spinner drop down option
    private String teams[] = {"Arsenal", "Chelsea", "Everton", "Leeds United", "Manchester United", "Manchester City"};
    //used for the image
    private byte[] bytes;
    private static final int RESULT_LOAD_IMAGE = 1;

    //on create is called from an activity to create this

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //used to set this activity using the FOOTBALLER_ID click4d from the list (edit only)
        String FOOTBALLER_ID = getActivity().getIntent().getStringExtra(FB_ID);
        //gets footballer clicked on in previous screen
        mFootballer = FootballerModel.get(getActivity()).getFootballer(FOOTBALLER_ID);
    }//close onCreate

    //this is where the view is edited when it loads
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        //uses the fragment_footballer xml file for the layout
        View view = inflater.inflate(R.layout.fragment_footballer, parent, false);

        //event for when the text is edited
        mTitleField = (EditText) view.findViewById(R.id.footballer_name);

        //event for when the number is edited
        mNumberField = (EditText) view.findViewById(R.id.footballer_number);

        //a variable assigned to the radio buttons so they can be edited or the selected value assigned to a footballer object
        mPositon = (RadioGroup) view.findViewById(R.id.position);

        //get the spinner item
        mTeam = (Spinner) view.findViewById(R.id.teams);

        //checkbox to see if the fooballer is still actively playing
        mCurrentlyPlaying = (CheckBox) view.findViewById(R.id.currently_playing);

        //gets the date picker
        mPicker = (DatePicker) view.findViewById(R.id.dob);

        //gets the imageview
        imageView = (ImageView) view.findViewById(R.id.imageView);

        //but used for create on create screen or delete on the edit screen
        btn = (Button) view.findViewById(R.id.btn);
        //array adapter
        teamSelector();

        //used to set date, contains all date methods
        dateSetter();

        /*
        log files used for testing
        Log.d("number", String.valueOf(mFootballer.getNumber()));
        Log.d("team", String.valueOf(mFootballer.getTeam()));
        Log.d("playing", String.valueOf(mFootballer.getPlaying()));
      */

        //gets the upload photo button
        mphoto = (Button) view.findViewById(R.id.upload_photo);
        //sets the text on the button
        mphoto.setText("Upload Photo");
        //event listener for the upload photo button
        mphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opens the gallery for the user to pick an image and loads the onActivityResult method
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        /*
         this is use to switch between edit and create mode
         in create mode the mFootballer object will be null
         */
        if (mFootballer == null) {
            //sets the button text to create
            btn.setText("Create");
            btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    //methods to create a new footballer
                    createNew();
                }
            });
        } //close if
        else {
            //adds the edit metods as this is edit mode
            editMehods();
            //sets the button text to delete
            btn.setText("Delete");
            //button event presss listener
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thisConn = getContext();
                    //uses the delete method from the footballer model and deletes from the db
                    FootballerModel footballerModel = FootballerModel.get(getActivity());
                    int deleted = footballerModel.deleteFootballer(mFootballer.getmId());
                    //moves back to the list fragment after item is deleted
                    //Log.d("del", String.valueOf(deleted));
                    //if the item is deleted
                    if (deleted == 1) {
                        //returns to the list view
                        startActivity(new Intent(thisConn, FootballerListActivity.class));
                    }//close if
                }
            });

        } //end else

        return view;
    } //close onCreateView

    /*
    method used to det the date, set the values of the date and update the database
     */
    private void dateSetter() {
        //if this is in edit mode the footballer will have a dob
        if (mFootballer != null) {
            //// sets the mDate object to the footballer objects dob
            mDate = mFootballer.getDob();

            /*log tests
            Log.d("day", mDate.substring(0, 2));
            Log.d("mm", mDate.substring(3, 5));
            Log.d("yyyy", mDate.substring(6, 10));*/

            //sets the day, month year used to update the date picker
            day = Integer.parseInt(mDate.substring(0, 2));

            month = Integer.parseInt(mDate.substring(3, 5)) - 1;

            year = Integer.parseInt(mDate.substring(6, 10));

        }

        /*
        sets the picker date using the default date or in it mode using the foobaalers dob(set above)
        event listener for changes to the date
         */
        mPicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //sets the myear to the changed date
                myear = String.valueOf(mPicker.getYear());
                //apends 0 the day string if less than len and sets mday
                if (mPicker.getDayOfMonth() < 10) {
                    mday = "0" + mPicker.getDayOfMonth();
                }
                //sets mday to day on the spinner if greater than 10
                else {
                    mday = String.valueOf(mPicker.getDayOfMonth());
                }
                //apends 0 the month string if less than 10 and sets mmonth
                if (mPicker.getMonth() < 10) {
                    mmonth = "0" + (mPicker.getMonth() + 1);
                }
                //sets mmonth to month on the spinner if greater than 10
                else {
                    mmonth = String.valueOf(mPicker.getMonth());
                }

                //sets mDate to the changed date
                mDate = mday + "/" + mmonth + "/" + myear;

                //checks if the mFootballer has an id (screen in edit mode)
                if (mFootballer != null) {
                    //updates the mFootballer objects dob
                    mFootballer.setDob(mDate);
                    //updates the database
                    FootballerModel footballerModel = FootballerModel.get(getActivity());
                    footballerModel.updateFootballer(mFootballer);
                }

            }
        });//close event listener

    } //close date setter

    /*
    this is used to add a new footballer to the database
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createNew() {

        //ensures no feilds are left blank
        if (mTitleField.getText().toString().matches("")) {
            //error message if the user name is blank
            Toast.makeText(getActivity(), "Please enter the footballer's name.", Toast.LENGTH_SHORT)
                    .show();
        } else if (mTitleField.getText().toString().matches("")) {
            //error message if the footballer no is blank
            Toast.makeText(getActivity(), "Please enter the footballer's number.", Toast.LENGTH_SHORT)
                    .show();
        } else if (mPositon.getCheckedRadioButtonId() == -1) {
            //error message if a positon is selected
            Toast.makeText(getActivity(), "Please select the footballer's postion.", Toast.LENGTH_SHORT)
                    .show();
        } else if (bytes == null) {
            //error message if a positon is selected
            Toast.makeText(getActivity(), "Please updoad the footballer's photo.", Toast.LENGTH_SHORT)
                    .show();
        } else {
            //creates a new footballer object
            Footballer footballer = new Footballer();
            //get the checked radio button
            int ax = mPositon.getCheckedRadioButtonId();
            //gets the value of the checked radio button
            radioButton = (RadioButton) getView().findViewById(ax);
            //sets the footballers position using the value of the checked radio button
            footballer.setPostion(radioButton.getText().toString());
            //sest the footballers dob using the mdate var
            footballer.setDob(mDate);
            //gets the list of all footballers from the db
            ArrayList<Footballer> mfootballers = FootballerModel.get(getActivity()).getFootballers();
            //increments the amount by one to set the id
            int id = mfootballers.size() + 1;
            //sets this footballers id
            footballer.setmId(String.valueOf(id));
            Log.d("new id", footballer.getmId());
            //set name from the name feild
            footballer.setName(mTitleField.getText().toString());
            //set number from the number feild
            footballer.setNumber(Integer.valueOf(mNumberField.getText().toString()));
            //sets the file location of the players pic
            footballer.setBlobber(bytes);
            //sets the footballers team to match the spinner
            footballer.setTeam(teams[mTeam.getSelectedItemPosition()]);
            //sets the footballers playing status
            if (mCurrentlyPlaying.isChecked()) {
                footballer.setPlaying(1);
            } else {
                footballer.setPlaying(0);
            }

            //create the footballer on the database
            FootballerModel footballerModel = FootballerModel.get(getActivity());
            footballerModel.createFootballer(footballer);
            //returns to list view and the new footballer should be here
            startActivity(new Intent(thisConn, FootballerListActivity.class));

        }//close else

    } //close  createNew()


    //upload image method
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //ensures on image is retrieved from the gallery
        if (data != null) {
            //sets a uri object to the image data
            Uri uri = data.getData();

            //set the image to the image view and convert to a byte array to store as a blob on the db
            try {
                //creates a bitmap of the image using the uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(thisConn.getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                //sets the imageView using the bitmap image
                imageView.setImageBitmap(bitmap);
                /*
                sets the bytes array using the bitmap of the image and the conversion helper class
                 */
                bytes = getBytes(bitmap);

                /*
                used for updating in edit mode only
                checks is the mFootballer excists and the bytes array excists
                 */
                if (bytes != null && mFootballer != null) {

                    //sets the mFootballer bytes array using the newly creates bytes array
                    mFootballer.setBlobber(bytes);
                    //uses the model to update the db with updated footballer object
                    FootballerModel footballerModel = FootballerModel.get(getActivity());
                    footballerModel.updateFootballer(mFootballer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//close if

    }//close onActivityResult


    //methods used in edit mode
    public void editMehods() {
        //update db methods
        updatePosition();
        updateName();
        updateNumber();
        updateStatus();
        updateTeam();

        //sets the image to the image from the database
        if (mFootballer.getBlobber() != null) {
            imageView.setImageBitmap(getImage(mFootballer.getBlobber()));
        } else {
            imageView.setImageResource(R.drawable.pl);
        }

    }//close editMehods

    /*
    this method is used in edit mode
    it sets the name of the footballer using the footballer object created from the db
    the text listener listens for changes and
     */
    public void updateName() {
        //sets the name to the footballer from the database
        mTitleField.setText(mFootballer.getName());
        //listens for changes in the text feild
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //sets the footballers new name
                mFootballer.setName(s.toString());
                //uses the model to update the db
                FootballerModel footballerModel = FootballerModel.get(getActivity());
                footballerModel.updateFootballer(mFootballer);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    } //close updateName

    /*
    sets the plays number to match the db
    updates the footballer object and db
     */
    public void updateNumber() {
        //sets the number to the number from the database
        mNumberField.setText(String.valueOf(mFootballer.getNumber()));

        //method to listen for changes to in the number textfeild and update the db with the changes
        mNumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //this has to be a number on the db so int assigned
                int x;
                //ensures the feild is not trying to update with an empty string as a number
                //as this would cause the app to crash
                if (!s.toString().isEmpty()) {
                    /* converts the chars form the text box to a number
                    note a user can only enter numbers here(int value only fixed by the xml)
                     */
                    x = Integer.valueOf(s.toString());
                    //used for testing
                    Log.d("changed to", String.valueOf(x));
                    //sets the footballer objects number to the user input value
                    mFootballer.setNumber(x);
                    //updates the footballer object on the db using the model singleton class
                    FootballerModel footballerModel = FootballerModel.get(getActivity());
                    footballerModel.updateFootballer(mFootballer);
                }//close if

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    } //close update number

    /*
    sets the radio in edit mode
    method for edit update players position
    */
    public void updatePosition() {

        radioSelector();
        //event listen of the radio buttons (footballer positon)
        mPositon.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //used to get the id of each radio button
                Log.d("radio", String.valueOf(checkedId));
                //sets the postion of the footballer object
               /* old bad testing solution
               if (checkedId == 2131492952) {
                    mFootballer.setPostion("goalkeeper");
                } else if (checkedId == 2131492953) {
                    mFootballer.setPostion("defender");
                } else if (checkedId == 2131492954) {
                    mFootballer.setPostion("midfeild");
                } else if (checkedId == 2131492955) {
                    mFootballer.setPostion("striker");
                }>*/

                radioButton = (RadioButton) getView().findViewById(checkedId);
                mFootballer.setPostion(radioButton.getText().toString());
                //updated the model and database
                FootballerModel footballerModel = FootballerModel.get(getActivity());
                footballerModel.updateFootballer(mFootballer);

            }
        });

    } // close updatePosition()

    //this method sets the radio button to match the data base value
    public void radioSelector() {
        //claches possible sections with  footballers current position
        if (mFootballer.getPostion().equalsIgnoreCase("goalkeeper")) {
            mPositon.check(R.id.goalkeeper);
        } else if (mFootballer.getPostion().equalsIgnoreCase("midfeilder")) {
            mPositon.check(R.id.midfield);
        } else if (mFootballer.getPostion().equalsIgnoreCase("defender")) {
            mPositon.check(R.id.defender);
        } else if (mFootballer.getPostion().equalsIgnoreCase("striker")) {
            mPositon.check(R.id.striker);
            Log.d("pos", mFootballer.getPostion());
        }
    }   //close radioSelector()

    //updated the footballers team
    public void updateTeam() {

        //event listener for change on the spinner and updates the footballers team
        mTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("section changed", String.valueOf(position));
                //sets the footballers team using the teams array and spinner position as the index
                mFootballer.setTeam(teams[position]);
                //updates the db with the new footballer object
                FootballerModel footballerModel = FootballerModel.get(getActivity());
                footballerModel.updateFootballer(mFootballer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } //close updateTeam()


    //this method assigns the items to the spinner at sets the spinner to the value of the footballer object
    public void teamSelector() {

        //gets the context
        thisConn = getContext();
        //array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(thisConn, R.layout.items, teams);
        //applies the adapter to the spinner
        mTeam.setAdapter(adapter);

        //checks if the footballer object has an id(for edit only)
        if (mFootballer != null) {
            //loops through all the teams
            for (int i = 0; i < teams.length; i++) {
                //compare eat iteration of the loop to the footballer objects team
                if (mFootballer.getTeam().equalsIgnoreCase(teams[i])) {
                    //assigns the selection to the fiootballers team
                    mTeam.setSelection(i);
                    //exits the loop
                    break;
                }  //close if

            }   //close for
        } //close if
        //this will be used if the footballer has no team(on the create )
        else {
            //assigns the spinner to the first item in the array
            mTeam.setSelection(0);
        }

    } //end teamSelector

    //update if the player is currently playing
    public void updateStatus() {
        //checks if the mFootballer is cuurently playing (=1)
        if (mFootballer.getPlaying() == 1) {
            //checks the check box
            mCurrentlyPlaying.setChecked(true);
        }
        //event listener for the check box
        mCurrentlyPlaying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if the box is checked set the footbalers value to reflect it
                if (isChecked) {
                    mFootballer.setPlaying(1);
                } else {
                    mFootballer.setPlaying(0);
                }
                //updates the database
                FootballerModel footballerModel = FootballerModel.get(getActivity());
                footballerModel.updateFootballer(mFootballer);
            }
        });
    }//end update status

} //close this class
