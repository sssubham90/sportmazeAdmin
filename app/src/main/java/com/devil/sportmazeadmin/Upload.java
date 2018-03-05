package com.devil.sportmazeadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload extends Fragment {
    private static final int SELECT_VIDEO = 0;
    private static final int SELECT_IMAGE = 1;
    private StorageReference mStorageRef;
    EditText editTextName1;
    Button buttonAdd,videoSelector,imageSelector;
    DatabaseReference myRef;
    FirebaseUser user;
    TextView videoTV,imageTV;
    private String videoPath,imagePath,name;
    private StorageReference videoRef,imageRef;
    private UploadTask uploadTask;
    private Uri selectedUri_Video,selectedUri_Image;
    private ProgressDialog progressVideoDialog,progressImageDialog;
    private String key;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);
        myRef = FirebaseDatabase.getInstance().getReference("Video");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressVideoDialog = new ProgressDialog(getActivity());
        progressVideoDialog.setMessage("Uploading Video:");
        progressVideoDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressVideoDialog.setIndeterminate(false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        progressImageDialog = new ProgressDialog(getActivity());
        progressImageDialog.setMessage("Uploading Image:");
        progressImageDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressImageDialog.setIndeterminate(false);

        editTextName1 = rootView.findViewById(R.id.editText1);
        buttonAdd = rootView.findViewById(R.id.btn1);
        videoSelector = rootView.findViewById(R.id.videoSelector);
        videoTV = rootView.findViewById(R.id.videoPath);
        imageTV = rootView.findViewById(R.id.thumnailPath);
        imageSelector = rootView.findViewById(R.id.thumbnailSelector);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addVideo();
            }
        });
        videoSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "Select a Video"),SELECT_VIDEO);
            }
        });
        imageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_IMAGE);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                selectedUri_Video = data.getData();
                if (selectedUri_Video != null){
                    videoPath = selectedUri_Video.toString();
                    videoTV.setText(videoPath);
                }
            }
            else if (requestCode == SELECT_IMAGE) {
                selectedUri_Image = data.getData();
                if (selectedUri_Image != null){
                    imagePath = selectedUri_Image.toString();
                    imageTV.setText(imagePath);
                }
            }
        }
    }

    private void addVideo(){
        if (validate()){
            key = myRef.push().getKey();
            name = editTextName1.getText().toString().trim();
            videoRef = mStorageRef.child("Videos/"+key+"/video.mp4");
            uploadTask = videoRef.putFile(selectedUri_Video);
            progressVideoDialog.show();
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getActivity(), "Video Upload Failed", Toast.LENGTH_LONG).show();
                    progressVideoDialog.setProgress(0);
                    progressVideoDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.child(key).child("Name").setValue(name);
                    myRef.child(key).child("URL").setValue(videoRef.getPath());
                    progressVideoDialog.dismiss();
                    Toast.makeText(getActivity(), "Video Uploaded Successfully", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressVideoDialog.setProgress((int) progress);
                }
            });
            imageRef = mStorageRef.child("Images/"+key+"/thumbnail.png");
            uploadTask = imageRef.putFile(selectedUri_Image);
            progressImageDialog.show();
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getActivity(), "Image Upload Failed", Toast.LENGTH_LONG).show();
                    progressImageDialog.setProgress(0);
                    progressImageDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.child(key).child("Image URL").setValue(imageRef.getPath());
                    myRef.child(key).child("Admin ID").setValue(user.getEmail());
                    progressImageDialog.dismiss();
                    Toast.makeText(getActivity(), "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressImageDialog.setProgress((int) progress);
                }
            });
        }
    }

    private boolean validate(){
        if(editTextName1.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "You should enter a name for the video", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(videoPath.isEmpty()){
            Toast.makeText(getActivity(), "Please choose a video to upload", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(imagePath.isEmpty()){
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_LONG).show();
            return false;
        }
        else return true;
    }
}
