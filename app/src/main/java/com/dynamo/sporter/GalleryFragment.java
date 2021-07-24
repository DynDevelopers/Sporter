package com.dynamo.sporter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dynamo.sporter.adapter.GalleryViewAdapter;
import com.dynamo.sporter.util.Utility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements MenuItem.OnMenuItemClickListener {
    List<Task<Uri>> imageURLs;
    RecyclerView recyclerView;
    private Context context;
    private Toolbar toolbar;
    private String folderID;
    private FirebaseStorage firebaseStorage;
    private View view;
    private TextView noImagesTextView;
    private Uri uri;
    public GalleryFragment(String folderID) {
        this.folderID = folderID;
    }
    RelativeLayout selectedImageLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sptr_gallery_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        toolbar = (Toolbar) view.findViewById(R.id.sptr_gallery_tool_bar);
        noImagesTextView = (TextView) view.findViewById(R.id.sptr_no_images);

        firebaseStorage = FirebaseStorage.getInstance();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        GalleryViewAdapter adapter = new GalleryViewAdapter(imageURLs, context);

        listImages();
        Button upload = ((Button) view.findViewById(R.id.sptr_upload_image));
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    Log.i("Upload Image", "URI is null");
                    return;
                }
                Log.i("Upload Image", "Uploading...");
                try {
                    upload(uri);
                } catch (Exception e) {
                    Log.i("Error", e.getMessage());
                }

            }
        });
        selectedImageLayout = ((RelativeLayout) view.findViewById(R.id.selected_image_layout));

        ((Button) view.findViewById(R.id.sptr_cancel_upload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImageLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.inflateMenu(R.menu.add_menu);
        toolbar.getMenu().findItem(R.id.sptr_add).setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.sptr_add) {
            openFileSelector();
        }
        return true;
    }

    private void openFileSelector() {
        Intent imageChooser = new Intent();
        imageChooser.setType("image/*");
        imageChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imageChooser, "Select image"),
                1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult", "Inside onActivityResult");
        if (resultCode == RESULT_OK && requestCode == 1) {
            Log.i("onActivityResult", "First If Passed");
            assert data != null;
            if (data.getData() != null) {
                Log.i("onActivityResult", "Second If Passed");
                this.uri = data.getData();
                selectedImageLayout.setVisibility(View.VISIBLE);

                ((ImageView) view.findViewById(R.id.sptr_selected_image)).setImageURI(this.uri);
            } else {
                Log.i("Upload User Image", "No Image Selected");
            }
        }
    }

    private void upload(Uri uri) throws IOException {
        StorageReference reference = firebaseStorage.getReference();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(((MainActivity) context).getContentResolver(), uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        reference.child(folderID.trim()).child(Utility.getTimestamp()).putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                selectedImageLayout.setVisibility(View.GONE);
                listImages();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Upload Failed", e.getMessage());
            }
        });
    }

    private void listImages() {
        noImagesTextView.setVisibility(View.GONE);
        StorageReference reference = firebaseStorage.getReference();
        imageURLs = new ArrayList();
        reference.child(folderID.trim()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference ref : listResult.getItems()) {
                    imageURLs.add(ref.getDownloadUrl());
                }
                if (imageURLs.size() > 0)
                    recyclerView.setAdapter(new GalleryViewAdapter(imageURLs, context));
                else
                    noImagesTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}