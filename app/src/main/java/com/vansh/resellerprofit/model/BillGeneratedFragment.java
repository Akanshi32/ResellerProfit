package com.vansh.resellerprofit.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vansh.resellerprofit.R;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BillGeneratedFragment extends Fragment {
    private View mRootView;
    private EditText mSubjectEditText, mBodyEditText;
    Button mSaveButton;
    private static final String LOG_TAG = "LOG_TAG";


    public BillGeneratedFragment() {
        // Required empty public constructor
    }

    public static BillGeneratedFragment newInstance(){
        BillGeneratedFragment fragment = new BillGeneratedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_bill_generated, container, false);
        mSubjectEditText = (EditText) mRootView.findViewById(R.id.edit_text_subject);
        mBodyEditText = (EditText) mRootView.findViewById(R.id.edit_text_body);
        mSaveButton = (Button) mRootView.findViewById(R.id.button_save);
        return mRootView;

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubjectEditText.getText().toString().isEmpty()) {
                    mSubjectEditText.setError("Subject is empty");
                    mSubjectEditText.requestFocus();
                    return;
                }

                if (mBodyEditText.getText().toString().isEmpty()) {
                    mBodyEditText.setError("Body is empty");
                    mBodyEditText.requestFocus();
                    return;
                }

                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void createPdf() throws FileNotFoundException {

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(myFile);

        //Step 1
       Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
        document.add(new Paragraph(mSubjectEditText.getText().toString()));
        document.add(new Paragraph(mBodyEditText.getText().toString()));

        //Step 5: Close the document
        document.close();

    }

    private void viewPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void emailNote() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, mSubjectEditText.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT, mBodyEditText.getText().toString());
        Uri uri = Uri.parse(myFile.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        startActivity(email);
    }

    private void promptForNextAction() {
        final String[] options = {getString(R.string.label_email), getString(R.string.label_preview),
                getString(R.string.label_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Note Saved, What Next?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(getString(R.string.label_email))) {
                    emailNote();
                } else if (options[which].equals(getString(R.string.label_preview))) {
                    viewPdf();
                } else if (options[which].equals(getString(R.string.label_cancel))) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

}

