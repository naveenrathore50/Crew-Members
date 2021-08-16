package com.example.firebaseapp.Profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseapp.MainActivity;
import com.example.firebaseapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    StorageReference storageReference;


    String storagePath="Users_Profile_Cover_Images/";
    TextView nameTV,emailTV,phoneTV;
    ImageView ProfileIV,CoverIV;
    FloatingActionButton fab;
    ProgressDialog pd;
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int PICK_IMAGE_CAMERA_CODE=300;
    private static final int PICK_IMAGE_GALLERY_CODE=400;
    String cameraPermissions[];
    String storagePermissions[];
    String profileorCoverPhoto;
    Uri image_uri;
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        nameTV=view.findViewById(R.id.nameTV);
        emailTV=view.findViewById(R.id.emailTV);
        phoneTV=view.findViewById(R.id.phoneTV);
        ProfileIV=view.findViewById(R.id.ProfileIV);
        CoverIV=view.findViewById(R.id.coverIV);
        fab=view.findViewById(R.id.fab);
        pd=new ProgressDialog(getActivity());
        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Query query=databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                  for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                      String name=""+dataSnapshot.child("Name").getValue();
                      String email=""+dataSnapshot.child("Email").getValue();
                      String Phone=""+dataSnapshot.child("Phone").getValue();
                      String Image=""+dataSnapshot.child("Image").getValue();
                      String Cover=""+dataSnapshot.child("Cover").getValue();
                      nameTV.setText(name);
                      emailTV.setText(email);
                      phoneTV.setText(Phone);
                      try {

                          Picasso.get().load(Image).into(ProfileIV);

                      }
                      catch (Exception e){
                          Picasso.get().load(R.drawable.ic_default_image_white).into(ProfileIV);
                      } 
                      try {

                          Picasso.get().load(Cover).into(CoverIV);

                      }
                      catch (Exception e){
                       //   Picasso.get().load(R.drawable.ic_add_image).into(ProfileIV);
                      }
                  }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    Boolean cameraAccepted=grantResults[0]==(PackageManager.PERMISSION_GRANTED);
                    Boolean writestorageAccepted=grantResults[1]==(PackageManager.PERMISSION_GRANTED);
                    if(cameraAccepted && writestorageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(getActivity(), "Please Enable Camera and Storage Permissions ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){

                    Boolean writestorageAccepted=grantResults[1]==(PackageManager.PERMISSION_GRANTED);
                    if(writestorageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(getActivity(), "Please Enable Storage Permissions ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /*private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                       if(checkcamerapermissions()){
                           pickFromCamera();
                       }
                       else if(checkstoragepermissions()) {
                           pickFromGallery();
                       }
                }
                else {
                    Toast.makeText(getActivity(), "Please Enable Required Permissions to perform required Action", Toast.LENGTH_SHORT).show();
                }
            });*/
    private Boolean checkstoragepermissions(){
        Boolean result= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requeststoragePermissions(){
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
     //   requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    private Boolean checkcamerapermissions(){
        Boolean result= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                ==(PackageManager.PERMISSION_GRANTED);
        Boolean resultl= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        return result && resultl;
    }
    private void requestcameraPermissions(){
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
       //requestPermissionLauncher.launch(Manifest.permission.CAMERA);
       // requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    private void showEditProfileDialog() {
        String options[]={"Edit Profile Picture","Edit Cover Photo","Edit Name","Edit Phone No."};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                   pd.setMessage("Updating Profile Picture");
                   profileorCoverPhoto="Image";
                   showImagePicDialog();
                }
                else if(which==1){
                    pd.setMessage("Updating Cover Picture");
                    profileorCoverPhoto="Cover";
                    showImagePicDialog();
                }
                else if(which==2){
                    pd.setMessage("Updating Name");
                    showphoneNameDialog("Name");
                }
                else if(which==3){
                    pd.setMessage("Updating Phone No.");
                    showphoneNameDialog("Phone");
                }
            }
        });
        builder.create().show();

    }

    private void showphoneNameDialog(String key) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Update "+key);
        LinearLayout linearLayout =new LinearLayout(getActivity());
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        EditText editText=new EditText(getActivity());
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value=editText.getText().toString().trim();
                      if(!TextUtils.isEmpty(value)) {
                          pd.show();

                          HashMap<String, Object> result = new HashMap<>();
                          result.put(key, value);
                          databaseReference.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  pd.dismiss();
                                  Toast.makeText(getActivity(), key + "Updated", Toast.LENGTH_SHORT).show();
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  pd.dismiss();
                                  Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                              }
                          });
                      }
                      else{
                          Toast.makeText(getActivity(), "Please Enter "+key, Toast.LENGTH_SHORT).show();
                      }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showImagePicDialog() {
        String options[]={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                       if(!checkcamerapermissions()){
                           requestcameraPermissions();
                       }
                       else{
                           pickFromCamera();
                       }
                }
                else if(which==1){
                        if(!checkstoragepermissions()){
                            requeststoragePermissions();
                        }
                        else{
                            pickFromGallery();
                        }
                }

            }
        });
        builder.create().show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
         if(resultCode == RESULT_OK){
              if(requestCode==PICK_IMAGE_GALLERY_CODE){
                   image_uri=data.getData();
                   updateprofilecoverPhoto(image_uri);
              }
              if(requestCode==PICK_IMAGE_CAMERA_CODE){
                 updateprofilecoverPhoto(image_uri);
              }
         }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateprofilecoverPhoto(Uri uri) {
          pd.show();
          String filepathandName=storagePath+""+profileorCoverPhoto+"_"+user.getUid();
          StorageReference storageReference2=storageReference.child(filepathandName);
          storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  Task<Uri>  uriTask=taskSnapshot.getStorage().getDownloadUrl();
                  while (!uriTask.isSuccessful());
                  Uri downloduri=uriTask.getResult();
                  if(uriTask.isSuccessful()){
                      HashMap<String,Object> results=new HashMap<>();
                      results.put(profileorCoverPhoto,downloduri.toString());
                      databaseReference.child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {
                              pd.dismiss();
                              Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_SHORT).show();
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull  Exception e) {
                              pd.dismiss();
                              Toast.makeText(getActivity(), "Error Occured on updating image....."+e.getMessage(), Toast.LENGTH_SHORT).show();
                          }
                      });
                  }
                  else{
                      pd.dismiss();
                      Toast.makeText(getActivity(), "Some error Occured", Toast.LENGTH_SHORT).show();
                  }
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                          pd.dismiss();
                  Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
              }
          });
    }

    private void pickFromCamera() {
        ContentValues Values=new ContentValues();
        Values.put(MediaStore.Images.Media.TITLE,"Temp pic");
        Values.put(MediaStore.Images.Media.DESCRIPTION,"Temp pic");
        image_uri=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,Values);
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,PICK_IMAGE_CAMERA_CODE);
    }
    private void pickFromGallery(){
          Intent galleyIntent =new Intent(Intent.ACTION_PICK);
          galleyIntent.setType("image/*");
          startActivityForResult(galleyIntent,PICK_IMAGE_GALLERY_CODE);

    }
    private  void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }
        else{
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
         super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.mlogout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}